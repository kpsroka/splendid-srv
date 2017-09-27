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

public final class GameActions {
  private static final String TAKE_RESOURCES = "TakeResources";
  private static final String TAKE_FACTORY = "TakeFactory";

  private GameActions() {}

  public static GameAction GetAction(String name, String payload) {
    switch (name) {
      case GameActions.TAKE_RESOURCES:
        return new TakeResourcesAction(payload);
      case GameActions.TAKE_FACTORY:
        return new TakeFactoryAction(payload);
      default:
        return NoOp();
    }
  }

  private static GameAction NoOp() {
    return (token, state) -> state;
  }
}
