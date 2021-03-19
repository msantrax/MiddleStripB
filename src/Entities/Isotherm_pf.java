
package Entities;

import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.propertylink;
import com.opus.syssupport.propertyfieldmap;
import java.util.ArrayList;
import java.util.logging.Logger;

import middlestripb.MongoLink ;
import middlestripb.EntityDescriptor ;
import middlestripb.EntityDescriptors ;
import middlestripb.QueuedDescriptors ;

import org.bson.Document;
import org.bson.types.ObjectId ;
import com.mongodb.client.model.Filters ;




public class Isotherm_pf extends Entity {

    private static final Logger log = Logger.getLogger(Isotherm_pf.class.getName());

    private transient MongoLink mongolink;

    private Long suid = 0L ;
    public Long getSuid() { return suid; }
    public void setSuid(Long suid) { this.suid = suid; }

    private ObjectId _id ;
    public ObjectId get_Id() { return _id; }
    public void set_Id(ObjectId _id) { this._id = _id; }

    Boolean cascade = false ;
    public Boolean getCascade() { return cascade; }
    public void setCascade(Boolean cascade) { this.cascade = cascade; }


    // Children Variables ==========================================================================================
    

    // Common / non Property / Private Variables ====================================================================
    private String owner = "instance" ;
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    private Integer index = 0 ;
    public Integer getIndex() { return index; }
    public void setIndex(Integer index) { this.index = index; }

    private Long parent = 0L ;
    public Long getParent() { return parent; }
    public void setParent(Long parent) { this.parent = parent; }

    

    //======================================= PROPERTIES ============================================================
    
       private String build_strat = "unloaded";

    public static final String PROP_BUILD_STRAT = "build_strat";

    @propertyfieldmap (propname = PROP_BUILD_STRAT )
    public String getBuild_Strat() {
        return build_strat;
    }

    
    public void setBuild_Strat(String build_strat ) {
        String oldbuild_strat = this.build_strat;
        this.build_strat = build_strat;
        propertyChangeSupport.firePropertyChange(PROP_BUILD_STRAT, oldbuild_strat, build_strat);
    }


       private String stab_strat = "unloaded";

    public static final String PROP_STAB_STRAT = "stab_strat";

    @propertyfieldmap (propname = PROP_STAB_STRAT )
    public String getStab_Strat() {
        return stab_strat;
    }

    
    public void setStab_Strat(String stab_strat ) {
        String oldstab_strat = this.stab_strat;
        this.stab_strat = stab_strat;
        propertyChangeSupport.firePropertyChange(PROP_STAB_STRAT, oldstab_strat, stab_strat);
    }






    // ========================================= CONSTRUCTOR & TOOLS =================================================
    public Isotherm_pf() {

    }


    private static Isotherm_pf instance;
    public static Isotherm_pf getInstance(Long parent){

        Object temp;
        instance = new Isotherm_pf();
        instance.mongolink = MongoLink.getInstance();
        instance.suid = instance.mongolink.getSuid();
        instance.setParent(parent);

        EntityDescriptor ed = new EntityDescriptor()
                .setSuid(instance.suid)
                .setInstance(instance);

        instance._descriptor = ed;

            

        EntityDescriptor ed1 = instance.mongolink.registerEntity(ed);
        if (ed1 != null){
            instance = (Isotherm_pf)ed1.getInstance();
            ed1.setStatus(EntityDescriptor.Status.UPDATE);
        }
        return instance;
    }

    @Override
    public String toString(){
        return String.valueOf(suid);
    }




    @Override
    public boolean loadChildren(boolean cascade, SMTraffic smm){

        EntityDescriptors ed = mongolink.getLoaded_descriptors();
        QueuedDescriptors tsk = mongolink.getTask_descriptors();
        boolean loadrequest = false;

        if (smm != null){
            EntityDescriptor rooted = new EntityDescriptor()
                    .setClazz(Isotherm_pf.class)
                    .setBson(Filters.eq("suid", suid))
                    .setCascade(Boolean.FALSE)
                    .setAction(smm)
                    .setLoaded(true)
                    .setSuid(suid);
            tsk.offer(rooted);
        }

            

        return loadrequest;
    }

    @Override
    public Document getDocument(){

        Document doc = new Document()
                .append ("suid", suid )
				.append ("cascade", cascade )
				.append ("owner", owner)
				.append ("index", index)
				.append ("parent", parent)
				.append ("build_strat", build_strat)
				.append ("stab_strat", stab_strat)

                ;
        return doc;
    }

    @Override
    public Entity loadEntity(Document document, EntityDescriptor ed){

        		_id = document.get("_id", ObjectId.class);
		suid = document.get("suid", Long.class);
		cascade = document.get("cascade", Boolean.class);
		owner = document.get("owner", String.class);
		index = document.get("index", Integer.class);
		parent = document.get("parent", Long.class);
		build_strat = document.get("build_strat", String.class);
		stab_strat = document.get("stab_strat", String.class);


        this.mongolink = MongoLink.getInstance();
        ed.setSuid(suid);

        EntityDescriptor ned = new EntityDescriptor()
                .setSuid(this.suid)
                .setInstance(this);

        this._descriptor = ned;
        mongolink.getLoaded_descriptors().add(ned);
        ed.setLoaded(true);
        
        return this;

    }



        


    //classcode_begin

    //classcode_end


}
