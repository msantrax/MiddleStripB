/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.util.Locale;
import javafx.util.StringConverter;

/**
 *
 * @author opus
 */
public class FXFListDialogStringConverter <T> extends StringConverter <T>{
    
    private FXFListDialogBuilder.DialogListItem  li;
    
    @Override
    public String toString(T t) {
        
        li = (FXFListDialogBuilder.DialogListItem)t;
        String out;
        try{
            out = li.text; 
        }
        catch (Exception ex) {
            out = " [X] Erro na conversão";
        }
        return out;
    }

    @Override
    public T fromString(String string) {
        
        return (T)li;
    }
    
}
