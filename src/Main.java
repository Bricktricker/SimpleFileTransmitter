import java.util.List;

import FileSystem.FileInfo;
import FileSystem.FileStorage;

public class Main {

	public static void main(String[] args) {
		FileStorage origStorage = new FileStorage("C:\\Users\\User\\Downloads");
		origStorage.fillMap();
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<FileInfo> changes = origStorage.update();
		System.out.println(changes.size());
		for(FileInfo info : changes) {
			System.out.println(info.getFilePath());
		}
		
		
	}
}
