package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BetterBlockDrop implements ListenerConstruct {

  private final Material[] ores = {Material.DIAMOND_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE,
      Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE, Material.DEEPSLATE_COAL_ORE,
      Material.DEEPSLATE_COPPER_ORE, Material.GOLD_ORE, Material.EMERALD_ORE,
      Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE};

  @Override
  public void injectContext(ServiceContext context) {

  }

  public void runLater(Runnable r, long d) {
    Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), r, d);
  }

  @EventHandler
  public void onBreak(BlockBreakEvent e) {
    World world = e.getBlock().getLocation().getWorld();
    if (world == null) {
      return;
    }

    Material m = e.getBlock().getBlockData().getMaterial();
    for (Material ore : ores) {

      if (m == ore && Objects.equals(RelluEssentials.getInstance().oreRespawn, world.getName())) {
        runLater(() -> e.getBlock().setType(m), 10000L);
        break;
      }
    }
  }
}