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
public class EmptyValidator extends FXFValidator{

    private static final Logger LOG = Logger.getLogger(EmptyValidator.class.getName());

    public EmptyValidator() {
        
    }
    
    
    public void getResult(String value){
        
        if (value != null ? value.equals("") : false){    
            message = "Campo não pode ser vazio";
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
    
    
    
}
