// Help Sauron create dungeons to store captured hobbits
/* Algorithm
 * 1) Create 10 rows in 2D arraylist with 10 random rooms for each row
 * 2) Go through each row again and check the rooms to make sure each room is connected to another
 *   + Starting on the 2nd room of the 2nd row, if current room has an opening on the top and left side, change current room to an opening
 * 3) ...
 */

import java.util.ArrayList;
import java.util.Random;

public class DungeonGen {
  private ArrayList<ArrayList<Room>> dungeon;
  private Random rand = new Random();
  private int randomValue;

  public DungeonGen() {
    dungeon = new ArrayList<ArrayList<Room>>(120);
  }

  public void addRow(ArrayList<Room> row) {
    dungeon.add(row);
  }

  public void createFirstAndLastRow() {
    ArrayList<Room> row = new ArrayList<Room>(12);
    
    for (int i = 0; i < 10; i++) {
      Room room = new Room("wall");
      row.add(room);
    }

    dungeon.add(row);
    
  }

  public void createRows() {
    for (int i = 0; i < 8; i++) {
      ArrayList<Room> row = new ArrayList<Room>(12);

      for (int j = 0; j < 10; j++) {
        randomValue = rand.nextInt(2);
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

  public void changeFirstColumnToWall() {
    for (int i = 0; i < 10; i++) {
      Room room = new Room("wall");
      dungeon.get(i).set(0, room);
    }
  }

  public void checkRows() {
    for (int i = 1; i < 9; i++) {
      for (int j = 1; j < 9; j++) {
        String currentRoom = dungeon.get(i).get(j).toString();
        String topRoom = dungeon.get(i-1).get(j).toString();
        String bottomRoom = dungeon.get(i+1).get(j).toString();
        String leftRoom = dungeon.get(i).get(j-1).toString();
        String rightRoom = dungeon.get(i).get(j+1).toString();
        if (currentRoom.equals("\uD83E\uDD86") && 
            topRoom.equals("\u2B1C") && bottomRoom.equals("\u2B1C") || leftRoom.equals("\u2B1C") && rightRoom.equals("\u2B1C") ||
            topRoom.equals("\u2B1C") && leftRoom.equals("\u2B1C") || topRoom.equals("\u2B1C") && rightRoom.equals("\u2B1C") || 
            topRoom.equals("\uD83E\uDD86") && bottomRoom.equals("\uD83E\uDD86") && leftRoom.equals("\uD83E\uDD86") && rightRoom.equals("\u2B1C") ||
            topRoom.equals("\uD83E\uDD86") && bottomRoom.equals("\uD83E\uDD86") && leftRoom.equals("\u2B1C") && rightRoom.equals("\uD83E\uDD86")) {
          Room room = new Room("opening");
          dungeon.get(i).set(j, room);
        }
      }
    }
  }

  public void randomlyPlaceWalls() {
    for (int i = 1; i < 9; i++) {
      for (int j = 2; j < 9; j++) {
        randomValue = rand.nextInt(4);
        String currentRoom = dungeon.get(i).get(j).toString();
        String topRoom = dungeon.get(i-1).get(j).toString();
        String bottomRoom = dungeon.get(i+1).get(j).toString();
        String leftRoom = dungeon.get(i).get(j-1).toString();
        String rightRoom = dungeon.get(i).get(j+1).toString();
        if (currentRoom.equals("\u2B1C") && randomValue == 1 &&
                 topRoom.equals("\u2B1C") && bottomRoom.equals("\u2B1C") && leftRoom.equals("\u2B1C") && rightRoom.equals("\u2B1C")) {
          Room room = new Room("wall");
          dungeon.get(i).set(j, room);
        }
      }
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

  public static void main(String[] args) {
    DungeonGen dungeon = new DungeonGen();

    // Generate dungeon
    dungeon.createFirstAndLastRow();
    dungeon.createRows();
    dungeon.createFirstAndLastRow();
    dungeon.changeFirstColumnToWall();
    System.out.println("Before checking: ");
    dungeon.printDungeon();

    dungeon.checkRows();
    dungeon.randomlyPlaceWalls();
    System.out.println("\nAfter checking: ");
    dungeon.printDungeon(); 
  }
}