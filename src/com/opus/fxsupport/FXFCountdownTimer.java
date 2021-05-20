/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;
;

import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.TickListener;
import com.opus.syssupport.VirnaServiceProvider;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import middlestripb.BaseAnaTask;
import middlestripb.TaskState;


/**
 *
 * @author opus
 */
public class FXFCountdownTimer extends AnchorPane implements Initializable, TickListener{

    private static final Logger LOG = Logger.getLogger(FXFCountdownTimer.class.getName());
    
    @FXML private Arc bkgbar;
    @FXML private Label PClock;
    @FXML private Label SClock;
    @FXML private Label Status;
    
    private long duration = 0L;
    
    private ArrayList<FXFCountdownTimerBar> bars = new ArrayList<>();
    private boolean running = false;
    private long timer_tick = 100L;
    
    private Double last_angle;
    private Double tick_angle;
    
    private long last_tick;
    private Long init_ts;
    private Long end_ts;
    
    //private TimeStringConverter tsc;
    public static final String timestamp_format = "%1$tM:%1$tS:%1$tL";
    public static final String seconds_clock_format = "%1$tS:%1$tL";
    
    private String pclock_mode = "SECONDS";
    private String sclock_mode = "SECONDS";
    
    protected String overevent = "NULLEVENT";
    
    private LinkedHashMap<String, Color> barcolors;
    private SMTraffic timeout_state;
    
    private VirnaServiceProvider ctrl;
    
    
    
    public FXFCountdownTimer() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXFCountdownTimer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        //getStylesheets().add("com/opus/fxsupport/fxfsupport.css");
        barcolors = new LinkedHashMap<>();
        barcolors.put("RED", Color.RED);
        barcolors.put("BLUE", Color.DODGERBLUE);
        barcolors.put("YELLOW", Color.YELLOW);
        barcolors.put("GREEN", Color.GREEN);
        
        
        
        Status.setText("Waiting ...");
        
