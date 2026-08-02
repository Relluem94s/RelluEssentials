package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Spawn;
import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

public class SignSpawnAction implements ListenerConstruct {

  private final RegistryKey signAction;

  public SignSpawnAction() {
    this.signAction = RegistryKey.of(RelluEssentials.getInstance(),
        SignConstants.PLUGIN_SIGN_ACTION_SPAWN);
  }

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onCustomSignInteract(@NonNull RelluEssentialsSignInteractEvent event) {
    if (!event.getActionKey().equals(signAction)) {
      return;
    }
    handleSpawn(event.getPlayer());
  }

  private void handleSpawn(Player player) {
    String spawnCommand = AnnotationHelper.getCommandName(Spawn.class);
    if (spawnCommand == null) {
      return;
    }
    player.performCommand(spawnCommand);
  }

}
