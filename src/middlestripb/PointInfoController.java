/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.opus.glyphs.FontAwesomeIcon;
import com.opus.glyphs.GlyphsBuilder;
import com.opus.syssupport.PicnoUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author opus
 */
public class PointInfoController extends AnchorPane implements Initializable{

    private static final Logger LOG = Logger.getLogger(PointInfoController.class.getName());

    
    @FXML
    private AnchorPane isoinfo;

    @FXML
    private AnchorPane generalpane;

    @FXML
    private TextField init_ts;

    @FXML
    private TextField strategy;

    @FXML
    private TableView<DosesTblRow> doses_table;

    @FXML
    private TextField end_ts;

    @FXML
    private TextField point_status;

    @FXML
    private TextField point_timing;

    @FXML
    private Label btgeneral;

    @FXML
    private Label btvolume;

    @FXML
    private Label btextra;

    @FXML
    private AnchorPane calcpane;

    @FXML
    private TextField p0_pressure;

    @FXML
    private TextField amb_temp;

    @FXML
    private TextField dewar;

    @FXML
    private TextField volume;

    @FXML
    private TextField start_pressure;

    @FXML
    private TextField end_pressure;

    @FXML
    private AnchorPane detailspane;

    @FXML
    private TextField void_vol;

    @FXML
    private TextField vtc_sw;

    @FXML
    private TextField vvoid_sw;

    @FXML
    private TextField dv;

    @FXML
    private TextField tan;

    @FXML
    private TextField ttc;

    
    @FXML
    void btextra_action(MouseEvent event) {
        generalpane.setVisible(false);
        calcpane.setVisible(false);
        detailspane.setVisible(true);
        
    }

    @FXML
    void btgeneral_action(MouseEvent event) {
        generalpane.setVisible(true);
        calcpane.setVisible(false);
        detailspane.setVisible(false);

    }

    @FXML
    void btvolume_action(MouseEvent event) {
        generalpane.setVisible(false);
        calcpane.setVisible(true);
        detailspane.setVisible(false);

    }

    
    
    private FX1Controller anct;
    
    public PointInfoController(FX1Controller anct) {
    
        this.anct = anct;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PointInfo.fxml"));
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
        
        btgeneral.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.COGS, "black", 3));
        btvolume.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.FLASK, "black", 3));
        btextra.setGraphic(GlyphsBuilder.getAwesomeGlyph(FontAwesomeIcon.INFO_CIRCLE, "black", 3));
        
        
        TableColumn<DosesTblRow, String > numberCol = new TableColumn<>("ID");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberCol.setMaxWidth(30);
        numberCol.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn<DosesTblRow, String > typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setMaxWidth(50);
        typeCol.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn<DosesTblRow, String > startCol = new TableColumn<>("P Load");
        startCol.setCellValueFactory(new PropertyValueFactory<>("pstart"));
        startCol.setMaxWidth(75);
        startCol.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn<DosesTblRow, String > endCol = new TableColumn<>("P End");
        endCol.setCellValueFactory(new PropertyValueFactory<>("pend"));
        endCol.setMaxWidth(75);
        endCol.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn<DosesTblRow, String > dtCol = new TableColumn<>("ΔT (S)");
        dtCol.setCellValueFactory(new PropertyValueFactory<>("deltat"));
        dtCol.setMaxWidth(60);
        dtCol.setStyle( "-fx-alignment: CENTER;");
        
        
        doses_table.getColumns().addAll(numberCol, typeCol, startCol, endCol, dtCol);
        
        doses_table.setRowFactory( tv -> {
            TableRow<DosesTblRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    DosesTblRow rowData = row.getItem();
                    LOG.info(String.format(" Row clicked %s / %s", rowData.getNumber(), rowData.getPstart()));
                }
            });
            return row ;
        });
        
        
        
    }
    
    
    
    
    public void update (PointInfoCTX uptctx){
    
        //String time = uptctx.init_ts.format(PicnoUtils.df);
        init_ts.setText(uptctx.init_ts.format(PicnoUtils.df));
        
        //tring time = uptctx.init_ts.format(PicnoUtils.df);
        end_ts.setText(uptctx.end_ts.format(PicnoUtils.df));
        
        doses_table.getItems().clear();
        doses_table.getItems().addAll(uptctx.dosestbl);
        doses_table.refresh();
        
        LOG.info("T");
        
    }
    
    
    
    
}
