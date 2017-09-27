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

import com.google.common.collect.Maps;
import com.googlecode.objectify.annotation.Stringify;
import com.googlecode.objectify.stringifier.Stringifier;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class IntegerStringifier implements Stringifier<Integer> {

  @Override
  public String toString(Integer obj) {
    return obj.toString();
  }

  @Override
  public Integer fromString(String str) {
    return Integer.parseInt(str);
  }
}

public class OfyResourceMap {
  public static List<Integer> COLORS =
      IntStream.range(0, 5).boxed().collect(Collectors.toList());
  private static Long INITIAL_BOARD_RESOURCE_COUNT = 5L;

  @Stringify(IntegerStringifier.class)
  private Map<Integer, Long> resourceMap = Maps.newHashMap();

  OfyResourceMap() {}

  public OfyResourceMap(List<Integer> resourceList) {
    this.resourceMap = resourceList.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  private OfyResourceMap(OfyResourceMap source) {
    this.resourceMap = new HashMap<>(source.resourceMap);
  }

  static int[] toResourceArray(OfyResourceMap ofyResourceMap) {
    return ofyResourceMap.resourceMap.entrySet().stream()
        .map((e) -> createFilledArray(e.getValue(), e.getKey()))
        .flatMapToInt(Arrays::stream)
        .toArray();
  }

  private static int[] createFilledArray(Long length, Integer value) {
    int[] array = new int[length.intValue()];
    Arrays.fill(array, value);
    return array;
  }

  OfyResourceMap increment(int resource) {
    OfyResourceMap increased = new OfyResourceMap(this);
    increased.resourceMap.put(
        resource,
        increased.resourceMap.getOrDefault(resource, 0L) + 1);
    return increased;
  }

  public OfyResourceMap join(OfyResourceMap addend) {
    OfyResourceMap joined = new OfyResourceMap();
    joined.resourceMap =
        Stream.concat(this.resourceMap.entrySet().stream(), addend.resourceMap.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    return joined;
  }

  public OfyResourceMap reduce(OfyResourceMap subtrahend) {
    OfyResourceMap reduced = new OfyResourceMap(this);
    reduced.resourceMap.replaceAll(
        (key, minuend) -> Math.max(0L, minuend - subtrahend.resourceMap.getOrDefault(key, 0L)));
    return reduced;
  }

  public boolean isZero() {
    Set<Long> values = new HashSet<>(resourceMap.values());
    return values.isEmpty() || (values.size() == 1 && values.contains(0L));
  }

  public boolean holds(OfyResourceMap other) {
    return Maps.filterKeys(
        other.resourceMap,
        resource -> this.resourceMap.getOrDefault(resource, 0L) < other.resourceMap.get(resource))
        .isEmpty();
  }

  public Map<Integer, Long> asMap() {
    return Maps.newHashMap(Maps.filterValues(this.resourceMap, value -> value != 0));
  }

  static OfyResourceMap createInitialBoardMap() {
    OfyResourceMap resourceMap = new OfyResourceMap();
    for (Integer resource : COLORS) {
      resourceMap.resourceMap.put(resource, INITIAL_BOARD_RESOURCE_COUNT);
    }
    return resourceMap;
  }

  @Override
  public String toString() {
    return resourceMap.toString();
  }
}
