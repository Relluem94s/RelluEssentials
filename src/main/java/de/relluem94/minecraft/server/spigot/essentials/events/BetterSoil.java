package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

/* Better Call Soil */
public class BetterSoil implements Listener {

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
      RegistryKey magicWaterBucketKey = RegistryKey.of(PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET);

      if (ItemRegistry.identifyFromItemStack(itemInHand).filter(magicWaterBucketKey::equals)
          .isPresent()) {
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
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p.getUniqueId());

    List<ItemStack> lis = BagHelper.collectItemStacks(e.getItemsHarvested(), e.getPlayer(), pe);
    e.getItemsHarvested().removeAll(lis);

    if (EnchantmentHelper.hasEnchant(e.getPlayer().getInventory().getItemInMainHand(),
        CustomEnchants.telekinesis)) {
      for (ItemStack is : e.getItemsHarvested()) {
        p.getInventory().addItem(is);
      }
      e.getItemsHarvested().clear();
    }
  }
}
