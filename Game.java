import java.util.Stack; 

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
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

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    private boolean isPickedUp,eaten,test;
    private Item pick,takeItem;
    private int count;
    private Room newRoom;

    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        isPickedUp =eaten = test = false;
        count = 0;
        newRoom= null;
        takeItem = null;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office,transporterRoom;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporterRoom = new TransporterRoom("in the transporting room");

        //items are created at the biginning of the game
        outside.addItem(new Item("ball: This is a ball",56.1,"ball"));
        outside.addItem(new Item("cat: This is a cat",563.2,"cat"));
        outside.addItem(new Item("mouse: This is a mouse",526.3,"mouse"));
        outside.addItem(new Item("cookie: This is a cookie",16.4,"cookie"));

        theater.addItem(new Item("white_board: a white board",12.5,"white_board"));
        theater.addItem(new Item("black_board: a black board",2235.6,"black_board"));
        theater.addItem(new Item("cookie: a brown cookie",33.7,"cookie"));

        pub.addItem(new Item("desk: a campus card is on the desk" ,67.8,"desk"));
        pub.addItem(new Item("cookie: a cookie is on the table" ,67.9,"cookie"));

        office.addItem(new Item("bucket: Nice bucket",45.0,"bucket"));
        office.addItem(new Item("cookie: A yellow cookie",12.0,"cookie"));

        // initialise room exits
        //outside has 3 exits: east, south, west(north doesn't exist)
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        //theater has one exit west(south, north and east are null)
        theater.setExit("west", outside);

        pub.setExit("east", outside);
        pub.setExit("north",transporterRoom);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom =outside;  // start game outside

        //creating 2 beamers. Beamer is a type of item
        outside.addItem( new Beamer("beamer: This is a beamer",16.4,"beamer"));
        lab.addItem( new Beamer("beamer: This is a beamer",16.4,"beamer"));

    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("fire")) {
            fire(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(currentRoom.getRoomName().equals("in the transporting room"))
        {
            Room r = (TransporterRoom)currentRoom;
            Room t =  r.getExit("");
            //updating the previous room in case we want to go back
            previousRoom = currentRoom;
            //updating the prevous room stack
            previousRoomStack.push(previousRoom);
            //updating the current room
            currentRoom = t;
            print();   
            return;
        }
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            //updating current room
            currentRoom = nextRoom;
            print();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack

                Room temp = currentRoom;
                //currentRoom is always ahead of previousRoom
                currentRoom = previousRoom;
                //updating previousRoom incase we want to go back
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                print();
            }
        }
    }

    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                print();
            }
        }
    }

    /**
     * "take" was entered. The player may want to pick up an item in a room
     * 
     *  @param command The command to be processed
     */
    private void take(Command command){
        if(command.hasSecondWord() && currentRoom.comparingItemName
        (command.getSecondWord())){
            if(isPickedUp){ //your already holding something
                System.out.println("You are already holding something");
                return;
            }

            pick = currentRoom.getItemName(command.getSecondWord());
            takeItem = pick;

            if( pick.getName().equals("cookie")){
                System.out.println("You have picked up a " + pick.getName());
                System.out.println("Now you have to eat the cookie");
                
                //removes the cookie from the room when we take it
                currentRoom.removeItem(takeItem);
                isPickedUp = true; //we've picked up a cookie
                return;
            }
            else if(eaten && count == 5){
                count =0;
                eaten = false;
            }
            if(eaten && count <5){
                isPickedUp = true;
                pick = currentRoom.getItemName(command.getSecondWord());
                System.out.println("You have picked up a " + pick.getName());
                count++;
                takeItem = pick; //updating the picked item
                //remove the item from the room if we pick it up
                currentRoom.removeItem(takeItem);
            }
            else{
                System.out.println("You have to pick a cookie and eat"
                    +"\n"+"before picking anything"); 
                return;
            }
        }else{
            System.out.println("That item is not in the room");
        }
    }

    /**
     * "drop" was entered. The player may want to drop an item in a room
     * 
     * @param command The command to be processed
     */

    private void drop(Command command){
        if(!command.hasSecondWord()){
            if(isPickedUp){
                //we now have to remove the picked item from the room
                //we have to change the rooms were we added it

                //we've picked up something if we reach here
                isPickedUp = false; //we've droped the item
                System.out.println("You have dropped a " + pick.getName());

                //we can drop an item in a room
                currentRoom.addItem(takeItem);
                pick = null;

                //we now have to remove the picked item from the room
                //currentRoom.removeItem(
            }else{
                System.out.println("You've nothing to drop");
            }

        }else{
            System.out.println("drop what?");
        }
    }

    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command){
        if(pick == null){
            System.out.println("You have to pick a cookie to eat");
            return;
        }

        if(command.hasSecondWord()){
            System.out.println("Eat What?");
        }else{
            //we can only eat cookies
            if(  pick.getName().equals("cookie") ){

                //atleast there is still a cookie if we reach this point
                System.out.println("You have eaten a cookie");
                eaten = true; //we've eaten a cookie
                //if we eat a cookie, we shouldn't be able to eat it again
                currentRoom.removeCookie();
                //and we can't eat what isn't in the room
                
                isPickedUp  =false;
                return;

            }
            System.out.println("Unfortunately, there is no food "+"\n"+
                "you have to pick up a cookie to eat");
        }
    }

    /**
     * prints the current room's description
     */
    private void print(){
        System.out.println(currentRoom.getLongDescription());
        if(isPickedUp){
            System.out.println("You are carrying a "+pick.getName());
            return;
        }
        System.out.println("You haven't picked up any item yet");
    }

    /**
     * "charge" was entered. The player may want to charge a beamer in a room 
     * 
     * @param command The command to be processed
     */
    private void charge(Command command){
        //if the user isn't holding anyting
        if(pick == null){
            System.out.println("You have to pick up a beamer");
            return;
        }
        if(command.hasSecondWord()){
            System.out.println("Charge What");
            return;
        }   
        else if(pick.getName().equals("beamer")&& !test)
        {

            /*if we got here then it's not charged 
             * and we have to charge it
             */
            test = true;
            //memorises the current room
            newRoom = currentRoom; 
            System.out.println("You have charged "+ pick.getName());
        }else if(!pick.getName().equals("beamer")){
            System.out.println("You can only charge a beamer");
        }
        else{
            System.out.println("You have to fire the beamer before "
                +"you charge it again");
        }

    }

    /**
     * "fire" was entered. The player may want to fire a beamer in a room
     * 
     * @param command The command to be processed
     */
    private void fire(Command command){
        //if the user isn't holding anyting
        if(pick == null){
            System.out.println("You have to pick up a beamer");
            return;
        }

        if(command.hasSecondWord()){
            System.out.println("Fire what");
            return;
        }

        /*
         * we should fire when the person has picked up a 
         * beamer and has charged it
         */

        else if(pick.getName().equals("beamer") && test){
            //since we've fired it, it's now ture 
            test = false;
            //storing the current room incase we want to go back
            Room temp = currentRoom;
            //updating previousRoom incase we want to go back
            previousRoom = temp;
            //adding the rooms visited into the stack of rooms
            previousRoomStack.push(temp);
            //fire us back to the room where we charged it
            currentRoom = newRoom;
            print();

        }
        else{
            System.out.println("You can only fire a beamer that is "+
                "not charged");
        }
    }

}
