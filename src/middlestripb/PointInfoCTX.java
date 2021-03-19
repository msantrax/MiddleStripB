/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Dose;
import Entities.Point;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author opus
 */
public class PointInfoCTX {

    private static final Logger LOG = Logger.getLogger(PointInfoCTX.class.getName());

    
    private Point point;
    private Context ctx;
  
    public Double pressure;
    public ZoneId zoneid;
    public LocalDateTime init_ts;
    public LocalDateTime end_ts;
    public Duration duration;
    
    public ArrayList<DosesTblRow> dosestbl;
    
    
    public AuxChartDescriptor aux;
    
    public boolean updated = false;
    
    
    public PointInfoCTX(Double pressure) {
        this.pressure = pressure;
        zoneid = ZoneId.systemDefault();
        dosestbl = new ArrayList<>();
        aux = new AuxChartDescriptor();
        ctx = Context.getInstance();
    }
   
    
    public PointInfoCTX setTempo (Long init, Long end){
        
        init_ts = LocalDateTime.ofInstant(Instant.ofEpochMilli(init), ZoneId.systemDefault());
        end_ts = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());
        duration = Duration.between(init_ts, end_ts);
        
        return this;
    }

    public Point getPoint() {
        return point;
    }

    public PointInfoCTX setPoint(Point point) {
        this.point = point;
        return this;
    }

    
    public void update(){
       
        if (updated) return;
        
        ArrayList<Dose> doses = point.getObjDoses();
        
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        Long chts_start  = 0L;
        Long chts_end = 0L;
        
        
        String start, end, type;
        Double lastp = 0.0;
        String deltat = "0.0";
        
        
        for (int i = 0; i < doses.size(); i++) {
            Dose d = doses.get(i);
            type = "G";
            if (d.getRedose()) type = "R";
            if (d.getEquilibrium()) type = "E";
            
            start = i==0 ? String.format("%6.3f", point.getP_Start()) : String.format("%6.3f", d.getAchieved()) ;
            lastp = type.equals("R") ? d.getRedose_P2() : point.getP_End() ;
            end  = String.format("%6.3f", lastp);
            
            Long dt = ((d.getTs_Stabend()/1000) - (d.getTs_Init()/1000));
            deltat = String.format("%d", dt);
            
            dosestbl.add(new DosesTblRow(
                    String.format("%d", i),
                    type,
                    start,
                    end,
                    deltat,
                    d,
                    this
            ));
            
            if (i == 0) chts_start = doses.get(0).getTs_Init();
            if (i == 0) aux.ymin = lastp;
            
            data.add(new XYChart.Data<Number, Number>(ctx.getSecTS(d.getTs_Init(), chts_start), lastp));
            data.add(new XYChart.Data<Number, Number>(ctx.getSecTS(d.getTs_Ach(), chts_start), d.getAchieved()));
            data.add(new XYChart.Data<Number, Number>(ctx.getSecTS(d.getTs_Stabinit(), chts_start), d.getAchieved()));
            data.add(new XYChart.Data<Number, Number>(ctx.getSecTS(d.getTs_Stabend(), chts_start), lastp));
            
            aux.xmax = ctx.getSecTS (d.getTs_Stabend(), chts_start) ;
            
            int maxflag = Double.compare(d.getAchieved() , (Double)aux.ymax);
            aux.ymax = maxflag > 0  ? d.getAchieved() : aux.ymax  ;
            
            int minflag = Double.compare(lastp , (Double)aux.ymin);
            aux.ymin = minflag < 0  ? lastp : aux.ymin;
            
            
        }
        
        aux.steps = FXCollections.observableArrayList(data);
        
        updated = true;
        
    }
    
}



