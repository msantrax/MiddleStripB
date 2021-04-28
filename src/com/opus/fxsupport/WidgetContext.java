/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;

import com.opus.fxsupport.validation.FXFField;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javafx.collections.ObservableMap;

// =============================================================================

public class WidgetContext {

    public FXFControllerInterface controller;
    public ObservableMap<String, Object> namespace;
    LinkedHashMap<Integer, WidgetDescriptor> widget_list = new LinkedHashMap<>();
    private ArrayList<Integer> focus_cycle_map = new ArrayList<>();
    
    
    public WidgetContext(FXFControllerInterface ctrl, ObservableMap<String, Object> namespace) {
        this.namespace = namespace;
        this.controller = ctrl;
    }

    public void setWidgetList(LinkedHashMap<Integer, WidgetDescriptor> widget_list) {
        this.widget_list = widget_list;
        updateFocusCycleMap();
    }

    public LinkedHashMap<Integer, WidgetDescriptor> getWidgetList() {
        return widget_list;
    }

    public WidgetDescriptor findByName(String name) {
        for (WidgetDescriptor wd : widget_list.values()) {
            if (wd.name.equals(name)) {
                return wd;
            }
        }
        return null;
    }

    public WidgetDescriptor findByHash(FXFField field) {
        for (WidgetDescriptor wd : widget_list.values()) {
            if (wd.node == field) {
                return wd;
            }
        }
        return null;
    }

    public void updateFocusCycleMap() {
        focus_cycle_map.clear();
        widget_list.forEach((idx, wdg) -> {
            if (wdg.managed) {
                focus_cycle_map.add(idx);
            }
        });
    }

    public ArrayList<Integer> getFocusCycleMap() {
        return focus_cycle_map;
    }
    
}
