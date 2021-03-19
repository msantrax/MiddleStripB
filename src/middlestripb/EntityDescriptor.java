/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.syssupport.SMTraffic;
import org.bson.conversions.Bson;

/**
 *
 * @author opus
 */
public class EntityDescriptor {
    
    public enum Status { UPDATE, CREATE, LOAD, DELETE};
    
    private Object instance;
    private Class clazz;
    private String reference;
    private String collname;
    private Long suid;
    private String query;
    private Bson bson;
    private Long tstamp;
    private String realm;
    private Status status;
    private Boolean cascade = false;
    private SMTraffic action = null;
    private Boolean loaded = false;
    
    
    public EntityDescriptor() {
        this.tstamp = System.currentTimeMillis();
        this.realm = "opus";
        this.query = "";
        this.status = Status.CREATE;
    }

    
    public Object getInstance() {
        return instance;
    }

    public EntityDescriptor setInstance(Object instance) {
        this.instance = instance;
        this.reference= instance.getClass().getName();
        this.collname = instance.getClass().getSimpleName().toLowerCase();
        return this;
    }

    public String getReference() {
        return reference;
    }
    
    public String getCollection(){
        return collname;
    }

    
    public Long getSuid() {
        return suid;
    }

    public EntityDescriptor setSuid(Long suid) {
        this.suid = suid;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public EntityDescriptor setQuery(String query) {
        this.query = query;
        return this;
    }

    public Long getTstamp() {
        return tstamp;
    }

    public EntityDescriptor setTstamp(Long tstamp) {
        this.tstamp = tstamp;
        return this;
    }

    public String getRealm() {
        return realm;
    }

    public EntityDescriptor setRealm(String realm) {
        this.realm = realm;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public EntityDescriptor setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Class getClazz() {
        return clazz;
    }

    public EntityDescriptor setClazz(Class clazz) {
        this.clazz = clazz;
        this.reference= clazz.getName();
        this.collname = clazz.getSimpleName().toLowerCase();
        return this;
    }

    public Bson getBson() {
        return bson;
    }

    public EntityDescriptor setBson(Bson bson) {
        this.bson = bson;
        return this;
    }

    public Boolean getCascade() {
        return cascade;
    }

    public EntityDescriptor setCascade(Boolean cascade) {
        this.cascade = cascade;
        return this;
    }

    public SMTraffic getAction() {
        return action;
    }

    public EntityDescriptor setAction(SMTraffic action) {
        this.action = action;
        return this;
    }

    public Boolean getLoaded() {
        return loaded;
    }

    public EntityDescriptor setLoaded(Boolean loaded) {
        this.loaded = loaded;
        return this;
    }

    
    
}
