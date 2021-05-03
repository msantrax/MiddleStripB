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
import javafx.geometry.Side;
import javafx.scene.chart.XYChart;
import middlestripb.VarPool.VarInfo;

/**
 *
 * @author opus
 */
public class CheckP0AnaTask extends BaseAnaTask {

    private static final Logger LOG = Logger.getLogger(CheckP0AnaTask.class.getName());
    

    private Integer windowsize = 120;
    
    private AuxChartDescriptor chdesc;
    
    private CalcP0 dtfr ; // = this.getDataframe();
    private CalcP0_pf prof ; // = (CalcP0_pf)dtfr.calcp0_pf;
    
   
    private JournalSideNode journal; 
    
    
    
    
    public CheckP0AnaTask(ASVPDevice asvpdev, Context ctx) {
        
        super(asvpdev, ctx);
        taskid = "checkp0";
 
//        dtfr = CalcP0.getInstance(1L);
//        dtfr.setDesc("Iniciado via codigo java");
   
    }

    
    @Override
    public void prepareGo(){
        
        super.Go();
        
        // Setup the journal
        if (journal == null){
            journal = new JournalSideNode(Side.TOP, ctx.getFXController().getAuxpane());
            ctx.journals.put(this, journal);   
            journal.addEntry("CheckP0 Task (Determine Zero&ATM pressures) is initializing...");
        }
        ctx.current_journal = journal;
        ctx.getFXController().getAuxpane().setTop(journal);
        
        if (dataframe == null){
            asvpdev.loadTaskConfig(CalcP0.class, "checkp0task", 1617981872798l);
        }
        else{
            Go();
        }
    }
    
    @Override
    public void Restart(){
        
        super.Go();
        ctx.auxcharts.remove("checkp0");
        asvpdev.loadTaskConfig(CalcP0.class, "checkp0task", 1617981872798l);
        
        
        
    }
    
    
    @Override
    public void Go(){
        
        dtfr = this.getDataframe();
        prof = (CalcP0_pf)dtfr.calcp0_pf;
        
        if (createGraph(true)){
            Platform.runLater(() -> {
                FX1Controller fx1 = FX1Controller.getInstance();
                auxchart = fx1.getAuxchart(); 
                auxchart.refreshChart("checkp0");
                
//                chdesc.addYRange("dvthrs", String.format("%s Threshold", prof.getChrt_Ycomplabel()), 
//                        0.0, 200.0, null, null, null);
                
//                chdesc.addYRange("dvthrs", String.format("%s Threshold", prof.getChrt_Ycomplabel()), 
//                        0.0, prof.getBprs_Thrs(), auxchart.getCompanionYAxis(), null, null);
//                accept = true;
                
            });    
        }
        
        this.initStates();
        
        SMTraffic nxt = goNext("TASKINIT");
        if (nxt != null){
            Controller.getInstance().processSignal(nxt);
        }
        
    }
    
    
    @Override
    public CalcP0 getDataframe() {
        return (CalcP0)dataframe;
    }

    @Override
    public void setDataframe(Object dataframe) {
        this.dataframe = (CalcP0)dataframe;
    }
    
    
   
