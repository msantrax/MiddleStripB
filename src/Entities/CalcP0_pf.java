
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




public class CalcP0_pf extends Entity {

    private static final Logger log = Logger.getLogger(CalcP0_pf.class.getName());

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
    
       private String desc = "CalcP0 Default Config";

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


       private Double bprs = 800.0;

    public static final String PROP_BPRS = "bprs";

    @propertyfieldmap (propname = PROP_BPRS )
    public Double getBprs() {
        return bprs;
    }

    
    public void setBprs(Double bprs ) {
        Double oldbprs = this.bprs;
        this.bprs = bprs;
        propertyChangeSupport.firePropertyChange(PROP_BPRS, oldbprs, bprs);
    }


       private Double bprs_thrs = 1.0;

    public static final String PROP_BPRS_THRS = "bprs_thrs";

    @propertyfieldmap (propname = PROP_BPRS_THRS )
    public Double getBprs_Thrs() {
        return bprs_thrs;
    }

    
    public void setBprs_Thrs(Double bprs_thrs ) {
        Double oldbprs_thrs = this.bprs_thrs;
        this.bprs_thrs = bprs_thrs;
        propertyChangeSupport.firePropertyChange(PROP_BPRS_THRS, oldbprs_thrs, bprs_thrs);
    }


       private Long bprs_stabtime = 3000L;

    public static final String PROP_BPRS_STABTIME = "bprs_stabtime";

    @propertyfieldmap (propname = PROP_BPRS_STABTIME )
    public Long getBprs_Stabtime() {
        return bprs_stabtime;
    }

    
    public void setBprs_Stabtime(Long bprs_stabtime ) {
        Long oldbprs_stabtime = this.bprs_stabtime;
        this.bprs_stabtime = bprs_stabtime;
        propertyChangeSupport.firePropertyChange(PROP_BPRS_STABTIME, oldbprs_stabtime, bprs_stabtime);
    }


       private Long bprs_timeout = 20000L;

    public static final String PROP_BPRS_TIMEOUT = "bprs_timeout";

    @propertyfieldmap (propname = PROP_BPRS_TIMEOUT )
    public Long getBprs_Timeout() {
        return bprs_timeout;
    }

    
    public void setBprs_Timeout(Long bprs_timeout ) {
        Long oldbprs_timeout = this.bprs_timeout;
        this.bprs_timeout = bprs_timeout;
        propertyChangeSupport.firePropertyChange(PROP_BPRS_TIMEOUT, oldbprs_timeout, bprs_timeout);
    }


       private Double p0max = 780.0;

    public static final String PROP_P0MAX = "p0max";

    @propertyfieldmap (propname = PROP_P0MAX )
    public Double getP0Max() {
        return p0max;
    }

    
    public void setP0Max(Double p0max ) {
        Double oldp0max = this.p0max;
        this.p0max = p0max;
        propertyChangeSupport.firePropertyChange(PROP_P0MAX, oldp0max, p0max);
    }


       private Double p0min = 710.0;

    public static final String PROP_P0MIN = "p0min";

    @propertyfieldmap (propname = PROP_P0MIN )
    public Double getP0Min() {
        return p0min;
    }

    
    public void setP0Min(Double p0min ) {
        Double oldp0min = this.p0min;
        this.p0min = p0min;
        propertyChangeSupport.firePropertyChange(PROP_P0MIN, oldp0min, p0min);
    }


       private Double p0_thrs = 1.0;

    public static final String PROP_P0_THRS = "p0_thrs";

    @propertyfieldmap (propname = PROP_P0_THRS )
    public Double getP0_Thrs() {
        return p0_thrs;
    }

    
    public void setP0_Thrs(Double p0_thrs ) {
        Double oldp0_thrs = this.p0_thrs;
        this.p0_thrs = p0_thrs;
        propertyChangeSupport.firePropertyChange(PROP_P0_THRS, oldp0_thrs, p0_thrs);
    }


       private Long p0_stabtime = 3000L;

    public static final String PROP_P0_STABTIME = "p0_stabtime";

