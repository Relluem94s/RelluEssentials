package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jspecify.annotations.NonNull;

@ListenerName("BetterWorlds")
public class BetterWorlds implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onWorldChange(@NonNull PlayerChangedWorldEvent e) {
    Player p = e.getPlayer();

    serviceContext.getWorldGroupService().saveWorldGroupInventoryForPlayerInWorld(p, e.getFrom(), true);
    serviceContext.getWorldGroupService().loadWorldGroupInventoryForPlayer(p);

    String newWorld = p.getWorld().getName();
    ScoreBoardManager.setScoreboardVisible(p, serviceContext.getWorldGroupService()
        .isSettingActiveForWorld(WorldSetting.SCOREBOARD_SHOW, newWorld), serviceContext.getWorldGroupService());

    if (WorldHelper.isInWorld(p, Constants.PLUGIN_WORLD_LOBBY)) {
      PlayerHelper.setLobbyItems(p, serviceContext.getItemService());
    }
  }
}