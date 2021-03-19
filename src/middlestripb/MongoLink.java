/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Entity;
import Entities.Isotherm;
import Entities.Point;
import java.util.logging.Logger;




import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;


/**
 *
 * @author opus
 */
public class MongoLink {

    private static final Logger LOG = Logger.getLogger(MongoLink.class.getName());

    
    private Controller ctrl; 
    
    public MongoClient mongoClient;
    private MongoDatabase database;
    
    private EntityDescriptors loaded_descriptors;
    private QueuedDescriptors dirty_descriptors;
    private QueuedDescriptors task_descriptors;
    
    private EntityDescriptor temp_descriptor;
   
    
    private static MongoLink instance; 
    public static MongoLink getInstance(){
        if (instance == null) {instance = new MongoLink();}
        return instance;
    }
    
    
    public MongoLink() {
    
        LOG.info("Starting MongoLink...");
      
        loaded_descriptors = new EntityDescriptors();
        dirty_descriptors = new QueuedDescriptors();
        task_descriptors = new QueuedDescriptors();         
        
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        
        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();
        
          
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("asvp2");
  
        
        LOG.info(String.format("Mongolink connected to : %s", database.getName()));
    
    }
    
    public void setAppController (Controller ctrl){
        this.ctrl = ctrl;
    }
    
    
    public void flush(){
        
        //LOG.info(String.format("Flush Mongolink..."));
        
//        while (!getDirty_descriptors().isEmpty()){
            EntityDescriptor ed = getDirty_descriptors().poll();
            if (ed != null){
                Entity e = (Entity)ed.getInstance();
                MongoCollection<Document> coll = getDatabase().getCollection(ed.getCollection());
                coll.insertOne(e.getDocument());
                LOG.info(String.format("Entity %s was flushed...", e.toString()));
            }   
//        }
        
        while (!getTask_descriptors().isEmpty()){
            EntityDescriptor tsked = getTask_descriptors().poll();
            if (tsked != null){
                if (tsked.getAction() != null){  // Task is root
                    if (getTask_descriptors().isEmpty()){ // May be first or last deppending on loaded flag
                        if (tsked.getLoaded()) { // It is the last bail out
                            Entity et = loaded_descriptors.findById(tsked.getSuid(), true);
//                            et.loadChildren(true);
                            SMTraffic smt = tsked.getAction();
                            smt.getPayload().vobject = et;
                            ctrl.processSignal(smt);
                            
//                            LOG.info("Loading Final phase ...");
                        }
                        else{  // it is the first -> proccess and reinsert on wire
                            loadEntity(tsked);
                            getTask_descriptors().offer(tsked);
                        }
                    }
                    else{ // Root surfaced but we're not done -> reinsert
                        getTask_descriptors().offer(tsked);
                    } 
                }
                else{ // task is not Root 
                    loadEntity (tsked);
                }
//                for (EntityDescriptor ads : getTask_descriptors().toArray(new EntityDescriptor[0])){
//                    LOG.info(String.format("Entity on wire is:  %s of type %s", ads.getBson().toString(), ads.getReference())); 
//                }
            }
        }
    }

    
    public void testDesc(){
        
//        Isotherm iso = Isotherm.getInstance(1L);
//        iso.addPoint();
//        iso.addPoint();
//        iso.addPoint();

        EntityDescriptor ed = new EntityDescriptor()
                .setClazz(Isotherm.class)
                .setBson(Filters.eq("suid", 1614971484317L))
                .setCascade(Boolean.FALSE)
                .setAction(new SMTraffic(0l, 0l, 0, "LOADISO", this.getClass(),
                        new VirnaPayload()
                ));        
        this.getTask_descriptors().offer(ed);
        
    }
    
    
    
    
//    @smstate (state = "LOADISO")
//    public boolean st_loadIso(SMTraffic smm){
//        
//        VirnaPayload payload = smm.getPayload();
//        Entity et = (Entity)payload.vobject;
//        et.loadChildren(false, new SMTraffic(0l, 0l, 0, "ISOLOADED", this.getClass(),new VirnaPayload())); 
//        
//        return true;
//    }
//    
    @smstate (state = "ISOLOADED")
    public boolean st_IsolOADED(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        Entity et = (Entity)payload.vobject;
        et.loadChildren(false, null);
        
//        Isotherm isoth = (Isotherm)et;
//        List<Long> pointList = isoth.getPoints();
//        List<Long> personListSorted =
//                pointList.stream()
//                  .sorted(Comparator.comparing(Point::getSuid))
//                  .collect(Collectors.toList());
        
        
        
        
        return true;
    }
    
    
    
    
    
    public EntityDescriptor registerEntity (EntityDescriptor ed){
        
        EntityDescriptor loaded = getLoaded_descriptors().findByInstance(ed.getInstance());
        if (loaded != null){
            if (!dirty_descriptors.contains(ed)) dirty_descriptors.offer(ed);
            return loaded;
        }    
        
        getLoaded_descriptors().add(ed);
        if (!dirty_descriptors.contains(ed)) dirty_descriptors.offer(ed);
        return null;
        
    }
    
    
    public void loadEntity (EntityDescriptor ed){
        
        MongoCollection<Document>collection = database.getCollection(ed.getCollection());
        
        collection.find(ed.getBson()).forEach(new Block<Document>(){ 
            @Override
            public void apply(final Document document) {
                try {
                    Entity obj = (Entity)ed.getClazz().getConstructors()[0].newInstance(null);
                    obj.loadEntity(document, ed);
                    if (ed.getCascade()){
                        boolean next = obj.loadChildren(ed.getCascade(), null);
                    }
                    
                    LOG.info(String.format("Mongolink loaded entity %s : %d", ed.getReference(), ed.getSuid()));
                    
                } catch (Exception ex) {
                    LOG.severe(String.format("Load Entity failed to load due %s ", ex.getMessage()));
                }
            };
        });
    }
    
    
    
    
    
  
    
   
    
