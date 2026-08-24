package de.relluem94.minecraft.server.spigot.essentials.listeners;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jspecify.annotations.NonNull;

@ListenerName("SignHomeAction")
public class SignHomeAction implements ListenerConstruct {


  private RelluEssentialsNamespacedKey signAction;

  @Override
  public void injectContext(ServiceContext context) {
    this.signAction = new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(),
        SignConstants.PLUGIN_SIGN_ACTION_HOME);
  }

  @EventHandler
  public void onCustomSignInteract(@NonNull RelluEssentialsSignInteractEvent event) {
    if (!event.getActionKey().equals(signAction)) {
      return;
    }
    handleHome(event.getPlayer(), event.getCustomInput());
  }

  private void handleHome(Player player, String customInput) {
    String homeCommand = AnnotationHelper.getCommandName(Home.class);
    if (homeCommand == null) {
      return;
    }
    player.performCommand(homeCommand + " tp " + customInput);
  }
}
