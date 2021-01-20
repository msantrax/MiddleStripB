/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isothermview;

import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author opus
 */
public interface IsoTableRenderInterface {
    
    public String getSign();
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column);
    
}
