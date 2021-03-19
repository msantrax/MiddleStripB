
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


import Entities.Dose_pf;
import Entities.Phase ;
import Entities.Phase ;
import Entities.Phase ;


public class Dose extends Entity {

    private static final Logger log = Logger.getLogger(Dose.class.getName());

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
    private Object dose_pf;
    private Object buildp_phase;
    private Object stabinit_phase;
    private Object stabend_phase;
    

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
    
       private Integer dose_type = 0;

    public static final String PROP_DOSE_TYPE = "dose_type";

    @propertyfieldmap (propname = PROP_DOSE_TYPE )
    public Integer getDose_Type() {
        return dose_type;
    }

    @propertylink (propname = PROP_DOSE_TYPE, plink = "it_dose_type", input=false, callstate="DOSETYPE")
    public void setDose_Type(Integer dose_type ) {
        Integer olddose_type = this.dose_type;
        this.dose_type = dose_type;
        propertyChangeSupport.firePropertyChange(PROP_DOSE_TYPE, olddose_type, dose_type);
    }


       private Double ach_dp = 1.778;

    public static final String PROP_ACH_DP = "ach_dp";

    @propertyfieldmap (propname = PROP_ACH_DP )
    public Double getAch_Dp() {
        return ach_dp;
    }

    
    public void setAch_Dp(Double ach_dp ) {
        Double oldach_dp = this.ach_dp;
        this.ach_dp = ach_dp;
        propertyChangeSupport.firePropertyChange(PROP_ACH_DP, oldach_dp, ach_dp);
    }


       private Double ach_tol = 2.795;

    public static final String PROP_ACH_TOL = "ach_tol";

    @propertyfieldmap (propname = PROP_ACH_TOL )
    public Double getAch_Tol() {
        return ach_tol;
    }

    
    public void setAch_Tol(Double ach_tol ) {
        Double oldach_tol = this.ach_tol;
        this.ach_tol = ach_tol;
        propertyChangeSupport.firePropertyChange(PROP_ACH_TOL, oldach_tol, ach_tol);
    }


       private Double achieved = 63.627;

    public static final String PROP_ACHIEVED = "achieved";

    @propertyfieldmap (propname = PROP_ACHIEVED )
    public Double getAchieved() {
        return achieved;
    }

    
    public void setAchieved(Double achieved ) {
        Double oldachieved = this.achieved;
        this.achieved = achieved;
        propertyChangeSupport.firePropertyChange(PROP_ACHIEVED, oldachieved, achieved);
    }


       private Double target = 61.664;

    public static final String PROP_TARGET = "target";

    @propertyfieldmap (propname = PROP_TARGET )
    public Double getTarget() {
        return target;
    }

    
    public void setTarget(Double target ) {
        Double oldtarget = this.target;
        this.target = target;
        propertyChangeSupport.firePropertyChange(PROP_TARGET, oldtarget, target);
    }


       private Double initialp = 130.987;

    public static final String PROP_INITIALP = "initialp";

    @propertyfieldmap (propname = PROP_INITIALP )
    public Double getInitialp() {
        return initialp;
    }

    
    public void setInitialp(Double initialp ) {
        Double oldinitialp = this.initialp;
        this.initialp = initialp;
        propertyChangeSupport.firePropertyChange(PROP_INITIALP, oldinitialp, initialp);
    }


       private Long ts_init = 0L;

    public static final String PROP_TS_INIT = "ts_init";

    @propertyfieldmap (propname = PROP_TS_INIT )
    public Long getTs_Init() {
        return ts_init;
    }

    
    public void setTs_Init(Long ts_init ) {
        Long oldts_init = this.ts_init;
        this.ts_init = ts_init;
        propertyChangeSupport.firePropertyChange(PROP_TS_INIT, oldts_init, ts_init);
    }


       private Long ts_ach = 0L;

    public static final String PROP_TS_ACH = "ts_ach";

    @propertyfieldmap (propname = PROP_TS_ACH )
    public Long getTs_Ach() {
        return ts_ach;
    }

    
    public void setTs_Ach(Long ts_ach ) {
        Long oldts_ach = this.ts_ach;
        this.ts_ach = ts_ach;
        propertyChangeSupport.firePropertyChange(PROP_TS_ACH, oldts_ach, ts_ach);
    }


       private Long ts_stabinit = 0L;

    public static final String PROP_TS_STABINIT = "ts_stabinit";

    @propertyfieldmap (propname = PROP_TS_STABINIT )
    public Long getTs_Stabinit() {
        return ts_stabinit;
    }

    
    public void setTs_Stabinit(Long ts_stabinit ) {
        Long oldts_stabinit = this.ts_stabinit;
        this.ts_stabinit = ts_stabinit;
        propertyChangeSupport.firePropertyChange(PROP_TS_STABINIT, oldts_stabinit, ts_stabinit);
    }


       private Long ts_stabend = 0L;

