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
package Utils;

import FileSystem.ChangeList;
import FileSystem.FileInfo;
import FileSystem.FileManager;
import FileSystem.FileStorage;
import FileSystem.FolderInfo;
import FileSystem.FolderWatcher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;

/**
 *
 * @author Philipp
 */
public class FileTester {
    
    public static void main(String[] args) throws IOException{
        FileManager.workingDir = "D:\\Eigene Dateien\\Bilder\\";        
        //Übergeben an Funktion
        FileStorage storage = FileManager.createFileStorage();
        
        //Vor der loop
        Path folder = storage.getFolder();
        FolderWatcher folderWatcher = new FolderWatcher(folder);
        while(true){
            ChangeList<FileInfo> fileChanges = new ChangeList<>();
            ChangeList<FolderInfo> folderChanges = new ChangeList<>();
            WatchKey key = folderWatcher.getEvents();
            Path dir = folderWatcher.getPath(key);

            for (WatchEvent<?> event : key.pollEvents()) {
                Kind<?> kind = event.kind();
                
                if(kind == OVERFLOW){
                    System.err.println("OVERFLOW");
                    continue;
                }
                
                WatchEvent<Path> ev = FolderWatcher.cast(event);
                Path name = ev.context();
                Path filename = dir.resolve(name);
                
                if(kind == ENTRY_CREATE){
                    if (Files.isDirectory(filename, NOFOLLOW_LINKS)) {
                            folderWatcher.registerAll(filename);
                        }
                    
                    File file = filename.toFile();
                    if(file.isDirectory()){
                        FolderInfo info = new FolderInfo(filename.toString());
                        info.setAdded(true);
                        folderChanges.add(info);
                        folderWatcher.addFolderToWatch(filename);
                    }else{
                        FileInfo info = new FileInfo(filename.toString(), FileManager.getHash(file));
                        info.setAdded(true);
                        fileChanges.add(info);
                    }
                }else if(kind == ENTRY_MODIFY){
                    File file = filename.toFile();
                    if(file.isDirectory()){
                        /*
                        FolderInfo info = new FolderInfo(filename.toString());
                        folderChanges.add(info);
                        folderWatcher.addFolderToWatch(filename);
                        */
                    }else{
                        FileInfo info = new FileInfo(filename.toString(), FileManager.getHash(file));
                        fileChanges.add(info);
                    }
                }else{
                    File file = filename.toFile();
                    if(file.isDirectory()){
                        FolderInfo info = new FolderInfo(filename.toString());
                        info.setRemoved(true);
                        folderChanges.remove(info);
                    }else{
                        FileInfo info = new FileInfo(filename.toString(), null);
                        info.setRemoved(true);
                        fileChanges.remove(info);
                    }
                }
                
            }
            boolean valid = folderWatcher.checkKey(key);
            if(!valid){
                throw new RuntimeException("Error in FileSystem");
            }
            
            //UPLOAD FILES
            if(fileChanges.size() > 0 || folderChanges.size() > 0){
                
            }
        }
    }
}
