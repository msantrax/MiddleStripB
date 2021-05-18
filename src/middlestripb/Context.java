/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.CalcP0;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
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
    
    public LinkedHashMap<String, AuxChartDescriptor> auxcharts;
    public AuxChartDescriptor current_auxdescriptor;

    public LinkedHashMap<String, BaseAnaTask> anatasks;
    public BaseAnaTask current_anatask;
    
    public LinkedHashMap<BaseAnaTask, JournalSideNode> journals;
    public JournalSideNode current_journal;
    
    public LinkedHashMap<String, SamplePanelController> samplepanels;
    public SamplePanelController current_samplepanel;
    
    
    //public CalcP0 calcp0;
    
    
    public Context() {
        
        auxcharts = new LinkedHashMap<>();
        anatasks = new LinkedHashMap<>();
        journals = new LinkedHashMap<>();
        samplepanels = new LinkedHashMap<>();
        
    }
    
    private ASVPDevice asvpdev;
    public void setAsvpdev (ASVPDevice dev) { this.asvpdev = dev;}
    public ASVPDevice getAsvpdev() {return this.asvpdev;}
    
    private FX1Controller anct;
    public void setFXController (FX1Controller anct) { this.anct = anct;}
    public FX1Controller getFXController() {return anct;}
    
    
    // ========================================== DATA REDIRECTION ===========================================================
    public ObservableList<Data<Number, Number>> auxmain_series;
    public ObservableList<Data<Number, Number>> auxcompanion_series;
    
    
    // ============================================== CONTEXT INSPECTION =======================================================
    
    public boolean isP0Updated(long timeout) { 
        
        return true;
    }
    
    
    // ============================================== TASK MANAGEMENT ===========================================================
    
    public void switchTask (String id){
        
        BaseAnaTask tsk = this.anatasks.get(id);
        if (tsk != null){
            if (this.current_anatask == tsk){
                tsk.Restart();
            }
            else{
                tsk.prepareGo();
                this.current_anatask = tsk;
            }
            
        } 
    }
    
    public void initDefaultTasks(){
        
        anatasks.put("roottask", new RootTask(asvpdev, this, "/home/opus/ASVPANA/Scripts/roottask"));
        anatasks.put("checkp0task", new CheckP0AnaTask(asvpdev, this, "/home/opus/ASVPANA/Scripts/checkp0"));
        
        current_anatask = anatasks.get("roottask");
        
    }
    
    
    
    
    
    
    public Double getSecTS (Long end, Long init){
        
        Double dbl = (Long.valueOf(end - init).doubleValue()) / 1000 ;
        return dbl;
    }
    
    
    
    
    public ObservableList<XYChart.Data<Number, Number>> getPrepPoints(String id) {
      
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
  
        // Find what point
//        ArrayList<Point>chartpoints = getIsotherm().getObjPoints();
        
        // Some dummy data to test ...
        if (id.equals("prep1_path")){
            data.add(new XYChart.Data<>(0.0, 25.0));
            data.add(new XYChart.Data<>(60.0, 25.0));
            data.add(new XYChart.Data<>(120.0, 50.0));
            data.add(new XYChart.Data<>(240.0, 150.0));
            data.add(new XYChart.Data<>(600.0, 150.0));
            data.add(new XYChart.Data<>(800.0, 240.0));
            data.add(new XYChart.Data<>(1600.0, 240.0));
            data.add(new XYChart.Data<>(1800.0, 200.0));
            data.add(new XYChart.Data<>(2400.0, 120.0));
            data.add(new XYChart.Data<>(3000.0, 120.0));
            data.add(new XYChart.Data<>(3200.0, 50.0));
            data.add(new XYChart.Data<>(3600.0, 50.0));
        }
        else if (id.equals("prep1_meas")){
            data.add(new XYChart.Data<>(0.0, 25.0));
            data.add(new XYChart.Data<>(60.0, 25.0));
            data.add(new XYChart.Data<>(120.0, 55.0));
            data.add(new XYChart.Data<>(240.0, 155.0));
            data.add(new XYChart.Data<>(600.0, 160.0));
        }
        
        
//        for (Point isopoint : chartpoints){
//            Double pressure = ppo ? isopoint.getP_P0() : isopoint.getP_End();
//            Double volume = volg ? isopoint.getPoint_Volume() : isopoint.getVtc_Sw();
//            XYChart.Data<Number, Number> d = new XYChart.Data<>(pressure, volume);
//            d.setExtraValue(isopoint.getP_End());
//            data.add(d);
//        }
        
        return FXCollections.observableArrayList(data);
    }
    
 
}