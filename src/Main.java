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

import FileSystem.FileManager;
import FileSystem.FileStorage;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class Main {
    
        private enum AppType {SERVER, CLIENT, UNKNOWN}

	public static void main(String[] args) {
            AppType appType = AppType.UNKNOWN;

            for(int i = 0; i < args.length; i++){
                String s = args[i];
                
                
                if(s.startsWith("-") || s.startsWith("/")){
                    if(s.substring(1).equalsIgnoreCase("server")){
                        appType = AppType.SERVER;
                        continue;
                    }
                    if(s.substring(1).equalsIgnoreCase("client")){
                        appType = AppType.CLIENT;
                        continue;
                    }
                    if(s.substring(1).equalsIgnoreCase("folder")){
                        try{
                           Paths.get(args[i+1]);
                           FileManager.workingDir = args[i+1];
                        }catch(InvalidPathException ex){
                            System.err.println("Path could not be resolved");
                            System.exit(1);
                        }
                        i++;
                        continue;
                    }
                    if(s.substring(1).equalsIgnoreCase("port")){
                        SeverHandler.port = Integer.parseInt(args[i+1]);
                        ClientHandler.port = SeverHandler.port;
                        i++;
                        continue;
                    }
                    if(s.substring(1).equalsIgnoreCase("ip")){
                        if(appType == AppType.SERVER){
                            System.err.println("-ip will be ignored on Server");
                            continue;
                        }
                        ClientHandler.serverAddress = args[i+1];
                        i++;
                        continue;
                    }
                }

            }
            
            if(appType == AppType.UNKNOWN){
                System.err.println("No type specivied \n Use either -server or -client");
                System.exit(1);
            }
            
            FileStorage origStorage = FileManager.createFileStorage();
            
            try {
                if(appType == AppType.SERVER){
                    SeverHandler.handleServer(origStorage);
                }else{
                    ClientHandler.handleClient(origStorage);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
 	
	}
}
