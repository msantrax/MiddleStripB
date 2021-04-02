/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

/**
 *
 * @author opus
 */
public class TaskState {
    
    
    private String callstate = "TASKSTATEERROR";
    private String statecmd = "";
    
    private String imediate = "";
    private String succeed = "";
    private String failed = "";
    private String timedout = "";
    private Long timeout = 0l;

    
    public TaskState() {
        
    }

    public TaskState(String callstate, String imediate) {
        this.callstate = callstate;
        this.imediate = imediate;
    }
    
    
    public TaskState(String callstate, String imediate, String cmd) {
        this.callstate = callstate;
        this.imediate = imediate;
        this.statecmd = cmd;
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

    public String getSucceed() {
        return succeed;
    }

    public TaskState setSucceed(String succeed) {
        this.succeed = succeed;
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
    
    
    
}
