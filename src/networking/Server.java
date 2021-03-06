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
package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Utils.NetworkingException;

/**
 * basic server implementation for sending packages
 * @author Philipp
 */
public class Server implements java.io.Closeable, AutoCloseable {

	private final ServerSocket socket;
	private Socket userSocket;

	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;

	/**
	 * constructs a server Object at specified port
	 * @param port to listen for communication
	 * @throws NetworkingException if not able to bind the server to port
	 */
	public Server(int port) throws NetworkingException {
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new NetworkingException("Error creating ServerSocket");
		}
	}

	/**
	 * disconnects the server from the current user
	 * @throws NetworkingException if not able to disconnect
	 */
	public void disconnect() throws NetworkingException {
		try {
			outStream.close();
			inStream.close();
			userSocket.close();
		} catch (IOException e) {
			throw new NetworkingException("Error disconnecting client");
		}
	}

	public void waitForUser() throws NetworkingException {
		try {
			userSocket = socket.accept();
			System.out.println("user connected: " + userSocket.toString());
			outStream = new ObjectOutputStream(userSocket.getOutputStream());
			inStream = new ObjectInputStream(userSocket.getInputStream());
		} catch (IOException e) {
			if (e.getMessage() != null)
				throw new NetworkingException(e.getMessage());

			throw new NetworkingException("Error while user connected");
		}
	}

	public void sendData(Packet data) throws NetworkingException {
		try {
			outStream.writeObject(data);
			outStream.flush();
		} catch (IOException e) {
			if (e.getMessage() != null)
				throw new NetworkingException(e.getMessage());

			throw new NetworkingException("Error while user connected");
		}
	}

	public Packet getData() {
		try {
			Object o = inStream.readObject();
			if (o instanceof Packet) {
				return (Packet) o;
			}else {
				System.err.println("Unknown Object received");
				return null;
			}
		} catch (IOException ex) {
			try {
				disconnect();
			} catch (NetworkingException e) {
			}
		} catch (ClassNotFoundException ex) {
		}
		return null;
	}

	public boolean isConnected() {
		return !userSocket.isClosed();
	}

	@Override
	public void close() throws IOException {
		outStream.close();
		inStream.close();
		userSocket.close();
		socket.close();
	}

}
