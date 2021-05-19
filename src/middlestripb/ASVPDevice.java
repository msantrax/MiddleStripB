/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Entity;
import cern.extjfx.chart.NumericAxis;
import cern.extjfx.chart.plugins.XRangeIndicator;
import cern.extjfx.chart.plugins.XValueIndicator;
import cern.extjfx.chart.plugins.YRangeIndicator;
import cern.extjfx.chart.plugins.YValueIndicator;
import com.fazecast.jSerialComm.SerialPort;
import com.mongodb.client.model.Filters;
import com.opus.fxsupport.FXFCountdownTimer;
import com.opus.syssupport.Config;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import isothermview.Isothermv;
import isothermview.IsothermBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;

/**
 *
 * @author opus
 */
public class ASVPDevice implements SerialDevice.SerialDeviceListener{

    
    private static final Logger log = Logger.getLogger(ASVPDevice.class.getName());
    
    public static final String JSONS = "/Bascon/ASVP/ASVP_ANA/Sandbox/MiddleStripB/src/JSONS/";
    
    private static ASVPDevice instance; 
    public static ASVPDevice getInstance(){
        if (instance == null) {instance = new ASVPDevice();}
        return instance;
    }
    
    
    public ASVPDevice() {
//        devstate = DEVICE_STATE.DISCONNECTED; 
    }
    
    
    private Controller ctrl = Controller.getInstance();  
    public void setAppController (Controller ctrl){
        this.ctrl = ctrl;
        
    }
    
    private FX1Controller anct;
    public void setFXController (FX1Controller anct){
        this.anct = anct;
        initProperties();
    }

    public FX1Controller getAnct() {
        return anct;
    }
   
   
    private ASVPDeviceController asvpdevctrl;  
    public void setASVPDevController (ASVPDeviceController ctrl){
        this.asvpdevctrl = ctrl;
        
    }
    
    private Context ctx = Context.getInstance();
    
    private MongoLink mongolink = MongoLink.getInstance();
    
    
    // ================================================ ISOTHERM SERVICES ====================================================
    
    private Isothermv isotherm;
    private IsothermBean isobean;
    
    public Isothermv getIsotherm() {
        return isotherm;
    }

    public void setIsotherm(Isothermv isotherm) {
        this.isotherm = isotherm;
        this.isobean = isotherm.getIsothermBean();  
    }
    
    
    
    

    // ============================================== PROPERTIES & BINDING SUPPORT ==============================================
    
    
    private LinkedHashMap<String, Double[]> datacalibs;
    
    
    private void updateDataProp(String name, Long value){
        
        Double[] calib = datacalibs.get(name);
//        log.info(String.format("Setra Data =   %d ", value));
        Double calc = calib[0] + (calib[1] * value) + (calib[2] * (value * value));
       
        ctx.current_anatask.addSample(System.currentTimeMillis(), calc, value);
        
        
    }
    
    
    
    private Double[] getPropCalib (String name){
        
        double setraA0 = -57.58;
        double setraA1 = 0.0009;
        double setraA2 = 0.0;
 
        if (name == "setra"){
            Double setracal[] = {setraA0, setraA1, setraA2};
            return setracal;
        } 
        
        return null;
    }
    
    
    private void initProperties(){
        
        
        datacalibs = new LinkedHashMap<>();
        
        datacalibs.put("setra", getPropCalib ("setra"));
        ctx.setAsvpdev(this);
        
    }
    
    
    public void loadTaskConfig(Class cls, String taskid, Long suid){
        
        
        Object tskcfg = mongolink.getLoaded_descriptors().findById(suid, true);
        if (tskcfg != null){
            BaseAnaTask tsk = ctx.anatasks.get(taskid);
            if (tsk != null){
                tsk.setDataframe(tskcfg);
                tsk.Go();
            }
            return;
        }
        
        
        EntityDescriptor ed = new EntityDescriptor()
               .setClazz(cls)
               .setBson(Filters.eq("suid", suid))
               .setCascade(Boolean.TRUE)
               .setAction(new SMTraffic(0l, 0l, 0, "UPDATEANATASK", this.getClass(),
                       new VirnaPayload()
                               .setCallerstate("LOADPHASE1")
                               .setString(taskid)
               ));         
        mongolink.getTask_descriptors().offer(ed);
        
    }
    
