/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Logger;




import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.opus.syssupport.PicnoUtils;
import isothermview.IsothermBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
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

    public MongoClient mongoClient;
    public MongoDatabase database;
    
    
    private ArrayList<IsothermBean> j_isotherms;
    private ArrayList<IsothermBean> b_isotherms;
    
    public MongoLink() {
    
        LOG.info("Starting MongoLink...");
        
        j_isotherms = new ArrayList<>();
        b_isotherms = new ArrayList<>();
        
        
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        
        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();
        
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("asvp2");
        
        LOG.info(String.format("Mongolink connected to : %s", database.getName()));
      
        //IsothermBean ib = coll.find().first();
        //LOG.info(String.format("Result from point : %f", ib.points.get(3).getVolume_g()));
        
        
    
    }
    
    public void report(){
        
       LOG.info(String.format("Reporting ")); 
        
    }
    
    
    public void loadAsJson(String filter){
        
        String scollection = "asvpcol";
        
        MongoCollection<Document> coll = database.getCollection(scollection);
        coll.find().forEach(printIsotherm);
        LOG.info(String.format("Loaded %d json records from %s", j_isotherms.size(), scollection));
        
    }
   
    
    public void loadAsBean(String filter){
        
        String scollection = "asvpcol";
        
        MongoCollection<IsothermBean> coll = database.getCollection(scollection, IsothermBean.class);
        coll.find().forEach(printBlock);
        LOG.info(String.format("Loaded %d bean records from %s", b_isotherms.size(), scollection));
        
    }
    
    
    public void savetoJsonFile(){
        
        for (IsothermBean ib : j_isotherms){
            String path = "/Bascon/ASVP/ASVP_ANA/isot/"+PicnoUtils.getSUID()+".json";
            try {
                PicnoUtils.saveJson(path , ib, true);
            } catch (IOException ex) {
                Logger.getLogger(MongoLink.class.getName()).log(Level.SEVERE, null, ex);
            }
            LOG.info(String.format("Saved file %s with Isotherm %s", path, ib.source));
        }
        
    }
    
    public void saveNewRecords(){
        
        String scollection = "asvpcol";
        
        MongoCollection<IsothermBean> coll = database.getCollection(scollection, IsothermBean.class);
        
        for (IsothermBean ib : j_isotherms){
            Long tstp = System.currentTimeMillis();
            ib.source="New @ " + tstp;
            ib.a_date = System.currentTimeMillis();
            ib.r_date = System.currentTimeMillis();
            
            coll.insertOne(ib);
            LOG.info(String.format("Inserted %s bean isotherm with %d points", ib.source, ib.points.size()));
        }    
        
    }
    
    Consumer<Document> printIsotherm = new Consumer<Document>() {
       @Override
       public void accept(final Document document) {
           String sjson = document.toJson();
           LOG.info(String.format("Isotherm : %s", sjson));
           
           GsonBuilder builder = new GsonBuilder(); 
            builder.setPrettyPrinting(); 
            Gson gson = builder.create();
            //gson.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
            IsothermBean nisotherm = new IsothermBean();
            nisotherm = gson.fromJson(sjson, IsothermBean.class);
            j_isotherms.add(nisotherm);
       }
    };
    
    Block<IsothermBean> printBlock = new Block<IsothermBean>() {
        @Override
        public void apply(final IsothermBean bean) {
            LOG.info(String.format("Isotherm data : %s has %d points", bean.sid, bean.points.size()));
            b_isotherms.add(bean);
        }
    };
    
    
    
    
    
}
