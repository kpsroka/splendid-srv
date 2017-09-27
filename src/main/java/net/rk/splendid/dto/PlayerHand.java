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

public final class PlayerHand {
  private ResourceFactory[] factories;
  private int[] resources;

  public PlayerHand(ResourceFactory[] factories, int[] resources) {
    this.factories = factories;
    this.resources = resources;
  }

  public ResourceFactory[] getFactories() {
    return factories;
  }

  public int[] getResources() {
    return resources;
  }
}
