package FileSystem.newImpl;

import java.util.ArrayList;
import java.util.List;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
public class TransferFolder {
    
    private final String folderName;
    private final List<TransferFolder> subFolders;
    private final List<TransferFile> folderFiles;
    
    public TransferFolder(String path){
        folderName = path;
        subFolders = new ArrayList<>();
        folderFiles = new ArrayList<>();
    }
    
    public void load(){
        throw new NotImplementedException();
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
