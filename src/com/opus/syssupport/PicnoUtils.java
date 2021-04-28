/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.syssupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author opus
 */
public class PicnoUtils {

    private static final Logger log = Logger.getLogger(PicnoUtils.class.getName());

    
    public PicnoUtils() {
    }
    
    
    
    // ================================================== FORMATERS ===========================================================
    
    public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS"); 
    public static final DateFormat datefullFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"); 
    public static final DateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss"); 
    public static final String timestamp_format = "%1$td-%1$tm-%1$tY %1$tH:%1$tM:%1$tS:%1$tL";  
    public static final String jsonautofile_format = "%1$td%1$tm_%1$tH%1$tM%1$tS.json";
    public static final String autofile_format = "%1$td%1$tm_%1$tH%1$tM%1$tS";
    public static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/uu - HH:mm:ss");    
    
    public static final String deviceiconcolor = "palegreen";    
    
    public static long getUID(){
        return (System.currentTimeMillis() << 20) | (System.nanoTime() & ~9223372036854251520L);
    }
    
    public static String getSUID(){
        long l = (System.currentTimeMillis() << 20) | (System.nanoTime() & ~9223372036854251520L);
        return String.format("%d", l);
    }
    
    public static String getTimestamp(){
        return String.format(PicnoUtils.timestamp_format,Calendar.getInstance());
    }
    
    public static String getFormatedTimestamp(){
        LocalDateTime ldt = LocalDateTime.now();
        String s = PicnoUtils.df.format(ldt);
        return s;
    }
    
    public static String getAutoFilenameJson(String prefix){
        
        return prefix + String.format(PicnoUtils.jsonautofile_format,Calendar.getInstance());
    }
    
    public static String getAutoFilename(){
        return String.format(PicnoUtils.autofile_format,Calendar.getInstance());
    }
    
    
    
    
    // ======================================= FILE LOADERS ===================================================================
    
    public static String loadFile (String filename) throws IOException{
               
        Path p = Paths.get( filename);

        byte[] bytes = Files.readAllBytes(p);
        String content = new String(bytes, StandardCharsets.UTF_8);
        return content;
   
    }
    
    public static void saveJson(String filename, String payload) throws IOException{
        
        Path p = Paths.get(filename);
        Files.write(p, payload.getBytes(StandardCharsets.UTF_8));
    }
    
    
    public static void saveFile(String filename, String payload) throws IOException{
        
        Path p = Paths.get(filename);
        Files.write(p, payload.getBytes(StandardCharsets.UTF_8));
    }
    
