/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport;


import com.opus.fxsupport.validation.FXFField;
import com.opus.fxsupport.validation.FXFFieldDescriptor;
import com.opus.fxsupport.validation.FXFValidator;
import com.opus.syssupport.PicnoUtils;
import com.opus.syssupport.SMTraffic;
import com.opus.syssupport.VirnaPayload;
import com.opus.syssupport.VirnaServiceProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;


public class FXFController implements FXFControllerInterface {

    private static final Logger LOG = Logger.getLogger(FXFController.class.getName());

    protected FXMLLoader fxmlLoader;
    protected Scene scene;
    protected ValidationSupport vs;
    protected LinkedHashMap<FXFField, FXFValidator>validators;
    
    protected WidgetContext wctx;
    protected int outfocus_counter = 0;
    
    protected String profileid;
    //protected LauncherItem launcher;
    
    public static VirnaServiceProvider sctrl;
        
    
    public FXFController() {
               
    }

    
    public static LinkedHashMap<String, String> vfieldtypes;
    public static LinkedHashMap<String, String> lfieldtypes;
    public static void initFieldtypes(){
        
        if (vfieldtypes == null){
            vfieldtypes = new LinkedHashMap<>();
            lfieldtypes = new LinkedHashMap<>();

            vfieldtypes.put("void", "Não Manejado");
            vfieldtypes.put("number", "Campo Numérico");
            vfieldtypes.put("number:device_temp", "Numerico Sensor de temperatura");
            vfieldtypes.put("text_notempty", "Texto (não vazio)");
            vfieldtypes.put("text_void", "Texto (simples)");
            vfieldtypes.put("regex", "Formato Definido");
            vfieldtypes.put("barcode", "Leitor Barcode/QR");

            lfieldtypes.put("Não Manejado", "void");
            lfieldtypes.put("Campo Numérico", "number");
            lfieldtypes.put("Numerico Sensor de temperatura", "number:device_temp");
            lfieldtypes.put("Texto (não vazio)", "text_notempty");
            lfieldtypes.put("Texto (simples)", "text_void");
            lfieldtypes.put("Formato Definido", "regex");
            lfieldtypes.put("Leitor Barcode/QR", "barcode");
        }
    }
    
    
    public void addContext (ObservableMap<String, Object> namespace){
        
        wctx = new WidgetContext(this, namespace); 
        
        LinkedHashMap<Integer,WidgetDescriptor> templist = new LinkedHashMap<>();
        outfocus_counter = 0;
   
        namespace.forEach((name, obj) -> {
            if (obj != null){
                if (obj instanceof Node){
                    ((Node) obj).setFocusTraversable(false);
                    if (obj instanceof FXFField){
                        FXFField field = (FXFField)obj;
                        int focus;
                        WidgetDescriptor wd;
                        
                        if (field.getFocusPosition() == null || field.getFocusPosition() == 0){
                            focus = getNextOutFocusCounter();
                            wd = new WidgetDescriptor(focus, field);
                            wd.enter_focusable = false;
                        }
                        else{
                            focus = field.getFocusPosition();
                            wd = new WidgetDescriptor(focus, field);
                        }
                        wd.name = name;
                        String fname = name;
                        field.setManagement(this, focus, wctx);
                        templist.put(focus, wd);
//                        String mes = String.format("Registering Key: %s -> %d / type=%s", name, focus, obj.toString());
//                        LOG.info(mes);
                    }
                }
            }
        });
        
        wctx.setWidgetList(templist.entrySet()
        .stream()
        .sorted(Map.Entry.<Integer, WidgetDescriptor>comparingByKey())
        .collect(Collectors.toMap(
            Map.Entry::getKey, 
            Map.Entry::getValue, 
            (oldValue, newValue) -> oldValue, LinkedHashMap::new)));      
    }
    
