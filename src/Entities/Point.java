
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


import Entities.Dose ;
import Entities.Point_pf;


public class Point extends Entity {

    private static final Logger log = Logger.getLogger(Point.class.getName());

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
    private ArrayList<Object> doses; 
    private Object point_pf;
    

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
    
       private Integer point_num = 0;

    public static final String PROP_POINT_NUM = "point_num";

    @propertyfieldmap (propname = PROP_POINT_NUM )
    public Integer getPoint_Num() {
        return point_num;
    }

    
    public void setPoint_Num(Integer point_num ) {
        Integer oldpoint_num = this.point_num;
        this.point_num = point_num;
        propertyChangeSupport.firePropertyChange(PROP_POINT_NUM, oldpoint_num, point_num);
    }


       private Integer point_type = 0;

    public static final String PROP_POINT_TYPE = "point_type";

    @propertyfieldmap (propname = PROP_POINT_TYPE )
    public Integer getPoint_Type() {
        return point_type;
    }

    
    public void setPoint_Type(Integer point_type ) {
        Integer oldpoint_type = this.point_type;
        this.point_type = point_type;
        propertyChangeSupport.firePropertyChange(PROP_POINT_TYPE, oldpoint_type, point_type);
    }


       private String point_status = "R";

    public static final String PROP_POINT_STATUS = "point_status";

    @propertyfieldmap (propname = PROP_POINT_STATUS )
    public String getPoint_Status() {
        return point_status;
    }

    
    public void setPoint_Status(String point_status ) {
        String oldpoint_status = this.point_status;
        this.point_status = point_status;
        propertyChangeSupport.firePropertyChange(PROP_POINT_STATUS, oldpoint_status, point_status);
    }


       private Long start_ts = 0L;

    public static final String PROP_START_TS = "start_ts";

    @propertyfieldmap (propname = PROP_START_TS )
    public Long getStart_Ts() {
        return start_ts;
    }

    
    public void setStart_Ts(Long start_ts ) {
        Long oldstart_ts = this.start_ts;
        this.start_ts = start_ts;
        propertyChangeSupport.firePropertyChange(PROP_START_TS, oldstart_ts, start_ts);
    }


       private Long end_ts = 0L;

    public static final String PROP_END_TS = "end_ts";

    @propertyfieldmap (propname = PROP_END_TS )
    public Long getEnd_Ts() {
        return end_ts;
    }

    
    public void setEnd_Ts(Long end_ts ) {
        Long oldend_ts = this.end_ts;
        this.end_ts = end_ts;
        propertyChangeSupport.firePropertyChange(PROP_END_TS, oldend_ts, end_ts);
    }


       private Double point_p0 = 0.0;

    public static final String PROP_POINT_P0 = "point_p0";

    @propertyfieldmap (propname = PROP_POINT_P0 )
    public Double getPoint_P0() {
        return point_p0;
    }

    
    public void setPoint_P0(Double point_p0 ) {
        Double oldpoint_p0 = this.point_p0;
        this.point_p0 = point_p0;
        propertyChangeSupport.firePropertyChange(PROP_POINT_P0, oldpoint_p0, point_p0);
    }


       private Double p_p0 = 0.0;

    public static final String PROP_P_P0 = "p_p0";

    @propertyfieldmap (propname = PROP_P_P0 )
    public Double getP_P0() {
        return p_p0;
    }

    
    public void setP_P0(Double p_p0 ) {
        Double oldp_p0 = this.p_p0;
        this.p_p0 = p_p0;
        propertyChangeSupport.firePropertyChange(PROP_P_P0, oldp_p0, p_p0);
    }


       private Double p_end = 0.0;

    public static final String PROP_P_END = "p_end";

    @propertyfieldmap (propname = PROP_P_END )
    public Double getP_End() {
        return p_end;
    }

    
    public void setP_End(Double p_end ) {
        Double oldp_end = this.p_end;
        this.p_end = p_end;
        propertyChangeSupport.firePropertyChange(PROP_P_END, oldp_end, p_end);
    }


       private Double p_start = 0.0;

    public static final String PROP_P_START = "p_start";

    @propertyfieldmap (propname = PROP_P_START )
    public Double getP_Start() {
        return p_start;
    }

    
    public void setP_Start(Double p_start ) {
        Double oldp_start = this.p_start;
        this.p_start = p_start;
        propertyChangeSupport.firePropertyChange(PROP_P_START, oldp_start, p_start);
    }


       private Double point_volume = 0.0;

    public static final String PROP_POINT_VOLUME = "point_volume";

