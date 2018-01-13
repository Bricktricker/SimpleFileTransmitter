/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philipp
 */
public class Server {
    
    private ServerSocket socket;
    private Socket userSocket;
    private int port;
    
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    
    public Server(int port) throws IOException{
        this.port = port;
        socket = new ServerSocket(port);
    }
    
    public void disconnect(){
        try {
            outStream.close();
            inStream.close();
            userSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopServer(){
        disconnect();
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void waitForUser() throws IOException{
        userSocket = socket.accept();
        System.out.println("use connected: " + userSocket.toString());
        outStream = new ObjectOutputStream(userSocket.getOutputStream());
        inStream = new ObjectInputStream(userSocket.getInputStream());
    }
    
    public void sendData(Packet data) throws IOException{
        outStream.writeObject(data);
        outStream.flush();
    }
    
    public Packet getData(){
        try {
            Object o = inStream.readObject();
            if(o instanceof Packet){
                return (Packet)o;
            }
        } catch (IOException ex) {
            disconnect();
        }catch (ClassNotFoundException ex){
            Logger.getLogger(Server.class.getName()).log(Level.WARNING, null, ex);
        }
        return null;
    }
    
    public boolean isConnected(){
        return !userSocket.isClosed();
    }
    
}
