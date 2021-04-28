/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;


import com.opus.fxsupport.validation.FXFField;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class PropertyLinkDescriptor {

    private static final Logger LOG = Logger.getLogger(PropertyLinkDescriptor.class.getName());
    
    
        private String plink;

    public String getPlink() {
        return plink;
    }

    public PropertyLinkDescriptor setPlink(String plink) {
        this.plink = plink;
        return this;
    }

        private String propname;

    public String getPropname() {
        return propname;
    }

    public PropertyLinkDescriptor setPropname(String propname) {
        this.propname = propname;
        return this;
    }
    
    
        private boolean valid = false;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    
        private boolean required = false;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
    
    
    
        private boolean input;

    public boolean isInput() {
        return input;
    }

    public PropertyLinkDescriptor setInput(boolean input) {
        this.input = input;
        return this;
    }

    
        private boolean stopfocus = false;

    public boolean isStopfocus() {
        return stopfocus;
    }

    public PropertyLinkDescriptor setStopfocus(boolean stopfocus) {
        this.stopfocus = stopfocus;
        return this;
    }

    
        private String callstate;

    public String getCallstate() {
        return callstate;
    }

    public PropertyLinkDescriptor setCallstate(String callstate) {
        this.callstate = callstate;
        return this;
    }

        private Method getmethod;

    public Method getGetmethod() {
        return getmethod;
    }

    public void setGetmethod(Method getmethod) {
        this.getmethod = getmethod;
    }

    
    
    private Method method;

    public Method getMethod() {
        return method;
    }

    public PropertyLinkDescriptor setMethod(Method method) {
        this.method = method;
        return this;
    }

    private Class clazz;

    public Class getClazz() {
        return clazz;
    }

    public PropertyLinkDescriptor setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    private Object instance;

    public Object getInstance() {
        return instance;
    }

    public PropertyLinkDescriptor setInstance(Object instance) {
        this.instance = instance;
        return this;
    }
    
        private FXFField fxfield;

    public FXFField getFxfield() {
        return fxfield;
    }

    public PropertyLinkDescriptor setFxfield(FXFField fxfield) {
        this.fxfield = fxfield;
        return this;
    }

        private String auxiliar = "";

    public String getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(String auxiliar) {
        this.auxiliar = auxiliar;
    }

    
    
}
