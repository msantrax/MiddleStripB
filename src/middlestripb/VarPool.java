/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;

/**
 *
 * @author opus
 */
public class VarPool {

    protected LinkedHashMap<String, VarInfo> vars;
    protected ArrayDeque<String> stack;
    
    

    
    public VarPool() {
        vars = new LinkedHashMap<>();
        stack = new ArrayDeque<>();
    }
    
    
    public void Push(String key, VarInfo vi){
        getVars().put(key, vi);
        getStack().push(key);
    }
    
    public void Push(String key, Object obj){
        VarInfo vi = new VarInfo(key, obj.getClass().getSimpleName());
        vi.payld = obj;
        getVars().put(key, vi);
        getStack().push(key);
    }
    
    
    public <T> T Pop(String key){
        
        if (getVars().containsKey(key)){
            Object obj = getVars().get(key).payld;
            getVars().remove(key);
            getStack().remove(obj);
            return (T) obj;
        }
        else{
            return (T)new Object();
        }
    }
    
    public Object Pop(){
        
        if (getStack().isEmpty()) return null;
        
        String sobj = getStack().pop();
        Object obj = getVars().get(sobj).payld;
        getVars().remove(sobj);
        return obj;
    }
    
    
    public <T> T Peek(String key){
        
        if (getVars().containsKey(key)){
            Object obj = getVars().get(key).payld;
            return (T) obj;
        }
        else{
            return null;
        }
    }
    
    
    public String SPop(String key){
        
        if (getVars().containsKey(key)){
            VarInfo vi = getVars().get(key);
            getVars().remove(key);
            return String.format(vi.format, vi.payld);
        }
        else{
            return "&Undef";
        }
    }
    
    public String SPeek(String key){
        
        if (getVars().containsKey(key)){
            VarInfo vi = getVars().get(key);
            return String.format(vi.format, vi.payld);
        }
        else{
            return "&Undef";
        }
    }

    public LinkedHashMap<String, VarInfo> getVars() {
        return vars;
    }

    public void setVars(LinkedHashMap<String, VarInfo> vars) {
        this.vars = vars;
    }

    public ArrayDeque<String> getStack() {
        return stack;
    }

    public void setStack(ArrayDeque<String> stack) {
        this.stack = stack;
    }

    
    
    public class VarInfo{
        
        public Object payld;
        public Class type;
        public String format;
        public Object vdefault;
        
        
        public VarInfo(Object payld, Class type, String format, Object vdefault) {
            this.payld = payld;
            this.type = type;
            this.format = format;
            this.vdefault = vdefault;
        }
        
        
        public VarInfo(Object payld, String stype) {
            this.payld = payld;
            
            switch (stype){
                case "String":
                    this.type = String.class;
                    this.format = "%s";
                    this.vdefault = "";
                    break;
                case "Double":
                    this.type = Double.class;
                    this.format = "%6.2f";
                    this.vdefault = 0.0;
                    break;
                case "DoubleNF":
                    this.type = Double.class;
                    this.format = "%f";
                    this.vdefault = 0.0;
                    break;    
                case "Long":
                    this.type = Long.class;
                    this.format = "%d";
                    this.vdefault = 0L;
                    break;
                case "Integer":
                    this.type = Integer.class;
                    this.format = "%d";
                    this.vdefault = 0;
                    break;
                    
                default:
                    this.type = String.class;
                    this.format = "%s";
                    this.vdefault = "";
                    break;    
            }
            
        }
        
        
    }
    
}
