package FileSystem;

public class FileManager {

	public static String workingDir = "";
	
	public static FileStorage createFileStorage() {
		FileStorage storage;
		if(workingDir.isEmpty()) {
			storage = new FileStorage();
		}else {
			storage = new FileStorage(workingDir);
		}
		
		storage.fillMap();
		return storage;
	}
}
