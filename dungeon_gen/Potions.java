package dungeon_gen;

import java.util.Random;

public class Potions {
  private String effectType;
  private int uses;
  private int power;

  Random rand = new Random();

  public Potions() {
    setEffectType();
    setUses();
    setPower();
  }

  private void setEffectType() {
    int randValueToChooseEffect = rand.nextInt(2);  // Values between 0 and 1, inclusive
    
    if (randValueToChooseEffect == 0) {
      this.effectType = "heal";
    }
    else {
      this.effectType = "repair";
    }    
  }

  private void setUses() {
    this.uses = rand.nextInt(10) + 1;  // Values between 1 and 10, inclusive
  }

  private void setPower() {
    this.power = rand.nextInt(10) + 1;  // Values between 1 and 10, inclusive
  }

  public String toString() {
    return "Effect Type: " + effectType + "\nPower: " + power + "\nUses: " + uses + "\n";
  }
}