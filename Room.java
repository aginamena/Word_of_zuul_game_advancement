import java.util.Set;
import java.util.HashMap; 
import java.util.Iterator;
import java.util.ArrayList; // or java.util.*; and replace the above
import java.util.LinkedHashSet;

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
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version A3 Solution
 * 
 * @author Agina Oghenemena Benaiah
 * @version march 7,2019
 */

public class Room 
{
    public static ArrayList<Room> holdRandomRoom = new ArrayList<Room>();
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> items;              // the items in this room
    private boolean test;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new ArrayList<Item>();
        test = false;
    }

    /**
     * Add an item to the room, best to check that it's not null.
     * 
     * @param item The item to add to the room
     */
    public void addItem(Item item) 
    {
        if (item!=null) { // not required, but good practice
            items.add(item);
        }
    }

    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit
     * @param neighbour The room to which the exit leads
     */
    public void setExit(String direction, Room neighbour) 
    {
        exits.put(direction, neighbour);

        //we want to add the rooms into the arraylist of rooms
        holdRandomRoom.add(neighbour);

        //we want to only unique rooms
        Set <Room> p = new LinkedHashSet<Room>(holdRandomRoom);

        //clear room
        holdRandomRoom.clear();

        //add all elements in the arraylist without duplicates
        holdRandomRoom.addAll(p);

        /*using an iterator object to go through the a room to remove the
         * transporter room because we don't want to be transported back
         * into the transporter room when we leave the room
         */
        Iterator<Room> a = holdRandomRoom.iterator();
        while(a.hasNext()){
            Room q = a.next();
            if(q.description.equals("in the transporting room")){
                a.remove();
            }
        }
    }

    /**
     * Returns a short description of the room, i.e. the one that
     * was defined in the constructor
     * 
     * @return The short description of the room
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     Items: 
     *        a chair weighing 5 kgs.
     *        a table weighing 10 kgs.
     *     
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString()
        + "\nItems:" + getItems();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits
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
     * 
     * @param direction The exit's direction
     * @return The room in the given direction
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * Return a String representing the items in the room, one per line.
     * 
     * @return A String of the items, one per line
     */
    public String getItems() 
    {
        // let's use a StringBuilder (not required)
        StringBuilder s = new StringBuilder();
        for (Item i : items) {
            s.append("\n    " + i.getDescription());
        }
        return s.toString(); 
    }

    /**
     * checks if there's an item in the room
     * 
     * @param secondBookName Holds the items name in a room
     * @return Returns true if the item is in the room or false if 
     * not found
     */
    public boolean comparingItemName(String secondBookName){
        /*
         * Each room is independent of their items. Meaning, different
         * rooms have different items. 
         */
        for(int i = 0; i<items.size(); i++){
            if(items.get(i).getName().equals(secondBookName)){
                return true;
            }
        }
        return false;

    }

    /**
     * Gets the name of an item in a room
     * 
     * @param secondBookName Holds the items name in a room
     * @return Returns the item the player picks or null if the item
     * is not in the room
     */
    public Item getItemName(String secondBookName){

        for(int i = 0; i<items.size(); i++){
            Item p = items.get(i); //get the item
            String s = p.getName(); // get the name
            if(s.equals(secondBookName)){
                return p;
            }
        }
        return null;

    }

    /**
     * Remove a cookie in a room
     */
    public void removeCookie(){
        int temp = getCookie();
        if(temp!= -1){
            items.remove(temp);
        }
    }

    /**
     * Get the index of the cookie in a room
     * 
     * @return Returns the index of the cookie (-1 if not found)
     */
    public int getCookie(){

        for(Item item : items){
            if(item.getName().equals("cookie")){
                //atleast there is still cookie
                test = true;
                return items.indexOf(item);
            }
        }
        return -1;//not found
    }

    /**
     * Try to see if there is a cookie in a room
     * 
     * @return Returns true if there's a cookie and false if there's 
     * no cookie
     */
    public boolean checkForCookie(){
        getCookie();
        return test;
    }

    /**
     * Returns the name of a room
     * 
     * @return Returns the description of a room
     */
    public String getRoomName(){
        return description;
    }

    /**
     * Removes an item in a room
     * 
     * @param p This is the item we want to remove from the room
     */
    public void removeItem(Item p){
        Iterator iter = items.iterator();
        while(iter.hasNext()){
            if(iter.next() == p){
                iter.remove();
            }
        }
    }

    
}

