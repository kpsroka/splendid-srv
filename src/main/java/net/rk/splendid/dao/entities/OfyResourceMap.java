package net.rk.splendid.dao.entities;

import com.google.common.collect.Maps;
import com.googlecode.objectify.annotation.Stringify;
import com.googlecode.objectify.stringifier.Stringifier;

import java.util.Map;

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
  Map<Integer, Integer> resourceMap = Maps.newHashMap();
}
