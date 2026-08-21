package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.commands.Spawn;
import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

@ListenerName("SignSpawnAction")
public class SignSpawnAction implements ListenerConstruct {

  private RelluEssentialsNamespacedKey signAction;

  @Override
  public void injectContext(ServiceContext context) {
    this.signAction = new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(),
        SignConstants.PLUGIN_SIGN_ACTION_SPAWN);
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
