/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philipp
 */
public class Client implements java.io.Closeable{
    
    private final Socket socket;
    private final ObjectOutputStream outStream;
    private final ObjectInputStream inStream;
    
    public Client(String host, int port, int timeOut) throws IOException{
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), timeOut);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }
    
    public void disconnect(){
        try {
            outStream.close();
            inStream.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendData(Packet o) throws IOException{
        try{
            outStream.writeObject(o);
            outStream.flush(); 
        }catch(IOException e){
            throw new IOException("Error sending Packet");
        }
        
    }
    
    public Packet readData(){
        try {
            Object o = inStream.readObject();
            if(o instanceof Packet){
                return (Packet)o;
            }
        } catch (IOException | ClassNotFoundException ex) {
            //Logger.getLogger(Server.class.getName()).log(Level.WARNING, null, ex);
        }
        return null;
    }
    
    public boolean isConnected(){
        return !socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        outStream.close();
        inStream.close();
        socket.close();
    }
    
}
