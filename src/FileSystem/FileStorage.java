package FileSystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import Utils.Pair;

public class FileStorage {
	
	private Path folder; //Project directory
	private HashMap<String, String> fileMap; //Map wit path, hash
	
	public FileStorage(String folder){
		this.folder = Paths.get(folder);
		fileMap = new HashMap<String, String>();
		System.out.println(folder.toString());
	}
	
	//Sets project directory to working directory
	public FileStorage(){
		Path currentRelativePath = Paths.get("");
		folder = currentRelativePath.toAbsolutePath();
		fileMap = new HashMap<String, String>();
		System.out.println(folder.toString());
	}
	
	//updates fileMap and return List of changes
	public List<FileInfo> update(){
		List<FileInfo> changes = new LinkedList<FileInfo>();
		FileStorage newStorage = new FileStorage(folder.toString());
		update(changes, folder.toString(), newStorage);
		
		addDeletedFiles(changes, newStorage);
		fileMap = newStorage.fileMap;
		return changes;
	}
	
	private void update(List<FileInfo> changeList, String path, FileStorage newStorage) {
		File[] files = new File(path).listFiles();
		
		for(File f : files) {
			if(f.isFile()) {
				
				try {
					String hash = getHash(f);
					String p = getRelPath(f.toPath());
					String oldHash = fileMap.get(p);
					
					//File was added to project
					if(oldHash == null) {
						FileInfo info = new FileInfo(p, hash);
						info.setAdded(true);
						changeList.add(info);
						
					//File got updated
					}else if(!hash.equals(oldHash)) {
						FileInfo info = new FileInfo(p, hash);
						changeList.add(info);
					}
					
					//Add file to new FileStorage
					newStorage.addFile(p, hash);
				
				} catch (NoSuchAlgorithmException | IOException e) {
					e.printStackTrace();
				}
			
			//File is folder, add it recursively to List
			}else {
				update(changeList, f.getAbsolutePath(), newStorage);
			}
		}
	}
	
	//Searches for files with got deleted
	private void addDeletedFiles(List<FileInfo> changeList, FileStorage newState) {
		Set<String> oldSet = fileMap.keySet();
		Set<String> newSet = newState.fileMap.keySet();
		oldSet.removeAll(newSet);
		
		//Add deleted files to List
		for(String del : oldSet) {
			FileInfo info = new FileInfo(del, fileMap.get(del));
			info.setRemoved(true);
			changeList.add(info);
		}
	}
	
	public void fillMap() {
		fillMap(folder.toString());
		System.out.println("filled Map");
	}
	
	//fills fileMap with pathes and hash values
	private void fillMap(String path) {
		File[] files = new File(path).listFiles();
		
		for(File f : files) {
			if(f.isFile()) {
				addToMap(f);
			}else {
				fillMap(f.getAbsolutePath());
			}
		}
	}
	
	//get Path of the File relative to the project folder
	private String getRelPath(Path filePath) {
		return folder.relativize(filePath).toString();
	}
	
	//add File to fileMap
	private void addToMap(File file) {
		try {
			String hash = getHash(file);
			String p = getRelPath(file.toPath());
			//System.out.println(p);
			fileMap.put(p, hash);
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//generate hash from file
	private String getHash(final File file) throws NoSuchAlgorithmException, IOException  {
	    final MessageDigest messageDigest = MessageDigest.getInstance("MD5");

	    try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
	      final byte[] buffer = new byte[1024];
	      for (int read = 0; (read = is.read(buffer)) != -1;) {
	        messageDigest.update(buffer, 0, read);
	      }
	    }

	    // Convert the byte to hex format
	    try (Formatter formatter = new Formatter()) {
	      for (final byte b : messageDigest.digest()) {
	        formatter.format("%02x", b);
	      }
	      return formatter.toString();
	    }
	}
	
	//return a List of all files in the fileMap
	public List<Pair<String, String>> getAllFiles(){
		List<Pair<String, String>> list = new ArrayList<>();
		Set<Entry<String, String>> entries = fileMap.entrySet();
	
		for(Entry<String, String> s : entries) {
			Pair<String, String> pair = new Pair<>(s.getKey(), s.getValue());
			list.add(pair);
		}
		
		return list;
	}
	
	//Get a hash from a filePath (if path is stord i fileMap)
	public String getHash(String path, boolean isAbsolute) {
		if(isAbsolute) {
			String relPath = getRelPath(Paths.get(path));
			return fileMap.get(relPath);
		}else {
			return fileMap.get(path);
		}
	}
	
	//Adds a file and the hash to the fileMap
	public void addFile(String path, String hash) {
		fileMap.put(path, hash);
	}
	
}