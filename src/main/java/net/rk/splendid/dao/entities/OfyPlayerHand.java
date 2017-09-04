package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import net.rk.splendid.dto.PlayerHand;
import net.rk.splendid.dto.ResourceFactory;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfyPlayerHand {
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
}