    @smstate (state = "UPDATEANATASK")
    public boolean st_updateAnaTask(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        String substate = payload.getCallerstate();
        
        
        switch(substate){
            
            case "LOADPHASE1" :
                Entity etph2 = (Entity)payload.vobject;
                etph2.loadChildren(false, new SMTraffic(0l, 0l, 0, "UPDATEANATASK", this.getClass(),
                        new VirnaPayload()
                                .setCallerstate("LOADPHASE2")
                                .setString(payload.vstring)
                        )); 
                //log.info(String.format("Update ana task arrived"));
                break;
                
            case "LOADPHASE2" :
                Entity et = (Entity)payload.vobject;
                
                BaseAnaTask tsk = ctx.anatasks.get(payload.vstring);
                if (tsk != null){
                    tsk.setDataframe(et);
                    tsk.Go();
                }
                
//                log.info(String.format("Update ana task arrived"));
                break;
        
        }
        
        
        
        return true;
    }
    
    
    
    // ================================================ PLUMBING ================================================================
    
    /**
     * Bargraph update wrapper
     * @param val
     * @param diff
     * @param variance 
     */
    public void sendtoBarGraphs (Double val, Double diff, Double variance){
        
        asvpdevctrl.updateBarGraphs(val, diff, variance);
    }
    
    
    
    
    
    // ================================================test states ===============================================================
    
    

    
    
    // ================================================== FINAL STATES =====================================================
    
    
    @smstate (state = "SHOWEVENT")
    public boolean st_showEvent(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            AuxChartDescriptor cd = ctx.auxcharts.get(tsk.taskid);
  
            Platform.runLater(() -> {
                XValueIndicator<Number> indicator = AuxChartDescriptor.XValIndicatorFactory (
                        "showindicator1", "Stab Init", tsk.current_dts, null);
                ctrl.processSignal(new SMTraffic(0l, 0l, 0, "AUXSHOWINDICATOR", this.getClass(),
                                       new VirnaPayload().setObject(indicator)
                                               .setLong1(0L)
                                               .setServicestatus("showindicator1")
                                               .setString(tsk.taskid)
                                               .setObjectType("XValue")));
            });
            
            
            SMTraffic nxt = tsk.goNext(tst.getImediate());
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
        }
        else{
            log.info(String.format("Notify Aux"));
        }
       
