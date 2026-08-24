package de.relluem94.minecraft.server.spigot.essentials.listeners.bag;

import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@ListenerName("EntityPickupItemBags")
public class EntityPickupItemBags implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    serviceContext = context;
  }

  @EventHandler
  public void onItemCollect(@NotNull EntityPickupItemEvent e) {
    if (e.getEntity() instanceof Player p) {

      PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
      ItemStack is = e.getItem().getItemStack();

      if (serviceContext.getItemService().hasKey(itemCoins(), is, PersistentDataType.INTEGER)) {
        ItemMeta im = is.getItemMeta();
        if (im != null) {
          Integer itemCoinsValue = im.getPersistentDataContainer()
              .get(itemCoins(), PersistentDataType.INTEGER);

          if (itemCoinsValue != null) {
            int coins = itemCoinsValue * is.getAmount();
            serviceContext.getChatService().sendMessageInActionBar(p,
                serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_PURSE_GAIN, StringHelper.formatInt(coins),
                        StringHelper.formatDouble(pe.getPurse() + coins)));
            pe.setPurse(pe.getPurse() + coins);

            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.PLAYERS, 1F, 1);

            pe.setUpdatedBy(pe.getId());
            pe.setHasToBeUpdated(true);

            e.getItem().setItemStack(new ItemStack(Material.AIR));
            e.setCancelled(true);
            return;
          }
        }
      }

      String worldName = p.getWorld().getName();
      boolean collectBagEnabled = serviceContext.getWorldGroupService()
          .isSettingActiveForWorld(WorldSetting.COLLECT_BAG, worldName);

      if (collectBagEnabled && serviceContext.getBagService().hasBags(pe.getId())
          && serviceContext.getBagService().collectItem(e.getItem(), p, pe)) {
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