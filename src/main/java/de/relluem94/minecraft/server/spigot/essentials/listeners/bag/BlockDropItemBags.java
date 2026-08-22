package de.relluem94.minecraft.server.spigot.essentials.listeners.bag;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@ListenerName("BlockDropItemBags")
public class BlockDropItemBags implements ListenerConstruct {

  private EnchantmentHelper autosmelt;
  private EnchantmentHelper replenishment;
  private EnchantmentHelper telekinesis;
  private ServiceContext context;

  @Override
  public void injectContext(ServiceContext context) {
    this.context = context;

    this.autosmelt = context.getEnchantmentService().find(
            new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT))
        .orElse(null);
    this.replenishment = context.getEnchantmentService().find(
            new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT))
        .orElse(null);
    this.telekinesis = context.getEnchantmentService().find(
            new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
        .orElse(null);
  }

  @EventHandler
  public void onBlockDrop(@NotNull BlockDropItemEvent e) {
    Player p = e.getPlayer();

    for (Item i : e.getItems()) {
      Material type = i.getItemStack().getType();
      int resolved = context.getBlockDropService()
          .resolveDropAmount(type, i.getItemStack().getAmount());
      i.getItemStack().setAmount(resolved);
    }

    if (autosmelt != null && EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(),
        autosmelt)) {
      if (EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), autosmelt)) {
        for (int i = 0; i < e.getItems().size(); i++) {
          ItemStack is = e.getItems().get(i).getItemStack().clone();
          if (e.getItems().get(i) != null && ItemHelper.getSmeltedItemStack(is) != null) {
            e.getItems().get(i).getItemStack()
                .setType(ItemHelper.getSmeltedItemStack(is).getType());
          }
        }
      }
    }

    if (replenishment != null && EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(),
        replenishment)) {
      if (EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), replenishment)) {
        for (int i = 0; i < e.getItems().size(); i++) {
          Material type = e.getItems().get(i).getItemStack().getType();
          if (e.getItems().get(i) != null && context.getBlockDropService().isSeed(type)) {
            e.getBlock().setType(context.getBlockDropService().getPlantForSeed(type));

            if (e.getBlock().getBlockData() instanceof Cocoa c) {
              Block cocoa = e.getBlock();
              Block wood = cocoa.getRelative(c.getFacing().getOppositeFace());
              if (!wood.getType().equals(Material.JUNGLE_LOG)) {
                for (BlockFace f : BlockFace.values()) {
                  wood = e.getBlock().getRelative(f);
                  if (wood.getType().equals(Material.JUNGLE_LOG)) {
                    c.setFacing(f);
                    cocoa.setBlockData(c);
                    break;
                  }
                }
              } else {
                c.setFacing(c.getFacing().getOppositeFace());
                cocoa.setBlockData(c);
              }
            }
            int oldAmount = e.getItems().get(i).getItemStack().getAmount();
            e.getItems().get(i).getItemStack().setAmount(oldAmount - 1);
          }
        }
      }
    }

    PlayerEntry pe = context.getPlayerService().getPlayerEntry(p);
    if (context.getBagService().hasBags(pe.getId())) {
      List<Item> lis = context.getBagService().collectItems(e.getItems(), e.getPlayer(), pe);
      e.getItems().removeAll(lis);
    }

    if (telekinesis != null && EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(),
        telekinesis)) {
      if (EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), telekinesis)) {
        List<Item> lis = new ArrayList<>();
        for (Item i : e.getItems()) {
          if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(i.getItemStack());
            lis.add(i);
          }
        }
        e.getItems().removeAll(lis);
      }
    }
  }
}