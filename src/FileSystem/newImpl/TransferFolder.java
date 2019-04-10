package FileSystem.newImpl;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import FileSystem.FileInfo;
import FileSystem.FileManager;
import FileSystem.FolderInfo;

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


/**
 *
 * @author Philipp
 */
public class TransferFolder implements Serializable {
    
	private static final long serialVersionUID = -2218718677428339463L;
	
	private final String folderName;
    private final List<TransferFolder> subFolders;
    private final List<TransferFile> folderFiles;
    
    public TransferFolder(String path){
        folderName = path;
        subFolders = new ArrayList<>();
        folderFiles = new ArrayList<>();
    }
    
    public void load() throws FileSystemException{
        File folder = new File(FileManager.workingDir.resolve(folderName).toString());
        for(File file : folder.listFiles()) {
        	if(file.isDirectory()) {
        		TransferFolder trFolder = new TransferFolder(FileManager.getRelPath(file.getPath()).toString());
        		trFolder.load();
        		subFolders.add(trFolder);
        	}else if(file.isFile()) {
        		folderFiles.add(new TransferFile(FileManager.getRelPath(file.getPath()).toString(), FileManager.getHash(file)));
        	}
        }
    }
    
    /**
     * computes and returns the difference from the stored files to the current files on disc
     * @return List of all changed files
     * @throws FileSystemException  
     */
    public List<FileInfo> getFileChanges() throws FileSystemException {
    	TransferFolder TffNew = new TransferFolder(folderName);
    	TffNew.load();
    	
    	//List of files that got changed in the current directory
    	List<FileInfo> changesRoot = Stream.concat(Stream.concat(
    	this.folderFiles.stream().filter(c->!TffNew.folderFiles.contains(c)).map(f -> new FileInfo(f.getPath(), f.getHash(), false, true)),
    	TffNew.folderFiles.stream().filter(c->!this.folderFiles.contains(c)).map(f -> new FileInfo(f.getPath(), f.getHash(), true, false))),
    			this.folderFiles.stream().filter(f-> {
    	    		int index = TffNew.folderFiles.indexOf(f);
    	    		if(index == -1)
    	    			return false;
    	    		return !TffNew.folderFiles.get(index).equals(f);
    	    	}
    	    	).map(f -> new FileInfo(f.getPath(), f.getHash()))		
    	).collect(Collectors.toList());
    	
    	//List of file changes in sub folder
    	List<FileInfo> changes = this.subFolders.stream().filter(f->TffNew.subFolders.contains(f)).map(f -> {
			try {
				return f.getFileChanges();
			} catch (FileSystemException e) {
				e.printStackTrace();
			}
			return null;
		}).flatMap(List::stream).collect(Collectors.toList());
    	
    	changes.addAll(changesRoot);
    	
    	return changes;
    }
    
    public List<FolderInfo> getFolderChanges() throws FileSystemException {
    	TransferFolder TffNew = new TransferFolder(folderName);
    	TffNew.load();
    	
    	return Stream.concat(
        		this.subFolders.stream().filter(c->TffNew.subFolders.contains(c)).map(f -> new FolderInfo(f.folderName, false)),
        		TffNew.subFolders.stream().filter(c->this.subFolders.contains(c)).map(f -> new FolderInfo(f.folderName, true))
        	).collect(Collectors.toList());
    }
    
    public void addFile(TransferFile file){
        folderFiles.add(file);
    }
    
    public void addFolder(TransferFolder folder){
        subFolders.add(folder);
    }
    
    public TransferFile getFile(String path){
        for (TransferFile folderFile : folderFiles) {
            if(folderFile.getPath().equals(path))
                return folderFile;
        }
        return null;
    }
    
    public TransferFolder getFolder(String path){
        for(TransferFolder folder : subFolders){
            if(folder.folderName.equals(path))
                return folder;
        }
        return null;
    }
    
}
