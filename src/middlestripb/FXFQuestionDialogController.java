/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import middlestripb.DialogMessageBuilder.DialogButtonStatus;
import middlestripb.DialogMessageBuilder.DialogMessageItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author opus
 */
public class FXFQuestionDialogController extends AnchorPane {
    
    
    @FXML
    private Text header;

    @FXML
    private Button ok_button;

    @FXML
    private Button cancel_button;

    @FXML
    private Button aux_button;

    @FXML
    private TextFlow textflow;

    
    
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
    
    //private String ok_label = "0.K.";
    private String ok_value = "ok";
    //private String cancel_label = "Cancelar";
    private String cancel_value = "cancel";
    //private String aux_label = "";
    private String aux_value = "aux";
    private Double dlg_height = 240.0;
    
    public SimpleStringProperty result = new SimpleStringProperty("");
    
    
    
    public FXFQuestionDialogController() {
         
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
    
    public void setStatus (DialogMessageBuilder dmb){

        ObservableList<Node> childrens = textflow.getChildren();
        
        if (dmb.getHeight() != null) dlg_height = dmb.getHeight();
        
        for (DialogMessageItem dmi : dmb.getItems()){
            if (dmi.spacer != null){
                //childrens.add(dmi.spacer);
                Text t = new Text("\n");
                t.setStyle("-fx-font-size: 3px;");
                childrens.add(t);
            }
            else{
                Text t = new Text(dmi.text+"\n");
                t.setStyle("-fx-fill:gray; " + dmi.style);
                childrens.add(t);
            } 
        } 
        
        for (DialogButtonStatus dbs : dmb.getButtons()){
            if (dbs.btindex.equals("ok")) enableOK (dbs.enabled, dbs.def, dbs.label, dbs.verb);
            if (dbs.btindex.equals("cancel")) enableCancel (dbs.enabled, dbs.def, dbs.label, dbs.verb);
            if (dbs.btindex.equals("aux")) enableAux (dbs.enabled, dbs.def, dbs.label, dbs.verb);
        } 
        
//        textflow.setPrefHeight(textflow.getPrefHeight() + -40.0);
//        setPrefHeight( getPrefHeight() + -40.0 );
        
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

