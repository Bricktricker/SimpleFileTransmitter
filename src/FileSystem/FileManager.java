package FileSystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {

	public static String workingDir = "";
	
	public static FileStorage createFileStorage() {
		FileStorage storage;
		if(workingDir.isEmpty()) {
			storage = new FileStorage();
		}else {
			storage = new FileStorage(workingDir);
		}
		
		storage.fillMap();
		return storage;
	}
        
        public static void writeFile(String path, byte[] fileData){
            try {
                FileOutputStream fos = new FileOutputStream(workingDir + path);
                fos.write(fileData);
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
