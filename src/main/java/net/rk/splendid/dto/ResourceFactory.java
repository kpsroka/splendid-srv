package net.rk.splendid.dto;

public final class ResourceFactory {
  private final int color;
  private final int[] cost;
  private final int points;

  public ResourceFactory(int color, int[] cost, int points) {
    this.color = color;
    this.cost = cost;
    this.points = points;
  }

  public int getColor() {
    return color;
  }

  public int[] getCost() {
    return cost;
  }

  public int getPoints() {
    return points;
  }
}