    @propertyfieldmap (propname = PROP_P0_STABTIME )
    public Long getP0_Stabtime() {
        return p0_stabtime;
    }

    
    public void setP0_Stabtime(Long p0_stabtime ) {
        Long oldp0_stabtime = this.p0_stabtime;
        this.p0_stabtime = p0_stabtime;
        propertyChangeSupport.firePropertyChange(PROP_P0_STABTIME, oldp0_stabtime, p0_stabtime);
    }


       private Long p0_timeout = 20000L;

    public static final String PROP_P0_TIMEOUT = "p0_timeout";

    @propertyfieldmap (propname = PROP_P0_TIMEOUT )
    public Long getP0_Timeout() {
        return p0_timeout;
    }

    
    public void setP0_Timeout(Long p0_timeout ) {
        Long oldp0_timeout = this.p0_timeout;
        this.p0_timeout = p0_timeout;
        propertyChangeSupport.firePropertyChange(PROP_P0_TIMEOUT, oldp0_timeout, p0_timeout);
    }


       private Double bgvalue_max = 900.0;

    public static final String PROP_BGVALUE_MAX = "bgvalue_max";

    @propertyfieldmap (propname = PROP_BGVALUE_MAX )
    public Double getBgvalue_Max() {
        return bgvalue_max;
    }

    
    public void setBgvalue_Max(Double bgvalue_max ) {
        Double oldbgvalue_max = this.bgvalue_max;
        this.bgvalue_max = bgvalue_max;
        propertyChangeSupport.firePropertyChange(PROP_BGVALUE_MAX, oldbgvalue_max, bgvalue_max);
    }


       private Double bgvalue_min = 0.0;

    public static final String PROP_BGVALUE_MIN = "bgvalue_min";

    @propertyfieldmap (propname = PROP_BGVALUE_MIN )
    public Double getBgvalue_Min() {
        return bgvalue_min;
    }

    
    public void setBgvalue_Min(Double bgvalue_min ) {
        Double oldbgvalue_min = this.bgvalue_min;
        this.bgvalue_min = bgvalue_min;
        propertyChangeSupport.firePropertyChange(PROP_BGVALUE_MIN, oldbgvalue_min, bgvalue_min);
    }


       private Double bgvalue_ok = 10.0;

    public static final String PROP_BGVALUE_OK = "bgvalue_ok";

    @propertyfieldmap (propname = PROP_BGVALUE_OK )
    public Double getBgvalue_Ok() {
        return bgvalue_ok;
    }

    
    public void setBgvalue_Ok(Double bgvalue_ok ) {
        Double oldbgvalue_ok = this.bgvalue_ok;
        this.bgvalue_ok = bgvalue_ok;
        propertyChangeSupport.firePropertyChange(PROP_BGVALUE_OK, oldbgvalue_ok, bgvalue_ok);
    }


       private Double bgdv_max = 20.0;

    public static final String PROP_BGDV_MAX = "bgdv_max";

    @propertyfieldmap (propname = PROP_BGDV_MAX )
    public Double getBgdv_Max() {
        return bgdv_max;
    }

    
    public void setBgdv_Max(Double bgdv_max ) {
        Double oldbgdv_max = this.bgdv_max;
        this.bgdv_max = bgdv_max;
        propertyChangeSupport.firePropertyChange(PROP_BGDV_MAX, oldbgdv_max, bgdv_max);
    }


       private Double bgdv_min = 0.0;

    public static final String PROP_BGDV_MIN = "bgdv_min";

    @propertyfieldmap (propname = PROP_BGDV_MIN )
    public Double getBgdv_Min() {
        return bgdv_min;
    }

    
    public void setBgdv_Min(Double bgdv_min ) {
        Double oldbgdv_min = this.bgdv_min;
        this.bgdv_min = bgdv_min;
        propertyChangeSupport.firePropertyChange(PROP_BGDV_MIN, oldbgdv_min, bgdv_min);
    }


       private Double bgdv_ok = 5.0;

    public static final String PROP_BGDV_OK = "bgdv_ok";

