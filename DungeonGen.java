// Help Sauron create dungeons to store captured hobbits

import java.util.ArrayList;

public class DungeonGen {
  private ArrayList<Room> dungeon;

  public DungeonGen() {
    dungeon = new ArrayList<>(120);
  }

  public void printDungeon() {
    //TODO: Print array in 10x10 grid
    for (Room room : dungeon) {
      System.out.println(room);
    }
  }

  public static void main(String[] args) {
    



    
  }
}