    @propertyfieldmap (propname = PROP_POINT_VOLUME )
    public Double getPoint_Volume() {
        return point_volume;
    }

    
    public void setPoint_Volume(Double point_volume ) {
        Double oldpoint_volume = this.point_volume;
        this.point_volume = point_volume;
        propertyChangeSupport.firePropertyChange(PROP_POINT_VOLUME, oldpoint_volume, point_volume);
    }


       private Double void_vol = 0.0;

    public static final String PROP_VOID_VOL = "void_vol";

    @propertyfieldmap (propname = PROP_VOID_VOL )
    public Double getVoid_Vol() {
        return void_vol;
    }

    
    public void setVoid_Vol(Double void_vol ) {
        Double oldvoid_vol = this.void_vol;
        this.void_vol = void_vol;
        propertyChangeSupport.firePropertyChange(PROP_VOID_VOL, oldvoid_vol, void_vol);
    }


       private Double dv = 0.0;

    public static final String PROP_DV = "dv";

    @propertyfieldmap (propname = PROP_DV )
    public Double getDv() {
        return dv;
    }

    
    public void setDv(Double dv ) {
        Double olddv = this.dv;
        this.dv = dv;
        propertyChangeSupport.firePropertyChange(PROP_DV, olddv, dv);
    }


       private Double vtc_sw = 0.0;

    public static final String PROP_VTC_SW = "vtc_sw";

    @propertyfieldmap (propname = PROP_VTC_SW )
    public Double getVtc_Sw() {
        return vtc_sw;
    }

    
    public void setVtc_Sw(Double vtc_sw ) {
        Double oldvtc_sw = this.vtc_sw;
        this.vtc_sw = vtc_sw;
        propertyChangeSupport.firePropertyChange(PROP_VTC_SW, oldvtc_sw, vtc_sw);
    }


       private Double vvoid_sw = 0.0;

    public static final String PROP_VVOID_SW = "vvoid_sw";

    @propertyfieldmap (propname = PROP_VVOID_SW )
    public Double getVvoid_Sw() {
        return vvoid_sw;
    }

    
    public void setVvoid_Sw(Double vvoid_sw ) {
        Double oldvvoid_sw = this.vvoid_sw;
        this.vvoid_sw = vvoid_sw;
        propertyChangeSupport.firePropertyChange(PROP_VVOID_SW, oldvvoid_sw, vvoid_sw);
    }


       private Integer tan = 0;

    public static final String PROP_TAN = "tan";

    @propertyfieldmap (propname = PROP_TAN )
    public Integer getTan() {
        return tan;
    }

    
    public void setTan(Integer tan ) {
        Integer oldtan = this.tan;
        this.tan = tan;
        propertyChangeSupport.firePropertyChange(PROP_TAN, oldtan, tan);
    }


       private Integer ttc = 0;

    public static final String PROP_TTC = "ttc";

    @propertyfieldmap (propname = PROP_TTC )
    public Integer getTtc() {
        return ttc;
    }

    
    public void setTtc(Integer ttc ) {
        Integer oldttc = this.ttc;
        this.ttc = ttc;
        propertyChangeSupport.firePropertyChange(PROP_TTC, oldttc, ttc);
    }


       private Integer flag = 0;

    public static final String PROP_FLAG = "flag";

    @propertyfieldmap (propname = PROP_FLAG )
    public Integer getFlag() {
        return flag;
    }

    
    public void setFlag(Integer flag ) {
        Integer oldflag = this.flag;
        this.flag = flag;
        propertyChangeSupport.firePropertyChange(PROP_FLAG, oldflag, flag);
    }






    // ========================================= CONSTRUCTOR & TOOLS =================================================
    public Point() {

    }


