package dungeon_gen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Monsters {
  private String monsterType;
  private int health;
  private String weaponType;
  private Weapons equippedWeapon;
  private Boolean isMonsterDead;

  Random rand = new Random();

  public Monsters() {
    this.isMonsterDead = false;
    setMonsterType();
    setHealth();
    setWeaponType();  // Weapon also equipped in this method
  }

  public Monsters(String monsterType, int health, String weaponType) {
    this.isMonsterDead = false;
    this.monsterType = monsterType;
    this.health = health;
    this.weaponType = weaponType;
    equippedWeapon = new Weapons(this.weaponType);
  }

  public boolean getIsMonsterDead() {
    return this.isMonsterDead;
  }

  public void setIsMonsterDead(boolean isMonsterDead) {
    this.isMonsterDead = isMonsterDead;
  }

  private void setMonsterType() {
    int randValueToChooseMonster = rand.nextInt(42);  // Values between 0 and 42
    int lineCounter = 0;

    String monstersFilePath = "monsters.txt";
    try (BufferedReader bf = new BufferedReader(new FileReader(monstersFilePath))) {
      String readLine = "";
      while ((readLine = bf.readLine()) != null) {
        if (lineCounter == randValueToChooseMonster) {
          this.monsterType = readLine;
        }
        lineCounter++;
      }

      bf.close();
    }
    catch (IOException e) {
      System.out.println("Error loading file: " + e.getMessage());
    }
  }

  private void setHealth() {
    this.health = rand.nextInt(11) + 10;
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
          equippedWeapon = new Weapons(this.weaponType);
        }
        lineCounter++;
      }

      bf.close();
    }
    catch (IOException e) {
      System.out.println("Error loading file: " + e.getMessage());
    }
  }

  public void damageMonster(int damageValue) {
    this.health -= damageValue;
  }

  public int getHealth() {
    return this.health;
  }

  public Weapons getWeapon() {
    return this.equippedWeapon;
  }

  public String toStringMonsterType() {
    return this.monsterType;
  }

  public String toStringMonsterInfo() {
    return this.monsterType + ", Health: " + this.health + ", Weapon: " + this.weaponType;
  }

  public String toString() {
    return "_!MonsterType!" + monsterType + "!Health!" + health + "!WeaponForMonster!" + weaponType;
  }
}
