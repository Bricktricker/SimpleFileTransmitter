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
package FileSystem.newImpl;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Philipp
 */
public class TransferFile implements Serializable {

	private static final long serialVersionUID = -3289573390277840858L;
	private final String filePath;
    private final String fileHash;
    
    public TransferFile(String name, String hash){
        filePath = name;
        fileHash = hash;
    }
    
    public String getPath(){
        return filePath;
    }
    
    public String getHash(){
        return fileHash;
    }
    
    /**
     * Compares two TransferFile objects
     * Only compares fileNames, not file hashes
     * To also check file hashes, call equalsWithHash
     */
    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!obj.getClass().equals(getClass()))
            return false;
        
        TransferFile other = (TransferFile)obj;
        
        System.out.println("compare " + this.toString() + " with " + other.toString());
        
        boolean pathEquals = filePath.equals(other.filePath);
        return pathEquals;
    }
    
    /**
     * Compares to TransferFile objects
     */
    public boolean equalsWithHash(TransferFile file) {
    	if(!this.equals(file)) {
    		return false;
    	}
    	return this.fileHash.equals(file.fileHash);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.filePath);
        hash = 59 * hash + Objects.hashCode(this.fileHash);
        return hash;
    }
    
    @Override
    public String toString() {
    	return this.filePath + " " + this.fileHash;
    }
    
}
