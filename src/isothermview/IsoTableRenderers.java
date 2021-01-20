/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author opus
 */
public class IsoTableRenderers {
    
    private static IsoTableRenderers instance;

    public static IsoTableRenderers getInstance(){
        if (instance == null){
            instance = new IsoTableRenderers(); 
        }
        return instance;
    }

    private LinkedHashMap<String, IsoTableRenderInterface> renderers;
    
    
    public IsoTableRenderers() {
        
        renderers = new LinkedHashMap<>();
        
        renderers.put("DecimalFormatRenderer", new DecimalFormatRenderer());
        renderers.put("SeriesTypeFormatRenderer", new SeriesTypeFormatRenderer());
        
        instance = this;
    }
    
    public IsoTableRenderInterface getRenderer (String type){
    
        IsoTableRenderInterface cl = renderers.get(type);
        return cl;
    }
    
    
    public class SeriesTypeFormatRenderer extends DefaultTableCellRenderer implements IsoTableRenderInterface{
        
        public String getSign() { return "SeriesTypeFormatRenderer";}
        
        public LinkedHashMap<Double,String> types = new LinkedHashMap<>();

        public SeriesTypeFormatRenderer addSeriesType(Double value, String stype){ 
            types.put(value, stype);
            return this;
        }
         
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            String stype = types.get(value);
            if (stype == null){
                value = "???";
            }
            else{
                value = stype;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    public class DefaultFormatRenderer extends DefaultTableCellRenderer implements IsoTableRenderInterface{
        
        public String getSign() { return "DefaultFormatRenderer";}
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            value = "???";
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    
    public static class DecimalFormatRenderer extends DefaultTableCellRenderer implements IsoTableRenderInterface{
        private static final DecimalFormat formatter = new DecimalFormat("#.0");

        public String getSign() { return "DecimalFormatRenderer";}
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            value = formatter.format((Number) value);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    
    
    
    
}
