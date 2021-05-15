/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;


import Entities.Entity;
import Entities.Point;
import cern.extjfx.chart.XYChartPane;
import com.google.gson.internal.LinkedTreeMap;
import com.opus.fxsupport.PropertyLinkDescriptor;
import com.opus.fxsupport.FXFController;
import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import isothermview.IsothermChart;
import java.io.IOException;


import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.validation.ValidationSupport;



public class FX1Controller extends FXFController implements com.opus.fxsupport.FXFControllerInterface, Initializable {

    private static final Logger LOG = Logger.getLogger(FX1Controller.class.getName());
    
    // Debug flag to prevent load of unwanted graph panes
    public final class SpeedLoad {
        public static final boolean ON = true;
    } 

   
    private String profilepath = ""; 
    
    
    private Controller appctrl = Controller.getInstance();
    private ASVPDevice asvpdevice;
    private Context ctx;
    private MongoLink mongolink;
    
    
    private AuxChart auxchart;
    private XYChartPane auxchartpane;
    
  
    public LinkedHashMap<String, AnchorPane> infopanes;
    public String current_infopane;
    
   
    private Parent preppane;
    private PrepController prepctrl;
 
    
    private ToggleButton station1bt;
    private ToggleButton station2bt;
    
    
    
//    @FXML
//    private ResourceBundle resources;
//
//    @FXML
//    private URL location;
//
//    @FXML
//    private Label lb_profile;

    
    @FXML
    private StackPane rootpane;
    
    
    // Sidebar - Main operations buttons
    @FXML
    private VBox sidebar;
    
    @FXML
    private Label sidebar_btcycle;

    @FXML
    private Label sidebar_btstore;

    @FXML
    private Label sidebar_btreport;

    @FXML
    private Label sidebar_btbroadcast;

    @FXML
    private Label sidebar_btloadfile;

    
    
    // Top Pane -------------------------------------- Sample info
    @FXML
    private HBox toppane;
    
    @FXML
    private VBox tpn_naveg;
    
    @FXML
    private HiddenSidesPane tophspane;
    
    
    // MiddlePane ------------------------------------ App main operations dock
    @FXML
    private SplitPane opsplit;

    @FXML
    private HiddenSidesPane mainhspane;

    @FXML
    private StackPane infopane;

    @FXML
    private HiddenSidesPane auxhspane;

    
    
    // BottomPane ------------------------------------ Secondary operations dock
    @FXML
    private VBox bpn_naveg; // VBox to hold the navigation icons

    @FXML
    private HiddenSidesPane bpn_main; // Placeholder to preparation stuff

    @FXML
    private AnchorPane bpn_sinoptic; // placeholder to sinoptic panel
    
    
    
    
    // Support widgets ------------------------------------ Placeholders to utility widgets & dialogs
    // Snack popup dialog support
    @FXML
    private TextFlow snack;
    
    @FXML
    private Text snacktext;
    
    
    @FXML
    private AnchorPane inputdialog;
    
    
    

    
    
    // ============================================== Pane & Controller init =============================================
    public FX1Controller() {
        this.fxmlLoader = fxmlLoader; 
    }
        
    public FX1Controller(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader; 
    }
    
    
    public FX1Controller(FXMLLoader fxmlLoader, String profilepath) {
        this.fxmlLoader = fxmlLoader; 
        this.profilepath = profilepath;
    }

    private static FX1Controller instance; 
    public static FX1Controller getInstance(){
//        if (instance == null) {instance = new ASVPDevice();}
        return instance;
    }
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        LOG.info(String.format("FX1Controller initializing with profile : %s", profilepath));
        

        ctx = Context.getInstance();
        // Apparently we should load fonts before use them on CSS -> TODO: Is that true ?
        loadFonts();
        
        
        // Init the sidebar main buttons -> TODO: Wouldn't be better to init them on code instead on FXML ?
        sidebar_btcycle.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.REFRESH, "black", 4));
        sidebar_btstore.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.DATABASE, "black", 4));
        sidebar_btreport.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.FILE_PDF_ALT, "black", 4));
        sidebar_btbroadcast.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.SHARE_ALT, "black", 4));
        sidebar_btloadfile.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.ARCHIVE, "black", 4));
  
        // Disable to speed up load time if on development.
