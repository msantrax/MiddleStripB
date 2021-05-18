/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.google.gson.reflect.TypeToken;
import com.opus.syssupport.PicnoUtils;
import com.opus.syssupport.SMEvent;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;

/**
 *
 * @author opus
 */
public class BaseAnaTask {

    private static final Logger log = Logger.getLogger(BaseAnaTask.class.getName());
    
    public String taskid = "basetask";
    
    protected ASVPDevice asvpdev;
    protected Context ctx;
    public AuxChart auxchart;
    
    public SampleRing samplering;
    
    protected Object dataframe;
    
 
    public LinkedHashMap<Long, String> events;
    public Long start_ts = 0L;

    
    public Double valueover = -1.0;
    public Double valueunder = -1.0;
    public boolean diffvalue = false;
    public String valuetask = "";

    public Double current_dts;
    public Double current_value;
    public Double current_diff;

    
    protected LinkedHashMap<String,TaskState> taskstates;
    protected TaskState current_taskstate;
    protected VarPool varpool;
    
    public Boolean accept = false;
    
    
    public String lockedstate = "";
    
    public String scriptsrootpath = "";
    public String currentrealm = "";
    
    
    public BaseAnaTask(ASVPDevice asvpdev, Context ctx) {
        this.asvpdev = asvpdev;
        this.ctx = ctx;
        
        events = new LinkedHashMap<>();
        samplering = new SampleRing(5);
        
    }
    
    public void prepareGo(){
     
    }
    
    public void Restart(){
     
    }
    
    public void Go(){    
        resetMeasurements();
    }
    
    
    
    public void loadScript(String spath){
        
        String fpath = scriptsrootpath+"/"+spath;
        Path path = Paths.get(fpath);
        Type stMapType = new TypeToken<LinkedHashMap<String, TaskState>>() {}.getType();
        LinkedHashMap<String,TaskState> tempstates = new LinkedHashMap<>();
        String scriptrealm;
        
        try {
            tempstates = PicnoUtils.loadJsonTT(path.toString(), stMapType);
            scriptrealm=tempstates.get("scriptinit").getRealm();
            removeRealm(scriptrealm);
            
            tempstates.forEach((k, v) -> {
                taskstates.put(v.getRealm() + "_" + k, v);
            });
            currentrealm = scriptrealm;
            
            SMTraffic nxt = goNext("scriptinit");
            
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
            
            
        }catch(IOException e) {
            log.info(String.format("Exception when loading states @ %s is : %s" , taskid, e.getCause().getMessage()));
        }
        
    }
    
    
    public void removeRealm(String realm){
    
        Controller ctrl = Controller.getInstance();
        
        ArrayList<String> states = new ArrayList<>();
        taskstates.forEach((k, v) -> {
            if (v.getRealm().equalsIgnoreCase(realm)){
                states.add(k);
                ctrl.removeSMEventListener(k, this);
            }
        });
        taskstates.keySet().removeAll(states);
        
        
    }
    
    public void initStates(String rootpath){
        
        taskstates = new LinkedHashMap<>();
        initVarPool();
        
        scriptsrootpath = rootpath;
        
//        Path path = Paths.get(ASVPDevice.JSONS + taskid + "/");
//        String pathbup = ASVPDevice.JSONS + taskid + "bup/";
        
        Path path = Paths.get(rootpath);
        String pathbup = rootpath + "bup/";


        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            Type stMapType = new TypeToken<LinkedHashMap<String, TaskState>>() {}.getType();
            LinkedHashMap tempstates = new LinkedHashMap<>();
            for (Path file : ds) {
                if (file.toFile().isDirectory() || !file.toString().contains(".json")) continue;
                log.info(String.format("======= Task %s is loading file : %s" , taskid, file.toString()));
                tempstates = PicnoUtils.loadJsonTT(file.toString(), stMapType);
                
                tempstates.forEach((k, v) -> {
                    TaskState vts = (TaskState)v;
                    taskstates.put(vts.getRealm() + "_" + k, vts);
                });
                currentrealm = taskid;
                
                if (pathbup != null){
//                    PicnoUtils.saveJson(pathbup+file.getFileName(), tempstates, true);
                }
                
                
            }
        }catch(IOException e) {
            log.info(String.format("Exception when loading states @ %s is : %s" , taskid, e.getCause().getMessage()));
            
        }
        