    @Override
    public void initStates(){
        
        taskstates = new LinkedHashMap<>();
        initVarPool();
        
        try {
            Type stMapType = new TypeToken<LinkedHashMap<String, TaskState>>() {}.getType();
            taskstates = PicnoUtils.loadJsonTT(ASVPDevice.JSONS + "checkp0_states.json", stMapType);
        } catch (IOException ex) {
            Logger.getLogger(CheckP0AnaTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        varpool.Push("MESSAGE1", varpool.new VarInfo("VarPool Message 1", "String"));
        varpool.Push("MESSAGE2", "VarPool message 2");
        varpool.Push("VPDOUBLE1", varpool.new VarInfo(37.2, "DoubleNF"));
        varpool.Push("TSTAMP", System.currentTimeMillis());
        
        
        
//        String tst = "Teste da &MESSAGE1 que aponta para a &VPDOUBLE1";
//        String sout = asvpdev.formatMessage(tst, this);
        
        setCurrent_taskstate(getTaskstates().get("TASKINIT"));
        
//        ArrayList<String> ltst = new ArrayList<>();
//        ltst.add("teste1");
//        ltst.add("teste2");
//        current_taskstate.setLoad(ltst);
        
//        ArrayList<TaskStateChronoSegment> ltst = new ArrayList<>();
//        TaskStateChronoSegment tscs1 = new TaskStateChronoSegment();
//        ltst.add(tscs1);
//        current_taskstate.setChronosegs(ltst);
        
        
//        try {
//            PicnoUtils.saveJson(ASVPDevice.JSONS + "checkp0_states_temp4.json", taskstates, true);
//        } catch (IOException ex) {
//            Logger.getLogger(CheckP0AnaTask.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
        LOG.info(String.format("CheckP0 task stats init"));
    }
    
    
    @Override
    public SMTraffic goNext(String nextstate){   
       
        //LOG.info(String.format("CheckP0 going next state : %s", nextstate));
        
        String state = nextstate;
        
        if (lockedstate.contains("LOCK")){
            lockedstate = nextstate;
            return null;
        }
        else if (!lockedstate.isEmpty()){
            state = lockedstate;
        }
        
        setCurrent_taskstate(getTaskstates().get(state));
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
    
    
    
    
    public void estimatorGate(Double dts, Double press, Double diff){
       
        
        current_dts = dts;
        current_value = press;
        current_diff = diff;
        
        Double value = diffvalue ? diff : press;
        
        if (valueover != -1 && value > valueover){
            LOG.info(String.format("Verified that  %f is over %f @ %f : ", value, valueover, dts));
            valueover = -1.0;
            SMTraffic nxt = goNext(valuetask);
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
        }
        if (valueunder != -1 && value < valueunder){
            LOG.info(String.format("Verified that  %f is under %f @ %f : ", value, valueunder, dts));
            valueunder = -1.0;
            SMTraffic nxt = goNext(valuetask);
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
        }
        
    }
    
    
    @Override
    public void addSample (Long ts, Double value, Long counts){
        
        if (!accept) return;
        
        samplering.addSample(value);
        if (start_ts == 0l) start_ts = System.currentTimeMillis();
        
        Double dts = ctx.getSecTS(ts, start_ts);
        
        //this.local_tsbuffer.add(ctx.getSecTS(ts, start_ts));
        dtfr.getTsbuffer().add(ctx.getSecTS(ts, start_ts));
        dtfr.getValbuffer().add(value);
        dtfr.getComp1Buffer().add(samplering.getDiff());

//        if((ctx.auxmain_series.size()%400) == 0 ){
//            dtfr.setTs_End(ts);
//        }
        
        
        Platform.runLater(() -> {
            
            asvpdev.sendtoBarGraphs(value, samplering.getDiff(), samplering.getVariance());
            if(ctx.auxmain_series.size() >= windowsize){
//                LOG.info(String.format("Scaling graph to : %d", ctx.auxmain_series.size()));
                chdesc.xmax = chdesc.xmax + (windowsize/8) ; // (chdesc.xmax /2 ) + chdesc.xmax;
                chdesc.xmin = chdesc.xmin + (windowsize/8) ; // (chdesc.xmax /2 ) + chdesc.xmin;
                auxchart.getxAxis().setUpperBound(chdesc.xmax);
                auxchart.getxAxis().setLowerBound(chdesc.xmin);
                for (int i = 0; i < (windowsize/2)-1; i++) {
                    ctx.auxmain_series.remove(i);
                    ctx.auxcompanion_series.remove(i);
                }
//                LOG.info(String.format("Done - Graph scale is : %d", ctx.auxmain_series.size()));
            }
            ctx.auxmain_series.add(new XYChart.Data<Number, Number>(dts, value));
            ctx.auxcompanion_series.add(new XYChart.Data<Number, Number>(dts, samplering.getDiff()));
            
//            LOG.info(String.format("Loaded %f(%d) / %f / %f / %f / %f", value , counts,  dts, 
//                    samplering.getAverage(),  
//                    samplering.getDiff(),
//                    samplering.diffabs));
            
//            LOG.info(String.format("Loaded %f(%d)@%f / %f ", value , counts, dts, samplering.getDiff()));
//            System.out.print('*');
          
        });  
        
        estimatorGate(dts, value, samplering.getDiff());
        
    }
    
    
    @Override
    public boolean createGraph(Boolean clear) {
      
        
        chdesc = ctx.auxcharts.get("checkp0");
        
        if (chdesc == null){
            chdesc = new AuxChartDescriptor();
            chdesc.overlay.label.setText(prof.getChrt_Header());
            chdesc.overlay.clearMessages();
        }
        else if (!chdesc.dirty){
            return true;
        }
        
        
        chdesc.xlabel = prof.getChrt_Xlabel();
        chdesc.ylabel = prof.getChrt_Ymainlabel();

        chdesc.xmin = prof.getChrt_Xmainmin();
        chdesc.xmax = prof.getChrt_Xmainmax();
        chdesc.ymin = prof.getChrt_Ymainmin();
        chdesc.ymax = prof.getChrt_Ymainmax();
        
        Double dws = (chdesc.xmax - chdesc.xmin) * 4 ;
        windowsize =  dws.intValue();
        chdesc.windowsize = this.windowsize;

        chdesc.series.put("main_data", FXCollections.observableArrayList());
        
        chdesc.overlay.addMessage(String.format("Loaded :"+ prof.getDesc()));

        
        chdesc.addYVal ("buildtgt", String.format("Build Target: %6.2f mmHg", prof.getBprs()), 
                prof.getBprs(), null, 0.2);
//        chdesc.addYRange("dvthrs", "\u0394P̣ Threshold", 0.0, 1.0, auxchart.getCompanionYAxis(), null, null);
        
        chdesc.auxlabel = prof.getChrt_Ycomplabel();
        chdesc.series.put("companion_data", FXCollections.observableArrayList());
        
        
        chdesc.dirty = false;
        ctx.auxcharts.put(this.taskid, chdesc);
        return true;
               
    }
  
}

