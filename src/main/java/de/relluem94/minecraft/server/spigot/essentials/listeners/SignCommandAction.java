package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

public class SignCommandAction implements ListenerConstruct {


  private final RegistryKey signAction;

  public SignCommandAction() {
    this.signAction = RegistryKey.of(SignConstants.PLUGIN_SIGN_ACTION_COMMAND);
  }

  @Override
  public void injectContext(ServiceContext context) {

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