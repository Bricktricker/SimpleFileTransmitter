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

public class FileInfo extends Info implements Serializable {

	private static final long serialVersionUID = 5143096375634312506L;
	
	private String filePath;
	private String fileHash;
	private boolean added;
	private boolean removed;
        private boolean isDirectory;
	
	public FileInfo(String path, String hash) {
		filePath = path;
		fileHash = hash;
		added = false;
		removed = false;
	}

        @Override
	public String getPath() {
		return filePath;
	}

        @Override
	public void setPath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
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
        
        public boolean isDirectory(){
            return isDirectory;
        }
        
        public void setDirectory(boolean dir){
            isDirectory = dir;
        }
	
        @Override
        public boolean equals( Object obj ){
            if(obj == null)
                return false;
            if(obj == this)
                return true;
            if (! obj.getClass().equals(getClass()))
                return false;
            
            FileInfo other = (FileInfo) obj;
            boolean filePathDif = filePath.equals(other.filePath);
            //boolean hashDif = fileHash.equals(other.fileHash);
            return filePathDif;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(this.filePath);
            return hash;
        }

        @Override
        public String toString(){
            return filePath + (added?" added":"") + (removed?" removed":"");
        }
	
}
