package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import net.rk.splendid.dto.PlayerHand;
import net.rk.splendid.dto.ResourceFactory;
import org.springframework.util.Assert;

import java.util.List;

public class OfyPlayerHand {
  private List<OfyResourceFactory> factories = Lists.newArrayList();
  private OfyResourceMap resources = new OfyResourceMap();

  OfyPlayerHand() {}

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

  public static OfyPlayerHand create() {
    return new OfyPlayerHand();
  }
}
