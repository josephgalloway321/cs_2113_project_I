package dungeon_gen;

import java.util.ArrayList;
import java.util.Random;

public class Room {
  final String wall = "\uD83E\uDD86";
  final String opening = "\u2B1C";
  private String roomType;
  private ArrayList<Weapons> weapons;
  private ArrayList<Potions> potions;
  private ArrayList<Monsters> monsters;

  Random rand = new Random();

  public Room(String roomType) {
    if (roomType.equals("wall")) {
      this.roomType = wall;
    }
    else {
      this.roomType = opening;
      // In an open room, populate with items & monsters
      populateOpeningRoomWithItems();
      populateOpeningRoomWithMonsters();
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

  private void populateOpeningRoomWithMonsters() {
    // Randomly choose total number of monsters in this room
    int randValueToChooseNumMonsters = rand.nextInt(4);  // Values between 0 and 3, inclusive

    monsters = new ArrayList<>();

    for (int i = 0; i < randValueToChooseNumMonsters; i++) {
      Monsters monster = new Monsters();
      monsters.add(monster);
    }
  }

  public void printItemsAndMonsters() {
    if (this.roomType.equals(opening)) {
      System.out.println();

      if (weapons.size() > 0) {
        System.out.println("The following are weapons in this room: ");
        for (Weapons weapon: weapons) {
          System.out.println(weapon);
        }
      }
      else {
        System.out.println("There are no weapons in this room.");
      }
      
      if (potions.size() > 0) {
        System.out.println("The following are potions in this room: ");
        for (Potions potion: potions) {
          System.out.println(potion);
        }
      }
      else {
        System.out.println("There are no potions in this room.");
      }

      if (monsters.size() > 0) {
        System.out.println("The following are monsters in this room: ");
        for (Monsters monster : monsters) {
          System.out.println(monster);
        }
      }
      else {
        System.out.println("There are no monsters in this room.");
      }
    }
    else {
      System.out.println("This room is a wall, so it contains neither items nor monsters.");
    }
  }

  public String toString() {
    return roomType;
  }

  public ArrayList<Weapons> getWeaponsInRoom() {
    return this.weapons;
  }

  public ArrayList<Potions> getPotionsInRoom() {
    return this.potions;
  }

  public ArrayList<Monsters> getMonstersInRoom() {
    return this.monsters;
  }
}
