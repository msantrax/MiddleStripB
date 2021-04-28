/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

/**
 *
 * @author opus
 */
public class ASVPDEVBargraphDescriptor {
    
    
    public String main_label = "Pressure mmHg";
    public String main_rangeformat = "%6.2f";
    public Double main_low = 0.0;
    public Double main_high = 1000.0;
    public Double main_threshold = 80.0;
    public Boolean main_inverted = true;
    
    public String dpdt_label = "dP/dT - ΔP";
    public String dpdt_rangeformat = "%5.3f";
    public Double dpdt_low = 0.0;
    public Double dpdt_high = 0.15;
    public Double dpdt_threshold = 10.0;
    public Boolean dpdt_inverted = false;
    
    
    public String sigma_label = "ϭ ² (Variance)";
    public String sigma_rangeformat = "%5.3f";
    public Double sigma_range = 10.0;
    public Double sigma_threshold = 10.0;
    public Boolean sigma_inverted = false;

    
    public ASVPDEVBargraphDescriptor() {
    
    }
    
    
    
}
