package dungeon_gen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Weapons {
  private int damage;
  private int durability;
  private String weaponType;

  Random rand = new Random();

  public Weapons() {
    setWeaponType();
    setDamage();
    setDurability();
  }

  public Weapons(String weaponType, int damage, int durability) {
    this.damage = damage;
    this.durability = durability;
    this.weaponType = weaponType;
  }

  private void setWeaponType() {
    int randValueToChooseWeapon = rand.nextInt(42);  // Values between 0 and 42
    int lineCounter = 0;

    String weaponsFilePath = "weapons.txt";
    try (BufferedReader bf = new BufferedReader(new FileReader(weaponsFilePath))) {
      String readLine = "";
      while ((readLine = bf.readLine()) != null) {
        if (lineCounter == randValueToChooseWeapon) {
          this.weaponType = readLine;
        }
        lineCounter++;
      }

      bf.close();
    }
    catch (IOException e) {
      System.out.println("Error loading file: " + e.getMessage());
    }
  }

  private void setDamage() {
    this.damage = rand.nextInt(10) + 1;  // Values between 1 and 10, inclusive
  }

  private void setDurability() {
    this.durability = rand.nextInt(21) + 10;  // Values between 10 and 30, inclusive
  }

  public String toStringWeaponType() {
    return this.weaponType;
  }

  public String toStringWeaponInfo() {
    return this.weaponType + ", Damage: " + this.damage + ", Durability: " + this.durability;
  }

  public String toString() {
    return "_!WeaponType!" + weaponType + "!Damage!" + damage + "!Durability!" + durability;
  }
}
