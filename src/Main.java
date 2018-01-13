
import FileSystem.FileStorage;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import networking.Client;
import networking.Packet;
import networking.PacketTypes;
import networking.Server;

public class Main {

	public static void main(String[] args) {
		FileStorage origStorage = new FileStorage();
		origStorage.fillMap();
		
            
                /*
                List<FileInfo> changes = origStorage.update();
                System.out.println(changes.size());
                for(FileInfo info : changes) {
                System.out.println(info.getFilePath());
                }
                */
                
		
            try {
                Server server = new Server(8080);
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
                            origStorage.update();
                            Packet treePack = new Packet(PacketTypes.SEND_TREE, origStorage);
                            server.sendData(treePack);
                            break;
                        default:
                            System.err.println("Type " + type + " not found");
                            continue;
                    }
                    
                    
                }
                server.stopServer();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            try {
                Client client = new Client("192.168.178.44", 8080);
                Packet pack = new Packet(PacketTypes.GET_TREE);
                client.sendData(pack);
                
                Packet ret = client.readData();
                System.out.println(ret.getType());
                FileStorage files = (FileStorage) ret.get(0);
                System.out.println(files.toString());
                
                client.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            try {
		Thread.sleep(2000);
            } catch (InterruptedException e) {
		e.printStackTrace();
            }
               
		
		System.out.println("finished");
	}
}
