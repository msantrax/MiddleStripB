/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class Isothermv {

    private static final Logger log = Logger.getLogger(Isothermv.class.getName());

    private IsothermBean isotherm_bean;
    
    public Isothermv() {
        isotherm_bean = new IsothermBean();
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

    public void addPoint(double pressure, double volume){
        isotherm_bean.points.add(new IsothermPoint(pressure, volume));
    }
    
    public void findMiniMax(){
        
        int pts = isotherm_bean.points.size();
        double current_adsorption = isotherm_bean.points.get(0).getPpo();
        boolean adsorption = true;
        isotherm_bean.points.get(0).setAdsorption(adsorption);
        
        for (int i = 1; i < pts; i++) {
            IsothermPoint isop = isotherm_bean.points.get(i);
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
    
    public int getSize(){
        return isotherm_bean.points.size();
    }
    
    public IsothermPoint getMinAdsorption(){
        return getAdsorptionPoints().get(0);
    }
    
    public IsothermPoint getMaxAdsorption(){
        ArrayList<IsothermPoint> maxp = getAdsorptionPoints();
        return maxp.get(maxp.size()-1);
    }
    
    public IsothermPoint getMinDesorption(){
        return getDesorptionPoints().get(0);
    }
    
    public IsothermPoint getMaxDesorption(){
        ArrayList<IsothermPoint> maxp = getDesorptionPoints();
        return maxp.get(maxp.size()-1);
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
    
    public ArrayList<IsothermPoint> getAdsorptionPoints(){
        
        ArrayList<IsothermPoint> adspoints = new ArrayList<>();
        for (IsothermPoint isop : isotherm_bean.points){
            if (isop.isAdsorption()){
                adspoints.add(isop);
            }
            else{
                return adspoints;
            }
        }
        return adspoints;
    }
    
    public ArrayList<IsothermPoint> getDesorptionPoints(){
       
        ArrayList<IsothermPoint> dspoints = new ArrayList<>();
        ArrayList<IsothermPoint> outpoints = new ArrayList<>();
        
        for (IsothermPoint isop : isotherm_bean.points){
            if (!isop.isAdsorption()) dspoints.add(isop);
        }
        
        for (int i = dspoints.size()-1; i >=0 ; i--) {
            IsothermPoint outpoint = dspoints.get(i);
            outpoints.add(outpoint);
        }
        return outpoints;
    }
   
}
