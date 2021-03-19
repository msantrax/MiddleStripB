/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.fxsupport.FXFBargraph;
import com.opus.fxsupport.FXFControllerInterface;
import com.opus.fxsupport.FXFCountdownTimer;
import com.opus.fxsupport.FXFFieldDescriptor;
import com.opus.fxsupport.WidgetContext;
import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ASVPDeviceController extends AnchorPane implements Initializable {

    
    private static final Logger LOG = Logger.getLogger(ASVPDeviceController.class.getName());

    
    @FXML
    private AnchorPane asvp;

    @FXML
    private Label led_fail;

    @FXML
    private Label led_charge;

    @FXML
    private Label led_final;

    @FXML
    private Label led_final1;

    @FXML
    private Label opmodo;

    @FXML
    private FXFCountdownTimer cdt;

    @FXML
    private FXFBargraph press_bgrf;

    @FXML
    private FXFBargraph deltap_bgrf;

    @FXML
    private Label startbt;

    @FXML
    private Label stopbt;

    @FXML
    private Label pausebt;

    @FXML
    private FXFBargraph sigma;

    @FXML
    void pause_action(MouseEvent event) {

    }

    @FXML
    void start_action(MouseEvent event) {

    }

    @FXML
    void stop_action(MouseEvent event) {

    }
    
    
    public ASVPDeviceController() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ASVPDevice.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
      
    }

    
    // =========================================== WIDGET CONTEXT & MANAGEMENT =======================================
    private FXFControllerInterface controller;
    private WidgetContext wctx;
    private Integer focusPosition = 0;
    
    
    public void setManagement(FXFControllerInterface controller, Integer idx, WidgetContext wctx){
        this.controller = controller;
        this.focusPosition = idx;
        this.wctx = wctx;
    }
    
    public void setFocusPosition(Integer pos){
        this.focusPosition = pos;
    }
    
    public Integer getFocusPosition (){
        return focusPosition;
    }
    
    public void setFocus(boolean set){
        
        if (set){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    requestFocus();
                }
            });
        }
        else{
           setFocused(false); 
        }
    }

    
    public void activateLed (String id, boolean status, boolean blink){
        
        if (id.equals("fail")){
            if (status){
                led_fail.getStyleClass().remove("fxf-ledfail-off");
                led_fail.getStyleClass().add("fxf-ledfail-on");
            }
            else{
                led_fail.getStyleClass().remove("fxf-ledfail-on");
                led_fail.getStyleClass().add("fxf-ledfail-off");
            }
        }
        else if (id.equals("charge")){
            if (status){
                led_charge.getStyleClass().remove("fxf-ledcharge-off");
                led_charge.getStyleClass().add("fxf-ledcharge-on");
            }
            else{
                led_charge.getStyleClass().remove("fxf-ledcharge-on");
                led_charge.getStyleClass().add("fxf-ledcharge-off");
            }
        }
        else if (id.equals("final")){
            if (status){
                led_final.getStyleClass().remove("fxf-ledfinal-off");
                led_final.getStyleClass().add("fxf-ledfinal-on");
            }
            else{
                led_final.getStyleClass().remove("fxf-ledfinal-on");
                led_final.getStyleClass().add("fxf-ledfinal-off");
            }
        }
   
    }
    
    public void updateProfile(){
         
  
    }
    
    
    public void setMode ( boolean running, String message){
        
       
    }
    
    
    
    public void initProfile (FXFFieldDescriptor fxfd, FXFFieldDescriptor average, FXFFieldDescriptor rsd){
//        
//        this.fxfd = fxfd;
//        this.analisefd = average;
//        //this.analisefield = analisefd.getField(FXFTextField.class);
//        this.rsdfd = rsd;
//        //this.rsdfield = rsdfd.getField(FXFTextField.class);
//        
//        if (fxfd.getCustom() instanceof LinkedTreeMap){
//            LinkedTreeMap ltm = (LinkedTreeMap)fxfd.getCustom();
//            setBfd(new BlaineFieldDescriptor());
//            if (ltm.size() != 0){
//                Gson gson = new Gson();
//                JsonObject jobj = gson.toJsonTree(ltm).getAsJsonObject();
//                getBfd().setOpmode(jobj.get("opmode").getAsString());
//                getBfd().setMaxruns(jobj.get("maxruns").getAsInt());
//                getBfd().setSkipfirst(jobj.get("skipfirst").getAsBoolean());
//                getBfd().setInterrun(jobj.get("interrun").getAsDouble());
//                getBfd().setAn_timeout(jobj.get("an_timeout").getAsDouble());
//                getBfd().setAuto_timeout(jobj.get("auto_timeout").getAsDouble());
//            }
//        }
//        else{
//            setBfd((BlaineFieldDescriptor)fxfd.getCustom());
//        }
//        
//        updateProfile();
        
    }
    
    
    
    // Application controller link 
    private Controller ctrl;
    public void setAppController (Controller ctrl){
        this.ctrl = ctrl;
//        cdt.setCtrl((com.opus.syssupport.VirnaServiceProvider)ctrl);
    }
    
    public FXFCountdownTimer getCDT() {
        return cdt;
    }
    
    
    
    
    public void initAnalises(){
    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        
//        cdt = new FXFCountdownTimer();
        
        startbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.GEARS, "black", 3));
        stopbt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.STOP, "black", 3));
        pausebt.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.PAUSE, "black", 3));
        
        
        cdt.setPclock_mode("SECONDS");
        cdt.setSclock_mode("SEGMENT_SECONDS");
        
        
        cdt.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    LOG.info(String.format("Blaine setup Context requested @ %f/%f",  e.getScreenX(), e.getScreenY()));                    
//                    ContextMenu ctxm = getTimmingMenu(cdt, fxfd);
//                    ctxm.show(cdt, e.getScreenX(), e.getScreenY());
                    e.consume();
                }
                else{
                    //if (desktop_context != null) desktop_context.hide();
                }
                e.consume();
            }
        });
    
    }
    
}


