/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.CalcP0;
import Entities.CalcP0_pf;
import com.google.gson.reflect.TypeToken;
import com.opus.syssupport.PicnoUtils;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import middlestripb.VarPool.VarInfo;

/**
 *
 * @author opus
 */
public class RootTaskSeed extends BaseAnaTask {

    private static final Logger LOG = Logger.getLogger(RootTaskSeed.class.getName());
        
    private AuxChartDescriptor chdesc;
 
   
    public RootTaskSeed(ASVPDevice asvpdev, Context ctx) {
        
        super(asvpdev, ctx);
        taskid = "roottaskseed";
   
    }

    
    @Override
    public void prepareGo(){       
        super.Go();

    }
    
    @Override
    public void Restart(){
        super.Go();
        
    }
    
    
    @Override
    public void Go(){
        
        if (createGraph(true)){
            Platform.runLater(() -> {
                FX1Controller fx1 = FX1Controller.getInstance();
                auxchart = fx1.getAuxchart(); 
                
            });    
        }
        
        this.initStates();
        
        SMTraffic nxt = goNext("SETAUTO_RESET");
        if (nxt != null){
            Controller.getInstance().processSignal(nxt);
        }
        
    }
    
   
    @Override
    public void initStates(){
        
        taskstates = new LinkedHashMap<>();
        initVarPool();
        
        try {
            Type stMapType = new TypeToken<LinkedHashMap<String, TaskState>>() {}.getType();
            taskstates = PicnoUtils.loadJsonTT(ASVPDevice.JSONS + "checkp0_states.json", stMapType);
        } catch (IOException ex) {
            Logger.getLogger(RootTaskSeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
        setCurrent_taskstate(getTaskstates().get("TASKINIT"));

    }
    
    
    @Override
    public SMTraffic goNext(String nextstate){   
       
        //LOG.info(String.format("CheckP0 going next state : %s", nextstate));
        setCurrent_taskstate(getTaskstates().get(nextstate));
        if (getCurrent_taskstate() != null){
            return new SMTraffic(0l, 0l, 0, getCurrent_taskstate().getCallstate(), this.getClass(),
                                    new VirnaPayload()
                                        .setObject(this)
                                        .setString(getCurrent_taskstate().getStatecmd())
                                );
        }
        return null;
    }
    
    @Override
    public SMTraffic getNext(String nextstate){   
       
        //LOG.info(String.format("CheckP0 going next state : %s", nextstate));
        TaskState taskstate = getTaskstates().get(nextstate);
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
    
   
  
}

