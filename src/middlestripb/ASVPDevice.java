/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.fazecast.jSerialComm.SerialPort;
import com.opus.syssupport.Config;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import isothermview.Isothermv;
import isothermview.IsothermBean;
import isothermview.IsothermPoint;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.reactfx.util.Timer;

/**
 *
 * @author opus
 */
public class ASVPDevice implements SerialDevice.SerialDeviceListener{

    
    private static final Logger log = Logger.getLogger(ASVPDevice.class.getName());
   
    
    
    
     
    private Timer timer;
    private long timer_tick = 100L;
   
    
    // Interface variables
    
    
    
    
    
    
    
    
    
    //private TimeStringConverter tsc;
    public static final String timestamp_format = "%1$tM:%1$tS:%1$tL";
    public static final String seconds_clock_format = "%1$tS:%1$tL";
    
    
    private static ASVPDevice instance; 
    public static ASVPDevice getInstance(){
        if (instance == null) {instance = new ASVPDevice();}
        return instance;
    }
    
    
    public ASVPDevice() {
        devstate = DEVICE_STATE.DISCONNECTED; 
//        defaultport = Config.getInstance().getAsvpport();
 
    }
    

    
    private Controller ctrl = Controller.getInstance();  
    public void setAppController (Controller ctrl){
        this.ctrl = ctrl;
        
    }
    
    private FX1Controller anct;
    public void setFXController (FX1Controller anct){
        this.anct = anct;
    }
   
   
    
    public ObservableList<XYChart.Data<Number, Number>> getIsoPoints(boolean adsorption, boolean ppo, boolean volg) {
      
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        ArrayList<IsothermPoint>isopoints = isobean.points;
        
        for (IsothermPoint isopoint : isopoints){
            if (adsorption == isopoint.isAdsorption()) {
                Double pressure = ppo ? isopoint.getPpo() : isopoint.getPo();
                Double volume = volg ? isopoint.getVolume_g() : isopoint.getVolume();
                XYChart.Data<Number, Number> d = new XYChart.Data<>(pressure, volume);
                d.setExtraValue(isopoint);
                data.add(d);
            }
        }
        
        return FXCollections.observableArrayList(data);
    }

    
 
    
    private static ArrayList<String> observed_ports = new ArrayList<>();
    private static Boolean available = false;
    
    private static String defaultport = "ttyUSB0";
    private static Integer defaulbaud = 115200;
    
