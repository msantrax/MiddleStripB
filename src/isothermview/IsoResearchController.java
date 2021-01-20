
package isothermview;


import cern.extjfx.chart.LogarithmicAxis;
import cern.extjfx.chart.NumericAxis;
import cern.extjfx.chart.XYChartPane;
import cern.extjfx.chart.plugins.DataPointTooltip;
import cern.extjfx.chart.plugins.Panner;
import cern.extjfx.chart.plugins.Zoomer;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;


public class IsoResearchController {

    private static final Logger LOG = Logger.getLogger(IsoResearchController.class.getName());
    
    private IsoResearchModel model;
    
    
    private LinkedHashMap<String, TableColumn<Map, ?>> columns = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<Double,String>> columntype_maps = new LinkedHashMap<>();
    private ObservableList<Map<String, Object>> items;
  
    @FXML
    private URL location;

    @FXML
    private VBox vbox1;

    @FXML
    private WebView webview;
    
    @FXML
    private Button bt_go;

    @FXML
    private TableView<Map> table;

     @FXML
    private Tab graph;

    
    
    
    public IsoResearchController(IsoResearchModel model) {
        this.model = model;
    }

    
    
    
    
    private TableColumn<Map, Number> generateIntegerColumn(String label, String key, boolean editable,Double size, String style){
         
        TableColumn<Map, Number> col = new TableColumn<>(label);
        
        if (size == -1){
            final Text text = new Text(label);
            size = text.getLayoutBounds().getWidth() + 40.0;
            col.setPrefWidth(size);
        }
        else if (size != 0){
            col.setPrefWidth(size);
        }
        
        if (style != null){
            col.setStyle(style);
        }
        else{
            col.setStyle( "-fx-alignment: center; -fx-text-fill: blue;");
        }
        
        if (editable){
            col.setCellValueFactory(cellData -> {
                Map p = cellData.getValue();
                Integer s = (Integer)p.get(key);
                return new SimpleIntegerProperty(s);
            });
            
            col.setEditable(true);
            col.setOnEditCommit( e -> {
                int row = e.getTablePosition().getRow();
                Map m = e.getRowValue();
                m.put(key, e.getNewValue());
            });
        }
        else{
            col.setCellValueFactory(new MapValueFactory<>(key)); 
        }
   
        return col;
    }
    
    private TableColumn<Map, Double> generateDoubleColumn(String label, String key){
        TableColumn<Map, Double> col = new TableColumn<>(label);
        col.setCellValueFactory(new MapValueFactory<>(key));
        
        return col;
    }
    
    
    
    
    
    
    private TableColumn<Map, String> generateTextColumn(String label, String key, boolean editable, Double size){
        
        TableColumn<Map, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> {
            Map p = cellData.getValue();
            String s = (String)p.get(key);
            return new ReadOnlyStringWrapper(s);
        });
        col.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
        
        if (editable){
            col.setEditable(true);
            col.setOnEditCommit( e -> {
                int row = e.getTablePosition().getRow();
                Map m = e.getRowValue();
                m.put(key, e.getNewValue());
            });
        }
        
