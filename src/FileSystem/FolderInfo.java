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

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Philipp
 */
public class FolderInfo extends Info implements Serializable{
    
    private static final long serialVersionUID = 69878761527707186L;
	
    private String path;
    private String oldPath;
    private boolean added;
    private boolean removed;
    private boolean renamed;
    
    public FolderInfo(String folderPath){
        path = folderPath;
    }
    
    @Override
    public String getPath(){
        return path;
    }
    
    @Override
    public void setPath(String path){
        this.path = path;
    }
    
    @Override
    public boolean isAdded() {
        return added;
    }

    @Override
    public void setAdded(boolean added) {
        this.added = added;
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }
    
    @Override
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
    
    
    public String getOldPath(){
        if(isRenamed())
            return oldPath;
        
        throw new IllegalStateException("Directory not renamed");
    }
    
    public boolean isRenamed(){
        return renamed;
    }
    
    public void setRenamed(boolean renamed, String oldName){
        this.renamed = renamed;
        this.oldPath = oldName;
    }
    
    @Override
    public int getWeight(){
        int base = -1;
        if(path != null && !path.isEmpty())
            base++;
        if(added || removed)
            base++;
        if(renamed)
            base+=2;

        return base;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj == null)
                return false;
        if(obj == this)
                return true;
        if (! obj.getClass().equals(getClass()))
                return false;
            
        FolderInfo other = (FolderInfo) obj;
        boolean folderPathDif = path.equals(other.path);
        return folderPathDif;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.path);
        return hash;
    }
    
    @Override
    public String toString(){
        return path + (added?" added":"") + (removed?" removed":"") + (renamed?" reanmed from: " + oldPath :"");
    }
    
    @Override
    public String getType(){
        return "FolderInfo";
    }
    
}
