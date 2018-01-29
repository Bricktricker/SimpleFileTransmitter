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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 *
 * @author Philipp
 */
public class FolderWatcher {
    
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private boolean trace = false;
    
    @SuppressWarnings("unchecked")
    public static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
    
    public FolderWatcher(Path dir) throws IOException{
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();

        System.out.format("Scanning %s ...\n", dir);
        registerAll(dir, null);
        System.out.println("Done.");


        // enable trace after initial registration
        this.trace = true;
    }
    
    public WatchKey getEvents(){
        try {
            return watcher.take();
        } catch (InterruptedException x) {
            return null;
        }
    }
    
    public Path getPath(WatchKey key){
        return keys.get(key);
    }
    
    public void registerAll(final Path start, BiConsumer<Path, Path> renameCallback) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir, renameCallback);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    private void register(Path dir, BiConsumer<Path, Path> renameCallback) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    if(renameCallback != null){
                        renameCallback.accept(prev, dir);
                    }
                    
                    //System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }
    
    public boolean checkKey(WatchKey key){
        boolean valid = key.reset();
        if (!valid) {
            keys.remove(key);

            // all directories are inaccessible
            if (keys.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isDirectoryRegisterd(Path path){
        return keys.containsValue(path);
    }

}
