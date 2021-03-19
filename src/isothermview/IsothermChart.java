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
//import cern.extjfx.chart.XYChart;
import cern.extjfx.chart.plugins.DataPointTooltip;
import cern.extjfx.chart.plugins.Panner;
import cern.extjfx.chart.plugins.Zoomer;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.SignalListener;
import com.opus.syssupport.VirnaPayload;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import middlestripb.Context;
import middlestripb.Controller;
import middlestripb.FX1Controller;

/**
 *
 * @author opus
 */
public class IsothermChart implements SignalListener {

    private static final Logger LOG = Logger.getLogger(IsothermChart.class.getName());

    
    private FX1Controller anct;
    private Controller ctrl;
    private Context ctx;
    
    private ObservableList<XYChart.Data<Number, Number>> adspoints;
    private ObservableList<XYChart.Data<Number, Number>> despoints;
    private XYChart.Series<Number, Number> des_series;
    private XYChart.Series<Number, Number> ads_series;
    
    
    private NumericAxis yAxis;
    private NumericAxis xAxis;
    
    private LineChart<Number, Number> lineChart;

    
    private Node selectedNode;
    
    
    
    public IsothermChart(FX1Controller anct) {
        this.anct = anct;
        this.ctrl = Controller.getInstance();
        this.ctx = Context.getInstance();
    }
    
    
    private final String selectedStyle = "-fx-background-color: red, red;" ;

    
    private void changeSymbol (XYChart.Series<Number,Number> series, int index, String style){
        
        ObservableList <Data<Number, Number>> sdataseries = series.getData();
        Data sdata = sdataseries.get(index);
        Node sdn = sdata.getNode();
        Node symbol = sdn.lookup(".chart-line-symbol");
        String oldstyle = symbol.getStyle();
        if (style != null){
            symbol.setStyle(style);
        }
        else {
            symbol.setStyle("");
        }
    }
    
    private void selectPoint (DataPointTooltip.DataPoint dp){
        
        if (dp != null){
            
            if (selectedNode != null){
                //Node symbol = selectedNode.lookup(".chart-line-symbol");
                selectedNode.setStyle("");
                selectedNode = null;
            }
            
            Data sdata = dp.getData();
            Node sdn = sdata.getNode();
            sdn.setStyle(selectedStyle);
            selectedNode = sdn;
            Double pressure = (Double)dp.getData().getXValue();
            ctrl.processSignal(new SMTraffic(0l, 0l, 0, "ACTIVATEPOINT", this.getClass(),
                                   new VirnaPayload()
                                           .setCallerstate("REGISTERPOINT")
                                           .setDouble1(pressure)));
        }
        else{
            
            if (selectedNode != null){
                //Node symbol = selectedNode.lookup(".chart-line-symbol");
                selectedNode.setStyle("");
                selectedNode = null;
            }
            
            anct.showInfoPane("isotherminfo");
            ctx.getAuxIso();
            ctx.aux = ctx.isoaux;
            anct.getAuxchart().refreshChart();
        }
        
    }
    
    
    public void refreshChart(){
        
        Platform.runLater(() -> {
            
            // Clear old data first
            lineChart.getData().clear();
            Context ctx = Context.getInstance();
            
            adspoints = ctx.getIsoPoints( true, true, true);
            ads_series = new XYChart.Series<>("Adsorption", adspoints);
            lineChart.getData().add((javafx.scene.chart.XYChart.Series<Number, Number>)ads_series);
//            Node n1 = ads_series.nodeProperty().get();
//            n1.setStyle("-fx-stroke: red; -fx-stroke-width: 1px;");

            Node sdn = ads_series.getNode();
            sdn.setOnMouseClicked(e -> System.out.println("Click on series"));

            despoints = ctx.getIsoPoints( false, true, true);
            des_series = new XYChart.Series<>("Desorption", despoints);
            lineChart.getData().add(des_series);
//            Node n2 = des_series.nodeProperty().get();
//            n2.setStyle("-fx-stroke-width: 1px;");

            ObservableList<XYChart.Series<Number,Number>> chartdata = lineChart.getData(); 
            //XYChart.Series<Number, Number> sdata = chartdata.get(0);

        });
        
    }
    
    
    public XYChartPane createCernChart(){
    
        
        xAxis = new NumericAxis(0.0, 1.2, 0.05);
        //xAxis = new NumericAxis(0.0, 1.02, 0.05);
        xAxis.setAnimated(false);
        xAxis.setLabel("Pressure  P0/Po");
        xAxis.setAutoRanging(false);

        
        yAxis = new NumericAxis(0.0, 300, 20);
        //yAxis = new NumericAxis(0.0, 180, 10);
        yAxis.setAnimated(false);
        yAxis.setLabel("Volume @ STP (cm³/g)");

        lineChart = new LineChart<>(xAxis, yAxis);
        
        lineChart.setAnimated(false);
    
        lineChart.setTitle("Line Chart Example");
        
        
        XYChartPane<Number, Number> chartPane = new XYChartPane<>(lineChart);
        chartPane.setLegendVisible(false);
        
        
        // Overlay LABEL =================================================
        Label label = new Label("Info about chart data");
        AnchorPane.setTopAnchor(label, 15.0);
        AnchorPane.setLeftAnchor(label, 15.0);
        AnchorPane anchorPane = new AnchorPane(label);
        // Pass any mouse events to the underlying chart
        anchorPane.setMouseTransparent(true);
        

        DataPointTooltip dptt = new DataPointTooltip();
        dptt.addSignalListener(this);
        

        chartPane.getPlugins().addAll(
                                new Zoomer(), 
                                new Panner(), 
                                dptt , 
                              new ChartOverlay<>(OverlayArea.PLOT_AREA, anchorPane));
   
        return chartPane;
        
    }
    
    
    
