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

import java.io.IOException;

/**
 * Exception for networking errors
 * @author Philipp
 * @see IOException
 */
public class NetworkingException extends IOException{
    
	private static final long serialVersionUID = -8274177573997610485L;

	/**
	 * Creates NetworkingException
	 */
	public NetworkingException(){
        super();
    }
    
	/**
	 * Creates NetworkingException and sets error cause
	 * @param message cause of Error
	 */
    public NetworkingException(String message){
        super(message);
    }
    
    /**
     * Creates NetworkingException
     * @param message cause of error
     * @param cause throwable
     */
    public NetworkingException(String message, Throwable cause){
        super(message, cause);
    }
    
    /**
     * creates NetworkingException
     * @param cause reason of exception
     */
    public NetworkingException(Throwable cause){
        super(cause);
    }
    
}
