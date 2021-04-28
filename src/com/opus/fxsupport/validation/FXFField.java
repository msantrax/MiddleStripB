/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport.validation;

import com.opus.fxsupport.FXFControllerInterface;
import com.opus.fxsupport.WidgetContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;

/**
 *
 * @author opus
 */
public interface FXFField {
   
   void setManagement(FXFControllerInterface controller, Integer idx, WidgetContext wctx);      
   void setFocusPosition(Integer pos);
   Integer getFocusPosition();
   void setFocus(boolean set);
   void updateValue(String value, boolean required);
   String getValue();
   public String getSid();
   public void setSid(String sid);
   
   public ContextMenu getConfigurationMenu(Control field, FXFFieldDescriptor fxfd);
   
   
}
