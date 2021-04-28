/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlestripb;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.opus.syssupport.VirnaServiceProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author opus
 */
public class SerialDevice implements Serializable{

    private static final Logger LOG = Logger.getLogger(SerialDevice.class.getName());
    
    private int baudrate=115200;
    private int databits = 8;
    private int stopbits=SerialPort.ONE_STOP_BIT;
    private int parity=SerialPort.NO_PARITY;
    private int flowctrl=0;


    private transient OutputStream out;
    private transient InputStream in;

    private transient SerialPort serialPort;

    public static final int SERIAL_BUFFER_LENGHT = 2048;
    private byte[] buffer = new byte[SERIAL_BUFFER_LENGHT];
    private int bufferptr=0;

    
    private String portName;
    private boolean available = true;
    private String owner;
    private int portType;
    private int error;
    private boolean connected = false;

    private boolean blockMode=false;
    
    private int synclenght=0;

    private long messagetag = 0;
    private long lastsync = -1;
    private int synclag = 3;
  
    private boolean closerequest = false;
      
    /** Estrutura para armazenamento dos listeners do dispositivo*/ 
    private ArrayList<SerialDevice.SerialDeviceListener> listeners = new ArrayList();

    // Message State Machine
    public static enum SERIAL_STATE { 
            LISTENING,
            CMD_REQUEST,
            CMD_BUILDSMALL,
            CMD_BUILDHEAVY,
            CMD_BUILDBEACON,
            CMD_BUILDTICK,
            DONE
    };
    public SERIAL_STATE serialstate = SERIAL_STATE.LISTENING;
    
    private final int FLAGHEADER = 0xFF;
    private final int FLAGEND = 0XF0;
    
    private final int FLAGSMALL = 0xFE;
    private final int FLAGHEAVY = 0xFD;
    
    private final int FLAGBEACON = 0xFC;
    private final int FLAGTICK = 0xFA;
   
    private final int SMALLLENGTH = 3;
    private final int HEAVYLENGTH = 32;
    
    private VirnaServiceProvider ctrl;
    private SerialPort sport;
    
    private byte[] sbuf = new byte[64];
    
