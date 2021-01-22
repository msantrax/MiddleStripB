/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author opus
 */
public class IsothermBean implements Serializable {

    private static final long serialversionUID = -1L;
    
    // Header Data
    
    public String source;
    public String a_operator;
    public Long a_date;
    public String r_operator;
    public Long r_date;
    public String sid;
    public String filename;
    public String sdesc;
    public String comment;
    public String added;
    
    public Double smp_weight;
    public Double smp_volume;
    public Double smp_density;
    public Double outgas_time;
    public Double outgas_temp;
        
    public ArrayList<IsothermPoint> points;
    public transient ArrayList<IsothermPoint> temp_points;
    
   
    public IsothermBean() {
        points = new ArrayList<>();
        temp_points = new ArrayList<>();
    }
    
  
    
}
