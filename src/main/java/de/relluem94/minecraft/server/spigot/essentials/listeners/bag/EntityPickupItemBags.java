package de.relluem94.minecraft.server.spigot.essentials.listeners.bag;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class EntityPickupItemBags implements ListenerConstruct {

  private final ItemHelper coinItem = ItemRegistry.find(RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_COINS))
      .orElseThrow();

  @EventHandler
  public void onItemCollect(@NotNull EntityPickupItemEvent e) {
    if (e.getEntity() instanceof Player p) {

      PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);

      ItemStack is = e.getItem().getItemStack();
      if (coinItem.almostEquals(is)) {
        ItemMeta im = is.getItemMeta();

        if (im != null && im.getPersistentDataContainer()
            .has(itemCoins(), PersistentDataType.INTEGER)) {
          Integer itemCoins = im.getPersistentDataContainer()
              .get(NamespacedKeyConstants.itemCoins(), PersistentDataType.INTEGER);

          if (itemCoins == null) {
            itemCoins = 0;
          }

          int coins = itemCoins * is.getAmount();
          ChatHelper.sendMessageInActionBar(p,
              languageHelper.getWithPrefix(MessageKey.COMMAND_PURSE_GAIN,
                  StringHelper.formatInt(coins), StringHelper.formatDouble(pe.getPurse() + coins)));
          pe.setPurse(pe.getPurse() + coins);

          p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.PLAYERS, 1F, 1);

          pe.setUpdatedBy(pe.getId());
          pe.setHasToBeUpdated(true);

          e.getItem().getItemStack().setAmount(0);
          e.setCancelled(true);
        }
      }

      String worldName = p.getWorld().getName();
      boolean collectBagEnabled = RelluEssentials.getInstance().collectBagWorlds.contains(
          worldName);

      if (collectBagEnabled && BagHelper.hasBags(pe.getId()) && BagHelper.collectItem(e.getItem(),
          p, pe)) {
        p.getInventory().remove(is);
        e.setCancelled(true);
        e.getItem().remove();
      } else {
        p.getInventory().addItem(is);
        e.setCancelled(true);
        e.getItem().remove();
        p.playSound(p, Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, 1f);
      }
    }
  }
}