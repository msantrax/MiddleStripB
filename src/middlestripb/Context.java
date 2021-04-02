/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Isotherm;
import Entities.Point;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

/**
 *
 * @author opus
 */
public class Context {

    private static final Logger LOG = Logger.getLogger(Context.class.getName());

    private static Context instance; 
    public static Context getInstance(){
        if (instance == null) {instance = new Context();}
        return instance;
    }
    
    private LinkedHashMap<Double, PointInfoCTX> points_ctx;
    
    
    public LinkedHashMap<String, AuxChartDescriptor> auxcharts;
    public AuxChartDescriptor current_auxdescriptor;

    public LinkedHashMap<String, BaseAnaTask> anatasks;
    public BaseAnaTask current_anatask;
    
    
  
    
    public Context() {
        points_ctx = new LinkedHashMap<>();
        auxcharts = new LinkedHashMap<>();
        anatasks = new LinkedHashMap<>();
    }
    
    private ASVPDevice asvpdev;
    public void setAsvpdev (ASVPDevice dev) { this.asvpdev = dev;}
    
    
    
    // ========================================== DATA REDIRECTION ===========================================================
 
    private List<XYChart.Data<Number, Number>> maindata = new ArrayList<>();
    private List<XYChart.Data<Number, Number>> companiondata = new ArrayList<>();
    
    public ObservableList<Data<Number, Number>> auxmain_series;
    public ObservableList<Data<Number, Number>> auxcompanion_series;
    
 
    
    // ========================================== ISOTHERM DATA ==============================================================
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
        
        chdesc = auxcharts.get("isotimedomain");
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
            ts = getSecTS (isopoint.getStart_Ts(), init_ts);
            data.add(new XYChart.Data<Number, Number>(ts, pressure));

            pressure = isopoint.getP_End();
            ts = getSecTS (isopoint.getEnd_Ts(), init_ts);
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

        chdesc.series.put("main_data", FXCollections.observableArrayList(data));
        
        chdesc.overlay.addMessage(String.format("Loaded %d points", isopoints.size()));
        chdesc.overlay.addMessage("No companion Yaxis requested");

//        chdesc.addYVal ("maxpoint", "Target : 720 mmHg", 200.45, null, 0.2);
//        chdesc.addXVal ("maxpoint", "Event", 20000.0, null);

//        chdesc.addYRange ("rangey", "Range Y", 0.0, 122.56 , null, null, null);
//        chdesc.addXRange ("rangex", "Range X", 10000.0, 15000.0 , null, null);
        
        chdesc.auxlabel = "\u0394P̣";
        
        
        chdesc.dirty = false;
        auxcharts.put("isotimedomain", chdesc);  
                
    }
    
    
    // ============================================== TASK MANAGEMENT ===========================================================
    
    public void switchTask (String id){
        
        BaseAnaTask tsk = this.anatasks.get(id);
        if (tsk != null){
            tsk.Go();
            this.current_anatask = tsk;
        }
        
    }
    
    
   
    // ============================================ ISO POINT SERVICES =========================================================
    
    public Double getSecTS (Long end, Long init){
        
        Double dbl = (Long.valueOf(end - init).doubleValue()) / 1000 ;
        return dbl;
    }
    
    
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
    
    
    
    
    
    
 
    
    
}