        col.setPrefWidth(size);
        return col;
    }
    
    
    private TableColumn<Map, Boolean> generateCheckColumn(String label, String key){
        
        TableColumn<Map, Boolean> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> {
            Map p = cellData.getValue();
            Boolean v = (Boolean)p.get(key);
            return new SimpleBooleanProperty(v);
        });
        
        
        col.setEditable(true);
        col.setCellFactory((TableColumn<Map, Boolean> p) -> {         
            CheckBox checkBox = new CheckBox();
            TableCell<Map, Boolean> tableCell = new TableCell<Map, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };

            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                event.consume();
                checkBox.setSelected(!checkBox.isSelected());
                Map<String, Object>item = (Map<String, Object>)tableCell.getTableRow().getItem();
                item.put(key, checkBox.isSelected());
            });
            
            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        
        return col;
    }
    
    private TableColumn<Map, String> generateTypeDoubleColumn(String label, String key){
    
        TableColumn<Map, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> {
            Map p = cellData.getValue();
            Double d = (Double)p.get(key);
            LinkedHashMap<Double,String> pointtype_map = columntype_maps.get(key);
            if (pointtype_map != null){
                String cellvalue = pointtype_map.get(d);
                if (cellvalue != null){
                    return new ReadOnlyStringWrapper(cellvalue); 
                }
            } 
            return new ReadOnlyStringWrapper("Default"); 
        });
        return col;
    }
    
    
    @FXML
    void initialize() {
        assert vbox1 != null : "fx:id=\"vbox1\" was not injected: check your FXML file 'isotherm.fxml'.";
        assert webview != null : "fx:id=\"webview\" was not injected: check your FXML file 'isotherm.fxml'.";
   
     
        
        LinkedHashMap<Double,String> pointtype_map = new LinkedHashMap<>();
        pointtype_map.put(0.0, "Adsorption");
        pointtype_map.put(1.0, "Desorption");
        columntype_maps.put("pointtype_key", pointtype_map);
        
        
        items = model.getMapData(1);
        table.getItems().addAll(items);
        //table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        

        

        columns.put("id_key", generateIntegerColumn("ID", "id_key", true, 0.0, null));
        columns.put("selected_key", generateCheckColumn("Active", "selected_key"));
        columns.put("pointtype_key", generateTypeDoubleColumn("Point Type", "pointtype_key"));
        columns.put("pressure_key",generateDoubleColumn("Pressure", "pressure_key"));
        columns.put("volume_key", generateDoubleColumn("Volume", "volume_key"));      
        columns.put("calculated_key", generateDoubleColumn("Calculated", "calculated_key"));
        columns.put("comment_key", generateTextColumn("Comment", "comment_key", true, 500.0));
        
        
        table.getColumns().addAll(columns.values());
   
        // Turn on multiple-selection mode for the TableView
        TableViewSelectionModel<Map> tsm = table.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<Integer> list = tsm.getSelectedIndices();
 
        // Add a ListChangeListener
        list.addListener((ListChangeListener.Change<? extends Integer> change) -> {
            LOG.info("Row has changed...");
            
        });
        
        
        
        // Menus =======================
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Menu 1");
        cm.getItems().add(mi1);
        MenuItem mi2 = new MenuItem("Menu 2");
        cm.getItems().add(mi2);

        table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) {
                    cm.show(table, t.getScreenX(), t.getScreenY());
                }
            }
        });
        
        // Graph ...
        createCernChart();
 
  
        
//        final WebEngine eng =webview.getEngine();
//        eng.load("http://google.com");
        LOG.info("FXController loaded");
   
    }
    
    
    
    private void createCernChart(){
        
        NumericAxis yAxis;
        NumericAxis xAxis;
        
        //LogarithmicAxis yAxis;
        //LogarithmicAxis xAxis;
        
        
        xAxis = new NumericAxis();
        //xAxis = new LogarithmicAxis();
        xAxis.setAnimated(false);
        xAxis.setLabel("Pressure");

        yAxis = new NumericAxis();
        //yAxis = new LogarithmicAxis();
        yAxis.setAnimated(false);
        yAxis.setLabel("Volume");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Logarithmic Axis Example");
        lineChart.setAnimated(false);

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>("Isotherm", createData());
        //lineChart.getData().add(new XYChart.Series<>("Isotherm", createData()));
        lineChart.getData().add(series1);
        
        
        XYChartPane<Number, Number> chartPane = new XYChartPane<>(lineChart);
        //chartPane.getPlugins().addAll(new Zoomer(), new Panner(), new DataPointTooltip(), new CrosshairIndicator<>());
        
        // Overlay LABEL =================================================
//        Label label = new Label("Info about chart data");
//        AnchorPane.setTopAnchor(label, 15.0);
//        AnchorPane.setLeftAnchor(label, 15.0);
//        AnchorPane anchorPane = new AnchorPane(label);
//        // Pass any mouse events to the underlying chart
//        anchorPane.setMouseTransparent(true);
        
        chartPane.getPlugins().addAll(new Zoomer(), 
                                        new Panner(), 
                                        new DataPointTooltip() ); //, 
                                        //new ChartOverlay<>(OverlayArea.PLOT_AREA, anchorPane));
        
        graph.setContent(chartPane);
        Node n = series1.nodeProperty().get();
        n.setStyle("-fx-stroke-width: 1px;");
        
        
    }
    
    private ObservableList<XYChart.Data<Number, Number>> createData() {
        
        List<XYChart.Data<Number, Number>> data = new ArrayList<>();
        
        for (Map<String, Object> entry : items) {    
            Boolean selected = (Boolean)entry.get("selected_key");
            if (selected){
                Double pressure = (Double)entry.get("pressure_key");
                Double volume = (Double)entry.get("volume_key");
                XYChart.Data<Number, Number> d = new XYChart.Data<>(pressure, volume);
                
                data.add(new XYChart.Data<>(pressure, volume));
            }
        }
        return FXCollections.observableArrayList(data);
    }
    
    
    
    private void createFXChart(){
        
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Pressure");
        
        NumberAxis yAxis = new NumberAxis(); 
        yAxis.setLabel("Volume");
        
        LineChart linechart = new LineChart(xAxis, yAxis);
        linechart.setTitle("Isotherma");
        
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
                
        for (Map<String, Object> entry : items) {    
            Boolean selected = (Boolean)entry.get("selected_key");
            if (selected){
                Double pressure = (Double)entry.get("pressure_key");
                Double volume = (Double)entry.get("volume_key");
                XYChart.Data<Number, Number> d = new XYChart.Data<>(pressure, volume);
                series1.getData().add(d);
            }
          
        }    
        linechart.getData().addAll(series1);
        
        graph.setContent(linechart);
        
        Node n = series1.nodeProperty().get();
        n.setStyle("-fx-stroke-width: 1px;");
            
    }
    
    
    @FXML
    void loadpage(ActionEvent evt) {
        LOG.info("Loadpage");
    }
    
   
}


































        
//              checkBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//                if(event.getCode() == KeyCode.SPACE)
//                    validate(checkBox, (Map<String, Object>) cell.getTableRow().getItem(), event);
//            });
//      
        
        
        
