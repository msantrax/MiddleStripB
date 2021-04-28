/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport.validation;

import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class VoidValidator extends FXFValidator {

    private static final Logger LOG = Logger.getLogger(VoidValidator.class.getName());

    
    public VoidValidator() {
    }
    
    
    public void getResult(String value){
        
        message = "OK !";
        warning = false;
        failed = false;
        updateTootip(default_message, Status.CLEAR);
       
        
    }
    
    
}
