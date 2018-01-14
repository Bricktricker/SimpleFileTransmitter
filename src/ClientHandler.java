
import FileSystem.FileInfo;
import FileSystem.FileStorage;
import it.sauronsoftware.cron4j.Scheduler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import networking.Client;
import networking.Packet;
import networking.PacketTypes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Philipp
 */
public class ClientHandler {
    
    public static int port = 8080;
    public static String serverAddress = "";
    public static int timeOut = 5000;
    
    public static Client client;
    public static Scheduler s;
    
    public static void handleClient(FileStorage storage) throws IOException{
        client = new Client(serverAddress, port, timeOut);
        
        //Connect
        {
            Packet pack = new Packet(PacketTypes.CONNECT);
            client.sendData(pack);       
            Packet ret = client.readData();
            if(ret.getType() != PacketTypes.CONNECTED){
                System.err.println("Error connecting to Server");
            }
        }
        
        //Update the Server Files
        {
            Packet pack = new Packet(PacketTypes.GET_TREE);
            client.sendData(pack);
            Packet retPack = client.readData();
            FileStorage serverStorage = (FileStorage) retPack.get(0);
            List<FileInfo> changes = serverStorage.update();
            updateServer(client, changes, storage);
        }
        
        s = new Scheduler();
        s.schedule("*/15 * * * *", new Runnable(){
            @Override
            public void run() {
                try {
                    List<FileInfo> changes = storage.update();
                    updateServer(client, changes, storage);
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        s.start();
               
    }
    
    public static void updateServer(Client client, List<FileInfo> changes, FileStorage storage) throws IOException{
        for(FileInfo info : changes){
            if(info.isRemoved()){
                Packet pack = new Packet(PacketTypes.SEND_FILE, info, (Object)null);
                client.sendData(pack);
            }else{
                Path path = Paths.get(info.getFilePath());
                byte[] data = Files.readAllBytes(path);
                
                Packet pack = new Packet(PacketTypes.SEND_FILE, info, data);
                client.sendData(pack);
            }
            Packet retPack = client.readData();
            if(retPack.getType() == PacketTypes.FILE_RECEIVED){
                String gotPath = (String) retPack.get(0);
                if(!gotPath.equals(info.getFilePath())){
                    System.err.println("Error sending file");
                }
            }
        }
        
        Packet pack = new Packet(PacketTypes.ALL_FILES_SEND);
        client.sendData(pack);
    }
    
}
