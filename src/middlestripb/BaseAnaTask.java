/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 *
 * @author opus
 */
public class BaseAnaTask {

    private static final Logger LOG = Logger.getLogger(BaseAnaTask.class.getName());
    
    protected ASVPDevice asvpdev;
    protected Context ctx;
    
    public SampleRing samplering;
    public ObservableMap<Long, Double> dbuffer;
    public LinkedHashMap<Long, String> events;
    public Long start_ts = 0L;

    public Double monitorvalue;
    
    protected LinkedHashMap<String,TaskState> taskstates;
    protected TaskState current_taskstate;
    
    
    public BaseAnaTask(ASVPDevice asvpdev, Context ctx) {
        this.asvpdev = asvpdev;
        this.ctx = ctx;
        
        dbuffer = FXCollections.observableHashMap();
        events = new LinkedHashMap<>();
        samplering = new SampleRing(5);
        
        initStates();
        
        
    }
    
    public void Go(){
        
        resetMeasurements();
        
    }
    
    public void initStates(){
        
        taskstates = new LinkedHashMap<>();
        
        taskstates.put("BaseIdle", new TaskState ("TASKIDLE", "NONE"));
        
    }
    
    public SMTraffic goNext(String nextstate){
        
        return null;
    }
    
    
    
    
    public void addSample (Long ts, Double value){
        
        samplering.addSample(value);
        if (start_ts == 0l) start_ts = System.currentTimeMillis();
        
        dbuffer.put(ts, value);
        LOG.info(String.format("Base Ana task Loaded %f / %d / %f", value , ts, samplering.getDiff()));
        
    }
    
    
    public void resetMeasurements(){
        
        start_ts = 0L;
        dbuffer.clear();
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
    
    
    
    
    
    
}