        return true;
    }
    
    
    
    @smstate (state = "MONITORVALUES")
    public boolean st_monitorValues(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
  
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
//            log.info(String.format("Seting Automation to : %s", tst.getStatecmd()));
     
            Platform.runLater(() -> {
                
                Double measure;
                if (Double.compare(tst.getParam1(), 0.0) == 0){
                    measure=tst.getParam2();
                    tsk.diffvalue = true;
                }
                else{
                    measure=tst.getParam1();
                    tsk.diffvalue = false;
                }
                
                if (tst.getFlag()){
                    tsk.valueunder = -1.0;
                    tsk.valueover = measure;
                }
                else{
                    tsk.valueover = -1.0;
                    tsk.valueunder = measure;
                }
                tsk.valuetask = tst.getImediate();
                
                
                asvpdevctrl.activateLed("final", true, true);
                if(tst.getChronosegs() != null && !tst.getChronosegs().isEmpty()){
                    FXFCountdownTimer cdt = asvpdevctrl.getCDT();
                    cdt.setTimeout_state(tsk.getNext(tst.getTimedout()));
                    cdt.clearCounter();
                    cdt.clearBars();

                    for (TaskStateChronoSegment tscs : tst.getChronosegs()){
                        cdt.pushBar(tscs.lenght, tscs.color, tscs.label, "", "", tsk.getNext(tscs.callback));
                    }

                    cdt.updateBars();
                    cdt.triggerTimer();
                }
            });
            
            updateNotify(tsk,tst);
        }
        else{
            
            
        }
        
        return true;
    }
    
    
    
    @smstate (state = "SETCHARTEVENT")
    public boolean st_setChartEvent(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            String cmd = tst.getStatecmd().toUpperCase().trim();
            
            AuxChart chart = tsk.auxchart;
            AuxChartDescriptor acd = tsk.ctx.auxcharts.get(tsk.taskid);
        
            VarPool vp = tsk.getVarPool();
            
            
            Platform.runLater(() -> {
                
                YValueIndicator<Number>  yvi = acd.yvalindicators.get(tst.getSparam1()) ;
                XValueIndicator<Number>  xvi = acd.xvalindicators.get(tst.getSparam1()) ;
                Long timeout;
                
                switch (cmd){
                    
                    case "SETX":
                        if (xvi != null){
                            chart.getChartPane().getPlugins().remove(xvi);
                            acd.xvalindicators.remove(tst.getSparam1());
                        }
                        xvi = AuxChartDescriptor.XValIndicatorFactory(tst.getSparam1(), tst.getSparam2(), 
                                tst.getParam1(), tst.getParam2());
                        acd.xvalindicators.put(tst.getSparam1(), xvi);
                        chart.getChartPane().getPlugins().add(xvi);
                        
                        timeout = tst.getTimeout();
                        if ( timeout != null && timeout != 0L){
                            SMTraffic alarm_config = new SMTraffic(0l, 1l, 0, "REMOVECHARTEVENT", this.getClass(),
                                    new VirnaPayload().setObjectType("XValue")
                                            .setString(tst.getSparam1())
                                            .setObject(tsk)
                            );
                            ctrl.setAlarm (-1l, -4, alarm_config, timeout, 0);
                        }
                        break;
                        
                    case "CLEARX":
                        if (xvi != null){
                            chart.getChartPane().getPlugins().remove(xvi);
                            acd.xvalindicators.remove(tst.getSparam1());
                        }
                        break;
                        
                    case "LOADX":
                        if (xvi != null){
                            chart.getChartPane().getPlugins().remove(xvi);
                            acd.xvalindicators.remove(tst.getSparam1());
                        }
                        xvi = AuxChartDescriptor.XValIndicatorFactory(vp.Pop("CHARTEVENTID", String.class), 
                                vp.Pop("CHARTEVENTLABEL", String.class), 
                                vp.Pop("CHARTEVENTVALUE", Double.class), vp.Pop("CHARTEVENTPOS", Double.class));
                        acd.xvalindicators.put(tst.getSparam1(), xvi);
                        chart.getChartPane().getPlugins().add(xvi);
                        
                        timeout = tst.getTimeout();
                        if ( timeout != null && timeout != 0L){
                            SMTraffic alarm_config = new SMTraffic(0l, 1l, 0, "REMOVECHARTEVENT", this.getClass(),
                                    new VirnaPayload().setObjectType("XValue")
                                            .setString(vp.Pop("CHARTEVENTID", String.class))
                                            .setObject(tsk)
                            );
                            ctrl.setAlarm (-1l, -4, alarm_config, timeout, 0);
                        }
                        break;
                    
                        
                    default:
                        break;
                        
                }

                SMTraffic nxt = tsk.goNext(tst.getImediate());
                if (nxt != null){
                    Controller.getInstance().processSignal(nxt);
                }
            });
        }
        else{
            log.info(String.format("Set range band requested"));
        }
       
        return true;
    }
    
    
    @smstate (state = "REMOVECHARTEVENT")
    public boolean st_removeChartEvent(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
        String type = pld.objecttype;
        String eventid = pld.vstring;
         
        AuxChart chart = tsk.auxchart;
        AuxChartDescriptor acd = tsk.ctx.auxcharts.get(tsk.taskid);
        
        Platform.runLater(() -> {
             
            boolean hasartifact = false;
            
            if (type.equals("XValue")){
                if (acd.xvalindicators.containsKey(eventid)){
                    XValueIndicator<Number> ind = acd.xvalindicators.get(eventid);
                    acd.xvalindicators.remove(eventid);
                    chart.getChartPane().getPlugins().remove(ind);
                    hasartifact = true;
                }
            }
            if (type.equals("YValue")){
                if (!acd.yvalindicators.containsKey(eventid)){
                    YValueIndicator<Number> ind = acd.yvalindicators.get(eventid);
                    acd.yvalindicators.put(eventid, ind);
                    chart.getChartPane().getPlugins().remove(ind);
                    hasartifact = true;
                }
            }
          
            if (!hasartifact) log.info(String.format("Aux chart has no indicator %s", eventid ));
               
         });
             
        return true;
    }   
    
    
    
    @smstate (state = "SETRANGEBAND")
    public boolean st_setRangeband(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            String cmd = tst.getStatecmd().toUpperCase().trim();
            
            AuxChart chart = tsk.auxchart;
            AuxChartDescriptor acd = tsk.ctx.auxcharts.get(tsk.taskid);
        
            VarPool vp = tsk.getVarPool();
            
            
            Platform.runLater(() -> {
                
                YRangeIndicator<Number>  yri;
                XRangeIndicator<Number>  xri;
                
                switch (cmd){
                    
                    case "SETX":
                        xri = AuxChartDescriptor.XRangeIndicatorFactory(tst.getSparam1(), tst.getSparam2(), 
                                tst.getParam1(), tst.getParam2(), 0.0, 0.0);
                        acd.xrangeindicators.put(tst.getSparam1(), xri);
                        chart.getChartPane().getPlugins().add(xri);
                        break;
                    case "CLEARX":
                        xri = acd.xrangeindicators.get(tst.getSparam1());
                        if (xri != null){
                            chart.getChartPane().getPlugins().remove(xri);
                            acd.xrangeindicators.remove(tst.getSparam1());
                        }
                        break;
                    case "LOADX":
                        xri = AuxChartDescriptor.XRangeIndicatorFactory(vp.Pop("RANGEID", String.class), 
                                vp.Pop("RANGELABEL", String.class), 
                                vp.Pop("RANGEMIN", Double.class), vp.Pop("RANGEMAX", Double.class), 0.0, 0.0);
                        acd.xrangeindicators.put(tst.getSparam1(), xri);
                        chart.getChartPane().getPlugins().add(xri);
                        break;
                    
                        
                    case "SETY":
                        yri = AuxChartDescriptor.YRangeIndicatorFactory(tst.getSparam1(), tst.getSparam2(), 
                                tst.getParam1(), tst.getParam2(), null, 0.0, 0.0);
                        acd.yrangeindicators.put(tst.getSparam1(), yri);
                        chart.getChartPane().getPlugins().add(yri);
                        break;
                    case "CLEARY":
                        yri = acd.yrangeindicators.get(tst.getSparam1());
                        if (yri != null){
                            chart.getChartPane().getPlugins().remove(yri);
                            acd.yrangeindicators.remove(tst.getSparam1());
                        }
                        break;
                    case "LOADY":
                        yri = AuxChartDescriptor.YRangeIndicatorFactory(vp.Pop("RANGEID", String.class), 
                                vp.Pop("RANGELABEL", String.class), 
                                vp.Pop("RANGEMIN", Double.class), vp.Pop("RANGEMAX", Double.class), null, 0.0, 0.0);
                        acd.yrangeindicators.put(tst.getSparam1(), yri);
                        chart.getChartPane().getPlugins().add(yri);
                        break;
                      
                        
                    default:
                        break;
                        
                }

                SMTraffic nxt = tsk.goNext(tst.getImediate());
                if (nxt != null){
                    Controller.getInstance().processSignal(nxt);
                }
            });
        }
        else{
            log.info(String.format("Set range band requested"));
        }
       
        return true;
    }
    
    
    
    private void configAxis (NumericAxis axis, Double min, Double max, Double tick, String label){
        
        if (min != null) axis.setLowerBound(min);
        if (max != null) axis.setUpperBound(max);
        if (tick != null && Double.compare(tick, 0.0) != 0) axis.setTickUnit(tick);
        if (label!= null && !label.isBlank()) axis.setLabel(label);
        
    }
    
    @smstate (state = "CONFIGAXIS")
    public boolean st_configAxis(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            String cmd = tst.getStatecmd().toUpperCase().trim();
            
            AuxChart chart = tsk.auxchart;
            AuxChartDescriptor acd = tsk.ctx.auxcharts.get(tsk.taskid);
        
            VarPool vp = tsk.getVarPool();
            
            Platform.runLater(() -> {
                
                switch (cmd){
                    
                    case "SETX":
                        configAxis (chart.getxAxis(), tst.getParam1(), tst.getParam2(), tst.getParam3(), tst.getSparam1());
                        break;
                    case "CLEARX":
                        configAxis (chart.getxAxis(), acd.xmin, acd.xmax, acd.xtick, acd.xlabel);
                        break;
                    case "UPDATEX":
                        configAxis (chart.getxAxis(), tst.getParam1(), tst.getParam2(), tst.getParam3(), tst.getSparam1());
                        acd.xmin = tst.getParam1();
                        acd.xmax = tst.getParam2();
                        acd.xtick = tst.getParam3();
                        acd.xlabel = tst.getSparam1();
                        break;
                    case "LOADX":
                        configAxis (chart.getxAxis(), vp.Pop("AXISMIN", Double.class), vp.Pop("AXISMAX", Double.class), 
                                vp.Pop("AXISTICK", Double.class), vp.Pop("AXISLABEL", String.class));
                        break;
                        
                        
                    case "SETY":
                        configAxis (chart.getyAxis(), tst.getParam1(), tst.getParam2(), tst.getParam3(), tst.getSparam1());
                        break;
                    case "CLEARY":
                        configAxis (chart.getyAxis(), acd.ymin, acd.ymax, acd.ytick, acd.ylabel);
                        break;
                    case "UPDATEY":
                        configAxis (chart.getyAxis(), tst.getParam1(), tst.getParam2(), tst.getParam3(), tst.getSparam1());
                        acd.ymin = tst.getParam1();
                        acd.ymax = tst.getParam2();
                        acd.ytick = tst.getParam3();
                        acd.ylabel = tst.getSparam1();
                        break;
                    case "LOADY":
                        configAxis (chart.getyAxis(), vp.Pop("AXISMIN", Double.class), vp.Pop("AXISMAX", Double.class), 
                                vp.Pop("AXISTICK", Double.class), vp.Pop("AXISLABEL", String.class));
                        break;    
                    
                    case "SETCOMPANION":
                        configAxis (chart.getCompanionYAxis(), tst.getParam1(), tst.getParam2(), tst.getParam3(), tst.getSparam1());
                        break;
                    case "CLEARCOMPANION":
                        configAxis (chart.getCompanionYAxis(), acd.auxmin, acd.auxmax, acd.auxtick, acd.auxlabel);
                        break;
                    case "UPDATECOMPANION":
                        configAxis (chart.getCompanionYAxis(), tst.getParam1(), tst.getParam2(), tst.getParam3(), tst.getSparam1());
                        acd.auxmin = tst.getParam1();
                        acd.auxmax = tst.getParam2();
                        acd.auxtick = tst.getParam3();
                        acd.auxlabel = tst.getSparam1();
                        break;
                    case "LOADCOMPANION":
                        configAxis (chart.getCompanionYAxis(), vp.Pop("AXISMIN", Double.class), vp.Pop("AXISMAX", Double.class), 
                                vp.Pop("AXISTICK", Double.class), vp.Pop("AXISLABEL", String.class));
                        break;
                    
                    default:
                        break;
                        
                }

                SMTraffic nxt = tsk.goNext(tst.getImediate());
                if (nxt != null){
                    Controller.getInstance().processSignal(nxt);
                }
            });
        }
        else{
            log.info(String.format("Set confi axis requested"));
        }
       
        return true;
    }
 
    
    @smstate (state = "DELAYTASK")
    public boolean st_delayTask(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        String substate = pld.getCallerstate();
        
        if (substate != null && substate.contains("TIMEOUT")){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            SMTraffic nxt = tsk.goNext(tst.getImediate());
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
            asvpdevctrl.activateLed("wait", false, false);
            log.info(String.format("Task finished Delaying"));
        }
        else{
            if (pld.vobject instanceof BaseAnaTask){
                BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
                TaskState tst = tsk.getCurrent_taskstate();
                
                Long tmout = tst.getTimeout();
                if (tmout == 0){
                    Platform.runLater(() -> {
                        asvpdevctrl.activateLed("wait", true, true);
                        if(tst.getChronosegs() != null && !tst.getChronosegs().isEmpty()){
                            FXFCountdownTimer cdt = asvpdevctrl.getCDT();
                            cdt.setTimeout_state(tsk.getNext(tst.getTimedout()));
                            cdt.clearCounter();
                            cdt.clearBars();
                            
                            for (TaskStateChronoSegment tscs : tst.getChronosegs()){
                                cdt.pushBar(tscs.lenght, tscs.color, tscs.label, "", "", tsk.getNext(tscs.callback));
                            }
                            
                            cdt.updateBars();
                            cdt.triggerTimer();
                        }
                    });
                }
                else{
                    SMTraffic alarm_config = new SMTraffic(0l, 1l, 0, "DELAYTASK", this.getClass(), pld.setCallerstate("TIMEOUT"));
                    ctrl.setAlarm (-1l, -5, alarm_config, tmout, 0);
                    log.info(String.format("Delaying Task by %d miliseconds", tmout ));
                }
                updateNotify(tsk,tst);
            }
            else{
                log.info(String.format("Delay task" ));
            }
        }
        
       
        return true;
    }
    
    
    
    
    private SMTraffic acknxt;
    @smstate (state = "AUTOACKCALLBACK")
    public boolean st_autoAckCallback(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        log.info(String.format("Auto ack arrived %s", payload.vstring));
        
        if (acknxt != null){
            ctrl.removeAlarm(-4);
            Controller.getInstance().processSignal(acknxt);
            acknxt = null;
        }
        return true;
    }
    
    
    public void updateNotify(BaseAnaTask tsk, TaskState tst){
        
        if (!tst.getNotifymessage().isBlank()){
            Platform.runLater(() -> {
                AuxChartDescriptor cd = ctx.auxcharts.get(tsk.taskid);                   
                cd.overlay.addMessage(tsk.formatMessage(tst.getNotifymessage(), tsk, tst));
            });
        }
        
    } 
    
    @smstate (state = "SETAUTO")
    public boolean st_setAutomation(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
  
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            log.info(String.format("Seting Automation to : %s", tst.getStatecmd()));

            Long tmout = tst.getTimeout();
            if (tmout != 0){
                SMTraffic alarm_config = new SMTraffic(0l, 1l, 0, tst.getTimedout(), this.getClass(),pld );
                ctrl.setAlarm (-1l, -4, alarm_config, tmout, 0);
                acknxt = tsk.goNext(tst.getImediate());
            }
            else{
                SMTraffic nxt = tsk.goNext(tst.getImediate());
                if (nxt != null){
                    Controller.getInstance().processSignal(nxt);
                }
            }
            
            // Update LED Status
            if (tst.getLoadtype() != null && !tst.getLoadtype().isEmpty()){
                Platform.runLater(() -> {
                    asvpdevctrl.activateLed(tst.getLoadtype(), true, true);
                });
            }
            
            String cmd = tst.getStatecmd(); 
            cmd = String.format("%1$-60s", cmd);
            byte[] load = cmd.getBytes();
            serialdevice.sendMessage(load);
            
            updateNotify(tsk,tst);
       
        }
        else{
            String cmd = pld.vstring; 
            cmd = String.format("%1$-60s", cmd);
            byte[] load = cmd.getBytes();
            serialdevice.sendMessage(load);
            
            log.info(String.format("Set Automation - RAW Mode : %s", cmd));
        }
        
        return true;
    }
    
    
    
    
    
    
    
    @smstate (state = "NOTIFYAUX")
    public boolean st_notifyAux(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            
            Platform.runLater(() -> {
                BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
                AuxChartDescriptor cd = ctx.auxcharts.get(tsk.taskid);
                
                TaskState tst = tsk.getCurrent_taskstate();
                // Correct if we're comming from event dispatch
                if (pld.getCaller() != null){
                    tst = (TaskState)pld.getCaller();
                }
                
                tsk.updateNotifications(tst);
                
                SMTraffic nxt = tsk.goNext(tst.getImediate());
                if (nxt != null){
                    Controller.getInstance().processSignal(nxt);
                }
                
            });
            //log.info(String.format("Notifying aux with : %s", tst.getStatecmd()));
            
        }
        else{
            log.info(String.format("Notify Aux"));
        }
       
        return true;
    }
    
    
    @smstate (state = "ENABLECAPTURE")
    public boolean st_enableCapture(SMTraffic smm){
        
        VirnaPayload pld = smm.getPayload();
        
        if (pld.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
            TaskState tst = tsk.getCurrent_taskstate();
            ctx.current_anatask.accept = tst.getFlag();
            
            SMTraffic nxt = tsk.goNext(tst.getImediate());
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
        }
        else{
            log.info(String.format("Notify Aux"));
            ctx.current_anatask.accept = pld.getFlag1();
        }
       
        return true;
    }
    
    
    @smstate (state = "TASKTIMERCALLBACK")
    public boolean st_timerCallback(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        log.info(String.format("Task timeout arrived %s", payload.vstring));
        
        return true;
    }
    
   
    
    
    
    
    
    
    // ================================================ SERIAL INTERFACE =========================================================
    
    private static ArrayList<String> observed_ports = new ArrayList<>();
    private static Boolean available = false;
    
    private static String defaultport = "ttyUSB0";
    private static Integer defaulbaud = 115200;
    
    private SerialDevice serialdevice;
    private static int timeout;
    
    
    public void connect(){
  
//        log.info(String.format("Enter connect ..."));    
        try{
            SerialPort[] comports = SerialPort.getCommPorts();
            for (SerialPort sp : comports){
                String portname = sp.getSystemPortName();
                //log.info(String.format("Connect is checking port : %s", portname));
                if (portname.equals(defaultport)){
                    activatePort(sp);
                    return;
                }
                observed_ports.add(portname);
            }
        }
        catch (Exception ex){
            log.info(String.format("Falha na conexão...."));
        } 
 
        log.info(String.format("No ports available"));
        timeout = Config.getInstance().getKeepalive_period();
//        devstate = DEVICE_STATE.NOPORTS;
        
    }
    
    
    private void activatePort(SerialPort sp){
        
        sp.setComPortParameters(defaulbaud, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        sp.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        //sport.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        
        serialdevice = new SerialDevice(sp, ctrl);
        
        if (serialdevice.isConnected()){
            serialdevice.enablePort();
            serialdevice.addSerialDeviceListener(this);
            observed_ports.add(sp.getSystemPortName());
//            devstate = DEVICE_STATE.PORT_OPEN;
            timeout = 1;
            log.info(String.format("Serial port %s was activated", sp.getSystemPortName()));
        }
        else {
            log.info(String.format("Unable to connect to serial port"));
        }
    }
    
    
    
    public void disconnect(){
        
        String id = serialdevice.getSport().getSystemPortName();
        observed_ports.remove(id);
        log.info(String.format("Serial port %s is disconnecting", id));
        serialdevice.closePort();
    }
    
    
 
    @Override
    public void eventoSerial(SerialDevice.Event e) {
 
        //log.info("Serial event arrived ...");
        
        boolean single = false;
        String dterm;
        String dcmd = "";
        String load = "";
        
        if (e.eventType == SerialDevice.Event.EVENT_TERM){
            dterm = ByteArrayConverter.convertByteBuffer(e.buffer);
            //String dterm = new String (e.buffer, "UTF-8");
            log.info("PicnoTerm : " + dterm);
        }
        else if (e.eventType == SerialDevice.Event.EVENT_CMDR){
    
        }
        else if (e.eventType == SerialDevice.Event.EVENT_BEACON){
            
            
            byte[] arr1 = Arrays.copyOfRange(e.buffer, 22, 30);
            Long data1 = ByteArrayConverter.toLong(arr1, true);
            updateDataProp("setra", data1);
            
            byte[] arr2 = Arrays.copyOfRange(e.buffer, 30, 38);
            Long data2 = ByteArrayConverter.toLong(arr2, true);
    
    
        }
        
        else if (e.eventType == SerialDevice.Event.EVENT_TICK){
             
            byte[] arr1 = Arrays.copyOfRange(e.buffer, 0, 8);
            Long data1 = ByteArrayConverter.toLong(arr1, true);
            if (data1 != -1){
                updateDataProp("setra", data1);
            }
            
         
            if (e.buffer[8] != 0){
                byte[] arr2 = Arrays.copyOfRange(e.buffer, 8, e.getLenght()- 2);
                String cmds = ByteArrayConverter.convertByteBuffer(arr2);
                log.info("Tick Cmds= " + cmds);
                String[] tokens = cmds.split("=");
                String cmd = tokens[0];
                if (ctrl.hasState(cmd)){
//                    log.info("Recognized cmd is " + cmd);
                    ctrl.processSignal(new SMTraffic(0l, 0l, 0, cmd, this.getClass(),
                                   new VirnaPayload().setString(tokens[1])));
                }
                else{
                    log.info("Unrecognized cmd on line ->  " + cmd);
                }
            }
            
        }
        
        else{
            log.info("Undetermined event...");
        
        }
    }
   
    
}



