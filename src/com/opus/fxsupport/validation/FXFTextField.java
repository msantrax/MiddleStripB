/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport.validation;

import com.opus.fxsupport.FXFController;
import com.opus.fxsupport.FXFControllerInterface;
import com.opus.fxsupport.PropertyLinkDescriptor;
import com.opus.fxsupport.WidgetContext;
import com.opus.fxsupport.WidgetDescriptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;


import org.controlsfx.validation.ValidationSupport;



public class FXFTextField extends CustomTextField implements FXFField{

    private static final Logger LOG = Logger.getLogger(FXFTextField.class.getName());
    
    private static final String NUMBER_REGEX =  "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
                        
    
    private FXFControllerInterface controller;
    //private FXFWidgetManager wdgtmanager;
    private WidgetContext wctx;
    
    private Integer focusPosition = 0;
    public final FXFTextField instance;
   
    private String sid= "";
    
    
    public FXFTextField() {
    
        // Add key pressed and released events to the TextField
        setOnKeyPressed((final KeyEvent keyEvent) -> {
            handleKeyEvent(keyEvent);
        });
        
        focusedProperty().addListener((obs, oldVal, newVal) -> {
           handleFocusEvent(newVal);
        }); 
        
        if (!isEditable()){
            ValidationSupport.setRequired(this, false);
        }
        
        instance = this;
    }

    
    public void setLinkedMode(){
        instance.setEditable(false);
        instance.setStyle("-fx-text-fill : blue;");
    }
    