//        selected_col.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {     
//            @Override
//            public ObservableValue<Boolean> call(Integer param) {
//                LOG.info(String.format("Selected changed @ %d", param));
//                return new SimpleBooleanProperty(Boolean.valueOf(true));
//            }
//            
//        }));
        
               
//        selected_col.setEditable(true);
//        selected_col.setOnEditCommit( e -> {
//            int row = e.getTablePosition().getRow();
//            Map m = e.getRowValue();
//            m.put("selected_key", e.getNewValue());
//            LOG.info(String.format("Cell @ %d changed to %s", row, e.getNewValue().toString()));
//        });
        
//        final CheckBoxTableCell<Map, Boolean> ctCell = new CheckBoxTableCell<>();
//        final SimpleBooleanProperty selected = new SimpleBooleanProperty();
//        ctCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
//            @Override
//            public ObservableValue<Boolean> call(Integer index) {
//                return selected;
//                //return new SimpleBooleanProperty((Boolean)table.getItems().get(index).get("selected_key"));
//            }
//        });
//        
//        selected.addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
//                LOG.info(String.format("selected is : %s", selected.toString()));
//            }
//        });
//        


//
//       TableColumn<Map, Boolean> selected_col = new TableColumn<>("Selected");
//        selected_col.setCellValueFactory(cellData -> {
//            Map p = cellData.getValue();
//            Boolean v = (Boolean)p.get("selected_key");
//            return new SimpleBooleanProperty(v);
//        });
//        selected_col.setEditable(true);
//        selected_col.setCellFactory((TableColumn<Map, Boolean> p) -> {         
//            CheckBox checkBox = new CheckBox();
//            TableCell<Map, Boolean> tableCell = new TableCell<Map, Boolean>() {
//                @Override
//                protected void updateItem(Boolean item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || item == null) 
//                        setGraphic(null);
//                    else {
//                        setGraphic(checkBox);
//                        checkBox.setSelected(item);
//                    }
//                }
//            };
//
//            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
//                event.consume();
//                checkBox.setSelected(!checkBox.isSelected());
//                Map<String, Object>item = (Map<String, Object>)tableCell.getTableRow().getItem();
//                item.put("selected_key", checkBox.isSelected());
//            });    
//            tableCell.setAlignment(Pos.CENTER);
//            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//            return tableCell;
//        });
//
//        
//           
        
//        TableColumn<Map, String> comment_col = new TableColumn<>("Comment");
//        comment_col.setCellValueFactory(cellData -> {
//            Map p = cellData.getValue();
//            String s = (String)p.get("comment_key");
//            return new ReadOnlyStringWrapper(s);
//        });
//        comment_col.setCellFactory(TextFieldTableCell.<Map>forTableColumn());
//        
//        comment_col.setOnEditCommit( e -> {
//            int row = e.getTablePosition().getRow();
//            Map m = e.getRowValue();
//            m.put("comment_key", e.getNewValue());
//            //LOG.info(String.format("Cell @ %d changed to %s", row, e.getNewValue().toString()));
//        });
//     
//        comment_col.setEditable(true);
//        comment_col.setPrefWidth(500.0);


        
//        TableColumn<Map, String> adsorption_col = new TableColumn<>("Point Type");
//        adsorption_col.setCellValueFactory(cellData -> {
//            Map p = cellData.getValue();
//            Boolean v = (Boolean)p.get("adsorption_key");
//            String cellvalue = v ? "Adsorption" : "Desorption";
//            return new ReadOnlyStringWrapper(cellvalue); 
//        });
        //adsorption_col.setCellFactory(TextFieldTableCell.<Map>forTableColumn(adsorption_col));

        