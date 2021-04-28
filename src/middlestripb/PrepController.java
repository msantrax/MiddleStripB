/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import cern.extjfx.chart.XYChartPane;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;

/**
 *
 * @author opus
 */
public class PrepController extends AnchorPane implements Initializable {

    private static final Logger LOG = Logger.getLogger(PrepController.class.getName());

    
    @FXML
    private AnchorPane prep_pane;

    @FXML
    private ChoiceBox<String> profile;

    @FXML
    private TextField sid;

    @FXML
    private TextField massinit;

    @FXML
    private Button scale;

    @FXML
    private TextField operator;
    
    
    
    private Controller appctrl = Controller.getInstance();
    private Context ctx;
    
    
    private XYChartPane chartpane;
    private LinkedHashMap<String, PrepChart> charts;
    private PrepChart current_prepchart;
    
    
    private HiddenSidesPane parentpane;
    private VBox bpn_naveg;
    
    
    
    
    
    public PrepController() {
        charts = new LinkedHashMap<>();
    }
    
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
       
    }
    
    
    public void loadCharts(HiddenSidesPane parentpane, VBox bpn_naveg){
        
        
        this.bpn_naveg = bpn_naveg;
        
      
        Tooltip tt;
        PrepChart pchart;
        
        String chartcss = getClass().getClassLoader().getResource("middlestripb/prepchart.css").toExternalForm();
      
        profile.getItems().addAll("Prep. Default", "Empty Slot", "Organic Sample", "Moisture Ore");
        profile.setValue("Prep. Default");
        
        pchart = new PrepChart(this);
        chartpane = pchart.createCernChart(680, 227);
        chartpane.getStylesheets().add(chartcss);
        pchart.refreshChart();
        AnchorPane.setTopAnchor(chartpane, 0.0);
        AnchorPane.setLeftAnchor(chartpane, 0.0);
        
        
     
        PrepToolbar preptb = new PrepToolbar(Side.BOTTOM, parentpane, this);
        parentpane.setBottom(preptb);
     
        ToggleGroup tg = new ToggleGroup();
        
        //
        Rectangle prep1icon = new Rectangle(25, 25);
        prep1icon.setFill(new ImagePattern(
                new Image(getClass().getClassLoader().getResource("middlestripb/prep1.png").toExternalForm()), 
                0, 0, 1, 1, true));
        ToggleButton prep1bt = new ToggleButton();
        prep1bt.setGraphic(prep1icon);
        prep1bt.setId("fxf-prepbt");
        tt = new Tooltip("Show Preparation on Slot 1");
        tt.setShowDelay(Duration.millis(500));
        prep1bt.setTooltip(tt);
        bpn_naveg.getChildren().add(prep1bt);
        prep1bt.setToggleGroup(tg);
        prep1bt.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue){
                LOG.info(String.format("prep1 selected"));
//                prep_pane.getChildren().clear();
               //prep_pane.getChildren().add(chartpane);
            }
        }));
        
       
        //
        Rectangle prep2icon = new Rectangle(25, 25);
        prep2icon.setFill(new ImagePattern(
                new Image(getClass().getClassLoader().getResource("middlestripb/prep2.png").toExternalForm()), 
                0, 0, 1, 1, true));
        ToggleButton prep2bt = new ToggleButton();
        prep2bt.setGraphic(prep2icon);
        prep2bt.setId("fxf-prepbt");
        tt = new Tooltip("Show Preparation on Slot 2");
        tt.setShowDelay(Duration.millis(500));
        prep2bt.setTooltip(tt);
        bpn_naveg.getChildren().add(prep2bt);
        prep2bt.setToggleGroup(tg);
        prep2bt.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue){
                LOG.info(String.format("prep2 selected"));
            }
        }));
        
        
        //
        Rectangle dewaricon = new Rectangle(25, 25);
        dewaricon.setFill(new ImagePattern(
                new Image(getClass().getClassLoader().getResource("middlestripb/dewar.png").toExternalForm()), 
                0, 0, 1, 1, true));
        ToggleButton dewarbt = new ToggleButton();
        dewarbt.setGraphic(dewaricon);
        dewarbt.setId("fxf-prepbt");
        tt = new Tooltip("Show Dewar (N2) status");
        tt.setShowDelay(Duration.millis(500));
        dewarbt.setTooltip(tt);
        bpn_naveg.getChildren().add(dewarbt);
        dewarbt.setToggleGroup(tg);
        dewarbt.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue){
                LOG.info(String.format("dewar selected"));
            }
        }));
        
        prep1bt.setSelected(true);
        prep_pane.getChildren().add(chartpane);
        
    }
    
  
    
}
