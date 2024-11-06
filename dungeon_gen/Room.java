package dungeon_gen;

public class Room {
  final String wall = "\uD83E\uDD86";
  final String opening = "\u2B1C";
  private String roomType;

  public Room(String roomType) {
    if (roomType.equals("wall")) {
      this.roomType = wall;
    }
    else {
      this.roomType = opening;
    }
  }

  public String toString() {
    return roomType;
  }
}
