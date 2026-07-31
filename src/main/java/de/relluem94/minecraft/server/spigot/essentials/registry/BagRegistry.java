package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class BagRegistry {

  private final List<BagTypeEntry> bagTypeEntryList = new ArrayList<>();

  /**
   *
   * @param bagTypes List of BagTypeEntry
   */
  public BagRegistry(List<BagTypeEntry> bagTypes) {
    bagTypeEntryList.addAll(bagTypes);
  }
}