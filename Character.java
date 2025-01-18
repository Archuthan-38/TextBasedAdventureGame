import java.util.ArrayList;
import java.util.Random;

/**
 * Class Character - represents an NPC that moves randomly about the map.
 *
 * @author Archuthan Mohanathasan
 * @version 2016.02.29
 */
public class Character
{
    private Room[] wizardRooms;
    private ArrayList <String> characters;
    private Random rand;
    private Room wizardLocation;
    private int rooms;
    private boolean withWizard;
    
    /**
     * Constructor for character class.
     */
    public Character(String name)
    {
        wizardRooms = new Room[12];
        rand = new Random();
        characters = new ArrayList <>();
        characters.add(name); // adds name of character to a list containing names of all characters.
    }
    
    /**
     * Creates an array of all the rooms that wizard can move to.
     */
    public void fillWizardRooms(Room room1, Room room2, Room room3,
    Room room4, Room room5, Room room6, Room room7, Room room8, Room room9, 
    Room room10,Room room11,Room room12)
    {
        wizardRooms[0] = room1;
        wizardRooms[1] = room2;
        wizardRooms[2] = room3;
        wizardRooms[3] = room4;
        wizardRooms[4] = room5;
        wizardRooms[5] = room6;
        wizardRooms[6] = room7;
        wizardRooms[7] = room8;
        wizardRooms[8] = room9;
        wizardRooms[9] = room10;
        wizardRooms[10] = room11;
        wizardRooms[11] = room12;

       
        rooms = wizardRooms.length;
    }
    
    /**
     * Moves character to a new room.
     * @return Room that character has moved to.
     */
    public Room moveCharacter()
    {
        int index = rand.nextInt(rooms);
        return wizardRooms[index];
    }
    
    /**
     * @return Current location of the wizard.
     */
    public Room whereIsWizard()
    {
        return wizardLocation;
    }
    
    /**
     * Changes the location of the wizard.
     * Sets location of wizard to @param room.
     */
    public void setLocation(Room room)
    {
        wizardLocation = room;
    }
    
    /**
     * Checks whether or not wizard is with player.
     * @param room Current room player is in.
     */
    public void withWizard(Room room)
    {
        String wizzardPlace = wizardLocation.getName();
        String name = room.getName();
        
        withWizard = false;

        if (name.equals(wizzardPlace)) {
            System.out.println("You are with the wizzard...");
            System.out.println("Perhaps use the talk command to find out why he's here...");
            withWizard = true;
        }
        
    }
    
    /**
     * @param playerMoves Number of moves player has made since the start of the game.
     * Moves character Wizard.
     */
    public void moveWizard(int playerMoves)
    {
        if (playerMoves%3 == 0){
           wizardLocation = moveCharacter(); 
           setLocation(wizardLocation);
        }
        
        // wizard only moves once for every 3 moves the player makes
        // (makes it more possible for player to come across wizard)
        // (if wizard moves when player moves, it is almost imposible for player to come across wizard)
    }
    
    /**
     * Checks if player is with wizard.
     * @return true if player is in same room as wizzard.
     */
    public boolean isPlayerWithWizard()
    {
        return withWizard;
    }
    
    /**
     * Checks if character exists.
     * @param character Name of character.
     * @return true if @param character exists in game.
     */
    public boolean containsCharacter(String character)
    {
        return characters.contains(character);
    }

}
