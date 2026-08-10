package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NO_DEATH_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import java.util.Objects;
import java.util.Random;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class NoDeathMessage implements ListenerConstruct {


  private final Random random = new Random();

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onDeath(@NotNull PlayerDeathEvent e) {
    e.setKeepLevel(true);
    e.setDroppedExp(0);
    e.setDeathMessage(null);

    Player p = e.getEntity();
    Location ploc = p.getLocation();

    String worldName = Objects.requireNonNull(ploc.getWorld()).getName();
    boolean deathLoseCoinsActive = serviceContext.getWorldGroupService()
        .isSettingActiveForWorld(WorldSetting.DEATH_LOSE_COINS, worldName);
    boolean deathCreateHomeActive = serviceContext.getWorldGroupService()
        .isSettingActiveForWorld(WorldSetting.DEATH_CREATE_HOME, worldName);

    if (deathLoseCoinsActive) {
      ItemHelper coinItem = ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_COINS))
          .orElseThrow();
      for (ItemStack is : p.getInventory().getContents()) {
        if (is != null && is.getItemMeta() != null && coinItem.almostEquals(is) && is.getItemMeta()
            .getPersistentDataContainer().has(itemCoins(), PersistentDataType.INTEGER)) {
          p.getInventory().remove(is);
        }
      }
    }

    if (deathCreateHomeActive) {
      Location location = new Location(ploc.getWorld(), ploc.getBlockX(), ploc.getBlockY(),
          ploc.getBlockZ(), ploc.getYaw(), ploc.getPitch());

      PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
      LocationEntry le = new LocationEntry();
      le.setLocation(location);
      le.setLocationName(String.format(PLUGIN_EVENT_NO_DEATH_MESSAGE, random.nextInt(994)));
      LocationTypeEntry locationType = RelluEssentials.getInstance().getLocationTypeEntryList().get(1);
      le.setLocationType(locationType);
      le.setPlayerId(pe.getId());

      TextComponent message = new TextComponent(
          serviceContext.getTranslationService().get(MessageKey.PLUGIN_EVENT_DEATH_TP));
      message.setColor(ChatColor.AQUA);
      message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
          "/home " + Home.Commands.TP.getName() + " " + le.getLocationName()));
      p.spigot().sendMessage(message);

      serviceContext.getDatabaseHelper().insertLocation(le);
      le = serviceContext.getDatabaseHelper().getLocation(location, locationType.getId());

      World world = le.getLocation().getWorld();
      String locationName = le.getLocationName();
      Location leLocation = le.getLocation();

      if (le != null) {
        pe.getHomes().add(le);
      }

      if (world == null) {
        return;
      }

      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(
              MessageKey.PLUGIN_EVENT_DEATH,
              locationName,
              serviceContext.getTranslationService().get(
                  MessageKey.COMMAND_WHERE_STRING,
                  (int) leLocation.getX(),
                  (int) leLocation.getY(),
                  (int) leLocation.getZ(),
                  world.getName()
              )
          )
      );
    }
  }

  @EventHandler
  public void onRespawn(@NotNull PlayerRespawnEvent e) {
    Player p = e.getPlayer();
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
    if (pe != null) {
      p.setAllowFlight(pe.isFlying());
      p.setFlying(pe.isFlying());
    }
  }

  @EventHandler
  public void onWorldChange(@NotNull PlayerChangedWorldEvent e) {
    Player p = e.getPlayer();
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
    if (pe != null) {
      p.setAllowFlight(pe.isFlying());
      p.setFlying(pe.isFlying());
    }
  }

  @EventHandler
  public void onWorldChange(@NotNull PlayerTeleportEvent e) {
    Player p = e.getPlayer();
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
    if (pe != null) {
      p.setAllowFlight(pe.isFlying());
      p.setFlying(pe.isFlying());
    }
  }
}