    @propertyfieldmap (propname = PROP_BGDV_OK )
    public Double getBgdv_Ok() {
        return bgdv_ok;
    }

    
    public void setBgdv_Ok(Double bgdv_ok ) {
        Double oldbgdv_ok = this.bgdv_ok;
        this.bgdv_ok = bgdv_ok;
        propertyChangeSupport.firePropertyChange(PROP_BGDV_OK, oldbgdv_ok, bgdv_ok);
    }


       private Double bgvar_max = 0.5;

    public static final String PROP_BGVAR_MAX = "bgvar_max";

    @propertyfieldmap (propname = PROP_BGVAR_MAX )
    public Double getBgvar_Max() {
        return bgvar_max;
    }

    
    public void setBgvar_Max(Double bgvar_max ) {
        Double oldbgvar_max = this.bgvar_max;
        this.bgvar_max = bgvar_max;
        propertyChangeSupport.firePropertyChange(PROP_BGVAR_MAX, oldbgvar_max, bgvar_max);
    }


       private Double bgvar_ok = 10.0;

    public static final String PROP_BGVAR_OK = "bgvar_ok";

    @propertyfieldmap (propname = PROP_BGVAR_OK )
    public Double getBgvar_Ok() {
        return bgvar_ok;
    }

    
    public void setBgvar_Ok(Double bgvar_ok ) {
        Double oldbgvar_ok = this.bgvar_ok;
        this.bgvar_ok = bgvar_ok;
        propertyChangeSupport.firePropertyChange(PROP_BGVAR_OK, oldbgvar_ok, bgvar_ok);
    }


       private String chrt_header = "Check P0 Pressure";

    public static final String PROP_CHRT_HEADER = "chrt_header";

