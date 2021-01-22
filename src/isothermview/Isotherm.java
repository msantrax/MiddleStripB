/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author opus
 */
public class Isotherm {

    private static final Logger log = Logger.getLogger(Isotherm.class.getName());

    private IsothermBean isotherm_bean;
    public boolean chart_ready = false;
    public int transferptr = 0;
    
    private ObservableList<XYChart.Data<Number, Number>> adspoints;
    private ObservableList<XYChart.Data<Number, Number>> despoints;

    public ObservableList<XYChart.Data<Number, Number>> getAdspoints() {
        return adspoints;
    }

    public ObservableList<XYChart.Data<Number, Number>> getDespoints() {
        return despoints;
    }
    
    
    public Isotherm() {
        isotherm_bean = new IsothermBean();
        isotherm_bean.points.add(new IsothermPoint());
        adspoints = FXCollections.observableList(new ArrayList<>());
        despoints = FXCollections.observableList(new ArrayList<>());
    }
    
    
    
    public String generateParams(){
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(isotherm_bean.a_operator.trim());
        sb.append('|');
        sb.append(isotherm_bean.sid.trim());
        sb.append('|');
        sb.append(isotherm_bean.sdesc.trim());
        sb.append('|');
        sb.append(String.format("%1$td-%1$tm-%1$tY",isotherm_bean.a_date));
        sb.append('|');
        sb.append(isotherm_bean.source.trim());
        sb.append('|');
        sb.append(String.format("%d",isotherm_bean.points.size()));
        sb.append('|');
        sb.append(isotherm_bean.comment.trim());
        
        
        return sb.toString();
    }
    
    
    public void setIsothermBean (IsothermBean isthb){
        this.isotherm_bean = isthb;
    }
   
    public IsothermBean getIsothermBean(){
        return this.isotherm_bean;
    }
    
    
    public ArrayList<IsothermPoint> getPoints() {
        return isotherm_bean.points;
    }

