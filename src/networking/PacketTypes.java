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
package networking;

/**
 * All packet types for communication
 * @author Philipp
 */
public enum PacketTypes {
    CONNECT, CONNECTED, GET_STATUS, SEND_STATUS, FOLDER_CHANGE, SEND_FILE, PACKET_RECEIVED, ALL_PACK_SEND, KEEP_ALIVE, ERROR
}
