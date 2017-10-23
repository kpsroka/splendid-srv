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

import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyResourceFactory;
import net.rk.splendid.dao.entities.OfyResourceMap;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GameFactoryTest {
  @Test
  public void createsGameWithGivenNumberOfPlayers() {
    FactoryGenerator factoryGenerator = integer ->
        new OfyResourceFactory(0, 0, new OfyResourceMap(Lists.newArrayList()));

    GameFactory factory = new GameFactory(factoryGenerator);
    Assert.assertEquals(
        3,
        factory.apply(new NewGameOptions(3))
            .getGameConfig()
            .getPlayersOrdered()
            .size());

    Assert.assertEquals(
        5,
        factory.apply(new NewGameOptions(5))
            .getGameConfig()
            .getPlayersOrdered()
            .size());
  }

  @Test
  public void createsGameWithNoPlayersJoined() {
    FactoryGenerator factoryGenerator = integer ->
        new OfyResourceFactory(0, 0, new OfyResourceMap(Lists.newArrayList()));

    GameFactory factory = new GameFactory(factoryGenerator);
    factory.apply(new NewGameOptions(6))
        .getGameConfig()
        .getPlayersOrdered()
        .values()
        .forEach(
            (player) -> Assert.assertFalse(player.hasJoined())
        );
  }
}
