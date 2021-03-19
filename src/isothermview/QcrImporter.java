/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;


import com.opus.syssupport.PicnoUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author opus
 */
public class QcrImporter {

    private static final Logger log = Logger.getLogger(QcrImporter.class.getName());

    final Pattern ptrn_isothermpoints = Pattern.compile("\\s{25}(.{11})\\s{36}(.{9})");
    final Pattern ptrn_absolutepoints = Pattern.compile("\\s{25}(.{11})\\s{36}(.{9})");
    final Pattern ptrn_rawpoints = Pattern.compile("\\s{25}(.{11})\\s{36}(.{9})");
    
    final Pattern ptrn_operator = Pattern.compile("Operator:(.{21})Date:(.{15}|.{10})");
    final Pattern ptrn_sid = Pattern.compile("Sample ID:(.{25})");
    final Pattern ptrn_filename = Pattern.compile("Filename:(.*)");
    final Pattern ptrn_sdesc = Pattern.compile("Sample Desc:(.{24})");
    final Pattern ptrn_comment = Pattern.compile("Comment:(.*)");
    
    final Pattern smp_weight = Pattern.compile("Sample weight:(.{21})");
    final Pattern smp_volume = Pattern.compile("Sample Volume:(.{21})");
    final Pattern smp_density = Pattern.compile("Sample Density:(.*)");
    final Pattern outgas_time = Pattern.compile("Outgas Time:(.{23})");
    final Pattern outgas_temp = Pattern.compile("OutgasTemp:(.*)");
    
    
    private Isothermv isotherm ;
    private IsothermBean isotherm_bean;
    private boolean useTemp = true;

    
    
    private Matcher matcher;
    private Double pressure =0.0;
    private Double volume = 0.0;
    private ArrayList<IsothermPoint> temp_points = new ArrayList<>();
    
    boolean isotherm_file ;
    boolean absolute_isotherm ;
    
    public String payload;
    public String filepath;
    
    private static QcrImporter instance; 
    public static QcrImporter getInstance(){
        if (instance == null) {instance = new QcrImporter();}
        return instance;
    }
    
    
    public QcrImporter() {
        //isotherm = new Isothermv();
    }
    
    
    public boolean isQcrBinary(String fpath){
        Path p = Paths.get(fpath);
        if (p.endsWith("qpc") || p.endsWith("qps")) return true;
        return false;
    }
    
    public boolean isQcrText(String fpath) throws IOException{
        filepath = fpath;
        payload = PicnoUtils.loadFile(fpath);
        if (payload.contains("Quantachrome Instruments")) return true;
        return false;
    }
    
    
    public Isothermv getIsotherm() { 
        return isotherm;
    }
    
    private Date convertDate(String sdate){
        try {
            Date date1=new SimpleDateFormat("yyyy/MM/dd").parse(sdate);
            return date1;
        } catch (ParseException ex) {
            return new Date();
        }
    }
    
