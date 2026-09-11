package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Listener that handles ore regeneration after a block break event.
 * When a player breaks an ore block in a world with the {@link WorldSetting#ORE_RESPAWN}
 * setting active, the broken ore block is restored to its original state after a delay.
 */
@ListenerName("BetterBlockDrop")
public class BetterBlockDrop implements ListenerConstruct {

  private final Material[] ores = {Material.DIAMOND_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE,
      Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE, Material.DEEPSLATE_COAL_ORE,
      Material.DEEPSLATE_COPPER_ORE, Material.GOLD_ORE, Material.EMERALD_ORE,
      Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE};

  ServiceContext serviceContext;

  /**
   * Injects the {@link ServiceContext} into this listener, providing access to required services.
   *
   * @param context the service context to inject
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles the {@link BlockBreakEvent} to restore broken ore blocks.
   * If the broken block is an ore and the world has the
   * {@link WorldSetting#ORE_RESPAWN} setting active,
   * the block is scheduled to be restored to its original material after a delay of 10000 ticks.
   *
   * @param event the block break event
   */
  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    World world = event.getBlock().getLocation().getWorld();
    if (world == null) {
      return;
    }

    boolean oreRespawnActive = serviceContext.getWorldGroupService()
        .isSettingActiveForWorld(WorldSetting.ORE_RESPAWN, world.getName());

    if (!oreRespawnActive) {
      return;
    }

    Material m = event.getBlock().getBlockData().getMaterial();

    Material blockMaterial = event.getBlock().getBlockData().getMaterial();
    boolean isOre = Arrays.stream(ores).anyMatch(ore -> ore == blockMaterial);

    if (isOre) {
      serviceContext.getSchedulerService().runTaskLater(() -> event.getBlock().setType(m), 10000L);
    }
  }
}