/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import Entities.Point;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class DoseInfoCTX {

    private static final Logger LOG = Logger.getLogger(DoseInfoCTX.class.getName());

    
    private Point point;
   
    
    public Double pressure;
    public ZoneId zoneid;
    public LocalDateTime init_ts;
    public LocalDateTime end_ts;
    public Duration duration;
    
    
    
    public DoseInfoCTX(Double pressure) {
        this.pressure = pressure;
        zoneid = ZoneId.systemDefault();
    }
   
    
    public DoseInfoCTX setTempo (Long init, Long end){
        
        init_ts = LocalDateTime.ofInstant(Instant.ofEpochMilli(init), ZoneId.systemDefault());
        end_ts = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());
        duration = Duration.between(init_ts, end_ts);
        
        return this;
    }

    public Point getPoint() {
        return point;
    }

    public DoseInfoCTX setPoint(Point point) {
        this.point = point;
        return this;
    }

    
    
    
}
