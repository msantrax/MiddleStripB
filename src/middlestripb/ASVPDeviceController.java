/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.fxsupport.FXFBargraph;
import com.opus.fxsupport.FXFCenterBargraph;
import com.opus.fxsupport.FXFCountdownTimer;
import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
    private FXFCenterBargraph sigma_brgf;

    
    @FXML
    void pause_action(MouseEvent event) {
//        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "PAUSEACTION", this.getClass(), new VirnaPayload()));
        LOG.info("pause clicked @ " + System.currentTimeMillis());
        press_bgrf.setValue(213.27);
        deltap_bgrf.setValue(1.8);
        sigma_brgf.setValue(-1.1);
        
//        Timer timer = new Timer();
//      
//        TimerTask task = new TimerTask(){
//            Double vl = 0.0;
//            @Override
//            public void run(){
//                Platform.runLater(() -> {
//                    LOG.info(String.format("Vl = %f @ %d", vl, System.currentTimeMillis()));
//                    press_bgrf.setValue(vl);
//                    deltap_bgrf.setValue(1.8);
//                    sigma_brgf.setValue(-1.1);
//                    vl +=20.0;
//                    if (vl > 780.0) timer.cancel();
//                });
//            }
//        };
//
//        
//        timer.schedule(task, 0l , 250l);
        
        LOG.info("pause finished @ " + System.currentTimeMillis());
        
       
    }

    @FXML
    void start_action(MouseEvent event) {
//        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "STARTACTION", this.getClass(), new VirnaPayload()));
        LOG.info("start clicked @ " + System.currentTimeMillis());
        press_bgrf.setValue(956.0);
        deltap_bgrf.setValue(-0.2);
        sigma_brgf.setValue(1.1);
        LOG.info("start finished @ " + System.currentTimeMillis());
    }

    @FXML
    void stop_action(MouseEvent event) {
//        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "STOPACTION", this.getClass(), new VirnaPayload()));
        LOG.info("Stop clicked @ " + System.currentTimeMillis());
        press_bgrf.setValue(-120.0);
        deltap_bgrf.setValue(2.8);
        sigma_brgf.setValue(0.5);
        LOG.info("stop finished @ " + System.currentTimeMillis());
    }
    
    
    
    public enum Status { STOPPED, RUNNING, PAUSED, TEST };
    
    private FX1Controller anct;
    private Controller ctrl;
    private ASVPDevice asvpdev;
    private ASVPDEVBargraphDescriptor bgraphdesc;
    
    
    public ASVPDeviceController(Controller ctrl, FX1Controller anct, ASVPDevice asvpdev ) {
        
        
        this.anct = anct;
        this.ctrl = ctrl;
        this.asvpdev = asvpdev;
        
        bgraphdesc = new ASVPDEVBargraphDescriptor();
        
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ASVPDevice.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
      
    
        
    }

    
    
    
    // ==========================================   WIDGETS INTERFACE ============================================================
    
    public void setStatus (Status sts){
          
        switch (sts){
            
            case STOPPED:
                startbt.setDisable(false);
                stopbt.setDisable(true);
                pausebt.setDisable(true);
                break;
            case RUNNING:
                startbt.setDisable(true);
                stopbt.setDisable(false);
                pausebt.setDisable(false);
                break;    
            case PAUSED:
                startbt.setDisable(false);
                stopbt.setDisable(false);
                pausebt.setDisable(true);
                break;    
            case TEST:
                startbt.setDisable(false);
                stopbt.setDisable(false);
                pausebt.setDisable(false);
                break;    
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
    
    
    public void updateBarGraphs(Double value, Double dp, Double var){
        
        press_bgrf.setValue(value);
        deltap_bgrf.setValue(dp);
        sigma_brgf.setValue(var);
        
        
    }
    
    public void initBarGraphs(){
        
        press_bgrf.setTitle(bgraphdesc.main_label);
        press_bgrf.setRange(bgraphdesc.main_rangeformat, bgraphdesc.main_low, bgraphdesc.main_high,
                                    bgraphdesc.main_threshold, bgraphdesc.main_inverted);
        
        deltap_bgrf.setTitle(bgraphdesc.dpdt_label);
        deltap_bgrf.setRange(bgraphdesc.dpdt_rangeformat, bgraphdesc.dpdt_low, bgraphdesc.dpdt_high,
                                    bgraphdesc.dpdt_threshold, bgraphdesc.dpdt_inverted);
        
        sigma_brgf.setTitle(bgraphdesc.sigma_label);
        sigma_brgf.setRange(bgraphdesc.sigma_rangeformat, bgraphdesc.sigma_range,
                                    bgraphdesc.sigma_threshold, bgraphdesc.sigma_inverted);
        
        
        
        press_bgrf.setValue(200.0);
        deltap_bgrf.setValue(2.4);
        sigma_brgf.setValue(0.0);
      
        
        
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
        
        this.initBarGraphs();
        this.setStatus(Status.TEST);
        
        
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
    
    
    
    public FXFCountdownTimer getCDT() {
        return cdt;
    }
    
    
    
}




 
//        final Timeline timeline = new Timeline();
//        //timeline.setCycleCount(Timeline.INDEFINITE);
//        //timeline.setAutoReverse(true);
////        
////        AnimationTimer timer = new AnimationTimer() {
////            @Override
////            public void handle(long l) {
////                LOG.info("timer...");
////            }
//// 
////        };
////        
//        Duration duration = Duration.millis(4000);
////        //one can add a specific action when the keyframe is reached
////        EventHandler onFinished = new EventHandler<ActionEvent>() {
////            public void handle(ActionEvent t) {
////                LOG.info("finished");
////            }
////        };
////        
//        final KeyValue kv = new KeyValue(press_bgrf.getVdata(), 800);
//        final KeyFrame kf = new KeyFrame(duration, kv);
//        timeline.getKeyFrames().add(kf);
//        timeline.play();
////        
//        


//Timer timer = new Timer();
//      
//        TimerTask task = new TimerTask(){
//            Double vl = 0.0;
//            @Override
//            public void run(){
//                Platform.runLater(() -> {
//                    LOG.info(String.format("Vl = %f", vl));
//                    press_bgrf.setValue(vl);
//                    vl +=5.0;
//                    if (vl > 780.0) timer.cancel();
//                });
//            }
//        };
//
//        
//        timer.schedule(task, 0l , 50l);