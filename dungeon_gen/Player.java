package dungeon_gen;

import java.util.ArrayList;

public class Player {
  private ArrayList<Weapons> weaponsInventory;
  private ArrayList<Potions> potionsInventory;
  private final String player = "\uD83D\uDD34";
  private Weapons equippedWeapon;
  private int rowPosition;
  private int columnPosition;
  private int health;
  private int numMonstersDefeated;
  private int startingRowPosition;
  private int startingColumnPosition;
  
  public Player() {
    weaponsInventory = new ArrayList<Weapons>();
    potionsInventory = new ArrayList<Potions>();

    rowPosition = 0;
    columnPosition = 0;
    health = 1000;

    Weapons weapon = new Weapons();  // Give the new player a random weapon 
    weaponsInventory.add(weapon);
    this.equippedWeapon = weaponsInventory.get(0);  // Equip player w/ weapon

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

  public void setStartingPosition(int row, int col) {
    this.startingRowPosition = row;
    this.startingColumnPosition = col;
  }

  public int getStartingRowPosition() {
    return this.startingRowPosition;
  }

  public int getStartingColumnPosition() {
    return this.startingColumnPosition;
  }

  public int getNumMonstersDefeated() {
    return this.numMonstersDefeated;
  }

  public void incrementNumMonstersDefeated() {
    this.numMonstersDefeated++;
  }

  public Weapons getEquippedWeapon() {
    return this.equippedWeapon;
  }

  public void setEquippedWeapon(Weapons weapon) {
    this.equippedWeapon = weapon;
  }

  public void dequipWeapon() {
    this.equippedWeapon = null;
  }

  public void addWeaponToInventory(Weapons weapon) {
    this.weaponsInventory.add(weapon);
  }

  public void addPotionToInventory(Potions potion) {
    this.potionsInventory.add(potion);
  }

  public void healPlayer(int healValue) {
    this.health += healValue;
  }

  public void damagePlayer(int damageValue) {
    this.health -= damageValue;
  }

  public int getPlayerHealth() {
    return this.health;
  }

  public ArrayList<Weapons> getWeaponsInvetory() {
    return this.weaponsInventory;
  }

  public ArrayList<Potions> getPotionsInvetory() {
    return this.potionsInventory;
  }

  public String toString() {
    return player;
  }
}