//        loadUpperPane();
//        loadBottomPane();
        
        
        infopanes = new LinkedHashMap<>();
        infopanes.put("initbanner", loadBanner());
      
        update();
    
    }
    
    private void loadUpperPane(){
        
        
        FXMLLoader fxmlLoader;
        AnchorPane apane;
        
        // Load Sample Panels
        try {
            URL spnl = getClass().getResource("SamplePanel.fxml");
            
            fxmlLoader = new FXMLLoader(spnl);
            apane = fxmlLoader.load();
            SamplePanelController sample1ctrl = fxmlLoader.<SamplePanelController>getController();
            sample1ctrl.setPane(apane);
            sample1ctrl.setPanelId("sample1");
            ctx.samplepanels.put(sample1ctrl.getPanelId(), sample1ctrl);
            
            
            fxmlLoader = new FXMLLoader(spnl);
            apane = fxmlLoader.load();
            SamplePanelController sample2ctrl = fxmlLoader.<SamplePanelController>getController();
            sample2ctrl.setPane(apane);
            sample2ctrl.setPanelId("sample2");
            ctx.samplepanels.put(sample2ctrl.getPanelId(), sample2ctrl);
            
            
        } catch (IOException ex) { 
            LOG.severe(String.format("Failed to load Sample Panels due %s", ex.getCause().getMessage()));
        }
        
        
        // Load SamplePanel Naveg Buttons  =================================================================
        ToggleGroup tg = new ToggleGroup();
        Tooltip tt;
        
        
        //
        Rectangle station1icon = new Rectangle(32, 60);
        station1icon.setFill(new ImagePattern(
                new Image(getClass().getClassLoader().getResource("middlestripb/station1.png").toExternalForm()), 
                0, 0, 1, 1, true));
        station1bt = new ToggleButton();
        station1bt.setGraphic(station1icon);
        station1bt.setId("fxf-prepbt");
        tt = new Tooltip("Show Analysis on Station 1");
        tt.setShowDelay(Duration.millis(500));
        station1bt.setTooltip(tt);
        tpn_naveg.getChildren().add(station1bt);
        station1bt.setToggleGroup(tg);
        station1bt.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue){
//                LOG.info(String.format("sample1 selected"));
                sidebar.setStyle("-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 3, 0);");
                tophspane.setContent(ctx.samplepanels.get("sample1").getPane());
                
            }
        }));
        
       
        Rectangle station2icon = new Rectangle(32, 60);
        station2icon.setFill(new ImagePattern(
                new Image(getClass().getClassLoader().getResource("middlestripb/station2.png").toExternalForm()), 
                0, 0, 1, 1, true));
        station2bt = new ToggleButton();
        station2bt.setGraphic(station2icon);
        station2bt.setId("fxf-prepbt");
        tt = new Tooltip("Show Analysis on Station 2");
        tt.setShowDelay(Duration.millis(500));
        station2bt.setTooltip(tt);
        tpn_naveg.getChildren().add(station2bt);
        station2bt.setToggleGroup(tg);
        station2bt.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue){
//                LOG.info(String.format("sample2 selected"));
                sidebar.setStyle("-fx-effect: dropshadow(three-pass-box, red, 10, 0, 3, 0);");
                tophspane.setContent(ctx.samplepanels.get("sample2").getPane());
                
            }
        }));
     
    }
    
    
    private void loadBottomPane(){
        
        FXMLLoader fxmlLoader;
        
        // Load Preparation Panels
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("Prep.fxml"));
            preppane = fxmlLoader.load();
            prepctrl = fxmlLoader.<PrepController>getController();
        } catch (IOException ex) { 
            LOG.severe("Failed to load bottom pane");
        }
      
        // Load Sinoptic Panel  ===================================================================================
        Rectangle sinop = new Rectangle(440, 150);
        sinop.setFill(new ImagePattern(
                new Image(getClass().getClassLoader().getResource("middlestripb/sinop.png").toExternalForm()), 
                0, 0, 1, 1, true)); 
        AnchorPane.setTopAnchor(sinop,20.0);
        AnchorPane.setLeftAnchor(sinop,0.0);
        bpn_sinoptic.getChildren().add(sinop);
              
        
        bpn_main.setContent(preppane);
        
        
        
    }
       
    @Override
    public void update(){
      
        
        mongolink = MongoLink.getInstance();
        asvpdevice = ASVPDevice.getInstance();
        appctrl.setFXANController(this);
        ctx.setFXController(this);
        
        
        vs = new ValidationSupport(); 
        validators = new LinkedHashMap<>();
        
        setUIState("FRESHANALYSIS");
        
        
        ASVPDeviceController asvpdevctrl = new ASVPDeviceController(appctrl, this, asvpdevice);
        asvpdevice.setASVPDevController(asvpdevctrl);
 
        
        infopanes.put("asvpdevice", asvpdevctrl);
        infopanes.put("isotherminfo", new IsothermInfoController(this) );
        infopanes.put("pointinfo", new PointInfoController(this) ); 
        
        infopane.getChildren().addAll(infopanes.values());
        
        
        loadMainCharts();
        
        instance = this;
        
        asvpdevice.connect();
 
    }
    
    
    
    
    
    
    public void loadMainCharts(){
    
        setAuxchart(new AuxChart());
        auxchartpane = getAuxchart().createCernChart();
        auxchartpane.getStylesheets().add(getClass().getClassLoader().getResource("middlestripb/auxchart.css").toExternalForm());
        
               
        ctx.switchTask ("roottask");
//        ctx.journals.put(ctx.current_anatask, new JournalSideNode(Side.TOP, getAuxpane()));

        
        Platform.runLater(() -> {
            
            showInfoPane("initbanner");
            
//            mainchartpane.setMinWidth(mainhspane.getWidth());
//            mainchartpane.setMinHeight(mainhspane.getHeight());            
            
            mainhspane.setContent(ctx.current_anatask.getMainPane());
            
            ChartsToolbar chtb = new ChartsToolbar(Side.LEFT, mainhspane);
            mainhspane.setLeft(chtb);
                      
            
            auxchartpane.setMinWidth(getAuxhspane().getWidth());
            auxchartpane.setMinHeight(getAuxhspane().getHeight());
            getAuxhspane().setContent(auxchartpane);
            
//            getAuxpane().setTop(ctx.journals.get(ctx.current_anatask));
            
            if (prepctrl != null) {
                prepctrl.loadCharts(bpn_main, bpn_naveg);
                station1bt.setSelected(true);
            }
   
            
            // Scan Controllers for app machine states
            appctrl.loadStates(AuxChart.class, this.auxchart);
            appctrl.loadStates(PrepController.class, this.prepctrl);
            
         
            
        });
    }
    
    
    // ========================================= Visualization & Plumbing Support ============================================== 
    public void showMainChart(Node content){
      
        mainhspane.setContent(content);
    }
    
    
    public void clearInfoPanes() {
    
        for (AnchorPane pane : infopanes.values()){
            pane.setVisible(false);
        }
    }

    public void showInfoPane(String id){
        
        clearInfoPanes();
        AnchorPane ap = infopanes.get(id);
        if (ap != null){
            ap.setVisible(true);
            current_infopane = id;
        }
    }

    
    // ========================================= Auxiliary init functions ==================================================== 
    private AnchorPane loadBanner(){
        
        // Load InitBanner
        Image inibanner = new Image(getClass().getClassLoader().getResource("middlestripb/asvpa.png").toExternalForm());
        Rectangle initbanner = new Rectangle(284, 420);
        initbanner.setFill(new ImagePattern(inibanner, 0, 0, 1, 1, true));
        AnchorPane bannerpane = new AnchorPane(initbanner);
        AnchorPane.setLeftAnchor(initbanner, 40.0) ;
        AnchorPane.setTopAnchor(initbanner, 30.0);
        bannerpane.getStyleClass().add("info-pane");
        
        return bannerpane;
    }
    
    
    
    private void loadFonts(){
        
        Font exobold = Font.loadFont(getClass().getClassLoader().getResource("middlestripb/Exo-Bold.ttf").toExternalForm(), 10);
        Font exo = Font.loadFont(getClass().getClassLoader().getResource("middlestripb/Exo-Regular.ttf").toExternalForm(), 10);
        Font exothin = Font.loadFont(getClass().getClassLoader().getResource("middlestripb/Exo-Thin.ttf").toExternalForm(), 10);
        
        Font robotobold = Font.loadFont(getClass().getClassLoader().getResource("middlestripb/Roboto-Bold.ttf").toExternalForm(), 10);
        Font roboto = Font.loadFont(getClass().getClassLoader().getResource("middlestripb/Roboto-Regular.ttf").toExternalForm(), 10);
        
    }
    
    
    
    
    
    
    // ============================================ SUPPORT WIDGETS =======================================================
    
    
    @smstate (state = "SHOWSNACK")
    public boolean st_showSnack(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        
        if (payload.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)payload.vobject;
            TaskState tst = tsk.getCurrent_taskstate();

            Long tmout = tst.getTimeout();
            
            SMTraffic nxt = tsk.goNext(tst.getImediate());
            if (nxt != null){
                Controller.getInstance().processSignal(nxt);
            }
          
            showSnack(tmout, tst.notifymessage); 
        }
        else{
           showSnack(payload.long1, payload.vstring);  
        }
        
        return true;
    }
    
    
    public void showSnack(Long period, String message) {
        
        Long showperiod = (period == null || period == 0l) ? 2500 : period;
        
        snacktext.setText(message);
        LOG.info(String.format("SNACK is showing : %s", message));
        
        snack.setVisible(true);
        
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(showperiod),
            ae -> snack.setVisible(false)    
        ));
        timeline.play();
        
    }
    
    
    // ================================================= DIALOG SERVICES =========================================================
    
    public Parent currentdlgpane;
    public AnchorPane getInputDialog() { return inputdialog;}
    
    
    public void hideInputDialog(){    
        inputdialog.getChildren().remove(currentdlgpane);
        inputdialog.setVisible(false);
    }
    
    @smstate (state = "SHOWINPUTDIALOG")
    public boolean st_showInputDialog(SMTraffic smm){

        VirnaPayload payload = smm.getPayload();
        TaskState tst;
        
        if (payload.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)payload.vobject;
            if (tsk.getCurrent_taskstate() == null){
                tst = (TaskState) payload.getCaller();
            }
            else{
                tst = tsk.getCurrent_taskstate();
            }
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    
                    SimpleStringProperty result = showInputDialog(tst.getSparam1(), tst.getSparam2(), tst.getFlag());
                    if (result != null){
                        result.addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue <? extends String> prop, String ov, String nv) {
                                LOG.info(String.format("Valor : %s ", nv));
                                hideInputDialog();
                                if (nv != null && !nv.isEmpty() && !nv.equals("cancel")){
                                    LOG.info(String.format("Validated."));
                                    SMTraffic nxt = tsk.goNext(tst.getImediate());
                                    if (nxt != null){
                                        TaskState tskst = tsk.current_taskstate;
                                        tskst.setSparam1(nv);
                                        Controller.getInstance().processSignal(nxt);
                                    }    
                                }
                                else{
                                    LOG.info(String.format("Canceled"));
                                    SMTraffic nxt = tsk.goNext(tst.getFailed());
                                    if (nxt != null){
                                        Controller.getInstance().processSignal(nxt);
                                    } 
                                }
                            }
                        });
                    } 
                }
            });    
            
