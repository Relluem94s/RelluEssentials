package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

public class SignTeleportAction implements ListenerConstruct {


  private final RegistryKey signAction;

  public SignTeleportAction() {
    this.signAction = RegistryKey.of(SignConstants.PLUGIN_SIGN_ACTION_TELEPORT);
  }

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onCustomSignInteract(@NonNull RelluEssentialsSignInteractEvent event) {
    if (!event.getActionKey().equals(signAction)) {
      return;
    }
    handleTeleport(event.getPlayer(), event.getCustomInput());
  }

  private void handleTeleport(@NonNull Player player, @NonNull String customInput) {
    String[] coordinates = customInput.split(",");
    if (coordinates.length < 3) {
      return;
    }
    Location destination = new Location(
        player.getWorld(),
        Integer.parseInt(coordinates[0]),
        Integer.parseInt(coordinates[1]),
        Integer.parseInt(coordinates[2])
    );
    player.teleport(destination);
  }
}
