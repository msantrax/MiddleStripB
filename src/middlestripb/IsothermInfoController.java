/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author opus
 */
public class IsothermInfoController extends AnchorPane implements Initializable{

    private static final Logger LOG = Logger.getLogger(IsothermInfoController.class.getName());

    
     @FXML
    private AnchorPane isoinfo;

    @FXML
    private TextField init_ts;

    @FXML
    private TextField strategy;

    @FXML
    private CheckBox concluded;

    
    
    
    private FX1Controller anct;
    
    public IsothermInfoController(FX1Controller anct) {
    
        this.anct = anct;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IsothermInfo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    
    }

    
    
    
    
    
    
    
    
    
    
    
    
    // Application controller link 
    private Controller ctrl;
    public void setAppController (Controller ctrl){
        this.ctrl = ctrl;
//        cdt.setCtrl((com.opus.syssupport.VirnaServiceProvider)ctrl);
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
    }
    
    
    
    
    
    
}
