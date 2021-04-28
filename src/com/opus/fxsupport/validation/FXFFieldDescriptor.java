/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opus.fxsupport.validation;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import org.controlsfx.control.textfield.AutoCompletionBinding;

/**
 *
 * @author opus
 */
public class FXFFieldDescriptor {

    private transient Object field;
    
    
    private String name = "";
    private String tooltip_message = "Default Tooltip Message";
    private String validator_type = "number";
    protected boolean locked = true;
    private boolean use_range = false;
    private boolean use_windowrange = true;
    private Double[] ranges = {Double.NaN, Double.NaN, Double.NaN, Double.NaN};
    
    private boolean use_autocomplete = false;
    private String autocomplete_file = "";
    protected boolean autocomplete_autoadd = false;
    protected Integer autocomplete_maxitem = 150;
    
    protected String regex = "";
    
    private String local_callback = "";
    
    protected Object custom = new Object();
    protected String custom_classtype = "";
    
    private String format = "%5.2f";
    protected String unit = "";
    private String default_value = "0.0";
    private boolean required = true;
    private boolean maybenull = false;
    
    private transient Double xpos = 0.0; 
    private transient Double ypos = 0.0;
    private transient String report_format = "";
    private transient String report_font = "";
    private transient Integer report_fontsize = 12;
    
    private transient ContextMenu ctxm;
    protected transient FXFValidator validator;
    protected transient AutoCompletionBinding<String> acbinding;
    protected transient ArrayList<String> acbindinglist;
    
    protected boolean useformula = false;
    protected String formulafile = "";
    protected transient ArrayList<String> formulalist;
    
    
    public FXFFieldDescriptor() {
        
    }

    public void clearReportData(){
        xpos = 0.0; 
        ypos = 0.0;
        report_format = "";
        report_font = "";
      
    }
    
    public <T>T getField( Class<T> clazz) {
        return (T)field;
    }

    public void setField(Object field) {
        this.field = field;
    }
    
    
    public String getName() {
        return name;
    }

    public FXFFieldDescriptor setName(String name) {
        this.name = name;
        return this;
    }

    public String getTooltip_message() {
        return tooltip_message;
    }

    public FXFFieldDescriptor setTooltip_message(String tooltip_message) {
        this.tooltip_message = tooltip_message;
        return this;
    }

    public String getValidator_type() {
        return validator_type;
    }

    public FXFFieldDescriptor setValidator_type(String validator_type) {
        this.validator_type = validator_type;
        return this;
    }

    public boolean isUse_range() {
        return use_range;
    }

    public FXFFieldDescriptor setUse_range(boolean use_range) {
        this.use_range = use_range;
        return this;
    }

    public boolean isUse_windowrange() {
        return use_windowrange;
    }

    public FXFFieldDescriptor setUse_windowrange(boolean use_windowrange) {
        this.use_windowrange = use_windowrange;
        return this;
    }

    public Double[] getRanges() {
        return ranges;
    }

    public FXFFieldDescriptor setRanges(Double[] ranges) {
        this.ranges = ranges;
        return this;
    }

    public boolean isUse_autocomplete() {
        return use_autocomplete;
    }

    public FXFFieldDescriptor setUse_autocomplete(boolean use_autocomplete) {
        this.use_autocomplete = use_autocomplete;
        return this;
    }

    public String getAutocomplete_file() {
        return autocomplete_file;
    }

    public FXFFieldDescriptor setAutocomplete_file(String autocomplete_file) {
        this.autocomplete_file = autocomplete_file;
        return this;
    }

    public String getLocal_callback() {
        return local_callback;
    }

    public FXFFieldDescriptor setLocal_callback(String local_callback) {
        this.local_callback = local_callback;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public FXFFieldDescriptor setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getDefault_value() {
        return default_value;
    }

    public FXFFieldDescriptor setDefault_value(String default_value) {
        this.default_value = default_value;
        return this;
    }

    public boolean isRequired() {
        return required;
    }

    public FXFFieldDescriptor setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public boolean isMaybenull() {
        return maybenull;
    }

    public FXFFieldDescriptor setMaybenull(boolean maybenull) {
        this.maybenull = maybenull;
        return this;
    }

    public Double getXpos() {
        return xpos;
    }

    public void setXpos(Double xpos) {
        this.xpos = xpos;
    }

    public Double getYpos() {
        return ypos;
    }

    public void setYpos(Double ypos) {
        this.ypos = ypos;
    }

    public String getReport_format() {
        return report_format;
    }

    public void setReport_format(String report_format) {
        this.report_format = report_format;
    }

    public String getReport_font() {
        return report_font;
    }

    public void setReport_font(String report_font) {
        this.report_font = report_font;
    }

    public Integer getReport_fontsize() {
        return report_fontsize;
    }

    public void setReport_fontsize(Integer report_fontsize) {
        this.report_fontsize = report_fontsize;
    }

    public ContextMenu getCtxm() {
        return ctxm;
    }

    public void setCtxm(ContextMenu ctxm) {
        this.ctxm = ctxm;
    }

    public FXFValidator getValidator() {
        return validator;
    }

    public void setValidator(FXFValidator validator) {
        this.validator = validator;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public AutoCompletionBinding<String> getAcbinding() {
        return acbinding;
    }

    public void setAcbinding(AutoCompletionBinding<String> acbinding) {
        this.acbinding = acbinding;
    }

    public ArrayList<String> getAcbindinglist() {
        return acbindinglist;
    }

    public void setAcbindinglist(ArrayList<String> acbindinglist) {
        this.acbindinglist = acbindinglist;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isAutocomplete_autoadd() {
        return autocomplete_autoadd;
    }

    public void setAutocomplete_autoadd(boolean autocomplete_autoadd) {
        this.autocomplete_autoadd = autocomplete_autoadd;
    }

    public Integer getAutocomplete_maxitem() {
        return autocomplete_maxitem;
    }

    public void setAutocomplete_maxitem(Integer autocomplete_maxitem) {
        this.autocomplete_maxitem = autocomplete_maxitem;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Object getCustom() {
        return custom;
    }

    public void setCustom(Object custom) {
        this.custom = custom;
    }

    public String getCustom_classtype() {
        return custom_classtype;
    }

    public void setCustom_classtype(String custom_classtype) {
        this.custom_classtype = custom_classtype;
    }

    public boolean isUseformula() {
        return useformula;
    }

    public void setUseformula(boolean useformula) {
        this.useformula = useformula;
    }

    public String getFormulafile() {
        return formulafile;
    }

    public void setFormulafile(String formulafile) {
        this.formulafile = formulafile;
    }

    public ArrayList<String> getFormulalist() {
        return formulalist;
    }

    public void setFormulalist(ArrayList<String> formulalist) {
        this.formulalist = formulalist;
    }

    
        
    
    
    
    
}
