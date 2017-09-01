package net.rk.splendid.dao.entities;

import com.google.common.collect.Maps;
import com.googlecode.objectify.annotation.Stringify;
import com.googlecode.objectify.stringifier.Stringifier;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
  @Stringify(IntegerStringifier.class)
  private Map<Integer, Long> resourceMap = Maps.newHashMap();

  OfyResourceMap() {}

  private OfyResourceMap(OfyResourceMap source) {
    this.resourceMap = new HashMap<>(source.resourceMap);
  }

  static OfyResourceMap fromResourceArray(int[] resources) {
    OfyResourceMap ofyResourceMap = new OfyResourceMap();
    ofyResourceMap.resourceMap = Arrays.stream(resources)
        .boxed()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    return ofyResourceMap;
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

  public OfyResourceMap increase(int resource, int value) {
    OfyResourceMap increased = new OfyResourceMap(this);
    increased.resourceMap.put(
        resource,
        increased.resourceMap.getOrDefault(resource, 0L) + value);
    return increased;
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
        resource -> this.resourceMap.getOrDefault(resource, 0L) >= other.resourceMap.get(resource))
        .isEmpty();
  }
}
