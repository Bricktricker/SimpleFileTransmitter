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
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Static class for basic file handling
 * 
 * @author Philipp
 *
 */
public class FileManager {

	public static Path workingDir = Paths.get("").toAbsolutePath();

	/**
	 * returns a new initialized FileStorage
	 * @return new FileStorage Object
	 */
	public static FileStorage createFileStorage() {
		FileStorage storage = new FileStorage();
		storage.updateStorage();
		return storage;
	}

	/**
	 * gets path of the file relative to the project folder
	 * @param filepath absolute path to file
	 * @return relative path to file
	 */
	public static Path getRelPath(Path filepath) {
		return workingDir.relativize(filepath);
	}

	/**
	 * gets path of the file relative to the project folder
	 * @param filepath relative String to file
	 * @return relative path to file
	 */
	public static Path getRelPath(String filepath) {
		return getRelPath(workingDir.resolve(filepath));
	}

	/**
	 * returns a new uninitialized FileStorage
	 * @return uninitialized new FileStorage
	 */
	public static FileStorage createEmptyStorage() {
		return new FileStorage();
	}

	/**
	 * Parses the FileInfo and apples changes to the file system
	 * @param info the file which has to be handled
	 * @param fileData the raw file data or null if the file has to be removed
	 * @throws IOException if it is not possible to delete the file
	 */
	public static void handleFileInput(FileInfo info, byte[] fileData) throws IOException {
		if (info.isRemoved()) {
			deleteFileOrFolder(workingDir.resolve(info.getPath()));
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
	 * deletes the file or folder at specified location
	 * @param path to file/folder
	 * @throws IOException if not able to delete the file/folder
	 */
	private static void deleteFileOrFolder(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path file, final IOException e) {
				return handleException(e);
			}

			private FileVisitResult handleException(final IOException e) {
				e.printStackTrace(); // replace with more robust error handling
				return FileVisitResult.TERMINATE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
				if (e != null)
					return handleException(e);
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	};

	/**
	 * Parses the FolderInfo and apples changes to the file system
	 * @param info the folder which has to be handled
	 * @throws IOException if not possible to delete the folder
	 */
	public static void handleFolderInput(FolderInfo info) throws IOException {
		if (info.isAdded()) {
			createFolder(getRelPath(info.getPath()));
		} else if (info.isRemoved()) {
			deleteFileOrFolder(workingDir.resolve(info.getPath()));
		} else if (info.isRenamed()) {
			File oldFolder = new File(workingDir.resolve(info.getOldPath()).toString());
			File newFolder = new File(workingDir.resolve(info.getPath()).toString());
			oldFolder.renameTo(newFolder);
		}
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
	 * 
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
