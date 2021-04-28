/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.util.ArrayList;

/**
 *
 * @author opus
 */
public class TaskState {
    
    private String callstate = "TASKSTATEERROR";
    private String statecmd = "";
    
    protected String notifymessage = "";
    protected String logmessage = "";
    
    private String imediate = "";
    private String failed = "";
    
    private String timedout = "";
    private Long timeout = 0l;
    
    private Long loopcount = 0L;

    private Object load = new Object();
    private String loadtype = "";
    
    private Boolean flag = false;
    
    private Double value1 = 0.0;
    private Double value2 = 0.0;
   
    
    
    
    
    private ArrayList<TaskStateChronoSegment> chronosegs;
    
    
    public TaskState() {
        
    }

    public TaskState(String callstate, String imediate) {
        this.callstate = callstate;
        this.imediate = imediate;
    }
    
    
    public TaskState(String callstate, String cmd, String imediate) {
        this.callstate = callstate;
        this.imediate = imediate;
        this.statecmd = cmd;
    }
    
    public TaskState(String callstate, String cmd, Long timeout, String timedout, String imediate) {
        this.callstate = callstate;
        this.imediate = imediate;
        this.statecmd = cmd;
        this.timeout = timeout;
        this.timedout = timedout;
        
    }
    
    
 
    public String getCallstate() {
        return callstate;
    }
    
    
    public String getStatecmd() {
        return statecmd;
    }
    
    public TaskState setStatecmd(String cmd) {
        this.statecmd = cmd;
        return this;
    }
    
    
    public String getImediate() {
        return imediate;
    }
    
    public TaskState setImediate(String imediate) {
        this.imediate = imediate;
        return this;
    }

   
    public String getFailed() {
        return failed;
    }

    public TaskState setFailed(String failed) {
        this.failed = failed;
        return this;
    }

    public String getTimedout() {
        return timedout;
    }

    public TaskState setTimedout(String timedout) {
        this.timedout = timedout;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public TaskState setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Long getLoopcount() {
        return loopcount;
    }

    public void setLoopcount(Long loopcount) {
        this.loopcount = loopcount;
    }

    public Object getLoad() {
        return load;
    }

    public void setLoad(Object load) {
        this.load = load;
    }

    public String getLoadtype() {
        return loadtype;
    }

    public void setLoadtype(String loadtype) {
        this.loadtype = loadtype;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }

    public ArrayList<TaskStateChronoSegment> getChronosegs() {
        return chronosegs;
    }

    public void setChronosegs(ArrayList<TaskStateChronoSegment> chronosegs) {
        this.chronosegs = chronosegs;
    }

    public String getNotifymessage() {
        return notifymessage;
    }

    public void setNotifymessage(String notifymessage) {
        this.notifymessage = notifymessage;
    }

    public String getLogmessage() {
        return logmessage;
    }

    public void setLogmessage(String logmessage) {
        this.logmessage = logmessage;
    }
    
    
    
}
