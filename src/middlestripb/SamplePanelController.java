/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author opus
 */
public class SamplePanelController extends AnchorPane implements Initializable{

    private static final Logger LOG = Logger.getLogger(SamplePanelController.class.getName());

    
    
    
    
    
    private Controller appctrl = Controller.getInstance();
    private Context ctx;
    
    private AnchorPane pane;
    private String id;
    
    
    public SamplePanelController() {
    
    }

    
    public AnchorPane getPane() {
        return pane;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    public String getPanelId() {
        return id;
    }

    public void setPanelId(String id) {
        this.id = id;
        this.ctx = Context.getInstance();
    }
    
    
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        
        
        
    }
    
    
    
}