        Controller ctrl = Controller.getInstance();
        ctrl.removeSMEventListener("all", this);
        
        for ( String k : taskstates.keySet()){
            TaskState ts = taskstates.get(k);
            if (ts.getStatetype().equalsIgnoreCase("SIGNAL")){
                SMEvent smevt = new SMEvent()
                .setTask(this)
                .setTaskstate(ts);  
                ctrl.addSMEventListener(k, smevt );
            }
        }
        
    }
    
    
    public SMTraffic goNext(String nextstate){
        
       //LOG.info(String.format("CheckP0 going next state : %s", nextstate));
        
        setCurrent_taskstate(getTaskstates().get(currentrealm + "_" + nextstate));
        
        
        
        if (getCurrent_taskstate() != null){
            return new SMTraffic(0l, 0l, 0, getCurrent_taskstate().getCallstate(), this.getClass(),
                                    new VirnaPayload()
                                        .setObject(this)
                                        .setString(getCurrent_taskstate().getStatecmd())
                                );
        }
        return null;
    }
    
    
    public SMTraffic getNext(String nextstate){ 
        //LOG.info(String.format("CheckP0 going next state : %s", nextstate));
        TaskState taskstate = getTaskstates().get(currentrealm + "_" + nextstate);
        if (taskstate != null){
            return new SMTraffic(0l, 0l, 0, taskstate.getCallstate(), this.getClass(),
                                    new VirnaPayload()
                                        .setObjectType(nextstate)
                                        .setObject(this)
                                        .setString(taskstate.getStatecmd())
                                );
        }
        return null;
    }
    
    
    
    
    
    public void estimatorGate(){    
    }
    
    
    public void addSample (Long ts, Double value, Long counts){
        
        samplering.addSample(value);
        if (start_ts == 0l) start_ts = System.currentTimeMillis();
        Double dts = ctx.getSecTS(ts, start_ts);
 
//        log.info(String.format("BaseAnaTask loaded %f(%d) / %f / %f / %f", value , counts,  dts, samplering.getAverage(),  samplering.getDiff()));
//        System.out.print('.');
    }
    
    
    public void resetMeasurements(){
        
        start_ts = 0L;
//        dbuffer.clear();
        events.clear();
        samplering.clear();
        
        if (ctx.auxmain_series != null && ctx.auxcompanion_series != null){
            Platform.runLater(() -> {
                ctx.auxmain_series.clear();
                ctx.auxcompanion_series.clear();
            }); 
        }
    }
    
    
    public boolean createGraph(Boolean clear){   
        return false;
    }

    public Object getDataframe() {
        return dataframe;
    }

    public void setDataframe(Object dataframe) {
        this.dataframe = dataframe;
    }
    
    public VarPool getVarPool() {
        return varpool;
    }
    
    public void initVarPool(){
        varpool = new VarPool();
    }
    
    public Node getMainPane(){ return null; }

//    public AnchorPane getInfoPane(){ return null; }
    
    
    public LinkedHashMap<String,TaskState> getTaskstates() {
        return taskstates;
    }

    public TaskState getCurrent_taskstate() {
        return current_taskstate;
    }

    public void setCurrent_taskstate(TaskState current_taskstate) {
        this.current_taskstate = current_taskstate;
    }
 
    public void setCurrent_taskstate(String state) {
        this.current_taskstate = taskstates.get(state);
    }
    
    
    
    
    
    
    
    
    public void geIsoTimeDomainPoints() {
        
    }
    
    public ObservableList<XYChart.Data<Number, Number>> getIsoDataDomainPoints(boolean adsorption, boolean ppo, boolean volg) {
       return null;
    }
    
    
}
