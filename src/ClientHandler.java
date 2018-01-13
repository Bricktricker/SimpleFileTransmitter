
import FileSystem.FileStorage;
import java.io.IOException;
import networking.Client;
import networking.Packet;
import networking.PacketTypes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Philipp
 */
public class ClientHandler {
    
    public static void handleClient(FileStorage storage) throws IOException{
        Client client = new Client("192.168.178.44", 8080);
        Packet pack = new Packet(PacketTypes.GET_TREE);
        client.sendData(pack);
                
        Packet ret = client.readData();
        System.out.println(ret.getType());
        FileStorage files = (FileStorage) ret.get(0);
        System.out.println(files.toString());
                
        client.disconnect();
    }
    
}
