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
public class JsonHeader {
 
        protected String classtype = "jsonclass";

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

        protected String id = "id";

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

        protected String argument = "";

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    
    public JsonHeader() {
    
    }

    
    
    
    
}
