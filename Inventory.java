import java.util.ArrayList;

/**
 * Class Inventory - Bag carried by player in an adventure game.
 * 
 * This class keeps track of what items player has picked up and what the player can pick up (depending on weight)
 *
 * @author Archuthan Mohanathasan
 * @version 2016.02.29
 */
public class Inventory
{
    private Item item;
    private int totalWeight;
    private boolean haveLanyard;
    private boolean haveCoffee;
    private boolean haveSandwich;
    private boolean haveExamNotes;
    
    private ArrayList <String> inventory;
    
    /**
     * Creates new inventory.
     */
    public Inventory()
    {
       inventory = new ArrayList<>();
       totalWeight = 0; // no items being carried so total weight = 0
    }
    
    /**
     * Checks if there is enough space to carry item (weight limit).
     * Adds item to inventory, if item is not carrrying it already.
     */
    public void add(Item item)
    {
        String name = item.getName();
        int weight = item.getWeight();
        if (!inventory.contains(name)) {
            if ((totalWeight + weight) <= 200) {
                totalWeight += weight;
                inventory.add(name);
                System.out.println("You have taken your " + name + ".");
            }
            else {
                System.out.println("You do not have enough space in your bag to pick up this item.");
                System.out.println("First you must drop an item.");
                System.out.println("Here is your current inventory: " + inventory);
                System.out.println("Use the drop command word followed by the item you wish to be dropped.");
            }
        }
        else {
            System.out.println("You already have this item in your inventory...");
        }
    }
    
    /**
     * Removes item from inventory, if item is being held.
     * Subtracts weight of item being dropped from total weight.
     */
    public void remove(Item item)
    {
        String name = item.getName();
        int weight = item.getWeight();
        if (inventoryHas(name)) {
            inventory.remove(name);
            totalWeight -= weight;
            System.out.println("You are no longer carrying your " + name + ".");
        }
        else {
            System.out.println("Item you wish to drop is not recognised in inventory"); // can't drop item that isn't being carried
        }
    }
    
    
    
    /**
     * Checks whether or not inventory contains a specific item.
     * @return true if item is found in inventory.
     */
    public boolean inventoryHas(String item)
    {
        return inventory.contains(item);
    }
    
    /**
     * Resets inventory.
     */
    public void clearInventory()
    {
        resetItems();
        inventory.clear();
        totalWeight = 0;
    }
    
    // mutator methods (used to change whether or not player has item)
    
    public void setNotes(boolean have)
    {
        haveExamNotes = have;
    }
    
    public void setLanyard(boolean have)
    {
        haveLanyard = have;
    }
    
    public void setCoffee(boolean have)
    {
        haveCoffee = have;
    }
    
    public void setSandwich(boolean have)
    {
        haveSandwich = have;
    }
    
    // accessor methods (obtains if player is carrying items)
    
    public boolean checkNotes()
    {
        return haveExamNotes;
    }
    
    public boolean checkLanyard()
    {
        return haveLanyard;
    }
    
    public boolean checkCoffee()
    {
        return haveCoffee;
    }
    
    public boolean checkSandwich()
    {
        return haveSandwich;
    }
    
    /**
     * Resets player's items.
     */
    private void resetItems()
    {
        haveLanyard = false;
        haveCoffee = false;
        haveSandwich = false;
        haveExamNotes = false;
    }

}