// print count of capturing groups
//            System.out.println("groupCount(): " + mr.groupCount());
//            // print complete matched text
//            System.out.println("group(): " + mr.group());
//            // print start position of matched text
//            System.out.println("start(): " + mr.start());
//            // print end position of matched text
//            System.out.println("end(): " + mr.end());
//            // print 1st captured group
//            System.out.println("group(1): " + mr.group(1));
//            // print 1st captured group's start position
//            System.out.println("start(1): " + mr.start(1));
//            // print 1st captured group's end position
//            System.out.println("end(1): " + mr.end(1));




// 
//    @smstate (state = "RESETMEAS")
//    public boolean st_resetMeas(SMTraffic smm){
//        
//        VirnaPayload payload = smm.getPayload();
//        
//        ctx.current_anatask.resetMeasurements();
//        
//        log.info(String.format("Reseting measurements"));
//        
//        return true;
//    }
//    
//    @smstate (state = "ENDMEAS")
//    public boolean st_endMeas(SMTraffic smm){
//        
//        VirnaPayload payload = smm.getPayload();
//        
//        log.info(String.format("Ending  Measurements"));
//        
//        return true;
//    }
//    
//    
//    @smstate (state = "MONITOR")
//    public boolean st_doMonitor(SMTraffic smm){
//        
//        VirnaPayload payload = smm.getPayload();
//        
//        log.info(String.format("Monitoring"));
//        
//        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "SETAUTO", this.getClass(),
//                                       new VirnaPayload()
//                                        ));
//        
//        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "NOTIFYAUX", this.getClass(),
//                                       new VirnaPayload()
//                                        ));
//        
//        ctrl.processSignal(new SMTraffic(0l, 0l, 0, "ENDMEAS", this.getClass(),
//                                       new VirnaPayload()
//                                        ));
//        
//        return true;
//    }
//    



   
    
