
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
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                //ClientHandler.client.disconnect();
            }
 	
	}
}
