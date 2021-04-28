
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

import java.util.ArrayList;
import Entities.CalcP0_pf;
import Entities.Dose_pf;


public class CalcP0 extends Entity {

    private static final Logger log = Logger.getLogger(CalcP0.class.getName());

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
    public Object calcp0_pf;
    public Object dose_pf;
    

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
    
       private String desc = "CalcP0 Run Default";

    public static final String PROP_DESC = "desc";

    @propertyfieldmap (propname = PROP_DESC )
    public String getDesc() {
        return desc;
    }

    
    public void setDesc(String desc ) {
        String olddesc = this.desc;
        this.desc = desc;
        propertyChangeSupport.firePropertyChange(PROP_DESC, olddesc, desc);
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


       private Long ts_end = 0L;

    public static final String PROP_TS_END = "ts_end";

    @propertyfieldmap (propname = PROP_TS_END )
    public Long getTs_End() {
        return ts_end;
    }

    
    public void setTs_End(Long ts_end ) {
        Long oldts_end = this.ts_end;
        this.ts_end = ts_end;
        propertyChangeSupport.firePropertyChange(PROP_TS_END, oldts_end, ts_end);
    }


       private Double p0 = 0.0;

    public static final String PROP_P0 = "p0";

    @propertyfieldmap (propname = PROP_P0 )
    public Double getP0() {
        return p0;
    }

    
    public void setP0(Double p0 ) {
        Double oldp0 = this.p0;
        this.p0 = p0;
        propertyChangeSupport.firePropertyChange(PROP_P0, oldp0, p0);
    }


       private Double dp = 0.0;

    public static final String PROP_DP = "dp";

    @propertyfieldmap (propname = PROP_DP )
    public Double getDp() {
        return dp;
    }

    
    public void setDp(Double dp ) {
        Double olddp = this.dp;
        this.dp = dp;
        propertyChangeSupport.firePropertyChange(PROP_DP, olddp, dp);
    }


       private Double temp = 23.4;

    public static final String PROP_TEMP = "temp";

    @propertyfieldmap (propname = PROP_TEMP )
    public Double getTemp() {
        return temp;
    }

    
    public void setTemp(Double temp ) {
        Double oldtemp = this.temp;
        this.temp = temp;
        propertyChangeSupport.firePropertyChange(PROP_TEMP, oldtemp, temp);
    }


       private Boolean routine = false;

    public static final String PROP_ROUTINE = "routine";

    @propertyfieldmap (propname = PROP_ROUTINE )
    public Boolean getRoutine() {
        return routine;
    }

    
    public void setRoutine(Boolean routine ) {
        Boolean oldroutine = this.routine;
        this.routine = routine;
        propertyChangeSupport.firePropertyChange(PROP_ROUTINE, oldroutine, routine);
    }


       private ArrayList<Double> tsbuffer = new ArrayList<>();

    public static final String PROP_TSBUFFER = "tsbuffer";

    @propertyfieldmap (propname = PROP_TSBUFFER )
    public ArrayList<Double> getTsbuffer() {
        return tsbuffer;
    }

    
    public void setTsbuffer(ArrayList<Double> tsbuffer ) {
        ArrayList<Double> oldtsbuffer = this.tsbuffer;
        this.tsbuffer = tsbuffer;
        propertyChangeSupport.firePropertyChange(PROP_TSBUFFER, oldtsbuffer, tsbuffer);
    }


       private ArrayList<Double> valbuffer = new ArrayList<>();

    public static final String PROP_VALBUFFER = "valbuffer";

    @propertyfieldmap (propname = PROP_VALBUFFER )
    public ArrayList<Double> getValbuffer() {
        return valbuffer;
    }

    
    public void setValbuffer(ArrayList<Double> valbuffer ) {
        ArrayList<Double> oldvalbuffer = this.valbuffer;
        this.valbuffer = valbuffer;
        propertyChangeSupport.firePropertyChange(PROP_VALBUFFER, oldvalbuffer, valbuffer);
    }


       private ArrayList<Double> comp1buffer = new ArrayList<>();

    public static final String PROP_COMP1BUFFER = "comp1buffer";

    @propertyfieldmap (propname = PROP_COMP1BUFFER )
    public ArrayList<Double> getComp1Buffer() {
        return comp1buffer;
    }

    
    public void setComp1Buffer(ArrayList<Double> comp1buffer ) {
        ArrayList<Double> oldcomp1buffer = this.comp1buffer;
        this.comp1buffer = comp1buffer;
        propertyChangeSupport.firePropertyChange(PROP_COMP1BUFFER, oldcomp1buffer, comp1buffer);
    }


       private ArrayList<Double> comp2buffer = new ArrayList<>();

    public static final String PROP_COMP2BUFFER = "comp2buffer";

    @propertyfieldmap (propname = PROP_COMP2BUFFER )
    public ArrayList<Double> getComp2Buffer() {
        return comp2buffer;
    }

    
    public void setComp2Buffer(ArrayList<Double> comp2buffer ) {
        ArrayList<Double> oldcomp2buffer = this.comp2buffer;
        this.comp2buffer = comp2buffer;
        propertyChangeSupport.firePropertyChange(PROP_COMP2BUFFER, oldcomp2buffer, comp2buffer);
    }


       private ArrayList<Object> events = new ArrayList<>();

    public static final String PROP_EVENTS = "events";

    @propertyfieldmap (propname = PROP_EVENTS )
    public ArrayList<Object> getEvents() {
        return events;
    }

    
    public void setEvents(ArrayList<Object> events ) {
        ArrayList<Object> oldevents = this.events;
        this.events = events;
        propertyChangeSupport.firePropertyChange(PROP_EVENTS, oldevents, events);
    }






    // ========================================= CONSTRUCTOR & TOOLS =================================================
    public CalcP0() {

    }


    private static CalcP0 instance;
    public static CalcP0 getInstance(Long parent){

        Object temp;
        instance = new CalcP0();
        instance.mongolink = MongoLink.getInstance();
        instance.suid = instance.mongolink.getSuid();
        instance.setParent(parent);

        EntityDescriptor ed = new EntityDescriptor()
                .setSuid(instance.suid)
                .setInstance(instance);

        instance._descriptor = ed;

            
        temp = CalcP0_pf.getInstance(parent).getSuid();
        instance.calcp0_pf = temp ;
 
        temp = Dose_pf.getInstance(parent).getSuid();
        instance.dose_pf = temp ;
 

        EntityDescriptor ed1 = instance.mongolink.registerEntity(ed);
        if (ed1 != null){
            instance = (CalcP0)ed1.getInstance();
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
                    .setClazz(CalcP0.class)
                    .setBson(Filters.eq("suid", suid))
                    .setCascade(Boolean.FALSE)
                    .setAction(smm)
                    .setLoaded(true)
                    .setSuid(suid);
            tsk.offer(rooted);
        }

            
        if (calcp0_pf instanceof Long){
                CalcP0_pf t_calcp0_pf = (CalcP0_pf)ed.findById((Long)calcp0_pf, true);
                if (t_calcp0_pf != null) {
                    calcp0_pf = t_calcp0_pf;
                    if (cascade) t_calcp0_pf.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(CalcP0_pf.class)
                            .setBson(Filters.eq("suid", (Long)calcp0_pf))
                            .setCascade(cascade));
                }
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
 

        return loadrequest;
    }

    @Override
    public Document getDocument(){

        Document doc = new Document()
                .append ("suid", suid )
				.append ("cascade", cascade )
				.append ("calcp0_pf", (calcp0_pf instanceof Long) ? calcp0_pf : Long.parseLong(calcp0_pf.toString()) )
				.append ("dose_pf", (dose_pf instanceof Long) ? dose_pf : Long.parseLong(dose_pf.toString()) )
				.append ("owner", owner)
				.append ("index", index)
				.append ("parent", parent)
				.append ("desc", desc)
				.append ("ts_init", ts_init)
				.append ("ts_end", ts_end)
				.append ("p0", p0)
				.append ("dp", dp)
				.append ("temp", temp)
				.append ("routine", routine)
				.append ("tsbuffer", tsbuffer)
				.append ("valbuffer", valbuffer)
				.append ("comp1buffer", comp1buffer)
				.append ("comp2buffer", comp2buffer)
				.append ("events", events)

                ;
        return doc;
    }

    @Override
    public Entity loadEntity(Document document, EntityDescriptor ed){

        try {
                		_id = document.get("_id", ObjectId.class);
		suid = document.get("suid", Long.class);
		cascade = document.get("cascade", Boolean.class);
		calcp0_pf = document.get("calcp0_pf", Long.class);
		dose_pf = document.get("dose_pf", Long.class);
		owner = document.get("owner", String.class);
		index = document.get("index", Integer.class);
		parent = document.get("parent", Long.class);
		desc = document.get("desc", String.class);
		ts_init = document.get("ts_init", Long.class);
		ts_end = document.get("ts_end", Long.class);
		p0 = document.get("p0", Double.class);
		dp = document.get("dp", Double.class);
		temp = document.get("temp", Double.class);
		routine = document.get("routine", Boolean.class);
		tsbuffer = document.get("tsbuffer", ArrayList.class);
		valbuffer = document.get("valbuffer", ArrayList.class);
		comp1buffer = document.get("comp1buffer", ArrayList.class);
		comp2buffer = document.get("comp2buffer", ArrayList.class);
		events = document.get("events", ArrayList.class);

        }
        catch (Exception ex) {
            log.severe(String.format("loadEntity failed to load due %s ", ex.getMessage()));
        }

        this.mongolink = MongoLink.getInstance();
        ed.setSuid(suid);

        EntityDescriptor ned = new EntityDescriptor()
                .setSuid(this.suid)
                .setInstance(this);

        this._descriptor = ned;
        mongolink.getLoaded_descriptors().add(ned);
        ed.setLoaded(true);
        this.addPropertyChangeListener(mongolink);
        
        return this;

    }



        


    //classcode_begin

    //classcode_end


}