//            
//            SMTraffic nxt = tsk.goNext(tst.getImediate());
//            if (nxt != null){
//                Controller.getInstance().processSignal(nxt);
//            }
          
           
        }
        else{
           Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    SimpleStringProperty result = showInputDialog(payload.vstring, payload.getServicestatus(), false);
                    if (result != null){
                        result.addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue <? extends String> prop, String ov, String nv) {
                                LOG.info(String.format("Valor : %s ", nv));
                                hideInputDialog();
                                if (nv != null && !nv.isEmpty() && !nv.equals("cancel")){
                                    LOG.info(String.format("Validated."));

                                }
                            }
                        });
                    } 
                }
            });    
        }
   
        return true;
    }
    
    
    
    public SimpleStringProperty showInputDialog(String header, String value, boolean enableok){
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXFInputDialog.fxml"));
            currentdlgpane = fxmlLoader.load();
            FXFInputDialogController dlgc = fxmlLoader.<FXFInputDialogController>getController();
            
            dlgc.setHeader(header);
            dlgc.setDefvalue(value);
            dlgc.enableOKButton(enableok);
            
            inputdialog.getChildren().add(currentdlgpane);
            //inputdialog.setMaxSize(200.0, 200.0);
            inputdialog.setVisible(true);
            return dlgc.result;
      
        } catch (IOException ex) {
            LOG.severe("Severe !!!");
            //Logger.getLogger(FXFWindowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        return null;
    }
    
    
    @smstate (state = "SHOWQUESTIONDIALOG")
    public boolean st_showQuestionDialog(SMTraffic smm){

        VirnaPayload payload = smm.getPayload();
        TaskState tst;
        
        if (payload.vobject instanceof BaseAnaTask){
            BaseAnaTask tsk = (BaseAnaTask)payload.vobject;
            if (tsk.getCurrent_taskstate() == null){
                tst = (TaskState) payload.getCaller();
            }
            else{
                tst = tsk.getCurrent_taskstate();
            }
   
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    
                    DialogMessageBuilder dmb = new DialogMessageBuilder();
                    
                    LinkedTreeMap ltm = (LinkedTreeMap) tst.getLoad();
                    ltm.forEach((k,v) -> {
//                        System.out.println(String.format("map : %s = %s", k, v));
                        if (k.equals("cancel") || k.equals("aux")){
                            dmb.enableButton((String)k, (String)v, (String)k, false);
                        }
                        else{
                            dmb.add((String) k, (String)v);
                        }
                    });
 
                    SimpleStringProperty result = showQuestionDialog(tst.getSparam1(), dmb);
                    
                    if (result != null){
                        result.addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue <? extends String> prop, String ov, String nv) {
                                LOG.info(String.format("Question dlg voltou  : %s ", nv));
                                hideInputDialog();
                                SMTraffic nxt;
                                if (nv != null && !nv.isEmpty() && nv.equals("ok")){
                                    nxt = tsk.goNext(tst.getImediate());  
                                }
                                else if (nv != null && !nv.isEmpty() && nv.equals("aux")){
                                    nxt = tsk.goNext(tst.getTimedout());   
                                }
                                else{
                                    nxt = tsk.goNext(tst.getFailed());
                                }
                                if (nxt != null){
                                    Controller.getInstance().processSignal(nxt);
                                }
                            }
                        });
                    } 
                }
            });    
            
        }
        else{
           
        }
   
        return true;
    }
    
    
    
    
    public SimpleStringProperty showQuestionDialog(String header, DialogMessageBuilder dmb){
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXFQuestionDialog.fxml"));
            currentdlgpane = fxmlLoader.load();
            FXFQuestionDialogController dlgc = fxmlLoader.<FXFQuestionDialogController>getController();
            
            dlgc.setHeader(header);
            dlgc.setStatus(dmb);
            AnchorPane ap = (AnchorPane)currentdlgpane;
            ap.setPrefHeight(dlgc.getDlg_height());
            ap.setPrefWidth(inputdialog.getWidth());
//            inputdialog.setPrefWidth(ap.getWidth());
            
            inputdialog.getChildren().add(currentdlgpane);
            inputdialog.setVisible(true);
            //inputdialog.setPrefHeight(400);
            return dlgc.result;
      
        } catch (IOException ex) {
            LOG.severe("Severe !!!");
        }
 
        return null;
    }
    
    
    
    
    public SimpleStringProperty showListDialog(String header, FXFListDialogBuilder dmb){
      
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXFListDialog.fxml"));
            currentdlgpane = fxmlLoader.load();
            FXFListDialogController dlgc = fxmlLoader.<FXFListDialogController>getController();
            
            dlgc.setHeader(header);
            dlgc.setStatus(dmb);
            AnchorPane ap = (AnchorPane)currentdlgpane;
            ap.setPrefHeight(dlgc.getDlg_height());
            
            inputdialog.getChildren().add(currentdlgpane);
            inputdialog.setVisible(true);
            //inputdialog.setPrefHeight(400);
            return dlgc.result;
      
        } catch (IOException ex) {
            LOG.severe("Severe !!!");
            //Logger.getLogger(FXFWindowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        return null;
    }
    
    
    
    
    
    // Charts Toolbar =====================================================================================================
    class ChartsToolbar extends VBox {
        
        public ChartsToolbar( final Side side, final HiddenSidesPane pane) {
            
            Button bt1;
            Tooltip tt;
            
            setAlignment(Pos.TOP_CENTER);
            setPrefSize(35, 200);
            setSpacing(20);
            
            setStyle("-fx-background-color: rgba(255,255,255,1);"
                    + "-fx-effect :  dropshadow(three-pass-box, black, 8.0, 0, 1.0, 1.0);"
                    + "-fx-background-radius: 6;" 
                    + "-fx-border-radius: 6;"
                    + "-fx-padding: 60 0 0 0;"
                    + "-fx-border-insets: 15 0 5 0;" 
                    + "-fx-background-insets: 15 0 5 0;");
            
            Rectangle r = new Rectangle(20, 30);
            r.setStyle( "-fx-background-color: transparent;"
                    +   "-fx-fill: transparent;" );
            this.getChildren().add(r);
            
            
            bt1 = new Button();
            bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.REFRESH, "black", 2));
            bt1.setId("fxf-chartbt");
            tt = new Tooltip("Tooltip buttom 1");
            tt.setShowDelay(Duration.millis(500));
            bt1.setTooltip(tt);
            this.getChildren().add(bt1);
            bt1.setOnMousePressed(new EventHandler<MouseEvent>(){
                @Override 
                public void handle(MouseEvent event) {
                    LOG.info(String.format("Chart Toolbar buttom 1 pressed"));
 
                }  
            });   
            
            
            bt1 = new Button();
            bt1.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.ANCHOR, "black", 2));
            bt1.setId("fxf-chartbt");
            tt = new Tooltip("Tooltip buttom 2");
            tt.setShowDelay(Duration.millis(500));
            bt1.setTooltip(tt);
            this.getChildren().add(bt1);
       
            
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (pane.getPinnedSide() != null) {
                        setStyle("-fx-background-color: rgba(255,255,255,1);"
                                + "-fx-effect :  dropshadow(three-pass-box, black, 8.0, 0, 1.0, 1.0);"
                                + "-fx-background-radius: 6;" 
                                + "-fx-border-radius: 6;"
                                + "-fx-padding: 60 0 0 0;"
                                + "-fx-border-insets: 15 0 5 0;" 
                                + "-fx-background-insets: 15 0 5 0;");
                        pane.setPinnedSide(null);
                    } else {
                        setStyle("-fx-background-color: rgba(255,255,255,1);"
                                + "-fx-effect :  dropshadow(three-pass-box, red, 8.0, 0, 1.0, 1.0);"
                                + "-fx-background-radius: 6;" 
                                + "-fx-border-radius: 6;"
                                + "-fx-padding: 60 0 0 0;"
                                + "-fx-border-insets: 15 0 5 0;" 
                                + "-fx-background-insets: 15 0 5 0;");
                        pane.setPinnedSide(side);
                    }
                }
            });
            
        }
      
    }
    
    
    // =====================================================================================================================
    
    @Override
    public void sendSignal (PropertyLinkDescriptor pld, String sigtype){
        
        appctrl.processSignal(new SMTraffic(0l, 0l, 0, pld.getCallstate(), this.getClass(),
                                   new VirnaPayload().setObject(pld)));
        
    }

    
    public void setUIState(String verb){
        
        
        switch (verb){
            case "FRESH_ANALISYS":
                sidebar_btcycle.setDisable(false);
                sidebar_btstore.setDisable(true);
                sidebar_btreport.setDisable(false);
                sidebar_btbroadcast.setDisable(true);
                sidebar_btloadfile.setDisable(false);
                //blainedevice.enableRun(true);
                break;
            
                
            case "ENABLEALL":
                sidebar_btcycle.setDisable(false);
                sidebar_btstore.setDisable(false);
                sidebar_btreport.setDisable(false);
                sidebar_btbroadcast.setDisable(false);
                sidebar_btloadfile.setDisable(false);
                break;    
                
            default:
                sidebar_btcycle.setDisable(false);
                sidebar_btstore.setDisable(true);
                sidebar_btreport.setDisable(false);
                sidebar_btbroadcast.setDisable(true);
                sidebar_btloadfile.setDisable(false);
                //blainedevice.enableRun(true);
                break;
        }
        
    }
    
    
    // ========================================= DISPATCH SECTION =======================================================
    
    @FXML
    void btcycle_action(MouseEvent event) {

        if (event.isControlDown()){
            setUIState("ENABLEALL");
        }
        else if (event.isShiftDown()){
            appctrl.processSignal(new SMTraffic(0l, 0l, 0, "EXIT", this.getClass(),
                                   new VirnaPayload()));
        }
        else{
            ctx.switchTask("roottask");
            
//            showInfoPane("asvpdevice");
//            ctx.switchTask("checkp0task");
        }
        
       
    }

    @FXML
    void btexport_action(MouseEvent event) {
        
//        MiddleStripB.mongolink.loadAsJson(UID);
//        MiddleStripB.mongolink.loadAsBean(UID);
//        appctrl.processSignal (new SMTraffic(0l, 0l, 0, "IMPORTISOTHERM", this.getClass(),
//                        new VirnaPayload().setString("/Bascon/ASVP/Quantawin/sample_a (Isotherm).txt")));
//        appctrl.processSignal (new SMTraffic(0l, 0l, 0, "UPDATEISOTHERMCHART", this.getClass(),
//                        new VirnaPayload()));

//        asvpdevice.connect();


//        showSnack(null, "Teste de operação do Snack com texto relativamente curto"); 
//        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "SHOWSNACK", this.getClass(),
//                                    new VirnaPayload()
//                                        .setString("Teste de operação do Snack com texto relativamente curto")
//                                        .setLong1(10000l)
//                             ));

        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "SHOWINPUTDIALOG", this.getClass(),
                                    new VirnaPayload()
                                        .setString("Teste do Input Dialog")
                                        .setAuxiliar("12345")
                             ));
        
    }

    @FXML
    void btreport_action(MouseEvent event) {
        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "EXIT", this.getClass(),
                                   new VirnaPayload()));
        
    }

    @FXML
    void btstore_action(MouseEvent event) {

        if (event.isControlDown()){
            appctrl.processSignal(new SMTraffic(0l, 0l, 0, "SETAUTO", this.getClass(),
                                       new VirnaPayload()
                                               .setString("SETVALVES=PUMP")
                                               .setFlag1(Boolean.TRUE)));
            
            appctrl.processSignal(new SMTraffic(0l, 0l, 0, "SETAUTO", this.getClass(),
                                       new VirnaPayload()
                                               .setString("SETVALVES=BUILDP")
                                               .setFlag1(Boolean.TRUE)));
            
        }
        else{
            for (int i = 0; i < 10; i++) {
                appctrl.processSignal(new SMTraffic(0l, 0l, 0, "SETAUTO", this.getClass(),
                                       new VirnaPayload()
                                               .setString(String.format("SETVALVES=%d", i))
                                               .setFlag1(Boolean.TRUE)));
            }
        }
   
//        if (event.isControlDown()){
//            appctrl.processSignal(new SMTraffic(0l, 0l, 0, "AUXDOMESSAGES", this.getClass(),
//                                       new VirnaPayload()
//                                               .setServicestatus("")
//                                               .setString("isotimedomain")
//                                               .setFlag1(Boolean.FALSE)));
//        }
//        else{
//            appctrl.processSignal(new SMTraffic(0l, 0l, 0, "AUXDOMESSAGES", this.getClass(),
//                                       new VirnaPayload()
//                                               .setServicestatus("Mes:"+ System.currentTimeMillis())
//                                               .setString("isotimedomain")
//                                               .setFlag1(Boolean.TRUE)));
//        }
        
//        if (event.isControlDown()){
//            appctrl.processSignal(new SMTraffic(0l, 0l, 0, "AUXCLEARINDICATOR", this.getClass(),
//                                       new VirnaPayload()
//                                               .setServicestatus("showindicator1")
//                                               .setString("isotimedomain")
//                                               .setObjectType("XValue")));
//        }
//        else{
//            XValueIndicator<Number> indicator = AuxChartDescriptor.XValIndicatorFactory ("showindicator1", "Test Event", 10000.0, null);
////           ValueIndicator<Number> indicator = AuxChartDescriptor.XValIndicatorFactory ("showindicator1", "Test Event", 10000.0, null);
//            appctrl.processSignal(new SMTraffic(0l, 0l, 0, "AUXSHOWINDICATOR", this.getClass(),
//                                       new VirnaPayload().setObject(indicator)
//                                               .setLong1(2000L)
//                                               .setServicestatus("showindicator1")
//                                               .setString("isotimedomain")
//                                               .setObjectType("XValue")));
//        }
        
                
//        MiddleStripB.mongolink.savetoJsonFile();
//        isothermchart.test1();

    }

    
    @FXML
    void btloadfile_action(MouseEvent event) {
        
        ViewisoTask vit = (ViewisoTask)ctx.anatasks.get("viewisotask");
        
        if ( vit == null){
            vit = new ViewisoTask(null, ctx);
            ctx.anatasks.put("viewisotask", vit );
            appctrl.loadStates(ViewisoTask.class, vit);
            vit.prepareGo();
        }
        
        vit.Restart();
   
        
        
//        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "LOADISO", this.getClass(),
//                                   new VirnaPayload().setCallerstate("ASKUSER")));
        
    }
   
    
    @Override
    public void clearCanvas(){ 
//        pnl_user.setVisible(false);
//        checklist1.setOpacity(0.0);
    }
    
    @FXML
    void canvas_clicked(MouseEvent event) {
        //LOG.info("Canvas clicked ...");
        //clearCanvas();
    }
    
    
    @FXML
    void user_action(MouseEvent event) {

    }
  
    
    public Scene getScene() { return getScene();}

    
    private static final String UID = "FX1";
    @Override
    public String getUID() { return UID;}

    public AuxChart getAuxchart() {
        return auxchart;
    }

    public void setAuxchart(AuxChart auxchart) {
        this.auxchart = auxchart;
    }

    public HiddenSidesPane getAuxhspane() {
        return auxhspane;
    }

    


    
}











