
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


import Entities.Point ;
import Entities.Isotherm_pf;


public class Isotherm extends Entity {

    private static final Logger log = Logger.getLogger(Isotherm.class.getName());

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
    private ArrayList<Object> points; 
    private Object isotherm_pf;
    

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
    
       private Integer iso_num = 8857;

    public static final String PROP_ISO_NUM = "iso_num";

    @propertyfieldmap (propname = PROP_ISO_NUM )
    public Integer getIso_Num() {
        return iso_num;
    }

    @propertylink (propname = PROP_ISO_NUM, plink = "it_iso_num", input=false, callstate="CALLSTATE1")
    public void setIso_Num(Integer iso_num ) {
        Integer oldiso_num = this.iso_num;
        this.iso_num = iso_num;
        propertyChangeSupport.firePropertyChange(PROP_ISO_NUM, oldiso_num, iso_num);
    }


       private String iso_status = "unloaded";

    public static final String PROP_ISO_STATUS = "iso_status";

    @propertyfieldmap (propname = PROP_ISO_STATUS )
    public String getIso_Status() {
        return iso_status;
    }

    
    public void setIso_Status(String iso_status ) {
        String oldiso_status = this.iso_status;
        this.iso_status = iso_status;
        propertyChangeSupport.firePropertyChange(PROP_ISO_STATUS, oldiso_status, iso_status);
    }






    // ========================================= CONSTRUCTOR & TOOLS =================================================
    public Isotherm() {

    }


    private static Isotherm instance;
    public static Isotherm getInstance(Long parent){

        Object temp;
        instance = new Isotherm();
        instance.mongolink = MongoLink.getInstance();
        instance.suid = instance.mongolink.getSuid();
        instance.setParent(parent);

        EntityDescriptor ed = new EntityDescriptor()
                .setSuid(instance.suid)
                .setInstance(instance);

        instance._descriptor = ed;

            
        instance.points = new ArrayList<Object>() ;
        temp = Point.getInstance(parent).getSuid() ;
        instance.points.add(temp) ;
 
        temp = Isotherm_pf.getInstance(parent).getSuid();
        instance.isotherm_pf = temp ;
 

        EntityDescriptor ed1 = instance.mongolink.registerEntity(ed);
        if (ed1 != null){
            instance = (Isotherm)ed1.getInstance();
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
                    .setClazz(Isotherm.class)
                    .setBson(Filters.eq("suid", suid))
                    .setCascade(Boolean.FALSE)
                    .setAction(smm)
                    .setLoaded(true)
                    .setSuid(suid);
            tsk.offer(rooted);
        }

            
        if (points.get(0) instanceof Long){
            for (int i = 0; i < points.size(); i++) {
                Long points_suid = (Long)points.get(i);
                Point t_points = (Point)ed.findById((Long)points_suid, true);
                if (t_points != null) {
                    points.set(i, t_points);
                    if (cascade) t_points.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Point.class)
                            .setBson(Filters.eq("suid", points_suid))
                            .setCascade(cascade));
                }
            }
        }
 
        if (isotherm_pf instanceof Long){
                Isotherm_pf t_isotherm_pf = (Isotherm_pf)ed.findById((Long)isotherm_pf, true);
                if (t_isotherm_pf != null) {
                    isotherm_pf = t_isotherm_pf;
                    if (cascade) t_isotherm_pf.loadChildren(cascade, null);
                }
                else{
                    tsk.offer(new EntityDescriptor()
                            .setClazz(Isotherm_pf.class)
                            .setBson(Filters.eq("suid", (Long)isotherm_pf))
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
				.append ("points", getPoints())
				.append ("isotherm_pf", (isotherm_pf instanceof Long) ? isotherm_pf : Long.parseLong(isotherm_pf.toString()) )
				.append ("owner", owner)
				.append ("index", index)
				.append ("parent", parent)
				.append ("iso_num", iso_num)
				.append ("iso_status", iso_status)

                ;
        return doc;
    }

    @Override
    public Entity loadEntity(Document document, EntityDescriptor ed){

        		_id = document.get("_id", ObjectId.class);
		suid = document.get("suid", Long.class);
		cascade = document.get("cascade", Boolean.class);
		points = document.get("points", ArrayList.class);
		isotherm_pf = document.get("isotherm_pf", Long.class);
		owner = document.get("owner", String.class);
		index = document.get("index", Integer.class);
		parent = document.get("parent", Long.class);
		iso_num = document.get("iso_num", Integer.class);
		iso_status = document.get("iso_status", String.class);


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



        

    public ArrayList<Point> getObjPoints(){

        ArrayList<Point> lpts = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) instanceof Long){
                break;
            }
            else if (points.get(i) instanceof Point){
                Point pt = (Point)points.get(i);
                lpts.add(pt);
            }
        }
        return lpts;
    }

    public ArrayList<Long> getPoints(){

        ArrayList<Long> lpts = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) instanceof Long){
                lpts.add((Long)points.get(i));
            }
            else if (points.get(i) instanceof Point){
                Point pt = (Point)points.get(i);
                lpts.add(pt.getSuid());
            }
        }
        return lpts;
    }

    public Point addPoint(){

        Point pt;

        if (points.get(0) instanceof Long){
            loadChildren(true, null);
        }

        pt = (Point)points.get(0);
        if (pt.getOwner().equals("instance")){
            pt.setOwner("activated");
        }
        else{
            pt = Point.getInstance(parent);
            pt.setOwner("added");
            points.add(pt);
            pt.loadChildren(true, null);
        }
        return pt;
    }

 


    //classcode_begin

    //classcode_end


}
