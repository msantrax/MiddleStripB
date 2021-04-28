/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;


import com.opus.fxsupport.validation.FXFField;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableMap;
import javafx.scene.Node;

/**
 *
 * @author opus
 */
public class FXFWidgetManager {

    private static final Logger LOG = Logger.getLogger(FXFWidgetManager.class.getName());
    
    private static FXFWidgetManager instance;    
    public static FXFWidgetManager getInstance(){
        if (instance == null) {instance = new FXFWidgetManager();}
        return instance;
    }
     
    public LinkedHashMap<FXFControllerInterface,WidgetContext> context = new LinkedHashMap<>(); 
    
    //private ValidationSupport vsupport = new ValidationSupport();
    private int outfocus_counter = 0;
    
    
    
    public FXFWidgetManager() {
        
        instance = this;
    }

    
    public FXFField getWidget(FXFControllerInterface ctrl, String name){
        
        WidgetContext wc = context.get(ctrl);
        if (wc != null){
            WidgetDescriptor wd = wc.findByName(name);
            if (wd != null){
                FXFField field = (FXFField)wd.node;
                return field;
            }
        }
        
        return null;
    }
    
    public WidgetDescriptor getWidgetDescriptor(FXFControllerInterface ctrl, String name){
        
        WidgetContext wc = context.get(ctrl);
        if (wc != null){
            WidgetDescriptor wd = wc.findByName(name);
            if (wd != null){
                return wd;
            }
        }   
        return null;
    }
    
    
    public int getNextOutFocusCounter() { return (++outfocus_counter) + 900 ;}
    
    
    public void addContext (FXFControllerInterface ctrl, ObservableMap<String, Object> namespace){
        
        WidgetContext wctx = new WidgetContext(ctrl, namespace); 
        
        LinkedHashMap<Integer,WidgetDescriptor> templist = new LinkedHashMap<>();
        outfocus_counter = 0;
   
        namespace.forEach((name, obj) -> {
            if (obj != null){
                if (obj instanceof Node){
                    ((Node) obj).setFocusTraversable(false);
                    if (obj instanceof FXFField){
                        FXFField field = (FXFField)obj;
                        int focus;
                        WidgetDescriptor wd;
                        
                        if (field.getFocusPosition() == null || field.getFocusPosition() == 0){
                            focus = getNextOutFocusCounter();
                            wd = new WidgetDescriptor(focus, field);
                            wd.enter_focusable = false;
                        }
                        else{
                            focus = field.getFocusPosition();
                            wd = new WidgetDescriptor(focus, field);
                        }
                        wd.name = name;
                        String fname = name;
                        field.setManagement(ctrl, focus, wctx);
                        templist.put(focus, wd);
                        
                        String mes = String.format("Registering Key: %s -> %d / type=%s", name, focus, obj.toString());
                        LOG.info(mes);
                    }
                }
            }
        });
        
        wctx.setWidgetList(templist.entrySet()
        .stream()
        .sorted(Map.Entry.<Integer, WidgetDescriptor>comparingByKey())
        .collect(Collectors.toMap(
            Map.Entry::getKey, 
            Map.Entry::getValue, 
            (oldValue, newValue) -> oldValue, LinkedHashMap::new)));
        
        context.put(ctrl, wctx);
        
    }
    
    
    public void initFocus(FXFControllerInterface ctrl) {
        
        WidgetContext wctx = context.get(ctrl);
//        FXFField field = (FXFField)wctx.widget_list.get(1);
//        field.setFocus(true);
//        LOG.info("Focus init..."); 
    }
    
    public void yieldFocus (FXFControllerInterface ctrl, FXFField field, boolean fwd){
        
        WidgetContext wctx = context.get(ctrl);
        //LOG.info("Focus cycled...");
    }
    
    
    public void yieldFocus (WidgetContext wctx, FXFField field, boolean fwd, boolean tab){
        
        WidgetDescriptor wd;
        int next_widget;
        Integer[] focusmap = new Integer[wctx.getFocusCycleMap().size()];
        wctx.getFocusCycleMap().toArray(focusmap);
        Integer focus_idx = field.getFocusPosition();
        
        //LOG.info(String.format("YieldFocus : field=%d / focuspos=%d", field.hashCode(), focus_idx));
        
        for (int i = 0; i < focusmap.length; i++) {
            
            if (Objects.equals(focusmap[i], focus_idx)){
                // Release focus from component and update focus map accordingly
                wd = wctx.getWidgetList().get(focus_idx);
                wd.hasfocus = false;
                field.setFocus(false);
                
                //LOG.info(String.format("\t Focus lost on: map index=%d", i));
                
                int startscan = i;
                boolean done = false;
                
                while (!done){
                    if (fwd){
                        next_widget = startscan+1;
                        if (next_widget > focusmap.length-1) next_widget = 0;
                    }
                    else{
                        next_widget = startscan-1;
                        if (next_widget < 0) next_widget = focusmap.length;
                    }

                    int next_index = focusmap[next_widget];
                    //LOG.info(String.format("\t Next focusmap index = %d -> next position=%d", next_widget, next_index));
                    wd = wctx.getWidgetList().get(next_index);
                    if (wd.enter_focusable || (tab && wd.tab_focusable)){
                        wd.hasfocus = true;
                        FXFField next_field = (FXFField)wd.node;
                        next_field.setFocus(true);
                        //LOG.info(String.format("\tFocus was set on widget = %d", next_field.hashCode()));
                        done = true;
                    }
                    else {
                        startscan = fwd ? startscan+1 : startscan-1;    
                    }
                }
                
                break; 
            }
            
        }
        
        //LOG.info("Focus cycled...");
    }
    
    
    public void updateFocus (WidgetContext wctx, Integer pos, boolean focused){
        
        WidgetDescriptor wgtdesc = wctx.widget_list.get(pos);
        wgtdesc.hasfocus = focused;
        //LOG.info(String.format("Focus @ %d (position = %d) was %s", wgtdesc.node.hashCode(), pos, focused ? "Gained":"Lost"));
        
    }
    
    
    
    
    
    
}



                    
//                    if (name.startsWith("FX")){
//                        String seq = name.substring(2, 5);
//                        try {
//                            Integer idx = Integer.parseInt(seq);
//                            FXFField field = (FXFField)obj;
//                            field.setManagement(ctrl, idx, wctx);
//                            templist.put(idx, new WidgetDescriptor(idx, field));
//                            String mes = String.format("Registering Key: %s -> %d / type=%s", name, idx, obj.toString());
//                            //LOG.info(mes);
//                        } 
//                        catch (NumberFormatException ex) {
//                            LOG.warning(String.format("Failed to convert focus node %s", name));
//                        }    
//                    }