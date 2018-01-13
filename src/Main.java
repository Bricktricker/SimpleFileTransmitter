
import FileSystem.FileManager;
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
		FileStorage origStorage = FileManager.createFileStorage();
		
            
                /*
                List<FileInfo> changes = origStorage.update();
                System.out.println(changes.size());
                for(FileInfo info : changes) {
                System.out.println(info.getFilePath());
                }
                */
                
		
            try {
               SeverHandler.handleServer(origStorage);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            try {
               ClientHandler.handleClient(origStorage);
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
