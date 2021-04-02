/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class SampleRing {

    private static final Logger LOG = Logger.getLogger(SampleRing.class.getName());
    
    private Double[] sbuf;
    private int sbufptr = -1;
    private Long ticklg = 250L;
    private Double average = 0.0;
    private Double diff = 0.0;
    private Double variance = 0.0;

    public SampleRing(int buflen) {
        
        sbuf = new Double[buflen];
        clear();
    }
    
    
    public void clear() {
        
        sbufptr = -1;
        for (int i = 0; i < sbuf.length; i++) {
            sbuf[i] = -1.0;
        }
    }
    
    
    public void addSample (Double value){
        
        int lastptr =0;
        
        
        sbufptr++;
        
        if (sbufptr > sbuf.length-1){
            sbufptr = 0;
            lastptr = sbuf.length-1; 
        }
        else{
            if (sbufptr > 0){
                lastptr = sbufptr-1;
            }
            else{
                if (sbuf[sbuf.length-1] > 0){
                    lastptr = sbuf.length-1;
                }
                else{
                    lastptr = -1;
                }
            }
        }
        
//        LOG.info(String.format("Sample ring : V=%f - bufptr=%d, lastptr=%d", value, sbufptr, lastptr)); 
        
        
        sbuf[sbufptr] = value;
        if (lastptr == -1){
            diff = 0.0;
        }
        else{
            diff = Math.abs((value - sbuf[lastptr]) / getTicklg());            
        }

        
        
        int avcounter = 0;
        
        average = 0.0;
        for (int i = 0; i < sbuf.length; i++) {
            if (sbuf[i] > 0){
                average += sbuf[i];
                avcounter++;
            }            
        }
        if (avcounter > 0 ){
            average = getAverage()/avcounter;
        }
        else{
            average = value;
        }
        
        variance = average - value;
        
        
        
//        LOG.info(String.format("Sample ring : VL=%f - bptr=%+d, lptr=%2d - AV=%f - AVCNT=%d, - Diff=%f - VAR=%+07.4f", 
//                value, sbufptr, lastptr, average, avcounter, diff, variance)); 
        
    }

    public Long getTicklg() {
        return ticklg;
    }

    public void setTicklg(Long ticklg) {
        this.ticklg = ticklg;
    }

    public Double getAverage() {
        return average;
    }

    public Double getDiff() {
        return diff;
    }

    public Double getVariance() {
        return variance;
    }
    
    
    
    
}
