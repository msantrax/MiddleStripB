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
public class FXFCenterBargraph extends AnchorPane implements Initializable {

    private static final Logger LOG = Logger.getLogger(FXFCenterBargraph.class.getName());

    
    @FXML
    private Line linebkg;

    @FXML
    private Line bar_backl;

    @FXML
    private Line bar_backr;

    @FXML
    private Text low_range;

    @FXML
    private Line meas_barr;

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

    @FXML
    private Line meas_barl;
    
    
    private Double range;
//    private Double high;
    private String format;
    
    private boolean outrange = false;
    
    
    public FXFCenterBargraph() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXFCenterBargraph.fxml"));
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
        
        setCache(true);
        setCacheShape(true);
        setCacheHint(CacheHint.SPEED);
        
    }
    

    
    public void setValue(Double newvalue){
        
        
        if (newvalue < 0.05 && newvalue > -0.05){
            meas_barl.setEndX(100);
            meas_barr.setStartX(0);
            value.setText(String.format(format, newvalue));
            return;
        }
        
        else if (newvalue < 0.05){
            
            meas_barr.setStartX(0);
            meas_barr.setEndX(100);
            if (newvalue < range * -1){
                meas_barl.setStartX(0);
                overflow.setVisible(false);
                underflow.setVisible(true);
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
                Double pc = 100 - (100 / (range / newvalue * -1)) ;
                meas_barl.setEndX(pc);
                value.setText(String.format(format, newvalue));
            }
            
        }
        else if (newvalue > 0.05){
            
            meas_barl.setEndX(100);
            meas_barl.setStartX(0);
            
            if(newvalue > range){
                meas_barr.setStartX(200);
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
                Double pc = 100 / (range / newvalue) ;
                meas_barr.setStartX(pc);
                value.setText(String.format(format, newvalue));
            }
            
        }
        
        
        
    }

    
    public void setTitle(String txt) { 
        title.setText(txt);
    }

    
    
    public void setRange (String format, Double range, Double threshold, boolean inv ){
        
        this.range = range;
        this.format = format;
        
        Double stth;
        
        try {
            
            low_range.setText(String.format(format, range * -1));
            high_range.setText(String.format(format, range));
            
            stth = inv ? (100.0 - threshold) : threshold ;
            String th_low = String.valueOf(stth - 2) + "%";
            String th_high = String.valueOf(stth + 2)+ "%";
            String dir = inv ? "left" : "right";
            
//            String styleformat = String.format("-fx-stroke : linear-gradient( to %s, #7cfc00 %s , #ff0000 %s)", dir, th_low, th_high ); 
            bar_backr.setStyle(String.format("-fx-stroke : linear-gradient( to right, #7cfc00 %s , #ff0000 %s)", th_low, th_high ));
            bar_backl.setStyle(String.format("-fx-stroke : linear-gradient( to left, #7cfc00 %s , #ff0000 %s)", th_low, th_high ));
            
        }
        catch (Exception ex){
            LOG.severe(String.format("CenterBargraph failed to set range due : %s", ex.getMessage()));
        }
        
        
        
        
    }
    
    
    
    
}