//        FX001_campo1.setTooltip(new Tooltip("Mensagem de teste numero 2"));
//        validators.put(FX001_campo1, new NumberValidator()
//                .setRangeWindows(10.0, 10.0, 15.0)
//        );
//    
//        validators.get(FX001_campo1).initTooltip(FX001_campo1.getTooltip());
//        
//        vs.registerValidator(FX001_campo1, new Validator<String>(){
//            @Override
//            public ValidationResult apply( Control control, String value ){
//                NumberValidator validator = (NumberValidator)validators.get(control);
//                validator.getResult(value);
// 
//                return ValidationResult.fromMessageIf(control, 
//                        validator.getMessage(), 
//                        validator.isWarning() ? Severity.WARNING : Severity.ERROR, 
//                        validator.isFailed() ? true : false);
//            };
//        });
        

//        FX002_campo2.setTooltip(new Tooltip("Mensagem de teste numero 2"));
//        validators.put(FX002_campo2, new NumberValidator()
//                .setRangeWindows(50.0, 10.0, 15.0)
//        );
//    
//        validators.get(FX002_campo2).initTooltip(FX002_campo2.getTooltip());
//        
//        vs.registerValidator(FX002_campo2, new Validator<String>(){
//            @Override
//            public ValidationResult apply( Control control, String value ){
//                NumberValidator validator = (NumberValidator)validators.get(control);
//                validator.getResult(value);
// 
//                return ValidationResult.fromMessageIf(control, 
//                        validator.getMessage(), 
//                        validator.isWarning() ? Severity.WARNING : Severity.ERROR, 
//                        validator.isFailed() ? true : false);
//            };
//        });



        //Color randomColor = new Color( Math.random(), Math.random(), Math.random(), 1);
        
        //Glyph glyph = new Glyph("FontAwesome", FontAwesome.Glyph.REFRESH);
        //Glyph graphic = Glyph.create( "FontAwesome|" + glyph.getText()).sizeFactor(5).color(Color.LIGHTBLUE).useGradientEffect();