    public static final String PROP_TS_STABEND = "ts_stabend";

    @propertyfieldmap (propname = PROP_TS_STABEND )
    public Long getTs_Stabend() {
        return ts_stabend;
    }

    
    public void setTs_Stabend(Long ts_stabend ) {
        Long oldts_stabend = this.ts_stabend;
        this.ts_stabend = ts_stabend;
        propertyChangeSupport.firePropertyChange(PROP_TS_STABEND, oldts_stabend, ts_stabend);
    }


       private Boolean redose = false;

    public static final String PROP_REDOSE = "redose";

    @propertyfieldmap (propname = PROP_REDOSE )
    public Boolean getRedose() {
        return redose;
    }

    
    public void setRedose(Boolean redose ) {
        Boolean oldredose = this.redose;
        this.redose = redose;
        propertyChangeSupport.firePropertyChange(PROP_REDOSE, oldredose, redose);
    }


       private Double redose_p2 = 48.997;

    public static final String PROP_REDOSE_P2 = "redose_p2";

    @propertyfieldmap (propname = PROP_REDOSE_P2 )
    public Double getRedose_P2() {
        return redose_p2;
    }

    
    public void setRedose_P2(Double redose_p2 ) {
        Double oldredose_p2 = this.redose_p2;
        this.redose_p2 = redose_p2;
        propertyChangeSupport.firePropertyChange(PROP_REDOSE_P2, oldredose_p2, redose_p2);
    }


       private Double redose_time = 33.0;

    public static final String PROP_REDOSE_TIME = "redose_time";

    @propertyfieldmap (propname = PROP_REDOSE_TIME )
    public Double getRedose_Time() {
        return redose_time;
    }

    
    public void setRedose_Time(Double redose_time ) {
        Double oldredose_time = this.redose_time;
        this.redose_time = redose_time;
        propertyChangeSupport.firePropertyChange(PROP_REDOSE_TIME, oldredose_time, redose_time);
    }


       private Boolean equilibrium = false;

    public static final String PROP_EQUILIBRIUM = "equilibrium";

    @propertyfieldmap (propname = PROP_EQUILIBRIUM )
    public Boolean getEquilibrium() {
        return equilibrium;
    }

    
    public void setEquilibrium(Boolean equilibrium ) {
        Boolean oldequilibrium = this.equilibrium;
        this.equilibrium = equilibrium;
        propertyChangeSupport.firePropertyChange(PROP_EQUILIBRIUM, oldequilibrium, equilibrium);
    }


       private Double equilibrium_dp = 0.0;

    public static final String PROP_EQUILIBRIUM_DP = "equilibrium_dp";

    @propertyfieldmap (propname = PROP_EQUILIBRIUM_DP )
    public Double getEquilibrium_Dp() {
        return equilibrium_dp;
    }

    
    public void setEquilibrium_Dp(Double equilibrium_dp ) {
        Double oldequilibrium_dp = this.equilibrium_dp;
        this.equilibrium_dp = equilibrium_dp;
        propertyChangeSupport.firePropertyChange(PROP_EQUILIBRIUM_DP, oldequilibrium_dp, equilibrium_dp);
    }


       private Double equilibrium_count = 0.0;

    public static final String PROP_EQUILIBRIUM_COUNT = "equilibrium_count";

    @propertyfieldmap (propname = PROP_EQUILIBRIUM_COUNT )
    public Double getEquilibrium_Count() {
        return equilibrium_count;
    }

    
    public void setEquilibrium_Count(Double equilibrium_count ) {
        Double oldequilibrium_count = this.equilibrium_count;
        this.equilibrium_count = equilibrium_count;
        propertyChangeSupport.firePropertyChange(PROP_EQUILIBRIUM_COUNT, oldequilibrium_count, equilibrium_count);
    }






    // ========================================= CONSTRUCTOR & TOOLS =================================================
    public Dose() {

    }


