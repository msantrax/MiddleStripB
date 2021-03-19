/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;


import Entities.Entity;
import Entities.Point;
import cern.extjfx.chart.XYChartPane;
import com.mongodb.client.model.Filters;
import com.opus.fxsupport.PropertyLinkDescriptor;
import com.opus.fxsupport.FXFController;
import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.smstate;
import isothermview.IsothermChart;


import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.validation.ValidationSupport;



public class FX1Controller extends FXFController implements com.opus.fxsupport.FXFControllerInterface, Initializable {

    private static final Logger LOG = Logger.getLogger(FX1Controller.class.getName());
    
    private String profilepath = ""; 
    private Controller appctrl = Controller.getInstance();
    
    private IsothermChart isothermchart; 
    private AuxChart auxchart;
    
    private XYChartPane chartpane;
    private XYChartPane auxchartpane;
    
    private ASVPDevice asvpdevice;

    private LinkedHashMap<String, AnchorPane> infopanes;
    private String current_infopane;
    
    private Context ctx;
    private MongoLink mongolink;
    
    
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lb_profile;

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

    @FXML
    private AnchorPane toppane;

    @FXML
    private SplitPane opsplit;

    @FXML
    private HiddenSidesPane mainchartpane;

    @FXML
    private StackPane infopane;

    @FXML
    private HiddenSidesPane auxpane;

    @FXML
    private AnchorPane bottompane;

    
    
    
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
    

    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOG.info(String.format("FX1Controller initializing with profile : %s", profilepath));
        
