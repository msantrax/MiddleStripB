/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.syssupport.SMTraffic;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author opus
 */
public class BaseAnaTask {

    private static final Logger LOG = Logger.getLogger(BaseAnaTask.class.getName());
    
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
    
    
    
    
    public BaseAnaTask(ASVPDevice asvpdev, Context ctx) {
        this.asvpdev = asvpdev;
        this.ctx = ctx;
        
        events = new LinkedHashMap<>();
        samplering = new SampleRing(5);
        
        initStates();
       
    }
    
    public void prepareGo(){
     
    }
    
    public void Restart(){
     
    }
    
    public void Go(){    
        resetMeasurements();
    }
    
    public void initStates(){
        
        taskstates = new LinkedHashMap<>();
        getTaskstates().put("BaseIdle", new TaskState ("TASKIDLE", "NONE"));
        
    }
    
    
    public SMTraffic goNext(String nextstate){ 
        return null;
    }
    
    public SMTraffic getNext(String nextstate){ 
        return null;
    }
    
    public void estimatorGate(){    
    }
    
    
    public void addSample (Long ts, Double value, Long counts){
        
        samplering.addSample(value);
        if (start_ts == 0l) start_ts = System.currentTimeMillis();
        Double dts = ctx.getSecTS(ts, start_ts);
 
//        LOG.info(String.format("BaseAnaTask loaded %f(%d) / %f / %f / %f", value , counts,  dts, samplering.getAverage(),  samplering.getDiff()));
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
}
