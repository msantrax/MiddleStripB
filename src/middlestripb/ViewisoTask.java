/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Entity;
import Entities.Isotherm;
import Entities.Point;
import cern.extjfx.chart.XYChartPane;
import com.mongodb.client.model.Filters;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import isothermview.IsothermChart;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.XYChart;

/**
 *
 * @author opus
 */
public class ViewisoTask extends BaseAnaTask {

    private static final Logger LOG = Logger.getLogger(ViewisoTask.class.getName());
        
    private AuxChartDescriptor chdesc;
    
    private JournalSideNode journal;
   
    private IsothermChart isothermchart;
    private XYChartPane mainchartpane;
    private LinkedHashMap<Double, PointInfoCTX> points_ctx;
    
    
    private FX1Controller anct;
    private Controller appctrl = Controller.getInstance();
    
    
    
    public ViewisoTask(FX1Controller anct, Context ctx, String script) {
        
        super(null, ctx);
        
        points_ctx = new LinkedHashMap<>();
        this.anct = anct;
        taskid = "viewisotask";
       
        initStates(script);
        
    }

    
    
    @Override
    public void prepareGo(){ 
        
        anct = ctx.getFXController();
        
        
        IsothermInfoController iic = new IsothermInfoController(anct);
        anct.infopanes.put("isotherminfo", iic );
        anct.getInfopane().getChildren().add(iic);
        
        PointInfoController pic = new PointInfoController(anct);
        anct.infopanes.put("pointinfo", pic );
        anct.getInfopane().getChildren().add(pic);
        
        
        
        isothermchart = new IsothermChart(anct, this);
        mainchartpane = isothermchart.createCernChart();
        mainchartpane.getStylesheets().add(getClass().getClassLoader().getResource("middlestripb/isochart.css").toExternalForm());
        
        createGraph(true);
        
        
        // Setup the journal
        if (journal == null){
            journal = new JournalSideNode(Side.TOP, anct.getAuxhspane());
            ctx.journals.put(this, journal);   
            journal.addEntry("This is view Isotherm Task.");
        }
        
        super.Go();
    }
    
    
    
    @Override
    public void Restart(){
        
        FX1Controller anct = ctx.getFXController();

        anct.showMainChart(mainchartpane);
        anct.showInfoPane("isotherminfo");
        
        
        auxchart = anct.getAuxchart(); 
        auxchart.refreshChart(taskid);
        
        
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

        SMTraffic nxt = goNext("startevent_input1");
        if (nxt != null){
            Controller.getInstance().processSignal(nxt);
        }
        
    }
    
    
//    @Override
//    public void initStates(){
//        
//        super.initStates();
//        setCurrent_taskstate(getTaskstates().get("viewisotask"));
//
//    }
//    
    
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
    
    
    @Override
    public boolean createGraph(Boolean clear) {
      
        
        chdesc = ctx.auxcharts.get(taskid);
        
        if (chdesc == null){
            chdesc = new AuxChartDescriptor();
            chdesc.overlay.label.setText("Time Domain Iso Chart");
            chdesc.overlay.clearMessages();
        }
        else if (!chdesc.dirty){
            return true;
        }
        
        
        // Hard coded =====================================================================================
        chdesc.xlabel = "Time - Sec";
        chdesc.ylabel = "Pressure (P0/Po";

        chdesc.xmin = 0.0;
        chdesc.xmax = 200.0;
        chdesc.xtick = 10.0;
        
        chdesc.ymin = 0.0;
        chdesc.ymax = 1.2;
        chdesc.ytick = 0.2;
        
        
        chdesc.series.put("main", FXCollections.observableArrayList());

        
        chdesc.dirty = false;
        ctx.auxcharts.put(this.taskid, chdesc);
        return true;
               
    }
    
    
    // ======================================== ISOTHERM SERVICES =============================================================
    
    
    private Isotherm isotherm;
    
    
    public Isotherm getIsotherm() {
        return isotherm;
    }

    public void setIsotherm(Isotherm isotherm) {
        this.isotherm = isotherm;
    }
    
    
    public Point findPointByPressure(double pressure, boolean relative){
        
        int pointer = 0;
        for (Point isop : isotherm.getObjPoints()){
            if (relative){
                if (isop.getP_P0() == pressure) return isop;
            }
            else{
                if (isop.getP_End() == pressure) return isop;
            }
            pointer++;
        }
        return null;
    }
    
