/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import cern.extjfx.chart.NumericAxis;
import cern.extjfx.chart.plugins.XRangeIndicator;
import cern.extjfx.chart.plugins.XValueIndicator;
import cern.extjfx.chart.plugins.YRangeIndicator;
import cern.extjfx.chart.plugins.YValueIndicator;
import java.util.LinkedHashMap;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.chart.XYChart;

/**
 *
 * @author opus
 */
public class AuxChartDescriptor {

    
    public LinkedHashMap<String, ObservableList<XYChart.Data<Number, Number>>> series;
    
    
    public Double xmin = 0.0;
    public Double xmax = 0.0;
    public Double ymin = 0.0;
    public Double ymax = 0.0;
    public Double auxmin = 0.0;
    public Double auxmax = 0.0;
    
    public Double xtick = null;
    public Double ytick = null;
    public Double auxtick = null;
    
    
    public String xlabel = "";
    public String ylabel = "";
    public String auxlabel = "";
    
    
    public Boolean dirty = true;
    
    
    // Overlays & indicators
    public AuxChartOverlay overlay;
    
    public LinkedHashMap<String, YValueIndicator<Number>> yvalindicators;
    public LinkedHashMap<String, XValueIndicator<Number>> xvalindicators;
    public LinkedHashMap<String, YRangeIndicator<Number>> yrangeindicators;
    public LinkedHashMap<String, XRangeIndicator<Number>> xrangeindicators;
    
    
    public AuxChartDescriptor() {
    
       series = new LinkedHashMap<>();
       overlay = new AuxChartOverlay();
       overlay.setMouseTransparent(true);
       
       yvalindicators = new LinkedHashMap<>();  
       xvalindicators = new LinkedHashMap<>();
       yrangeindicators = new LinkedHashMap<>();
       xrangeindicators = new LinkedHashMap<>();
       
    }
    
    
    
    public static YValueIndicator<Number>YValIndicatorFactory (String id, String label, Double value, NumericAxis axis, Double pos){
        
        YValueIndicator<Number> indicator;
        Double position = pos == null ? 0.1 : pos;
        
        if (axis == null){
            indicator = new YValueIndicator<>(value, label);
        }
        else{
            indicator = new YValueIndicator<>(value, label, axis);
        }
        indicator.setLabelPosition(position);
        
        return indicator;
    }
    
    
    public void addYVal (String id, String label, Double value, NumericAxis axis, Double pos){
        
        YValueIndicator<Number> indicator = YValIndicatorFactory (id, label, value, axis, pos);
        this.yvalindicators.put(id, indicator);   
    }
    
    
    public static XValueIndicator<Number> XValIndicatorFactory (String id, String label, Double value, Double pos){
        
        XValueIndicator<Number> indicator;
        Double position = pos == null ? 0.1 : pos;
        
        indicator = new XValueIndicator<>(value, label);
        indicator.setLabelPosition(position);
        
        return indicator;
    }
    
    public void addXVal (String id, String label, Double value, Double pos){
 
        XValueIndicator<Number> indicator = XValIndicatorFactory (id, label, value, pos);
        this.xvalindicators.put(id, indicator);          
    }
    
    
    
    public static YRangeIndicator<Number> YRangeIndicatorFactory (String id, String label, 
                                                Double bottom, Double top , NumericAxis axis, 
                                                Double vpos, Double hpos){
        
        YRangeIndicator<Number> range;
        Double vposition = vpos == null ? 0.95 : vpos;
        Double hposition = hpos == null ? 0.95 : hpos;
        
        if (axis == null){
            range = new YRangeIndicator<>(bottom, top, label);
        }
        else{
            range = new YRangeIndicator<>(bottom, top, axis);
        }
        
        range.setLabelHorizontalAnchor(HPos.RIGHT);
        range.setLabelHorizontalPosition(0.95);
        range.setLabelVerticalAnchor(VPos.TOP);
        range.setLabelVerticalPosition(0.95);
        
        return range;
        
    }
    
    public void addYRange (String id, String label, Double bottom, Double top , NumericAxis axis, Double vpos, Double hpos){
        
        YRangeIndicator<Number> range = YRangeIndicatorFactory(id, label, bottom, top, axis, vpos, hpos);
        this.yrangeindicators.put(id, range);   
        
    }
    
    
    public static XRangeIndicator<Number> XRangeIndicatorFactory (String id, String label, 
                                                Double bottom, Double top, 
                                                Double vpos, Double hpos){
        
        XRangeIndicator<Number> range;
        Double vposition = vpos == null ? 0.95 : vpos;
        Double hposition = hpos == null ? 0.95 : hpos;
        
        range = new XRangeIndicator<>(bottom, top, label);
        
        range.setLabelHorizontalAnchor(HPos.RIGHT);
        range.setLabelHorizontalPosition(0.95);
        range.setLabelVerticalAnchor(VPos.TOP);
        range.setLabelVerticalPosition(0.95);
        
        return range;
    }
    
    
    public void addXRange (String id, String label, Double bottom, Double top , Double vpos, Double hpos){
        
        XRangeIndicator<Number> range = XRangeIndicatorFactory(id, label, bottom, top, vpos, hpos);
        this.xrangeindicators.put(id, range);   
        
    }
    
    
    
    
}
