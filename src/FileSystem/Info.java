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

/**
 * Abstract class for basic Info functions
 * 
 * @author Philipp
 */
public abstract class Info {

	/**
	 * returns the relative path to the project directory from this info
	 * @return path of info
	 */
	public abstract String getPath();

	/**
	 * sets the path for this info object
	 * @param relative path from the working directory to this info
	 */
	public abstract void setPath(String path);

	/**
	 * returns if this info was added to the filesystem
	 * @return info added to filesystem
	 */
	public abstract boolean isAdded();

	/**
	 * sets the information, that the info was added to the filesystem
	 * @param added
	 */
	public abstract void setAdded(boolean added);

	/**
	 * returns if this info was removed from the filesystem
	 * @return info removed from filesystem
	 */
	public abstract boolean isRemoved();

	/**
	 * sets the information, that the info was removed from the filesystem
	 * @param removed
	 */
	public abstract void setRemoved(boolean removed);

	/**
	 * gets the "importance" of this info
	 * @return
	 */
	public abstract int getWeight();

	/**
	 * returns the type of the implemented info
	 * @return type
	 */
	public abstract String getType();

}
