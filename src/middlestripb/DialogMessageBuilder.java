/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author opus
 */
public class DialogMessageBuilder{

    private ArrayList<DialogMessageBuilder.DialogMessageItem> items;
    private ArrayList<DialogMessageBuilder.DialogButtonStatus> buttons;
    private Double pref_height;
    
    public DialogMessageBuilder() {
        items = new ArrayList<>();
        buttons = new ArrayList<>();
    }
    
    public DialogMessageBuilder add (DialogMessageItem item){
        items.add(item);
        return this;
    }
    
    public DialogMessageBuilder addSpacer (double height){
        items.add(new DialogMessageItem (height));
        return this;
    }
    
    public DialogMessageBuilder add (String msg, String style){
        items.add(new DialogMessageItem (msg, style));
        return this;
    }
    
    public DialogMessageBuilder setHeight (Double height){
        pref_height = height;
        return this;
    }
    
    public Double getHeight (){ return pref_height;}
       
    
    
    public ArrayList<DialogMessageBuilder.DialogMessageItem> getItems() { return items;}
    
    public ArrayList<DialogMessageBuilder.DialogButtonStatus> getButtons() { return buttons;}
    
    public DialogMessageBuilder enableButton(String index, String label, String verb, boolean def){
        buttons.add(new DialogButtonStatus(index,label,verb,def));
        return this;
    } 
    
    
    
    public class DialogMessageItem {
        
        public String text;
        public String style;
        public Rectangle spacer;
        

        public DialogMessageItem(String text, String style) {
            this.text = text;
            this.style = style;
        }
        
        public DialogMessageItem(double height) {
            spacer = new Rectangle(20, height);
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