    public SerialDevice(SerialPort sport, Controller ctrl) {
        
        sbuf[0] = (byte)0xff;
        sbuf[1] = (byte)0xfe;
        sbuf[62] = (byte)0xff;
        sbuf[63] = (byte)0xf0;
        
        this.ctrl = ctrl;
        this.sport = sport;

        available=true;
        messagetag = 0;
        lastsync = -1;
        closerequest = false;
        bufferptr=0;
        
        if (sport.openPort()){
            this.setConnected(true);
            out = sport.getOutputStream();
            in = sport.getInputStream();
        }
        else{
            this.setConnected(false);
        }
    }
    
    
    public void enablePort (){
        
        getSport().addDataListener(new SerialPortDataListener() {
            
            int data; 
            byte mtype;
            
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            
            @Override
            public void serialEvent(SerialPortEvent event){
               
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
               
                int avail = event.getSerialPort().bytesAvailable();
                if (avail == 0 || in == null) {
                    return;
                }
                
               //byte[] newData = new byte[sport.bytesAvailable()];
               //int numRead = sport.readBytes(newData, newData.length);
               //LOG.info(String.format("Read  %d bytes.", numRead));
               
                try{               
                    do{
                        if (in.available() == 0) break;
//                        LOG.log(Level.INFO,"available = {0}" , in.available());
                        
                        switch (serialstate){
           
                            case LISTENING :
                                //bufferptr = 0; // inicie o buffer
                                while ((data = in.read()) > -1){
                                    // Se o token de dados é a flag de bloco parta para montar comando binário
                                    if ( data == FLAGHEADER) {
//                                        LOG.log(Level.INFO, "Achei delimitador : {0}", String.valueOf(data) );
                                        //Se há dados de Listening, livre-se deles
                                        if (bufferptr !=0){
                                            notifyTraffic(SerialDevice.Event.EVENT_TERM, messagetag, buffer, bufferptr);
                                            // Renove o buffer
                                            bufferptr = 0;
                                        }
                                        // Interrompa leitura da stream de entrada (essa deve ser executada agora em CMD_REQUEST)
                                        serialstate=SerialDevice.SERIAL_STATE.CMD_REQUEST;                               
                                        break;
                                    }
                                    // Stream normal de saida para o terminal, só adicione no buffer
                                    if ( data == '\n'&& bufferptr !=0) {
                                        buffer[bufferptr++] = (byte)data;  
                                        buffer[bufferptr++] = 0x00;  
                                        // Nova linha no terminal, empacote e envie a mensagem
                                        notifyTraffic(SerialDevice.Event.EVENT_TERM, messagetag, buffer, bufferptr);
                                        // Renove o buffer
                                        bufferptr = 0;
                                    }
                                    else if (data == '\r'){
                                        
                                    }
                                    else{
                                        buffer[bufferptr++] = (byte)data;  
                                    }
                                    LOG.log(Level.INFO, "Em Listening freerunning data={0} - available={1})", 
                                            new Object[]{data,in.available()});
                                }
                                break;
                                
                            case CMD_REQUEST :                               
                                //LOG.log(Level.INFO,"Em CMD_RQS (available = {0}" , in.available());
                                
                                data = in.read();
                                if (data == FLAGSMALL) {
                                    // Header de bloco de mensagem identificado - SMALL
                                    bufferptr = 0; // inicie o buffer
                                    serialstate=SerialDevice.SERIAL_STATE.CMD_BUILDSMALL;
                                }
                                else if (data == FLAGHEAVY){
                                    // Header de bloco de mensagem identificado - HEAVY
                                    bufferptr = 0; // inicie o buffer
                                    // Proximo byte é o identificador de bloco
                                    mtype =(byte)in.read();
                                    LOG.log(Level.INFO,"Heavy Message found type = {0}" , mtype);
                                    buffer[bufferptr++] = mtype;
                                    serialstate=SerialDevice.SERIAL_STATE.CMD_BUILDHEAVY;
                                }
                                else if (data == FLAGBEACON){
                                    // Header de bloco de mensagem identificado - ASVP BEACON
                                    bufferptr = 0; // inicie o buffer
                                    serialstate=SerialDevice.SERIAL_STATE.CMD_BUILDBEACON;
                                }
                                else if (data == FLAGTICK){
                                    // Header de bloco de mensagem identificado - ASVP BEACON
                                    bufferptr = 0; // inicie o buffer
                                    serialstate=SerialDevice.SERIAL_STATE.CMD_BUILDTICK;
                                }
                                else{
                                    //serialstate= SerialDevice.SERIAL_STATE.LISTENING;
                                    serialstate= SerialDevice.SERIAL_STATE.CMD_REQUEST;
                                    LOG.log(Level.INFO, String.format("CMD_REQUEST = %d / %s", data, Character.toString((char)data)));
                                }
                                break;
                            
                            case CMD_BUILDBEACON :
                                while ((data = in.read()) > -1){
//                                        LOG.log(Level.INFO,"Building beacon : available={0} - Data={1} / {2}" , 
//                                                new Object[] {in.available(), Character.toString((char)data), data});
                                        if (data == FLAGEND) {
                                            //serialstate=SerialDevice.SERIAL_STATE.DONE;
                                            serialstate=SerialDevice.SERIAL_STATE.LISTENING;
                                            LOG.log(Level.INFO,"OK ! - Enviando BUILBEACON {0}", buffer[0]); 
                                            // O bloco está completo, despache para o aplicativo
                                            in.skip(in.available());
                                            notifyTraffic(SerialDevice.Event.EVENT_BEACON, messagetag, buffer, bufferptr);
                                            bufferptr = 0;
                                        }
                                        else{
                                            buffer[bufferptr++] = (byte)data; 
                                        }
                                    break;
                                }
                                break;    
                            
                            case CMD_BUILDTICK :
                                while ((data = in.read()) > -1){
//                                        LOG.log(Level.INFO,"Building tick : available={0} - Data={1} / {2}" , 
//                                                new Object[] {in.available(), Character.toString((char)data), data});
                                        if (data == FLAGEND) {
                                            //serialstate=SerialDevice.SERIAL_STATE.DONE;
                                            serialstate=SerialDevice.SERIAL_STATE.LISTENING;
//                                            LOG.info(String.format("Message OK ! - Enviando BUILTICK @ %d", System.currentTimeMillis())); 
                                            // O bloco está completo, despache para o aplicativo
                                            in.skip(in.available());
                                            notifyTraffic(SerialDevice.Event.EVENT_TICK, messagetag, buffer, bufferptr);
                                            bufferptr = 0;
                                        }
                                        else{
                                            buffer[bufferptr++] = (byte)data; 
                                        }
                                    break;
                                }
                                break;      
                                
                            case CMD_BUILDSMALL :
                                while ((data = in.read()) > -1){
                                        LOG.log(Level.INFO,"Building small");    
//                                        LOG.log(Level.INFO,"Building small : available={0} - Data={1} / {2}" , 
//                                                new Object[] {in.available(), Character.toString((char)data), data})
                                        if (data == FLAGEND) {
                                            //serialstate=SerialDevice.SERIAL_STATE.DONE;
                                            serialstate=SerialDevice.SERIAL_STATE.LISTENING;
                                            //LOG.log(Level.INFO,"OK ! - Enviando comando {0}", buffer[0]); 
                                            // O bloco está completo, despache para o aplicativo
                                            in.skip(in.available());
                                            notifyTraffic(SerialDevice.Event.EVENT_CMDR, messagetag, buffer, bufferptr);
                                            bufferptr = 0;
                                        }
                                        else{
                                            buffer[bufferptr++] = (byte)data; 
                                        }
                                    break;
                                }
                                break;
                            
                            case CMD_BUILDHEAVY :
                                while ((data = in.read()) > -1){
                                    LOG.log(Level.INFO,"Building heavy : available={0} - Data={1}" , new Object[] {in.available(), data});
                                    buffer[bufferptr++] = (byte)data;
                                    if (bufferptr == HEAVYLENGTH) serialstate=SerialDevice.SERIAL_STATE.DONE;
                                    break;
                                }
                                break;
                            
                                
                            case DONE :
                                serialstate=SerialDevice.SERIAL_STATE.LISTENING;
                                LOG.log(Level.INFO,"OK ! - Enviando comando {0}", buffer[0]); 
                                // O bloco está completo, despache para o aplicativo
                                in.skip(in.available());
                                notifyTraffic(SerialDevice.Event.EVENT_CMDR, messagetag, buffer, bufferptr);
                                bufferptr = 0;
                                break;   
                        }                   
                    } while (in.available() !=0 ); //|| serialstate==SerialDevice.SERIAL_STATE.DONE);
                }
                
                
                catch ( IOException e ) {
                    LOG.log(Level.SEVERE,String.format("Serial Exception (%s) = %s", e.getClass().getName(), e.getMessage())); 
                }
            }
        });   
            
    }
    
    
    public void closePort(){
        
        sport.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        
        getSport().removeDataListener();
        getSport().closePort();
        
        setConnected(false);
        available=false;
        closerequest = false;
        listeners = new ArrayList();
            
    }
    
    
    
