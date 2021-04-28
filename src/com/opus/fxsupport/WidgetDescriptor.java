/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;

/**
 *
 * @author opus
 */
public class WidgetDescriptor {
    
    public String name;
    public boolean hasfocus = false;
    public boolean enter_focusable = true;
    public boolean tab_focusable = true;
    public Integer focus_position;
    public Object node;
    public boolean managed = true;
    public boolean group_descriptor = false;
    public boolean required = true;
    public PropertyLinkDescriptor linkdescriptor;
   
    
    public WidgetDescriptor(Integer focus_position, Object node) {
        this.focus_position = focus_position;
        this.node = node;
    }
    
}
