/**
 * Class Item - an item found in an adventure game.
 * 
 * An "item" represents something that can be carried by the player.
 * It is linked to a name, its weight and some information about it.
 *
 * @author Archuthan Mohanathasan
 * @version 2016.02.29
 */
public class Item
{
    private String info;
    private String name;
    private int weight;
    
    /**
     * Creates item and stores, information, name and weight.
    */
    public Item(String info, String name, int weight)
    {
        this.info = info;
        this.name = name;
        this.weight = weight;
    }

    /**
     * Returns information about the item.
     */
    public String getInfo()
    {
        return info;
    }
    
    /**
     * Returns name of item (string).
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the weight of item.
     */
    public int getWeight()
    {
        return weight;
    }
}