    public ObservableList<XYChart.Data<Number, Number>> getIsoDataDomainPoints(boolean adsorption, boolean ppo, boolean volg) {
      
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        ArrayList<Point>isopoints = getIsotherm().getObjPoints();
        
        Integer ptype = adsorption ? 0 : 1 ;
        
        for (Point isopoint : isopoints){
            if (isopoint.getPoint_Type() == ptype ) {
                Double pressure = ppo ? isopoint.getP_P0() : isopoint.getP_End();
                Double volume = volg ? isopoint.getPoint_Volume() : isopoint.getVtc_Sw();
                XYChart.Data<Number, Number> d = new XYChart.Data<>(pressure, volume);
                d.setExtraValue(isopoint.getP_End());
                data.add(d);
            }
        }
        return FXCollections.observableArrayList(data);
    }

    
    
    public void geIsoTimeDomainPoints() {
      
        AuxChartDescriptor chdesc;
        
        chdesc = ctx.auxcharts.get("isotimedomain");
        if (chdesc == null){
            chdesc = new AuxChartDescriptor();
            chdesc.overlay.label.setText("Isotherm @ time domain");
            chdesc.overlay.clearMessages();
        }
        else if (!chdesc.dirty){
            return;
        }
        
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        
        ArrayList<Point>isopoints = getIsotherm().getObjPoints();

        Double pressure, ts;
        Long init_ts = isopoints.get(0).getStart_Ts();

        for (Point isopoint : isopoints){

            pressure = isopoint.getP_Start();
            ts = ctx.getSecTS (isopoint.getStart_Ts(), init_ts);
            data.add(new XYChart.Data<Number, Number>(ts, pressure));

            pressure = isopoint.getP_End();
            ts = ctx.getSecTS (isopoint.getEnd_Ts(), init_ts);
            data.add(new XYChart.Data<Number, Number>(ts, pressure));

            chdesc.xmax = ts;
            int maxflag = Double.compare(pressure , (Double)chdesc.ymax);
            chdesc.ymax = maxflag > 0  ? pressure : chdesc.ymax  ;

        }

        chdesc.xlabel = "Time(sec)";
        chdesc.ylabel = "Pressure (mmHg)";

        chdesc.xmin = chdesc.xmin - 10;
        chdesc.xmax = chdesc.xmax + (chdesc.xmax / 10);
        chdesc.ymin = chdesc.ymin - (chdesc.ymin / 10);
        chdesc.ymax = chdesc.ymax + (chdesc.ymax / 10);

        chdesc.series.put("main", FXCollections.observableArrayList(data));
        
        chdesc.overlay.addMessage(String.format("Loaded %d points", isopoints.size()));
        chdesc.overlay.addMessage("No companion Yaxis requested");

//        chdesc.addYVal ("maxpoint", "Target : 720 mmHg", 200.45, null, 0.2);
//        chdesc.addXVal ("maxpoint", "Event", 20000.0, null);

//        chdesc.addYRange ("rangey", "Range Y", 0.0, 122.56 , null, null, null);
//        chdesc.addXRange ("rangex", "Range X", 10000.0, 15000.0 , null, null);
        
//        chdesc.auxlabel = "\u0394P̣";
        
        
        chdesc.dirty = false;
        ctx.auxcharts.put("isotimedomain", chdesc);  
                
    }
    
    
    
    
    // ============================================ ISO POINT SERVICES =========================================================
    
    
    
    
    public PointInfoCTX registerPoint(Double pressure, boolean relative){
        
        if (points_ctx.containsKey(pressure)){
            return points_ctx.get(pressure);
        }
        
        Point pt = findPointByPressure(pressure, relative);
        if (pt != null){
            PointInfoCTX ptctx = new PointInfoCTX(pressure)
                    .setTempo(pt.getStart_Ts(), pt.getEnd_Ts())
                    .setPoint(pt);
            points_ctx.put(pressure, ptctx);
            return ptctx;
        }
        return null;
    }
    
    
    public PointInfoCTX updatePoint(Double pressure){
        
        PointInfoCTX ptctx = points_ctx.get(pressure);
        if (ptctx != null) {
            
        }
        
        return ptctx;
    }
    
    
    public void updateIsothermChart(){
  
        Platform.runLater(() -> {
            isothermchart.refreshChart();
            geIsoTimeDomainPoints();
            auxchart.refreshChart("isotimedomain");
            
            
        });         
          
    }
    
    
    
    // ======================================== ISOTHERM MACHINE STATES  =========================================================
    