    public int getNextOutFocusCounter() { return (++outfocus_counter) + 900 ;}
    
    
    public <T>T getWidget(String name, Class<T> clazz){
   
        WidgetDescriptor wd = wctx.findByName(name);
        if (wd != null){
            return (T)wd.node;
        }
        return null;
    }
    
    
    public WidgetDescriptor getWidgetDescriptor(String name){
      
        WidgetDescriptor wd = wctx.findByName(name);
        if (wd != null){
            return wd;
        }  
        return null;
    }
    
    
    public void updateFocus (Integer pos, boolean focused){
        
        WidgetDescriptor wgtdesc = wctx.widget_list.get(pos);
        wgtdesc.hasfocus = focused;
        //LOG.info(String.format("Focus @ %d (position = %d) was %s", wgtdesc.node.hashCode(), pos, focused ? "Gained":"Lost"));
    }
   
    
    public void yieldFocus (FXFField field, boolean fwd, boolean tab){
        
        WidgetDescriptor wd;
        int next_widget;
        Integer[] focusmap = new Integer[wctx.getFocusCycleMap().size()];
        wctx.getFocusCycleMap().toArray(focusmap);
        Integer focus_idx = field.getFocusPosition();
        
        //LOG.info(String.format("YieldFocus : field=%d / focuspos=%d", field.hashCode(), focus_idx));
        
        for (int i = 0; i < focusmap.length; i++) {
            
            if (Objects.equals(focusmap[i], focus_idx)){
                // Release focus from component and update focus map accordingly
                wd = wctx.getWidgetList().get(focus_idx);
                wd.hasfocus = false;
                field.setFocus(false);
                
                //LOG.info(String.format("\t Focus lost on: map index=%d", i));
                
                int startscan = i;
                boolean done = false;
                
                while (!done){
                    if (fwd){
                        next_widget = startscan+1;
                        if (next_widget > focusmap.length-1) next_widget = 0;
                    }
                    else{
                        next_widget = startscan-1;
                        if (next_widget < 0) next_widget = focusmap.length;
                    }

                    int next_index = focusmap[next_widget];
                    //LOG.info(String.format("\t Next focusmap index = %d -> next position=%d", next_widget, next_index));
                    wd = wctx.getWidgetList().get(next_index);
                    if (wd.enter_focusable || (tab && wd.tab_focusable)){
                        wd.hasfocus = true;
                        FXFField next_field = (FXFField)wd.node;
                        next_field.setFocus(true);
                        //LOG.info(String.format("\tFocus was set on widget = %d", next_field.hashCode()));
                        done = true;
                    }
                    else {
                        startscan = fwd ? startscan+1 : startscan-1;    
                    }
                }
                break; 
            } 
        }
        //LOG.info("Focus cycled...");
    }
    
    
    // ====================================== Utils ===============================================
    
    public static void updateAutocomplete(FXFFieldDescriptor fxfd, ArrayList<String> values){
        
        String acfile;
        
        if (fxfd.isUse_autocomplete()){
            acfile = fxfd.getAutocomplete_file();
            if (acfile != null && !acfile.isEmpty()){
                try {
                    if (values != null){
                        fxfd.setAcbindinglist(values);
                        PicnoUtils.saveAuxJson(acfile, values, true);
                    }
                    else{
                        fxfd.setAcbindinglist(PicnoUtils.loadAuxJson(acfile, ArrayList.class));
                    }
                } catch (IOException ex) {
                    LOG.warning(String.format("AuxJson unable to load acfile from %s due %s - using default list instead", 
                                                                acfile, ex.getMessage()));
                }
            }
            else{
                acfile = PicnoUtils.getAutoFilenameJson("ac_");
                try {
                    if (values == null){
                        values = new ArrayList<String>();
                        values.add("Nova lista de sugestões");
                    }
                    fxfd.setAcbindinglist(values);
                    PicnoUtils.saveAuxJson(acfile, values, true);
                    fxfd.setAutocomplete_file(acfile);
                } catch (IOException ex) {
                    LOG.warning(String.format("AuxJson unable to load acfile from %s due %s - using default list instead", acfile, ex.getMessage()));
                }
            }
            if (fxfd.getAcbinding() != null){
                fxfd.getAcbinding().dispose();
            }
            fxfd.setAcbinding(TextFields.bindAutoCompletion((TextField)fxfd.getField(FXFField.class), fxfd.getAcbindinglist()));   
        }
    }
    
//    public static void updateFormulas(FXFFieldDescriptor fxfd, ArrayList<String> values, ActivityModel model){
//        
//        String acfile;
//        
//      
//        if (fxfd.isUseformula()){
//            acfile = fxfd.getFormulafile();
//            if (acfile != null && !acfile.isEmpty()){
//                try {
//                    if (values != null){
//                        fxfd.setFormulalist(values);
//                        PicnoUtils.saveAuxJson(acfile, values, true);
//                    }
//                    else{
//                        fxfd.setFormulalist(PicnoUtils.loadAuxJson(acfile, ArrayList.class));
//                        //FormulaResources.getInstance().addFormula(fxfd, model);
//                    }
//                } catch (IOException ex) {
//                    LOG.warning(String.format("AuxJson unable to load formulas from %s due %s - using default list instead", 
//                                                                acfile, ex.getMessage()));
//                }
//            }
//            else{
//                acfile = PicnoUtils.getAutoFilenameJson("form_");
//                try {
//                    if (values == null){
//                        values = new ArrayList<String>();
//                        //values.add("#Insira aqui suas formulas de calculo");
//                        values.add("#Formulas de cálculo para analise Yara - parametro SSA :");
//                        values.add("# Os seguintes argumentos estão disponíveis para uso :");
//                        values.add("#\taltura \u003d campo altura da amostra (via micrometro)");
//                        values.add("#\tdensidade \u003d densidade");
//                        values.add("#\tpeso \u003d peso da mostra");
//                        values.add("#\ttemperatura \u003d temperatura do ensaio (via sensor)");
//                        values.add("#\tescoamento \u003d média das amostragens.");
//                        values.add("");
//                    }
//                    fxfd.setFormulalist(values);
//                    PicnoUtils.saveAuxJson(acfile, values, true);
//                    fxfd.setFormulafile(acfile);
//                    //FormulaResources.getInstance().addFormula(fxfd, model);
//                    
//                } catch (IOException ex) {
//                    LOG.warning(String.format("AuxJson unable to load formulas from %s due %s - using default instead", acfile, ex.getMessage()));
//                }
//            }
////            if (fxfd.getAcbinding() != null){
////                fxfd.getAcbinding().dispose();
////            }
////            fxfd.setAcbinding(TextFields.bindAutoCompletion((TextField)fxfd.getField(FXFField.class), fxfd.getAcbindinglist()));  
//        }
//        
//    }
    
