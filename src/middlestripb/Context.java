/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Isotherm;
import Entities.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

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
    
    private Isotherm isotherm;
    
    private LinkedHashMap<Double, PointInfoCTX> points_ctx;
    
    public AuxChartDescriptor aux;
    public AuxChartDescriptor isoaux;
    public boolean isoaux_loaded = false;
    
    
    public Context() {
        points_ctx = new LinkedHashMap<>();
    }
    
    
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
    
    
    public ObservableList<XYChart.Data<Number, Number>> getIsoPoints(boolean adsorption, boolean ppo, boolean volg) {
      
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

    
    
    public void getAuxIso() {
      
        if (isoaux_loaded) return; 
        
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        ArrayList<Point>isopoints = getIsotherm().getObjPoints();
        isoaux = new AuxChartDescriptor();
        
        Double pressure, ts;
        Long init_ts = isopoints.get(0).getStart_Ts();
       
        
        for (Point isopoint : isopoints){
                
            pressure = isopoint.getP_Start();
            ts = getSecTS (isopoint.getStart_Ts(), init_ts);
            data.add(new XYChart.Data<Number, Number>(ts, pressure));

            pressure = isopoint.getP_End();
            ts = getSecTS (isopoint.getEnd_Ts(), init_ts);
            data.add(new XYChart.Data<Number, Number>(ts, pressure));
        
            isoaux.xmax = ts;
            int maxflag = Double.compare(pressure , (Double)isoaux.ymax);
            isoaux.ymax = maxflag > 0  ? pressure : isoaux.ymax  ;
            
        }
        
        isoaux.steps = FXCollections.observableArrayList(data);
        isoaux_loaded = true;
        
    }
    
    
    public Isotherm getIsotherm() {
        return isotherm;
    }

    public void setIsotherm(Isotherm isotherm) {
        this.isotherm = isotherm;
    }
    
    
    
    
    
    
    
}
