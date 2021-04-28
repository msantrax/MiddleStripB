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
    private Double[] diffsbuf;
    private int sbufptr = -1;
    private Long ticklg = 1L;
    private Double average = 0.0;
    private Double diff = 0.0;
    public Double diffabs = 0.0;
    private Double variance = 0.0;

    
    
    
    public SampleRing(int buflen) {
        
        sbuf = new Double[buflen];
        diffsbuf = new Double[buflen];
        clear();
    }
    
    
    public void clear() {
        
        sbufptr = -1;
        for (int i = 0; i < sbuf.length; i++) {
            sbuf[i] = 0.0;
            diffsbuf[i] = 0.0;
        }
    }
    
    
    public void addSample (Double value){
        
        int lastptr =0;
        
        
        sbufptr++;
        
        if (sbufptr > sbuf.length-1){
            // ptr overflow 
            sbufptr = 0;
            lastptr = sbuf.length-1; 
            sbuf[sbufptr] = value;
            diffsbuf[sbufptr] = value - sbuf[lastptr]; 
            diffabs = value - sbuf[lastptr];
        }
        else{
            if (sbufptr > 0){
                // ptr 1 to 4
                lastptr = sbufptr-1;
                sbuf[sbufptr] = value;
                diffsbuf[sbufptr] = value - sbuf[lastptr]; 
                diffabs = value - sbuf[lastptr];
            }
            else{
                // ptr is 0
                lastptr = sbuf.length-1;
                sbuf[sbufptr] = value;
                diffsbuf[sbufptr] = value - sbuf[lastptr];
                diffabs = value - sbuf[lastptr];
            }
        }
        
//        LOG.info(String.format("Sample ring : V=%f - bufptr=%d, lastptr=%d", value, sbufptr, lastptr)); 
        

        int avcounter = 0;
        
        average = 0.0;
        diff = 0.0;
//        diffabs= 0.0;
        
        for (int i = 0; i < sbuf.length; i++) {
            if (sbuf[i] > 0){
                average += sbuf[i];
//                diffabs += diffsbuf[i];
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
        
        

        diffabs = Math.abs(diffabs);
//        diff = Math.abs(diffabs / (average * 2));   
        diff = diffabs ;   
        
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



//        for (int i = 0; i < sbuf.length; i++) {
//            int diffptr = sbufptr + i;  
//            if (diffptr == 0){
//                diff += average - sbuf[sbuf.length-1]; 
//            }
//            else if (diffptr == sbuf.length-1){
//                diff += average - sbuf[0]; 
//            }
//            else{
//                diff += average - sbuf[diffptr -1]; 
//            }
//        }


       
        
        
        
        
        
        
//        LOG.info(String.format("Sample ring : VL=%f - bptr=%+d, lptr=%2d - AV=%f - AVCNT=%d, - Diff=%f - VAR=%+07.4f", 
//                value, sbufptr, lastptr, average, avcounter, diff, variance)); 