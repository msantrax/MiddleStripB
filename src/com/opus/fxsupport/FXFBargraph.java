/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 *
 * @author opus
 */
public class FXFBargraph extends AnchorPane implements Initializable {

    private static final Logger LOG = Logger.getLogger(FXFBargraph.class.getName());

    @FXML
    private Line linebkg;

    @FXML
    private Line bar_back;

    @FXML
    private Text low_range;

    @FXML
    private Line meas_bar;

    @FXML
    private Line underflow;

    @FXML
    private Line overflow;

    @FXML
    private Text high_range;

    @FXML
    private Text title;

    @FXML
    private Text value;
    
    
    private Double low;
    private Double high;
    private String format;
    
    private boolean outrange = false;
    
//    private SimpleDoubleProperty vdata ; 
    
    
    
    public FXFBargraph() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXFBargraph.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
//        vdata = (new SimpleDoubleProperty(0.0));
//        
//        vdata.addListener(new ChangeListener<Number>(){
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
//                LOG.info(String.format("Vdata from %f to %f", oldVal, newVal));
//                setValue((Double)newVal);
//            }
//        });
        
        setCache(true);
        setCacheShape(true);
        setCacheHint(CacheHint.SPEED);


    }
    

    
    public void setValue(Double newvalue){
        
//        LOG.info("Setvalue in @ " + System.currentTimeMillis());
        
        if (newvalue < low){
            meas_bar.setStartX(0);
            overflow.setVisible(false);
            underflow.setVisible(true);
            value.setText(String.format("["+format+"]", newvalue));
            outrange = true;
            return;
        }
        else if(newvalue > high){
            meas_bar.setStartX(200);
            underflow.setVisible(false);
            overflow.setVisible(true);
            value.setText(String.format("["+format+"]", newvalue));
            outrange = true;
            return;
        }
        else{
            if (outrange){
                underflow.setVisible(false);
                overflow.setVisible(false);
                outrange = false;
            }
            Double pc = 200 / ((high - low) / newvalue) ;
            meas_bar.setStartX(pc);
            value.setText(String.format(format, newvalue));
        }
        
//        LOG.info("Setvalue out @ " + System.currentTimeMillis());
  
    }

    
    public void setTitle(String txt) { 
        title.setText(txt);
    }

    
    public void setRange (String format, Double low, Double high, Double threshold, boolean inv ){
        
        this.low = low;
        this.high = high;        
        this.format = format;
        
        Double stth;
        
        try {
            low_range.setText(String.format(format, low));
            high_range.setText(String.format(format, high));
           
            stth = inv ? (100.0 - threshold) : threshold ;
            String th_low = String.valueOf(stth - 2) + "%";
            String th_high = String.valueOf(stth + 2)+ "%";
            String dir = inv ? "left" : "right";
            
            String styleformat = String.format("-fx-stroke : linear-gradient( to %s, #7cfc00 %s , #ff0000 %s)", dir, th_low, th_high );
            
            bar_back.setStyle(styleformat);
            
            
            
        }
        catch (Exception ex){
            LOG.severe(String.format("Bargraph %s failed to set properties due : %s", title.getText(), ex.getMessage()));
        }
        
        
        
        
    }

//    public SimpleDoubleProperty getVdata() {
//        return vdata;
//    }

    
    
    
}
