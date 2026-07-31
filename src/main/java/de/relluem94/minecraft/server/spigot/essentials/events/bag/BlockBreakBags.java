package de.relluem94.minecraft.server.spigot.essentials.events.bag;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockBreakBags implements Listener {

  private final Set<Block> processingBlocks = new HashSet<>();

  @EventHandler
  public void onBlockBreak(@NotNull BlockBreakEvent e) {
    Player p = e.getPlayer();
    Block b = e.getBlock();
    Material m = b.getType();

    if (EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(),
        CustomEnchants.delicate)) {
      if (m.equals(Material.PUMPKIN_STEM) || m.equals(Material.MELON_STEM) || m.equals(
          Material.ATTACHED_PUMPKIN_STEM) || m.equals(Material.ATTACHED_MELON_STEM)) {
        e.setCancelled(true);
      }

      if (e.getBlock().getBlockData() instanceof Ageable age) {
        if (age.getAge() != age.getMaximumAge() && !m.equals(Material.SUGAR_CANE)) {
          e.setCancelled(true);
        }
      }

      if (m.equals(Material.TORCH) || m.equals(Material.LILY_PAD)) {
        e.setCancelled(true);
      }
    }

    if (EnchantmentHelper.hasEnchant(p.getInventory().getItemInMainHand(),
        CustomEnchants.telekinesis)) {
      int dropCount = 0;

      if (isChorusPlant(b) && b.getRelative(BlockFace.DOWN).getType().equals(Material.END_STONE)) {

        List<Block> blocks = new ArrayList<>(getChorusBlocks(b, 0, null));

        if (blocks.size() <= 50) {
          e.setCancelled(true);
          blocks.forEach(block -> block.setType(Material.AIR));
          b.setType(Material.AIR);

          Item item = e.getBlock().getWorld().dropItem(e.getBlock().getLocation(),
              new ItemStack(Material.CHORUS_FRUIT, blocks.size() + 1));
          EntityPickupItemEvent entityPickupItemEvent = new EntityPickupItemEvent(p, item,
              blocks.size() + 1);
          Bukkit.getPluginManager().callEvent(entityPickupItemEvent);
        }
      }

      if (isSugarCaneOrIsBamboo(b)) {
        if (processingBlocks.contains(b)) {
          return;
        }

        Block originalBlock = b;
        while (isSugarCaneOrIsBamboo(b.getRelative(BlockFace.UP))) {
          Block blockAbove = b.getRelative(BlockFace.UP);

          processingBlocks.add(blockAbove);
          BlockBreakEvent fakeBreakEvent = new BlockBreakEvent(blockAbove, p);
          Bukkit.getPluginManager().callEvent(fakeBreakEvent);
          processingBlocks.remove(blockAbove);

          dropCount++;
          b.setType(Material.AIR);
          b = blockAbove;
        }

        if (isSugarCaneOrIsBamboo(b)) {
          processingBlocks.add(b);
          BlockBreakEvent fakeBreakEvent = new BlockBreakEvent(b, p);
          Bukkit.getPluginManager().callEvent(fakeBreakEvent);
          processingBlocks.remove(b);

          b.setType(Material.AIR);
          dropCount++;
        }

        if (!m.equals(Material.AIR) && dropCount > 0) {
          e.setCancelled(true);
          Item item = originalBlock.getWorld()
              .dropItem(originalBlock.getLocation(), new ItemStack(m, dropCount));
          EntityPickupItemEvent entityPickupItemEvent = new EntityPickupItemEvent(p, item,
              dropCount);
          Bukkit.getPluginManager().callEvent(entityPickupItemEvent);
        }
      }
    }
  }

  private boolean isChorusPlant(@NotNull Block block) {
    return block.getType().equals(Material.CHORUS_PLANT);
  }

  private boolean isSugarCaneOrIsBamboo(@NotNull Block b) {
    return b.getType().equals(Material.SUGAR_CANE) || b.getType().equals(Material.BAMBOO);
  }

  private @NotNull Set<Block> getChorusBlocks(Block b, int count, BlockFace prevBlockFace) {
    Set<Block> blocks = new LinkedHashSet<>();

    count++;
    if (count == 30) {
      return blocks;
    }

    if (isChorusPlant(b)) {
      if (b.getBlockData() instanceof MultipleFacing bdf) {
        for (BlockFace bf : bdf.getFaces()) {
          Block block = b.getRelative(bf);
          if (bf.equals(BlockFace.DOWN)) {
            continue;
          }
          if ((bf.equals(prevBlockFace))) {
            continue;
          }
          if (blocks.add(block)) {
            blocks.addAll(getChorusBlocks(block, 28, bf.getOppositeFace()));
          }
        }
      }
    }

    return blocks;
  }
}
