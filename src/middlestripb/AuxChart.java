/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import cern.extjfx.chart.NumericAxis;
import cern.extjfx.chart.XYChartPane;
import cern.extjfx.chart.plugins.DataPointTooltip;
import cern.extjfx.chart.plugins.Panner;
import cern.extjfx.chart.plugins.Zoomer;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.SignalListener;
import com.opus.syssupport.VirnaPayload;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author opus
 */
public class AuxChart implements SignalListener{

    private static final Logger LOG = Logger.getLogger(AuxChart.class.getName());

//    private ObservableList<XYChart.Data<Number, Number>> buildp_points;
//    private ObservableList<XYChart.Data<Number, Number>> initstab_points;
//    private ObservableList<XYChart.Data<Number, Number>> endstab_points;
//    private ObservableList<XYChart.Data<Number, Number>> interpstab_points;
//    private ObservableList<XYChart.Data<Number, Number>> deltap_points;
//    
//    private XYChart.Series<Number, Number> buildp_series;
//    private XYChart.Series<Number, Number> initstab_series;
//    private XYChart.Series<Number, Number> endstab_series;
//    private XYChart.Series<Number, Number> interpstab_series;
//    private XYChart.Series<Number, Number> deltap_series;
    
    
    private LineChart<Number, Number> lineChart;
    private ObservableList<XYChart.Series<Number,Number>> chartdata;
    private NumericAxis xAxis;
    private NumericAxis yAxis;
    private NumericAxis auxAxis;
    private XYChart.Series<Number, Number> aux_series;
    
    
    public AuxChart() {
    }


    public XYChartPane createCernChart(){
    
        xAxis = new NumericAxis(0.0, 50, 5);
        xAxis.setAnimated(false);
        xAxis.setLabel("Time");

        yAxis = new NumericAxis(0.0, 65, 5);
        yAxis.setAnimated(false);
        yAxis.setLabel("Pressure");

        
        auxAxis = new NumericAxis(0.0, 0.5, 0.05);
        auxAxis.setAnimated(false);
        auxAxis.setLabel("Delta P");
        
        auxAxis.setSide(Side.RIGHT);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Line Chart Example");
        lineChart.setAnimated(false);
    
        chartdata = lineChart.getData(); 
        //XYChart.Series<Number, Number> sdata = chartdata.get(0);
        
        
        XYChartPane<Number, Number> chartPane = new XYChartPane<>(lineChart);
        chartPane.setLegendVisible(false);
        
        
        
        // Overlay LABEL =================================================
//        Label label = new Label("Info about chart data");
//        AnchorPane.setTopAnchor(label, 15.0);
//        AnchorPane.setLeftAnchor(label, 15.0);
//        AnchorPane anchorPane = new AnchorPane(label);
//        // Pass any mouse events to the underlying chart
//        anchorPane.setMouseTransparent(true);
        

        DataPointTooltip dptt = new DataPointTooltip();
        dptt.addSignalListener(this);
        

        chartPane.getPlugins().addAll(
                                new Zoomer(), 
                                new Panner(), 
                                dptt ); //, 
//                                        new ChartOverlay<>(OverlayArea.PLOT_AREA, anchorPane));
   
        return chartPane;
        
    }    
    
    
    public void refreshChart(){
        
        Platform.runLater(() -> {
            
            // Clear old data first
            if (chartdata != null) chartdata.clear();
            Context ctx = Context.getInstance();
  
//            xAxis = new NumericAxis(ctx.chart_xmin, ctx.chart_xmax, (ctx.chart_xmin - ctx.chart_xmax) / 10);
//            xAxis.setAnimated(false);
//            xAxis.setLabel("Time(msec)");
//
//            yAxis = new NumericAxis(ctx.chart_ymin, ctx.chart_ymax, (ctx.chart_ymin - ctx.chart_ymax) / 10);
//            yAxis.setAnimated(false);
//            yAxis.setLabel("Pressure mmHg");
            
//            lineChart = new LineChart<>(xAxis, yAxis);

            xAxis.setLowerBound(ctx.aux.xmin - 10);
            xAxis.setUpperBound(ctx.aux.xmax + (ctx.aux.xmax / 10));
//            xAxis.setTickUnit((ctx.chart_xmin - ctx.chart_xmax) / 10);
            xAxis.setLabel("Time(sec)");
            
            yAxis.setLowerBound(ctx.aux.ymin - (ctx.aux.ymin / 10));
            yAxis.setUpperBound(ctx.aux.ymax + (ctx.aux.ymax / 10));
            yAxis.setLabel("Pressure (mmHg)");
            
            aux_series = new XYChart.Series<>("Dose", ctx.aux.steps);
            lineChart.getData().add((javafx.scene.chart.XYChart.Series<Number, Number>)aux_series);
            
            chartdata = lineChart.getData(); 
            

        });
        
        
        
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
//            selectPoint(dp); 
            //LOG.info(String.format("Selecting Isotherm point"));
        }
        else if (signal.getCommand().equals("CLEARPOINT")){
            //LOG.info(String.format("Clearing Isotherm Point Selection"));
//            selectPoint(null); 
        }
        
        
    }
    
    
}