    public void setNormalMode(){
        instance.setEditable(true);
        instance.setStyle("-fx-text-fill : black;");
    }
    
    
    @Override
    public void updateValue(String value, boolean required){
   
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                instance.setText(value);
                ValidationSupport.setRequired(instance, required);
            }
        });
    }
    
    @Override
    public String getValue(){
        return instance.getText();  
    } 
    
    
    public void refreshField(Control field, FXFFieldDescriptor fxfd){
        
        controller.initValidators(this, fxfd);
        updateValue(this.getText(), true);
        
//        ProfileResources pr = PicnoUtils.profile_resources;
//        pr.updateProfile(controller.getProfileID());
   
        //LOG.info("Refreshing...."); 
    }
    
    
    public ContextMenu getConfigurationMenu(Control field, FXFFieldDescriptor fxfd){
        
        String temp1;
        
        ContextMenu ctxm = new ContextMenu();
        TextField tf = (TextField)field;
        FXFController.initFieldtypes();
        
        ArrayList<MenuItem> menuitems = new ArrayList<>();
        
        ctxm.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, e -> {
            e.consume();
        });

        String ftype =  tf.isEditable() ? "" : " - Campo calculado";
        menuitems.add(FXFController.getContextMenuLabel ("Perfil do Item"+ ftype , true));
      
        AutoCompletionBinding<String> acbinding = fxfd.getAcbinding();
        
        
        ChoiceBox fieldtype = FXFController.addContextChoiceBox(menuitems, "Tipo de Manejo :", 
                FXFController.vfieldtypes.get(fxfd.getValidator_type()), 
                FXFController.lfieldtypes.keySet());        
        fieldtype.setOnAction((event) -> {
            ctxm.hide();
            if (!tf.isEditable() || fxfd.isLocked()){
                // Show snack with error
//                FXFHeaderband hb = FXFWindowManager.getInstance().getHeaderBand();
//                hb.showSnack("Ooops! - Esse campo é usado em rotinas de cálculo e seu  manejo não deve ser modificado.");
                //String result = wm.showAutoCompleteEditorDialog(fxfd);
            }
            else{
                fxfd.setValidator_type(FXFController.lfieldtypes.get((String)fieldtype.getValue()));
                refreshField(field,fxfd);
            }
            
        });
        
        
        if (!fxfd.getValidator_type().equals("void")){
            
            if (fxfd.getValidator_type().contains("number")){
                
                
                if (tf.isEditable()){
                    TextField  defaultvalue = FXFController.addContextValidatedTextField (menuitems, 
                        "Valor padrão :", fxfd.getUnit(), fxfd.getDefault_value());
                    defaultvalue.setOnAction((event) -> {
                        ctxm.hide();
                        fxfd.setDefault_value(defaultvalue.getText());
                        refreshField(field,fxfd);
                    });
                }
                else if (fxfd.isUseformula()){
                    MenuItem formedit = new MenuItem("Editar Formula");
                    // TODO : Show Formula edit dialog 
//                    formedit.setOnAction((event) -> {
//                        ctxm.hide();
//                        FXFWindowManager wm = FXFWindowManager.getInstance();
//                        FXFHeaderband hb = wm.getHeaderBand();
//                        SimpleStringProperty result = hb.showFormulaEditorDialog(fxfd);
//                        
//                        if (result != null){                
//                            result.addListener(new ChangeListener<String>() {
//                                @Override
//                                public void changed(ObservableValue <? extends String> prop, String ov, String nv) {
//                                    //LOG.info(String.format("Valor : %s ", nv));
//                                    hb.hideInputDialog();
//                                    if(nv != null && !nv.equals("cancel")){
//                                        FXFController.updateFormulas(fxfd, fxfd.getFormulalist(), null);
//                                        refreshField(field,fxfd);
//                                    }
//                                }
//                            });    
//                        }
//                    });
                    menuitems.add(formedit);
                }
                
                
                TextField  unit = FXFController.addContextValidatedTextField (menuitems, 
                        "Unidade de medida", "", fxfd.getUnit());
                unit.setOnAction((event) -> {
                    ctxm.hide();
                    fxfd.setUnit(unit.getText());
                    refreshField(field,fxfd);
                });
                
                ToggleSwitch userange = FXFController.addContextCheckBox (menuitems, "Use Validador", fxfd.isUse_range());
                userange.selectedProperty().addListener(ev -> {
                //userange.setOnAction((event) -> {
                    ctxm.hide();
                    fxfd.setUse_range(userange.isSelected());
                    refreshField(field,fxfd);
                });

                if (fxfd.isUse_range()){
                    //final Boolean usewindow = fxfd.isUse_windowrange();
                    ToggleSwitch relativerange = FXFController.addContextCheckBox (menuitems, "Use Valores relativos",fxfd.isUse_windowrange());
                    relativerange.selectedProperty().addListener(ev -> {
                        ctxm.hide();
                        if (relativerange.isSelected()){
                            fxfd.setUse_windowrange(true);
                            fxfd.getRanges()[0] = Double.parseDouble(fxfd.getDefault_value());
                            fxfd.getRanges()[1] = 10.0;
                            fxfd.getRanges()[2] = 50.0;
                        }
                        else{
                            fxfd.setUse_windowrange(false);
                            Double target = Double.parseDouble(fxfd.getDefault_value());
                            fxfd.getRanges()[0] = target - (target/2);
                            fxfd.getRanges()[1] = target - (target/10);
                            fxfd.getRanges()[2] = target + (target/10);
                            fxfd.getRanges()[3] = target + (target/2);
                        }
                        refreshField(field,fxfd);
                    });

                    if (fxfd.isUse_windowrange()){
                        addRangeEditor(menuitems, fxfd, 0, ctxm, field, "Alvo Desejado :" , fxfd.getUnit());
                        addRangeEditor(menuitems, fxfd, 1, ctxm, field, "Janela de Alarme :" , "%");
                        addRangeEditor(menuitems, fxfd, 2, ctxm, field, "Janela de Erro :" , "%");
                    }
                    else{
                        String sunit = fxfd.getUnit();
                        addRangeEditor(menuitems, fxfd, 0, ctxm, field, "Erro inferior" , sunit);
                        addRangeEditor(menuitems, fxfd, 1, ctxm, field, "Alarme inferior" , sunit);
                        addRangeEditor(menuitems, fxfd, 2, ctxm, field, "Alarme superior" , sunit);
                        addRangeEditor(menuitems, fxfd, 3, ctxm, field, "Erro superior" , sunit);
                    }
                }   
            }
            else if (fxfd.getValidator_type().equals("text_notempty") || 
                    fxfd.getValidator_type().equals("text_void") ||
                    fxfd.getValidator_type().equals("regex")){
                
                if (fxfd.getValidator_type().equals("regex")){
                    TextField  regexvalue = FXFController.addContextTextField (menuitems, 
                        "Expressão a observar : ", fxfd.getRegex(), 400);
                    regexvalue.setOnAction((event) -> {
                        ctxm.hide();
                        fxfd.setRegex(regexvalue.getText());
                        refreshField(field,fxfd);
                    });
                } 
                
                TextField  defaultvalue = FXFController.addContextTextField (menuitems, 
                    "Valor padrão :", fxfd.getDefault_value(), 200);
                defaultvalue.setOnAction((event) -> {
                    ctxm.hide();
                    fxfd.setDefault_value(defaultvalue.getText());
                    refreshField(field,fxfd);
                });
                
                
                ToggleSwitch useautoc = FXFController.addContextCheckBox (menuitems, "Sugerir valores", fxfd.isUse_autocomplete());
                useautoc.selectedProperty().addListener(ev -> {
                    ctxm.hide();
                    fxfd.setUse_autocomplete(useautoc.isSelected());
                    refreshField(field,fxfd);
                });
                
                if (fxfd.isUse_autocomplete()){
                    MenuItem acedit = new MenuItem("Editar lista de sugestões");
                    // TODO : show sugestion list 
//                    acedit.setOnAction((event) -> {
//                        ctxm.hide();
//                        FXFWindowManager wm = FXFWindowManager.getInstance();
//                        FXFHeaderband hb = wm.getHeaderBand();
//                        SimpleStringProperty result = hb.showAutoCompleteEditorDialog(fxfd);
//                        
//                        if (result != null){                
//                            result.addListener(new ChangeListener<String>() {
//                                @Override
//                                public void changed(ObservableValue <? extends String> prop, String ov, String nv) {
//                                    //LOG.info(String.format("Valor : %s ", nv));
//                                    hb.hideInputDialog();
//                                    if(nv != null && !nv.equals("cancel")){
//                                        FXFController.updateAutocomplete(fxfd, fxfd.getAcbindinglist());
//                                        refreshField(field, fxfd);
//                                    }
//                                }
//                            });    
//                        }
//                        
////                        if(result != null && !result.equals("cancel")){
////                            //LOG.info(String.format("ACEditor returned : %s", result));
////                            FXFController.updateAutocomplete(fxfd, fxfd.getAcbindinglist());
////                            refreshField(field,fxfd);
////                        }
//                    });
                    menuitems.add(acedit);
                }
            }
            
            else if (fxfd.getValidator_type().equals("barcode")){
                String reader = "Não Conectado...";
                HashSet its = new HashSet<String>();
                its.add(reader);
                ChoiceBox readertype = FXFController.addContextChoiceBox(menuitems, "Tipo de Leitor :", 
                reader, its);        
                readertype.setOnAction((event) -> {
                    ctxm.hide();
                });
            }
            
            
            if (tf.isEditable()){
                ToggleSwitch required = FXFController.addContextCheckBox (menuitems, "Requer confirmação", fxfd.isRequired());
                required.selectedProperty().addListener(ev -> {
                    ctxm.hide();
                    fxfd.setRequired(required.isSelected());
                    refreshField(field,fxfd);
                });
            }
            else{
                fxfd.setRequired(false);
            }
            
            
            TextField  tooltipvalue = FXFController.addContextTextField (menuitems, 
                "Tooltip :", fxfd.getTooltip_message(), 400);
            tooltipvalue.setOnAction((event) -> {
                ctxm.hide();
                fxfd.setTooltip_message(tooltipvalue.getText());
                refreshField(field,fxfd);
            });
            
            
        }
        
        ctxm.getItems().addAll(menuitems);
        
        fxfd.setCtxm(ctxm);
        return ctxm;
    }
    
    
    private void addRangeEditor (ArrayList<MenuItem> menuitems, FXFFieldDescriptor fxfd, int index, ContextMenu ctxm, Control field,
                                    String label, String unit ){
                                    
        // Determine rangetarget
        final String srangeitem = String.format(Locale.US, "%5.2f", fxfd.getRanges()[index]);
        TextField rangeitem = FXFController.addContextValidatedTextField (menuitems, label, unit, srangeitem);
        rangeitem.setOnAction((event) -> {
            ctxm.hide();
            if (!srangeitem.equals(rangeitem.getText())){
                Double dvalue = FXFController.convertToDouble(rangeitem.getText(), 
                    "O valor [ %s ] não é apropriado a esse campo.", 
                    fxfd.getRanges()[index]);
                fxfd.getRanges()[index] = dvalue;
                
                if (fxfd.isUse_windowrange()){
                    if (index == 0){
                        fxfd.getRanges()[1] = 10.0;
                        fxfd.getRanges()[2] = 50.0;
                    }
                    else if (index == 1){
                        fxfd.getRanges()[2] = fxfd.getRanges()[1] + 40.0 ;
                    }
                }
                
//                BlaineDevice bd = BlaineDevice.getInstance();
//                bd.updateProfile();
                
                refreshField(field,fxfd);
            }  
        });
        
        
    }
    
    
    private void handleFocusEvent(boolean focused){
        //LOG.info(String.format("Widget %d %s focus", this.hashCode(), focused ? "has got" : "lost"));
        //wdgtmanager.updateFocus(wctx, focusPosition, focused);
        controller.updateFocus(focusPosition, focused);
        controller.clearCanvas();
    }
    
    
    // Helper Methods for Event Handling
    private void handleKeyEvent(KeyEvent e) {
 
        ValidationSupport.setRequired(this, getText().isEmpty());
        
        if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.ENTER) {
            //LOG.info(String.format("Enter pressed on widget %d", this.hashCode()));
            WidgetDescriptor wd = wctx.findByHash(this);
            if (wd != null){
                PropertyLinkDescriptor pld = wd.linkdescriptor;
//                if (wd.required){
//                    ValidationSupport.setRequired(this, getText().isEmpty());
//                }
                if (pld != null && pld.isStopfocus() && pld.isValid()){
                    //LOG.info(String.format("Stop focus Enter 
                    String callback = pld.getCallstate();
                    if (!callback.equals("NONE") && !getText().trim().isEmpty()){
                        pld.setAuxiliar("STOPENTER");
                        controller.sendSignal(pld, "");
                    }
                }
                else{
                    //wdgtmanager.yieldFocus(wctx, this, true, false);
                    controller.yieldFocus(this, true, false);
                }
            }
            else{
                //wdgtmanager.yieldFocus(wctx, this, true, false);
                controller.yieldFocus(this, true, false);
            }
            e.consume();
        }
        else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.TAB){
            //wdgtmanager.yieldFocus(wctx, this, true, true);
            controller.yieldFocus(this, true, true);
            e.consume();
        }
        else if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode() == KeyCode.COMMA){
            //wdgtmanager.yieldFocus(wctx, this, true, true);
            //e.consume();
            this.setText(this.getText()+".");
        }
    }
    
    
    @Override
    public void setManagement(FXFControllerInterface controller, Integer idx, WidgetContext wctx){
        this.controller = controller;
        this.focusPosition = idx;
        this.wctx = wctx;
        //this.wdgtmanager = FXFWidgetManager.getInstance();
    }
    
    
    
    @Override
    public void setFocusPosition(Integer pos){
        this.focusPosition = pos;
    }
    
    @Override
    public Integer getFocusPosition (){
        return focusPosition;
    }
    
    
    public void setFocus(boolean set){
        
        if (set){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    requestFocus();
                }
            });
        }
        else{
           setFocused(false); 
        }
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    
}








