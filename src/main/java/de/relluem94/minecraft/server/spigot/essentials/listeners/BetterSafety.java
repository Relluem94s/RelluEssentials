package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jspecify.annotations.NonNull;

public class BetterSafety implements ListenerConstruct {

  private final List<String> strings2block = List.of("/pl", "/bukkit", "/ver");

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onType(@NonNull PlayerCommandPreprocessEvent e) {
    boolean commandIsBlocked = strings2block.stream()
        .anyMatch(s2b -> e.getMessage().toLowerCase().startsWith(s2b));

    if (serviceContext.getGroupService().isSenderAuthorized(e.getPlayer(), "admin") || !commandIsBlocked) {
      return;
    }
    e.setCancelled(true);
    e.getPlayer()
        .sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
  }
}
