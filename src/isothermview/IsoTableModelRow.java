/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;

import java.util.ArrayList;



public class IsoTableModelRow {
    
    public static int nextseq;
    
    private boolean active = true;
    private int sequence;
    private ArrayList<Double> values;
    
    public IsoTableModelRow() {
        values = new ArrayList<>();
        sequence = nextseq;
    }
    
    
    public ArrayList<Double> getValues() { return values;}
    
    public IsoTableModelRow setActive (boolean active) { 
        this.active = active;
        return this;
    }
    
    public IsoTableModelRow setSequence (int seq) { 
        this.sequence = seq;
        return this;
    }
    
    public int getSequence () { return sequence;}
    
    
    public ArrayList<Double> setValue (int index, Double value) {
        if (index == -1){
            values.add(value);
        }
        else{
            values.set(index, value);
        }
        return values;
    }
    
    
    
}