    public static MenuItem getContextMenuLabel (String mes, boolean title){
        
        MenuItem labelitem = new MenuItem(mes);
        labelitem.setDisable(true);
        
        if (title){
            labelitem.getStyleClass().add("context-menu-title");
        }
        else{
            labelitem.getStyleClass().add("context-menu-label");
        }
        return labelitem;
    }
    
    public static TextField addContextTextField (ArrayList<MenuItem> menuitems, String label, String value, int width){
        
       menuitems.add(getContextMenuLabel (label, false)); 
       
       TextField tf = new TextField(value);
       if (width > 0 ){
           tf.setMinWidth(width);
       }
       tf.getStyleClass().add("fxf-text-field");
       CustomMenuItem cmi = new CustomMenuItem(tf, false);
       menuitems.add(cmi);
       
       return tf;
    }
    
    
    public static CustomTextField addContextValidatedTextField (ArrayList<MenuItem> menuitems,
                                String label, String unit, String value){
        
        if (unit == null) unit = "";
        if (value == null) value = "";
        
        HBox hbox = new HBox(10.0);
        hbox.setAlignment(Pos.CENTER_LEFT);
        
        Label lbl = new Label(label);
        lbl.setMinWidth(120.0);
        
        Label unt = new Label(unit);
        
        CustomTextField ctf = new CustomTextField();
        
        ctf.getStyleClass().add("fxf-text-field");
        
        ctf.setText(value);
        final double width = ctf.getLayoutBounds().getWidth();
        if (width < 100){
            ctf.setPrefWidth(100.0);
        }
        else{
            ctf.setPrefWidth(width + 10.0);
        }
        
//        NumberValidator nv = new NumberValidator();
//        ValidationSupport.setRequired(ctf, true);
//        nv.setRanges(fxfd.getRanges());
        
        hbox.getChildren().addAll(lbl,ctf,unt);
        CustomMenuItem cmi = new CustomMenuItem(hbox, false);
        menuitems.add(cmi);
     
        return ctf;
    }
    
    
    public static ChoiceBox addContextChoiceBox (ArrayList<MenuItem> menuitems, String label, String setv,
            Set<String> values){
        
       menuitems.add(getContextMenuLabel (label, false)); 
       
       ChoiceBox chbx = new ChoiceBox<>();
       ObservableList<String> activitylist = FXCollections.<String>observableArrayList(values);
       chbx.getItems().addAll(activitylist);
       chbx.setValue(setv);
       
       CustomMenuItem cmi = new CustomMenuItem(chbx, false);
       menuitems.add(cmi);
       
       return chbx;
    }
    
