package de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared;

import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import org.bukkit.block.Block;
import org.jspecify.annotations.NonNull;

public class BlockProcessor {

  private final int blocksPerTick;
  private long currentDelay = 0;
  private int counter = 0;

  public BlockProcessor(int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
  }

  public void process(@NonNull Block block, @NonNull BlockService blockService) {
    blockService.addLocation(block.getLocation(), currentDelay);
    counter++;
    if (counter >= blocksPerTick) {
      currentDelay++;
      counter = 0;
    }
  }
}