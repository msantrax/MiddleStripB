/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;


/**
 *
 * @author opus
 */
public class IsoResearchModel {

    private static final Logger LOG = Logger.getLogger(IsoResearchModel.class.getName());
  
    private IsothermBean isobean;
    private String dbblob;
    private boolean loaded = false;
    
    private ArrayList<String>columns = new ArrayList<>();
    

    public IsoResearchModel(String dbblob) {
        
        this.dbblob = dbblob;
        
//        entitymanager = dbs.getEntityManager();
//        isobean = entitymanager.retrieveObj(IsothermBean.class, dbblob);
        
        if (isobean != null){          
            loaded = true;
        }
    
    }
    
    
    
    public ObservableList<Map<String, Object>> getMapData(int mode){
      
        final AtomicInteger count = new AtomicInteger(0); 
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String,Object>>observableArrayList();
        
        ArrayList<IsothermPoint>isopoints = isobean.points;
        
        for (IsothermPoint isopoint : isopoints){
            Map<String, Object> map = new HashMap<>();
            map.put("id_key", count.incrementAndGet());
            map.put("selected_key", Boolean.valueOf(isopoint.isAdsorption()));
            map.put("pointtype_key", isopoint.isAdsorption() ? 0.0 : 1.0);
            map.put("pressure_key", isopoint.getPpo());
            map.put("volume_key", isopoint.getVolume_g());
            map.put("calculated_key", Double.valueOf(0.0));
            map.put("comment_key", "comment");
            items.add(map);
        }
        return items;
        
    }
    
    
    // GETSET Area ==============================================================================================
    public IsothermBean getIsoBean() { return isobean;}
    
    public String getDBBlob() { return dbblob;}
    
    public String getTabName() { return "Research @ " + dbblob;}
    
    public boolean isLoaded() { return loaded;}
    
    
    
}



//ObservableList<Map<String, Object>> items = FXCollections.<Map<String,Object>>observableArrayList(
//                item -> new javafx.beans.Observable[] {         
//                    new SimpleBooleanProperty((Boolean)item.get("selected_key"))
//                }
//        );
//        
//        items.addListener(new ListChangeListener<Map<String, Object>>() {
//            @Override 
//            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Map<String, Object>> c) {
//                
//                while (c.next()) {
//                    if (c.wasUpdated()) {
//                        LOG.info(String.format("List was changed"));
//                    }
//                }
//            }
//        });