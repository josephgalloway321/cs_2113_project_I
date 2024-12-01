package dungeon_gen;

import java.util.ArrayList;

public class Player {
  private ArrayList<Weapons> weaponsInventory;
  private ArrayList<Potions> potionsInventory;
  private final String player = "\uD83D\uDD34";
  private String equippedWeapon;
  private int rowPosition = 0;
  private int columnPosition = 0;

  public Player() {
    weaponsInventory = new ArrayList<Weapons>();
    potionsInventory = new ArrayList<Potions>();

    //System.out.println(player);
    Weapons weapon = new Weapons();  // Give the new player a random weapon 
    weaponsInventory.add(weapon);
    this.equippedWeapon = weaponsInventory.get(0).toStringWeaponType();  // Equip player w/ weapon

    Potions potion = new Potions();  // Give the new player a random potion
    this.potionsInventory.add(potion);
  }

  public void setPosition(int row, int col) {
    this.rowPosition = row;
    this.columnPosition = col;
  }

  public int getRowPosition() {
    return this.rowPosition;
  }

  public int getColumnPosition() {
    return this.columnPosition;
  }

  public String toString() {
    return player;
  }
}
