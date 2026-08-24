package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

@ListenerName("SignTeleportAction")
public class SignTeleportAction implements ListenerConstruct {


  private RelluEssentialsNamespacedKey signAction;

  @Override
  public void injectContext(ServiceContext context) {
    this.signAction = new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(),
        SignConstants.PLUGIN_SIGN_ACTION_TELEPORT);
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
    Location destination = new Location(player.getWorld(), Integer.parseInt(coordinates[0]),
        Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));
    player.teleport(destination);
  }
}
