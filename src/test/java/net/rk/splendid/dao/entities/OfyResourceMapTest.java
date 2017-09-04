package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class OfyResourceMapTest {
  @Test
  public void incrementTest() {
    OfyResourceMap resourceMap = new OfyResourceMap()
        .increment(2)
        .increment(3)
        .increment(6)
        .increment(2);

    HashMap<Integer, Long> expectedMap = new HashMap<>();
    expectedMap.put(2, 2L);
    expectedMap.put(3, 1L);
    expectedMap.put(6, 1L);

    Assert.assertEquals(expectedMap, resourceMap.asMap());
  }

  @Test
  public void isZeroTest() {
    OfyResourceMap resourceMap = new OfyResourceMap();
    Assert.assertTrue(resourceMap.isZero());

    resourceMap = new OfyResourceMap(new ArrayList<>());
    Assert.assertTrue(resourceMap.isZero());

    resourceMap = resourceMap.increment(2);
    Assert.assertFalse(resourceMap.isZero());

    resourceMap = resourceMap.reduce(resourceMap);
    Assert.assertTrue(resourceMap.isZero());
  }

  @Test
  public void holdsTest() {
    OfyResourceMap resourceMap = new OfyResourceMap();
    Assert.assertTrue(resourceMap.holds(resourceMap));

    resourceMap = resourceMap.increment(2).increment(3).increment(5);
    Assert.assertTrue(resourceMap.holds(resourceMap));

    Assert.assertTrue(
        resourceMap.holds(new OfyResourceMap()));
    Assert.assertTrue(
        resourceMap.holds(new OfyResourceMap(Lists.newArrayList(2))));
    Assert.assertTrue(
        resourceMap.holds(new OfyResourceMap(Lists.newArrayList(3, 5))));

    Assert.assertFalse(
        resourceMap.holds(new OfyResourceMap(Lists.newArrayList(1))));
    Assert.assertFalse(
        resourceMap.holds(new OfyResourceMap(Lists.newArrayList(2, 2, 3))));
    Assert.assertFalse(
        resourceMap.holds(new OfyResourceMap(Lists.newArrayList(2, 3, 4, 5))));
  }

  @Test
  public void reduceTest() {
    OfyResourceMap resourceMap = new OfyResourceMap(Lists.newArrayList(1, 3, 5, 5));
    Map<Integer, Long> expectedMap = resourceMap.asMap();

    resourceMap = resourceMap.reduce(new OfyResourceMap());
    Assert.assertEquals(expectedMap, resourceMap.asMap());

    resourceMap = resourceMap.reduce(new OfyResourceMap(Lists.newArrayList(1, 5)));
    expectedMap = new HashMap<>();
    expectedMap.put(3, 1L);
    expectedMap.put(5, 1L);
    Assert.assertEquals(expectedMap, resourceMap.asMap());

    resourceMap = resourceMap.reduce(new OfyResourceMap(Lists.newArrayList(1)));
    Assert.assertEquals(expectedMap, resourceMap.asMap());
  }

  @Test
  public void joinTest() {
    OfyResourceMap resourceMap = new OfyResourceMap().join(new OfyResourceMap());
    Assert.assertTrue(resourceMap.asMap().isEmpty());

    OfyResourceMap joinedMap = new OfyResourceMap(Lists.newArrayList(2, 4, 6));
    resourceMap = resourceMap.join(joinedMap);

    Assert.assertEquals(joinedMap.asMap(), resourceMap.asMap());
    resourceMap = resourceMap.join(new OfyResourceMap(Lists.newArrayList(4, 6, 6, 8)));

    HashMap<Integer, Long> expectedMap = Maps.newHashMap();
    expectedMap.put(2, 1L);
    expectedMap.put(4, 2L);
    expectedMap.put(6, 3L);
    expectedMap.put(8, 1L);

    Assert.assertEquals(expectedMap, resourceMap.asMap());
  }
}
