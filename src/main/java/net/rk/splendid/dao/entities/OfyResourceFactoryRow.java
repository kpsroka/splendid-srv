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
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

class OfyResourceFactoryRow {
  private List<OfyResourceFactory> resourceFactoryList = new ArrayList<>();

  private OfyResourceFactoryRow() {}

  static ResourceFactory[] toDto(OfyResourceFactoryRow ofyFactoryRow) {
    return ofyFactoryRow.resourceFactoryList.stream()
        .map(OfyResourceFactory::toDto)
        .toArray(ResourceFactory[]::new);
  }

  OfyResourceFactory getFactory(int index) {
    return resourceFactoryList.get(index);
  }

  void setFactory(int index, OfyResourceFactory resourceFactory) {
    Assert.notNull(resourceFactory, "Attempting to set null factory.");
    resourceFactoryList.set(index, resourceFactory);
  }

  public static OfyResourceFactoryRow create(List<OfyResourceFactory> factories) {
    OfyResourceFactoryRow factoryRow = new OfyResourceFactoryRow();
    factoryRow.resourceFactoryList.addAll(factories);
    return factoryRow;
  }
}
