
/**
 * A beamer is a device that can be charged and fired. When you charge
 * the beamer, it memorizes the current room. When  you fire the beamer,
 * it transports you immediately back to the room it was charged in
 *
 * @author Agina Oghenemena Benaiah
 * @version march 7, 2019
 */
public class Beamer extends Item
{
    
    /**
     * Constructor for objects of class Item.
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The short form of the description
     */
    public Beamer(String description, double weight,String name)
    {
        super(description, weight, name);
    
    }
    
   
}
