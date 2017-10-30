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

import net.rk.splendid.dto.Player;

public class OfyPlayer {
  private String name;
  private int index;
  private boolean joined = false;

  private OfyPlayer() {}

  private OfyPlayer(String playerName, int playerIndex) {
    this.name = playerName;
    this.index = playerIndex;
  }

  int getIndex() {
    return index;
  }

  static Player toDto(OfyPlayer ofyPlayer) {
    return new Player(ofyPlayer.name);
  }

  public static OfyPlayer create(int index, String name) {
    return new OfyPlayer(name, index);
  }

  void setJoined() {
    this.joined = true;
  }

  public boolean hasJoined() {
    return this.joined;
  }

  void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
