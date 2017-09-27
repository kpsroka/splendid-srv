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

import net.rk.splendid.dto.PlayerState;

public class OfyPlayerState {
  private OfyPlayerHand playerHand;

  private OfyPlayerState() {}

  public static PlayerState toDto(OfyPlayerState ofyPlayerState) {
    return new PlayerState(OfyPlayerHand.toDto(ofyPlayerState.playerHand));
  }

  public OfyPlayerHand getHand() {
    return playerHand;
  }

  static OfyPlayerState create() {
    OfyPlayerState playerState = new OfyPlayerState();
    playerState.playerHand = OfyPlayerHand.create();
    return playerState;
  }
}
