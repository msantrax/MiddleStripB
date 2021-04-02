/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import cern.extjfx.chart.NumericAxis;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author opus
 */
public class AuxChartOverlay extends AnchorPane implements Initializable {

    private static final Logger LOG = Logger.getLogger(AuxChartOverlay.class.getName());

   
    public Label label;
    public VBox messages;
    
    
    private LinkedBlockingQueue<Label> mesqueue;
    
    
    public AuxChartOverlay() {

        this.initialize(null, null);

    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        mesqueue = new LinkedBlockingQueue<>(10);
        
        label = new Label ("Time Domain Chart");
        AnchorPane.setTopAnchor(label, 10.0);
        AnchorPane.setLeftAnchor(label, 10.0);
        AnchorPane.setRightAnchor(label, 10.0);
        label.getStyleClass().add("chartoverlay-header");
        label.setAlignment(Pos.CENTER_RIGHT);
        
        messages = new VBox();
        AnchorPane.setTopAnchor(messages, 40.0);
        AnchorPane.setRightAnchor(messages, 10.0);
        messages.setPrefSize(175.0, 153.0);
        messages.setAlignment(Pos.TOP_RIGHT);
        addMessage("Chart is ready to display data");
        addMessage("But there is no estimator defined");
        
        this.getChildren().addAll(label,messages);
        
    }
    
    
    public void addMessage (String message){
        
        Label lb = new Label(message);
        lb.getStyleClass().add("chartoverlay-messagelabel");
        if (!mesqueue.offer(lb)){
            mesqueue.poll();
            mesqueue.offer(lb);
        }
        updateMessages(); 
    }
    
    public void clearMessages(){
        mesqueue.clear();
        updateMessages();
    }
    
    public void updateMessages(){
        
        messages.getChildren().clear();
        
        for (Label lb : mesqueue){
            messages.getChildren().add(lb);
        }
        
    }
    
    
}

