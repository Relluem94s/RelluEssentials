package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BuyBackSlotResolver;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Listener that handles player interactions with trader NPCs.
 *
 * <p>Processes right-click interactions on villager entities that are registered
 * as trader NPCs, opening the appropriate GUI for the player.</p>
 *
 * <p>Handles the special case of the banker NPC, including bank account creation
 * and validation of sufficient funds before opening the banking interface.</p>
 *
 * @author rellu
 */
@ListenerName("InteractTraderNpc")
public class InteractTraderNpc implements ListenerConstruct {

  private BuyBackSlotResolver buyBackSlotResolver;
  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
    this.buyBackSlotResolver = new BuyBackSlotResolver(
        serviceContext.getBuyBackService(), serviceContext.getItemService().find(
            new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
                PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow().toItemStack());
  }

  /**
   * Handles the {@link org.bukkit.event.player.PlayerInteractEntityEvent} for trader NPCs.
   *
   * <p>Checks whether the clicked entity is a registered trader villager NPC and
   * opens the corresponding GUI for the interacting player.</p>
   *
   * <p>If the NPC is the banker, it either opens the existing bank account GUI or
   * attempts to create a new bank account by deducting the required cost from the
   * player's purse.</p>
   *
   * @param e the event triggered when a player right-clicks an entity
   */
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
    Player p = e.getPlayer();
    if (e.getRightClicked() instanceof Villager) {
      if (e.getRightClicked().getCustomName() != null) {
        String customName = e.getRightClicked().getCustomName();
        for (int i = 0;
            i < serviceContext.getTraderNpcService().getNpcNames().size();
            i++) {
          if (serviceContext.getTraderNpcService().getNpcNames().get(i)
              .equals(customName)) {
            if (customName.equals(serviceContext.getTraderNpcService().getBankerNpc().getName())) {
              PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
              BankAccountEntry bae = serviceContext.getBankService()
                  .findBankAccountByPlayerId(pe.getId());
              if (bae != null) {
                InventoryHelper.openInventory(p, serviceContext.getTraderNpcService()
                    .getBankerNpc().getMainGui());
              } else {
                BankTierEntry bte = serviceContext.getBankService().getBankTierEntryById(1);
                if (bte == null) {
                  return;
                }
                if (pe.getPurse() > bte.getCost()) {
                  pe.setPurse(pe.getPurse() - bte.getCost());
                  pe.setUpdatedBy(pe.getId());
                  pe.setHasToBeUpdated(true);

                  bae = new BankAccountEntry();
                  bae.setValue(0);
                  bae.setTier(bte);
                  bae.setPlayerId(pe.getId());

                  serviceContext.getBankService().insertBankAccount(bae);
                  p.sendMessage(serviceContext.getTranslationService().getWithPrefix(
                      MessageKey.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT));
                } else {
                  p.sendMessage(serviceContext.getTranslationService().getWithPrefix(
                      MessageKey.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT_TO_LESS_COINS,
                      PLUGIN_NAME_MONEY, PLUGIN_NAME_MONEY, bte.getCost()));
                }
              }
              e.setCancelled(true);
            } else {
              org.bukkit.inventory.Inventory gui =
                  serviceContext.getTraderNpcService().getNpc(i).getMainGui();

              gui.setItem(49, buyBackSlotResolver.resolveForPlayer(p));
              InventoryHelper.openInventory(p, gui);
              e.setCancelled(true);
            }
          }
        }
      }
    }
  }
}
