/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class EntityDescriptors extends ArrayList<EntityDescriptor>{

    private static final Logger LOG = Logger.getLogger(EntityDescriptors.class.getName());

    
    public EntityDescriptors() {
        
    }
    
    
    public <T> T findById(Long id, boolean instance){
                
        for(EntityDescriptor ed : this){
//            Long v1 = (Long)ed.getSuid();
//            Long v2 = (Long)id;
            if ( ed.getSuid().compareTo(id) == 0) {
                if (instance) return (T)ed.getInstance();
                return (T) ed;
            }
        }
        
        return null;

    }
    
    public EntityDescriptor findByInstance(Object instance){
                
        for(EntityDescriptor ed : this){
            if (ed.getInstance() == instance) {
                return ed;
            }
        }
       
        return null;

    }
    
    
}
