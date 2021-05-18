/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import cern.extjfx.chart.plugins.XValueIndicator;
import com.google.gson.reflect.TypeToken;
import com.opus.syssupport.PicnoUtils;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author opus
 */
public class RootTask extends BaseAnaTask {

    private static final Logger log = Logger.getLogger(RootTask.class.getName());
        
    private AuxChartDescriptor chdesc;
    
    private boolean bootstrap = true;
    
    private Rectangle chartbanner;
    
    public Parent controlpane;
    public RootTaskController rtctrl;
    private JournalSideNode journal;
   
    
    public RootTask(ASVPDevice asvpdev, Context ctx, String script) {
        
        super(asvpdev, ctx);
        taskid = "roottask";
        
        Image chbanner = new Image(getClass().getClassLoader().getResource("middlestripb/logoacp.png").toExternalForm());
        chartbanner = new Rectangle(660, 500);
        chartbanner.setFill(new ImagePattern(chbanner, 0, 0, 1, 1, true));

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RootTaskPanel.fxml"));
            controlpane = fxmlLoader.load();
            rtctrl = fxmlLoader.<RootTaskController>getController();
        } catch (IOException ex) { 
            Logger.getLogger(RootTask.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        Controller appctrl = Controller.getInstance();
        appctrl.loadStates(RootTask.class, this);
        
        this.initStates(script);
        
    }

    @Override
    public Node getMainPane(){
        
        if (bootstrap){
            return chartbanner;
        }
        else{
            return controlpane;
        }
    }
    
    
    
    @Override
    public void prepareGo(){ 
        super.Go();
    }
    
    
    
    @Override
    public void Restart(){
        FX1Controller anct = ctx.getFXController();
        
        super.Go();
        
        anct.showInfoPane("asvpdevice");
        
        anct.showMainChart(controlpane);
  
        auxchart = anct.getAuxchart(); 
        auxchart.refreshChart(taskid);
        
        // Setup the journal
        if (journal == null){
            journal = new JournalSideNode(Side.TOP, anct.getAuxhspane());
            ctx.journals.put(this, journal);   
            journal.addEntry("Root Task is initializing...");
        }
        
        ctx.current_journal = journal;
        ctx.getFXController().getAuxhspane().setTop(journal);
        AuxChartDescriptor cd = ctx.auxcharts.get(taskid);               
        
        
        Go();
  
        
    }
    
    
    @Override
    public void Go(){
        
        if (createGraph(true)){
            Platform.runLater(() -> {
                FX1Controller fx1 = FX1Controller.getInstance();
                auxchart = fx1.getAuxchart(); 
            });    
        }

        SMTraffic nxt = goNext("TASKINIT");
        if (nxt != null){
            Controller.getInstance().processSignal(nxt);
        }
        
    }
    
    
    
    @Override
    public void initStates(String script){
        
        super.initStates(script);
        
        setCurrent_taskstate(getTaskstates().get("TASKINIT"));

    }
    
 
    
    @Override
    public boolean createGraph(Boolean clear) {
      
        
        chdesc = ctx.auxcharts.get(taskid);
        
        if (chdesc == null){
            chdesc = new AuxChartDescriptor();
            chdesc.overlay.label.setText("Root aux chart");
            chdesc.overlay.clearMessages();
        }
        else if (!chdesc.dirty){
            return true;
        }
        
        
        // Hard coded =====================================================================================
        chdesc.xlabel = "Time - Sec";
        chdesc.ylabel = "Pressure (mmHg)";

        chdesc.xmin = 0.0;
        chdesc.xmax = 30.0;
        chdesc.xtick = 2.0;
        
        chdesc.ymin = 0.0;
        chdesc.ymax = 100.0;
        chdesc.ytick = 10.0;
        
        
        chdesc.series.put("main_data", FXCollections.observableArrayList());
        
        chdesc.overlay.addMessage(String.format("Loaded : Hard coded"));

//        chdesc.addYVal ("buildtgt", String.format("Build Target: %6.2f mmHg", prof.getBprs()), prof.getBprs(), null, 0.2);
//        chdesc.addYRange("dvthrs", "\u0394P̣ Threshold", 0.0, 1.0, auxchart.getCompanionYAxis(), null, null);
        
//        chdesc.auxlabel = "Delta P";
//        chdesc.series.put("companion_data", FXCollections.observableArrayList());
        
        chdesc.dirty = false;
        ctx.auxcharts.put(this.taskid, chdesc);
        return true;
               
    }
    
    
    // ================================================= SERVICE STATES ========================================================
    
    @smstate (state = "CALLSCRIPT")
    public boolean st_callScript(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            
            loadScript(tst.getStatecmd());
            
            
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
    
    
    
    
    
    
    
    @smstate (state = "ISP0UPDATED")
    public boolean st_isP0Updated(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            
            
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
  
}





   
//    @Override
//    public SMTraffic goNext(String nextstate){   
//       
//        //LOG.info(String.format("CheckP0 going next state : %s", nextstate));
//        setCurrent_taskstate(getTaskstates().get(nextstate));
//        if (getCurrent_taskstate() != null){
//            return new SMTraffic(0l, 0l, 0, getCurrent_taskstate().getCallstate(), this.getClass(),
//                                    new VirnaPayload()
//                                        .setObject(this)
//                                        .setString(getCurrent_taskstate().getStatecmd())
//                                );
//        }
//        return null;
//    }
//    
//    @Override
//    public SMTraffic getNext(String nextstate){   
//       
//        //LOG.info(String.format("CheckP0 going next state : %s", nextstate));
//        TaskState taskstate = getTaskstates().get(nextstate);
//        if (taskstate != null){
//            return new SMTraffic(0l, 0l, 0, taskstate.getCallstate(), this.getClass(),
//                                    new VirnaPayload()
//                                        .setObjectType(nextstate)
//                                        .setObject(this)
//                                        .setString(taskstate.getStatecmd())
//                                );
//        }
//        return null;
//    }
//    
    