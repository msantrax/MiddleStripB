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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class ASVPDevice implements SerialDevice.SerialDeviceListener{

    
    private static final Logger log = Logger.getLogger(ASVPDevice.class.getName());
     
    
    private static ASVPDevice instance; 
    public static ASVPDevice getInstance(){
        if (instance == null) {instance = new ASVPDevice();}
        return instance;
    }
    
    
    public ASVPDevice() {
//        devstate = DEVICE_STATE.DISCONNECTED; 
    }
    
    
    private Controller ctrl = Controller.getInstance();  
    public void setAppController (Controller ctrl){
        this.ctrl = ctrl;
        
    }
    
    private FX1Controller anct;
    public void setFXController (FX1Controller anct){
        this.anct = anct;
        initProperties();
    }

    public FX1Controller getAnct() {
        return anct;
    }
   
   
    private ASVPDeviceController asvpdevctrl;  
    public void setASVPDevController (ASVPDeviceController ctrl){
        this.asvpdevctrl = ctrl;
        
    }
    
    private Context ctx = Context.getInstance(); 
    
    
    // ================================================ ISOTHERM SERVICES ====================================================
    
    private Isothermv isotherm;
    private IsothermBean isobean;
    
    public Isothermv getIsotherm() {
        return isotherm;
    }

    public void setIsotherm(Isothermv isotherm) {
        this.isotherm = isotherm;
        this.isobean = isotherm.getIsothermBean();  
    }
    
    
    
    

    // ============================================== PROPERTIES & BINDING SUPPORT ==============================================
    
    
    private LinkedHashMap<String, Double[]> datacalibs;
    
    
    private void updateDataProp(String name, Long value){
        
        Double[] calib = datacalibs.get(name);
        Double calc = calib[0] + (calib[1] * value) + (calib[2] * (value * value));
       
        ctx.current_anatask.addSample(System.currentTimeMillis(), calc);
        
        
    }
    
    
    
    private Double[] getPropCalib (String name){
        
        double setraA0 = 0.0;
        double setraA1 = 0.02;
        double setraA2 = 0.0;
 
        if (name == "setra"){
            Double setracal[] = {setraA0, setraA1, setraA2};
            return setracal;
        } 
        
        return null;
    }
    
    
    private void initProperties(){
        
        
        datacalibs = new LinkedHashMap<>();
        
        
        datacalibs.put("setra", getPropCalib ("setra"));
        ctx.setAsvpdev(this);
        
        
        ctx.anatasks.put("nulltask", new BaseAnaTask(this, ctx));
        ctx.anatasks.put("checkp0task", new CheckP0AnaTask(this, ctx));
        ctx.current_anatask = ctx.anatasks.get("nulltask");
        
        
    }
    
    
    // ================================================ PLUMBING ================================================================
    
    /**
     * Bargraph update wrapper
     * @param val
     * @param diff
     * @param variance 
     */
    public void sendtoBarGraphs (Double val, Double diff, Double variance){
        
        asvpdevctrl.updateBarGraphs(val, diff, variance);
    }
    
    
    
    
    
    @smstate (state = "TASKIDLE")
    public boolean st_taskIdle(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        log.info(String.format("Task Idle"));
        
        return true;
    }
    
    
    @smstate (state = "RESETMEAS")
    public boolean st_resetMeas(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        ctx.current_anatask.resetMeasurements();
        
        log.info(String.format("Reseting measurements"));
        
        return true;
    }
    
    @smstate (state = "ENDMEAS")
    public boolean st_endMeas(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        log.info(String.format("Ending  Measurements"));
        
        return true;
    }
    
    
    @smstate (state = "MONITOR")
    public boolean st_doMonitor(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        log.info(String.format("Monitoring"));
        
        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "SETAUTO", this.getClass(),
                                       new VirnaPayload()
                                        ));
        
        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "NOTIFYAUX", this.getClass(),
                                       new VirnaPayload()
                                        ));
        
        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "ENDMEAS", this.getClass(),
                                       new VirnaPayload()
                                        ));
        
        return true;
    }
    
    
    @smstate (state = "SETAUTO")
    public boolean st_setAutomation(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.current_taskstate;
            log.info(String.format("Seting Automation to : %s", tst.getStatecmd()));
            
            SMTraffic nxt = tsk.goNext(tst.getImediate());
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
        }
        else{
            String cmd = pld.vstring;
            
            
            log.info(String.format("Set Automation - RAW Mode : %s", cmd));
        }
        
        
        
        return true;
    }
    
    @smstate (state = "NOTIFYAUX")
    public boolean st_notifyAux(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.current_taskstate;
            log.info(String.format("Notifying aux with : %s", tst.getStatecmd()));
            
            SMTraffic nxt = tsk.goNext(tst.getImediate());
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
        }
        else{
            log.info(String.format("Notify Aux"));
        }
       
        return true;
    }
    
    
    
    
    // ================================================ SERIAL INTERFACE =========================================================
    
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
                //log.info(String.format("Connect is checking port : %s", portname));
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
//        devstate = DEVICE_STATE.NOPORTS;
        
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
//            devstate = DEVICE_STATE.PORT_OPEN;
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
            dterm = ByteArrayConverter.convertByteBuffer(e.buffer);
            //String dterm = new String (e.buffer, "UTF-8");
            log.info("PicnoTerm : " + dterm);
        }
        else if (e.eventType == SerialDevice.Event.EVENT_CMDR){
    
        }
        else if (e.eventType == SerialDevice.Event.EVENT_BEACON){
            
            
            byte[] arr1 = Arrays.copyOfRange(e.buffer, 22, 30);
            Long data1 = ByteArrayConverter.toLong(arr1, true);
            updateDataProp("setra", data1);
            
            byte[] arr2 = Arrays.copyOfRange(e.buffer, 30, 38);
            Long data2 = ByteArrayConverter.toLong(arr2, true);
    
    
        }
        
        else if (e.eventType == SerialDevice.Event.EVENT_TICK){
             
            byte[] arr1 = Arrays.copyOfRange(e.buffer, 0, 8);
            Long data1 = ByteArrayConverter.toLong(arr1, true);
            if (data1 != -1){
                updateDataProp("setra", data1);
            }
            
            
            if (e.buffer[8] != 0){
                byte[] arr2 = Arrays.copyOfRange(e.buffer, 8, e.getLenght()- 2);
                String cmds = ByteArrayConverter.convertByteBuffer(arr2);
                log.info("Tick Cmds= " + cmds);
                String[] tokens = cmds.split(":");
                String cmd = tokens[0];
                if (ctrl.hasState(cmd)){
                    log.info("Recognized cmd is " + cmd);
                    ctrl.processSignal(new SMTraffic(0l, 0l, 0, cmd, this.getClass(),
                                   new VirnaPayload().setString(cmds)));
                }
                else{
                    log.info("Unrecognized cmd on line ->  " + cmd);
                }
            }
            
        }
        
        else{
            log.info("Undetermined event...");
        
        }
    }
    
    
    
    
    
    
    
}


