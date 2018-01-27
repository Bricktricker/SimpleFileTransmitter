package FileSystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
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
		
		storage.updateStorage();
		return storage;
	}
        
        public static void handleFileInput(FileInfo info, byte[] fileData){
            if(info.isRemoved()){
                try {
                    Files.delete(Paths.get(workingDir + "/" + info.getFilePath()));
                } catch (NoSuchFileException ex) {
                    System.err.format("%s: no such" + " file or directory%n", workingDir + info.getFilePath());
                } catch (DirectoryNotEmptyException ex) {
                    System.err.format("%s not empty%n", workingDir + info.getFilePath());
                } catch (IOException ex) {
                    // File permission problems are caught here.
                    ex.printStackTrace();
                }
            }else{
                writeFile(info.getFilePath(), fileData);
            }
        }
        
        private static void writeFile(String path, byte[] fileData){
            try {
                FileOutputStream fos = new FileOutputStream(workingDir + "/" + path);
                fos.write(fileData);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
