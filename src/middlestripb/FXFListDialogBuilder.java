/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.syssupport.PicnoUtils;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author opus
 */
public class FXFListDialogBuilder{

    private ObservableList<FXFListDialogBuilder.DialogListItem> items = FXCollections.observableArrayList();;
    private ArrayList<FXFListDialogBuilder.DialogButtonStatus> buttons;
    
    private Double pref_height;
    private String files_path;
    
    public FXFListDialogBuilder() {
        
        buttons = new ArrayList<>();
    }
    
    
    public FXFListDialogBuilder setHeight (Double height){
        pref_height = height;
        return this;
    }
    
    public Double getHeight (){ return pref_height;}
     
    public ObservableList<FXFListDialogBuilder.DialogListItem> getItems() { return items;}
    
    public ArrayList<FXFListDialogBuilder.DialogButtonStatus> getButtons() { return buttons;}
    
    public FXFListDialogBuilder enableButton(String index, String label, String verb, boolean def){
        buttons.add(new DialogButtonStatus(index,label,verb,def));
        return this;
    } 
    
    public FXFListDialogBuilder add (String text, String link){
        items.add(new DialogListItem(text, link));
        return this;
    }
    
    public FXFListDialogBuilder addFiles (String path, String mode, String prefix) {
        
        files_path = path;
        
        try {
            ArrayList<String> files = PicnoUtils.scanDir(path, "absolute");
            
            for (String s : files){
                if (mode.equals("formated_time") && prefix.isEmpty() || s.contains(prefix)){
                    String fs = format2Timestamp (s, path, ".json", prefix); 
                    items.add(new DialogListItem(fs, s));
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FXFListDialogBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this;
    }
 
    private String format2Timestamp (String fname, String abspath, String sufix, String prefix){
        
        String out = "";
        out = fname.replace(abspath+"/", "");
        
        if (!sufix.isEmpty()) out = out.replace(sufix, "");
        if (!prefix.isEmpty()) out = out.replace(prefix, "");
 
        
        SimpleDateFormat sdfi = new SimpleDateFormat("ddMMyyHHmmss");
        SimpleDateFormat sdfo = new SimpleDateFormat("'Analise Blaine em 'dd/MM/YYYY 'as' HH:mm:ss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdfi.parse(out));
            out = sdfo.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return out;
    }
    
    
    public class DialogListItem {
        
        public String text;
        public String link;
        public boolean selected = false;
       

        public DialogListItem(String text, String link) {
            this.text = text;
            this.link = link;
        }
        
        @Override
        public String toString(){
            return this.text;
        }
        
    }
    
    public class DialogButtonStatus {
        
        public String btindex = "ok";
        public String label = "O.K.";
        public String verb = "ok";
        public boolean enabled = true;
        public boolean def = false;

        public DialogButtonStatus(String index, String label, String verb, boolean def) {
            this.btindex = index;
            this.label = label;
            this.verb = verb;
            this.def = def;
        }
        
    }
    
    
}
