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

import Utils.NetworkingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author Philipp
 */
public class Client implements java.io.Closeable{
    
    private Socket socket;
    private final ObjectOutputStream outStream;
    private final ObjectInputStream inStream;
    
    public Client(String host, int port, int timeOut) throws NetworkingException{
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeOut);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            try{ if(socket != null){ socket.close(); } }catch(IOException e) {}
            throw new NetworkingException();
        }
    }
    
    public void sendData(Packet o) throws NetworkingException{
        try{
            outStream.writeObject(o);
            outStream.flush(); 
        }catch(IOException e){
            throw new NetworkingException("Error sending Packet");
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