    private SerialDevice serialdevice;
    private static int timeout;
    
    
    public void connect(){
  
//        log.info(String.format("Enter connect ..."));    
        try{
            SerialPort[] comports = SerialPort.getCommPorts();
            for (SerialPort sp : comports){
                String portname = sp.getSystemPortName();
                log.info(String.format("Connect is checking port : %s", portname));
                if (portname.equals(defaultport)){
                    activatePort(sp);
                    return;
                }
                observed_ports.add(portname);
            }
        }
        catch (Exception ex){
            log.info(String.format("Falha na conexão...."));
        } 
 
        log.info(String.format("No ports available"));
        timeout = Config.getInstance().getKeepalive_period();
        devstate = DEVICE_STATE.NOPORTS;
        
    }
    
    
    private void activatePort(SerialPort sp){
        
        sp.setComPortParameters(defaulbaud, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        sp.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        //sport.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        
        serialdevice = new SerialDevice(sp, ctrl);
        
        if (serialdevice.isConnected()){
            serialdevice.enablePort();
            serialdevice.addSerialDeviceListener(this);
            observed_ports.add(sp.getSystemPortName());
            devstate = DEVICE_STATE.PORT_OPEN;
            timeout = 1;
        }
        else {
            log.info(String.format("Unable"));
        }
    }
    
    
    
    public void disconnect(){
        
        String id = serialdevice.getSport().getSystemPortName();
        observed_ports.remove(id);
     
        serialdevice.closePort();
        
    }
    
    
 
    @Override
    public void eventoSerial(SerialDevice.Event e) {
 
        boolean single = false;
        String dterm;
        String dcmd = "";
        String load = "";
        
        if (e.eventType == SerialDevice.Event.EVENT_TERM){
            dterm = convertByteBuffer(e.buffer);
            //String dterm = new String (e.buffer, "UTF-8");
            log.info("PicnoTerm : " + dterm);
            if (dterm.contains("ACP")){
                ctrl.processSignal(new SMTraffic(0l, 0l, 0, "GOTRESET", this.getClass(), 
                        new VirnaPayload()
                        .setString(load)
                ));
            }
        }
        else if (e.eventType == SerialDevice.Event.EVENT_CMDR){
    
        }
        else if (e.eventType == SerialDevice.Event.EVENT_BEACON){
            

            byte[] bf = e.buffer;
            byte[] arr1 = Arrays.copyOfRange(bf, 22, 30);
            Long data1 = ByteArrayConverter.toLong(arr1, true);
            
            byte[] arr2 = Arrays.copyOfRange(bf, 30, 38);
            Long data2 = ByteArrayConverter.toLong(arr2, true);
            
            log.info("Beacon -> ");
        }
        else{
            log.info("Undetermined event...");
        
        }
        
    }
    
    
    private String convertByteBuffer(byte [] buf){
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == 0x00) break;
            sb.append((char) buf[i]);
        }
        return sb.toString();
        
    }
    
    static public enum endian {BIG,LITTLE};
    
    
    /**
     * Calcula valor em bytes de um array
     * @param buf
     *  Array onde estão os bytes a calcular 
     * @param index
     *  Inicio dos dados
     * * @param endian
     *  Ordem dos bytes no array
     * @return 
     *  Integer com o campo calculado
     */
    static public int getUnsignedInt (byte[] buf, int index, endian end){
        
        int result=0;
        
        int msb = (end==endian.BIG) ? buf[index] : buf[index+1] ;
        result = (msb<0) ? (256-Math.abs(msb))*256 : msb * 256;
        int lsb = (end==endian.BIG) ? buf[index+1] : buf[index];
        result += (lsb<0) ? (256-Math.abs(lsb)) : lsb;
        
        return result;
    }
    
    
    
    
    //================================================= COMMS STATES ==============================================
    
    public static DEVICE_STATE devstate; 
    private ArrayDeque <DEVICE_STATE>states_stack = new ArrayDeque<>();;
    
    
    public static enum DEVICE_STATE { 
            DISCONNECTED,
            NOPORTS,
            PORT_OPEN,
            GETID,
            WAITID,
            GETTEMP,
            WAITTEMP,
            CONNECTED,
            GETMICRO,
            WAITMICRO,
            
            ANALISING,
            SIMULATING,
            
            
            DONE
    };
    
   
    
    public Boolean simulating_temp = false;
    public Long lastconnected_temp = System.currentTimeMillis();
    public SimpleStringProperty tempresult  = new SimpleStringProperty("");
    
    
   
    public Boolean pd_attached = true;
    @smstate (state = "BLAINEBEAT")
    public boolean st_doBlaineBeat(SMTraffic smm){
        
        //log.info(String.format("Blainebeat Triggered..."));
        return true;
    }    

    @smstate (state = "GOTINFO")
    public boolean st_gotInfo(SMTraffic smm){
        
//        enableBlaineIcon (true, "white");
//        devstate = DEVICE_STATE.GETTEMP;
        return true;
    }
    
    @smstate (state = "GOTRESET")
    public boolean st_gotReset(SMTraffic smm){
        
//        enableBlaineIcon (true, "red");
//        enableTempIcon (true, "red");
//        enableMicroIcon (true, "red");
//        
//        serialdevice.writeString("i");
//        devstate = DEVICE_STATE.WAITID;
        
        return true;
    }
    
    

    //================================================= RUN STATES ==============================================
    
    protected Boolean running = false;
    protected Integer run_number = 0;
    private Integer max_runs = 2;
    protected boolean runsdone = false;
    private Random rand = new Random();
    
    @smstate (state = "RESETASVP")
    public boolean st_resetASVP(SMTraffic smm){
        
        return true;
    }
    
    
    @smstate (state = "EMERGENCY")
    public boolean st_Emergency(SMTraffic smm){
        log.info(String.format("EMERGENCY Happened ++++++++  +++++++++++   ++++++++++ +++++++++ ++++++++++++++"));
    
       
        return true;
    }

    
    private Isothermv isotherm;
    private IsothermBean isobean;
    
    public Isothermv getIsotherm() {
        return isotherm;
    }

    public void setIsotherm(Isothermv isotherm) {
        this.isotherm = isotherm;
        this.isobean = isotherm.getIsothermBean();
        
    }
    
    
    
    

    
}