//final ObservableList<String> calibs = FXCollections.observableArrayList();
//        for (int i = 0; i <= 15; i++) {
//            calibs.add("Padrão HF-207 calib:" + i);
//        }
//        
//        cb_calibration.setItems(calibs);
//        cb_calibration.setValue("Escolha uma calibração");


               
                
//                System.out.println("============================================");
//                System.out.println("Change: " + change);
//                System.out.println("Added sublist " + change.getAddedSubList());
//                System.out.println("Removed sublist " + change.getRemoved());
//                System.out.println("From " + change.getFrom());
//                System.out.println("To " + change.getTo());
//                System.out.println("Next " + change.next());
//                System.out.println("Added Size " + change.getAddedSize());
//                System.out.println("Removed Size " + change.getRemovedSize());
//                System.out.println("List " + change.getList());
//                System.out.println("Added " + change.wasAdded() + " Permutated " + change.wasPermutated() + " Removed " + change.wasRemoved() + " Replaced "
//                        + change.wasReplaced() + " Updated " + change.wasUpdated());
//                System.out.println("============================================");
                
                
                
        
//        TextInputControl tic = (TextInputControl)field;
//        sp_tic.addListener((ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
//            LOG.info(String.format("Field @ %d changed to %s", tic.hashCode(),  newVal));
//        });
        
       

    
    