    @propertyfieldmap (propname = PROP_CHRT_HEADER )
    public String getChrt_Header() {
        return chrt_header;
    }

    
    public void setChrt_Header(String chrt_header ) {
        String oldchrt_header = this.chrt_header;
        this.chrt_header = chrt_header;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_HEADER, oldchrt_header, chrt_header);
    }


       private String chrt_xlabel = "Time - (sec)";

    public static final String PROP_CHRT_XLABEL = "chrt_xlabel";

    @propertyfieldmap (propname = PROP_CHRT_XLABEL )
    public String getChrt_Xlabel() {
        return chrt_xlabel;
    }

    
    public void setChrt_Xlabel(String chrt_xlabel ) {
        String oldchrt_xlabel = this.chrt_xlabel;
        this.chrt_xlabel = chrt_xlabel;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_XLABEL, oldchrt_xlabel, chrt_xlabel);
    }


       private String chrt_ymainlabel = "Pressure (mmHg)";

    public static final String PROP_CHRT_YMAINLABEL = "chrt_ymainlabel";

    @propertyfieldmap (propname = PROP_CHRT_YMAINLABEL )
    public String getChrt_Ymainlabel() {
        return chrt_ymainlabel;
    }

    
    public void setChrt_Ymainlabel(String chrt_ymainlabel ) {
        String oldchrt_ymainlabel = this.chrt_ymainlabel;
        this.chrt_ymainlabel = chrt_ymainlabel;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_YMAINLABEL, oldchrt_ymainlabel, chrt_ymainlabel);
    }


       private String chrt_ycomplabel = "\u0394P̣";

    public static final String PROP_CHRT_YCOMPLABEL = "chrt_ycomplabel";

    @propertyfieldmap (propname = PROP_CHRT_YCOMPLABEL )
    public String getChrt_Ycomplabel() {
        return chrt_ycomplabel;
    }

    
    public void setChrt_Ycomplabel(String chrt_ycomplabel ) {
        String oldchrt_ycomplabel = this.chrt_ycomplabel;
        this.chrt_ycomplabel = chrt_ycomplabel;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_YCOMPLABEL, oldchrt_ycomplabel, chrt_ycomplabel);
    }


       private Double chrt_ymainmax = 1000.0;

    public static final String PROP_CHRT_YMAINMAX = "chrt_ymainmax";

    @propertyfieldmap (propname = PROP_CHRT_YMAINMAX )
    public Double getChrt_Ymainmax() {
        return chrt_ymainmax;
    }

    
    public void setChrt_Ymainmax(Double chrt_ymainmax ) {
        Double oldchrt_ymainmax = this.chrt_ymainmax;
        this.chrt_ymainmax = chrt_ymainmax;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_YMAINMAX, oldchrt_ymainmax, chrt_ymainmax);
    }


       private Double chrt_ymainmin = 0.0;

    public static final String PROP_CHRT_YMAINMIN = "chrt_ymainmin";

    @propertyfieldmap (propname = PROP_CHRT_YMAINMIN )
    public Double getChrt_Ymainmin() {
        return chrt_ymainmin;
    }

    
    public void setChrt_Ymainmin(Double chrt_ymainmin ) {
        Double oldchrt_ymainmin = this.chrt_ymainmin;
        this.chrt_ymainmin = chrt_ymainmin;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_YMAINMIN, oldchrt_ymainmin, chrt_ymainmin);
    }


       private Double chrt_xmainmax = 30.0;

    public static final String PROP_CHRT_XMAINMAX = "chrt_xmainmax";

    @propertyfieldmap (propname = PROP_CHRT_XMAINMAX )
    public Double getChrt_Xmainmax() {
        return chrt_xmainmax;
    }

    
    public void setChrt_Xmainmax(Double chrt_xmainmax ) {
        Double oldchrt_xmainmax = this.chrt_xmainmax;
        this.chrt_xmainmax = chrt_xmainmax;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_XMAINMAX, oldchrt_xmainmax, chrt_xmainmax);
    }


       private Double chrt_xmainmin = 0.0;

    public static final String PROP_CHRT_XMAINMIN = "chrt_xmainmin";

    @propertyfieldmap (propname = PROP_CHRT_XMAINMIN )
    public Double getChrt_Xmainmin() {
        return chrt_xmainmin;
    }

    
    public void setChrt_Xmainmin(Double chrt_xmainmin ) {
        Double oldchrt_xmainmin = this.chrt_xmainmin;
        this.chrt_xmainmin = chrt_xmainmin;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_XMAINMIN, oldchrt_xmainmin, chrt_xmainmin);
    }


       private Double chrt_ycompmax = 5.0;

    public static final String PROP_CHRT_YCOMPMAX = "chrt_ycompmax";

    @propertyfieldmap (propname = PROP_CHRT_YCOMPMAX )
    public Double getChrt_Ycompmax() {
        return chrt_ycompmax;
    }

    
    public void setChrt_Ycompmax(Double chrt_ycompmax ) {
        Double oldchrt_ycompmax = this.chrt_ycompmax;
        this.chrt_ycompmax = chrt_ycompmax;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_YCOMPMAX, oldchrt_ycompmax, chrt_ycompmax);
    }


       private Double chrt_ycompmin = 0.0;

    public static final String PROP_CHRT_YCOMPMIN = "chrt_ycompmin";

    @propertyfieldmap (propname = PROP_CHRT_YCOMPMIN )
    public Double getChrt_Ycompmin() {
        return chrt_ycompmin;
    }

    
    public void setChrt_Ycompmin(Double chrt_ycompmin ) {
        Double oldchrt_ycompmin = this.chrt_ycompmin;
        this.chrt_ycompmin = chrt_ycompmin;
        propertyChangeSupport.firePropertyChange(PROP_CHRT_YCOMPMIN, oldchrt_ycompmin, chrt_ycompmin);
    }






    // ========================================= CONSTRUCTOR & TOOLS =================================================
    public CalcP0_pf() {

    }


    private static CalcP0_pf instance;
    public static CalcP0_pf getInstance(Long parent){

        Object temp;
        instance = new CalcP0_pf();
        instance.mongolink = MongoLink.getInstance();
        instance.suid = instance.mongolink.getSuid();
        instance.setParent(parent);

        EntityDescriptor ed = new EntityDescriptor()
                .setSuid(instance.suid)
                .setInstance(instance);

        instance._descriptor = ed;

            

        EntityDescriptor ed1 = instance.mongolink.registerEntity(ed);
        if (ed1 != null){
            instance = (CalcP0_pf)ed1.getInstance();
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
                    .setClazz(CalcP0_pf.class)
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
				.append ("desc", desc)
				.append ("bprs", bprs)
				.append ("bprs_thrs", bprs_thrs)
				.append ("bprs_stabtime", bprs_stabtime)
				.append ("bprs_timeout", bprs_timeout)
				.append ("p0max", p0max)
				.append ("p0min", p0min)
				.append ("p0_thrs", p0_thrs)
				.append ("p0_stabtime", p0_stabtime)
				.append ("p0_timeout", p0_timeout)
				.append ("bgvalue_max", bgvalue_max)
				.append ("bgvalue_min", bgvalue_min)
				.append ("bgvalue_ok", bgvalue_ok)
				.append ("bgdv_max", bgdv_max)
				.append ("bgdv_min", bgdv_min)
				.append ("bgdv_ok", bgdv_ok)
				.append ("bgvar_max", bgvar_max)
				.append ("bgvar_ok", bgvar_ok)
				.append ("chrt_header", chrt_header)
				.append ("chrt_xlabel", chrt_xlabel)
				.append ("chrt_ymainlabel", chrt_ymainlabel)
				.append ("chrt_ycomplabel", chrt_ycomplabel)
				.append ("chrt_ymainmax", chrt_ymainmax)
				.append ("chrt_ymainmin", chrt_ymainmin)
				.append ("chrt_xmainmax", chrt_xmainmax)
				.append ("chrt_xmainmin", chrt_xmainmin)
				.append ("chrt_ycompmax", chrt_ycompmax)
				.append ("chrt_ycompmin", chrt_ycompmin)

                ;
        return doc;
    }

    @Override
    public Entity loadEntity(Document document, EntityDescriptor ed){

        try {
                		_id = document.get("_id", ObjectId.class);
		suid = document.get("suid", Long.class);
		cascade = document.get("cascade", Boolean.class);
		owner = document.get("owner", String.class);
		index = document.get("index", Integer.class);
		parent = document.get("parent", Long.class);
		desc = document.get("desc", String.class);
		bprs = document.get("bprs", Double.class);
		bprs_thrs = document.get("bprs_thrs", Double.class);
		bprs_stabtime = document.get("bprs_stabtime", Long.class);
		bprs_timeout = document.get("bprs_timeout", Long.class);
		p0max = document.get("p0max", Double.class);
		p0min = document.get("p0min", Double.class);
		p0_thrs = document.get("p0_thrs", Double.class);
		p0_stabtime = document.get("p0_stabtime", Long.class);
		p0_timeout = document.get("p0_timeout", Long.class);
		bgvalue_max = document.get("bgvalue_max", Double.class);
		bgvalue_min = document.get("bgvalue_min", Double.class);
		bgvalue_ok = document.get("bgvalue_ok", Double.class);
		bgdv_max = document.get("bgdv_max", Double.class);
		bgdv_min = document.get("bgdv_min", Double.class);
		bgdv_ok = document.get("bgdv_ok", Double.class);
		bgvar_max = document.get("bgvar_max", Double.class);
		bgvar_ok = document.get("bgvar_ok", Double.class);
		chrt_header = document.get("chrt_header", String.class);
		chrt_xlabel = document.get("chrt_xlabel", String.class);
		chrt_ymainlabel = document.get("chrt_ymainlabel", String.class);
		chrt_ycomplabel = document.get("chrt_ycomplabel", String.class);
		chrt_ymainmax = document.get("chrt_ymainmax", Double.class);
		chrt_ymainmin = document.get("chrt_ymainmin", Double.class);
		chrt_xmainmax = document.get("chrt_xmainmax", Double.class);
		chrt_xmainmin = document.get("chrt_xmainmin", Double.class);
		chrt_ycompmax = document.get("chrt_ycompmax", Double.class);
		chrt_ycompmin = document.get("chrt_ycompmin", Double.class);

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
        
        return this;

    }



        


    //classcode_begin

    //classcode_end


}
