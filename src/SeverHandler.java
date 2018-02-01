
import FileSystem.FileInfo;
import FileSystem.FileManager;
import FileSystem.FileStorage;
import networking.Packet;
import networking.PacketTypes;
import networking.Server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Philipp
 */
public class SeverHandler {

	public static int port = 8636;

	public static void handleServer(FileStorage storage) {
		Server server = new Server(port);
		server.waitForUser();

		while (server.isConnected()) {
			Packet pack = (Packet) server.getData();
			if (pack == null) {
				continue;
			}

			PacketTypes type = pack.getType();
			switch (type) {
			case CONNECT:
				Packet p = new Packet(PacketTypes.CONNECTED);
				server.sendData(p);
				break;
			case CONNECTED:
				System.err.println("Got packet of type CONNECTED, should not happen!");
				break;
			case GET_TREE:
				storage.getChanges();
				Packet treePack = new Packet(PacketTypes.SEND_TREE, storage);
				server.sendData(treePack);
				break;
			case SEND_FILE:
				FileInfo info = (FileInfo) pack.get(0);
				byte[] fileData = (byte[]) pack.get(1);
				FileManager.handleFileInput(info, fileData);

				Packet recvPack = new Packet(PacketTypes.PACKET_RECEIVED, info.getPath());
				server.sendData(recvPack);
				break;
			case ALL_PACK_SEND:
				storage.getChanges();
				break;
			case KEEP_ALIVE:
				break;
			default:
				System.err.println("Type " + type + " not found");
				continue;
			}

		}
		server.stopServer();
	}

}
