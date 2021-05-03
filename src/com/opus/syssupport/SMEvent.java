/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.syssupport;

import middlestripb.BaseAnaTask;
import middlestripb.TaskState;

/**
 *
 * @author opus
 */
public class SMEvent {
   
    
    private String id = "";
    private Integer priority = 0;

    
    private BaseAnaTask task;
    private TaskState taskstate;
    private SMTraffic message;
    
    
    public SMEvent() {
        
        
    }

    public String getId() {
        return id;
    }

    public SMEvent setId(String id) {
        this.id = id;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public SMEvent setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public BaseAnaTask getTask() {
        return task;
    }

    public SMEvent setTask(BaseAnaTask task) {
        this.task = task;
        return this;
    }

    public TaskState getTaskstate() {
        return taskstate;
    }

    public SMEvent setTaskstate(TaskState taskstate) {
        this.taskstate = taskstate;
        return this;
    }

    public SMTraffic getMessage() {
        return message;
    }

    public SMEvent setMessage(SMTraffic message) {
        this.message = message;
        return this;
    }
    
    
    
    
    
    
}
