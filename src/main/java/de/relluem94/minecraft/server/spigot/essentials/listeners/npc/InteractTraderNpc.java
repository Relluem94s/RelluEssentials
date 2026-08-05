package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BuyBackSlotResolver;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;


public class InteractTraderNpc implements ListenerConstruct {

  private final BuyBackSlotResolver buyBackSlotResolver;
  TranslationService translationService;
  BankerNpc bankerNpc;

  public InteractTraderNpc() {
    this.buyBackSlotResolver = new BuyBackSlotResolver(
        RelluEssentials.getInstance().getBuyBackService(), ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow().getCustomItem());
  }

  @Override
  public void injectContext(ServiceContext context) {
    translationService = context.getTranslationService();
    bankerNpc = context.getBankerNpc();
  }

  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
    Player p = e.getPlayer();
    if (e.getRightClicked() instanceof Villager) {
      if (e.getRightClicked().getCustomName() != null) {
        String customName = e.getRightClicked().getCustomName();
        for (int i = 0;
            i < RelluEssentials.getInstance().getTraderNpcRegistry().getNPCNameList().size();
            i++) {
          if (RelluEssentials.getInstance().getTraderNpcRegistry().getNPCNameList().get(i)
              .equals(customName)) {
            if (customName.equals(bankerNpc.getName())) {
              PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);
              BankAccountEntry bae = RelluEssentials.getInstance().getDatabaseHelper()
                  .getPlayerBankAccount(pe.getId());
              if (bae != null) {
                InventoryHelper.openInventory(p, bankerNpc.getMainGUI());
              } else {
                BankTierEntry bte = RelluEssentials.getInstance().getDatabaseHelper()
                    .getBankTier(1);
                if (pe.getPurse() > bte.getCost()) {
                  pe.setPurse(pe.getPurse() - bte.getCost());
                  pe.setUpdatedBy(pe.getId());
                  pe.setHasToBeUpdated(true);

                  bae = new BankAccountEntry();
                  bae.setValue(0);
                  bae.setTier(bte);
                  bae.setPlayerId(pe.getId());

                  RelluEssentials.getInstance().getDatabaseHelper().insertBankAccount(bae);
                  p.sendMessage(translationService.getWithPrefix(
                      MessageKey.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT));
                } else {
                  p.sendMessage(translationService.getWithPrefix(
                      MessageKey.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT_TO_LESS_COINS,
                      PLUGIN_NAME_MONEY, PLUGIN_NAME_MONEY, bte.getCost()));
                }
              }
              e.setCancelled(true);
            } else {
              org.bukkit.inventory.Inventory gui =
                  RelluEssentials.getInstance().getTraderNpcRegistry().getNPC(i).getMainGUI();

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
