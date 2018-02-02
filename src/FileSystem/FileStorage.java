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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import Utils.Pair;
import java.io.Serializable;
import java.nio.file.FileSystemException;

/**
 * Stores the filepathes and the filehashes of specified folder
 *
 * @author Philipp
 *
 */
public class FileStorage implements Serializable {

    private static final long serialVersionUID = 3258695147163353327L;
    private HashMap<String, String> fileMap; // Map with relative path, hash

    /**
     * Initializes FileStorage object
     */
    public FileStorage() {
        fileMap = new HashMap<>();
    }

    // updates fileMap and return List of changes
    public List<FileInfo> getChanges() {
        List<FileInfo> changes = new LinkedList<>();
        FileStorage newStorage = new FileStorage();
        getChangesRecursive(changes, FileManager.workingDir.toString(), newStorage);

        addDeletedFiles(changes, newStorage);
        fileMap = newStorage.fileMap;
        return changes;
    }

    /**
     *
     * @param changeList list, where changes get added to
     * @param absolute path to folder
     * @param storage where the new filesystem situation gets stored
     */
    private void getChangesRecursive(List<FileInfo> changeList, String path, FileStorage newStorage) {
        File[] files = new File(path).listFiles();

        for (File f : files) {
            if (f.isFile()) {

                try {
                    String hash = FileManager.getHash(f);
                    String pathRel = FileManager.getRelPath(f.toPath()).toString();
                    String oldHash = fileMap.get(pathRel);

                    // File was added to project
                    if (oldHash == null) {
                        FileInfo info = new FileInfo(pathRel, hash);
                        info.setAdded(true);
                        changeList.add(info);

                        // File got updated
                    } else if (!hash.equals(oldHash)) {
                        FileInfo info = new FileInfo(pathRel, hash);
                        changeList.add(info);
                    }

                    // Add file to new FileStorage
                    newStorage.addFile(pathRel, hash);

                } catch (FileSystemException e) {
                    System.err.println("could not read " + e.getFile());
                }

                // File is folder, add it recursively to List
            } else {
                getChangesRecursive(changeList, f.getAbsolutePath(), newStorage);
            }
        }
    }

    // Searches for files which got deleted
    private void addDeletedFiles(List<FileInfo> changeList, FileStorage newState) {
        Set<String> oldSet = fileMap.keySet();
        Set<String> newSet = newState.fileMap.keySet();
        oldSet.removeAll(newSet);

        // Add deleted files to List
        for (String del : oldSet) {
            FileInfo info = new FileInfo(del, fileMap.get(del));
            info.setRemoved(true);
            changeList.add(info);
        }
    }

    // loads current folder status and saves it in fileMap
    public void updateStorage() {
        fileMap.clear();
        updateStorageRecursive(FileManager.workingDir.toString());
    }

    // fills fileMap with pathes and hash values
    private void updateStorageRecursive(String path) {
        File[] files = new File(path).listFiles();

        for (File f : files) {
            if (f.isFile()) {
                try {
                    addFileToMap(f);
                } catch (FileSystemException e) {
                    System.err.println("Error while reading " + e.getFile());
                }
            } else {
                updateStorageRecursive(f.getAbsolutePath());
            }
        }
    }

    // add File to fileMap
    private void addFileToMap(File file) throws FileSystemException {
        try {
            String hash = FileManager.getHash(file);
            String pathRel = FileManager.getRelPath(file.toPath()).toString();
            fileMap.put(pathRel, hash);
        } catch (IOException e) {
            throw new FileSystemException(file.toString());
        }
    }

    public Pair<String, String> removeFromMap(File file) {
        String path = FileManager.getRelPath(file.toPath()).toString();
        String hash = fileMap.get(path);
        fileMap.remove(path);
        return new Pair<>(path, hash);
    }

    // return a List of all files in the fileMap
    public List<Pair<String, String>> getAllFiles() {
        List<Pair<String, String>> list = new ArrayList<>();
        Set<Entry<String, String>> entries = fileMap.entrySet();

        for (Entry<String, String> s : entries) {
            Pair<String, String> pair = new Pair<>(s.getKey(), s.getValue());
            list.add(pair);
        }

        return list;
    }

    // Get a hash from a filePath (if path is stored fileMap)
    public String getHash(String path, boolean isAbsolute) {
        if (isAbsolute) {
            String relPath = FileManager.getRelPath(Paths.get(path)).toString();
            return fileMap.get(relPath);
        } else {
            return fileMap.get(path);
        }
    }

    // Adds a file and the hash to the fileMap
    public void addFile(String path, String hash) {
        fileMap.put(path, hash);
    }

    public void addFile(File file) throws FileSystemException {
        addFileToMap(file);
    }

    public HashMap<String, String> getMap() {
        return fileMap;
    }

    @Override
    public String toString() {
        String s = "";
        List<Pair<String, String>> files = getAllFiles();

        for (Pair<String, String> file : files) {
            s += file.toString() + " ";
        }

        return s;
    }

    public void clear() {
        fileMap.clear();
    }

}
