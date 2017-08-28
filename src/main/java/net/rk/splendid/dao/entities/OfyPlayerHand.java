package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import net.rk.splendid.dto.PlayerHand;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class OfyPlayerHand {
  List<OfyResourceFactory> factories = Lists.newArrayList();
  OfyResourceMap resources;

  private OfyPlayerHand() {}

  static OfyPlayerHand fromDto(PlayerHand hand) {
    OfyPlayerHand ofyPlayerHand = new OfyPlayerHand();
    ofyPlayerHand.factories =
        Arrays.stream(hand.getFactories())
            .map(OfyResourceFactory::fromDto)
            .collect(Collectors.toList());

    ofyPlayerHand.resources = OfyResourceMap.fromResourceArray(hand.getResources());

    return ofyPlayerHand;
  }
}
