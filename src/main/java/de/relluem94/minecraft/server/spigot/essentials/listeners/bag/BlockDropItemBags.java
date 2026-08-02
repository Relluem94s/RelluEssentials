package de.relluem94.minecraft.server.spigot.essentials.listeners.bag;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.EnchantmentRegistry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

public class BlockDropItemBags implements ListenerConstruct {

  private final Random random = new Random();
  private final EnchantmentHelper autosmelt;
  private final EnchantmentHelper replenishment;
  private final EnchantmentHelper telekinesis;

  public BlockDropItemBags() {
    this.autosmelt = EnchantmentRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT))
        .orElse(null);
    this.replenishment = EnchantmentRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT))
        .orElse(null);
    this.telekinesis = EnchantmentRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
        .orElse(null);
  }

  @EventHandler
  public void onBlockDrop(@NotNull BlockDropItemEvent e) {
    Player p = e.getPlayer();
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);

    for (Item i : e.getItems()) {
      if (RelluEssentials.getInstance().dropMap.containsKey(i.getItemStack().getType())) {
        if (i.getItemStack().getAmount() == 1) {
          DoubleStore<Integer, Integer> ds = RelluEssentials.getInstance().dropMap.get(
              i.getItemStack().getType());
          i.getItemStack().setAmount(random.nextInt(ds.getSecondValue()) + ds.getValue());
        }
      }
    }

    if (autosmelt != null && EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), autosmelt)) {
      if (EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), autosmelt)) {
        for (int i = 0; i < e.getItems().size(); i++) {
          ItemStack is = e.getItems().get(i).getItemStack().clone();
          if (e.getItems().get(i) != null && ItemHelper.getSmeltedItemStack(is) != null) {
            e.getItems().get(i).getItemStack().setType(ItemHelper.getSmeltedItemStack(is).getType());
          }
        }
      }
    }

    if (replenishment != null && EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), replenishment)) {
      if (EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), replenishment)) {
        for (int i = 0; i < e.getItems().size(); i++) {
          if (e.getItems().get(i) != null && RelluEssentials.getInstance().crops.containsKey(
              e.getItems().get(i).getItemStack().getType())) {
            e.getBlock().setType(RelluEssentials.getInstance().crops.get(
                e.getItems().get(i).getItemStack().getType()));

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

    if (BagHelper.hasBags(pe.getId())) {
      List<Item> lis = BagHelper.collectItems(e.getItems(), e.getPlayer(), pe);
      e.getItems().removeAll(lis);
    }

    if (telekinesis != null && EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(), telekinesis)) {
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