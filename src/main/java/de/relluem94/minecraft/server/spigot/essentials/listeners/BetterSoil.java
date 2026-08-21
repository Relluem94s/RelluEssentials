package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

/* Better Call Soil */
@ListenerName("BetterSoil")
public class BetterSoil implements ListenerConstruct {


  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onChange(@NonNull PlayerInteractEvent e) {
    Block b = e.getClickedBlock();

    if (b == null) {
      return;
    }

    if (e.getAction() == Action.PHYSICAL) {
      if (b.getType().equals(Material.FARMLAND) && !e.getPlayer().isSneaking()) {
        e.setUseInteractedBlock(Event.Result.DENY);
        e.setCancelled(true);
      }
    } else {
      ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
      RelluEssentialsNamespacedKey magicWaterBucketKey = new RelluEssentialsNamespacedKey(
          serviceContext.getPluginMetadataService().getName(),
          PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET);

      if (serviceContext.getItemService().find(magicWaterBucketKey)
          .map(itemHelper -> itemHelper.toItemStack().isSimilar(itemInHand)).orElse(false)) {
        e.setCancelled(true);
        b = e.getClickedBlock().getRelative(e.getBlockFace());
        if (b.getType().equals(Material.AIR)) {
          b.setType(Material.WATER);
        }
      }
    }
  }

  @EventHandler
  public void onChange(@NonNull EntityInteractEvent e) {
    Block b = e.getBlock();
    if (b.getType().equals(Material.FARMLAND)) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onHarvest(@NonNull PlayerHarvestBlockEvent e) {
    Player p = e.getPlayer();
    PlayerEntry pe = serviceContext.getPlayerService()
        .getPlayerEntry(p.getUniqueId());

    List<ItemStack> lis = serviceContext.getBagService()
        .collectItemStacks(e.getItemsHarvested(), e.getPlayer(), pe);
    e.getItemsHarvested().removeAll(lis);

    EnchantmentRegistry.find(RegistryKey.of(EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
        .filter(telekinesis -> EnchantmentHelper.hasEnchant(
            e.getPlayer().getInventory().getItemInMainHand(), telekinesis))
        .ifPresent(_ -> {
          for (ItemStack is : e.getItemsHarvested()) {
            p.getInventory().addItem(is);
          }
          e.getItemsHarvested().clear();
        });
  }
}

