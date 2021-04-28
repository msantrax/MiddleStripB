/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import java.util.ArrayList;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.controlsfx.control.HiddenSidesPane;

class JournalSideNode extends AnchorPane {

    private static final Logger LOG = Logger.getLogger(JournalSideNode.class.getName());
    public static final String tsfmt = "%1$td%1$tm%1$tY %1$tH%1$tM%1$tS:%1$tL-";  
    
    private TextArea jta;
    
    
    public JournalSideNode(final Side side, final HiddenSidesPane pane) {

        setPrefSize(50, 220);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        setStyle( "-fx-background-color: rgba(255,255,255,1);"
                + "-fx-effect : dropshadow(three-pass-box, black, 8.0, 0, 1.0, 1.0);"
                + "-fx-background-radius: 6;"
                + "-fx-border-radius: 6;"
                + "-fx-padding: 1 0 0 0;"
                + "-fx-border-insets: 15 0 5 0;" 
                + "-fx-background-insets: 15 15 15 15;");


        jta = new TextArea();
        AnchorPane.setRightAnchor(jta, 20.0);
        AnchorPane.setTopAnchor(jta, 20.0);
        AnchorPane.setBottomAnchor(jta, 20.0);
        AnchorPane.setLeftAnchor(jta, 65.0);
        
        jta.getStyleClass().add("journal-textarea");
        jta.setEditable(false);
        this.getChildren().add(jta);
//        loadJTA();
        
        
        Tooltip tt;
        
        VBox vbox = new VBox();
        
        AnchorPane.setTopAnchor(vbox, 20.0);
        AnchorPane.setBottomAnchor(vbox, 20.0);
        AnchorPane.setLeftAnchor(vbox, 20.0);
            
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPrefWidth(40);
//        vbox.setSpacing(20);
        
        

        Button expbt = new Button();
        expbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.EXPAND, "black", 1.5));
        expbt.setId("fxf-chartbt");
        tt = new Tooltip("Big/Small Journal Viewer");
        tt.setShowDelay(Duration.millis(500));
        expbt.setTooltip(tt);
        
        expbt.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                if(getHeight() == 220){
                   expbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.COMPRESS, "black", 1.5)); 
                   setPrefHeight(400);   
                }
                else{
                   expbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.EXPAND, "black", 1.5)); 
                   setPrefHeight(220); 
                }
            }  
        }); 

        
        Button lockbt = new Button();
        lockbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.ANCHOR, "black", 1.5));
        lockbt.setId("fxf-chartbt");
        tt = new Tooltip("Lock view position / See last entry");
        tt.setShowDelay(Duration.millis(500));
        lockbt.setTooltip(tt);
        
        lockbt.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                if(lockbt.getUserData() == Boolean.TRUE){
                   lockbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.ANCHOR, "black", 1.5)); 
                   lockbt.setUserData(Boolean.FALSE);
                }
                else{
                   lockbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.RETWEET, "black", 1.5)); 
                   lockbt.setUserData(Boolean.TRUE);
                }
            }  
        }); 
      
        
        Button pinnedbt = new Button();
        pinnedbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.THUMB_TACK, "black", 1.5));
        pinnedbt.setId("fxf-chartbt");
        tt = new Tooltip("Pin/Unpin Journal Viewer");
        tt.setShowDelay(Duration.millis(500));
        pinnedbt.setTooltip(tt);
        
        pinnedbt.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent event) {
                if (pane.getPinnedSide() != null) {
                    expbt.setDisable(false);
                    setStyle("-fx-background-color: rgba(255,255,255,1);"
                            + "-fx-effect :  dropshadow(three-pass-box, black, 8.0, 0, 1.0, 1.0);"
                            + "-fx-background-radius: 6;" 
                            + "-fx-border-radius: 6;"
                            + "-fx-padding: 1 0 0 0;"
                            + "-fx-border-insets: 15 0 5 0;" 
                            + "-fx-background-insets: 15 15 15 15;");
                    pane.setPinnedSide(null);
                } else {
                    expbt.setDisable(true);
                    setStyle("-fx-background-color: rgba(255,255,255,1);"
                            + "-fx-effect : dropshadow(three-pass-box, red, 8.0, 0, 1.0, 1.0);"
                            + "-fx-background-radius: 6;" 
                            + "-fx-border-radius: 6;"
                            + "-fx-padding: 1 0 0 0;"
                            + "-fx-border-insets: 15 0 5 0;" 
                            + "-fx-background-insets: 15 15 15 15;");
                    pane.setPinnedSide(side);
                }
            }  
        }); 
        
        vbox.getChildren().add(pinnedbt);
        vbox.getChildren().add(expbt);
        vbox.getChildren().add(lockbt);
        
        this.getChildren().add(vbox);
      
    }
    
    public void addEntry(String entry, ArrayList<String>lines){
        
        String stmst = String.format("%1$td%1$tm%1$ty-%1$tH%1$tM%1$tS:%1$tL  %2$s\n", System.currentTimeMillis(), entry);
        jta.appendText(stmst);
        
        if (lines != null){
            for (String s : lines){
                if (s.contains("\\t")){
                    jta.appendText(s+"\n");
                }
                else{
                    jta.appendText("\t\u2514 "+s+"\n");
                }
            }
        }
    }
    
    public void addEntry(String entry){
        
        String[] tokens = entry.split("\\|");
        for (int i = 0; i < tokens.length; i++) {
            String line = "";
            if (i == 0){
                line = String.format("%1$td%1$tm%1$ty-%1$tH%1$tM%1$tS:%1$tL  %2$s\n", System.currentTimeMillis(), tokens[0]);
            }
            else{
                String s = tokens[i];
                if (s.contains("\\t")){
                    line = tokens[i]+"\n";
                }
                else{
                    line = "\t\u2514 "+tokens[i]+"\n";
                }
            }
            jta.appendText(line);
        }
     
    }
    
    
    
    
    
    
    
    private void loadJTA(){
        
        String load = "May 29 15:05:42 2014   Received: C:\\Setups\\Setup_99.DAT\n" +
        "May 29 15:05:43 2014   Received: C:\\Presets\\NovaWin!.RUN\n" +
        "May 29 15:05:43 2014   C:\\Presets\\NovaWin!.run StOn=01h, Setup[99, 0, 0, 0], Cell[8323, 4227, 4227, 4227]\n" +
        "May 29 15:05:43 2014   Sample Weight[0.1839 0.1000 0.1000 0.1000] Density[9999.0000 9999.0000 9999.0000 9999.0000] NovaWin! \n" +
        "May 29 15:05:43 2014    xover=70\n" +
        "May 29 15:05:43 2014   [t Down] -0.016\n" +
        "May 29 15:05:43 2014   C:\\Setups\\Setup_99.dat Ads: Type=0 Density=0.806000 MolWeight=28.013399 XSectionArea=16.200001\n" +
        "May 29 15:05:43 2014   WeightPerCC=0.001250 SurfAreaPerGram=3482.516602 CorrFactor=0.000066 BathT=77.349998 Pmax=0.000000\n" +
        "May 29 15:05:44 2014   P0=760.000 P0Type=104h PtsPerP0=5 TEquTime=600 ptsReq: BET=0 ADS=30 DES=20\n" +
        "May 29 15:05:44 2014   ADS[0.100, 60, 240] DES[0.100,  60, 240] BJH=0 POR=0 STSA=0\n" +
        "May 29 15:05:45 2014   He mode. BackFill:ADS.  HePumpTime= 180 secs CellType= 2 (0,StA)\n" +
        "May 29 15:05:45 2014   (4)[t Down] -0.016\n" +
        "May 29 15:05:45 2014   (5)[t Down] 0.000\n" +
        "May 29 15:05:45 2014   DeltaPCell=0: 0.000mmHg [t Down]10ms [t Down]50ms\n" +
        "May 29 15:05:52 2014   [t Down] P= 0.016 after 4sec  lvon=60 maxDp= 0.016mmHg\n" +
        "May 29 15:05:53 2014   BuildPressure 700.000: [t Down] ~He xover 70.000 InitialP:0.000\n" +
        "May 29 15:06:09 2014   Achieved: P 701.725 tol 0.246 dP 1.725 Vact=79 Gast=20 loop=0\n" +
        "May 29 15:06:09 2014   DeltaPCell=0: 700.732mmHg [STA t Down]10ms [t Down]50ms\n" +
        "May 29 15:06:15 2014   [STA t Down] P= 697.703 after 3sec  lvon=14 maxDp= 1.433mmHg\n" +
        "May 29 15:06:16 2014   !A using C:\\Data\\N452901.dat and C:\\System\\LastData.001\n" +
        "May 29 15:06:16 2014               Analyzing Sample\n" +
        "May 29 15:06:16 2014   Calculating P0...\n" +
        "May 29 15:06:17 2014   BuildPressure 780.000: [t Down] ~He xover 70.000 InitialP:697.686\n" +
        "May 29 15:06:18 2014   Achieved: P 777.151 tol 0.365 dP 2.849 Vact=80 Gast=21 loop=0\n" +
        "May 29 15:06:21 2014   [775.3] Ambient Pressure Measurement...";
        
         jta.appendText(load);
        
    }
    
    
}





