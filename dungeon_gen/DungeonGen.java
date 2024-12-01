// Help Sauron create dungeons to store captured hobbits

package dungeon_gen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class DungeonGen {
  private ArrayList<ArrayList<Room>> dungeon;
  private Random rand = new Random();
  private int randomValue;

  public DungeonGen() {
    dungeon = new ArrayList<ArrayList<Room>>(120);
  }

  public void createWallRow() {
    ArrayList<Room> row = new ArrayList<Room>(12);
    
    for (int i = 0; i < 10; i++) {
      Room room = new Room("wall");
      row.add(room);
    }

    dungeon.add(row);
  }

  public void createRandomRoomRows() {
    for (int i = 0; i < 8; i++) {
      ArrayList<Room> row = new ArrayList<Room>(12);

      for (int j = 0; j < 10; j++) {
        randomValue = rand.nextInt(8);
        if (randomValue == 0) {
          Room room = new Room("wall");
          row.add(room);
        }
        else {
          Room room = new Room("opening");
          row.add(room);
        }
      }

      dungeon.add(row);
    }
  }

  public void changeFirstAndLastColumnsToWalls() {
    for (int i = 0; i < 10; i++) {
      Room room = new Room("wall");
      dungeon.get(i).set(0, room);
      dungeon.get(i).set(9, room);
    }
  }

  public void changeLastColumnToWallWithOpenings() {
    boolean isOpeningExist = false;
    Room openRoom = new Room("opening");

    for (int i = 1; i < 9; i++) {
      randomValue = rand.nextInt(8);
      if (randomValue == 0 || randomValue == 5 || randomValue == 7) {
        dungeon.get(i).set(7, openRoom);
        dungeon.get(i).set(8, openRoom);
        dungeon.get(i).set(9, openRoom);
        isOpeningExist = true;
      }
    }
    if (!isOpeningExist) {
      dungeon.get(4).set(7, openRoom);
      dungeon.get(4).set(8, openRoom);
      dungeon.get(4).set(9, openRoom);
    }
  }

  public void checkRowsForIsolatedRooms() {
    Room openRoom = new Room("opening");
    Room wallRoom = new Room("wall");

    for (int i = 1; i < 9; i++) {
      for (int j = 1; j < 9; j++) {
        String currentRoom = dungeon.get(i).get(j).toString();
        String leftRoom = dungeon.get(i).get(j-1).toString();
        String rightRoom = dungeon.get(i).get(j+1).toString();
        String topRoom = dungeon.get(i-1).get(j).toString();
        String bottomRoom = dungeon.get(i+1).get(j).toString();
        if (currentRoom.equals(openRoom.toString()) && 
            (leftRoom.equals(wallRoom.toString()) && rightRoom.equals(wallRoom.toString()))) {
          dungeon.get(i).set(j+1, openRoom);
          dungeon.get(i).set(j-1, openRoom);
        }
        if (currentRoom.equals(wallRoom.toString()) && 
        (topRoom.equals(openRoom.toString()) && bottomRoom.equals(openRoom.toString())) && rightRoom.equals(openRoom.toString()) && leftRoom.equals(wallRoom.toString())) {
          dungeon.get(i).set(j, openRoom);
        }
      }
    }
    
  }

  public void readRowsToSave(int genCount) {
    try {
      FileWriter fw = new FileWriter(String.valueOf(genCount) + ".txt");
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          Room currentRoom = dungeon.get(i).get(j);

          if (currentRoom.toString().equals("\u2B1C")) {
            fw.write(currentRoom.toString());  // Save room type
            ArrayList<Weapons> weapons = currentRoom.getWeaponsInRoom();
            ArrayList<Potions> potions = currentRoom.getPotionsInRoom();
            ArrayList<Monsters> monsters = currentRoom.getMonstersInRoom();

            // Save each weapon 
            for (Weapons weapon : weapons) {
              fw.write(weapon.toString());
            }

            // Save each potion
            for (Potions potion : potions) {
              fw.write(potion.toString());
            }

            // Save each monster
            for (Monsters monster : monsters) {
              fw.write(monster.toString());
            }
            fw.write("\n");
          }

          else {
            fw.write(currentRoom.toString() + "\n");  // Save room type
          }
        }
      }
      fw.close();
    }
    catch (IOException e) {
      System.out.println("Error writing to file...");
    }
  }

  public void printDungeon() {
    for (ArrayList<Room> row : dungeon) {
      for (Room room : row) {
        System.out.print(room.toString());
      }
      System.out.println();
    }
  }

  public Room getRoom(int row, int col) {
    return dungeon.get(row).get(col);
  }

  public void generateDungeon(int genCount) {
    createWallRow();
    createRandomRoomRows();
    createWallRow();
    changeFirstAndLastColumnsToWalls();
    changeLastColumnToWallWithOpenings();
    checkRowsForIsolatedRooms();
    //printDungeon();
    readRowsToSave(genCount);
  }

  public void loadDungeon(String fileName) {
    Room wallRoom;
    Room openingRoom;

    fileName += ".txt";

    try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
      String readRow = "";

      ArrayList<Room> row = new ArrayList<Room>(12);  // 10 Rooms at most per row

      while ((readRow = bf.readLine()) != null) {
        if (readRow.codePointAt(0) == 11036) {
          ArrayList<Weapons> weapons = new ArrayList<>();
          ArrayList<Potions> potions = new ArrayList<>();
          ArrayList<Monsters> monsters = new ArrayList<>();

          if (readRow.length() < 2) {
            //System.out.println("\nEmpty open room found!");
            continue;
          }

          String[] itemsAndMonsters = readRow.split("_");
          
          for (String weaponPotionMonster : itemsAndMonsters) {
            String[] weaponInfo;
            String[] potionInfo;
            String[] monsterInfo;
            
            if (weaponPotionMonster.contains("WeaponType")) {
              String weaponType = "";
              int damage = 0;
              int durability = 0;

              weaponInfo = weaponPotionMonster.split("!");
              weaponType = weaponInfo[2];
              damage = Integer.parseInt(weaponInfo[4]);
              durability = Integer.parseInt(weaponInfo[6]);

              Weapons weapon = new Weapons(weaponType, damage, durability);
              weapons.add(weapon);
            }
            else if (weaponPotionMonster.contains("EffectType")) {
              String effectType = "";
              int power = 0;
              int uses = 0;

              potionInfo = weaponPotionMonster.split("!");
              effectType  = potionInfo[2];
              power = Integer.parseInt(potionInfo [4]);
              uses = Integer.parseInt(potionInfo [6]);

              Potions potion = new Potions(effectType, power, uses);
              potions.add(potion);
            }
            else if (weaponPotionMonster.contains("MonsterType")) {
              String monsterType = "";
              int health = 0;
              String weaponType = "";

              monsterInfo = weaponPotionMonster.split("!");
              monsterType  = monsterInfo[2];
              health = Integer.parseInt(monsterInfo[4]);
              weaponType = monsterInfo[6];

              Monsters monster = new Monsters(monsterType, health, weaponType);
              monsters.add(monster);
            }
          }

          openingRoom = new Room("opening", weapons, potions, monsters);
          row.add(openingRoom);
          //openingRoom.printItemsAndMonsters();
        }
        else {
          wallRoom = new Room("wall");
          row.add(wallRoom);
        }

        // Check if row has ten objects, 
        // If so, then add row to dungeon then create new row
        if (row.size() >= 10) {
          dungeon.add(row);
          row = new ArrayList<Room>(12);
        }
      }
      bf.close();
    }
    catch (IOException e) {
      System.out.println("Error loading file...");
    }
  }

  public static void printDungeonWithPlayer(DungeonGen dungeon, Player player) {
    int playerRowPosition = player.getRowPosition();
    int playerColumnPosition = player.getColumnPosition();

    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (row == playerRowPosition && col == playerColumnPosition) {
          System.out.print(player.toString());
        }
        else {
          System.out.print(dungeon.getRoom(row, col));
        }
      }
      System.out.println();
    }
    dungeon.getRoom(playerRowPosition, playerColumnPosition).printItemsAndMonsters();
  }

  public static void setPlayerInitialPosition(DungeonGen dungeon, Player player) {
    Random rand = new Random();
    Room room;
    int randRow = 0;
    int randCol = 0;
    boolean isRoomWall = true;

    while (isRoomWall) {
      randRow = rand.nextInt(10);
      randCol = rand.nextInt(10);
      room = dungeon.getRoom(randRow, randCol);

      if (room.toString().equals("\u2B1C")) {
        isRoomWall = false;
        player.setPosition(randRow, randCol);
      }
    }
  }

  public static void interactWithRoom(Room room, Player player, Scanner scnr) {
    int userChoice = 0;
    while (userChoice != 11) {
      System.out.println("\nPlease type in a room interaction option from the list below:\n" + 
                           "0) Examine monster [index]\n" + 
                           "1) Examine weapon [index]\n" +
                           "2) Examine potion [index]\n" +
                           "3) Take weapon [index]\n" + 
                           "4) Take potion [index]\n" + 
                           "5) Weapon inventory [index]\n" +
                           "6) Potion inventory [index]\n" + 
                           "7) Inventory\n" + 
                           "8) Use from weapon inventory [index]\n" +
                           "9) Use from potion inventory [index]\n" + 
                           "10) Attack [index]\n" + 
                           "11) Return");
      userChoice = scnr.nextInt();

      if (userChoice == 0) {
        System.out.println("Which monster do you want to examine? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(room.getMonstersInRoom().get(userChoice).toStringMonsterInfo());
      }
      else if (userChoice == 1) {
        System.out.println("Which weapon do you want to examine? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(room.getWeaponsInRoom().get(userChoice).toStringWeaponInfo());
      }
      else if (userChoice == 2) {
        System.out.println("Which potion do you want to examine? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(room.getPotionsInRoom().get(userChoice).toStringPotionInfo());
      }
      else if (userChoice == 3) {
        System.out.println("Which weapon do you want to take? (Enter the index): ");
        userChoice = scnr.nextInt();
        player.addWeaponToInventory(room.getWeaponsInRoom().get(userChoice));
        System.out.println("Added " + room.getWeaponsInRoom().get(userChoice).toStringWeaponInfo() + " to weapons inventory");
      }
      else if (userChoice == 4) {
        //TODO: Complete option
        System.out.println("Which potion do you want to take? (Enter the index): ");
        userChoice = scnr.nextInt();
        player.addPotionToInventory(room.getPotionsInRoom().get(userChoice));
        System.out.println("Added " + room.getPotionsInRoom().get(userChoice).toStringPotionInfo() + " to potions inventory");
      }
      else if (userChoice == 5) {
        //TODO: Complete option
        System.out.println("Which weapon inventory item do you want to know more about? (Enter the index): ");
        userChoice = scnr.nextInt();

      }
      else if (userChoice == 6) {
        //TODO: Complete option
        System.out.println("Which potion inventory item do you want to know more about? (Enter the index): ");
        userChoice = scnr.nextInt();

      }
      else if (userChoice == 7) {
        //TODO: Complete option
        System.out.println("Printing current inventory...");
      }
      else if (userChoice == 8) {
        //TODO: Complete option
        System.out.println("Which weapon inventory item do you want to use? (Enter the index): ");
        userChoice = scnr.nextInt();

      }
      else if (userChoice == 9) {
        //TODO: Complete option
        System.out.println("Which potion inventory item do you want to use? (Enter the index): ");
        userChoice = scnr.nextInt();

      }
      else if (userChoice == 10) {
        //TODO: Complete option
        System.out.println("Which monster do you want to attack? (Enter the index: )");
        userChoice = scnr.nextInt();

      }
      else if (userChoice == 11) {
        System.out.println("Returning to main menu...");
      }
      else {
        System.out.println("Please choose a valid option");
      }
    }
  }

  public static void beginGame(DungeonGen dungeon, Scanner scnr) {
    Player player = new Player();
    char userChoice = ' ';

    System.out.println("Beginning game...\n");
    setPlayerInitialPosition(dungeon, player);
    printDungeonWithPlayer(dungeon, player);

    while (userChoice != 'q') {
      System.out.println("\nEnter one of the following characters:\n" +
                        "- Move player (n, s, e, or w)\n" + 
                        "- Interact with room (i)\n" +
                        "- Quit game (q)");
      userChoice = scnr.next().charAt(0);
      int playerRowPosition = player.getRowPosition();
      int playerColumnPosition = player.getColumnPosition();
      
      if (userChoice == 'n' || userChoice == 'N') {
        if (player.getRowPosition() == 0) {
          System.out.println("Move failed");
        }
        else if (dungeon.getRoom(playerRowPosition - 1, playerColumnPosition).toString().equals("\u2B1C")) {
          player.setPosition(playerRowPosition - 1, playerColumnPosition);
          printDungeonWithPlayer(dungeon, player);
        }
        else {
          System.out.println("Move failed");
        }
      }
      else if (userChoice == 's' || userChoice == 'S') {
        if (player.getRowPosition() == 9) {
          System.out.println("Move failed");
        }
        else if (dungeon.getRoom(playerRowPosition + 1, playerColumnPosition).toString().equals("\u2B1C")) {
          player.setPosition(playerRowPosition + 1, playerColumnPosition);
          printDungeonWithPlayer(dungeon, player);
        }
        else {
          System.out.println("Move failed");
        }
      }
      else if (userChoice == 'e' || userChoice == 'E') {
        if (player.getColumnPosition() == 9) {
          System.out.println("Move failed");
        }
        else if (dungeon.getRoom(playerRowPosition, playerColumnPosition + 1).toString().equals("\u2B1C")) {
          player.setPosition(playerRowPosition, playerColumnPosition + 1);
          printDungeonWithPlayer(dungeon, player);
        }
        else {
          System.out.println("Move failed");
        }
      }
      else if (userChoice == 'w' || userChoice == 'W') {
        if (player.getColumnPosition() == 0) {
          System.out.println("Move failed");
        }
        else if (dungeon.getRoom(playerRowPosition, playerColumnPosition - 1).toString().equals("\u2B1C")) {
          player.setPosition(playerRowPosition, playerColumnPosition - 1);
          printDungeonWithPlayer(dungeon, player);
        }
        else {
          System.out.println("Move failed");
        }
      }
      else if (userChoice == 'q' || userChoice == 'Q') {
        System.out.println("Ending game...\n");
      }
      else if (userChoice == 'i' || userChoice == 'I') {
        interactWithRoom(dungeon.getRoom(playerRowPosition, playerColumnPosition), player, scnr);
      }
      else {
        System.out.println("Please choose a valid option!");
      }
    }
  }

  public static void main(String[] args) {
    Scanner scnr = new Scanner(System.in);
    DungeonGen dungeon;
    int userChoice = -1;
    int genCount = 0;

    while (userChoice != 2) {
      // Decision from user
      System.out.print("Enter 0 to generate, 1 to load, or 2 to exit: ");
      userChoice = scnr.nextInt();
      System.out.println();

      if (userChoice == 0) {
        genCount++;
        System.out.println("Generating dungeon " + genCount + "...\n");
        
        try {
          FileWriter fw = new FileWriter("dungeon_data.txt");
          fw.write(String.valueOf(genCount));
          fw.close();
        } 
        catch (IOException e) {
          System.out.println("Could not open file...");
        }
        dungeon = new DungeonGen();
        dungeon.generateDungeon(genCount);
        beginGame(dungeon, scnr);  // Begin game
      }
      else if (userChoice == 1) {
        try {
          System.out.print("Please enter the filename (Only the number): ");
          String fileName = scnr.next();
          dungeon = new DungeonGen();
          dungeon.loadDungeon(fileName);   
          dungeon.printDungeon();       
          beginGame(dungeon, scnr);  // Begin game
        }
        catch (InputMismatchException e) {
          System.out.println("Error: Incorrect filename\n");
        }
      }
      else if (userChoice == 2) {
        System.out.println("Exiting program...\n");
      }
      else {
        System.out.println("Please enter a valid choice");
      }
    }
    scnr.close();
  }
}