    // ================================================== SERVICES =======================================================
    public void sendMessage(byte[] load){
  
        for (int i = 0; i < load.length; i++) {
            sbuf[i+2] = load[i];
        }
        
        try{
            this.out.write(sbuf);
            this.out.flush();
        }
        catch ( IOException e ){
            e.printStackTrace();
        }
    }
    

    public void writeString(String tokens){

        if (isConnected()){
            try{
                this.out.write(tokens.getBytes());
            }
            catch ( IOException e ){
                e.printStackTrace();
            }
        }else{
            System.out.println("Porta não está conectada !");
        }
    }

    
    public void putc (int c){
        
        if (isConnected()){
            try{
                this.out.write(c);
            }
            catch ( IOException e ){
                e.printStackTrace();
            }
        }else{
            System.out.println("Porta não está conectada !");
        }
        
    }
    
    
    // ============================================== get set & status ==========================================================
    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * @param connected the connected to set
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * @return the blockMode
     */
    public boolean isBlockMode() {
        return blockMode;
    }

    /**
     * @param blockMode the blockMode to set
     */
    public void setBlockMode(boolean blockMode) {
        this.blockMode = blockMode;
    }

    /**
     * @return the lastsync
     */
    public long getLastsync() {
        return lastsync;
    }
    
    
    
    
    
