
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




public class Phase extends Entity {

    private static final Logger log = Logger.getLogger(Phase.class.getName());

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
    
       private Integer iso_status = 0;

    public static final String PROP_ISO_STATUS = "iso_status";

    @propertyfieldmap (propname = PROP_ISO_STATUS )
    public Integer getIso_Status() {
        return iso_status;
    }

    
    public void setIso_Status(Integer iso_status ) {
        Integer oldiso_status = this.iso_status;
        this.iso_status = iso_status;
        propertyChangeSupport.firePropertyChange(PROP_ISO_STATUS, oldiso_status, iso_status);
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


       private Double recal_a0 = 0.0;

    public static final String PROP_RECAL_A0 = "recal_a0";

    @propertyfieldmap (propname = PROP_RECAL_A0 )
    public Double getRecal_A0() {
        return recal_a0;
    }

    
    public void setRecal_A0(Double recal_a0 ) {
        Double oldrecal_a0 = this.recal_a0;
        this.recal_a0 = recal_a0;
        propertyChangeSupport.firePropertyChange(PROP_RECAL_A0, oldrecal_a0, recal_a0);
    }


       private Double recal_a1 = 1.0;

    public static final String PROP_RECAL_A1 = "recal_a1";

    @propertyfieldmap (propname = PROP_RECAL_A1 )
    public Double getRecal_A1() {
        return recal_a1;
    }

    
    public void setRecal_A1(Double recal_a1 ) {
        Double oldrecal_a1 = this.recal_a1;
        this.recal_a1 = recal_a1;
        propertyChangeSupport.firePropertyChange(PROP_RECAL_A1, oldrecal_a1, recal_a1);
    }


       private Double poly1_a0 = 0.0;

    public static final String PROP_POLY1_A0 = "poly1_a0";

    @propertyfieldmap (propname = PROP_POLY1_A0 )
    public Double getPoly1_A0() {
        return poly1_a0;
    }

    
    public void setPoly1_A0(Double poly1_a0 ) {
        Double oldpoly1_a0 = this.poly1_a0;
        this.poly1_a0 = poly1_a0;
        propertyChangeSupport.firePropertyChange(PROP_POLY1_A0, oldpoly1_a0, poly1_a0);
    }


       private Double poly1_a1 = 0.0;

    public static final String PROP_POLY1_A1 = "poly1_a1";

    @propertyfieldmap (propname = PROP_POLY1_A1 )
    public Double getPoly1_A1() {
        return poly1_a1;
    }

    
    public void setPoly1_A1(Double poly1_a1 ) {
        Double oldpoly1_a1 = this.poly1_a1;
        this.poly1_a1 = poly1_a1;
        propertyChangeSupport.firePropertyChange(PROP_POLY1_A1, oldpoly1_a1, poly1_a1);
    }


       private Double poly1_a2 = 0.0;

    public static final String PROP_POLY1_A2 = "poly1_a2";

    @propertyfieldmap (propname = PROP_POLY1_A2 )
    public Double getPoly1_A2() {
        return poly1_a2;
    }

    
    public void setPoly1_A2(Double poly1_a2 ) {
        Double oldpoly1_a2 = this.poly1_a2;
        this.poly1_a2 = poly1_a2;
        propertyChangeSupport.firePropertyChange(PROP_POLY1_A2, oldpoly1_a2, poly1_a2);
    }


       private Double poly1_a3 = 0.0;

    public static final String PROP_POLY1_A3 = "poly1_a3";

    @propertyfieldmap (propname = PROP_POLY1_A3 )
    public Double getPoly1_A3() {
        return poly1_a3;
    }

    
    public void setPoly1_A3(Double poly1_a3 ) {
        Double oldpoly1_a3 = this.poly1_a3;
        this.poly1_a3 = poly1_a3;
        propertyChangeSupport.firePropertyChange(PROP_POLY1_A3, oldpoly1_a3, poly1_a3);
    }


       private Double poly2_a0 = 0.0;

    public static final String PROP_POLY2_A0 = "poly2_a0";

    @propertyfieldmap (propname = PROP_POLY2_A0 )
    public Double getPoly2_A0() {
        return poly2_a0;
    }

    
    public void setPoly2_A0(Double poly2_a0 ) {
        Double oldpoly2_a0 = this.poly2_a0;
        this.poly2_a0 = poly2_a0;
        propertyChangeSupport.firePropertyChange(PROP_POLY2_A0, oldpoly2_a0, poly2_a0);
    }


       private Double poly2_a1 = 0.0;

    public static final String PROP_POLY2_A1 = "poly2_a1";

    @propertyfieldmap (propname = PROP_POLY2_A1 )
    public Double getPoly2_A1() {
        return poly2_a1;
    }

    
    public void setPoly2_A1(Double poly2_a1 ) {
        Double oldpoly2_a1 = this.poly2_a1;
        this.poly2_a1 = poly2_a1;
        propertyChangeSupport.firePropertyChange(PROP_POLY2_A1, oldpoly2_a1, poly2_a1);
    }


       private Double poly2_a2 = 0.0;

    public static final String PROP_POLY2_A2 = "poly2_a2";

    @propertyfieldmap (propname = PROP_POLY2_A2 )
    public Double getPoly2_A2() {
        return poly2_a2;
    }

    
    public void setPoly2_A2(Double poly2_a2 ) {
        Double oldpoly2_a2 = this.poly2_a2;
        this.poly2_a2 = poly2_a2;
        propertyChangeSupport.firePropertyChange(PROP_POLY2_A2, oldpoly2_a2, poly2_a2);
    }


       private Double poly2_a3 = 0.0;

    public static final String PROP_POLY2_A3 = "poly2_a3";

    @propertyfieldmap (propname = PROP_POLY2_A3 )
    public Double getPoly2_A3() {
        return poly2_a3;
    }

    
    public void setPoly2_A3(Double poly2_a3 ) {
        Double oldpoly2_a3 = this.poly2_a3;
        this.poly2_a3 = poly2_a3;
        propertyChangeSupport.firePropertyChange(PROP_POLY2_A3, oldpoly2_a3, poly2_a3);
    }


       private ArrayList<Double> xaxis = new ArrayList<>();

    public static final String PROP_XAXIS = "xaxis";

    @propertyfieldmap (propname = PROP_XAXIS )
    public ArrayList<Double> getXaxis() {
        return xaxis;
    }

    
    public void setXaxis(ArrayList<Double> xaxis ) {
        ArrayList<Double> oldxaxis = this.xaxis;
        this.xaxis = xaxis;
        propertyChangeSupport.firePropertyChange(PROP_XAXIS, oldxaxis, xaxis);
    }


       private ArrayList<Double> yaxis = new ArrayList<>();

    public static final String PROP_YAXIS = "yaxis";

    @propertyfieldmap (propname = PROP_YAXIS )
    public ArrayList<Double> getYaxis() {
        return yaxis;
    }

    
    public void setYaxis(ArrayList<Double> yaxis ) {
        ArrayList<Double> oldyaxis = this.yaxis;
        this.yaxis = yaxis;
        propertyChangeSupport.firePropertyChange(PROP_YAXIS, oldyaxis, yaxis);
    }






    // ========================================= CONSTRUCTOR & TOOLS =================================================
    public Phase() {

    }


    private static Phase instance;
    public static Phase getInstance(Long parent){

        Object temp;
        instance = new Phase();
        instance.mongolink = MongoLink.getInstance();
        instance.suid = instance.mongolink.getSuid();
        instance.setParent(parent);

        EntityDescriptor ed = new EntityDescriptor()
                .setSuid(instance.suid)
                .setInstance(instance);

        instance._descriptor = ed;

            

        EntityDescriptor ed1 = instance.mongolink.registerEntity(ed);
        if (ed1 != null){
            instance = (Phase)ed1.getInstance();
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
                    .setClazz(Phase.class)
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
				.append ("iso_status", iso_status)
				.append ("p0", p0)
				.append ("recal_a0", recal_a0)
				.append ("recal_a1", recal_a1)
				.append ("poly1_a0", poly1_a0)
				.append ("poly1_a1", poly1_a1)
				.append ("poly1_a2", poly1_a2)
				.append ("poly1_a3", poly1_a3)
				.append ("poly2_a0", poly2_a0)
				.append ("poly2_a1", poly2_a1)
				.append ("poly2_a2", poly2_a2)
				.append ("poly2_a3", poly2_a3)
				.append ("xaxis", xaxis)
				.append ("yaxis", yaxis)

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
		iso_status = document.get("iso_status", Integer.class);
		p0 = document.get("p0", Double.class);
		recal_a0 = document.get("recal_a0", Double.class);
		recal_a1 = document.get("recal_a1", Double.class);
		poly1_a0 = document.get("poly1_a0", Double.class);
		poly1_a1 = document.get("poly1_a1", Double.class);
		poly1_a2 = document.get("poly1_a2", Double.class);
		poly1_a3 = document.get("poly1_a3", Double.class);
		poly2_a0 = document.get("poly2_a0", Double.class);
		poly2_a1 = document.get("poly2_a1", Double.class);
		poly2_a2 = document.get("poly2_a2", Double.class);
		poly2_a3 = document.get("poly2_a3", Double.class);
		xaxis = document.get("xaxis", ArrayList.class);
		yaxis = document.get("yaxis", ArrayList.class);


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
