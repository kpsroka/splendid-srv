/*
 * Copyright 2017 K. P. Sroka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.PlayerState;
import net.rk.splendid.game.FactoryGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OfyGameState {
  private OfyGameStatus gameStatus;
  private int round;
  private OfyBoard board;
  private Map<String, OfyPlayerState> playerState = new HashMap<>();

  private OfyGameState() {}

  public static GameState toDto(
      OfyGameState ofyGameState,
      OfyGameConfig ofyGameConfig,
      String playerToken) {
    List<String> playerTokensOrdered = ofyGameConfig.getPlayerTokensOrdered(playerToken);

    return new GameState(
        ofyGameState.gameStatus.name(),
        ofyGameState.round,
        getCurrentPlayerIndex(ofyGameState, ofyGameConfig.getPlayer(playerToken).getIndex()),
        OfyBoard.toDto(ofyGameState.board),
        playerTokensOrdered.stream()
            .map(ofyGameState.playerState::get)
            .map(OfyPlayerState::toDto)
            .toArray(PlayerState[]::new));
  }

  private static int getCurrentPlayerIndex(OfyGameState gameState, int currentPlayerIndex) {
    int playerCount = gameState.playerState.size();
    return (playerCount + gameState.round - currentPlayerIndex) % playerCount;
  }

  public OfyBoard getBoard() {
    return board;
  }

  public OfyPlayerState getPlayerState(String playerToken) {
    if (playerState.containsKey(playerToken)) {
      return playerState.get(playerToken);
    } else {
      throw new IllegalArgumentException("No player with token " + playerToken);
    }
  }

  public OfyGameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(OfyGameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public void incrementRound() {
    round++;
  }

  public static OfyGameState create(
      String[] playerRefs,
      FactoryGenerator factoryGenerator) {
    OfyGameState gameState = new OfyGameState();
    gameState.gameStatus = OfyGameStatus.UNKNOWN;
    gameState.round = 0;
    gameState.board = OfyBoard.create(factoryGenerator);
    gameState.playerState =
        Arrays.stream(playerRefs)
            .collect(Collectors.toMap(Function.identity(), ref -> OfyPlayerState.create()));
    return gameState;
  }

  public int getRound() {
    return round;
  }
}
