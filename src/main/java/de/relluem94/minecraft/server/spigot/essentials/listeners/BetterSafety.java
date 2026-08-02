package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BetterSafety implements ListenerConstruct {

  private final String[] strings2block = {"/pl", "/bukkit", "/ver"};

  GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @EventHandler
  public void onType(PlayerCommandPreprocessEvent e) {
    for (String s2b : strings2block) {
      if (!groupService.isSenderAuthorized(e.getPlayer(), "admin")) {
        if (e.getMessage().toLowerCase().startsWith(s2b)) {
          e.setCancelled(true);
          e.getPlayer()
              .sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        }
      }
    }
  }
}
