/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author opus
 */
public class IsoTableModel extends AbstractTableModel{

    private static final Logger LOG = Logger.getLogger(IsoTableModel.class.getName());
    
    private ArrayList<String> names;
    private ArrayList<IsoTableModelRow> rows;
    
    private boolean multivar = false;

    public IsoTableModel() {
        names = new ArrayList<>();
        rows = new ArrayList<>();
    }
    
    public ArrayList<String> addName (String name) {
        names.add(name);
        return names;
    }
    
    public ArrayList<String> getNames() { return names;}
    
    public ArrayList<String> addNames (String[] inames){
        for (String s : inames){
            names.add(s);
        }
        return names;
    }
    
    public ArrayList<IsoTableModelRow> getRows() { return rows;}

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return names.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        
        
        
        IsoTableModelRow isorow = rows.get(rowIndex);
        return new Object();
        
    }
    
    
    
}
