// Help Sauron create dungeons to store captured hobbits
/* Algorithm
 * 1) Create 10 rows in 2D arraylist with 10 random rooms for each row
 * 2) Go through each row again using the following subalgorithm
 *   + Starting on the 2nd room, if my current room has walls on all sides, change room to my left to an opening
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

  public void createRows() {
    for (int i = 0; i < 10; i++) {
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
    dungeon.createRows();

    dungeon.printDungeon(); 
  }
}