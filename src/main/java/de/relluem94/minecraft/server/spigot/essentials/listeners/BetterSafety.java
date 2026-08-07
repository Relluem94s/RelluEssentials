package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jspecify.annotations.NonNull;

public class BetterSafety implements ListenerConstruct {

  private final List<String> strings2block = List.of("/pl", "/bukkit", "/ver");

  GroupService groupService;
  TranslationService translationService;

  @Override
  public void injectContext(ServiceContext context) {
    this.translationService = context.getTranslationService();
    this.groupService = context.getGroupService();
  }

  @EventHandler
  public void onType(@NonNull PlayerCommandPreprocessEvent e) {
    boolean commandIsBlocked = strings2block.stream()
        .anyMatch(s2b -> e.getMessage().toLowerCase().startsWith(s2b));

    if (groupService.isSenderAuthorized(e.getPlayer(), "admin") || !commandIsBlocked) {
      return;
    }
    e.setCancelled(true);
    e.getPlayer()
        .sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
  }
}
