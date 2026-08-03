package de.relluem94.minecraft.server.spigot.essentials.listeners.bag;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.translationService;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryClickBags implements ListenerConstruct {

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onInventoryClickItem(@NotNull InventoryClickEvent e) {
    if (e.getWhoClicked() instanceof Player p && e.getCurrentItem() != null) {
      String MAIN_GUI = translationService.get(MessageKey.PLUGIN_BAG_GUI_TITLE);
      if (e.getView().getTitle()
          .startsWith(Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE)
          && e.getView().getTitle().endsWith(" Bag")) {

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);
        BagTypeEntry bte = BagHelper.getBagTypeByName(e.getView().getTitle());

        if (bte == null) {
          return;
        }

        BagEntry be = BagHelper.getBag(pe.getId(), bte.getId());

        if (be == null) {
          return;
        }

        ItemStack is = e.getCurrentItem();

        int slot = BagHelper.getSlotByItemStack(be, is);
        if (slot != -1 && e.getClickedInventory() != null) {
          int value = be.getSlotValue(slot);
          boolean isRightClick = e.isRightClick();
          boolean isBagInventory = e.getClickedInventory().getType().equals(InventoryType.CHEST);

          if (!isBagInventory) {
            if (isRightClick) {
              for (ItemStack fis : p.getInventory().getContents()) {
                if (fis != null && ItemHelper.getCleanItemStack(fis)
                    .equals(ItemHelper.getCleanItemStack(is))) {
                  value = be.getSlotValue(slot);
                  be.setSlotValue(slot, value + fis.getAmount());
                  be.setHasToBeUpdated(true);
                  p.getInventory().remove(fis);
                }
              }
            } else {
              be.setSlotValue(slot, value + is.getAmount());
              be.setHasToBeUpdated(true);
              Objects.requireNonNull(p.getInventory().getItem(e.getSlot())).setAmount(0);
            }
          } else {
            ItemStack cleanIS = ItemHelper.getCleanItemStack(is);
            if (value > 0) {
              if (isRightClick) {
                while (p.getInventory().firstEmpty() != -1) {
                  if (p.getInventory().firstEmpty() == -1) {
                    break;
                  }

                  if (value >= is.getMaxStackSize()) {
                    value -= is.getMaxStackSize();
                    cleanIS.setAmount(is.getMaxStackSize());
                    be.setSlotValue(slot, value);
                    be.setHasToBeUpdated(true);
                    p.getInventory().addItem(cleanIS);
                    if (p.getInventory().firstEmpty() == -1) {
                      break;
                    }
                  } else {
                    cleanIS.setAmount(value);
                    be.setSlotValue(slot, 0);
                    be.setHasToBeUpdated(true);
                    p.getInventory().addItem(cleanIS);
                    break;
                  }
                }
              } else {
                if (p.getInventory().firstEmpty() != -1) {
                  if (value >= is.getMaxStackSize()) {
                    cleanIS.setAmount(is.getMaxStackSize());
                    be.setSlotValue(slot, value - is.getMaxStackSize());
                  } else {
                    cleanIS.setAmount(value);
                    be.setSlotValue(slot, 0);
                  }
                  be.setHasToBeUpdated(true);
                  p.getInventory().addItem(cleanIS);
                }
              }
            }
          }

          p.openInventory(Objects.requireNonNull(BagHelper.getBag(be.getBagTypeId(), pe)));
        }
        e.setCancelled(true);
      } else if (e.getView().getTitle().equals(MAIN_GUI)) {
        e.setCancelled(true);
        String name = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
        BagTypeEntry bte = BagHelper.getBagTypeByName(name);
        if (bte != null) {
          PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
              .getPlayerEntry(e.getWhoClicked().getUniqueId());
          e.getWhoClicked()
              .openInventory(Objects.requireNonNull(BagHelper.getBag(bte.getId(), pe)));
        }
      }
    }
  }
}