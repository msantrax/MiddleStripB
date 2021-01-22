/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;

import cern.extjfx.chart.NumericAxis;
import cern.extjfx.chart.XYChartPane;
import cern.extjfx.chart.plugins.ChartOverlay;
import cern.extjfx.chart.plugins.ChartOverlay.OverlayArea;
import cern.extjfx.chart.plugins.DataPointTooltip;
import cern.extjfx.chart.plugins.Panner;
import cern.extjfx.chart.plugins.Zoomer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author opus
 */
public class IsothermChart {

    private static final Logger LOG = Logger.getLogger(IsothermChart.class.getName());

    
    private IsothermBean isobean;
    private Isotherm isotherm;
    
    private ObservableList<XYChart.Data<Number, Number>> adspoints;
    private ObservableList<XYChart.Data<Number, Number>> despoints;
    
    
    private NumericAxis yAxis;
    private NumericAxis xAxis;

    
    public IsothermChart(Isotherm isotherm) {
        this.isotherm = isotherm;
        this.isobean = isotherm.getIsothermBean();
    }
    
    
    public XYChartPane createCernChart(){
    
        xAxis = new NumericAxis(0.0, 1.02, 0.05);
        //xAxis = new LogarithmicAxis();
        xAxis.setAnimated(false);
        xAxis.setLabel("Pressure  P0/Po");

        yAxis = new NumericAxis(0.0, 180, 10);
        //yAxis = new LogarithmicAxis();
        yAxis.setAnimated(false);
        yAxis.setLabel("Volume");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Line Chart Example");
        lineChart.setAnimated(false);
    
        
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>("Adsorption", isotherm.getAdspoints());
        lineChart.getData().add(series1);
        Node n1 = series1.nodeProperty().get();
        n1.setStyle("-fx-stroke-width: 0px;");
    
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>("Desorption", isotherm.getDespoints());
        lineChart.getData().add(series2);
        Node n2 = series2.nodeProperty().get();
        n2.setStyle("-fx-stroke-width: 2px;");
        
        
        
        XYChartPane<Number, Number> chartPane = new XYChartPane<>(lineChart);
        chartPane.setLegendVisible(false);
        
        
        
        // Overlay LABEL =================================================
//        Label label = new Label("Info about chart data");
//        AnchorPane.setTopAnchor(label, 15.0);
//        AnchorPane.setLeftAnchor(label, 15.0);
//        AnchorPane anchorPane = new AnchorPane(label);
//        // Pass any mouse events to the underlying chart
//        anchorPane.setMouseTransparent(true);
        
        chartPane.getPlugins().addAll(new Zoomer(), 
                                        new Panner(), 
                                        new DataPointTooltip() ); //, 
//                                        new ChartOverlay<>(OverlayArea.PLOT_AREA, anchorPane));
   
        return chartPane;
        
    }
    
    
    public boolean addPoint (boolean ads, Double p, Double v){
        
        XYChart.Data<Number, Number> point = new XYChart.Data<>(p, v);
        
        boolean res = ads ? adspoints.add(point) : despoints.add(point);
        return res;
        
    }
    
    
    
    private ObservableList<XYChart.Data<Number, Number>> getIsoPoints(boolean adsorption, 
                                                                        boolean ppo, 
                                                                        boolean volg
    
                                                                        ) {
        
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        ArrayList<IsothermPoint>isopoints = isobean.points;
        
        for (IsothermPoint isopoint : isopoints){
            if (adsorption == isopoint.isAdsorption()) {
                Double pressure = ppo ? isopoint.getPpo() : isopoint.getPo();
                Double volume = volg ? isopoint.getVolume_g() : isopoint.getVolume();
                XYChart.Data<Number, Number> d = new XYChart.Data<>(pressure, volume);
                data.add(new XYChart.Data<>(pressure, volume));
            }
        }
        
        return FXCollections.observableArrayList(data);
    
    }
    
    
    
    
}



//public ObservableList<Map<String, Object>> getMapData(int mode){
//      
//        final AtomicInteger count = new AtomicInteger(0); 
//        ObservableList<Map<String, Object>> items = FXCollections.<Map<String,Object>>observableArrayList();
//        
//        ArrayList<IsothermPoint>isopoints = isobean.points;
//        
//        for (IsothermPoint isopoint : isopoints){
//            Map<String, Object> map = new HashMap<>();
//            map.put("id_key", count.incrementAndGet());
//            map.put("selected_key", Boolean.valueOf(isopoint.isAdsorption()));
//            map.put("pointtype_key", isopoint.isAdsorption() ? 0.0 : 1.0);
//            map.put("pressure_key", isopoint.getPpo());
//            map.put("volume_key", isopoint.getVolume_g());
//            map.put("calculated_key", Double.valueOf(0.0));
//            map.put("comment_key", "comment");
//            items.add(map);
//        }
//        return items;
//        
//    }



//private ObservableList<XYChart.Data<Number, Number>> createData() {
//        
//        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
//        
//        for (Map<String, Object> entry : items) {    
//            Boolean selected = (Boolean)entry.get("selected_key");
//            if (selected){
//                Double pressure = (Double)entry.get("pressure_key");
//                Double volume = (Double)entry.get("volume_key");
//                XYChart.Data<Number, Number> d = new XYChart.Data<>(pressure, volume);
//                
//                data.add(new XYChart.Data<>(pressure, volume));
//            }
//        }
//        return FXCollections.observableArrayList(data);
//    }