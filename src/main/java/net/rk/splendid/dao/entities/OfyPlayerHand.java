package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import net.rk.splendid.dto.PlayerHand;
import net.rk.splendid.dto.ResourceFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class OfyPlayerHand {
  private List<OfyResourceFactory> factories = Lists.newArrayList();
  private OfyResourceMap resources;

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

  public static PlayerHand toDto(OfyPlayerHand ofyPlayerHand) {
    return new PlayerHand(
        ofyPlayerHand.factories.stream().map(OfyResourceFactory::toDto).toArray(ResourceFactory[]::new),
        OfyResourceMap.toResourceArray(ofyPlayerHand.resources));
  }
}