//Validator<String> isnumber_validator = new Validator<String>(){
//      @Override
//      public ValidationResult apply( Control control, String value ){
//        boolean condition =
//            value != null
//                ? !value
//                    .matches(
//                        "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*" )
//                : value == null;
//
//        System.out.println( condition );
//
//        return ValidationResult.fromMessageIf( control, "not a number", Severity.ERROR, condition );
//      }
//    };



//        Validator<String> isnumber_validator = new Validator<String>(){
//            
//            Double number ;
//            Double min = 0.2;
//            Double max = 15.0;
//     
//            
//            @Override
//            public ValidationResult apply( Control control, String value ){
//                
//                if (value != null ? !value.matches(NUMBER_REGEX) : false){
//                    return ValidationResult.fromMessageIf(control, "Deve ser um numero", Severity.ERROR, true );
//                }
//                try{
//                    number = Double.parseDouble(value);
//                    if (number < min){
//                        return ValidationResult.fromMessageIf(control, String.format("Valor menor que %f", min), Severity.WARNING, true );
//                    }
//                    if (number > max){
//                        return ValidationResult.fromMessageIf(control, String.format("Valor maior que %f", max), Severity.WARNING, true );
//                    }
//                }
//                catch (Exception ex){
//                    return ValidationResult.fromMessageIf(control, "Não posso converter o numero", Severity.ERROR, true );
//                }
//                
//                return ValidationResult.fromMessageIf(control, "OK !", Severity.ERROR, false );
//            }
//        };



//MenuItem mi1 = FXFController.getContextMenuLabel ("Menu1", true);
//        menuitems.add(mi1);
//        Menu mn1 = new Menu("PlaceHolder 1");
//        MenuItem mi3 = FXFController.getContextMenuLabel ("Sub Menu1", true);
//        MenuItem mi4 = FXFController.getContextMenuLabel ("Sub Menu1", true);
//        MenuItem mi5 = FXFController.getContextMenuLabel ("Sub Menu1", true);
//        mn1.getItems().addAll(mi3, mi4, mi5);
//        menuitems.add(mn1);

/*
Olá Marcello, bom dia ! - No GDrive há updates do PP200..

O resultado do Blaine foi corrigido.
O Kfactor foi corrigido.
A calibração está gravando OK e o link para a default também está operacional.
A sequencia de foco na calibração foi corrigida.
Instalado botão de leitura do micrometro no perfil Yara.
Instalado botão de calculo de porosidade via curva calculada no Blaine.
Instalado botao de leitura de peso na balança no Blaine.

O dialogo da formula do calculo de porosidade ainda não está habilitado.
Tanto leitura de Micrometro como da Balança está direto mas vai obedecer a presença do dispositivo.

Qdo der me liga OK ?










*/