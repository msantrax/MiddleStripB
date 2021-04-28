/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.syssupport;

/**
 *
 * @author opus
 */
public class VirnaPayload {
    
    public Object vobject;
    private Object auxiliar;
    public String objecttype ="";
    public String vstring= "";
    public Integer int1, int2;
    public Long long1, long2;
    public Double double1, double2;
    private Boolean flag1 = false;
    private Boolean flag2 = false;
    
    private Object caller;
    private String callerstate;
    private String servicestatus;
    
    

    public VirnaPayload() {
        
    }
   
    public VirnaPayload setString(String s){
        vstring = s;
        return this;
    }
    
    public VirnaPayload setLong1(Long data){
        long1 = data;
        return this;
    }
    
    public VirnaPayload setLong2(Long data){
        long2 = data;
        return this;
    }
    
    public VirnaPayload setDouble1(Double data){
        double1 = data;
        return this;
    }
    
    public VirnaPayload setDouble2(Double data){
        double2 = data;
        return this;
    }
    
    public VirnaPayload setObject(Object data){
        vobject = data;
        return this;
    }
    
    public VirnaPayload setObjectType(String data){
        objecttype = data;
        return this;
    }
    
    public VirnaPayload setInt1(Integer data){
        int1 = data;
        return this;
    }
    
    public VirnaPayload setInt2(Integer data){
        int2 = data;
        return this;
    }

    public Object getCaller() {
        return caller;
    }

    public VirnaPayload setCaller(Object caller) {
        this.caller = caller;
        return this;
    }

    public String getCallerstate() {
        return callerstate;
    }

    public VirnaPayload setCallerstate(String callerstate) {
        this.callerstate = callerstate;
        return this;
    }

    public Boolean getFlag1() {
        return flag1;
    }

    public VirnaPayload setFlag1(Boolean flag1) {
        this.flag1 = flag1;
        return this;
    }

    public Boolean getFlag2() {
        return flag2;
    }

    public VirnaPayload setFlag2(Boolean flag2) {
        this.flag2 = flag2;
        return this;
    }

    public String getServicestatus() {
        return servicestatus;
    }

    public VirnaPayload setServicestatus(String servicestatus) {
        this.servicestatus = servicestatus;
        return this;
    }

    public Object getAuxiliar() {
        return auxiliar;
    }

    public VirnaPayload setAuxiliar(Object auxiliar) {
        this.auxiliar = auxiliar;
        return this;
    }
    
}
