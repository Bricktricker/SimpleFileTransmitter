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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Philipp
 * @param <T>
 */
public class ChangeList<T extends Info> {
    
    private final HashMap<String, T> changes;
    
    public ChangeList(){
        changes = new HashMap<>();
    }
    
    public void addWithoutWeight(T file){
        changes.put(file.getPath(), file);
        System.out.println("added to " + file.getType() + " ChangeList " + file.toString());
    }
    
    public void remove(T file){
        if(changes.containsKey(file.getPath())){
            changes.remove(file.getPath());
            System.out.println("removed " + file.getPath() + " from map");
        }else{
            file.setRemoved(true);
            add(file);
        }
    }
    
    public List<T> getAllChanges(){
        Collection<T> changeCollection = changes.values();
        List<T> changesAsList = new LinkedList<>();
        changeCollection.forEach( v->changesAsList.add(v) );
        return changesAsList;
    }
    
    public boolean containsKey(String path){
        return changes.containsKey(path);
    }
    
    public boolean containsValue(T value){
        return changes.containsValue(value);
    }
    
    public void add(T newVal){
        if(containsKey(newVal.getPath())){
            T oldVal = changes.get(newVal.getPath());
            if(oldVal.getWeight() < newVal.getWeight()){
                addWithoutWeight(newVal);
            }
        }else{
            addWithoutWeight(newVal);
        }
    }
    
    public void clear(){
        changes.clear();
    }
    
    public int size(){
        return changes.size();
    }
    
}