    private double convertDouble (String number){
        try {
            Double dbl = Double.parseDouble(number);
            return dbl;
        } catch (Exception ex) {
            return 0.0;
        }
    }
    
    
    public void loadTextFile(){
        
        String temp;
        isotherm = new Isothermv();
        isotherm_bean = isotherm.getIsothermBean();
        
        isotherm_bean.source = "Importado-QChrome";
        
        matcher = this.ptrn_operator.matcher(payload);
        if (matcher.find()){
            isotherm_bean.a_operator = matcher.group(1).trim();
            //isotherm_bean.a_date = convertDate(matcher.group(2).trim());
        }
        if (matcher.find()){
            isotherm_bean.r_operator = matcher.group(1).trim();
            //isotherm_bean.r_date = convertDate(matcher.group(2).trim());
        }
        
        matcher = this.ptrn_sid.matcher(payload);
        if (matcher.find()){
            isotherm_bean.sid = matcher.group(1).trim();
        }
        
        matcher = this.ptrn_filename.matcher(payload);
        if (matcher.find()){
            isotherm_bean.filename = matcher.group(1).trim();
        }
        
        matcher = this.ptrn_sdesc.matcher(payload);
        if (matcher.find()){
            isotherm_bean.sdesc = matcher.group(1).trim();
        }
        
        matcher = this.ptrn_comment.matcher(payload);
        if (matcher.find()){
            isotherm_bean.comment = matcher.group(1).trim();
        }
        
        matcher = this.smp_weight.matcher(payload);
        if (matcher.find()){
            temp = matcher.group(1).trim();
            isotherm_bean.smp_weight = convertDouble(temp.replace('g', ' '));
        }
        
        matcher = this.smp_volume.matcher(payload);
        if (matcher.find()){
            temp = matcher.group(1).trim();
            isotherm_bean.smp_volume = convertDouble(temp.replace("cc", " "));
        }
        
        matcher = this.smp_density.matcher(payload);
        if (matcher.find()){
            temp = matcher.group(1).trim();
            isotherm_bean.smp_density = convertDouble(temp.replace("g/cc", " "));
        }
        
        matcher = this.outgas_time.matcher(payload);
        if (matcher.find()){
            temp = matcher.group(1).trim();
            isotherm_bean.outgas_time = convertDouble(temp.replace("hrs", " "));
        }
        
        matcher = this.outgas_temp.matcher(payload);
        if (matcher.find()){
            temp = matcher.group(1).trim();
            isotherm_bean.outgas_temp = convertDouble(temp.replace("C", " "));
        }
        
        
        
        isotherm_file = payload.contains("Data Reduction Parameters");
        absolute_isotherm = (payload.contains("Relative") && payload.contains("Pressure"));
        if (isotherm_file){
            if (absolute_isotherm){
                matcher = ptrn_absolutepoints.matcher(payload);   
            }
            else{
                matcher = ptrn_isothermpoints.matcher(payload);
            }
        }
        else{
            matcher = ptrn_rawpoints.matcher(payload);
        }
        
        while (matcher.find()){
            try{
                pressure = Double.parseDouble(matcher.group(1));
                volume = Double.parseDouble(matcher.group(2));
                
                isotherm.addPoint(pressure, volume);
                
            }
            catch (NumberFormatException ex){
                log.finer("No point");
            }
        }
        isotherm.findMiniMax();
        log.info(String.format("Loaded %d points from isotherm file : %s", isotherm.getSize(), filepath));
        
    }
    
    public void loadBinaryFile (String fpath){
        log.info(String.format("Importing Isotherm binary file %s ", fpath));
        
    }
    
    
    public void showStatus(){
        
        StringBuilder sb = new StringBuilder();
        int ptr = 1;
        
        sb.append(String.format("\nIsotherm points  = %d\n", isotherm.getSize()));
        ArrayList<IsothermPoint> isopoints = new ArrayList<>();
        IsothermPoint isotp;
        isopoints = isotherm.getAdsorptionPoints();
        sb.append(String.format("Adsorption points  = %d\n", isopoints.size()));
        isotp = isotherm.getMinAdsorption();
        sb.append(String.format("Adsorption min (pressure/vol)  = %f / %f\n", isotp.getPpo(), isotp.getVolume_g()));
        isotp = isotherm.getMaxAdsorption();
        sb.append(String.format("Adsorption max (pressure/vol)  = %f / %f\n", isotp.getPpo(), isotp.getVolume_g()));
        for (IsothermPoint isop : isopoints){
            sb.append(String.format("\tPoint %02d: %f / %f\n", ptr++, isop.getPpo(), isop.getVolume_g()));
        }

        isopoints = isotherm.getDesorptionPoints();
        ptr= 1;
        sb.append(String.format("\nDesorption points  = %d\n", isopoints.size()));
        isotp = isotherm.getMinDesorption();
        sb.append(String.format("Desorption min (pressure/vol)  = %f / %f\n", isotp.getPpo(), isotp.getVolume_g()));
        isotp = isotherm.getMaxDesorption();
        sb.append(String.format("Desorption max (pressure/vol)  = %f / %f\n", isotp.getPpo(), isotp.getVolume_g()));
        for (IsothermPoint isop : isopoints){
            sb.append(String.format("\tPoint %02d: %f / %f\n", ptr++, isop.getPpo(), isop.getVolume_g()));
        }
         
        log.info(sb.toString());
        
        
    }
    
    
    
    public boolean isUseTemp() {
        return useTemp;
    }

    public void setUseTemp(boolean useTemp) {
        this.useTemp = useTemp;
    }
    
}
