/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;


import cern.extjfx.chart.XYChartPane;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.SignalListener;
import com.opus.syssupport.StateDescriptor;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.VirnaServiceProvider;
import com.opus.syssupport.smstate;
import isothermview.Isotherm;
import isothermview.IsothermChart;
import isothermview.QcrImporter;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;



public class Controller implements SignalListener, VirnaServiceProvider, PropertyChangeListener {

    private static final Logger log = Logger.getLogger(Controller.class.getName());
    
    private Controller.SMThread service_thread;     
    private LinkedBlockingQueue<SMTraffic> smqueue;
    private LinkedHashMap<String, StateDescriptor> statesptr ;
    private final ScheduledExecutorService scheduler;

    private FX1Controller anct;
    private boolean fx1open = false;
    
    private boolean locked = false;
    
    public static final Color RPT_COLOR = new Color(0, 50 , 130);
    
    private static Controller instance; 
    public static Controller getInstance(){
        if (instance == null) {instance = new Controller();}
        return instance;
    }

    
    public Controller() {
        
        smqueue = new LinkedBlockingQueue<>();
        statesptr = new LinkedHashMap<>();
        
        alarms = new LinkedHashMap<>();
        scheduler = Executors.newScheduledThreadPool(5); 
        
        addSignalListener(this);
//        sys_services = Lookup.getDefault().lookup(SystemServicesProvider.class).getDefault();
        
        log.setLevel(Level.FINE);
        instance = this;
    }

//    public DBService getDbservice() {
//        return dbservice;
//    }
   
    
    
    // ================================================================ALARMS  =====================================================
    private LinkedHashMap<Integer, AlarmHandle> alarms;
    private static Integer alarmid = 1;
    
    public static Integer getAlarmID(){
        return alarmid++;
    }
    
    public boolean hasAlarmSet(Long uid){
        
        for (AlarmHandle al : alarms.values()){
            if (al.handleid == uid) return true; 
        }
        return false;
    }
    