        PClock.setText(formatPClock(0L));
  
    }
    
   
    public void setCtrl(VirnaServiceProvider ctrl) {
        this.ctrl = ctrl;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initGraphics();  
    } 
    
    
    @Override
    public void processTick (Long tstamp){
        
//        LOG.info(String.format("Tick @ CDT ->  %03d @ %d", tstamp, ((System.currentTimeMillis() % 1000000))));
        if (running == true){
            tickTimer();
        }    
    }
    
    
    public void clearCounter(){
        
        running = false;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                clearBars();
                PClock.setText("0000:00");
                SClock.setText("Waiting...");
            }
        });
    }
    
    public void triggerTimer(){
        
        //String mes = "Changed timer to : %s";
        
        if (running == true) {
            running  = false;
        }
        else{
            last_angle = 0.0;
            last_tick = 0L;
            init_ts = System.currentTimeMillis();
            LOG.info(String.format("===========================================Timer started @ "+ timestamp_format, init_ts));
            sendEvent(bars.get(0));
            running = true;
        }
    }
   
    
    private void tickTimer(){
        
//        LOG.info(String.format("Tick Timer called @ %d", System.currentTimeMillis()));
        
        Platform.runLater(() -> {
//            LOG.info(String.format("Tick Timer now on platform thread @ %d -> %f / %d", 
//                    System.currentTimeMillis(),
//                    last_angle,
//                    last_tick
//                    ));
            last_angle = last_angle + tick_angle;
            last_tick = last_tick + timer_tick;

            setBar(last_tick, 0.0, last_angle);

            if (last_angle > 360.0){
                end_ts=System.currentTimeMillis();
                running = false;
//                LOG.info(String.format("Run took = %d msec to finish", end_ts-init_ts));
                sendEvent(null);
                setBar(0L, 0.0, 0.0);
            }
        });
    }
    
    
    public void setBar(Long tick, Double init, Double lenght){
        
        for (FXFCountdownTimerBar tbar : bars){
            if ((tick != 0) && (tick > tbar.getInit_tick()-50) && (tick < tbar.getInit_tick()+ 50)){ 
                sendEvent(tbar);
            }
            PClock.setText(formatPClock(System.currentTimeMillis()-init_ts));
            SClock.setText(formatSClock(tick, tbar.getInit_tick(), duration -(System.currentTimeMillis()-init_ts)));
            tbar.updateBar(init, lenght);
        }
        
    }
    
    private String formatSClock (long tick, long init_tick, long ts){
        
        String s = "";
        
        if (getSclock_mode() == null) return s;
            
        if(getSclock_mode().equals("SECONDS")){
            Double sec = ts / 1000.0;
            Double msec = (ts % 1000.0)/10;
            s = String.format("%04.0f:%02.0f", sec, msec);
        }
        else if(getSclock_mode().equals("SEGMENT_SECONDS")){
            long seg = tick - init_tick;
            
            s = String.format("%d", seg);
        }
        
        return s;
    }
    
    
    private String formatPClock (long ts){
        
        String s = "";
        
        if(pclock_mode.equals("SECONDS")){
            Double sec = ts / 1000.0;
            Double msec = (ts % 1000.0)/10;
            s = String.format("%04.0f:%02.0f", sec, msec);
        }
        
        return s;
    }
    
    
    
    
    
    
    public FXFCountdownTimer clearBars(){
        
        //ObservableList<Node> c = getChildren(); 
        for (FXFCountdownTimerBar tbar : bars){
            getChildren().remove(tbar.getBar());
        }
        bars.clear();
        
        return this;
    }
    
    
    public FXFCountdownTimer updateBars(){
        
        Double ms_degree;
        
        duration = 0L;
        for (FXFCountdownTimerBar tbar : bars){
            duration += tbar.getDuration();
        }
        
        ms_degree = duration / 360.0;
        tick_angle = timer_tick / ms_degree;
        last_angle = 0.0;
        
        
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
                for (int i = 0; i < bars.size(); i++) { 
                    FXFCountdownTimerBar wbar = bars.get(i);
                    if (i == 0){
                        wbar.setInit_angle(0.0);
                        wbar.setEnd_angle(wbar.getDuration() / ms_degree);
                        wbar.setInit_tick(0L);
                        wbar.setEnd_tick(wbar.getDuration());
                    }
                    else{
                        FXFCountdownTimerBar lastbar = bars.get(i-1);
                        wbar.setInit_angle(lastbar.getEnd_angle());
                        wbar.setEnd_angle(wbar.getInit_angle() + (wbar.getDuration() / ms_degree));
                        wbar.setInit_tick(lastbar.getEnd_tick());
                        wbar.setEnd_tick(wbar.getInit_tick() + wbar.getDuration());
                    }

                    Arc arc = wbar.getBar();
                    ObservableList<Node> ch = getChildren();
                    ch.add(arc);
                }
//            }
//        });
        
        return this;
    }
    
    
    public FXFCountdownTimer pushBar(long duration, String color, String message, 
                                String init_event, String end_event, SMTraffic init_task){
        
        FXFCountdownTimerBar ctbar = new FXFCountdownTimerBar(this, bars.size(), duration, color, message, 
                                init_event, end_event, init_task);
        bars.add(ctbar);
        return this;
    }

    
    private void sendEvent(FXFCountdownTimerBar tbar){
        
        long tstamp;
        
        if (tbar == null){
            tstamp = System.currentTimeMillis();
            
            if (timeout_state != null){
                BaseAnaTask bat = (BaseAnaTask)timeout_state.getPayload().vobject;
                //TaskState ts = bat.(timeout_state.getPayload().objecttype);
                bat.setCurrent_taskstate(bat.currentrealm+"_"+timeout_state.getPayload().objecttype);
                ctrl.processSignal(timeout_state);
            }
            
            LOG.info(String.format("Event Timer -> ended run @ " + timestamp_format, tstamp));
            Status.setText("Not Running...");
        }
        else{
            tstamp = System.currentTimeMillis()-init_ts;
            
            SMTraffic it = tbar.getInit_task();
            if (it != null){
                BaseAnaTask bat = (BaseAnaTask)it.getPayload().vobject;
                bat.setCurrent_taskstate(it.getPayload().objecttype);
                ctrl.processSignal(it);
                LOG.info(String.format("Event Timer -> reached init of %s @ %d", tbar.getMessage(), tstamp));
            }
           
            
            Status.setText(tbar.getMessage());
        }
        
        
    }
    
    
    
    public String getPclock_mode() {
        return pclock_mode;
    }

    public void setPclock_mode(String pclock_mode) {
        this.pclock_mode = pclock_mode;
    }

    public String getSclock_mode() {
        return sclock_mode;
    }

    public void setSclock_mode(String sclock_mode) {
        this.sclock_mode = sclock_mode;
    }

    

    public String getOverevent() {
        return overevent;
    }

    public void setOverevent(String overevent) {
        this.overevent = overevent;
    }

    private void initGraphics() {
        
    }
    
    public long getDuration() {
        return duration;
    }

    
    public FXFCountdownTimer setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public SMTraffic getTimeout_state() {
        return timeout_state;
    }

    public void setTimeout_state(SMTraffic timeout_state) {
        this.timeout_state = timeout_state;
    }
    
    
    
    
    
}









//Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
//        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);