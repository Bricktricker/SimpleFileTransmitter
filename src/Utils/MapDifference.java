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
package Utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Holds static functions for map manipulation
 * @author Philipp
 */
public class MapDifference {
    
	/**
	 * Calculates the symmetric map difference from the key-set of two given maps
	 * @param left first map
	 * @param right second map
	 * @return set of symmetric difference
	 */
	@Deprecated
    public static <K, V> Set<Entry<K, V>> symmetricMapDifference(Map<K,V> left, Map<K,V> right) {
        Set<Entry<K, V>> diff12 = new HashSet<>(left.entrySet());
        Set<Entry<K, V>> diff21 = new HashSet<>(right.entrySet());
        
        diff12.removeAll(right.entrySet());
        diff21.removeAll(left.entrySet());
        diff12.addAll(diff21);
        return diff12;
    }

}
