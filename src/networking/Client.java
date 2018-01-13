/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Philipp
 */
public class Client {
    
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    
    public Client(String host, int port) throws IOException{
        socket = new Socket(host, port);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }
    
    public void sendData(Object o) throws IOException{
        outStream.writeObject(o);
        outStream.flush();
    }
    
}