    private static Dose instance;
    public static Dose getInstance(Long parent){

        Object temp;
        instance = new Dose();
        instance.mongolink = MongoLink.getInstance();
        instance.suid = instance.mongolink.getSuid();
        instance.setParent(parent);

        EntityDescriptor ed = new EntityDescriptor()
                .setSuid(instance.suid)
                .setInstance(instance);

        instance._descriptor = ed;

            
        temp = Dose_pf.getInstance(parent).getSuid();
        instance.dose_pf = temp ;
 
        temp = Phase.getInstance(parent).getSuid();
        instance.buildp_phase = temp ;
 
        temp = Phase.getInstance(parent).getSuid();
        instance.stabinit_phase = temp ;
 
        temp = Phase.getInstance(parent).getSuid();
        instance.stabend_phase = temp ;
 

        EntityDescriptor ed1 = instance.mongolink.registerEntity(ed);
        if (ed1 != null){
            instance = (Dose)ed1.getInstance();
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
                    .setClazz(Dose.class)
                    .setBson(Filters.eq("suid", suid))
                    .setCascade(Boolean.FALSE)
                    .setAction(smm)
                    .setLoaded(true)
                    .setSuid(suid);
            tsk.offer(rooted);
        }

            
        if (dose_pf instanceof Long){
                Dose_pf t_dose_pf = (Dose_pf)ed.findById((Long)dose_pf, true);
                if (t_dose_pf != null) {
                    dose_pf = t_dose_pf;
                    if (cascade) t_dose_pf.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Dose_pf.class)
                            .setBson(Filters.eq("suid", (Long)dose_pf))
                            .setCascade(cascade));
                }
            }
 
        if (buildp_phase instanceof Long){
                Phase t_buildp_phase = (Phase)ed.findById((Long)buildp_phase, true);
                if (t_buildp_phase != null) {
                    buildp_phase = t_buildp_phase;
                    if (cascade) t_buildp_phase.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Phase.class)
                            .setBson(Filters.eq("suid", (Long)buildp_phase))
                            .setCascade(cascade));
                }
            }
 
        if (stabinit_phase instanceof Long){
                Phase t_stabinit_phase = (Phase)ed.findById((Long)stabinit_phase, true);
                if (t_stabinit_phase != null) {
                    stabinit_phase = t_stabinit_phase;
                    if (cascade) t_stabinit_phase.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Phase.class)
                            .setBson(Filters.eq("suid", (Long)stabinit_phase))
                            .setCascade(cascade));
                }
            }
 
        if (stabend_phase instanceof Long){
                Phase t_stabend_phase = (Phase)ed.findById((Long)stabend_phase, true);
                if (t_stabend_phase != null) {
                    stabend_phase = t_stabend_phase;
                    if (cascade) t_stabend_phase.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Phase.class)
                            .setBson(Filters.eq("suid", (Long)stabend_phase))
                            .setCascade(cascade));
                }
            }
 

        return loadrequest;
    }

    @Override
    public Document getDocument(){

        Document doc = new Document()
                .append ("suid", suid )
				.append ("cascade", cascade )
				.append ("dose_pf", (dose_pf instanceof Long) ? dose_pf : Long.parseLong(dose_pf.toString()) )
				.append ("buildp_phase", (buildp_phase instanceof Long) ? buildp_phase : Long.parseLong(buildp_phase.toString()) )
				.append ("stabinit_phase", (stabinit_phase instanceof Long) ? stabinit_phase : Long.parseLong(stabinit_phase.toString()) )
				.append ("stabend_phase", (stabend_phase instanceof Long) ? stabend_phase : Long.parseLong(stabend_phase.toString()) )
				.append ("owner", owner)
				.append ("index", index)
				.append ("parent", parent)
				.append ("dose_type", dose_type)
				.append ("ach_dp", ach_dp)
				.append ("ach_tol", ach_tol)
				.append ("achieved", achieved)
				.append ("target", target)
				.append ("initialp", initialp)
				.append ("ts_init", ts_init)
				.append ("ts_ach", ts_ach)
				.append ("ts_stabinit", ts_stabinit)
				.append ("ts_stabend", ts_stabend)
				.append ("redose", redose)
				.append ("redose_p2", redose_p2)
				.append ("redose_time", redose_time)
				.append ("equilibrium", equilibrium)
				.append ("equilibrium_dp", equilibrium_dp)
				.append ("equilibrium_count", equilibrium_count)

                ;
        return doc;
    }

    @Override
    public Entity loadEntity(Document document, EntityDescriptor ed){

        _id = document.get("_id", ObjectId.class);
		suid = document.get("suid", Long.class);
		cascade = document.get("cascade", Boolean.class);
		dose_pf = document.get("dose_pf", Long.class);
		buildp_phase = document.get("buildp_phase", Long.class);
		stabinit_phase = document.get("stabinit_phase", Long.class);
		stabend_phase = document.get("stabend_phase", Long.class);
		owner = document.get("owner", String.class);
		index = document.get("index", Integer.class);
		parent = document.get("parent", Long.class);
		dose_type = document.get("dose_type", Integer.class);
		ach_dp = document.get("ach_dp", Double.class);
		ach_tol = document.get("ach_tol", Double.class);
		achieved = document.get("achieved", Double.class);
		target = document.get("target", Double.class);
		initialp = document.get("initialp", Double.class);
		ts_init = document.get("ts_init", Long.class);
		ts_ach = document.get("ts_ach", Long.class);
		ts_stabinit = document.get("ts_stabinit", Long.class);
		ts_stabend = document.get("ts_stabend", Long.class);
		redose = document.get("redose", Boolean.class);
		redose_p2 = document.get("redose_p2", Double.class);
		redose_time = document.get("redose_time", Double.class);
		equilibrium = document.get("equilibrium", Boolean.class);
		equilibrium_dp = document.get("equilibrium_dp", Double.class);
		equilibrium_count = document.get("equilibrium_count", Double.class);


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