    public static ToggleSwitch addContextCheckBox (ArrayList<MenuItem> menuitems, String label, boolean setv){
        
        ToggleSwitch cbox = new ToggleSwitch(label);
        cbox.setSelected(setv);
        CustomMenuItem cmi = new CustomMenuItem(cbox, false);
        menuitems.add(cmi);
        
        return cbox;
    }
    
    
    public static ContextMenu showContextNoPermission(){
        
        ContextMenu ctxm = new ContextMenu();
        MenuItem witem1 = new MenuItem("Não há permissão para a alteração de dados desse Desktop");
        witem1.setDisable(true);
        ctxm.getItems().add(witem1);
        MenuItem witem2 = new MenuItem("Solicite autenticação como administrador");
        witem2.setDisable(true);
        ctxm.getItems().add(witem2);
        
        return ctxm;
    }
    
    public static Double convertToDouble(String value, String message, Double vdefault){
        
        String mes = (message == null) ? "Ooops ! - O valor digitado não é um numero." : message; 
        
        try{
            String svalue = value.replace(',', '.');
            Double dvalue = Double.valueOf(svalue);
            return dvalue;
        }
        catch (Exception ex){
//            FXFHeaderband hb = FXFWindowManager.getInstance().getHeaderBand();
//            hb.showSnack(String.format(mes, value));
            return vdefault;
        }
    }
    
    public static String convertFromDouble(Double value, String message, String vdefault){
        
        if (message == null) message = "Erro na conversão de valor em ponto flutuante - Campo é nulo !";
        
        if (value != null){
            return String.valueOf(value);
        }
        else{
            sctrl.processSignal(new SMTraffic(0l, 0l, 0, "ADD_NOTIFICATION", FXFController.class,
                           new VirnaPayload().setString(
                                "Conversor de unidades&" + "INFO&" +
                                String.format("%s&", message) +
                                "void"        
                           )
            ));
            return vdefault;
        }
    }
    
    
    public static Integer convertToInteger(String value, String message, Integer vdefault){
        
        String mes = (message == null) ? "Ooops ! - O valor digitado não é um numero." : message; 
        
        try{
            String svalue = value.replace(',', '.');
            Integer dvalue = Integer.valueOf(svalue);
            return dvalue;
        }
        catch (Exception ex){
//            FXFHeaderband hb = FXFWindowManager.getInstance().getHeaderBand();
//            hb.showSnack(String.format(mes, value));
            return vdefault;
        }
    }
    
    
    public static String convertFromInteger(Integer value, String message, String vdefault){
        
        if (message == null) message = "Erro na conversão de valor numeral - Campo é nulo !";
        
        if (value != null){
            return String.valueOf(value);
        }
        else{
            sctrl.processSignal(new SMTraffic(0l, 0l, 0, "ADD_NOTIFICATION", FXFController.class,
                           new VirnaPayload().setString(
                                "Conversor de unidades&" + "INFO&" +
                                String.format("%s&", message) +
                                "void"        
                           )
            ));
            return vdefault;
        }
    }
    
    
    // ================================================= Implementations =========================================================
    
    @Override
    public String getUID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public void yieldFocus (FXFField field, boolean fwd, boolean tab) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public void sendSignal(PropertyLinkDescriptor pld, String sigtype) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearCanvas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Scene scene) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAppController(VirnaServiceProvider ctrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public SystemMenu getMenu(boolean isadm) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public void updateField(String fieldname, String value, boolean required) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void activateModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initValidators(FXFField field, FXFFieldDescriptor fxfd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProfileID() {
        return profileid;
    }

//    @Override
//    public FXFCheckListViewNumber<String> getRunControl() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public FXFCountdownTimer getCDT() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void resetDevices() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public FXFBlaineDeviceController getBlaineDevice() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

//    @Override
//    public LauncherItem getLauncher() {
//        return launcher;
//    }
//
//    @Override
//    public void setLauncher(LauncherItem launcher) {
//        this.launcher = launcher;
//        
//    }

    @Override
    public void resetDevices() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
}
