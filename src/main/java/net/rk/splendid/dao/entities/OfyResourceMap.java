package net.rk.splendid.dao.entities;

import com.google.common.collect.Maps;
import com.googlecode.objectify.annotation.Stringify;
import com.googlecode.objectify.stringifier.Stringifier;

import java.util.Arrays;
import java.util.Map;
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

class OfyResourceMap {
  @Stringify(IntegerStringifier.class)
  Map<Integer, Long> resourceMap = Maps.newHashMap();

  private OfyResourceMap() {}

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
}
