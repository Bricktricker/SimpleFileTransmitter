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

import FileSystem.ChangeList;
import FileSystem.FileInfo;
import FileSystem.FileManager;
import FileSystem.FileStorage;
import FileSystem.FolderInfo;
import FileSystem.FolderWatcher;
import java.io.File;
import java.io.IOException;
import java.io.SyncFailedException;
import java.nio.file.*;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.util.List;
import networking.Client;
import networking.Packet;
import networking.PacketTypes;

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
            serverStorage.setFolder(storage.getFolder());
            List<FileInfo> changes = serverStorage.getChanges();
            updateServer(client, changes, storage);
                
        }catch(IOException e){
            System.err.println("Could not connect to Server");
            if(e.getCause() != null)
                System.err.println(e.getCause().toString());
            
            try { client.close(); } catch (IOException | NullPointerException x) {}
            System.exit(1);
        }
        
        System.out.println("initial ready");
        
        try{
        boolean isRunning = true;
        Path folder = storage.getFolder();
        FolderWatcher folderWatcher = new FolderWatcher(folder);
        
        
        while(isRunning){
            try{
                
                ChangeList<FileInfo> fileChanges = new ChangeList<>();
                ChangeList<FolderInfo> folderChanges = new ChangeList<>();
                WatchKey key = folderWatcher.getEvents();
                Path dir = folderWatcher.getPath(key);

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if(kind == OVERFLOW){
                        System.err.println("OVERFLOW");
                        continue;
                    }

                    WatchEvent<Path> ev = FolderWatcher.cast(event);
                    Path name = ev.context();
                    Path filename = dir.resolve(name);

                    if(kind == ENTRY_CREATE){
                        if (Files.isDirectory(filename, NOFOLLOW_LINKS)) {
                                folderWatcher.registerAll(filename, (oldPath, NewPath)->{
                                    FolderInfo info = new FolderInfo(NewPath.toString());
                                    info.setRenamed(true, oldPath.toString());
                                    folderChanges.add(info);
                                });
                            }

                        File file = filename.toFile();
                        if(file.isDirectory()){
                            FolderInfo info = new FolderInfo(filename.toString());
                            info.setAdded(true);
                            folderChanges.add(info);
                        }else{
                            FileInfo info = new FileInfo(filename.toString(), FileManager.getHash(file));
                            info.setAdded(true);
                            fileChanges.add(info);
                        }
                    }else if(kind == ENTRY_MODIFY){
                        File file = filename.toFile();
                        if(file.isFile()){
                            FileInfo info = new FileInfo(filename.toString(), FileManager.getHash(file));
                            fileChanges.add(info);
                        }
                    }else{
                        if(folderWatcher.isDirectoryRegisterd(filename)){
                            FolderInfo info = new FolderInfo(filename.toString());
                            info.setRemoved(true);
                            folderChanges.remove(info);
                        }else{
                            FileInfo info = new FileInfo(filename.toString(), null);
                            info.setRemoved(true);
                            fileChanges.remove(info);
                        }

                    }

                }//For end
                
                boolean valid = folderWatcher.checkKey(key);
                if(!valid){
                    throw new RuntimeException("Error in FileSystem");
                }
            
            //UPLOAD FILES
            }catch(IOException e){
                System.err.println("Error while sending data");
                if(e.getCause() != null)
                    System.err.println(e.getCause().toString());
            }
        }
        
        }catch(FileSystemException e){
            System.err.println("Filesystem error");
            if(e.getCause() != null)
                System.err.println(e.getCause().toString());
        }finally{
            try { client.close(); } catch (IOException x) {}
        }
    }
    
    public static void updateServer(Client client, List<FileInfo> changes, FileStorage storage) throws IOException{
        for(FileInfo info : changes){
            try{
                
            
            if(info.isRemoved()){
                Packet pack = new Packet(PacketTypes.SEND_FILE, info, (Object)null);
                client.sendData(pack);
            }else{
                Path path = Paths.get(storage.getFolder().toString() + "/" + info.getPath());
                byte[] data = Files.readAllBytes(path);
                
                Packet pack = new Packet(PacketTypes.SEND_FILE, info, data);
                client.sendData(pack);
            }
            Packet retPack = client.readData();
            if(retPack.getType() == PacketTypes.FILE_RECEIVED){
                String gotPath = (String) retPack.get(0);
                if(!gotPath.equals(info.getPath())){
                    System.err.println("Error sending file");
                }
            }
            
            }catch(NoSuchFileException e){
                System.err.println("File " + info.getPath() + " not found");
                e.printStackTrace();
            }
        }
        
        Packet pack = new Packet(PacketTypes.ALL_FILES_SEND);
        client.sendData(pack);
    }
    
}
