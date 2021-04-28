/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

// Charts Toolbar =====================================================================================================

import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import java.awt.Paint;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;



class PrepToolbar extends HBox {

    private static final Logger LOG = Logger.getLogger(PrepToolbar.class.getName());

    
    private LinkedHashMap <String, Button> toolbuttons;
    private PrepController prepctrl;
    
    
    public PrepToolbar( final Side side, final HiddenSidesPane pane, PrepController prep) {
        
        
        this.prepctrl = prep;
        toolbuttons = new LinkedHashMap<>();
        
        Button bt1;
        Tooltip tt;

        setAlignment(Pos.CENTER_LEFT);
        setPrefSize(200, 45);
        setSpacing(20);
        
        pane.setTriggerDistance(60.0);
        

        setStyle("-fx-background-color: rgba(255,255,255,1);"
                + "-fx-effect :  dropshadow(three-pass-box, black, 8.0, 0, 1.0, 1.0);"
                + "-fx-background-radius: 6;" 
                + "-fx-border-radius: 6;"
                + "-fx-padding: 0 0 10 40;"
                + "-fx-border-insets: 0 0 0 0;" 
                + "-fx-background-insets: 0 420 10 40;");

        Rectangle r = new Rectangle(20, 30);
        r.setStyle( "-fx-background-color: transparent;"
                +   "-fx-fill: transparent;" );
        this.getChildren().add(r);


        
        
        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.THUMB_TACK, "black", 2));
        bt1.setId("fxf-chartbt");
        tt = new Tooltip("Pin/Unpin Toolbar");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("pin", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                //LOG.info(String.format("Chart Toolbar buttom 1 pressed"));
                if (pane.getPinnedSide() != null) {
                    setStyle("-fx-background-color: rgba(255,255,255,1);"
                        + "-fx-effect :  dropshadow(three-pass-box, black, 8.0, 0, 1.0, 1.0);"
                        + "-fx-background-radius: 6;" 
                        + "-fx-border-radius: 6;"
                        + "-fx-padding: 0 0 10 40;"
                        + "-fx-border-insets: 0 0 0 0;" 
                        + "-fx-background-insets: 0 420 10 40;");
                    pane.setPinnedSide(null);
                } 
                else {
                    setStyle("-fx-background-color: rgba(255,255,255,1);"
                        + "-fx-effect :  dropshadow(three-pass-box, red, 8.0, 0, 1.0, 1.0);"
                        + "-fx-background-radius: 6;" 
                        + "-fx-border-radius: 6;"
                        + "-fx-padding: 0 0 10 40;"
                        + "-fx-border-insets: 0 0 0 0;" 
                        + "-fx-background-insets: 0 420 10 40;");
                    pane.setPinnedSide(side);
                }
            }  
        });   


        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.PLAY, "black", 2));
        bt1.setId("fxf-chartbt");
        tt = new Tooltip("Start preparation cycle");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("play", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("play pressed"));
                
            }  
        });   

        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.STOP, "black", 2));
        bt1.setId("fxf-chartbt");
        bt1.setDisable(true);
        tt = new Tooltip("Stop Preparation cycle\n Will cancel BOTH samples !");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("stop", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("stop pressed"));
                
            }  
        });  
        
        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.PAUSE, "black", 2));
        bt1.setId("fxf-chartbt");
        bt1.setDisable(true);
        tt = new Tooltip("Pause preparation cycle\n Will affect only this sample !");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("pause", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("stop pressed"));
                
            }  
        }); 
        
        Rectangle sep = new Rectangle(10, 20);
        sep.setOpacity(0.0);
        this.getChildren().add(sep);
        
        
        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.EYE, "black", 2));
        bt1.setId("fxf-chartbt");
        //bt1.setDisable(true);
        tt = new Tooltip("Show current status Painel");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("showcurrent", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("showcurrent pressed"));
                
            }  
        }); 
        
        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.GEAR, "black", 2));
        bt1.setId("fxf-chartbt");
        //bt1.setDisable(true);
        tt = new Tooltip("Show Edit Painel");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("showedit", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("showedit pressed"));
      
            }  
        });
        
        
        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.DATABASE, "black", 2));
        bt1.setId("fxf-chartbt");
        //bt1.setDisable(true);
        tt = new Tooltip("Overwrite / Save as new Profile.\n Use same name to overwrite.");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("saveprof", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("saveprof pressed"));
                
            }  
        });
        
        
        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.LINE_CHART, "black", 2));
        bt1.setId("fxf-chartbt");
        //bt1.setDisable(true);
        tt = new Tooltip("Show/Remove Gridlines");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("gridlines", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("showgridlines pressed"));
                
            }  
        });
        
        bt1 = new Button();
        bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.EDIT, "black", 2));
        bt1.setId("fxf-chartbt");
        //bt1.setDisable(true);
        tt = new Tooltip("Override Temp. profile path points");
        tt.setShowDelay(Duration.millis(500));
        bt1.setTooltip(tt);
        this.getChildren().add(bt1);
        toolbuttons.put("override", bt1);
        bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                LOG.info(String.format("override pressed"));
                
            }  
        });
        
        
        
        
    }

}
    