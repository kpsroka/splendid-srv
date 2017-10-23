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

import com.google.common.collect.Lists;
import net.rk.splendid.dto.PlayerHand;
import net.rk.splendid.dto.ResourceFactory;
import org.springframework.util.Assert;

import java.util.List;

public class OfyPlayerHand {
  private List<OfyResourceFactory> factories = Lists.newArrayList();
  private OfyResourceMap resources = new OfyResourceMap();

  OfyPlayerHand() {}

  public static PlayerHand toDto(OfyPlayerHand ofyPlayerHand) {
    return new PlayerHand(
        ofyPlayerHand.factories.stream().map(OfyResourceFactory::toDto).toArray(ResourceFactory[]::new),
        OfyResourceMap.toResourceArray(ofyPlayerHand.resources));
  }

  public OfyResourceMap getResources() {
    return resources;
  }

  public OfyResourceMap getFactoryResources() {
    OfyResourceMap resourceMap = new OfyResourceMap();
    for (OfyResourceFactory factory : factories) {
      resourceMap = resourceMap.increment(factory.getResource());
    }
    return resourceMap;
  }

  public void setResources(OfyResourceMap resources) {
    this.resources = resources;
  }

  public void addFactory(OfyResourceFactory factory) {
    Assert.notNull(factory, "Attempting to add a null factory.");
    factories.add(factory);
  }

  static OfyPlayerHand create() {
    return new OfyPlayerHand();
  }

  public int getScore() {
    return factories.stream()
        .mapToInt(OfyResourceFactory::getPoints)
        .sum();
  }
}
