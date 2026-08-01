package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NonNull;

public class SignCommandAction implements Listener {

  private final RegistryKey signAction;
  public SignCommandAction() {
    this.signAction = RegistryKey.of(SignConstants.PLUGIN_SIGN_ACTION_COMMAND);
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