/*
 * Copyright 2018 Philipp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Philipp
 */
public class Server implements java.io.Closeable{
    
    private final ServerSocket socket;
    private Socket userSocket;
    
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    
    public Server(int port) throws IOException{
        socket = new ServerSocket(port);
    }
    
    
    public void disconnect() throws IOException{
        outStream.close();
        inStream.close();
        userSocket.close();
    }
    
    
    public void stopServer(){
        try {
            close();
        } catch (IOException ex) { }
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
            try { disconnect(); } catch (IOException e) { }
        }catch (ClassNotFoundException ex){ }
        return null;
    }
    
    public boolean isConnected(){
        return !userSocket.isClosed();
    }

    @Override
    public void close() throws IOException {
        outStream.close();
        inStream.close();
        userSocket.close();
        socket.close();
    }
    
}