    public void setPoints(ArrayList<IsothermPoint> points) {
        isotherm_bean.points = points;
    }

    
    public void addPoint(IsothermPoint ip, double pressure, double volume, boolean ads){
        
        
        XYChart.Data<Number, Number> d ;
        
        if (ip != null){
            isotherm_bean.points.add(ip);
            d = new XYChart.Data<>(ip.getPpo(), ip.getVolume_g());
        }
        else{
            isotherm_bean.points.add(new IsothermPoint(pressure, volume));
            d = new XYChart.Data<>(pressure, volume);
        }
        
        
        log.info(String.format("Loading point  = [%d]", transferptr));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (ads){
                    adspoints.add(d);
                }
                else{
                    despoints.add(d);
                }
            }
        });
        
        
    }
    
    public void addTempPoint(double pressure, double volume){
        isotherm_bean.temp_points.add(new IsothermPoint(pressure, volume));
    }
    
    public void transferPoint(){
        
        if (transferptr >= isotherm_bean.temp_points.size()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    adspoints.clear();
                    despoints.clear();
                    transferptr = 0;
                }
            });
            return;
        }
        
        IsothermPoint ip = isotherm_bean.temp_points.get(transferptr);
        addPoint(ip, 0.0, 0.0, ip.isAdsorption());
        transferptr++;
    }
    
    
    // Utilities =========================================================================================================
    
    public void findMiniMax(boolean temp){
        
        int pts = temp ? isotherm_bean.temp_points.size() : isotherm_bean.points.size();
        double current_adsorption = temp ? isotherm_bean.temp_points.get(0).getPpo() : isotherm_bean.points.get(0).getPpo();
        boolean adsorption = true;
        if (temp){
            isotherm_bean.temp_points.get(0).setAdsorption(adsorption);
        }
        else{
            isotherm_bean.temp_points.get(0).setAdsorption(adsorption);
        }
       
        for (int i = 1; i < pts; i++) {
            IsothermPoint isop = temp ? isotherm_bean.temp_points.get(i) : isotherm_bean.points.get(i);
            if (adsorption){
                if (isop.getPpo() > current_adsorption){
                    isop.setAdsorption(true);
                    current_adsorption = isop.getPpo();
                }
                else{
                    adsorption = false;
                    isop.setAdsorption(false);
                }
            }
            else{
                isop.setAdsorption(false);
            }
        }
    }
    
    
    public int getSize(boolean temp){
        return temp ? isotherm_bean.temp_points.size() :  isotherm_bean.points.size() ;
    }
    
    public IsothermPoint getMinAdsorption(boolean temp){
        return getAdsorptionPoints(temp).get(0);
    }
    
    public IsothermPoint getMaxAdsorption(boolean temp){
        ArrayList<IsothermPoint> maxp = getAdsorptionPoints(temp);
        return maxp.get(maxp.size()-1);
    }
    
    public IsothermPoint getMinDesorption(boolean temp){
        ArrayList<IsothermPoint> ips = getDesorptionPoints(temp);
        return ips.get(0);
    }
    
    public IsothermPoint getMaxDesorption(boolean temp){
        ArrayList<IsothermPoint> maxp = getDesorptionPoints(temp);
        return maxp.get(maxp.size()-1);
    }
    
    
    public ArrayList<IsothermPoint> getAdsorptionPoints(boolean temp){
        
        ArrayList<IsothermPoint> adspoints = new ArrayList<>();
        ArrayList<IsothermPoint> srcpoints = temp ? isotherm_bean.temp_points : isotherm_bean.points;
        for (IsothermPoint isop : srcpoints){
            if (isop.isAdsorption()){
                adspoints.add(isop);
            }
            else{
                return adspoints;
            }
        }
        return adspoints;
    }
    
    
    public ArrayList<IsothermPoint> getDesorptionPoints(boolean temp){
       
        ArrayList<IsothermPoint> dspoints = new ArrayList<>();
        ArrayList<IsothermPoint> outpoints = new ArrayList<>();
        
        ArrayList<IsothermPoint> srcpoints = temp ? isotherm_bean.temp_points : isotherm_bean.points;
        for (IsothermPoint isop : srcpoints){
            if (!isop.isAdsorption()) dspoints.add(isop);
        }
        
        for (int i = dspoints.size()-1; i >=0 ; i--) {
            IsothermPoint outpoint = dspoints.get(i);
            outpoints.add(outpoint);
        }
        return outpoints;
    }
    
    
    
    
    
    
    
    public int findPointByPressure(double pressure, boolean relative){
        int pointer = 0;
        for (IsothermPoint isop : isotherm_bean.points){
            if (relative){
                if (isop.getPpo() == pressure) return pointer;
            }
            else{
                if (isop.getPo() == pressure) return pointer;
            }
            pointer++;
        }
        return -1;
    }
    
    public int findPointByVolume(double volume, boolean relative){
        int pointer = 0;
        for (IsothermPoint isop : isotherm_bean.points){
            if (relative){
                if (isop.getVolume_g() == volume) return pointer;
            }
            else{
                if (isop.getVolume_g() == volume) return pointer;
            }
            pointer++;
        }
        return -1;
    }
    
    
    public double getPressure (int index){
        return isotherm_bean.points.get(index).getPpo();
    }
    
    public double getVolume (int index){
        return isotherm_bean.points.get(index).getVolume_g();
    }
    
    public double getPressureByVolume (double volume, boolean relative){
        int index = findPointByVolume(volume, relative);
        if (index !=-1){
            return relative ? isotherm_bean.points.get(index).getPpo() : isotherm_bean.points.get(index).getPo();
        }
        return -1;
    }
    
    public double getVolumeByPressure (double pressure, boolean relative){
        int index = findPointByVolume(pressure, relative);
        if (index !=-1){
            return relative ? isotherm_bean.points.get(index).getVolume_g() : isotherm_bean.points.get(index).getVolume();
        }
        return -1;
    }
    
    
    
   
    
    
    
    
}
