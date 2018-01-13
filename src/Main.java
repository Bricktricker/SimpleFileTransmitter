import java.util.List;

import FileSystem.FileInfo;
import FileSystem.FileStorage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import networking.Client;
import networking.Server;

public class Main {

	public static void main(String[] args) {
		FileStorage origStorage = new FileStorage();
		origStorage.fillMap();
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
		
            
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
                
                Object o = server.getData();
                if(o != null){
                    System.out.println((String)o);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                Client client = new Client("localhost", 8080);
                client.sendData("Hello World");
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
                
		
		System.out.println("finished");
	}
}