    public void setAlarm (final Long addr, Integer id, final SMTraffic message, long init, long period){
        
        if (alarms.containsKey(id)){
            log.warning("Trying to set an already loaded alarm");
            return;
        }
        
        log.fine(String.format("Setting alarm to %d with id = %d", addr, id));
        
        final Runnable alarm = new Runnable() {
            
            public void run() {
                Controller al_ctrl = Controller.getInstance();
                al_ctrl.notifySignalListeners(addr, message);             
                log.finest(String.format("Alarm to addr %d : %s to %d",
                        addr,
                        message.getState(),
                        message.getHandle()
                )) ; 
            }
        };
        
        final ScheduledFuture<?> alarmhandle = scheduler.scheduleAtFixedRate(alarm, init, period, TimeUnit.MILLISECONDS); 
        
        alarms.put(id, new AlarmHandle(addr, 0l, alarmhandle));
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    private class AlarmHandle{
      
        public Long handleid;
        public Long context;
        
        public int type = 0;
        
        public ScheduledFuture<?> handle;

        public AlarmHandle(Long handleid, Long context, ScheduledFuture<?> handle) {
            this.handleid = handleid;
            this.context = context;
            this.handle = handle;
        }
        
    };
    
    public void removeAlarm(Integer id){
        
        log.info(String.format("Removing alarm id = %d", id));
        AlarmHandle handle = alarms.get(id); 
        
        if (handle != null){
            handle.handle.cancel(true);
            alarms.remove(id);
        }
    }
    
    public void isAlarmSet (Long uid){
   
    }
    
    
    // ===========SIGNAL HANDLING ===================================================================
        
    /** Estrutura para armazenamento dos listeners do dispositivo*/ 
    //private transient LinkedHashMap<Long,SignalListener> listeners = new LinkedHashMap<>();
    
    private transient ArrayList<SignalListener> listeners = new ArrayList<>();
    
    @Override
    public Long getContext() {
        return -1L;
    }
    
    @Override
    public Long getUID() {
        return -1L;
    }
    
    @Override
    public void processSignal (SMTraffic signal){
        smqueue.offer(signal);
    }
    
     /** Método de registro do listener do dispositivo serial
     * @param l */
    public void addSignalListener (SignalListener l){
        listeners.add(l);
    }

    /** Método de remoção do registro do listener do dispositivo serial
     * @param l */
    public void removeSignalListener (SignalListener l){
        listeners.remove(l);
    }

    /** Esse método é chamado quando algo acontece no dispositivo
     * @param uid_addr
     * @param signal */
    protected void notifySignalListeners(long uid_addr, SMTraffic signal) {

        if (!listeners.isEmpty()){      
            //log.fine("Notifying "+ uid_addr);
            for (SignalListener sl : listeners){
                if (sl.getUID() == uid_addr){
                    sl.processSignal(signal);
                }
            }
        }
    }
    
    
    // ===================================================== Controllers ===================================================
 
    
    public void setFXANController (FX1Controller controller){
        this.anct = controller;   
    }
    public boolean isFX1open() { return fx1open;}
    public void setFX1open (boolean open) { fx1open = open;}


    public LinkedBlockingQueue<SMTraffic> getQueue(){ return smqueue;}
    
    
    // ======================================== STATE MACHINE ======================================================================
    @Override
    public void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    @Override
    public void startService(){      
        smqueue = new LinkedBlockingQueue<>() ;
        
        if (service_thread != null && service_thread.isAlive()){
            log.info("The Controller thread is active, no need to wakeup her");
            return;
        }
        
        service_thread = new Controller.SMThread(smqueue, statesptr, this);
        
        
        Class<?> c; 
        smstate annot;
        StateDescriptor stdesc;
 
        c = this.getClass();     
        for (Method mt : c.getDeclaredMethods() ){
            annot = mt.getAnnotation(smstate.class);
            if (annot != null){
                stdesc =   new StateDescriptor().setClazz(c)
                                                .setInstance(this)
                                                .setMethod(mt)
                                                .setSID(annot.state());
                if (statesptr.get(annot.state()) != null){
                    log.info(String.format("State name colision :  %s @ %s", stdesc.getSID(), c.getName()));
                    System.exit(1);
                }
                else{
                    statesptr.put(annot.state(), stdesc);
                    //log.info(String.format("Registering state %s @ %s", stdesc.getSID(), stdesc.getClazz().getName()));
                }
            }
        }
        new Thread(service_thread).start();
    }
    
    
    
    public void loadStates(Class c, Object instance){
        
        smstate annot;
        StateDescriptor stdesc;
        
        
        // Loading FX1
        //c = FX1SMachine.getInstance().getClass();
        
        for (Method mt : c.getDeclaredMethods()){
            annot = mt.getAnnotation(smstate.class);
            if (annot != null){
                stdesc =   new StateDescriptor().setClazz(c)
                                                .setInstance(instance)
                                                .setMethod(mt)
                                                .setSID(annot.state());
                if (statesptr.get(annot.state()) != null){
                    log.info(String.format("State name colision :  %s @ %s", stdesc.getSID(), c.getName()));
                    System.exit(1);
                }
                else{
                    statesptr.put(annot.state(), stdesc);
                    //log.info(String.format("Registering state %s @ %s", stdesc.getSID(), stdesc.getClazz().getName()));
                }
            }
        }
    }
    
    
    
    
    private class SMThread extends Thread {
    
        
        private String state;
        private ArrayDeque <String>states_stack;
        private LinkedHashMap<String, StateDescriptor> statesptr;
        
        private boolean done;
        protected BlockingQueue<SMTraffic> tqueue;
        private String cmd;
        
        private Controller parent;
        private SMTraffic smm;
        private VirnaPayload payload;
        
        private Method m;
        StateDescriptor stdesc;
      
        protected long start_tick =  System.currentTimeMillis();
 

        public SMThread(BlockingQueue<SMTraffic> tqueue, LinkedHashMap<String, StateDescriptor> _statesptr, Controller parent ) {
            
            this.parent = parent;
            this.tqueue = tqueue;
            states_stack = new ArrayDeque<>();
            this.statesptr = _statesptr;
            
            states_stack.push("RESET");
            setDone(false);
        }
        
        
        public boolean idleHook (){
            
            smm = tqueue.poll();                         
            if (smm != null){
                cmd = smm.getCommand();
                if (cmd.equals("LOADSTATE")){
                    //state = smm.getState();
                    pushState(smm.getState(), smm);
                }
            }
            else{
                try {
                    //System.out.print('.');
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    //Exceptions.printStackTrace(ex);
                }
            }   
            
            return true;
        }

       
        public void pushState (String SID, SMTraffic payload){
            
            if (statesptr.get(SID) == null) return;
            
            StateDescriptor sd = statesptr.get(SID);
            if (sd != null){
                if (payload !=null){
                    sd.setContext(payload);
                }
            }
            //new SMTraffic( 0l, 0l, 0, SID, new VirnaPayload())
            states_stack.push(SID);
            
        }
        
        @Override
        public void run(){
   
            //log.log(Level.FINE, "Iniciando Thread de Serviços principal");
            states_stack.clear();
            states_stack.push("RESET");
            setDone(false);
           
            try {
                while (!done){
                    
                    if (states_stack.isEmpty()){
                       state = "IDLE"; 
                    }
                    else{
                       state = states_stack.pop();
                    }
                    
                    if (state.equals("IDLE")){
                        idleHook();
                    }
                    else{
                        stdesc = statesptr.get(state);
                        if (state != null){
                            m = stdesc.getMethod();
                            smm = stdesc.getContext();
                            if (!state.equals("HOUSEKEEP") && !state.equals("BLAINEBEAT")){
                                log.log(Level.INFO, String.format("Activating state %s @ %d", state, System.currentTimeMillis()-start_tick));
                            }
                            Boolean ret = (Boolean)m.invoke(stdesc.getInstance(), smm);
                            if (!ret){
                                log.log(Level.WARNING, String.format("State: %s failed @ %d", state, System.currentTimeMillis()-start_tick));
                            }
                        }
                    } 
                }
            } catch (Exception ex) {
                log.log(Level.SEVERE,String.format("Controller State Machine failed ..."));
                //log.log(Level.SEVERE,String.format("Controller State Machine failed with %s @ state %s", ex.getMessage(), state));
                ex.printStackTrace();
                System.exit(0);
                //startService();
                
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "Controller is Stopping Service");
            this.done = done;
        }
            
    };
    
    
    
    // =========================================================================================================================
    // ========================================   STATES  ======================================================================
    // =========================================================================================================================
    
    
    
    
    
    
    @smstate (state = "NULLEVENT")
    public boolean st_nullEvent(SMTraffic smm){
        log.info(String.format("NULLEVENT was called"));
        return true;
    }
   
    @smstate (state = "RESET")
    public boolean st_reset(SMTraffic smm){
        
        //log.log(Level.INFO, String.format("RESET state activated with payload : %s", smm.getPayload().vstring));
        service_thread.states_stack.push("IDLE");
        service_thread.states_stack.push("CONFIG");
        service_thread.states_stack.push("INIT");
        return true;
    }
    
    @smstate (state = "INIT")
    public boolean st_init(SMTraffic smm){
        log.log(Level.INFO, String.format("INIT state activated with payload : %s", smm.getPayload().vstring));
        return true;
    }
    
    @smstate (state = "CONFIG")
    public boolean st_config(SMTraffic smm){
      
        // Para que isso mesmo ?
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        
        SMTraffic alarm_config = new SMTraffic(0l, 0l, 0, "HOUSEKEEP", this.getClass(),
                        new VirnaPayload()
                );
        setAlarm (-1l, -1, alarm_config, 250l, 250l);
        

        
        
        return true;
    }
    
    
    
    
    private int housekeep_loop = 0;
//    private PriorityQueue<StatusMessage> statusmessages = new PriorityQueue<>();
//    private StatusMessage currentstatus;
    
    @smstate (state = "HOUSEKEEP")
    public boolean st_doHousekeep(SMTraffic smm){

        VirnaPayload payload = smm.getPayload();
        
//        log.info(String.format("Housekeep loop %03d @ %d", housekeep_loop++, ((System.currentTimeMillis() % 1000000))));
        
        if (isotherm != null && isotherm.chart_ready){
            isotherm.transferPoint();
        }
        
        return true;
        
    }
    
    
    @smstate (state = "UPDATESTATUS")
    public boolean st_updateStatus(SMTraffic smm){

        VirnaPayload payload = smm.getPayload();
//        StatusMessage sm = (StatusMessage)payload.vobject; 
//        log.info(String.format("Status = [%d]%s", sm.getPriority(), sm.getMessage()));
//        statusmessages.add(sm);
        
        return true;
    }
    
    
    @smstate (state = "ADD_NOTIFICATION")
    public boolean st_addNotification(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        String mes = payload.vstring;
        
        String[] tokens = mes.split("&");
 
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//               FXFWindowManager wm = FXFWindowManager.getInstance();
//               wm.addNotification(tokens[0], tokens[1], tokens[2], tokens[3]);
            }
        });
        return true;
    }
    

    @smstate (state = "REGISTERNOTIFICATION")
    public boolean st_registerNotification(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        String[] messages = payload.vstring.split("&");

        return true;
    }
    
    @smstate (state = "SHOWOOPS")
    public boolean st_showOops(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        VirnaServiceProvider vsp = (VirnaServiceProvider)payload.getCaller();
        
        return true;
    }
    
    
    public Isotherm isotherm ;
    public IsothermChart isothermchart ;
    
    
    @smstate (state = "IMPORTISOTHERM")
    public boolean st_import_isotherm(SMTraffic smm){
        
        Object caller;
        VirnaPayload pload = smm.getPayload();
        QcrImporter qcrimporter = QcrImporter.getInstance();
        
        if (pload != null){
            caller = pload.vobject;
           
            try {
                if (qcrimporter.isQcrBinary(pload.vstring)){
                    qcrimporter.loadBinaryFile(pload.vstring);
                }
                else if (qcrimporter.isQcrText(pload.vstring)){
                    qcrimporter.loadTextFile();
                    qcrimporter.showStatus();
                    isotherm = qcrimporter.getIsotherm();                    
                    log.info(String.format("Done importing isotherm from  %s ", pload.vstring));
                }
                else{
                    log.info(String.format("Importing some other file %s ", pload.vstring));
                    return false;
                }
            } catch (IOException ex) {
                log.info(String.format("Failed to load Isotherm file %s ", pload.vstring));
                return false;
            }    
        }
        return true;
    }
    
    
    @smstate (state = "LOADISOTHERMCHART")
    public boolean st_loadIsoThermChart(SMTraffic smm){
        
        log.info(String.format("Loading Chart  %s ", "TESTE"));
        
        if (isotherm != null){
            anct.loadMainChart(isotherm);
//            isothermchart = new IsothermChart(isotherm);
//            XYChartPane chartpane = isothermchart.createCernChart();
//            anct.loadMainChart(chartpane);
        }
        
        
        
        return true;
    }

    
    
    
    
}


