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
package FileSystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

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
        
        public static FileStorage createEmptyStorage() {
		FileStorage storage;
		if(workingDir.isEmpty()) {
			storage = new FileStorage();
		}else {
			storage = new FileStorage(workingDir);
		}
		
		return storage;
	}
        
        public static void handleFileInput(FileInfo info, byte[] fileData){
            if(info.isRemoved()){
                try {
                    Files.delete(Paths.get(workingDir + "/" + info.getPath()));
                } catch (NoSuchFileException ex) {
                    System.err.format("%s: no such" + " file or directory%n", workingDir + "/" + info.getPath());
                } catch (DirectoryNotEmptyException ex) {
                    System.err.format("%s not empty%n", workingDir + "/" + info.getPath());
                } catch (IOException ex) {
                    System.err.println("Not allowed to write to " + workingDir + "/" + info.getPath());
                }
            }else{
                writeFile(info.getPath(), fileData);
            }
        }
        
        public static void createFolder(FileInfo info){
            Paths.get(workingDir + "/" + info.getPath()).toFile().mkdirs();
        }
        
        private static void writeFile(String path, byte[] fileData){
            try {
                FileOutputStream fos = new FileOutputStream(workingDir + "/" + path);
                fos.write(fileData);
                fos.flush();
                fos.close();
            }catch (IOException ex) {
                System.err.println("Error writing file " + workingDir + "/" + path);
            }
        }
        
        //generate hash from file
	public static String getHash(final File file) throws FileSystemException {
	    try{
                
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");

	    try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
	      final byte[] buffer = new byte[1024];
	      for (int read = 0; (read = is.read(buffer)) != -1;) {
	        messageDigest.update(buffer, 0, read);
	      }
	    }catch(IOException e){
                throw new FileSystemException(null);
            }

	    // Convert the byte to hex format
	    try (Formatter formatter = new Formatter()) {
	      for (final byte b : messageDigest.digest()) {
	        formatter.format("%02x", b);
	      }
	      return formatter.toString();
	    }
            }catch(NoSuchAlgorithmException e){
                return "";
            }
	}
}
