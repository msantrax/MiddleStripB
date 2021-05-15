/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphIcon;
import com.opus.glyphs.GlyphsBuilder;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author opus
 */
public class RootTaskController extends AnchorPane {

    private static final Logger LOG = Logger.getLogger(RootTaskController.class.getName());

    @FXML
    private Label lb_checkp0;

    @FXML
    private Label lb_isotherm;

    @FXML
    private Label lb_analysis;

    @FXML
    private Label lb_autoreport;

    @FXML
    private Label lb_autotransmit;

    @FXML
    private Label lb_xducercal;

    @FXML
    private Label lb_sampleholdercal;

    @FXML
    private Label lb_controlstandardcal;
    
    
    @FXML
    void checkp0_action(MouseEvent event) {

    }
   
    
    
    public RootTaskController() {
    
        
    }
    
    
    @FXML
    void initialize() {
        
        setPhase ("P0", "WAIT", false);
        setPhase ("ISO", "WAIT", true);
        setPhase ("RES", "WAIT", true);
 
        
        lb_autoreport.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.PAUSE, "black", 1.5));
        
        
        lb_autotransmit.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.PAUSE, "black", 1.5));
        lb_xducercal.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.CHECK, "green", 1.5));
        lb_sampleholdercal.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.CHECK, "green", 1.5));
        lb_sampleholdercal.setOnMousePressed(new EventHandler<MouseEvent>(){
                @Override 
                public void handle(MouseEvent event) {
                    LOG.info(String.format("Cell calibration called"));
 
                }  
            });   
        
        lb_controlstandardcal.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.CLOSE, "red", 1.5));
        
    }
    
    
    public void setPhase (String phase, String status, boolean disable){
        
        boolean failed = false;
        
        switch (phase){
            case "P0":
                lb_checkp0.setDisable(disable);
                if (status.contains("WAIT")){
                    lb_checkp0.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.HOURGLASS_START, "gray", 2));
                }
                else if (status.contains("CHECK")){
                    lb_checkp0.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.CHECK, "green", 2));
                }
                else if (status.contains("RUN")){
                    lb_checkp0.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.GEARS, "dodgerblue", 2));
                }
                else{
                    failed = true;
                }
                break;
            
            case "ISO":
                lb_isotherm.setDisable(disable);
                if (status.contains("WAIT")){
                    lb_isotherm.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.HOURGLASS_START, "gray", 2));
                }
                else if (status.contains("CHECK")){
                    lb_isotherm.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.CHECK, "green", 2));
                }
                else if (status.contains("RUN")){
                    lb_isotherm.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.GEARS, "dodgerblue", 2));
                }
                else{
                    failed = true;
                }
                break;
                
            case "RES":
                lb_analysis.setDisable(disable);
                if (status.contains("WAIT")){
                    lb_analysis.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.HOURGLASS_START, "gray", 2));
                }
                else if (status.contains("CHECK")){
                    lb_analysis.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.CHECK, "green", 2));
                }
                else if (status.contains("RUN")){
                    lb_analysis.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.GEARS, "dodgerblue", 2));
                }
                else{
                    failed = true;
                }
                break;    
            
            default :
                failed = true;
                break;
        }
        if (failed) LOG.warning(String.format("Failed to set Control Painel buttom %s with %s", phase, status));
        
    }
    
    
    
    
    
    
}
