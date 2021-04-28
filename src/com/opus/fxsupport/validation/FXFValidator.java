/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport.validation;

import java.util.logging.Logger;
import javafx.scene.control.Tooltip;

/**
 *
 * @author opus
 */
public class FXFValidator {

    private static final Logger LOG = Logger.getLogger(FXFValidator.class.getName());
    
    //private static final String SHADOW_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"; //$NON-NLS-1$
    public static final String POPUP_SHADOW_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 5);"; //$NON-NLS-1$
    public static final String TOOLTIP_COMMON_EFFECTS = "-fx-font-weight: bold; -fx-padding: 5; -fx-border-width:1;"; //$NON-NLS-1$
    public static final String ERROR_TOOLTIP_EFFECT = POPUP_SHADOW_EFFECT + TOOLTIP_COMMON_EFFECTS
            + "-fx-background-color: FBEFEF; -fx-text-fill:cc0033; -fx-border-color:cc0033;";
    public static final String WARNING_TOOLTIP_EFFECT = POPUP_SHADOW_EFFECT + TOOLTIP_COMMON_EFFECTS
            + "-fx-background-color: FFFFCC; -fx-text-fill:CC9900; -fx-border-color:CC9900;";
    public static final String CLEAR_TOOLTIP_EFFECT = POPUP_SHADOW_EFFECT + TOOLTIP_COMMON_EFFECTS
            + "-fx-background-color: FFFFFF; -fx-text-fill:000000; -fx-border-color:000000;";
    
    protected Tooltip tooltip;
    protected enum Status { WARNING, ERROR, CLEAR } 
    
    
    protected String default_message;
    protected String message;
    protected boolean warning;
    protected boolean failed;
    
    

    public FXFValidator() {
        
    }
    
    
    public String getMessage() {
        return message;
    }

    public boolean isWarning() {
        return warning;
    }
    
    public boolean isFailed() {
        return failed;
    }
    
    public Tooltip getTooltip() {
        return tooltip;
    }
    
    public void initTooltip (Tooltip tooltip){
        this.tooltip = tooltip;
        default_message = tooltip.getText();
        tooltip.setOpacity(.9);
        tooltip.setAutoFix(true);
        //tooltip.setStyle(CLEAR_TOOLTIP_EFFECT);
    }
    
    protected void updateTootip(String message, FXFValidator.Status st){
        
        //Platform.runLater(() -> {
            tooltip.setText(message);
//            if (st == FXFValidator.Status.CLEAR){
//                tooltip.setStyle(CLEAR_TOOLTIP_EFFECT);
//            }
//            else{
//                tooltip.setStyle( st == FXFValidator.Status.ERROR ? ERROR_TOOLTIP_EFFECT: WARNING_TOOLTIP_EFFECT);
//            }
        //});

        //String s = tooltip.getStyle();
        //LOG.info(String.format("Tooltip style = %s", s));
        
    }
    
    
    
    
    
}
