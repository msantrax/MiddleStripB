/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;

import com.opus.syssupport.SMTraffic;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author opus
 */
public class FXFCountdownTimerBar {

    
    protected static final double                   BARX  = 70;
    protected static final double                   BARY  = 71;
    protected static final double                   BARW  = 52;
    protected static final double                   BARH  = 52;
    protected static final double                   BARS  = 12;
    
    private Arc bar;
    private FXFCountdownTimer ctrl;
    private int index;
    
    private Double init_angle; // inicio da área de pintura em graus absolutos
    private Double end_angle;  // fim da área de pintura em graus absolutos
    private Long duration;
    private Long init_tick;
    private Long end_tick;
    private Color bar_color;
    private Boolean full = false;
    private String message;
    
    private String init_event;
    private String end_event;
    private SMTraffic init_task;    
    
    
    public FXFCountdownTimerBar(Double init_angle, Double end_angle, Color bar_color) {
        
        this.init_angle = init_angle;
        this.end_angle = end_angle;
        this.bar_color = bar_color;
        
        Double start_angle = 90-init_angle;
        if (start_angle == 90){
            start_angle = 89.0;
        }
        
        bar = new Arc(BARX, BARY, BARW, BARH, start_angle, 0);
        bar.setType(ArcType.OPEN);
        bar.setStroke(bar_color);
        bar.setStrokeWidth(BARS);
        bar.setStrokeLineCap(StrokeLineCap.BUTT);
        bar.setFill(null);
        
    }

    
    
    public FXFCountdownTimerBar(FXFCountdownTimer ctrl, int index,
                                    Long duration, String bar_color, String message, 
                                    String init_event, String end_event,
                                    SMTraffic init_task) {
        this.ctrl = ctrl;
        this.index = index;
        this.duration = duration;
        this.bar_color = Color.valueOf(bar_color);
        this.message = message;
        this.init_event = init_event;
        this.end_event = end_event;
        this.init_task = init_task;
        
        bar = new Arc(BARX, BARY, BARW, BARH, 0, 0);
        bar.setType(ArcType.OPEN);
        bar.setStroke(this.bar_color);
        bar.setStrokeWidth(BARS);
        bar.setStrokeLineCap(StrokeLineCap.BUTT);
        bar.setFill(null);
     
    }
    
    
    
    
    public void updateBar(Double init, Double lenght){
        
        Double bar_start_angle;
        Double bar_arc_lenght;
         
        if (full) return;
        
        Double end = init + lenght;
        
        // O Segmento está dentro da area a renderizar ?
        // O pincel começa ou deve passar pela area...
        if (init < end_angle && end > init_angle){
            // Sim, área de interesse
            // Agora ajuste o inicio da area
            // Se já estamos pintando de outro segmento o init é menor
            if (init <= init_angle){
                // Vem de outro pinte somente nossa area
                bar_start_angle = 90-init_angle;
            }
            else{
                // A pintura é específica para essa área
                bar_start_angle = 90-init;
            }
            
            // Agora calcule o comprimento do arco nesse segmento
            // Vamos pintar o segmento inteiro ?
            if (end > end_angle){
                // Sim 
                bar_arc_lenght = ((90-end_angle) - bar_start_angle);
            }
            else{
                
                bar_arc_lenght = (lenght-init_angle) * -1.0;
            }
            
            // Caso onde o segmento é o inicio do timer
            // Começar um pixel para frente para permitir o display da barra de zero.
            if (bar_start_angle == 90){
                bar_start_angle = 89.0;
                bar_arc_lenght = bar_arc_lenght -1.0;
            }

        }
        else{
            bar_start_angle = 90-init_angle;
            bar_arc_lenght = 0.0;
        }
        
        getBar().setLength(bar_arc_lenght);
        getBar().setStartAngle(bar_start_angle);
        
    }
    
   
    public Arc getBar() {
        return bar;
    }

    private void setBar(Arc bar) {
        this.bar = bar;
    }

    
    public Double getInit_angle() {
        return init_angle;
    }

    public FXFCountdownTimerBar setInit_angle(Double init_angle) {
        this.init_angle = init_angle;
        return this;
    }

    public Double getEnd_angle() {
        return end_angle;
    }

    public FXFCountdownTimerBar setEnd_angle(Double end_angle) {
        this.end_angle = end_angle;
        return this;
    }

    public Color getBar_color() {
        return bar_color;
    }

    public FXFCountdownTimerBar setBar_color(Color bar_color) {
        this.bar_color = bar_color;
        return this;
    }

    public Boolean getFull() {
        return full;
    }

    public FXFCountdownTimerBar setFull(Boolean full) {
        this.full = full;
        return this;
    }
    
    public String getMessage() {
        return message;
    }

    public FXFCountdownTimerBar setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getInit_event() {
        return init_event;
    }

    public FXFCountdownTimerBar setInit_event(String init_event) {
        this.init_event = init_event;
        return this;
    }

    public String getEnd_event() {
        return end_event;
    }

    public FXFCountdownTimerBar setEnd_event(String end_event) {
        this.end_event = end_event;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public FXFCountdownTimerBar setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public Long getInit_tick() {
        return init_tick;
    }

    public void setInit_tick(Long init_tick) {
        this.init_tick = init_tick;
    }

    public Long getEnd_tick() {
        return end_tick;
    }

    public void setEnd_tick(Long end_tick) {
        this.end_tick = end_tick;
    }

    public SMTraffic getInit_task() {
        return init_task;
    }

    public void setInit_task(SMTraffic init_task) {
        this.init_task = init_task;
    }
    
}
