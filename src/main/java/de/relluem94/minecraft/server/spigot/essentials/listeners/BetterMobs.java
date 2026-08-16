package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper.hasEnchant;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.EntityCoins;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@ListenerName("BetterMobs")
public class BetterMobs implements ListenerConstruct {


  private final EnchantmentHelper telekinesis;
  private final EnchantmentHelper thunderstrike;
  private final EnchantmentHelper scavengers;
  private final EnchantmentHelper lifesteal;
  private ServiceContext serviceContext;

  public BetterMobs() {
    this.telekinesis = EnchantmentRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS))
        .orElse(null);
    this.thunderstrike = EnchantmentRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE))
        .orElse(null);
    this.scavengers = EnchantmentRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS))
        .orElse(null);
    this.lifesteal = EnchantmentRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_LIFESTEAL))
        .orElse(null);
  }

  @Override
  public void injectContext(ServiceContext context) {
    serviceContext = context;
  }

  @EventHandler
  public void onSpawn(@NotNull CreatureSpawnEvent e) {
    EntityType et = e.getEntity().getType();

    if (et == EntityType.PHANTOM) {
      World world = e.getLocation().getWorld();
      if (world == null) {
        return;
      }

      boolean isPhantomSpawnDisabled = serviceContext.getWorldGroupService()
          .isSettingActiveForWorld(WorldSetting.ENTITIES_SPAWN_PHANTOM, world.getName());
      e.setCancelled(isPhantomSpawnDisabled);
    }
  }

  @EventHandler
  public void onKill(@NonNull PlayerDeathEvent e) {
    String worldName = e.getEntity().getWorld().getName();
    boolean deathLoseCoinsActive = serviceContext.getWorldGroupService()
        .isSettingActiveForWorld(WorldSetting.DEATH_LOSE_COINS, worldName);
    if (deathLoseCoinsActive) {
      Player p = e.getEntity();
      PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);

      double purse = pe.getPurse();
      double losses = purse / 2;
      if (purse - losses >= 1) {
        pe.setPurse(purse - losses);
        p.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_PLAYER_DEATH_LOST_COINS,
                    StringHelper.formatDouble(losses), PLUGIN_NAME_MONEY));
      } else {
        pe.setPurse(0);
      }

      pe.setUpdatedBy(pe.getId());
      pe.setHasToBeUpdated(true);
    }
  }

  @EventHandler
  public void onDeath(@NotNull EntityDeathEvent e) {
    if (e.getEntity().getKiller() != null) {
      EntityCoins entityCoins = EntityCoins.from(e.getEntity().getType());
      int coinsPerDeath = entityCoins.getCoins();

      if (coinsPerDeath > 0) {
        Player p = e.getEntity().getKiller();
        PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);

        boolean hasScavengers = p.getInventory().getItemInMainHand().hasItemMeta()
            && scavengers != null
            && hasEnchant(p.getInventory().getItemInMainHand(), scavengers);

        int totalCoins = hasScavengers ? coinsPerDeath + (coinsPerDeath / 2) : coinsPerDeath;

        pe.setPurse(pe.getPurse() + totalCoins);
        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);
        serviceContext.getChatService().sendMessageInActionBar(
            p,
            serviceContext.getTranslationService().getWithPrefix(
                MessageKey.COMMAND_PURSE_GAIN,
                StringHelper.formatInt(totalCoins),
                PLUGIN_NAME_MONEY,
                StringHelper.formatDouble(pe.getPurse()),
                PLUGIN_NAME_MONEY
            )
        );

        if (serviceContext.getBagService().hasBags(pe.getId())) {
          List<ItemStack> li = new ArrayList<>(e.getDrops());
          e.getDrops().removeAll(serviceContext.getBagService().collectItemStacks(li, p, pe));
        }

        if (p.getInventory().getItemInMainHand().hasItemMeta() && telekinesis != null
            && hasEnchant(p.getInventory().getItemInMainHand(), telekinesis)) {
          List<ItemStack> lis = new ArrayList<>();
          for (ItemStack is : e.getDrops()) {
            if (p.getInventory().firstEmpty() != -1) {
              p.getInventory().addItem(is);
              lis.add(is);
            }
          }
          e.getDrops().removeAll(lis);
        }
      }
    }
  }

  @EventHandler
  public void onHit(@NotNull EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Monster m && e.getDamager() instanceof Player p) {
      PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
      if (pe.getPlayerState().equals(PlayerState.DAMAGE_INFO)) {
        p.sendMessage(
            serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.PLUGIN_EVENT_DAMAGE_SHOW, e.getDamage(),
                    m.getLastDamage(), m.getHealth()));
      }

      if (p.getInventory().getItemInMainHand().hasItemMeta() && thunderstrike != null
          && hasEnchant(p.getInventory().getItemInMainHand(), thunderstrike)) {
        if (m.getLocation().getWorld() == null) {
          return;
        }
        m.getLocation().getWorld().strikeLightningEffect(m.getLocation());
      }

      if (p.getInventory().getItemInMainHand().hasItemMeta() && lifesteal != null
          && hasEnchant(p.getInventory().getItemInMainHand(), lifesteal)) {
        double playerHealth = p.getHealth();
        double maxHealth = p.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        boolean playerIsDamaged = playerHealth < maxHealth;
        if (playerIsDamaged) {
          double healAmount = e.getDamage() / 4;
          double healedHealth = Math.min(playerHealth + healAmount, maxHealth);
          p.setHealth(healedHealth);
        }
      }
    }
  }

}