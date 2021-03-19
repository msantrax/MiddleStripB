/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import com.opus.syssupport.SMTraffic;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import middlestripb.EntityDescriptor;
import org.bson.Document;

/**
 *
 * @author opus
 */
public class Entity {

    
    // ===============================================Housekeep =================================================================
    
    public transient boolean _dirty = false;
    public transient EntityDescriptor _descriptor;
    
    public Entity() {
        
    }
   
    public Document getDocument(){
        return null;
    }
    
    
    public Entity loadEntity(Document document, EntityDescriptor ed){
        return null;
    }
    
    public boolean loadChildren(boolean cascade, SMTraffic smm){
        return false;
    }
    
    
    // ========================================= PROPERTY CHANGE SUPPORT =============================================
    protected transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    
    
    // ======================================= Methods Mapping ==================================================================
    
//    protected transient LinkedHashMap<String, Method> propertymethods;
//    
//    public LinkedHashMap<String, Method> getMethods() { 
//        if (propertymethods == null) mapMethods();
//        return propertymethods;
//    }
//
//    private void mapMethods(){
//
//        propertyfieldmap pfm;
//        String fieldtag;
//
//        propertymethods = new LinkedHashMap<>();
//        Class<?>c = this.getClass();
//
//        for (Method mt : c.getDeclaredMethods() ){
//            pfm = mt.getAnnotation(propertyfieldmap.class);
//            if (pfm != null){
//                fieldtag = pfm.propname();
//                propertymethods.put(fieldtag, mt);
//            }
//        }
//    }
//
//    public String getFieldAsString(String id){
//
//        String out ;
//        Type t;
//        Method m = propertymethods.get(id);
//        
//        if (m != null){
//            try {
//                String st = m.getReturnType().getTypeName();
//                if (st.equals("java.lang.String")){
//                    out = (String)m.invoke(this, null);
//                    return out;
//                }
//                else{
//                    //Class cl = Class.forName(st);
//                    Object obj = m.invoke(this, null);
//                    String s2 = String.valueOf(obj);
//                    return s2;
//                }
//            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//                //Exceptions.printStackTrace(ex);
////            } catch (ClassNotFoundException ex) {
////                Exceptions.printStackTrace(ex);
//            }
//        }
//        return null;
//    }
    
 
}
