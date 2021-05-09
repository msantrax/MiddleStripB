/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;


import middlestripb.FXFListDialogBuilder.DialogButtonStatus;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 *
 * @author opus
 */
public class FXFListDialogController extends AnchorPane {

    private static final Logger LOG = Logger.getLogger(FXFListDialogController.class.getName());
    
    

    @FXML
    private Text header;

    @FXML
    private Button ok_button;

    @FXML
    private Button cancel_button;

    @FXML
    private Button aux_button;

    @FXML
    private ListView<FXFListDialogBuilder.DialogListItem> listview;
    
    
    @FXML
    void cancel_action(ActionEvent event) {
        result.set(cancel_value);
    }

    @FXML
    void ok_action(ActionEvent event) {
        result.set(ok_value);
    }
    
    @FXML
    void aux_action(ActionEvent event) {
        result.set(aux_value);
    }
    
    private String ok_value = "ok";
    private String cancel_value = "cancel";
    private String aux_value = "aux";
    private Double dlg_height = 240.0;
    
    public SimpleStringProperty result = new SimpleStringProperty("");
    
    private ObservableList<FXFListDialogBuilder.DialogListItem> dmb_items;
    //private FXFListDialogBuilder.DialogListItem sel_item;
    
    public FXFListDialogController() {
         
    }
    
    
    
    @FXML
    void initialize() {
        
        ok_button.setVisible(true);
        cancel_button.setVisible(false);
        aux_button.setVisible(false);
        
        ok_button.setDisable(false);
        ok_button.setText("O.K.");
        ok_button.requestFocus();      
    }
    
    public void setHeader (String defheader){
        header.setText(defheader);
    }
    
    public void setStatus (FXFListDialogBuilder dmb){

        
        if (dmb.getHeight() != null) dlg_height = dmb.getHeight();
        
        dmb_items = dmb.getItems();
        listview.setItems(dmb_items);
   
        listview.getSelectionModel().selectedItemProperty().addListener(this::selectionChanged);

        for (DialogButtonStatus dbs : dmb.getButtons()){
            if (dbs.btindex.equals("ok")) enableOK (dbs.enabled, dbs.def, dbs.label, dbs.verb);
            if (dbs.btindex.equals("cancel")) enableCancel (dbs.enabled, dbs.def, dbs.label, dbs.verb);
            if (dbs.btindex.equals("aux")) enableAux (dbs.enabled, dbs.def, dbs.label, dbs.verb);
        } 

    }
   
    public void selectionChanged(ObservableValue<? extends FXFListDialogBuilder.DialogListItem> observable, 
            FXFListDialogBuilder.DialogListItem oldValue,
            FXFListDialogBuilder.DialogListItem newValue) {
        
        ok_value = newValue.link;
        LOG.info(String.format("Selection changed to %s", newValue.link));
        
    }

    
    public void enableOK (boolean enabled, boolean def, String label, String verb){
        ok_button.setVisible(enabled);
        if (enabled) {
            ok_button.setText(label);
            ok_value=verb;
            if (def) ok_button.requestFocus();
        }
        
    }

    public void enableCancel (boolean enabled, boolean def, String label, String verb){
        cancel_button.setVisible(enabled);
        if (enabled) {
            cancel_button.setText(label);
            cancel_value=verb;
            if (def) cancel_button.requestFocus();
        }
    }
    
    public void enableAux (boolean enabled, boolean def, String label, String verb){
        aux_button.setVisible(enabled);
        if (enabled) {
            aux_button.setText(label);
            aux_value=verb;
            if (def) aux_button.requestFocus();
        }
    }

    public Double getDlg_height() {
        return dlg_height;
    }

    public void setDlg_height(Double dlg_height) {
        this.dlg_height = dlg_height;
    }
    
}