    public static String readCSV(String filename) throws IOException{
        
        Path p = Paths.get(filename);

        byte[] bytes = Files.readAllBytes(p);
        String content = new String(bytes, StandardCharsets.UTF_8);

        return content;
    }
    
    
    public static String lastused_dir; 
    public static String selectFile(boolean save, String dir, String type){
        
        String wd = (dir != null) ? dir : lastused_dir;
        
        JFileChooser fc = new JFileChooser(wd);

        FileNameExtensionFilter defaultFilter = new FileNameExtensionFilter("Analises", type); 
        
        fc.addChoosableFileFilter(defaultFilter);      
        fc.setFileFilter(defaultFilter);

        int rc;
        if (save){
            rc = fc.showDialog(null, "Gravar Arquivo");
        }
        else{
            rc = fc.showDialog(null, "Abrir Arquivo");
        }
        
        if (rc == JFileChooser.APPROVE_OPTION) {
            //lastDir = fc.getSelectedFile().getParent();
            return fc.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
    
    
//    public static void setLoginDLG (LoginDLG dlg) { ldlg = dlg;}
    
    public static void publishDLGLog (String mes){
        
//        if (ldlg != null){
//            ldlg.setInfo(mes + "\n\r");
//        }
        log.info(mes);
    } 
    
    
    
    public static <T> T loadAuxJson (String fname, Class<T> fileclass) throws IOException{
        
        Path p;
        File f;
        Object obj;
        
        if (fname.contains("P100")){
            p = Paths.get(fname);
        }
        else{
           p = Paths.get(Config.getInstance().getAux_dir()+fname);
        }
        f = p.toFile(); 
        
        if (f.exists()){
            byte[] bytes = Files.readAllBytes(p);
            String json_out = new String(bytes, StandardCharsets.UTF_8);
            GsonBuilder builder = new GsonBuilder(); 
            builder.setPrettyPrinting(); 
            Gson gson = builder.create();
            obj = gson.fromJson(json_out, fileclass);
            //log.info(String.format("AuxJson loaded from %s", f.getAbsolutePath()));
            return (T) obj;
        }
        else{
//            try {
//                return fileclass.newInstance();
//            } catch (InstantiationException | IllegalAccessException ex) {
//                log.info(String.format("Failed to auxload from %s (exists = no & no instantiantion", f.getAbsolutePath()));
//            }
            log.info(String.format("AuxJson said there is no requested file %s", f.getAbsolutePath()));
            return null;
        }
    }
    
    public static void saveAuxJson (String fname, Object obj, boolean overwrite) throws IOException{
    
        //Class cl = obj.getClass();
        Path p = Paths.get(Config.getInstance().getAux_dir()+fname);
        File f = p.toFile();
        
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();
        String sjson = gson.toJson(obj);
        
        if (!f.exists() || overwrite){        
            Files.write(p, sjson.getBytes(StandardCharsets.UTF_8));
        }
    }
    
    
    public static void saveJson (String fname, Object obj, boolean overwrite) throws IOException{
    
        //Class cl = obj.getClass();
        Path p = Paths.get(fname);
        File f = p.toFile();
        
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting();
        builder.serializeSpecialFloatingPointValues(); 
        Gson gson = builder.create();
        String sjson = gson.toJson(obj);
        
        if(!f.exists() || (f.exists() && overwrite)){        
            Files.write(p, sjson.getBytes(StandardCharsets.UTF_8));
        }
        
    }
    
    
    public static <T> T loadJsonTT (String fname, Type tt) throws IOException{
        
        Path p;
        File f;
        Object obj;
        
        p = Paths.get(fname);
        f = p.toFile(); 
        
        if (f.exists()){
            byte[] bytes = Files.readAllBytes(p);
            String json_out = new String(bytes, StandardCharsets.UTF_8);
            GsonBuilder builder = new GsonBuilder(); 
            builder.setPrettyPrinting(); 
            Gson gson = builder.create();
            obj = gson.fromJson(json_out, tt);
            //log.info(String.format("AuxJson loaded from %s", f.getAbsolutePath()));
            return (T) obj;
        }
        else{
            //log.info(String.format("AuxJson said there is no requested file %s", f.getAbsolutePath()));
            return null;
        }
    }
    
    
    
    public static <T> T loadJson (String fname, Class<T> fileclass) throws IOException{
        
        Path p;
        File f;
        Object obj;
        
        p = Paths.get(fname);
        f = p.toFile(); 
        
        if (f.exists()){
            byte[] bytes = Files.readAllBytes(p);
            String json_out = new String(bytes, StandardCharsets.UTF_8);
            GsonBuilder builder = new GsonBuilder(); 
            builder.setPrettyPrinting(); 
            Gson gson = builder.create();
            obj = gson.fromJson(json_out, fileclass);
            //log.info(String.format("AuxJson loaded from %s", f.getAbsolutePath()));
            return (T) obj;
        }
        else{
            //log.info(String.format("AuxJson said there is no requested file %s", f.getAbsolutePath()));
            return null;
        }
    }
    
    
    public static ArrayList<String> scanDir (String spath, String filepattern) throws IOException{
        
        ArrayList<String> files = new ArrayList<>();
        
        Path p = Paths.get(spath);
        File f = p.toFile();

        Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                if (filepattern.equals("absolute")){
                    files.add(file.toAbsolutePath().toString());
                }
                else if (filepattern.equals("")){
                    String sfile = file.getFileName().toString();
                    int i = sfile.lastIndexOf('.');
                    sfile = sfile.substring(0, i);
                    files.add(sfile);
                }
                else {
                    String sfile = file.getFileName().toString();
                    if (sfile.contains(filepattern)){
                        files.add(sfile);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });  
        
        return files;
    }
    
    
    
    
    
    
    private static boolean checkDir (String dirpath) throws IOException{
        
        Path p = Paths.get(dirpath);
        File f = p.toFile();
            
        if (!f.exists()){
            log.info(String.format("There is no %s on Sandbox dir -> creating one.", dirpath));
            Files.createDirectory(p);
            return false;
        }
        return true;
    }
    
    
    public static String getResourceFileAsString(String fileName) {
        
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;
    }
    
    
    public static void removeFile (String spath) throws IOException{
        
        Path p = Paths.get(spath);
        File f = p.toFile();
    
        Files.deleteIfExists(p);
    }
    
    
    public static void enableSudo(String pass){
        
        if (sudo_enabled || pass.equals("")){
            sudo_enabled = false;
            ctrl.processSignal(new SMTraffic(0l, 0l, 0, "ENABLESU", PicnoUtils.class,
                                   new VirnaPayload()
                                           .setFlag2(true)
                                           .setFlag1(false)
                                           .setString(user_name)
            )); 
        }
        else{
            if (pass.equals(supasswd)){
                sudo_enabled = true;
                ctrl.processSignal(new SMTraffic(0l, 0l, 0, "ENABLESU", PicnoUtils.class,
                                       new VirnaPayload()
                                               .setFlag2(false)
                                               .setFlag1(true)
                                               .setString(user_name)
                ));
            }
            else{
                sudo_enabled = false;
                ctrl.processSignal(new SMTraffic(0l, 0l, 0, "ENABLESU", PicnoUtils.class,
                                       new VirnaPayload()
                                               .setFlag2(false)
                                               .setFlag1(false)
                                               .setString(user_name)
                ));
            }
        }
   
    }
    
    
    // Application controller link 
    private static VirnaServiceProvider ctrl;
    public static void setAppController (VirnaServiceProvider appctrl){
        ctrl = appctrl;
    }
    
    
    
    
    // ================================================== CONFIG SETUP ==========================================================
    
    public static String user_home ;
    public static String user_name ;
    public static String fx_runtime ;
    public static String java_classpath ;
    public static String boot_library_path ;
    public static String jdk_module_path ;
    public static String java_home ;
    public static String tmp_dir ;
    public static String user_dir ;
    public static String file_separator;
    public static String config_path;
    
    public static String JAVAFX_LIBS = "/opt/javafx";
    public static String APP_ID = "ASVPANA";
    public static String APP_PREFIX ;
    public static String PUBLIC_PREFIX ;
    
//    public static LauncherConfig launcherconfig;
//    public static LinkedHashMap<String,String> launchers;
//    
//    public static ImageResources image_resources;
//    public static ProfileResources profile_resources;
//    
    public static Class appclass;
    public static Boolean sudo_enabled = true; 
    public static Boolean admin = false; 
    public static String supasswd = "4556"; 
    public static String version = "V-1.7.4";
    
    
    
    private static void publishEnv(){
        
        StringBuilder sb = new StringBuilder();
        
        
         
        sb.append("\tFX Runtime = ").append(fx_runtime).append("\n\r")
          .append("\tBoot Library Path = ").append(boot_library_path).append("\n\r")
          .append("\tJDK library Path = ").append(jdk_module_path).append("\n\r")      
          .append("\tUser Home = ").append(user_home).append("\n\r")          
          .append("\tJava Home = ").append(java_home).append("\n\r")
          .append("\tUser Dir = ").append(user_dir).append("\n\r")
          
          .append("\tVersion = ").append(version).append("\n\r")  
                ;
        log.info("ASVPANA is booting \n\r" + sb.toString());
       
    }
    
    public static boolean loadConfig(){
        
        Properties sprop = System.getProperties();
            
        user_home = sprop.getProperty("user.home");
        user_name = sprop.getProperty("user.name");
        fx_runtime = sprop.getProperty("javafx.runtime.version");
        java_classpath = sprop.getProperty("java.class.path");
        boot_library_path = sprop.getProperty("sun.boot.library.path");
        jdk_module_path = sprop.getProperty("jdk.module.path");
        java_home = sprop.getProperty("java.home");
        tmp_dir = sprop.getProperty("java.io.tmpdir");
        user_dir = sprop.getProperty("user.dir");
        file_separator = sprop.getProperty("file.separator");
 
        publishEnv();
        
        APP_PREFIX = PicnoUtils.user_home + "/" + PicnoUtils.APP_ID;
        config_path = APP_PREFIX;
        PUBLIC_PREFIX = "/home/opus/"+ APP_ID;
        
        
        loadJsonConfig(); 
        Config cfg = Config.getInstance();
       
        
//        LauncherConfig lc = new LauncherConfig().getDefault();
//        try {
//            saveNewLauncherConfig(PicnoUtils.user_home + "/" + PicnoUtils.APP_PREFIX, lc);
//        } catch (IOException ex) {
//            Logger.getLogger(PicnoUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//        launchers = scanJsons(APP_PREFIX + "/Desktop/", 
//                                    "com.opus.fxsupport.LauncherConfig",
//                                    false);
//        //log.info(String.format("Loaded %d launchers", launchers.size()));   
//        
//        image_resources = new ImageResources();
//        
//        profile_resources = new ProfileResources();
        
        
        //System.exit(0);
        return true;
        
    }
   
    
    public static void updateJsonConfig(){
        
        try {
            saveJsonConfig(config_path + "/" + APP_ID.toLowerCase() + "config.json");
        } catch (IOException ex) {
            Logger.getLogger(PicnoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void loadJsonConfig(){
        
        Config cfg = Config.getInstance();
        Path p;
        File f;
      
        try {      
            p = Paths.get(config_path + "/" + APP_ID.toLowerCase() + "config.json");
            f = p.toFile();
            
            // Lookup primary config
            log.info(String.format("Looking for primary config @ %s", f.getCanonicalPath()));
            if (!f.exists()){
                log.info(String.format("There is no config to this user -> creating one on : %s", f.getParent()));
                if (!Paths.get(config_path).toFile().exists()){
                    Files.createDirectory(Paths.get(config_path));
                }
                saveJsonConfig(f.getCanonicalPath()); 
            }
            else{
                String json_out = PicnoUtils.loadFile(f.getAbsolutePath());
                GsonBuilder builder = new GsonBuilder(); 
                builder.setPrettyPrinting(); 
                Gson gson = builder.create();
                cfg = gson.fromJson(json_out, Config.class);
                log.info(String.format("Config loaded from %s", f.getAbsolutePath()));
            }
            
            // Load UI config
//            if (!checkDir(config_path + "/Desktop")){
//                launcherconfig = new LauncherConfig().getDefault();
//                String outfile = saveNewLauncherConfig(config_path, launcherconfig);
//                
//                ArrayList<String> cmds = new ArrayList<>();
//                cmds.add("ln");
//                cmds.add("-s");
//                cmds.add(outfile);
//                cmds.add(config_path + "/Desktop/default.json");
//                
//                ProcessBuilder builder = new ProcessBuilder();
//                builder.directory(new File(config_path + "/Desktop"));
//                builder.redirectErrorStream(true);
//                builder.command(cmds);
//
//                Process process = builder.start();
//                StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), e -> {
//                                                    log.info(e);                                                    
//                                              });
//                Executors.newSingleThreadExecutor().submit(streamGobbler);
//                int exitCode = process.waitFor();
//                
//                log.info("There is no launcher config available -> link created...");                  
//            }
//            else{
//                loadLauncher("");
//            }      
        } 
        catch (IOException ex) {
            publishDLGLog(String.format("Config Bootstrap failed due: %s", ex.getMessage()));
        } 
//        catch (InterruptedException ex) {
//            publishDLGLog(String.format("Config Bootstrap failed due: %s", ex.getMessage()));
//        }
      
    }
   
    
    
    
    
    
    public static LinkedHashMap<String, String> scanJsons(String spath, String type, boolean listlink){
        
        LinkedHashMap<String, String> out = new LinkedHashMap<>();
      
        Path p = Paths.get(spath);
        File f = p.toFile();
        
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        
        
        log.info(String.format("Searching Jsons of type %s @ %s", type.isEmpty() ? "Generic":type, spath));
        try {
            Files.list(p)
            .filter(Files::isRegularFile)
            .filter(x -> listlink || !Files.isSymbolicLink(x))    
            .forEach(e -> {
                try {
                    String fpath = e.toFile().getAbsolutePath();
                    String json_out = PicnoUtils.loadFile(fpath);
                    JsonHeader gobj = gson.fromJson(json_out, JsonHeader.class);
                    if (type != null && !type.isEmpty()){
                        if (gobj.getClasstype().equals(type)){
                            out.put(gobj.getArgument(), fpath);
                            //log.info(String.format("Json %s has name=%s", fpath, gobj.getClassname() ));
                        }
                    }
                    else{
                        out.put(PicnoUtils.getSUID(), fpath);
                        //log.info(String.format("Generic Json found @ %s", fpath ));
                    }
                } catch (IOException ex) {
                    log.severe(String.format("scanJsons failed to read file @ %s due : ", e.getFileName(), ex.getMessage()));
                }
            });
        } catch (IOException ex) {
            log.severe(String.format("scanJsons failed to scan directory @ %s due : ", p.getFileName(), ex.getMessage()));
        }
        
        return out;  
    }
   
    
    public static void saveJsonConfig(String spath) throws IOException{
        
        Config cfg = Config.getInstance();
        
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();
        String sjson = gson.toJson(cfg);

        log.info("Creating new config ============");
        log.info(sjson);         
        PicnoUtils.saveJson(spath, sjson);
        log.info("JSON saved to : " + spath);  
        
    }
    
//    public static String saveNewLauncherConfig(String config_path, LauncherConfig lc) throws IOException{
//        
//        String outpath = config_path + "/Desktop/" + getAutoFilenameJson("");
//        GsonBuilder builder = new GsonBuilder(); 
//        builder.setPrettyPrinting(); 
//        Gson gson = builder.create();
//        String sjson = gson.toJson(lc);
//
//        log.info("Creating new UI config ============");
//        log.info(sjson);         
//        PicnoUtils.saveJson(outpath, sjson);
//        log.info("LauncherConfig saved to : " + outpath);
//        
//        return outpath;
//    }
//    
//    
//    
//    public static String saveNewLauncher (LauncherConfig lc){
//        
//        
//        String outpath = config_path + "/Desktop/" + getAutoFilenameJson("");
//        
//        try {
//            GsonBuilder builder = new GsonBuilder();
//            builder.setPrettyPrinting();
//            Gson gson = builder.create();
//            String sjson = gson.toJson(lc);
//            PicnoUtils.saveJson(outpath, sjson);
//            log.info("New LauncherConfig saved to : " + outpath);
//        } catch (IOException ex) {
//            Logger.getLogger(PicnoUtils.class.getName()).log(Level.SEVERE, null, ex);
//            return "";
//        }
//        
//        return outpath;
//    }
//    
//    
//    
//    public static void saveLauncher (LauncherItem li){
//        
//        try {
//            //Path p = Paths.get(config_path + "/Desktop/default.json");
//            String outpath = config_path + "/Desktop/default.json";
//            
//            GsonBuilder builder = new GsonBuilder();
//            builder.setPrettyPrinting();
//            Gson gson = builder.create();
//            
//            if (li != null){
//                launcherconfig.updateItem(li);   
//            }
//            
//            String sjson = gson.toJson(launcherconfig);
//            //log.info(sjson);
//            PicnoUtils.saveJson(outpath, sjson);
//            log.info("LauncherConfig saved to : " + outpath);
//        } catch (IOException ex) {
//            log.severe(String.format("Failed to save Laucher "));
//        }
//        
//    }
//    
//    
//    public static void loadLauncher (String uid){
//        
//        Config cfg = Config.getInstance();
//        Path p;
//        File f;
//        
//        try {
//            if (uid.isEmpty()){
//                p = Paths.get(config_path + "/Desktop/default.json");
//            }
//            else{
//                String launcher = launchers.get(uid);
//                p = Paths.get(launcher);
//            }
//            
//            f = p.toFile();
//            String json_out = PicnoUtils.loadFile(f.getAbsolutePath());
//            GsonBuilder builder = new GsonBuilder();
//            builder.setPrettyPrinting();
//            Gson gson = builder.create();
//            launcherconfig = gson.fromJson(json_out, LauncherConfig.class);
//            log.info(String.format("Launcher config loaded from profile %s", launcherconfig.getArgument()));
//            
//            if (!uid.isEmpty()){
//                Path lk = Paths.get(config_path + "/Desktop/default.json");
//                if (Files.exists(lk)) {
//                    Files.delete(lk);
//                }
//                Files.createSymbolicLink(lk, p);
//            }
//           
//        } catch (IOException ex) {
//            Logger.getLogger(PicnoUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        
//    }
//    
    
    public static void exportResource(String localfile, String externalpath) throws Exception{
        
        Path p = Paths.get(externalpath);
        File f = p.toFile();
        
        if (!f.exists()){
            try (InputStream is = Config.getInstance().getClass().getClassLoader().getResourceAsStream(localfile)) {
                Files.copy(is, Paths.get(externalpath));
            } catch (Exception e) {
                publishDLGLog(String.format("Unable to export resource %s to %s due : %s", localfile, externalpath, e.getMessage()));
                //throw e;
            }
        }
    }
    
    
    
    
    /**
     * Copia recursivamente os arquivos de um Jar para um subdiretório
     * @param source_folder
     * @param dest_folder
     * @throws IOException 
     */
//    private static void copyJarFolder(FileObject source_folder, FileObject dest_folder) throws IOException {
//        
//        FileObject fo;
//        
//        FileObject childrens[] =  source_folder.getChildren();
//         
//        for (int i = 0; i < childrens.length; i++) {
//            fo = childrens[i];
//            if (fo.isFolder()){
//                if(fo.getName().equals("META-INF")) continue;
//                FileObject new_folder = dest_folder.createFolder(fo.getName());
//                copyJarFolder(fo, new_folder);
//            }
//            else{
//                FileUtil.copyFile(fo, dest_folder, fo.getName());
//            }          
//        }
//    }
    
    public static boolean zipDir(String source, String dest){
        
        try {
            PicnoUtils.zipFolder(new File(source), new File(dest));
            return true;
        } catch (Exception ex) {
            log.info(String.format("Unable to Zip %s to %s due %s", source, dest, ex.getCause().getMessage()));
            return false;
        }
    }
    
    
    public static void zipFolder(File srcFolder, File destZipFile) throws Exception {
        try (FileOutputStream fileWriter = new FileOutputStream(destZipFile);
                ZipOutputStream zip = new ZipOutputStream(fileWriter)) {

            addFolderToZip(srcFolder, srcFolder, zip);
        }
    }

    public static void addFileToZip(File rootPath, File srcFile, ZipOutputStream zip) throws Exception {

        if (srcFile.isDirectory()) {
            addFolderToZip(rootPath, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            try (FileInputStream in = new FileInputStream(srcFile)) {
                String name = srcFile.getPath();
                name = name.replace(rootPath.getPath(), "");
                log.info(String.format("Zip %s \n to %s", srcFile, name));
                zip.putNextEntry(new ZipEntry(name));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
            }
        }
    }

    public static void addFolderToZip(File rootPath, File srcFolder, ZipOutputStream zip) throws Exception {
        for (File fileName : srcFolder.listFiles()) {
            addFileToZip(rootPath, fileName, zip);
        }
    }
    
    
    
    private static void copyZipToFolder(String source, String destination){
        
        ZipEntry entry;
        
        try {
            ZipFile file = new ZipFile(source);
            FileSystem fileSystem = FileSystems.getDefault();
            Enumeration<? extends ZipEntry> entries = file.entries();          
            Files.createDirectory(fileSystem.getPath(destination));
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (entry.isDirectory()) {
                    publishDLGLog ("Creating Context Directory:" + destination + file_separator + entry.getName());
                    Files.createDirectories(fileSystem.getPath(destination + file_separator + entry.getName()));
                }
                else {
                    InputStream is = file.getInputStream(entry);
                    //System.out.println("File :" + entry.getName());
                    BufferedInputStream bis = new BufferedInputStream(is);
                    
                    String uncompressedFileName = destination + file_separator  + entry.getName();
                    Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                    Files.createFile(uncompressedFilePath);
                    FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);

                    while (bis.available() > 0) {
                        fileOutput.write(bis.read());
                    }
                    fileOutput.close();
                }
            }
            log.info("Startup Structure created @ " + destination);
        } catch (IOException ex) {
            log.info("Bypassing Startup Structure creation : " + ex.getMessage());
            //Exceptions.printStackTrace(ex);
        }
        
    }
    

    private static MessageDigest md;
    public static String cryptWithMD5(String pass){
       
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public static Color convertFXColor(String name){
        
       javafx.scene.paint.Color fx = javafx.scene.paint.Color.web(name);
       if (fx != null){
           return new java.awt.Color((float) fx.getRed(),
                                             (float) fx.getGreen(),
                                             (float) fx.getBlue(),
                                             (float) fx.getOpacity());
       }
       return Color.BLACK;
    }
    
    
    // ======================================== FX Dialogs =======================================================
    
    public static void showFXConfirmation(){
       
    }
 
};



//        try {
//            
//            checkDir(cfg.getExport_dir());
//            checkDir(cfg.getReport_dir());
//            checkDir(cfg.getTemplate_dir());
//            checkDir(cfg.getProfile_dir());
//            checkDir(cfg.getAux_dir());
//            checkDir(cfg.getDatabase_dir());
//            checkDir(cfg.getDatabase_backupdir());
//            
//            
//            exportResource ("com/opus/pp100/acp1", cfg.getDatabase_dir()+"acp1" );
//            
//            exportResource ("com/opus/pp100/pdf1.pdf", cfg.getTemplate_dir()+"pdf1.pdf");
//            exportResource ("com/opus/pp100/pdf1.json", cfg.getTemplate_dir()+"pdf1.json");
//            exportResource ("com/opus/pp100/calib_template.pdf", cfg.getTemplate_dir()+"calib_template.pdf");
//            exportResource ("com/opus/pp100/calib_template.json", cfg.getTemplate_dir()+"calib_template.json");
//            exportResource ("com/opus/pp100/folhinha1.pdf", cfg.getTemplate_dir()+"folhinha1.pdf");
//            exportResource ("com/opus/pp100/folhinha1.json", cfg.getTemplate_dir()+"folhinha1.json");
//            
//            
//            exportResource ("com/opus/pp100/Cimento-CP32.json", cfg.getProfile_dir()+"Cimento-CP32.json");
//            exportResource ("com/opus/pp100/Cimento-CP40.json", cfg.getProfile_dir()+"Cimento-CP40.json");
//            exportResource ("com/opus/pp100/Clinker-teste1.json", cfg.getProfile_dir()+"Clinker-teste1.json");
//            exportResource ("com/opus/pp100/Quartzo.json", cfg.getProfile_dir()+"Quartzo.json");
//            exportResource ("com/opus/pp100/default.json", cfg.getProfile_dir()+"default.json");
//            
//            exportResource ("com/opus/pp100/cal_standard_ac.json", cfg.getAux_dir()+"cal_standard_ac.json");
//            exportResource ("com/opus/pp100/sid_ac1.json", cfg.getAux_dir()+"sid_ac1.json");
//            exportResource ("com/opus/pp100/raso3.png", cfg.getAux_dir()+"raso3.png");
//            
//            exportResource ("com/opus/pp100/cal-default.json", cfg.getExport_dir()+"cal-default.json");
//            exportResource ("com/opus/pp100/cal-080120131220.json", cfg.getExport_dir()+"cal-080120131220.json");
//      
//        } catch (Exception ex) {
//            publishDLGLog(String.format("Config Bootstrap failed due: %s", ex.getMessage()));
//        }
     


//cmds.add("bash");
//        cmds.add("-c");
//        cmds.add("echo");
//        cmds.add("'mrf426'");
//        cmds.add("|");
//        cmds.add("sudo");
//        cmds.add("-S");
////        cmds.add("-h");
//        
//        cmds.add("ls");
//        cmds.add("-l");
//        cmds.add(">");
//        cmds.add("/home/opus/acp/out.txt");



//        ArrayList<String> cmds = new ArrayList<>();
        //cmds.add("bash");
        //cmds.add("-c");
//        cmds.add("sudo");
//        cmds.add("/home/acp/PP200/teste1.sh");
//        
//
//        ProcessBuilder builder = new ProcessBuilder();
//        builder.redirectErrorStream(true);
//        builder.directory(new File("/Bascon"));
//        builder.command(cmds);
//
//        Process process;
//        try {
//            process = builder.start();
//        
//            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), new Consumer<String>() {
//                @Override
//                public void accept(String e) {
////                    if (e.contains("password")){
////                        try {
////                            process.getOutputStream().write("mrf426".getBytes());
////                            log.info("sudo asked to password...");
////                        } catch (IOException ex) {
////                            Logger.getLogger(PicnoUtils.class.getName()).log(Level.SEVERE, null, ex);
////                        }
////                    }
//                    log.info(String.format("process line = %s", e));
//                }
//            });
//            Executors.newSingleThreadExecutor().submit(streamGobbler);
//            int exitCode = process.waitFor();
//            

//            log.info("link created...");
//        
//        } catch (IOException ex) {
//            Logger.getLogger(PicnoUtils.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(PicnoUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        