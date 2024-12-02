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

  public Potions(String effectType, int power, int uses) {
    this.effectType = effectType;
    this.uses = uses;
    this.power = power;
  }

  private void setEffectType() {
    int randValueToChooseEffect = rand.nextInt(2);  // Values between 0 and 1, inclusive
    
    if (randValueToChooseEffect == 0) {
      this.effectType = "Healing";
    }
    else {
      this.effectType = "Repairing";
    }    
  }

  private void setUses() {
    this.uses = rand.nextInt(10) + 1;  // Values between 1 and 10, inclusive
  }

  private void setPower() {
    this.power = rand.nextInt(10) + 1;  // Values between 1 and 10, inclusive
  }

  public String getEffectType() {
    return this.effectType;
  }

  public int getPower() {
    return this.power;
  }

  public void decrementUse() {
    if (this.uses != 0) {
      this.uses--;
    }
  }

  public String toStringPotionType() {
    return "Potion of " + this.effectType;
  }

  public String toStringPotionInfo() {
    return this.effectType + ", Power: " + this.power + ", Uses: " + this.uses;
  }

  public String toString() {
    return "_!EffectType!" + effectType + "!Power!" + power + "!Uses!" + uses;
  }
}
