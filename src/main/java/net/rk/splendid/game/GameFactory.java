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

import net.rk.splendid.dao.entities.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

@Component
public class GameFactory implements Function<NewGameOptions, GameEntity> {

  private final FactoryGenerator factoryGenerator;

  @Inject
  public GameFactory(FactoryGenerator factoryGenerator) {
    this.factoryGenerator = factoryGenerator;
  }

  @Override
  public GameEntity apply(NewGameOptions options) {
    OfyPlayer[] players = new OfyPlayer[options.getNumberOfPlayers()];
    Arrays.setAll(players, i -> OfyPlayer.create(i,"Waiting…"));
    String[] playerRefs = new String[options.getNumberOfPlayers()];
    Arrays.setAll(playerRefs, i -> UUID.randomUUID().toString());

    OfyGameConfig gameConfig = OfyGameConfig.create(players, playerRefs);

    OfyGameState gameState = OfyGameState.create(playerRefs, factoryGenerator);
    gameState.setGameStatus(OfyGameStatus.PREPARING);

    return new GameEntity(gameConfig, gameState);
  }
}
