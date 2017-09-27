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

import net.rk.splendid.dto.PlayerHand;
import net.rk.splendid.dto.ResourceFactory;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class OfyPlayerHandTest {
  @Test
  public void dtoConversionTest() {
    OfyPlayerHand ofyHand = new OfyPlayerHand();
    ofyHand.setResources(new OfyResourceMap(Lists.newArrayList(1, 2, 4, 4)));
    ofyHand.addFactory(new OfyResourceFactory(3, 2, new OfyResourceMap(Lists.newArrayList(1))));
    ofyHand.addFactory(new OfyResourceFactory(4, 3, new OfyResourceMap(Lists.newArrayList(2, 2))));

    PlayerHand dtoHand = OfyPlayerHand.toDto(ofyHand);
    List<ResourceFactory> dtoFactories = Arrays.asList(dtoHand.getFactories());

    assertDtoFactoryProperties(
        dtoFactories.get(0), 3, 2, new int[] {1});
    assertDtoFactoryProperties(
        dtoFactories.get(1), 4, 3, new int[] {2, 2});

    Arrays.sort(dtoHand.getResources());
    Assert.assertArrayEquals(new int[] {1, 2, 4, 4}, dtoHand.getResources());
  }

  private void assertDtoFactoryProperties(
      ResourceFactory resourceFactory,
      int expectedColor,
      int expectedPoints,
      int[] expectedResourceCost) {
    Assert.assertEquals(expectedColor, resourceFactory.getColor());
    Assert.assertEquals(expectedPoints, resourceFactory.getPoints());

    Arrays.sort(expectedResourceCost);
    Arrays.sort(resourceFactory.getCost());
    Assert.assertArrayEquals(expectedResourceCost, resourceFactory.getCost());
  }
}
