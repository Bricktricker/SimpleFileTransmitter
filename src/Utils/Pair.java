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

import java.util.Objects;

/**
 * Packs to Object in to one pair
 * @author Philipp
 *
 * @param <T1> Object 1
 * @param <T2> Object 2
 */
public class Pair<T1, T2>{
		
	private T1 val1;
	private T2 val2;
	
	public Pair(T1 val1, T2 val2){
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public T1 getFirst() {
		return val1;
	}
	
	public T2 getSecond() {
		return val2;
	}

	public void setFirst(T1 val1) {
		this.val1 = val1;
	}

	public void setSecond(T2 val2) {
		this.val2 = val2;
	}
	
        @Override
	public String toString() {
		return "" + val1.toString() + " | " + val2.toString();
	}
        
        @Override
        public boolean equals(Object obj){
            if(obj == null)
                return false;
            if(obj == this)
                return true;
            if (! obj.getClass().equals(getClass()))
                return false;
            
            @SuppressWarnings("unchecked")
			Pair<T1, T2> other = (Pair<T1, T2>) obj;
            
            boolean val1Equals = val1.equals(other.val1);
            boolean val2Equals = val2.equals(other.val2);
            return val1Equals && val2Equals;
        }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.val1);
        hash = 53 * hash + Objects.hashCode(this.val2);
        return hash;
    }
}
