/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import cern.extjfx.chart.NumericAxis;
import cern.extjfx.chart.XYChartPane;
import cern.extjfx.chart.plugins.ChartOverlay;
import cern.extjfx.chart.plugins.ChartOverlay.OverlayArea;
import cern.extjfx.chart.plugins.DataPointTooltip;
import cern.extjfx.chart.plugins.Panner;
import cern.extjfx.chart.plugins.XRangeIndicator;
import cern.extjfx.chart.plugins.XValueIndicator;
import cern.extjfx.chart.plugins.YRangeIndicator;
import cern.extjfx.chart.plugins.YValueIndicator;
import cern.extjfx.chart.plugins.Zoomer;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.SignalListener;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
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

    
    private LineChart<Number, Number> mainChart;
    private LineChart<Number, Number> suppChart;
    
    private XYChartPane<Number, Number> chartPane;
    private ObservableList<XYChart.Series<Number,Number>> chartdata;
    
    
    private NumericAxis xAxis;
    private NumericAxis yAxis;
    private XYChart.Series<Number, Number>main_series;
    
    private NumericAxis companionXAxis;
    private NumericAxis companionYAxis;
    private XYChart.Series<Number, Number> companion_series;
    
    public AuxChartOverlay clo;
    private Zoomer zoomer;
    private Panner panner;
    private DataPointTooltip dptt;
    
    public AuxChart() {
    }


    public XYChartPane createCernChart(){
    
        xAxis = new NumericAxis(0.0, 50, 5);
        getxAxis().setAnimated(false);
        getxAxis().setLabel("Time");
        getxAxis().setForceZeroInRange(false);
//        xAxis.setAutoRanging(true);
        
        yAxis = new NumericAxis(0.0, 65, 5);
        getyAxis().setAnimated(false);
        getyAxis().setLabel("Pressure");

        
        companionXAxis = new NumericAxis(0.0, 50, 5);
        getCompanionXAxis().setAnimated(false);
        getCompanionXAxis().setLabel("Time");
        
        companionYAxis = new NumericAxis(0.0, 5, 0.5);
        getCompanionYAxis().setAnimated(false);
        getCompanionYAxis().setLabel("Delta P");
        getCompanionYAxis().setSide(Side.RIGHT);
     
        
        mainChart = new LineChart<>(getxAxis(), getyAxis());
        mainChart.setAnimated(false);
        mainChart.setCreateSymbols(false);
    
        suppChart = new LineChart<>(getCompanionXAxis(), getCompanionYAxis());
        suppChart.setAnimated(false);
        suppChart.setCreateSymbols(false);
        
        
        
        chartdata = mainChart.getData(); 
        
        chartPane = new XYChartPane<>(mainChart);
        getChartPane().setLegendVisible(false);

        clo = new AuxChartOverlay();
        clo.setMouseTransparent(true);
        
        zoomer = new Zoomer();
        panner = new Panner();
        dptt = new DataPointTooltip();
        dptt.addSignalListener(this);
        

        getChartPane().getPlugins().addAll(zoomer, panner, dptt,
                                new ChartOverlay<>(OverlayArea.PLOT_AREA, clo ));
        
        return getChartPane();
        
    }    
    
    
    public void refreshChart(String type){
        
        
        Platform.runLater(() -> {
            
            // Clear old data first
            if (chartdata != null) chartdata.clear();
            
            Context ctx = Context.getInstance();
            AuxChartDescriptor cd = ctx.auxcharts.get(type);
  
            if (cd == null){
                LOG.severe("No chart descriptor available ;-( -> bail out !!!!");
                return;
            }
           
            getxAxis().setLowerBound(cd.xmin);
            getxAxis().setUpperBound(cd.xmax);
            if (cd.xtick != null) getxAxis().setTickUnit(cd.xtick);
            getxAxis().setLabel(cd.xlabel);
            
//            xAxis.setAutoRanging(true);
            
            getyAxis().setLowerBound(cd.ymin);
            getyAxis().setUpperBound(cd.ymax);
            if (cd.ytick != null) getyAxis().setTickUnit(cd.ytick);
            getyAxis().setLabel(cd.ylabel);
    
            
            for (String skey : cd.series.keySet()){
                if (skey.contains("main") || skey.contains("Doses")){
                    ObservableList<XYChart.Data<Number, Number>> serie = cd.series.get(skey);
                    main_series = new XYChart.Series<>(skey, serie);
                    ctx.auxmain_series = main_series.getData();
                    mainChart.getData().clear();
                    mainChart.getData().add((javafx.scene.chart.XYChart.Series<Number, Number>)main_series);
                }
            }
         
            
            getChartPane().getPlugins().clear();
            cd.overlay.clearMessages();
            getChartPane().getPlugins().addAll(zoomer, panner, dptt,
                                new ChartOverlay<>(OverlayArea.PLOT_AREA, cd.overlay ));
   
            
            for (YValueIndicator<Number> ind : cd.yvalindicators.values()){
                getChartPane().getPlugins().add(ind);
            }
            
            for (XValueIndicator<Number> ind : cd.xvalindicators.values()){
                getChartPane().getPlugins().add(ind);
            }
            
            for (XRangeIndicator<Number> ind : cd.xrangeindicators.values()){
                getChartPane().getPlugins().add(ind);
            }
            
            
            for (YRangeIndicator<Number> ind : cd.yrangeindicators.values()){
                getChartPane().getPlugins().add(ind);
            }
            
            
            getChartPane().getOverlayCharts().clear();
            if (!cd.auxlabel.equals("")){
                getCompanionYAxis().setLabel(cd.auxlabel);
                if (cd.auxmin != 0.0) getCompanionYAxis().setLowerBound(cd.auxmin);
                if (cd.auxmax != 0.0) getCompanionYAxis().setUpperBound(cd.auxmax);
                for (String skey : cd.series.keySet()){
                    if (skey.contains("companion")){
                        ObservableList<XYChart.Data<Number, Number>> serie = cd.series.get(skey);
                        companion_series = new XYChart.Series<>(skey, serie);
                        ctx.auxcompanion_series = companion_series.getData();
                        suppChart.getData().clear();
                        suppChart.getData().add((javafx.scene.chart.XYChart.Series<Number, Number>)companion_series);
                    }
                }
                getChartPane().getOverlayCharts().add(suppChart);
            }
            else{
                
            }
            
            if (companion_series != null){
                Node line = companion_series.getNode().lookup(".chart-series-line");
                line.setStyle("-fx-stroke: red;");
            }
            
            chartdata = mainChart.getData(); 
            
        });
       
    }
 

    
    // ============================================== SM STATES ================================================================
    
    @smstate (state = "AUXSHOWINDICATOR")
    public boolean st_showIndicator(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        Controller ctrl = Controller.getInstance();
        
        Context ctx = Context.getInstance();
        AuxChartDescriptor cd = ctx.auxcharts.get(payload.vstring);
        
        
        Platform.runLater(() -> {
            
            boolean artifactset = false;
            
            if (payload.objecttype.equals("XValue")){
                XValueIndicator<Number> ind = (XValueIndicator<Number>)payload.vobject;
                if (!cd.xvalindicators.containsKey(payload.getServicestatus())){
                    cd.xvalindicators.put(payload.getServicestatus(), ind);
                    getChartPane().getPlugins().add(ind);
                    artifactset = true;
                }
            }
            if (payload.objecttype.equals("YValue")){
                YValueIndicator<Number> ind = (YValueIndicator<Number>)payload.vobject;
                if (!cd.yvalindicators.containsKey(payload.getServicestatus())){
                    cd.yvalindicators.put(payload.getServicestatus(), ind);
                    getChartPane().getPlugins().add(ind);
                    artifactset = true;
                }
            }
            
            if (artifactset && payload.long1 != 0){
                SMTraffic alarm_config = new SMTraffic(0l, 1l, 0, "AUXCLEARINDICATOR", this.getClass(),
                        payload
                );
                ctrl.setAlarm (-1l, -4, alarm_config, payload.long1, 0);
            }
            
            if (!artifactset) LOG.info(String.format("Aux chart already has indicator %s", payload.getServicestatus()));
            
        });
        
//        LOG.info(String.format("Showing Indicator"));
        
        return true;
    }
    
    
    @smstate (state = "AUXCLEARINDICATOR")
    public boolean st_clearIndicator(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        
        Context ctx = Context.getInstance();
        AuxChartDescriptor cd = ctx.auxcharts.get(payload.vstring);
        
        Platform.runLater(() -> {
            
            boolean hasartifact = false;
            
            if (payload.objecttype.equals("XValue")){
//                XValueIndicator<Number> ind = (XValueIndicator<Number>)payload.vobject;
                if (cd.xvalindicators.containsKey(payload.getServicestatus())){
                    XValueIndicator<Number> ind = cd.xvalindicators.get(payload.getServicestatus());
                    cd.xvalindicators.remove(payload.getServicestatus());
                    getChartPane().getPlugins().remove(ind);
                    hasartifact = true;
                }
            }
            if (payload.objecttype.equals("YValue")){
                YValueIndicator<Number> ind = (YValueIndicator<Number>)payload.vobject;
                if (!cd.yvalindicators.containsKey(payload.getServicestatus())){
                    cd.yvalindicators.put(payload.getServicestatus(), ind);
                    getChartPane().getPlugins().add(ind);
                    hasartifact = true;
                }
            }
          
            
            if (!hasartifact) LOG.info(String.format("Aux chart has no indicator %s", payload.getServicestatus()));
            
            
        });
 
        return true;
    }
    
    
    @smstate (state = "AUXDOMESSAGES")
    public boolean st_doMessages(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        Controller ctrl = Controller.getInstance();
        
        Context ctx = Context.getInstance();
        AuxChartDescriptor cd = ctx.auxcharts.get(payload.vstring);
        
        Platform.runLater(() -> {
            if (payload.getFlag1()){
                cd.overlay.addMessage(payload.getServicestatus());
            }
            else{
                cd.overlay.clearMessages();
                if (!payload.getServicestatus().equals("")){
                    cd.overlay.addMessage(payload.getServicestatus());
                }
            }
        });
        
        return true;
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
        
//        if (signal.getCommand().equals("POINTSELECTED")){
//            VirnaPayload vp = signal.getPayload();
//            DataPointTooltip.DataPoint dp = (DataPointTooltip.DataPoint)vp.vobject;
////            selectPoint(dp); 
//            //LOG.info(String.format("Selecting Isotherm point"));
//        }
//        else if (signal.getCommand().equals("CLEARPOINT")){
//            //LOG.info(String.format("Clearing Isotherm Point Selection"));
////            selectPoint(null); 
//        }
                
    }

    public NumericAxis getxAxis() {
        return xAxis;
    }

    public NumericAxis getyAxis() {
        return yAxis;
    }

    public NumericAxis getCompanionXAxis() {
        return companionXAxis;
    }

    public NumericAxis getCompanionYAxis() {
        return companionYAxis;
    }

    public XYChartPane<Number, Number> getChartPane() {
        return chartPane;
    }
    
    
    
    
    
    
}








//            xAxis = new NumericAxis(ctx.chart_xmin, ctx.chart_xmax, (ctx.chart_xmin - ctx.chart_xmax) / 10);
//            xAxis.setAnimated(false);
//            xAxis.setLabel("Time(msec)");
//
//            yAxis = new NumericAxis(ctx.chart_ymin, ctx.chart_ymax, (ctx.chart_ymin - ctx.chart_ymax) / 10);
//            yAxis.setAnimated(false);
//            yAxis.setLabel("Pressure mmHg");
            
//            mainChart = new LineChart<>(xAxis, yAxis);


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
    