/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport.validation;

import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;

/**
 *
 * @author opus
 * @param <T>
 */
public class NumberValidator extends FXFValidator{

    private static final Logger LOG = Logger.getLogger(NumberValidator.class.getName());

    
    private static final String NUMBER_REGEX =  "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
    public static final String OUTRANGE = "Fora de faixa !\nValor %s que %5.3f";
    public static final String WARNING = "Inadequado mas toleravel...\n%6.2f < Valor > %6.2f";
    public static final String CLEAR = "Tooltip default";
    
    private Double number ;
    private Double[] ranges = {Double.NaN, Double.NaN, Double.NaN, Double.NaN};
    private String[] messages = {OUTRANGE, WARNING, WARNING, OUTRANGE, CLEAR };
   
    private boolean maybenull = false;
    
    
    
    public NumberValidator() {
        
    }

    public NumberValidator setRanges (Double[] ranges){
        this.ranges = ranges;
        return this;
    }
    
    public NumberValidator setRangeWindows (Double target, Double warnig_window, Double error_window){
        
        Double w1 = target/100;
        
        if ((warnig_window != Double.NaN && error_window != Double.NaN) && (warnig_window > error_window)){
            warnig_window = Double.NaN;
            error_window = warnig_window;        
        }
        
        ranges[0] = error_window == Double.NaN ? Double.NaN :target -(w1 * error_window);
        ranges[1] = warnig_window == Double.NaN ? Double.NaN :target -(w1 * warnig_window);
        ranges[2] = warnig_window == Double.NaN ? Double.NaN :target +(w1 * warnig_window);
        ranges[3] = error_window == Double.NaN ? Double.NaN :target +(w1 * error_window);
        
        return this;
    }
    
    public NumberValidator setRangeMessage (int index, String message){
        messages[index] = message;
        return this;
    }
    
    
    public void getResult(String value){
        
       if (value.equals("") && maybenull){
            message = "OK !";
            warning = false;
            failed = false;
            updateTootip(default_message, Status.CLEAR);
            return;
        }
        
        if (value != null ? !value.matches(NUMBER_REGEX) : false){
            //return new ValidationResult().addMessageIf(control, "Deve ser um numero", Severity.ERROR, true );
            message = "Deve ser um numero";
            warning = false;
            failed = true;
            updateTootip(message, Status.ERROR);
            return;
        }
        
        try{
            number = Double.parseDouble(value);

            if (ranges[0] != Double.NaN && number < ranges[0]){
                //return new ValidationResult().addMessageIf(control, String.format(OUTRANGE, "menor", ranges[0]), Severity.ERROR, true );
                message = String.format(OUTRANGE, "menor", ranges[0]);
                warning = false;
                failed = true;
                updateTootip(message, Status.ERROR);
                return;
            }
            if (ranges[1] != Double.NaN && number < ranges[1]){
                //return new ValidationResult().addMessageIf(control, String.format(WARNING, "menor", ranges[1]), Severity.WARNING, true );
                message = String.format(WARNING, ranges[0], ranges[1]);
                warning = true;
                failed = true;
                updateTootip(message, Status.WARNING);
                return;
            }
            if (ranges[3] != Double.NaN && number > ranges[3]){
                //return new ValidationResult().addMessageIf(control, String.format(OUTRANGE, "maior", ranges[3]), Severity.ERROR, true );
                message = String.format(OUTRANGE, "maior", ranges[3]);
                warning = false;
                failed = true;
                updateTootip(message, Status.ERROR);
                return;
            }
            if (ranges[2] != Double.NaN && number > ranges[2]){
                //return new ValidationResult().addMessageIf(control, String.format(WARNING, "maior", ranges[2]), Severity.WARNING, true );
                message = String.format(WARNING, ranges[2], ranges[3]);
                warning = true;
                failed = true;
                updateTootip(message, Status.WARNING);
                return;
            }
        
        }
        catch (Exception ex){
            //return new ValidationResult().addMessageIf(control, "Não posso converter o numero", Severity.ERROR, true );
            message = "Não posso converter o numero";
            warning = false;
            failed = true;
            updateTootip(message, Status.ERROR);
            return;
        }

        message = "OK !";
        warning = false;
        failed = false;
        updateTootip(default_message, Status.CLEAR);
        
    }

    public boolean isMaybenull() {
        return maybenull;
    }

    public void setMaybenull(boolean maybenull) {
        this.maybenull = maybenull;
    }
    
    
}
