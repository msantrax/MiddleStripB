package com.opus.syssupport;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author opus
 */
public interface VirnaServiceProvider {
    
    public void processSignal (SMTraffic signal);
    public void stopService();
    public void startService();
    
    public void addTickListener (TickListener l);
    
    
    
}
