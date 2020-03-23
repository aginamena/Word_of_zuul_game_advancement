 
/**
 * This class represents an item which may be put
 * in a room in the game of Zuul.
 * 
 * @author Lynn Marshall 
 * @version A3 Solution
 * 
 * @author Agina Oghenemena Benaiah
 * @version march 7,2019
 */
public class Item
{
    // description of the item
    private String description;
    
    // weight of the item in kilgrams
    private double weight;
    
    //short form of the description
    private String name;

    /**
     * Constructor for objects of class Item.
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The short form of the description
     */
    public Item(String description, double weight, String name)
    {
        this.description = description;
        this.weight = weight;
        this.name = name;
    }

    /**
     * Returns a description of the item, including its
     * description and weight.
     * 
     * @return  A description of the item
     */
    public String getDescription()
    {
        return description + " that weighs " + weight + "kg.";
    }
    
    /**
     * Returns the name of an item in a room
     * 
     * @return Returns the name of an item
     */
    public String getName(){
        return name;
    }
    
}
