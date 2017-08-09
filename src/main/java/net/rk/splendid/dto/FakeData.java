package net.rk.splendid.dto;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class FakeData {
  static final Player[] FIXED_PLAYERS = new Player[]{
      new Player("Adam"),
      new Player("Barbara"),
      new Player("Claude"),
      new Player("Dominique")
  };
  private static final int FIXED_FACTORY_ROW_LENGTH = 5;
  private static final int COLOR_COUNT = 5;
  private static final int RESOURCE_STACK_LIMIT = 5;

  static Board CreateRandomBoard() {
    return new Board(new ResourceFactory[][]{
        CreateRandomResourceFactoryRow(0),
        CreateRandomResourceFactoryRow(1),
        CreateRandomResourceFactoryRow(2)},
        CreateRandomBoardResources(),
        new NoSelection());
  }

  private static ResourceFactory[] CreateRandomResourceFactoryRow(int row) {
    return
        IntStream.range(0, FIXED_FACTORY_ROW_LENGTH)
            .mapToObj((i) -> CreateRandomResourceFactory(row))
            .toArray(ResourceFactory[]::new);
  }

  private static ResourceFactory CreateRandomResourceFactory(int row) {
    return new ResourceFactory(
        (new Random()).nextInt(COLOR_COUNT),
        CreateRandomCost(GetMinCostForRow(row), GetMaxCostForRow(row)),
        GetRandomPoints(row));
  }

  private static int GetRandomPoints(int row) {
    return row + (new Random()).nextInt(row + 3);
  }

  private static int[] CreateRandomCost(int min, int max) {
    Random random = new Random();
    int costTotal = min + random.nextInt(max - min);
    return IntStream.generate(() -> random.nextInt(COLOR_COUNT))
        .limit(costTotal)
        .toArray();
  }

  private static int GetMinCostForRow(int row) {
    return 1 + (row * 2);
  }

  private static int GetMaxCostForRow(int row) {
    return 4 + (row * 3);
  }

  private static int[] CreateRandomBoardResources() {
    Random random = new Random();
    return IntStream.range(0, COLOR_COUNT)
        .mapToObj(
            (color) -> IntStream.generate(() -> color).limit(random.nextInt(RESOURCE_STACK_LIMIT + 1)))
        .reduce(IntStream::concat)
        .orElse(IntStream.empty())
        .toArray();
  }

  static PlayerState[] CreateRandomPlayerState(int playerCount) {
    return Stream.generate(FakeData::CreateRandomPlayerState)
        .limit(playerCount)
        .toArray(PlayerState[]::new);
  }

  private static PlayerState CreateRandomPlayerState() {
    return new PlayerState(CreateRandomPlayerHand());
  }

  private static PlayerHand CreateRandomPlayerHand() {
    return new PlayerHand(CreateRandomResourceFactoryRow(1), new int[0]);
  }
}
