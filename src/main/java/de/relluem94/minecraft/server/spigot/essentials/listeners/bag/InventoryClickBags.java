package de.relluem94.minecraft.server.spigot.essentials.listeners.bag;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryClickBags implements ListenerConstruct {

  TranslationService translationService;
  BagService bagService;

  @Override
  public void injectContext(ServiceContext context) {
    translationService = context.getTranslationService();
    bagService = context.getBagService();
  }

  @EventHandler
  public void onInventoryClickItem(@NotNull InventoryClickEvent e) {
    if (e.getWhoClicked() instanceof Player p && e.getCurrentItem() != null) {
      String MAIN_GUI = translationService.get(MessageKey.PLUGIN_BAG_GUI_TITLE);
      if (e.getView().getTitle()
          .startsWith(Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE)
          && e.getView().getTitle().endsWith(" Bag")) {

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);
        Optional<BagTypeEntry> bte = RelluEssentials.getInstance().getBagTypeRegistry()
            .findByPartialName(e.getView().getTitle());

        if (!bte.isPresent()) {
          return;
        }

        Optional<BagEntry> bagEntryOptional = bagService.findBag(pe.getId(), bte.get().getId());

        if (!bagEntryOptional.isPresent()) {
          return;
        }

        BagEntry bagEntry = bagEntryOptional.get();

        ItemStack is = e.getCurrentItem();

        int slot = bagService.getSlotByItemStack(bagEntry, is);
        if (slot != -1 && e.getClickedInventory() != null) {
          int value = bagEntry.getSlotValue(slot);
          boolean isRightClick = e.isRightClick();
          boolean isBagInventory = e.getClickedInventory().getType().equals(InventoryType.CHEST);

          if (!isBagInventory) {
            if (isRightClick) {
              for (ItemStack fis : p.getInventory().getContents()) {
                if (fis != null && ItemHelper.getCleanItemStack(fis)
                    .equals(ItemHelper.getCleanItemStack(is))) {
                  value = bagEntry.getSlotValue(slot);
                  bagEntry.setSlotValue(slot, value + fis.getAmount());
                  bagEntry.setHasToBeUpdated(true);
                  p.getInventory().remove(fis);
                }
              }
            } else {
              bagEntry.setSlotValue(slot, value + is.getAmount());
              bagEntry.setHasToBeUpdated(true);
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
                    bagEntry.setSlotValue(slot, value);
                    bagEntry.setHasToBeUpdated(true);
                    p.getInventory().addItem(cleanIS);
                    if (p.getInventory().firstEmpty() == -1) {
                      break;
                    }
                  } else {
                    cleanIS.setAmount(value);
                    bagEntry.setSlotValue(slot, 0);
                    bagEntry.setHasToBeUpdated(true);
                    p.getInventory().addItem(cleanIS);
                    break;
                  }
                }
              } else {
                if (p.getInventory().firstEmpty() != -1) {
                  if (value >= is.getMaxStackSize()) {
                    cleanIS.setAmount(is.getMaxStackSize());
                    bagEntry.setSlotValue(slot, value - is.getMaxStackSize());
                  } else {
                    cleanIS.setAmount(value);
                    bagEntry.setSlotValue(slot, 0);
                  }
                  bagEntry.setHasToBeUpdated(true);
                  p.getInventory().addItem(cleanIS);
                }
              }
            }
          }

          p.openInventory(
              Objects.requireNonNull(bagService.getBagInventory(bagEntry.getBagTypeId(), pe)));
        }
        e.setCancelled(true);
      } else if (e.getView().getTitle().equals(MAIN_GUI)) {
        e.setCancelled(true);
        String name = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
        Optional<BagTypeEntry> bte = RelluEssentials.getInstance().getBagTypeRegistry()
            .findByPartialName(name);

        if (bte.isPresent()) {
          PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
              .getPlayerEntry(e.getWhoClicked().getUniqueId());
          e.getWhoClicked()
              .openInventory(
                  Objects.requireNonNull(bagService.getBagInventory(bte.get().getId(), pe)));
        }
      }
    }
  }
}