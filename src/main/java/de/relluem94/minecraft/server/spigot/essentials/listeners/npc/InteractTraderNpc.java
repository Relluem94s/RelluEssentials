package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BuyBackSlotResolver;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;


public class InteractTraderNpc implements ListenerConstruct {

  private BuyBackSlotResolver buyBackSlotResolver;
  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
    this.buyBackSlotResolver = new BuyBackSlotResolver(
        serviceContext.getBuyBackService(), ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow().getCustomItem());
  }

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
              BankAccountEntry bae = serviceContext.getDatabaseHelper()
                  .getPlayerBankAccount(pe.getId());
              if (bae != null) {
                InventoryHelper.openInventory(p, serviceContext.getTraderNpcService().getBankerNpc().getMainGUI());
              } else {
                BankTierEntry bte = serviceContext.getDatabaseHelper()
                    .getBankTier(1);
                if (pe.getPurse() > bte.getCost()) {
                  pe.setPurse(pe.getPurse() - bte.getCost());
                  pe.setUpdatedBy(pe.getId());
                  pe.setHasToBeUpdated(true);

                  bae = new BankAccountEntry();
                  bae.setValue(0);
                  bae.setTier(bte);
                  bae.setPlayerId(pe.getId());

                  serviceContext.getDatabaseHelper().insertBankAccount(bae);
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
                  serviceContext.getTraderNpcService().getNpc(i).getMainGUI();

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
