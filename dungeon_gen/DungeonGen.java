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

  public static void monstersInRoomAttackPlayer(ArrayList<Monsters> roomMonsters, Player player) {
    if (roomMonsters.size() > 0) {
      System.out.println("\nThe monsters in this room have attacked!");
      System.out.println("Player health: " + player.getPlayerHealth());
  
      for (Monsters monster : roomMonsters) {
        if (monster.getHealth() > 0) {
          System.out.println(monster.toStringMonsterType() + " attacked player!");
          player.damagePlayer(monster.getWeapon().getDamage());
          System.out.println("Player health after attack: " + player.getPlayerHealth());
        }
        else {
          System.out.println(monster.toStringMonsterType() + "'s health is less than 1, so it can't attack");
        }
      }
    }
    else {
      System.out.println("There are no monsters in this room to attack the player");
    }
    System.out.println();
  }

  public static void interactWithRoom(Room room, Player player, Scanner scnr) {
    // TODO: Assumes user will input a valid index
    int userChoice = 0;
    while (userChoice != 12) {
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
                           "11) See currently equipped weapon\n" + 
                           "12) Return");
      userChoice = scnr.nextInt();

      if (userChoice == 0) {
        System.out.println("\nWhich monster do you want to examine? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(room.getMonstersInRoom().get(userChoice).toStringMonsterInfo());
      }
      else if (userChoice == 1) {
        System.out.println("\nWhich weapon do you want to examine? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(room.getWeaponsInRoom().get(userChoice).toStringWeaponInfo());
      }
      else if (userChoice == 2) {
        System.out.println("\nWhich potion do you want to examine? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(room.getPotionsInRoom().get(userChoice).toStringPotionInfo());
      }
      else if (userChoice == 3) {
        System.out.println("\nWhich weapon do you want to take? (Enter the index): ");
        userChoice = scnr.nextInt();
        player.addWeaponToInventory(room.getWeaponsInRoom().get(userChoice));
        System.out.println("Added " + room.getWeaponsInRoom().get(userChoice).toStringWeaponInfo() + " to weapons inventory");
        monstersInRoomAttackPlayer(room.getMonstersInRoom(), player);
      }
      else if (userChoice == 4) {
        System.out.println("\nWhich potion do you want to take? (Enter the index): ");
        userChoice = scnr.nextInt();
        player.addPotionToInventory(room.getPotionsInRoom().get(userChoice));
        System.out.println("Added " + room.getPotionsInRoom().get(userChoice).toStringPotionInfo() + " to potions inventory");
        monstersInRoomAttackPlayer(room.getMonstersInRoom(), player);
      }
      else if (userChoice == 5) {
        System.out.println("\nWhich weapon inventory item do you want to know more about? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(player.getWeaponsInvetory().get(userChoice).toStringWeaponInfo());
      }
      else if (userChoice == 6) {
        System.out.println("\nWhich potion inventory item do you want to know more about? (Enter the index): ");
        userChoice = scnr.nextInt();
        System.out.println(player.getPotionsInvetory().get(userChoice).toStringPotionInfo());
      }
      else if (userChoice == 7) {
        System.out.println("\nPrinting current inventory...");
        ArrayList<Weapons> currentRoomWeapons = room.getWeaponsInRoom();
        ArrayList<Potions> currentRoomPotions = room.getPotionsInRoom();

        for (int i = 0; i < currentRoomWeapons.size(); i++) {
          System.out.print(currentRoomWeapons.get(i).toStringWeaponType() + " " + i);
        }
        System.out.println();

        for (int i = 0; i < currentRoomPotions.size(); i++) {
          System.out.print(currentRoomPotions.get(i).toStringPotionType() + " " + i);
        }
        System.out.println();

      }
      else if (userChoice == 8) {
        System.out.println("\nWhich weapon inventory item do you want to use? (Enter the index): ");
        userChoice = scnr.nextInt();
        player.setEquippedWeapon(player.getWeaponsInvetory().get(userChoice));
        System.out.println(player.getEquippedWeapon().toStringWeaponType());
        monstersInRoomAttackPlayer(room.getMonstersInRoom(), player);
      }
      else if (userChoice == 9) {
        System.out.println("\nWhich potion inventory item do you want to use? (Enter the index): ");
        userChoice = scnr.nextInt();
        Potions potionSelected = player.getPotionsInvetory().get(userChoice);

        if (potionSelected.getEffectType().equals("Healing")) {
          System.out.println("Player health: " + player.getPlayerHealth());
          player.healPlayer(potionSelected.getPower());
          potionSelected.decrementUse();
          System.out.println("Player health after healing: " + player.getPlayerHealth());
        }
        else if (potionSelected.getEffectType().equals("Repairing")) {
          System.out.println("Weapon durability: " + player.getEquippedWeapon().getDurability());
          player.getEquippedWeapon().repairWeapon(potionSelected.getPower());
          potionSelected.decrementUse();
          System.out.println("Weapon durability after repairing: " + player.getEquippedWeapon().getDurability());
        }
        monstersInRoomAttackPlayer(room.getMonstersInRoom(), player);
      }
      else if (userChoice == 10) {
        System.out.println("\nWhich monster do you want to attack? (Enter the index: )");
        userChoice = scnr.nextInt();
        Monsters selectedMonster = room.getMonstersInRoom().get(userChoice);
        Weapons equippedWeapon = player.getEquippedWeapon();

        if (equippedWeapon == null) {
          System.out.println("Monster health: " + selectedMonster.getHealth());
          selectedMonster.damageMonster(1);
          System.out.println("Monster health after open hand slap from player: " + selectedMonster.getHealth());
        }
        else {
          System.out.println("Monster health: " + selectedMonster.getHealth());
          selectedMonster.damageMonster(equippedWeapon.getDamage());
          equippedWeapon.decrementDurability();
          System.out.println(selectedMonster.toStringMonsterType() + " dealt " + equippedWeapon.getDamage() + " damage");
          System.out.println("Monster health: " + selectedMonster.getHealth());
          if (equippedWeapon.getIsWeaponBroken()) {
            player.dequipWeapon();
            System.out.println("Player's weapon broken");
          }
        }
        monstersInRoomAttackPlayer(room.getMonstersInRoom(), player);
      }
      else if (userChoice == 11) {
        System.out.println("\nCurrently equipped weapon: " + player.getEquippedWeapon().toStringWeaponInfo());
      }
      else if (userChoice == 12) {
        System.out.println("\nReturning to main menu...");
      }
      else {
        System.out.println("\nPlease choose a valid option");
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
          monstersInRoomAttackPlayer(dungeon.getRoom(playerRowPosition, playerColumnPosition).getMonstersInRoom(), player);
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
          monstersInRoomAttackPlayer(dungeon.getRoom(playerRowPosition, playerColumnPosition).getMonstersInRoom(), player);
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
          monstersInRoomAttackPlayer(dungeon.getRoom(playerRowPosition, playerColumnPosition).getMonstersInRoom(), player);
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
          monstersInRoomAttackPlayer(dungeon.getRoom(playerRowPosition, playerColumnPosition).getMonstersInRoom(), player);
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