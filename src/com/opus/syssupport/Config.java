/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.syssupport;


import static com.opus.syssupport.PicnoUtils.file_separator;
import java.util.Properties;
import java.util.logging.Logger;



public class Config {

    private static final Logger LOG = Logger.getLogger(Config.class.getName());

    private static transient Config instance; 
    public static transient String configpath;
    
    
    
     // ============================================== PROPERTIES ===============
        private String report_dir;

    public String getReport_dir() {
        return report_dir;
    }

    public void setReport_dir(String report_dir) {
        report_dir = report_dir;
    }

        private String context_dir;

    public String getContext_dir() {
        return context_dir;
    }

    public void setContext_dir(String context_dir) {
        context_dir = context_dir;
    }

        private String export_dir;

    public String getExport_dir() {
        return export_dir;
    }

    public void setExport_dir(String export_dir) {
        export_dir = export_dir;
    }

        private String aux_dir;

    public String getAux_dir() {
        return aux_dir;
    }

    public void setAux_dir(String aux_dir) {
        this.aux_dir = aux_dir;
    }

    
        private String profile_dir;

    public String getProfile_dir() {
        return profile_dir;
    }

    public void setProfile_dir(String profile_dir) {
        this.profile_dir = profile_dir;
    }

    
    
        private String template_dir;

    public String getTemplate_dir() {
        return template_dir;
    }

    public void setTemplate_dir(String template_dir) {
        this.template_dir = template_dir;
    }

    
        private String templatepath_full;

    public String getTemplatepath_full() {
        return templatepath_full;
    }

    public void setTemplatepath_full(String templatepath_full) {
        templatepath_full = templatepath_full;
    }

    
        private String templatepath_simple;

    public String getTemplatepath_simple() {
        return templatepath_simple;
    }

    public void setTemplatepath_simple(String templatepath_simple) {
        templatepath_simple = templatepath_simple;
    }

    
        private String database_dir;

    public String getDatabase_dir() {
        return database_dir;
    }

    public void setDatabase_dir(String database_dir) {
        this.database_dir = database_dir;
    }

        private String database_file;

    public String getDatabase_file() {
        return database_file;
    }

    public void setDatabase_file(String database_file) {
        this.database_file = database_file;
    }

        private String database_backupdir;

    public String getDatabase_backupdir() {
        return database_backupdir;
    }

    public void setDatabase_backupdir(String database_backupdir) {
        this.database_backupdir = database_backupdir;
    }

        private int database_backup_period = 604800;

    public int getDatabase_backup_period() {
        return database_backup_period;
    }

    public void setDatabase_backup_period(int database_backup_period) {
        this.database_backup_period = database_backup_period;
    }

        private boolean use_timesavings = true;

    public boolean isUse_timesavings() {
        return use_timesavings;
    }

    public void setUse_timesavings(boolean use_timesavings) {
        this.use_timesavings = use_timesavings;
    }

        private String timezone = "America/Sao_Paulo";

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    
        private boolean usefullscreen = true;

    public boolean isUsefullscreen() {
        return usefullscreen;
    }

    public void setUsefullscreen(boolean usefullscreen) {
        this.usefullscreen = usefullscreen;
    }

    
    
    
        private Boolean scanports = false;

    public Boolean isScanports() {
        return scanports;
    }

    public void setScanports(Boolean scanports) {
        this.scanports = scanports;
    }

        private int keepalive_period = 10;

    public int getKeepalive_period() {
        return keepalive_period;
    }

    public void setKeepalive_period(int keepalive_period) {
        this.keepalive_period = keepalive_period;
    }

    
    
        private String asvpport = "/dev/ttyUSB0";

    public String getAsvpport() {
        return asvpport;
    }

    public void setAsvpport(String asvpport) {
        this.asvpport = asvpport;
    }

        private String asvpbaudrate = "115200";

    public String getAsvpbaudrate() {
        return asvpbaudrate;
    }

    public void setAsvpbaudrate(String asvpbaudrate) {
        this.asvpbaudrate = asvpbaudrate;
    }

    
    
    
    
        private Boolean simulate_blaine = true;

    public Boolean isSimulate_blaine() {
        return simulate_blaine;
    }

    public void setSimulate_blaine(Boolean simulate_blaine) {
        this.simulate_blaine = simulate_blaine;
    }

    
        private int max_retry = 3;

    public int getMax_retry() {
        return max_retry;
    }

    public void setMax_retry(int max_retry) {
        this.max_retry = max_retry;
    }

    
    // Blaine simulation params ====================================================================== 
    
        private String sim_chargetime = "2000";

    public String getSim_chargetime() {
        return sim_chargetime;
    }

    public void setSim_chargetime(String sim_chargetime) {
        this.sim_chargetime = sim_chargetime;
    }

        private String sim_runtime = "4000";

    public String getSim_runtime() {
        return sim_runtime;
    }

    public void setSim_runtime(String sim_runtime) {
        this.sim_runtime = sim_runtime;
    }

        private Boolean sim_doemergency = false;

    public Boolean isSim_doemergency() {
        return sim_doemergency;
    }

    public void setSim_doemergency(Boolean sim_doemergency) {
        this.sim_doemergency = sim_doemergency;
    }

        private String sim_runerror = "150";

    public String getSim_runerror() {
        return sim_runerror;
    }

    public void setSim_runerror(String sim_runerror) {
        this.sim_runerror = sim_runerror;
    }

    
    
    
    
    // ====================== Devices config ================================================================
    
        private String dht11_offset = "0.0";

    public String getDht11_offset() {
        return dht11_offset;
    }

    public void setDht11_offset(String dht11_offset) {
        this.dht11_offset = dht11_offset;
    }

    
        private String dht11_slope = "1.0";

    public String getDht11_slope() {
        return dht11_slope;
    }

    public void setDht11_slope(String dht11_slope) {
        this.dht11_slope = dht11_slope;
    }

    
        private Boolean simulate_dht = false;

    public Boolean isSimulate_dht() {
        return simulate_dht;
    }

    public void setSimulate_dht(Boolean simulate_dht) {
        this.simulate_dht = simulate_dht;
    }

    
        private String micro_offset = "0.0";

    public String getMicro_offset() {
        return micro_offset;
    }

    public void setMicro_offset(String micro_offset) {
        this.micro_offset = micro_offset;
    }

        private String micro_slope = "1.0";

    public String getMicro_slope() {
        return micro_slope;
    }

    public void setMicro_slope(String micro_slope) {
        this.micro_slope = micro_slope;
    }

        private Boolean simulate_micro = true;

    public Boolean isSimulate_micro() {
        return simulate_micro;
    }

    public void setSimulate_micro(Boolean simulate_micro) {
        this.simulate_micro = simulate_micro;
    }
    
    
    
   



    
    public static Config getInstance(){
        if (instance == null) {instance = new Config();}
        return instance;
    }
    
    
    public Config() {
             
        context_dir = "/home/acp/PP200/";
        configpath = context_dir + "pp200config.json";

        template_dir = context_dir + "Templates/";
        templatepath_full = template_dir + "pdfroot_full.pdf";
        templatepath_simple = template_dir + "pdfroot.pdf";

        export_dir = context_dir + "Export/";
        report_dir = context_dir + "Reports/";
        profile_dir = context_dir + "Profiles/";
        aux_dir = context_dir + "Auxiliar/";

        database_dir = context_dir + "Database/";
        database_file = database_dir + "acp1";
        database_backupdir = context_dir + "Database/";
            
        instance = this;
        
    }
    
    
    
    
    
    
}
