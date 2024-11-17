package dungeon_gen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Monsters {
  private String monsterType;
  private int health;
  private String weaponType;

  Random rand = new Random();

  public Monsters() {
    setMonsterType();
    setHealth();
    setWeaponType();
  }

  public Monsters(String monsterType, int health, String weaponType) {
    this.monsterType = monsterType;
    this.health = health;
    this.weaponType = weaponType;
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
        }
        lineCounter++;
      }

      bf.close();
    }
    catch (IOException e) {
      System.out.println("Error loading file: " + e.getMessage());
    }
  }

  public String toString() {
    return "_-MonsterType-" + monsterType + "-Health-" + health + "-MonsterWeaponType-" + weaponType;
  }
}
