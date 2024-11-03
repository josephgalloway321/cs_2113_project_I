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
      System.out.println("Merp!");
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
    dungeon.createWallRow();
    dungeon.createRandomRoomRows();
    dungeon.createWallRow();
    dungeon.changeFirstAndLastColumnsToWalls();
    dungeon.changeLastColumnToWallWithOpenings();
    dungeon.checkRowsForIsolatedRooms();
    dungeon.printDungeon();

  }
}