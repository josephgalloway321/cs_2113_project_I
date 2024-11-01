// Help Sauron create dungeons to store captured hobbits

import java.util.ArrayList;

public class DungeonGen {
  private ArrayList<Room> dungeon;

  public DungeonGen() {
    dungeon = new ArrayList<>(120);
  }

  public void addRoom(Room room) {
    dungeon.add(room);
  }

  public void printDungeon() {
    //TODO: Print array in 10x10 grid
    for (Room room : dungeon) {
      System.out.print(room.toString());
    }
    System.out.println();
  }

  public static void main(String[] args) {
    DungeonGen dungeon = new DungeonGen();

    Room object1 = new Room("wall");
    Room object2 = new Room("opening");
    dungeon.addRoom(object1);
    dungeon.addRoom(object2);

    dungeon.printDungeon();
  }
}