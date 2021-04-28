/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.syssupport;

/**
 *
 * @author opus
 */
public interface SignalListener {
    
    Long getContext();
    
    Long getUID();
    
    void processSignal (SMTraffic signal);
    
    
    
}
