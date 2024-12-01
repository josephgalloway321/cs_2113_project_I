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

  public Room(String roomType, ArrayList<Weapons> weapons, ArrayList<Potions> potions, ArrayList<Monsters> monsters) {
    this.roomType = opening;
    this.weapons = weapons;
    this.potions = potions;
    this.monsters = monsters;
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

      if (monsters.size() > 0) {
        System.out.print("Monsters: ");
        for (int i = 0; i < monsters.size(); i++) {
          System.out.print(monsters.get(i).toStringMonsterType() + " " + i);
          if (i < monsters.size() - 1) {
            System.out.print(", ");
          }
        }
        System.out.println();
      }
      else {
        System.out.println("Monsters: None");
      }

      if (weapons.size() > 0) {
        System.out.print("Weapons: ");
        for (int i = 0; i < weapons.size(); i++) {
          System.out.print(weapons.get(i).toStringWeaponType() + " " + i);
          if (i < weapons.size() - 1) {
            System.out.print(", ");
          }
        }
        System.out.println();
      }
      else {
        System.out.println("Weapons: None");
      }
      
      if (potions.size() > 0) {
        System.out.print("Potions: ");
        for (int i = 0; i < potions.size(); i++) {
          System.out.print(potions.get(i).toStringPotionType() + " " + i);
          if (i < potions.size() - 1) {
            System.out.print(", ");
          }
        }
        System.out.println();
      }
      else {
        System.out.println("Potions: None");
      }
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