    @smstate (state = "LOADISOVIEW")
    public boolean st_loadIsoView(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        String substate = payload.getCallerstate();
        TaskState tst;
        Long suid = 0L ;
        
        if (payload.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)payload.vobject;
            if (tsk.getCurrent_taskstate() == null){
                tst = (TaskState) payload.getCaller();
            }
            else{
                tst = tsk.getCurrent_taskstate();
            }
            
            String sid = tst.getSparam1();
            
            try {
                Long lid = Long.parseLong(sid);
                appctrl.processSignal(new SMTraffic(0l, 0l, 0, "LOADISOVIEW", this.getClass(),
                                           new VirnaPayload()
                                                    .setCallerstate("LOADPHASE1")
                                                    .setLong1(lid)
                                                    .setInt1(10))); 
            } catch (NumberFormatException ex) {
                LOG.severe("Severe !!!");
            }
            
            
        }
        else{
            switch(substate){
            
                case "ASKUSER" :
                    suid = 1615509387066L;
                    appctrl.processSignal(new SMTraffic(0l, 0l, 0, "LOADISO", this.getClass(),
                                       new VirnaPayload()
                                                .setCallerstate("LOADPHASE1")
                                                .setLong1(suid)
                                                .setInt1(10)));  
                    break;

                case "LOADPHASE1" :
                    EntityDescriptor ed = new EntityDescriptor()
                    .setClazz(Isotherm.class)
                    .setBson(Filters.eq("iso_num", payload.int1))
                    .setCascade(Boolean.FALSE)
                    .setAction(new SMTraffic(0l, 0l, 0, "LOADISOVIEW", this.getClass(),
                            new VirnaPayload().setCallerstate("LOADPHASE2")));         
                    MongoLink.getInstance().getTask_descriptors().offer(ed);
                    break;

                case "LOADPHASE2" :
                    Entity etph2 = (Entity)payload.vobject;
                    etph2.loadChildren(false, new SMTraffic(0l, 0l, 0, "LOADISOVIEW", this.getClass(),
                            new VirnaPayload().setCallerstate("LOADPHASE3"))); 
                    break;

                case "LOADPHASE3" :
                    Entity et = (Entity)payload.vobject;
                    et.loadChildren(false, null);

                    setIsotherm((Isotherm)et);
                    updateIsothermChart();
                    break;

            }
         
        }
        
        return true;
    }
    
    
    
    
    @smstate (state = "ACTIVATEPOINT")
    public boolean st_activatePoint(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        String substate = payload.getCallerstate();
        
    
        switch(substate){
            
            case "REGISTERPOINT" :
                PointInfoCTX ptctx = registerPoint(payload.double1, true);
                if (ptctx != null){
                    Point pt = ptctx.getPoint();
                    if (pt.getDoses().get(0) instanceof Long){
                        pt.loadChildren(false, new SMTraffic(0l, 0l, 0, "ACTIVATEPOINT", this.getClass(),
                                                new VirnaPayload()
                                                    .setDouble1(payload.double1)
                                                    .setCallerstate("LOADPHASE3"))); 
                    }
                    else{
                        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "ACTIVATEPOINT", this.getClass(),
                                                new VirnaPayload()
                                                    .setCallerstate("PREPARECTX")
                                                    .setDouble1(payload.double1) ));  
                    }             
                }
                else{
                    LOG.severe(" No point to register !!!");
                }
                break;
                
            case "LOADPHASE3" :
                Entity ph3et = (Entity)payload.vobject;
                ph3et.loadChildren(false, null);
                appctrl.processSignal(new SMTraffic(0l, 0l, 0, "ACTIVATEPOINT", this.getClass(),
                                                new VirnaPayload()
                                                    .setCallerstate("UPDATEUI")
                                                    .setDouble1(payload.double1) )); 
                break;
            
            case "UPDATEUI" :
                PointInfoCTX uptctx = updatePoint(payload.double1);
                PointInfoController pic = (PointInfoController)anct.infopanes.get("pointinfo");
                uptctx.update();
                pic.update(uptctx);
                
                if (!anct.current_infopane.equals("pointinfo")){
                    anct.showInfoPane("pointinfo");
                }
                
//                ctx.current_auxdescriptor = uptctx.chdesc;
                anct.getAuxchart().refreshChart("point");
                
        }
    
        return true;
    }    
     
    
    public void activatePoint (Double Pressure){
        registerPoint(Pressure, true);    
    }
  
  
}