    public boolean addPoint (boolean ads, Double p, Double v){
        
        XYChart.Data<Number, Number> point = new XYChart.Data<>(p, v);
        
        boolean res = ads ? adspoints.add(point) : despoints.add(point);
        return res;
        
    }
 
    
    @Override
    public Long getContext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long getUID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    @Override
    public void processSignal(SMTraffic signal) {
        
        if (signal.getCommand().equals("POINTSELECTED")){
            VirnaPayload vp = signal.getPayload();
            DataPointTooltip.DataPoint dp = (DataPointTooltip.DataPoint)vp.vobject;
            selectPoint(dp); 
            LOG.info(String.format("Selecting Isothermv point"));
        }
        else if (signal.getCommand().equals("CLEARPOINT")){
            LOG.info(String.format("Clearing Isothermv Point Selection"));
            selectPoint(null); 
        }
        
        
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




// 
//    private final String diamondStyle = "-fx-background-radius: 0; \n" +
//                        "-fx-padding: 7px 5px 7px 5px; \n "+
//                        "-fx-shape: \"M5,0 L10,9 L5,18 L0,9 Z\";\n";
//    
//    
//    private final String selectedStyle = "-fx-background-color: red, red;" ;
////                "-fx-background-radius: 16px;\n -fx-padding: 3px;" ;
////                "-fx-shape: \"M0,4 L2,4 L4,8 L7,0 L9,0 L4,11 Z\";\n" ;
////                "-fx-shape: \"M5,0 L10,9 L5,18 L0,9 Z\";\n" +
////                "-fx-shape: \"M20,0 L40,32 L0,32 Z\";\n" +
////                "-fx-background-insets: 0, 2;\n" +
////                "-fx-background-radius: 0px;\n" +
////                "-fx-padding: 7px 5px 7px 5px;";



//private void applySeriesStyle(XYChart.Series<Number,Number> series, String linestyle, String symbolstyle){
//        
//        Node n1 = series.nodeProperty().get();
//        Node line = n1.lookup(".chart-series-line");
//        
//        ObservableList<String> styles = n1.getStyleClass();
//        styles.set(2, ".default-color1");         
//    }



   //    public void loadIsothermPoints(ASVPDevice asvpdev){
//        
//        if ( asvpdev.getIsotherm()!= null){
//            
//            Platform.runLater(() -> {
//                // Clear old data first
//                lineChart.getData().clear();
//                
//                
//                adspoints = asvpdev.getIsoPoints( true, true, true);
//                ads_series = new XYChart.Series<>("Adsorption", adspoints);
//                lineChart.getData().add((javafx.scene.chart.XYChart.Series<Number, Number>)ads_series);
//        //        Node n1 = ads_series.nodeProperty().get();
//        //        n1.setStyle("-fx-stroke: green; -fx-stroke-width: 1px;");
//
//                Node sdn = ads_series.getNode();
//                sdn.setOnMouseClicked(e -> System.out.println("Click on series"));
//
//                despoints = asvpdev.getIsoPoints( false, true, true);
//                des_series = new XYChart.Series<>("Desorption", despoints);
//                lineChart.getData().add(des_series);
//                Node n2 = des_series.nodeProperty().get();
//                n2.setStyle("-fx-stroke-width: 1px;");
//
//                ObservableList<XYChart.Series<Number,Number>> chartdata = lineChart.getData(); 
//                //XYChart.Series<Number, Number> sdata = chartdata.get(0);
//     
//            });
//   
//        }
//       
//    }