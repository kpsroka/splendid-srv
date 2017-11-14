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

package net.rk.splendid;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Named;
import java.io.Serializable;

@Named
@Scope(
    scopeName = WebApplicationContext.SCOPE_SESSION,
    proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonSessionParameters implements Serializable, RequestContext {
  private String gameRef;
  private String playerToken;

  public CommonSessionParameters() {}

  void setGameRef(String gameRef) {
    this.gameRef = gameRef;
  }

  void setPlayerToken(String playerToken) {
    this.playerToken = playerToken;
  }

  @Override
  public String getGameRef() {
    if (gameRef != null) {
      return gameRef;
    } else {
      throw new NullPointerException("[@" + this.hashCode() + "] Null gameRef.");
    }
  }

  @Override
  public String getPlayerToken() {
    if (playerToken != null) {
      return playerToken ;
    } else {
      throw new NullPointerException("[@" + this.hashCode() + "] Null playerToken.");
    }
  }
}
