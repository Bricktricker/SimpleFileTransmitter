
import FileSystem.FileManager;
import FileSystem.FileStorage;
import java.io.IOException;
import networking.Packet;
import networking.PacketTypes;
import networking.Server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Philipp
 */
public class SeverHandler {
    
    public static int port = 8080;
    
    public static void handleServer(FileStorage storage) throws IOException{
        Server server = new Server(port);
                server.waitForUser();
                
                while(server.isConnected()){
                    Packet pack = (Packet) server.getData();
                    if(pack == null){
                        continue;
                    }
                    
                    PacketTypes type = pack.getType();
                    switch (type) {
                        case CONNECT:
                            Packet p = new Packet(PacketTypes.CONNECTED);
                            server.sendData(p);
                            break;
                        case CONNECTED:
                            System.err.println("Got packet of type CONNECTED, should not happen!");
                            break;
                        case GET_TREE:
                            storage.update();
                            Packet treePack = new Packet(PacketTypes.SEND_TREE, storage);
                            server.sendData(treePack);
                            break;
                        case SEND_FILE:
                            String path = (String) pack.get(0);
                            byte[] fileData = (byte[]) pack.get(1);
                            FileManager.writeFile(path, fileData);
                            
                            Packet recvPack = new Packet(PacketTypes.FILE_RECEIVED, path);
                            server.sendData(recvPack);
                            
                            storage.update();
                            break;
                        case KEEP_ALIVE:
                            break;
                        default:
                            System.err.println("Type " + type + " not found");
                            continue;
                    }
                    
                    
                }
                server.stopServer();
    }
    
}
