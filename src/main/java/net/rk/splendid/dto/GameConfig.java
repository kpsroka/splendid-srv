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

package net.rk.splendid.dto;

public final class GameConfig {
  private final GameRef gameRef;
  private final Player[] players;

  public GameConfig(GameRef gameRef, Player[] players) {
    this.gameRef = gameRef;
    this.players = players;
  }

  public GameRef getRef() { return gameRef; }

  public Player[] getPlayers() { return players; }
}
