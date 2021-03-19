/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Dose;

/**
 *
 * @author opus
 */
public class DosesTblRow {

    
    
    private String number;
    private String type;
    private String pstart;
    private String pend;
    private String deltat;
    private final PointInfoCTX outer;
    private final Dose dose;

    public DosesTblRow(String number, String type, String pstart, String pend, String deltat, 
            final Dose dose,
            final PointInfoCTX outer) {
        
        this.outer = outer;
        this.dose = dose;
        
        this.number = number;
        this.type = type;
        this.pstart = pstart;
        this.pend = pend;
        this.deltat = deltat;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPstart() {
        return pstart;
    }

    public void setPstart(String pstart) {
        this.pstart = pstart;
    }

    public String getPend() {
        return pend;
    }

    public void setPend(String pend) {
        this.pend = pend;
    }

    public String getDeltat() {
        return deltat;
    }

    public void setDeltat(String deltat) {
        this.deltat = deltat;
    }
    
    public Dose getDose() {
        return dose;
    }
}
