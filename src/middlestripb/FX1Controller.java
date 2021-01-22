/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;


import cern.extjfx.chart.XYChartPane;
import com.opus.fxsupport.PropertyLinkDescriptor;
import com.opus.fxsupport.FXFController;
import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import isothermview.Isotherm;
import isothermview.IsothermChart;


import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.validation.ValidationSupport;



public class FX1Controller extends FXFController implements com.opus.fxsupport.FXFControllerInterface, Initializable {

    private static final Logger LOG = Logger.getLogger(FX1Controller.class.getName());
    
    private String profilepath = ""; 
    private Controller appctrl = Controller.getInstance();
    
    
    
    
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
    private AnchorPane mainchartpane;

    @FXML
    private AnchorPane infopane;

    @FXML
    private AnchorPane auxpane;

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
      
        //this.setScene(scene);
        vs = new ValidationSupport(); 
        validators = new LinkedHashMap<>();
        
        appctrl.setFXANController(this);
        
        sidebar_btcycle.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.REFRESH, "black", 4));
        sidebar_btstore.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.DATABASE, "black", 4));
        sidebar_btreport.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.FILE_PDF_ALT, "black", 4));
        sidebar_btbroadcast.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.SHARE_ALT, "black", 4));
        sidebar_btloadfile.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.ARCHIVE, "black", 4));
        
        
        appctrl.processSignal (new SMTraffic(0l, 0l, 0, "IMPORTISOTHERM", this.getClass(),
                        new VirnaPayload().setString("/Bascon/ASVP/Quantawin/sample_a (Isotherm).txt")));
        
        appctrl.processSignal (new SMTraffic(0l, 0l, 0, "LOADISOTHERMCHART", this.getClass(), new VirnaPayload()));
        
             
    }
    
    
    public void loadMainChart (Isotherm isotherm){
        
        IsothermChart isothermchart = new IsothermChart(isotherm);
        XYChartPane chartpane = isothermchart.createCernChart();
        
        Platform.runLater(() -> {
            
            chartpane.setMinWidth(mainchartpane.getWidth());
            chartpane.setMinHeight(mainchartpane.getHeight());
            mainchartpane.getChildren().add(chartpane);
            isotherm.chart_ready = true;
        });
      
    }
    
    
    
    
    
    
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
        System.exit(0);
    }

    @FXML
    void btexport_action(MouseEvent event) {
        MiddleStripB.mongolink.loadAsJson(UID);
        MiddleStripB.mongolink.loadAsBean(UID);
    }

    @FXML
    void btreport_action(MouseEvent event) {
        MiddleStripB.mongolink.saveNewRecords();
    }

    @FXML
    void btstore_action(MouseEvent event) {
        MiddleStripB.mongolink.savetoJsonFile();
    }
    
    @FXML
    void btloadfile_action(MouseEvent event) {
        MiddleStripB.mongolink.report();
    }
    
    @FXML
    void btrun_action(MouseEvent event) {
        //Random rand = new Random();
        //addTimeEntry (String.format(Locale.US, "%5.2f", 125.0 + (rand.nextDouble()-0.5)*2));
        //checklist1.addEntry(160 + ((rand.nextDouble()-0.5)*2), "Normal");
        appctrl.processSignal(new SMTraffic(0l, 0l, 0, "INITRUNS", this.getClass(),
                                   new VirnaPayload()));
    }
    
   
    @Override
    public void clearCanvas(){ 
//        pnl_user.setVisible(false);
//        checklist1.setOpacity(0.0);
    }
    
    @FXML
    void canvas_clicked(MouseEvent event) {
        //LOG.info("Canvas clicked ...");
        clearCanvas();
    }
    
    
    @FXML
    void user_action(MouseEvent event) {

    }
  
    public Scene getScene() { return getScene();}

    
    
    private static final String UID = "FX1";
    @Override
    public String getUID() { return UID;}

    


    
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