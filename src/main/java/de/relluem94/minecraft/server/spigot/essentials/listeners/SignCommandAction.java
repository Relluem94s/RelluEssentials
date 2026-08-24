package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

@ListenerName("SignCommandAction")
public class SignCommandAction implements ListenerConstruct {


  private RelluEssentialsNamespacedKey signAction;

  @Override
  public void injectContext(ServiceContext context) {
    this.signAction = new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(),
        SignConstants.PLUGIN_SIGN_ACTION_COMMAND);
  }

  @EventHandler
  public void onCustomSignInteract(@NonNull RelluEssentialsSignInteractEvent event) {
    if (!event.getActionKey().equals(signAction)) {
      return;
    }
    handleCommand(event.getPlayer(), event.getCustomInput());
  }

  private void handleCommand(Player player, String customInput) {
    player.performCommand(customInput);
  }
}