        update();
    }
    
    
       
    @Override
    public void update(){
      
        ctx = Context.getInstance();
        mongolink = MongoLink.getInstance();
        
        vs = new ValidationSupport(); 
        validators = new LinkedHashMap<>();
        
        appctrl.setFXANController(this);
        
        sidebar_btcycle.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.REFRESH, "black", 4));
        sidebar_btstore.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.DATABASE, "black", 4));
        sidebar_btreport.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.FILE_PDF_ALT, "black", 4));
        sidebar_btbroadcast.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.SHARE_ALT, "black", 4));
        sidebar_btloadfile.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.ARCHIVE, "black", 4));
        
        infopanes = new LinkedHashMap<>();
        
        infopanes.put("asvpdevice", new ASVPDeviceController() );
        infopanes.put("isotherminfo", new IsothermInfoController(this) );
        infopanes.put("pointinfo", new PointInfoController(this) );
            
        infopane.getChildren().addAll(infopanes.values());
        
        showInfoPane("asvpdevice");
        
        loadMainCharts();
        asvpdevice = ASVPDevice.getInstance();
        
    }
    
    public void clearInfoPanes() {
    
        for (AnchorPane pane : infopanes.values()){
            pane.setVisible(false);
        }
    }

    public void showInfoPane (String id){
        
        clearInfoPanes();
        AnchorPane ap = infopanes.get(id);
        if (ap != null){
            ap.setVisible(true);
            current_infopane = id;
        }
    }

    
    
    @smstate (state = "ACTIVATEPOINT")
    public boolean st_activatePoint(SMTraffic smm){
        
        VirnaPayload payload = smm.getPayload();
        String substate = payload.getCallerstate();
    
        switch(substate){
            
            case "REGISTERPOINT" :
                PointInfoCTX ptctx = ctx.registerPoint(payload.double1, true);
                if (ptctx != null){
                    Point pt = ptctx.getPoint();
                    if (pt.getDoses().get(0) instanceof Long){
                        pt.loadChildren(false, new SMTraffic(0l, 0l, 0, "ACTIVATEPOINT", this.getClass(),
                                                new VirnaPayload()
                                                    .setDouble1(payload.double1)
                                                    .setCallerstate("LOADPHASE3"))); 
                    }
                    else{
                        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "ACTIVATEPOINT", this.getClass(),
                                                new VirnaPayload()
                                                    .setCallerstate("PREPARECTX")
                                                    .setDouble1(payload.double1) ));  
                    }             
                }
                else{
                    LOG.severe(" No point to register !!!");
                }
                break;
                
            case "LOADPHASE3" :
                Entity ph3et = (Entity)payload.vobject;
                ph3et.loadChildren(false, null);
                appctrl.processSignal(new SMTraffic(0l, 0l, 0, "ACTIVATEPOINT", this.getClass(),
                                                new VirnaPayload()
                                                    .setCallerstate("UPDATEUI")
                                                    .setDouble1(payload.double1) )); 
                break;
            
            case "UPDATEUI" :
                PointInfoCTX uptctx = ctx.updatePoint(payload.double1);
                PointInfoController pic = (PointInfoController)infopanes.get("pointinfo");
                uptctx.update();
                pic.update(uptctx);
                
                
                if (!current_infopane.equals("pointinfo")){
                    showInfoPane("pointinfo");
                }
                
                ctx.aux = uptctx.aux;
                getAuxchart().refreshChart();
                
        }
    
        return true;
    }    
     
    
    public void activatePoint (Double Pressure){
        ctx.registerPoint(Pressure, true);    
    }
    
    
    public void updateIsothermChart(){
        isothermchart.refreshChart();
        showInfoPane("isotherminfo");
        ctx.getAuxIso();
        ctx.aux = ctx.isoaux;
        getAuxchart().refreshChart();
    }
    
    
    
    public void loadMainCharts(){
        
        isothermchart = new IsothermChart(this);
        chartpane = isothermchart.createCernChart();
//        String ccs = getClass().getClassLoader().getResource("middlestripb/isochart.css").toExternalForm();
        chartpane.getStylesheets().add(getClass().getClassLoader().getResource("middlestripb/isochart.css").toExternalForm());
        
        setAuxchart(new AuxChart());
        auxchartpane = getAuxchart().createCernChart();
        auxchartpane.getStylesheets().add(getClass().getClassLoader().getResource("middlestripb/auxchart.css").toExternalForm());
        
        
        Platform.runLater(() -> {
            
            chartpane.setMinWidth(mainchartpane.getWidth());
            chartpane.setMinHeight(mainchartpane.getHeight());
            mainchartpane.setContent(chartpane);
            
            ChartsToolbar chtb = new ChartsToolbar(Side.LEFT, mainchartpane);
            mainchartpane.setLeft(chtb);
            
            
            auxchartpane.setMinWidth(auxpane.getWidth());
            auxchartpane.setMinHeight(auxpane.getHeight());
            auxpane.setContent(auxchartpane);
            
            SideNode bottom = new SideNode("Bottom", Side.BOTTOM, auxpane);
            bottom.setStyle("-fx-background-color: rgba(255,255,255,1);"
                    + "-fx-effect :  dropshadow(three-pass-box, black, 5.0, 0, 1.0, 1.0);");
            auxpane.setBottom(bottom);
     
        });
    
        
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
                    + "-fx-padding: 60 0 0 0"
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
                                + "-fx-padding: 60 0 0 0"
                                + "-fx-border-insets: 15 0 5 0;" 
                                + "-fx-background-insets: 15 0 5 0;");
                        pane.setPinnedSide(null);
                    } else {
                        setStyle("-fx-background-color: rgba(255,255,255,1);"
                                + "-fx-effect :  dropshadow(three-pass-box, red, 8.0, 0, 1.0, 1.0);"
                                + "-fx-background-radius: 6;" 
                                + "-fx-border-radius: 6;"
                                + "-fx-padding: 60 0 0 0"
                                + "-fx-border-insets: 15 0 5 0;" 
                                + "-fx-background-insets: 15 0 5 0;");
                        pane.setPinnedSide(side);
                    }
                }
            });
            
        }
      
    }
    
    
    class SideNode extends Label {

        public SideNode(final String text, final Side side,final HiddenSidesPane pane) {

            super(text + " (Click to pin / unpin)");

            setAlignment(Pos.CENTER);
            setPrefSize(50, 200);

            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (pane.getPinnedSide() != null) {
                        setText(text + " (unpinned)");
                        pane.setPinnedSide(null);
                    } else {
                        setText(text + " (pinned)");
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
                sidebar_btreport.setDisable(true);
                sidebar_btbroadcast.setDisable(true);
                sidebar_btloadfile.setDisable(false);
                //blainedevice.enableRun(true);
                break;
            
            default:
                sidebar_btcycle.setDisable(false);
                sidebar_btstore.setDisable(true);
                sidebar_btreport.setDisable(true);
                sidebar_btbroadcast.setDisable(true);
                sidebar_btloadfile.setDisable(false);
                //blainedevice.enableRun(true);
                break;
        }
        
    }
    
    
    
    // ========================================= DISPATCH SECTION =======================================================
    
    @FXML
    void btcycle_action(MouseEvent event) {
        showInfoPane("asvpdevice");
    }

    @FXML
    void btexport_action(MouseEvent event) {
//        MiddleStripB.mongolink.loadAsJson(UID);
//        MiddleStripB.mongolink.loadAsBean(UID);
//        appctrl.processSignal (new SMTraffic(0l, 0l, 0, "IMPORTISOTHERM", this.getClass(),
//                        new VirnaPayload().setString("/Bascon/ASVP/Quantawin/sample_a (Isotherm).txt")));
//        appctrl.processSignal (new SMTraffic(0l, 0l, 0, "UPDATEISOTHERMCHART", this.getClass(),
//                        new VirnaPayload()));
        
    }

    @FXML
    void btreport_action(MouseEvent event) {
        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "EXIT", this.getClass(),
                                   new VirnaPayload()));
        
    }

    @FXML
    void btstore_action(MouseEvent event) {
//        MiddleStripB.mongolink.savetoJsonFile();
//        isothermchart.test1();
        

    }
    
    @FXML
    void btloadfile_action(MouseEvent event) {
        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "LOADISO", this.getClass(),
                                   new VirnaPayload().setCallerstate("ASKUSER")));
        
    }
    
    @FXML
    void btrun_action(MouseEvent event) {
        
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