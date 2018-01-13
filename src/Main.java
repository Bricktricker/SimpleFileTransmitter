
import FileSystem.FileStorage;
import java.io.IOException;
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
                        default:
                            System.err.println("Type not found");
                            continue;
                    }
                    
                    
                }
                server.stopServer();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
              
            try {
                Client client = new Client("192.168.178.44", 8080);
                Packet pack = new Packet(PacketTypes.CONNECT);
                client.sendData(pack);
                
                Packet ret = client.readData();
                System.out.println(ret.getType());
                
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
