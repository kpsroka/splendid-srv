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

import net.rk.splendid.dto.ResourceFactory;

public class OfyResourceFactory {
  private int color;
  private int points;
  private OfyResourceMap cost;

  private OfyResourceFactory() {}

  public OfyResourceFactory(int color, int points, OfyResourceMap cost) {
    this.color = color;
    this.points = points;
    this.cost = cost;
  }

  public static ResourceFactory toDto(OfyResourceFactory ofyResourceFactory) {
    return new ResourceFactory(
        ofyResourceFactory.color,
        OfyResourceMap.toResourceArray(ofyResourceFactory.cost),
        ofyResourceFactory.points);
  }

  public OfyResourceMap getCost() {
    return cost;
  }

  int getResource() {
    return color;
  }
}
