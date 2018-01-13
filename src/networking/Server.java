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
    
    public void waitForUser() throws IOException{
        userSocket = socket.accept();
        outStream = new ObjectOutputStream(userSocket.getOutputStream());
        inStream = new ObjectInputStream(userSocket.getInputStream());
    }
    
    public void sendData(Object data) throws IOException{
        outStream.writeObject(data);
        outStream.flush();
    }
    
    public Object getData(){
        try {
            Object obj = inStream.readObject();
            return obj;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.WARNING, null, ex);
            return null;
        }
    }
    
}
