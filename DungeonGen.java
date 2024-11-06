// Help Sauron create dungeons to store captured hobbits

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
        if (currentRoom.equals(openRoom.toString()) && 
            (leftRoom.equals(wallRoom.toString()) && rightRoom.equals(wallRoom.toString()))) {
          dungeon.get(i).set(j+1, openRoom);
          dungeon.get(i).set(j-1, openRoom);
        }
      }
    }
    
  }

  public void readRowsToSave(int genCount) {
    try {
      FileWriter fw = new FileWriter(String.valueOf(genCount) + ".txt");
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          fw.write(dungeon.get(i).get(j).toString());
        }
        fw.write("\n");
      }
      fw.write("\n");
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

  public void generateDungeon(int genCount) {
    createWallRow();
    createRandomRoomRows();
    createWallRow();
    changeFirstAndLastColumnsToWalls();
    changeLastColumnToWallWithOpenings();
    checkRowsForIsolatedRooms();
    printDungeon();
    readRowsToSave(genCount);
  }

  public void loadDungeon(String fileName) {
    Room wallRoom;
    Room openingRoom;

    try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
      char[] rowChar;
      String readRow = "";
      while ((readRow = bf.readLine()) != null) {
        ArrayList<Room> row = new ArrayList<Room>(12);
        rowChar = new char[22];  // Walls take up 2 characters each
        boolean isWall = false;

        for (int i = 0; i < readRow.length(); i++) {
          if (isWall) {
            isWall = false;
            continue;  // Skip next value to avoid duplicate wall
          }

          rowChar[i] = readRow.charAt(i);
          if (Character.codePointAt(rowChar, i) == 11036) {
            openingRoom = new Room("opening");
            row.add(openingRoom);
          }
          else {
            wallRoom = new Room("wall");
            row.add(wallRoom);
            isWall = true;
          }
        }
        rowChar = null;
        dungeon.add(row);
      }
      bf.close();
    }
    catch (IOException e) {
      System.out.println("Error loading file...");
    }
  }

  public static void main(String[] args) {
    Scanner scnr = new Scanner(System.in);
    DungeonGen dungeon;
    //ArrayList<ArrayList<Room>> loadedDungeon;
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
      }
      else if (userChoice == 1) {
        try {
          System.out.print("Please enter the filename: ");
          String fileName = scnr.next();
          dungeon = new DungeonGen();
          dungeon.loadDungeon(fileName);   
          dungeon.printDungeon();       
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