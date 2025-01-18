import java.util.Set;
import java.util.HashMap;
import java.util.Random;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room 
{
    private String description;
    private String name;
    private HashMap<String, Room> exits;        // stores exits of this room. 
    private HashMap<Room, Integer > time;       // stores the time taken to get from one room to another
    private Random rand;
    private Room[] teleportRoomList;            // array of all rooms that teleporter can teleport player to
    private int teleport;                       // number of possible rooms teleporter can teleport player to
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, String name) 
    {
        this.description = description;
        this.name = name;
        teleportRoomList = new Room[11];
        teleport = 11;
        exits = new HashMap<>();
        time = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * Defines how long it takes to get from one room to another.
     * @param neighbor The room being travelled to.
     * @param travel The time taken to travel to Room neighbor from currentRoom.
     */
    public void setTime(Room neighbor, int travel)
    {
        time.put(neighbor, travel);
        
    }

    /**
     * Creates an array of all the rooms that teleporter can teleport player too.
     */
    public void setTeleportRooms(Room room1, Room room2, Room room3,
    Room room4, Room room5, Room room6, Room room7, Room room8, Room room9, 
    Room room10,Room room11)
    {
        teleportRoomList[0] = room1;
        teleportRoomList[1] = room2;
        teleportRoomList[2] = room3;
        teleportRoomList[3] = room4;
        teleportRoomList[4] = room5;
        teleportRoomList[5] = room6;
        teleportRoomList[6] = room7;
        teleportRoomList[7] = room8;
        teleportRoomList[8] = room9;
        teleportRoomList[9] = room10;
        teleportRoomList[10] = room11;
    }
    
    /**
     * Returns a room from teleportRoomList that correlates to @param index.
     * (Used to pick random teleport destination)
     */
    public Room destination(int index)
    {
        return teleportRoomList[index];
    }
    
    /**
     * Returns number of possible rooms that teleporter can teleport player to.
     */
    public int teleportableRooms()
    {
        return teleport;
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * @return The name of the room (String)
     * (the one that was defined in the constructor).
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns String stating that player has entered a room.
     */
    public String getEnterInfo()
    {
        return "You have entered " + name + ".";
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Returns the time taken to travel from one room to another.
     */
    public int getTimeTaken(Room room)
    {
        if (room!=null){
           return time.get(room); 
        }
        else{
            return 0;
        }
    }
    
}

