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

import com.google.common.collect.Lists;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.exceptions.NoSuchActionException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class GameActionProviderTest {
  @Test
  public void constructorThrowsOnActionTypeConflict() {
    GameAction actionOne = new GameAction() {
      @Override public String getActionType() { return "foo"; }
      @Override public OfyGameState apply(GameActionContext context, OfyGameState state) { return null; }
    };
    GameAction actionTwo = new GameAction() {
      @Override public String getActionType() { return "bar"; }
      @Override public OfyGameState apply(GameActionContext context, OfyGameState state) { return null; }
    };
    GameAction actionThree = new GameAction() {
      @Override public String getActionType() { return "foo"; }
      @Override public OfyGameState apply(GameActionContext context, OfyGameState state) { return null; }
    };

    ArrayList<GameAction> actionList = Lists.newArrayList(actionOne, actionTwo, actionThree);
    try {
      new GameActionProvider(actionList);
      Assert.fail("Should have thrown IllegalActionException.");
    } catch (IllegalArgumentException exception) {
      // pass
    }
  }

  @Test
  public void getActionThrowsOnUnknownActionType() {
    GameAction actionOne = new GameAction() {
      @Override public String getActionType() { return "foo"; }
      @Override public OfyGameState apply(GameActionContext context, OfyGameState state) { return null; }
    };
    GameAction actionTwo = new GameAction() {
      @Override public String getActionType() { return "bar"; }
      @Override public OfyGameState apply(GameActionContext context, OfyGameState state) { return null; }
    };

    GameActionProvider provider = new GameActionProvider(Lists.newArrayList(actionOne, actionTwo));
    try {
      provider.getAction("baz");
      Assert.fail("Should have thrown NoSuchActionException.");
    } catch (NoSuchActionException exception) {
      // pass
    }
  }

  @Test
  public void getActionSuppliesInjectedInstances() {
    GameAction actionOne = new GameAction() {
      @Override public String getActionType() { return "foo"; }
      @Override public OfyGameState apply(GameActionContext context, OfyGameState state) { return null; }
    };
    GameAction actionTwo = new GameAction() {
      @Override public String getActionType() { return "bar"; }
      @Override public OfyGameState apply(GameActionContext context, OfyGameState state) { return null; }
    };

    GameActionProvider provider = new GameActionProvider(Lists.newArrayList(actionOne, actionTwo));

    Assert.assertSame(actionOne, provider.getAction("foo"));
    Assert.assertSame(actionTwo, provider.getAction("bar"));
  }
}
