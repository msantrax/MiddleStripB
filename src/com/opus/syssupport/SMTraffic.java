/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.syssupport;


public class SMTraffic {
    
    protected Long addr;
    private String command;
    private int code;
    protected Long handle;
    private String state;
    private VirnaPayload payload;
    protected Class caller;
    
    private String next_true;
    private String next_false;
    
    
    public SMTraffic( Long addr, String command, int code, String state, Class caller, VirnaPayload payload) {
        
        this.addr = addr;
        this.command = command;
        this.code = code;
        this.caller = caller;
        
        if (payload == null){
            this.payload = new VirnaPayload().setString("");
        }
        else{
            this.payload = payload;
        }
        this.code=code;
        this.state=state;
    }

     public SMTraffic( Long addr, Long handle, int code, String state, Class caller, VirnaPayload payload) {
        
        this.addr = addr;
        this.command = "LOADSTATE"; //VirnaServices.CMDS.LOADSTATE;
        this.code = code;
        this.handle = handle;
        this.caller = caller;
        
        if (payload == null){
            this.payload = new VirnaPayload().setString("");
        }
        else{
            this.payload = payload;
        }
        this.code=code;
        this.state=state;
        
    }
    
    
    
    public String getCommand() {
        return command;
    }

    public int getCode() {
        return code;
    }

    public String getState() {
        return state;
    }
    
    public VirnaPayload getPayload() {
        return payload;
    }

    /**
     * @return the uid
     */
    public Long getAddr() {
        return addr;
    }

    /**
     * @param uid the uid to set
     */
    public void setAddr(Long addr) {
        this.addr = addr;
    }

    /**
     * @return the handle
     */
    public Long getHandle() {
        return handle;
    }

    /**
     * @param handle the handle to set
     */
    public void setHandle(Long handle) {
        this.handle = handle;
    }

    public Class getCaller() {
        return caller;
    }

    public String getNext_true() {
        return next_true;
    }

    public void setNext_true(String next_true) {
        this.next_true = next_true;
    }

    public String getNext_false() {
        return next_false;
    }

    public void setNext_false(String next_false) {
        this.next_false = next_false;
    }
    
}    