    private static Point instance;
    public static Point getInstance(Long parent){

        Object temp;
        instance = new Point();
        instance.mongolink = MongoLink.getInstance();
        instance.suid = instance.mongolink.getSuid();
        instance.setParent(parent);

        EntityDescriptor ed = new EntityDescriptor()
                .setSuid(instance.suid)
                .setInstance(instance);

        instance._descriptor = ed;

            
        instance.doses = new ArrayList<Object>() ;
        temp = Dose.getInstance(parent).getSuid() ;
        instance.doses.add(temp) ;
 
        temp = Point_pf.getInstance(parent).getSuid();
        instance.point_pf = temp ;
 

        EntityDescriptor ed1 = instance.mongolink.registerEntity(ed);
        if (ed1 != null){
            instance = (Point)ed1.getInstance();
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
                    .setClazz(Point.class)
                    .setBson(Filters.eq("suid", suid))
                    .setCascade(Boolean.FALSE)
                    .setAction(smm)
                    .setLoaded(true)
                    .setSuid(suid);
            tsk.offer(rooted);
        }

            
        if (doses.get(0) instanceof Long){
            for (int i = 0; i < doses.size(); i++) {
                Long doses_suid = (Long)doses.get(i);
                Dose t_doses = (Dose)ed.findById((Long)doses_suid, true);
                if (t_doses != null) {
                    doses.set(i, t_doses);
                    if (cascade) t_doses.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Dose.class)
                            .setBson(Filters.eq("suid", doses_suid))
                            .setCascade(cascade));
                }
            }
        }
 
        if (point_pf instanceof Long){
                Point_pf t_point_pf = (Point_pf)ed.findById((Long)point_pf, true);
                if (t_point_pf != null) {
                    point_pf = t_point_pf;
                    if (cascade) t_point_pf.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Point_pf.class)
                            .setBson(Filters.eq("suid", (Long)point_pf))
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
				.append ("doses", getDoses())
				.append ("point_pf", (point_pf instanceof Long) ? point_pf : Long.parseLong(point_pf.toString()) )
				.append ("owner", owner)
				.append ("index", index)
				.append ("parent", parent)
				.append ("point_num", point_num)
				.append ("point_type", point_type)
				.append ("point_status", point_status)
				.append ("start_ts", start_ts)
				.append ("end_ts", end_ts)
				.append ("point_p0", point_p0)
				.append ("p_p0", p_p0)
				.append ("p_end", p_end)
				.append ("p_start", p_start)
				.append ("point_volume", point_volume)
				.append ("void_vol", void_vol)
				.append ("dv", dv)
				.append ("vtc_sw", vtc_sw)
				.append ("vvoid_sw", vvoid_sw)
				.append ("tan", tan)
				.append ("ttc", ttc)
				.append ("flag", flag)

                ;
        return doc;
    }

    @Override
    public Entity loadEntity(Document document, EntityDescriptor ed){

        		_id = document.get("_id", ObjectId.class);
		suid = document.get("suid", Long.class);
		cascade = document.get("cascade", Boolean.class);
		doses = document.get("doses", ArrayList.class);
		point_pf = document.get("point_pf", Long.class);
		owner = document.get("owner", String.class);
		index = document.get("index", Integer.class);
		parent = document.get("parent", Long.class);
		point_num = document.get("point_num", Integer.class);
		point_type = document.get("point_type", Integer.class);
		point_status = document.get("point_status", String.class);
		start_ts = document.get("start_ts", Long.class);
		end_ts = document.get("end_ts", Long.class);
		point_p0 = document.get("point_p0", Double.class);
		p_p0 = document.get("p_p0", Double.class);
		p_end = document.get("p_end", Double.class);
		p_start = document.get("p_start", Double.class);
		point_volume = document.get("point_volume", Double.class);
		void_vol = document.get("void_vol", Double.class);
		dv = document.get("dv", Double.class);
		vtc_sw = document.get("vtc_sw", Double.class);
		vvoid_sw = document.get("vvoid_sw", Double.class);
		tan = document.get("tan", Integer.class);
		ttc = document.get("ttc", Integer.class);
		flag = document.get("flag", Integer.class);


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



        

    public ArrayList<Dose> getObjDoses(){

        ArrayList<Dose> lpts = new ArrayList<>();

        for (int i = 0; i < doses.size(); i++) {
            if (doses.get(i) instanceof Long){
                break;
            }
            else if (doses.get(i) instanceof Dose){
                Dose pt = (Dose)doses.get(i);
                lpts.add(pt);
            }
        }
        return lpts;
    }

    public ArrayList<Long> getDoses(){

        ArrayList<Long> lpts = new ArrayList<>();

        for (int i = 0; i < doses.size(); i++) {
            if (doses.get(i) instanceof Long){
                lpts.add((Long)doses.get(i));
            }
            else if (doses.get(i) instanceof Dose){
                Dose pt = (Dose)doses.get(i);
                lpts.add(pt.getSuid());
            }
        }
        return lpts;
    }

    public Dose addDose(){

        Dose pt;

        if (doses.get(0) instanceof Long){
            loadChildren(true, null);
        }

        pt = (Dose)doses.get(0);
        if (pt.getOwner().equals("instance")){
            pt.setOwner("activated");
        }
        else{
            pt = Dose.getInstance(parent);
            pt.setOwner("added");
            doses.add(pt);
            pt.loadChildren(true, null);
        }
        return pt;
    }

 


    //classcode_begin

    //classcode_end


}