//    @smstate (state = "SCALEMAINAXIS")
//    public boolean st_scaleMainAxis(SMTraffic smm){
//        
//        VirnaPayload pld = smm.getPayload();
//        
//        if (pld.vobject instanceof BaseAnaTask){
//            BaseAnaTask tsk = (BaseAnaTask)pld.vobject;
//            TaskState tst = tsk.getCurrent_taskstate();
//            //tsk.auxchart.getxAxis()
//            AuxChart chart = tsk.auxchart;
//            AuxChartDescriptor acd = tsk.ctx.auxcharts.get(tsk.taskid);
//            
//            Platform.runLater(() -> {
//                
//                if ( Double.compare(tst.getParam2(), tst.getParam1()) == 0){
//                    chart.getyAxis().setLowerBound(acd.ymin);
//                    chart.getyAxis().setUpperBound(acd.ymax);
//                }
//                else{
//                    if (tst.getFlag()){
//                        chart.getCompanionYAxis().setLowerBound(tst.getParam2());
//                        chart.getCompanionYAxis().setUpperBound(tst.getParam1()); 
//                    }
//                    else{
//                        chart.getyAxis().setLowerBound(tst.getParam2());
//                        chart.getyAxis().setUpperBound(tst.getParam1());
//                    }
//                }
//
//                SMTraffic nxt = tsk.goNext(tst.getImediate());
//                if (nxt != null){
//                    Controller.getInstance().processSignal(nxt);
//                }
//            });
//        }
//        else{
//            log.info(String.format("Notify Aux"));
//        }
//       
//        return true;
//    }
    