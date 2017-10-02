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

package net.rk.splendid.game;

import org.springframework.util.Assert;

public final class GameActionContext {
  private final String payload;
  private final String playerToken;

  public GameActionContext(String payload, String playerToken) {
    Assert.notNull(payload, "Null payload given.");
    Assert.notNull(playerToken, "Null playerToken given.");
    this.payload = payload;
    this.playerToken = playerToken;
  }

  String getPayload() {
    return payload;
  }

  String getPlayerToken() {
    return playerToken;
  }
}
