
import FileSystem.FileInfo;
import FileSystem.FileStorage;
import java.io.IOException;
import java.io.SyncFailedException;
import java.nio.file.*;
import java.util.List;
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
    
    private static Client client;
    
    public static void handleClient(FileStorage storage){
        //Connect and initial update
        try{
            //Connect
            client = new Client(serverAddress, port, timeOut);
            Packet pack = new Packet(PacketTypes.CONNECT);
            client.sendData(pack);       
            Packet ret = client.readData();
            if(ret.getType() != PacketTypes.CONNECTED){
                throw new SyncFailedException("Server answerd not properly");
            }
            
            //Inital update
            Packet treePack = new Packet(PacketTypes.GET_TREE);
            client.sendData(treePack);
            Packet retPack = client.readData();
            FileStorage serverStorage = (FileStorage) retPack.get(0);
            List<FileInfo> changes = serverStorage.update();
            updateServer(client, changes, storage);
                
        }catch(IOException e){
            System.err.println("Could not connect to Server");
            if(e.getCause() != null)
                System.err.println(e.getCause().toString());
            
            try { client.close(); } catch (IOException | NullPointerException x) {}
            System.exit(1);
        }
        
        
        try{
        boolean isRunning = true;
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path folder = storage.getFolder();
        WatchKey watchKey = folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        while(isRunning){
            try{
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    System.out.println(event.kind());
                    Path file = folder.resolve((Path) event.context());
                    System.out.println(file + " was last modified at " + file.toFile().lastModified());
                }
                
                
                
                
                //Update the Server Files
                Packet pack = new Packet(PacketTypes.GET_TREE);
                client.sendData(pack);
                Packet retPack = client.readData();
                FileStorage serverStorage = (FileStorage) retPack.get(0);
                List<FileInfo> changes = serverStorage.update();
                updateServer(client, changes, storage);
                
                /*
                try {
                    List<FileInfo> changes = storage.update();
                    updateServer(client, changes, storage);
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
}
                */
                
            }catch(IOException e){
                System.err.println("Error while sending data");
                if(e.getCause() != null)
                    System.err.println(e.getCause().toString());
            }
            Thread.sleep(60000);
        }
        
        }catch (InterruptedException ex) {
            System.err.println("Thread error");
            System.err.println(ex.getCause().toString());
            System.exit(1);
        }catch(IOException e){
            System.err.println("Filesystem error");
            if(e.getCause() != null)
                System.err.println(e.getCause().toString());
        }finally{
            try { client.close(); } catch (IOException x) {}
        }
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
