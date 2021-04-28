/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import cern.extjfx.chart.NumericAxis;
import cern.extjfx.chart.XYChartPane;
import cern.extjfx.chart.plugins.ChartOverlay;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author opus
 */
public class PrepChart implements SignalListener {

    private static final Logger LOG = Logger.getLogger(PrepChart.class.getName());
    
    
    private PrepController prepctrl;
    private Controller ctrl;
    private Context ctx;

    
    private ObservableList<XYChart.Data<Number, Number>> pathpoints;
    private ObservableList<XYChart.Data<Number, Number>> measpoints;
    private XYChart.Series<Number, Number> path_series;
    private XYChart.Series<Number, Number> meas_series;
    
    private NumericAxis yAxis;
    private NumericAxis xAxis;
    
    private LineChart<Number, Number> lineChart;
    protected XYChartPane<Number, Number> chartPane;
    
    
    private Node selectedNode;
    
    
    public PrepChart(PrepController prepctrl) {
        this.prepctrl = prepctrl;
        this.ctrl = Controller.getInstance();
    }
    
    
    public XYChartPane createCernChart(int widht, int height){
    
        xAxis = new NumericAxis(-50.0, 3650.0, 100.0);
        xAxis.setAnimated(false);
        xAxis.setLabel("Time Sec.");
        xAxis.setAutoRanging(false);
        //xAxis.setForceZeroInRange(false);
        
        yAxis = new NumericAxis(0.0, 300, 20);
        //yAxis = new NumericAxis(0.0, 180, 10);
        yAxis.setAnimated(false);
        yAxis.setLabel("Temperature °C");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setTitle("Preparation Chart");
        
        
        chartPane = new XYChartPane<>(lineChart);
        getChartPane().setPrefWidth(widht);
        getChartPane().setPrefHeight(height);
        getChartPane().setLegendVisible(false);
        
        
        // Overlay LABEL =================================================
        Label label = new Label("Preparation chart");
        AnchorPane.setTopAnchor(label, 15.0);
        AnchorPane.setLeftAnchor(label, 15.0);
        AnchorPane anchorPane = new AnchorPane(label);
        // Pass any mouse events to the underlying chart
        anchorPane.setMouseTransparent(true);
        

        DataPointTooltip dptt = new DataPointTooltip();
        dptt.addSignalListener(this);
        

        getChartPane().getPlugins().addAll(
                                new Zoomer(), 
                                new Panner(), 
                                dptt , 
                              new ChartOverlay<>(ChartOverlay.OverlayArea.PLOT_AREA, anchorPane));
   
        return getChartPane();
        
    }
    
    
    public void refreshChart(){
        
        Platform.runLater(() -> {
            
            this.ctx = Context.getInstance();
            
            // Clear old data first
            lineChart.getData().clear();
            Context ctx = Context.getInstance();
            
            pathpoints = ctx.getPrepPoints("prep1_path");
            path_series = new XYChart.Series<>("Prog Time/Temp :", pathpoints);
            lineChart.getData().add((javafx.scene.chart.XYChart.Series<Number, Number>)path_series);
//            Node n1 = ads_series.nodeProperty().get();
//            n1.setStyle("-fx-stroke: red; -fx-stroke-width: 1px;");
            Node sdn = path_series.getNode();
            sdn.setOnMouseClicked(e -> System.out.println("Click on series"));

            measpoints = ctx.getPrepPoints("prep1_meas");
            meas_series = new XYChart.Series<>("Measured point", measpoints);
            
            lineChart.getData().add(meas_series);
//            Node n2 = des_series.nodeProperty().get();
//            n2.setStyle("-fx-stroke-width: 1px;");

            ObservableList<XYChart.Series<Number,Number>> chartdata = lineChart.getData(); 
            //XYChart.Series<Number, Number> sdata = chartdata.get(0);

        });
        
    }

    public XYChartPane<Number, Number> getChartPane() {
        return chartPane;
    }
    
    
    
    
    
    
    // ================================== SIGNAL PROCESSING TO POINT SELECTIONS SERVICES ======================================
    
    @Override
    public Long getContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getUID() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
    @Override
    public void processSignal(SMTraffic signal) {
        
        if (signal.getCommand().equals("POINTSELECTED")){
            VirnaPayload vp = signal.getPayload();
            DataPointTooltip.DataPoint dp = (DataPointTooltip.DataPoint)vp.vobject;
            //selectPoint(dp); 
            LOG.info(String.format("Selecting Isothermv point"));
        }
        else if (signal.getCommand().equals("CLEARPOINT")){
            LOG.info(String.format("Clearing Isothermv Point Selection"));
            //selectPoint(null); 
        }
        
    }
    
    
}