    // ======================================================================
    // Tratamento e disparo de eventos ======================================

    
    public static class Event extends java.util.EventObject{

        public static final int EVENT_DATA   = 1;  // Data disponivel
        public static final int EVENT_OPEN   = 2;  // Porta abriu
        public static final int EVENT_CLOSE  = 3;  // Porta fechou
        public static final int EVENT_FAIL   = 4;  // Porta Falhou
        
        public static final int EVENT_TERM   = 5;  // Caracteres simples para o terminal
        public static final int EVENT_CMDR   = 6;  // Retorno de comando generico
        public static final int EVENT_BEACON = 7;  // Retorno de comando generico
        public static final int EVENT_TICK   = 8;  // Retorno de comando generico
        
        public int eventType;
        public long tag;
        public byte []buffer;
        public  int lenght;
        

        public Event(SerialDevice source, int eventType, long tag, byte[] buffer, int lenght){

            super(source);
            this.eventType=eventType;
            this.buffer=buffer;
            this.lenght=lenght;
            this.tag=tag;

        }

        public SerialDevice getDevice() { return (SerialDevice)getSource();}
        public int getEventType() { return eventType;}
        public long getTag() { return tag;}
        public byte[] getBuffer() {return buffer;}
        public int getLenght() { return lenght;}
    }

    /** Interface interna que deverá ser implementada por qualquer objeto
     * que deseja ser notificado quando houver movimento no dispositivo serial */
    public interface SerialDeviceListener extends java.util.EventListener {
        public void eventoSerial(SerialDevice.Event e);
    }

    /** Método de registro do listener do dispositivo serial */
    public void addSerialDeviceListener (SerialDevice.SerialDeviceListener l){
        listeners.add(l);
    }

    /** Método de remoção do registro do listener do dispositivo serial */
    public void removeSerialDeviceListener (SerialDevice.SerialDeviceListener l){
        listeners.remove(l);
    }

    /** Esse método é chamado quando algo acontece no dispositivo */
    public void notifyTraffic(int localType, long tag, byte[] localBuffer, int lenght) {

        if (!listeners.isEmpty()){
            // Crie um objeto de evento para descrever a seleção
            SerialDevice.Event e = new SerialDevice.Event(this, localType, tag, localBuffer, lenght);

            // Rode entre os listeners
            for (Iterator i=listeners.iterator(); i.hasNext();){
                SerialDevice.SerialDeviceListener l = (SerialDevice.SerialDeviceListener)i.next();
                l.eventoSerial(e); //Notifique cada listener
            }
        }
    }

    // Final do disparo de eventos --------------------------------------------------------

    public SerialPort getSport() {
        return sport;
    }

    public void setSport(SerialPort sport) {
        this.sport = sport;
    }
    
    
    
    
    
    
    
}
