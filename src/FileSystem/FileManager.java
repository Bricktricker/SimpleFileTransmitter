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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Static class for basic file handling
 * @author Philipp
 *
 */
public class FileManager {

	public static Path workingDir = Paths.get("").toAbsolutePath();

	public static FileStorage createFileStorage() {
		FileStorage storage = new FileStorage();
		storage.updateStorage();
		return storage;
	}
	
	/**
	 * gets path of the file relative to the project folder
	 * @param absolute path to file
	 * @return relative path to file
	 */
	public static Path getRelPath(Path filepath) {
		return workingDir.relativize(filepath);
	}

	public static FileStorage createEmptyStorage() {
		return new FileStorage();
	}

	public static void handleFileInput(FileInfo info, byte[] fileData) {
		if (info.isRemoved()) {
			try {
				Files.delete(workingDir.resolve(info.getPath()));
			} catch (NoSuchFileException ex) {
				System.err.format("%s: no such" + " file or directory%n", workingDir.resolve(info.getPath()));
			} catch (DirectoryNotEmptyException ex) {
				System.err.format("%s not empty%n", workingDir.resolve(info.getPath()));
			} catch (IOException ex) {
				System.err.println("Not allowed to write to " + workingDir.resolve(info.getPath()));
			}
		} else {
			writeFile(info.getPath(), fileData);
		}
	}

	/**
	 * creates folder at path location
	 * @param relative path to new folder
	 */
	public static void createFolder(Path path) {
		workingDir.resolve(path).toFile().mkdirs();
	}

	/**
	 * writes file to disc
	 * @param realtive path to written file
	 * @param fileData data of the file
	 */
	private static void writeFile(String path, byte[] fileData) {
		try {
			FileOutputStream fos = new FileOutputStream(workingDir.resolve(path).toString());
			fos.write(fileData);
			fos.flush();
			fos.close();
		} catch (IOException ex) {
			System.err.println("Error writing file " + workingDir + "/" + path);
		}
	}

	/**
	 * generates hash from file
	 * @param file
	 * @return generated MD5 hash of file
	 * @throws FileSystemException
	 */
	public static String getHash(final File file) throws FileSystemException {
		try {

			final MessageDigest messageDigest = MessageDigest.getInstance("MD5");

			try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
				final byte[] buffer = new byte[1024];
				for (int read = 0; (read = is.read(buffer)) != -1;) {
					messageDigest.update(buffer, 0, read);
				}
			} catch (IOException e) {
				throw new FileSystemException(file.getPath());
			}

			// Convert the byte to hex format
			try (Formatter formatter = new Formatter()) {
				for (final byte b : messageDigest.digest()) {
					formatter.format("%02x", b);
				}
				return formatter.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}
}
