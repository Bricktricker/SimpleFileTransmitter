/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Philipp
 */
public class Packet extends ArrayList<Object> implements Serializable{
    
    PacketTypes type;
    
    public Packet(PacketTypes type, Object... o){
        this.type = type;
        for(Object obj : o){
            this.add(obj);
        }
    }
    
    public Packet(PacketTypes type){
        this.type = type;
    }
    
    public PacketTypes getType(){
        return type;
    }
    
}
