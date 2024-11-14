package dungeon_gen;

import java.util.ArrayList;
import java.util.Random;

public class Room {
  final String wall = "\uD83E\uDD86";
  final String opening = "\u2B1C";
  private String roomType;
  private ArrayList<Weapons> weapons;
  private ArrayList<Potions> potions;

  Random rand = new Random();

  public Room(String roomType) {
    if (roomType.equals("wall")) {
      this.roomType = wall;
    }
    else {
      this.roomType = opening;
      // In an open room, populate with items
      populateOpeningRoomWithItems();
    }
  }

  private void populateOpeningRoomWithItems() {
    // Randomly choose total number of items in this room
    int randValueToChooseNumItems = rand.nextInt(4);  // Values between 0 and 3, inclusive
    //System.out.println("\nTotal Items for Room = " + randValueToChooseNumItems);

    weapons = new ArrayList<>();
    potions = new ArrayList<>();

    for (int i = 0; i < randValueToChooseNumItems; i++) {
      int randValueToChooseWeaponOrPotion = rand.nextInt(2);  // Values between 0 and 1, inclusive 
      // 0 for weapon, 1 for potion
      if (randValueToChooseWeaponOrPotion == 0) {
        Weapons weapon = new Weapons();
        weapons.add(weapon);
      }
      else {
        Potions potion = new Potions();
        potions.add(potion);
      }

    }

  }

  public void printItems() {
    System.out.println("The following are weapons in this room: ");
    for (Weapons weapon: weapons) {
      System.out.println(weapon);
    }

    System.out.println("The following are potions in this room: ");
    for (Potions potion: potions) {
      System.out.println(potion);
    }
  }

  public String toString() {
    return roomType;
  }
}
