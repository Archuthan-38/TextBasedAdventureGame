import java.util.ArrayList;
import java.util.Random;

/**
 *  This class is the main class of the "A day in KCL with a Twist" application. 
 *  "A day in KCL with a Twist" is a very simple, text based adventure game.  Users
 *  must explore the campus all while attending their lab, lecture and exam on time. 
 *  Many secrets to be discovered...
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 * @author  Michael Kölling, David J. Barnes and Archuthan Mohanathasan
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Time clock;
    private Item currentItem;
    private Exam test;
    private Calculator calc;
    private Inventory bag;
    private Character wizard;
    private Random rand;    
    
    private boolean access;
    private boolean inRoom;
    private boolean hasItem;
    private boolean itemGiven;
    private boolean taken;
    private boolean permission;
    private boolean rewardGranted;
    
    private int counter; // every 3 moves, wizard will move as well

    private ArrayList<String> locationNotes; // array list used to store possible location notes
    private ArrayList<String> labList; // array list used to store possible designated labs
    private ArrayList <String> prevCommands; // array list used to keep track of commands (for back command to function)
    
    private String correctLab;
    private String notesLocation;
    
    private Room greggs, outside, theater, canteen, office, lab1, lab2, groupstudy, independentstudy1, independentstudy2, exam, teleporter;
    private Item lanyard, examNotes, coffee, sandwich, examMarkScheme;
    
    public static void main(String[] args) {
        // Initialize the game (create rooms, items, etc.)
        Game game = new Game();
        System.out.println("Game started!");
        game.play();
        // Further game logic to start the game (like game.start(), or any initialization)
    }

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        calc = new Calculator();
        parser = new Parser();
        bag = new Inventory();
        rand = new Random();
        prevCommands = new ArrayList<>();
        locationNotes = new ArrayList<>();
        labList = new ArrayList<>();
    }
    
    /**
     * This initialises all the boolean variables to false; this needs to be done
     * at the start of every new game.
     */
    private void startingBooleans()
    {
        access = false;
        inRoom = false;
        itemGiven = false;
        taken = false;
        permission = false;
        rewardGranted = false;
    }
        
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        
        outside = new Room("outside the main entrance of the university","outside");
        greggs = new Room("outside Greggs", "Greggs");
        theater = new Room("outside the lecture theater", "the lecture theater");
        canteen = new Room("outside the canteen", "the canteen");
        lab1 = new Room("outside computing lab 1", "computing lab 1");
        lab2 = new Room("outside computing lab 2", "computing lab 2");
        groupstudy = new Room("outside group study room", "group study");
        independentstudy1 = new Room("outside independent study room 1", "independent study 1");
        independentstudy2 = new Room("outside independent study room 2", "independent study 2");
        office = new Room("outside the computing admin office", "the computing admin office");
        exam = new Room("outside the exam room", "the exam room");
        teleporter = new Room("outside the magic teleporter room", "the magic teleporter room");
        
        // initialise room exits
        
        outside.setExit("east", canteen);
        outside.setExit("south", theater);
        outside.setExit("west", lab1);
        outside.setExit("north", greggs);
        
        greggs.setExit("south", outside);

        theater.setExit("north", outside);
        theater.setExit("south", groupstudy);
        theater.setExit("west", lab2);
        theater.setExit("east", exam);
        
        groupstudy.setExit("north", theater);
        groupstudy.setExit("west", independentstudy1);
        groupstudy.setExit("south", teleporter);
        
        independentstudy1.setExit("east", groupstudy);
        independentstudy1.setExit("south", independentstudy2);
        
        independentstudy2.setExit("north", independentstudy1);
        
        lab2.setExit("east", theater);

        canteen.setExit("west", outside);
        
        lab1.setExit("east", outside);
        lab1.setExit("north", office);
        
        office.setExit("south", lab1);
        
        exam.setExit("west", theater);
        
        teleporter.setExit("north", groupstudy);
        
        //initialise time taken to travel between rooms
        
        outside.setTime(canteen, 5);
        outside.setTime(theater, 5);
        outside.setTime(lab1, 3);
        outside.setTime(greggs, 45);
        
        greggs.setTime(outside, 45);

        theater.setTime(outside, 5);
        theater.setTime(groupstudy, 7);
        theater.setTime(lab2, 3);
        theater.setTime(exam, 4);
        
        groupstudy.setTime(theater, 7);
        groupstudy.setTime(independentstudy1, 2);
        groupstudy.setTime(teleporter, 8);
        
        independentstudy1.setTime(groupstudy, 2);
        independentstudy1.setTime(independentstudy2, 2);
    
        independentstudy2.setTime(independentstudy1, 2);
        
        lab2.setTime(theater, 3);

        canteen.setTime(outside, 5);

        lab1.setTime(outside, 3);
        lab1.setTime(office, 2);
        
        office.setTime(lab1, 2);
        
        exam.setTime(theater, 4);
        
        teleporter.setTime(groupstudy, 8);
        
        currentRoom = outside;  // start game outside
        
        
        // initialise list of rooms that teleporter can teleport you too.
        
        teleporter.setTeleportRooms(greggs, outside, theater, canteen,
        office, lab1, lab2, groupstudy, independentstudy1, independentstudy2,
        exam);
    }
    
    /**
     * Creates all items; item stores information about it, name (in string) 
     * and its weight.
     */
    private void createItems()
    {
        lanyard = new Item("Lanyard is needed for labs, lectures and exam.", "lanyard", 20);
        examNotes = new Item("Exam notes improves score in exam.", "notes", 100);
        coffee = new Item("Coffee boosts exam score.", "coffee", 70);
        sandwich = new Item("Sandwich can be given to a friend in exchange for a secret reward.", "sandwich", 80);
        examMarkScheme = new Item("***YOU WERE NOT MEANT TO FIND THIS***", "markscheme", 50);
        
        currentItem = null;
    }
    
    /**
     * Creates NPC (wizard); wizard spawns outside (with player)
     * 
     */
    private void createCharacter()
    {
        wizard = new Character("wizard");
        wizard.setLocation(outside); //starting location for wizard character
        wizard.fillWizardRooms(greggs, outside, theater, canteen, office, lab1, 
        lab2, groupstudy, independentstudy1, independentstudy2, exam, 
        teleporter); // list of all the rooms that wizard can move to
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        createRooms();
        createCharacter();
        createItems();
        clock = new Time();
        calc = new Calculator();
        test = new Exam();
        prevCommands = new ArrayList<>();
        correctLab = whichLab();
        notesLocation = examNotesLocation();
        bag.clearInventory();
        printWelcome();
        startingBooleans();
        counter = 0;
        
        
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over
                
        boolean finished = clock.end(); // there are 3 ways to end the game 
        // (1) by using quit command
        // (2) by getting results after finishing exam (results command)
        // (3) time running out (when time is past 20:00)
        
        // setting finished clock.end(), checks using the Time class, whether 
        // or not time is past 20:00
        
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        
        System.out.println("Thank you for playing. Good bye!");
    }

    /**
     * Print out the opening message for the player and spawns wizard outside too.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the KCL!");
        System.out.println("Lab at 09:00");
        System.out.println("Lecture at 13:00");
        System.out.println("Exam at 18:00");
        System.out.println("First go to office to collect lanyard and for more information.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(outside.getLongDescription());
        wizard.withWizard(currentRoom); // informs player that wizard also starts outside
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise. (returns true when quit command is run)
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = clock.end();

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
        else if (commandWord.equals("back")) {
            goBack(command);
        }
        else if(commandWord.equals("enter")) {
            enterRoom(command);
        }
        else if (commandWord.equals("wait")) {
            wait(command);
        }
        else if (commandWord.equals("restart")) {
            restart(command);
        }
        else if (commandWord.equals("take")) {
            pickUp(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("drink")) {
            drinkCoffee(command);
        }
        else if (commandWord.equals("revise")) {
            revise(command);
        }
        else if (commandWord.equals("results")) {
            wantToQuit = getResults(command);
        }
        else if (commandWord.equals("teleport")) {
            teleport(command);
        }
        else if (commandWord.equals("talk")) {
            talk(command);
        }
        else if (commandWord.equals("give")) {
            give(command);
        }
        // else command not recognised.
        return wantToQuit;
    }
    
    
    // implementations of user commands:
    
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    /**
     * Enables player to give items to characters
     * Currently only one character (wizard, who is looking for a sandwich)
     */
    private void give(Command command)
    {
        // 1 or 2 word commands here are not sufficient
        
        if (!command.hasSecondWord()) {
            System.out.println("Give what?");
            return;
        }
        
        if (!command.hasThirdWord()) {
            System.out.println("To who?");
            return;
        }
        
        // player must use a valid 3 word command to give item
        
        String recipient = command.getSecondWord(); // who player gives item to
        String item = command.getThirdWord(); // the item player is giving
        
        if (wizard.isPlayerWithWizard()) {
            if (item.equals("sandwich") && recipient.equals("wizard")) {
                if (!rewardGranted) {
                    System.out.println("Ah, it's you again... I knew you'd find me!");
                    System.out.println("**Wizard is very pleased**");
                    System.out.println("It is only right if I return the favour...");
                    System.out.println("****Thou have been given the Eyes of the Seer****");
                    System.out.println("**Your eyes begin to glow with a soft, ethereal light**");
                    System.out.println("Hope this helps ...");
                    calc.removePenalty(10); // reward
                    rewardGranted = true;
                }
                else {
                    System.out.println("****WIZARD BECOMES ANGRY****");
                    System.out.println("I HAVE ALREADY GRANTED YOU WITH YOUR REWARD"); // reduction of 
                    // penalty cannot be claimed from this method more than once
                }
                bag.remove(sandwich); // wizard takes sandwich, so player is no longer carrying it
            }
            else {
                System.out.println("***Wizard is of most confusion***");
            }
        }
        else {
            System.out.println("Find the wizzard first..."); // player must first
            // be in the same room as wizard to be able to carry out this
            // interaction
        }
        
    }
    
    /**
     * Talking to a character provides imformation to the player
     */
    private void talk(Command command)
    {
        boolean withWizard = wizard.isPlayerWithWizard();
        if (withWizard) {
            System.out.println("Ah, young one, I sense you've come seeking knowledge...");
            System.out.println("Bring me that deluxe sandwich from Greggs and I shall grant you with wisdom...");
        }
        else {
            System.out.println("Not quite sure what you mean... Talk to who?"); // must be in same room 
            // as wizard to be able to talk to him
        }
    }
    
    /**
     * Teleport command enables player to travel to a random room.
     * Can only be used when inside the teleporter room.
     * Boolean inRoom ensures player has entered the room (i.e teleport command can't be called when 
     * outside teleporter room.
     */
    private void teleport(Command command)
    {
        String roomName = currentRoom.getName();
        
        if (roomName.equals("the magic teleporter room") && inRoom) {
            int index = rand.nextInt(currentRoom.teleportableRooms()); // provides random room index from
            // list of rooms teleporter can teleport the player to
            Room nextRoom = currentRoom.destination(index);
            
        
            currentRoom = nextRoom; // changes the room player is currently in
            System.out.println("*************TELEPORTING************");
            System.out.println(currentRoom.getLongDescription());
            clock.updateDisplay(15); // teleporter takes 15 minutes regardless of where player is
            // teleported to
            prevCommands.clear(); // back command is made unavailable after teleport has been executed
            // (don't want player to be able to go back and forth between teleporter and random rooms)
            
            inRoom = false; // teleports player outside the next room (player hasn't entered room yet)
        }
        else
        {
            System.out.println("I don't quite know what you mean...."); // when teleport command is
            // called from elsewhere
        }
    }
    
    /**
     * getResults command enables user to get their exam results.
     * The earliest this command can be executed is at 20:00 (scheduled end of game is exam was writen).
     * @return true (to end the game) if results collected (i.e. game will not end from this command if
     * results cannot be collected.
     */
    private boolean getResults(Command command)
    {
        
        boolean end = clock.end();
        
        if (!end) {
            System.out.println("You have not taken the exam yet...");
            return false;
        }
        else {
            int score = calc.calcResults();
            test.calcGrade(score);
            return true;
        }
    }
    
    /**
     *revise command enables player to reduce penalty points via revision.
     *Can only be called when inside (simply being outside room is not sufficient) group study, 
     *independent study 1 or 2 rooms.
     *Player must also be carrying exam notes in their inventory to revise.
     */
    private void revise(Command command)
    {
        String name = currentRoom.getName();
        
        if ((name.equals("group study")||name.equals("independent study 1")||name.equals("independent study 2")) && inRoom) {
            if (bag.checkNotes()) {
                calc.removePenalty(1);
                clock.updateDisplay(20);
                System.out.println("**REVISED FOR 20 MINUTES**");
                System.out.println("To revise more use 'revise' command again...");
            }
            else if (!bag.checkNotes()){
                System.out.println("You can't revise without exam notes, go find them first...");
                // can't revise without exam notes
            }
        }
        else {
            System.out.println("You cannot revise here..."); // can't revise while not inside valid rooms
        }
    }
    
    /**
     * drinkCoffee command also removes penalty points.
     * Can only be executed if player is carrying coffee.
     */
    private void drinkCoffee(Command command)
    {
       if (!command.hasSecondWord()) {
           System.out.println("Drink what?"); // player must specify item they wish to drink
           return;
       }
       
       String drink = command.getSecondWord();
       currentItem = coffee;
       
       if (drink.equals("coffee")) {
           if (bag.checkCoffee()) {
               System.out.println("You have now drank your coffee...");
               bag.remove(coffee); // removes coffee from player's inventory after coffee is drank
               bag.setCoffee(false);
               calc.removePenalty(2);
           }
           else {
               System.out.println("You do not have coffee in your inventory");
           }
       }
       else {
           System.out.println("What do you mean?");
       }
       
    }
    
    /**
     * Picks up item and adds it to player's inventory.
     */
    private void pickUp(Command command)
    {
        if (!taken) {
            if (currentItem == null || !itemGiven ||!inRoom) {
                System.out.println("Take what?");
            }
            else if (!permission && itemGiven && inRoom && currentItem != null) {
                System.out.println("You do not have access to this item...");
            }
            else if (currentItem != null && itemGiven && permission) {
                bag.add(currentItem); // adds item to player's inventory
                itemGiven = false;
                taken = true;
            }
        }
        else if (taken) {
            System.out.println("You have already taken this item");
        }
        
        if (currentItem == lanyard) {
            bag.setLanyard(true);
        }
        else if (currentItem == coffee) {
            bag.setCoffee(true);
        }
        else if (currentItem == sandwich) {
            bag.setSandwich(true);
        }
        else if (currentItem == examNotes) {
            bag.setNotes(true);
        }
    }
    
    /**
     * Drops item and removes it from player's inventory.
     */
    private void drop(Command command)
    {
       if (!command.hasSecondWord()) { 
            // if there is no second word, we don't know what item to drop...
            System.out.println("Drop what?");
            return;
       }
       
       String item = command.getSecondWord();
       
       // removes item from player's inventory
       
       if (item.equals("lanyard")) {
           bag.remove(lanyard);
           bag.setLanyard(false);
       }
       else if (item.equals("notes")) {
           bag.remove(examNotes);
           bag.setNotes(false);
       }
       else if (item.equals("coffee")) {
           bag.remove(coffee);
           bag.setCoffee(false);
       }
       else if (item.equals("sandwich")) {
           bag.remove(sandwich);
           bag.setSandwich(false);
       }
       else {
            System.out.println("Item you wish to drop is not recognised in inventory");
       }
       
       
    }
    
    /**
     * This command takes you back all the way to the start. This will reset the game.
     * Any items you have before this command are taken away.
     */
    private void restart(Command command)
    {
        currentRoom = outside;
        clock = new Time();
        test = new Exam();
        calc = new Calculator();
        prevCommands.clear();
        bag.clearInventory();
        correctLab = whichLab();
        notesLocation = examNotesLocation();
        startingBooleans();
        counter = 0;
        printWelcome();
    }

    /**
     * Used to enter rooms.
     */
    private void enterRoom(Command command)
    {
        itemsPresent();
        String enter = currentRoom.getEnterInfo();
        String name = currentRoom.getName();
        boolean access = allowed(name);
        int late = 0;
        int penalty = 0;
        taken = false;
        permission = false;
        if (!inRoom) {
            if (name.equals("outside")) {
                System.out.println("Enter where?"); // there is no where to enter when outside(room)
            }
            else{
                if (((name.equals("computing lab 1"))||(name.equals("computing lab 2")))&& name.equals(correctLab)) {
                    if (access) {
                        System.out.println(enter);
                        inRoom = true; //since player has now entered the room
                        late = clock.calcLate("lab");
                        calc.lateInfo(late,"lab");
                        // penalty for being late (magnitude of penalty depends on how late you are)
                        if (late > 0 && late < 120) {
                            penalty = calc.calcPoints(late, "lab");
                            calc.addPenalty(penalty);
                        }
                    }
                    else {
                        System.out.println("You need your lanyard to enter"); // cannot enter labs without lanyard
                    } 
                    }
                else if((name.equals("computing lab 1"))||(name.equals("computing lab 2"))&&!name.equals(correctLab)) {
                    if (access) {
                        System.out.println("This is not you scheduled lab session"); // player must go to their designated lab
                    }
                    else {
                        System.out.println("You need your lanyard to enter");
                    } 
                }
                else if (name.equals(notesLocation)) {
                    System.out.println(enter);
                    inRoom = true;
                    if (!bag.checkNotes()) {
                        System.out.println("You seem to have found your exam notes...");
                        System.out.println(currentItem.getInfo());
                    }
                }
                else if (name.equals("Greggs")||(name.equals("the computing admin office"))||(name.equals("the canteen"))) {
                    System.out.println(enter);
                    inRoom = true;
                    System.out.println("There seems to be a queue.");
                }
                else if (name.equals("the lecture theater")) {
                    if (access) {
                        System.out.println(enter);
                        inRoom = true;
                        late = clock.calcLate("lecture");
                        calc.lateInfo(late,"lecture");
                        System.out.println("You have found the markscheme for the exam today....");
                        System.out.println(currentItem.getInfo());
                        // penalty for being late (magnitude of penalty depends on how late you are)
                        if (late > 0 && late < 120) {
                            penalty = calc.calcPoints(late, "lecture");
                            calc.addPenalty(penalty);
                        }
                    }
                    else {
                        System.out.println("You need your lanyard to enter");
                    }
                }
                else if (name.equals("the exam room")) {
                    if (access) {
                        System.out.println(enter);
                        inRoom = true;
                        late = clock.calcLate("exam");
                        calc.lateInfo(late,"exam");
                        // penalty for being late (magnitude of penalty depends on how late you are)
                        if (late > 0 && late < 120) {
                            penalty = calc.calcPoints(late,"exam");
                            calc.addPenalty(penalty);
                        }
                    }
                    else {
                        System.out.println("You need your lanyard to enter");
                    } 
                }
                else if (name.equals("the magic teleporter room")) {
                    System.out.println(enter);
                    inRoom = true;
                    System.out.println("***You weren't meant to find this***");
                    System.out.println("Use the teleport command to travel to a random room...");
                    System.out.println("It will cost you 15 mins though, regardless of wherever you teleport too.");
                    System.out.println("Please note back command will not be functional after you teleport.");
                }
                else {
                    if (access) {
                        System.out.println(enter);
                        inRoom = true;
                    }
                    else {
                        System.out.println("You need your lanyard to enter");
                    } 
                }
            }
        }
        else {
            System.out.println("You have already entered..."); // cannot enter a room when already in that room
        }
        itemsPresent();
    }
    
    /**
     * wait command used to wait in a queue. This must be done before obtaining certain items from certian rooms.
     */
    private void wait(Command command)
    {
        int time = calcWaitTime();
        String name = currentRoom.getName();
        if (time!=0 && inRoom) {
            System.out.println ("Waiting Time: " + time + " minutes.");
            clock.updateDisplay(time); // updates display due to time spent waiting
            interact();
            itemGiven = true;
        }
        else {
            System.out.println("Wait for what?");
        }
    }

    /** 
     * Tries to go in a direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {   
        
        
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        

        // Try to leave current room
        
        Room nextRoom = currentRoom.getExit(direction);
        int timeTaken = currentRoom.getTimeTaken(nextRoom);
    
        
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            prevCommands.add(direction);
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            clock.updateDisplay(timeTaken);
            counter += 1; // for every 3 moves the player makes, the wizard moves once
            wizard.withWizard(currentRoom); // checks is wizard is in same room as player
            wizard.moveWizard(counter); // checks if wizard should move
            taken = false; // any item inside this next room hasn't been taken yet
            inRoom = false; // doesn't take player inside room
        }
    }
    
    /**
     * goBack command takes player to the previous room they were in via doing the opposite of the previous command.
     */
    private void goBack(Command command)
    {
        if (prevCommands.size() > 0) {
            int lastIndex = prevCommands.size() - 1;
            String lastCommand = prevCommands.get(lastIndex);
        
            if (lastCommand.equals("north")){
            String back = "south";
            reverse(back);
            }
            if (lastCommand.equals("south")){
            String back = "north";
            reverse(back);
            }
            if (lastCommand.equals("east")){
            String back = "west";
            reverse(back);
            }
            if (lastCommand.equals("west")){
            String back = "east";
            reverse(back);
            }
            prevCommands.remove(lastIndex); // to prevent infinite looping (going back and forth between 2 rooms)
            taken = false; // any item inside this next room hasn't been taken yet
            inRoom = false; // doesn't take player inside room
        }
        else {
            System.out.println("Back where?"); // back command can't be used at the start (when still outside)
            // or straight after teleporting
            return;
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;
            // signal that we want to quit
        }
    }
    
    /**
     * This method is used to get the previous room and to update time display (according to how long it took
     * to get to prev room).
     * @param where is the exit key (opposite to what was found in previous command -> allows player to go back)
     */
    private void reverse(String where) {
        Room prevRoom = currentRoom.getExit(where);
        int timeTaken = currentRoom.getTimeTaken(prevRoom);
        currentRoom = prevRoom;
        System.out.println(currentRoom.getLongDescription());
        clock.updateDisplay(timeTaken);
        counter += 1; // for every 3 moves the player makes, the wizard moves once
        wizard.withWizard(currentRoom); // checks is wizard is in same room as player
        wizard.moveWizard(counter); // checks if wizard should move
    }
    
    /**
     * This method checks whether or not the player has access to a specific room. 
     * (some rooms need lanyard to enter).
     * @return true if player has access to a specific room.
     */
    private boolean allowed(String name)
    {
        if ((name.equals("the computing admin office"))||(name.equals("Greggs"))||(name.equals("the canteen"))||(name.equals("independent study 1"))||(name.equals("independent study 2"))||(name.equals("group study"))) {
            access = true;
        }
        else if ((name.equals("computing lab 1"))||(name.equals("computing lab 2"))||(name.equals("the exam room"))||(name.equals("the lecture theater"))) {
            if(bag.checkLanyard()) {
                access = true;
            }
            if(!bag.checkLanyard()) {
                access = false;
            }
        }
        
        return access;
    }
    
    /**
     * This randomly picks where exam notes item will be found.
     * Returns the name of which room notes will be found.
     */
    private String examNotesLocation()
    {
        fillNotesLocations();
        int notesIndex = rand.nextInt(locationNotes.size());
        String name = locationNotes.get(notesIndex);
        
        return name;
    }
    
    /**
     * This randomly picks which lab the player will be told to go to.
     * Returns the name of the lab that player will need to go to.
     */
    private String whichLab()
    {   
        fillLabs();
        int labIndex = rand.nextInt(labList.size());
        return labList.get(labIndex);
    }

    /**
     * Player can either be assigned to computing lab 1 or 2.
     * This method creates a list to store possible labs.
     */
    private void fillLabs()
    {
        labList.add("computing lab 1");
        labList.add("computing lab 2");
    }
    
    /**
     * Adds all the locations that player's exam notes can be found to locationNotes ArrayList.
     */
    private void fillNotesLocations()
    {
        locationNotes.add("independent study 1");
        locationNotes.add("independent study 2");
        locationNotes.add("group study");
    }
    
    /**
     * Some rooms have a queue, others do not.
     * @return true if room player has currently entered has a queue.
     */
    private boolean queue()
    {
        Room room = currentRoom;
        String name = currentRoom.getName();
        if (name.equals("the computing admin office")||name.equals("Greggs")||name.equals("the canteen")) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Generates random waiting time.
     */
    private int calcWaitTime()
    {
        if (queue()) {
            int time = rand.nextInt(15) + 1;
            return time;
        }
        else {
           return 0; 
        }
    }
    
    /**
     * States the interactions at different rooms.
     */
    private void interact()
    {
        String name = currentRoom.getName();
        if (name.equals("the computing admin office")) {
            System.out.println("Thank you for waiting!");
            System.out.println("Here is your Lanyard. Your scheduled lab is: " + correctLab);
            System.out.println(currentItem.getInfo());
        }
        else if (name.equals("Greggs"))  {
            System.out.println("Thank you for waiting!");
            System.out.println("Here is your free sandwich.");
            System.out.println(currentItem.getInfo());
        }
        else if (name.equals("the canteen")) {
            System.out.println("Thank you for waiting!");
            System.out.println("Here is your free coffee.");
            System.out.println(currentItem.getInfo());
        }
    }
    
    /**
     * Different rooms have different items.
     * This method assigns different items to currentItem when in different rooms.
     */
    private void itemsPresent()
    {
        String name = currentRoom.getName();
        
        if (name.equals("the computing admin office")) {
            currentItem = lanyard;
            permission = true;
        }
        else if (name.equals(notesLocation) && !bag.checkNotes()) {
            currentItem = examNotes;
            itemGiven = true;
            permission = true;
        }
        else if (name.equals("the canteen")) {
            currentItem = coffee;
            permission = true;
        }
        else if (name.equals("Greggs")) {
            currentItem = sandwich;
            permission = true;
        }
        else if (name.equals("the lecture theater")) {
            itemGiven = true;
            currentItem = examMarkScheme;
        }
        else {
            currentItem = null;
        }
    }
}
