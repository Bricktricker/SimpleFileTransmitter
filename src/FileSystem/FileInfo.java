package FileSystem;

import java.io.Serializable;
import java.util.Objects;

public class FileInfo implements Serializable {

	private static final long serialVersionUID = 5143096375634312506L;
	
	private String filePath;
	private String fileHash;
	private boolean added;
	private boolean removed;
	
	public FileInfo(String path, String hash) {
		filePath = path;
		fileHash = hash;
		added = false;
		removed = false;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
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
            boolean hashDif = fileHash.equals(other.fileHash);
            boolean stats = added == other.added && removed == other.removed;
            return filePathDif && hashDif && stats;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 41 * hash + Objects.hashCode(this.filePath);
            hash = 41 * hash + Objects.hashCode(this.fileHash);
            hash = 41 * hash + (this.added ? 1 : 0);
            hash = 41 * hash + (this.removed ? 1 : 0);
            return hash;
        }
	
}
