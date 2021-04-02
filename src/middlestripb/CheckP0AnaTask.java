/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;

/**
 *
 * @author opus
 */
public class CheckP0AnaTask extends BaseAnaTask {

    private static final Logger LOG = Logger.getLogger(CheckP0AnaTask.class.getName());

    
    
    public CheckP0AnaTask(ASVPDevice asvpdev, Context ctx) {
        super(asvpdev, ctx);
    }

    
    @Override
    public void Go(){
        
        super.Go();
        if (createGraph(true)){
            Platform.runLater(() -> {
                FX1Controller fx1 = FX1Controller.getInstance();
                fx1.getAuxchart().refreshChart("checkp0");
            });    
        }
        SMTraffic nxt = goNext("SETAUTO_BUILDP");
        if (nxt != null){
            Controller.getInstance().processSignal(nxt);
        }
        
    }
    
    
   
    @Override
    public void initStates(){
        
        taskstates = new LinkedHashMap<>();
        
        taskstates.put("TASKINIT", new TaskState ("TASKIDLE", "SETAUTO_BUILDP"));
        taskstates.put("SETAUTO_BUILDP", new TaskState ("SETAUTO", "NOTIFY_BUILDP", "SETVALVES=BUILDP"));
        taskstates.put("NOTIFY_BUILDP", new TaskState ("NOTIFYAUX", "NONE", "Instrument set to Build Pressure"));
        
        current_taskstate = taskstates.get("TASKINIT");
        
        
    }
    
    
    
    @Override
    public SMTraffic goNext(String nextstate){   
       
        current_taskstate = taskstates.get(nextstate);
        if (current_taskstate != null){
            return new SMTraffic(0l, 0l, 0, current_taskstate.getCallstate(), this.getClass(),new VirnaPayload()
                                        .setObject(this)
                                        .setString(current_taskstate.getStatecmd())
                                        
                                    );
            
        }
        return null;
    }
    
    
    
    @Override
    public void addSample (Long ts, Double value){
        
        samplering.addSample(value);
        if (start_ts == 0l) start_ts = System.currentTimeMillis();
        
        dbuffer.put(ts, value);
        
        Platform.runLater(() -> {
            asvpdev.sendtoBarGraphs(value, samplering.getDiff(), samplering.getVariance());
            Double dts = ctx.getSecTS(ts, start_ts);
            ctx.auxmain_series.add(new XYChart.Data<Number, Number>(dts, value));
            ctx.auxcompanion_series.add(new XYChart.Data<Number, Number>(dts, samplering.getDiff()));
//            LOG.info(String.format("Loaded %f / %f / %f", value , dts, samplering.getDiff()));
        });  
        
    }
    
    
    @Override
    public boolean createGraph(Boolean clear) {
      
        AuxChartDescriptor chdesc;
        
        
        chdesc = ctx.auxcharts.get("checkp0");
        if (chdesc == null){
            chdesc = new AuxChartDescriptor();
            chdesc.overlay.label.setText("Check P0 Pressure");
            chdesc.overlay.clearMessages();
        }
        else if (!chdesc.dirty){
            return true;
        }
        
        
        chdesc.xlabel = "Time(sec)";
        chdesc.ylabel = "Pressure (mmHg)";

        chdesc.xmin = 0.0;
        chdesc.xmax = 12.0;
        chdesc.ymin = 0.0;
        chdesc.ymax = 1000.0;

        chdesc.series.put("main_data", FXCollections.observableArrayList());
        
        
        chdesc.overlay.addMessage(String.format("Setup loaded from profile : "));

        chdesc.addYVal ("buildtgt", "Build Target : 800 mmHg", 800.0, null, 0.2);

        
        chdesc.auxlabel = "\u0394P̣";
        chdesc.series.put("companion_data", FXCollections.observableArrayList());
        
        
        chdesc.dirty = false;
        ctx.auxcharts.put("checkp0", chdesc);
        return true;
               
    }
    
    
    
    
    
}
