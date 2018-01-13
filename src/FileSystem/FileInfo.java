package FileSystem;

import java.io.Serializable;

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
	
	
}
