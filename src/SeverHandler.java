
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

import java.io.IOException;
import FileSystem.FileInfo;
import FileSystem.FileManager;
import FileSystem.FileStorage;
import FileSystem.FolderInfo;
import Utils.NetworkingException;
import networking.Packet;
import networking.PacketTypes;
import networking.Server;

/**
 * handles the server and incoming packets
 * 
 * @author Philipp
 */
public class SeverHandler {

	public static int port = 8636;
	private static Server server;

	public static void handleServer() throws IOException {
		while (true) {
			FileStorage storage = FileManager.createFileStorage();
			try {
				server = new Server(port);
				server.waitForUser();
			} catch (NetworkingException e) {
				String errStr = "Error creating server";
				if (e.getCause() != null)
					errStr += " " + e.getMessage();

				throw new NetworkingException(errStr);
			}

			while (server.isConnected()) {
				try {
					Packet pack = server.getData();
					if (pack == null) {
						continue;
					}

					PacketTypes type = pack.getType();
					switch (type) {
					case CONNECT:
						Packet p = new Packet(PacketTypes.CONNECTED);
						server.sendData(p);
						break;
					case GET_STATUS:
						storage.updateStorage();
						Packet treePack = new Packet(PacketTypes.SEND_TREE, storage);
						server.sendData(treePack);
						break;
					case FOLDER_CHANGE:
						FolderInfo folderInfo = (FolderInfo) pack.get(0);
						FileManager.handleFolderInput(folderInfo);
						server.sendData(new Packet(PacketTypes.PACKET_RECEIVED));
						break;
					case SEND_FILE:
						FileInfo info = (FileInfo) pack.get(0);
						byte[] fileData = (byte[]) pack.get(1);
						try {
							FileManager.handleFileInput(info, fileData);
						} catch (IOException e) {
							Packet errPack = new Packet(PacketTypes.ERROR, e.getMessage());
							server.sendData(errPack);
							break;
						}
						Packet recvPack = new Packet(PacketTypes.PACKET_RECEIVED, info.getPath());
						server.sendData(recvPack);
						break;
					case ALL_PACK_SEND:
						storage.updateStorage();
						break;
					case KEEP_ALIVE:
						break;
					default:
						System.err.println("Type " + type + " not supported");
						continue;
					}//end switch
				
				//try after while connected
				}catch (NetworkingException e) {
					String errStr = "Error in server";
					if (e.getCause() != null)
						errStr = e.getMessage();

					throw new NetworkingException(errStr);
				}
			}//while connected
		}//endless loop
	}

}