//    public void loadProfile(){
//        
//        profile = appctrl.getProfile();
//        lb_profile.setText(profile.getLabel());
//        
//    }
//    
//    public Profile getProfile() { return profile;}
//    
//    
//    public void updateUserPanel(){
//        
//        cb_profile.setItems(FXCollections.observableArrayList(appctrl.getProfile_list()));
//        cb_profile.setValue(profile.getLabel());
////        if (PicnoUtils.user.isMay_changeprofile()){
////            cb_profile.setDisable(false);
////        }
////        else{
////            cb_profile.setDisable(true);
////        }
//        lb_avatar.setText("User");
//        String log1 = String.format(PicnoUtils.timestamp_format, appctrl.getLogTime());
//        lb_logged.setText(log1);
//        
//        String savatar = "";
//        if (!savatar.isEmpty()){
//            File file = new File(Config.getInstance().getAux_dir()+savatar);
//            Image image = new Image(file.toURI().toString());
//            avatar.setImage(image);          
//            ImageView iv = new ImageView(image);
//            iv.setFitHeight(50);
//            iv.setFitWidth(50);
//            lb_user.setGraphic(iv);
//        }
//        else{
//            lb_user.setGraphic(Glyph.create( "FontAwesome|" + new Glyph("FontAwesome", FontAwesome.Glyph.USER).getText())
//                .sizeFactor(3).color(Color.WHITE)
//            );
//        }
//    }
//    



        
//        sidebar_btcycle.setGraphic(Glyph.create( "FontAwesome|" + new Glyph("FontAwesome", FontAwesome.Glyph.REFRESH).getText())
//                .sizeFactor(5).color(Color.LIGHTBLUE)
//                .useGradientEffect()
//        );



//
//        // Create the CheckListView with the data 
////        checklist1.setItems(strings);
//        //initTimeList();
//        
//        Random rand = new Random();
//        for (int i = 0; i <= 10; i++) {
//            time_entries.add(String.format("%5.2f", 125.0 + (rand.nextDouble()-0.5)*2));
//        }
//        
////        Platform.runLater(new Runnable() {
////            @Override
////            public void run() {
//                checklist1.setItems(time_entries);
////            }
////        });
////        



//                LOG.info(String.format("Loaded = %d", mongolink.getLoaded_descriptors().size()));



//try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("JournalSideNode.fxml"));
//                AnchorPane journalpane = fxmlLoader.load();
////                FXFInputDialogController dlgc = fxmlLoader.<FXFInputDialogController>getController();
//                logsidenode.getChildren().add(journalpane);
//                
//                journalpane("çkjklfdjglkfdjgfjdlgjfd \n fjdklfjlkdsjf \n erpoierf095843857");
//
//            } catch (IOException ex) {
//                LOG.severe("Failed to load Journal fxml !!!");
//            }