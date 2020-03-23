import java.util.Random;
/**
 * The transporterRoom class,randomly transports a player into one of the
 * other rooms
 *
 * @author Agina Oghenemena Benaiah
 * @version march 7, 2019
 */
public class TransporterRoom extends Room
{
    /**
     * Constructor for objects of class TransporterRoom
     * 
     * @param description The description of the room
     */
    public TransporterRoom(String description)
    {
        super(description);
    }

    /**
     * Returns a random room regardless of the direction
     * 
     * @param direction The exit's direction
     * @return Returns a random room
     */
    public Room getExit(String direction){
        return findRandomRoom();
    }
    
    /**
     * FindRandomRoom picks the random room
     * 
     * @return returns a random room
     */
    private Room findRandomRoom(){
        Random r= new Random();
        int v = r.nextInt(holdRandomRoom.size()); //returns from 0 - (size -1)
        Room s =  holdRandomRoom.get(v); //return s a room;
        return s;
    }
}