    // ============================================= GET SET & GADGETS =======================================================
    
    public EntityDescriptors getLoaded_descriptors() {
        return loaded_descriptors;
    }

    public QueuedDescriptors getDirty_descriptors() {
        return dirty_descriptors;
    }
    
    public QueuedDescriptors getTask_descriptors() {
        return task_descriptors;
    }
   
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public Long getSuid(){
        
        Long suid = System.currentTimeMillis();
        
        while (System.currentTimeMillis() == suid);
        return System.currentTimeMillis();
    }


    
    
}    
    



 

    
    
//    loaded_descriptors.add(new EntityDescriptor()
//                    .setSuid(10l)
//                    .setReference("isotherm")
//                    .setInstance(new Isotherm())
//                    
//        );
//        
//     
//        EntityDescriptor ts = loaded_descriptors.findById(10l, false);
//        dirty_descriptors.add(ts);
    
   


//public void testIso(){
//        
//    Isotherm isotherm = new Isotherm();
//
//    MongoCollection<Isotherm> coll = getDatabase().getCollection("isotherm", Isotherm.class);
//
//    isotherm.setIso_Status("testIso");
//    isotherm.setSuid(System.currentTimeMillis());
//    coll.insertOne(isotherm);
//
//}
//
//public void loadIso(){
//
//    MongoCollection<Isotherm> collection = getDatabase().getCollection("jisotherm", Isotherm.class);
//    Isotherm iso = collection.find().first();
//
//    LOG.info(String.format("Result from point : %d", 7));
//
//}








//
//
//public void report(Isotherm isotherm){
//        
//       LOG.info(String.format("Reporting ")); 
//       
//       String path = "/Bascon/ASVP/ASVP_ANA/isot/"+PicnoUtils.getSUID()+".json";
//        try {
//            PicnoUtils.saveJson(path , isotherm, true);
//        } catch (IOException ex) {
//            Logger.getLogger(MongoLink.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
//    
//    
//    public void loadAsJson(String filter){
//        
//        String scollection = "isotherm";
//        
//        MongoCollection<Document> coll = database.getCollection(scollection);
//        coll.find().forEach(printIsotherm);
//        LOG.info(String.format("Loaded %d json records from %s", 0, scollection));
//        
//    }
//   
//    
//    public void loadAsBean(String filter){
//        
//        String scollection = "isotherm";
//        
//        MongoCollection<IsothermBean> coll = database.getCollection(scollection, IsothermBean.class);
//        coll.find().forEach(printBlock);
//        LOG.info(String.format("Loaded %d bean records from %s", 0, scollection));
//        
//    }
//    
//    
//    public void savetoJsonFile(){
//        
//        for (IsothermBean ib : j_isotherms){
//            String path = "/Bascon/ASVP/ASVP_ANA/isot/"+PicnoUtils.getSUID()+".json";
//            try {
//                PicnoUtils.saveJson(path , ib, true);
//            } catch (IOException ex) {
//                Logger.getLogger(MongoLink.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            LOG.info(String.format("Saved file %s with Isotherm %s", path, ib.source));
//        }
//        
//    }
//    
//    public void saveNewRecords(){
//        
//        String scollection = "isotherm";
//        
//        MongoCollection<IsothermBean> coll = database.getCollection(scollection, IsothermBean.class);
//        
//        for (IsothermBean ib : j_isotherms){
//            Long tstp = System.currentTimeMillis();
//            ib.source="New @ " + tstp;
//            ib.a_date = System.currentTimeMillis();
//            ib.r_date = System.currentTimeMillis();
//            
//            coll.insertOne(ib);
//            LOG.info(String.format("Inserted %s bean isotherm with %d points", ib.source, ib.points.size()));
//        }    
//        
//    }
//    
//    Consumer<Document> printIsotherm = new Consumer<Document>() {
//       @Override
//       public void accept(final Document document) {
//            String sjson = document.toJson();
//            LOG.info(String.format("Isotherm : %s", sjson));
//           
//            GsonBuilder builder = new GsonBuilder(); 
//            builder.setPrettyPrinting(); 
//            Gson gson = builder.create();
//            //gson.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
//            IsothermBean nisotherm = new IsothermBean();
//            nisotherm = gson.fromJson(sjson, IsothermBean.class);
//            j_isotherms.add(nisotherm);
//       }
//    };
//    
//    Block<IsothermBean> printBlock = new Block<IsothermBean>() {
//        @Override
//        public void apply(final IsothermBean bean) {
//            LOG.info(String.format("Isotherm data : %s has %d points", bean.sid, bean.points.size()));
//            b_isotherms.add(bean);
//        }
//    };
//    


//
//        try {
//            Class cl = Class.forName(ed.getReference());
//            MongoCollection<Isotherm> coll = instance.mongolink.getDatabase().getCollection(
//                ed.getCollection(), 
//                cl);
//            ed.setColl(coll);
//            
//        } catch (ClassNotFoundException ex) {
//            log.severe(String.format("Unable to instantiate class %s ", ed.getReference()));
//        }