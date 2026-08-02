package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_SIGN_CLICK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_SIGN_NAME;

import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.SignAction;
import de.relluem94.minecraft.server.spigot.essentials.registry.SignRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.util.Optional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;
import org.jetbrains.annotations.NotNull;

public class CreateSignActions implements ListenerConstruct {

  private GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @EventHandler
  public void onChangeSignCreateActionSign(@NotNull SignChangeEvent event) {
    if (!groupService.isSenderAuthorized(event.getPlayer(), "mod")) {
      return;
    }

    String lineOne = event.getLine(1);
    if (lineOne == null) {
      return;
    }

    Optional<SignAction> foundAction = SignRegistry.findByLine(lineOne);
    if (foundAction.isEmpty()) {
      return;
    }

    SignAction signAction = foundAction.get();
    event.setLine(0, PLUGIN_SIGN_NAME);
    event.setLine(1, signAction.getDisplayName());
    event.setLine(3, PLUGIN_SIGN_CLICK);

    if (!signAction.requiresCustomInput()) {
      event.setLine(2, "");
    }
  }
}
