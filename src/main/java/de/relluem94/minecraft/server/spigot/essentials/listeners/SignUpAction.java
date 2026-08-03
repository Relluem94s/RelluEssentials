package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.translationService;

import de.relluem94.minecraft.server.spigot.essentials.constants.SignConstants;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.SignRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jspecify.annotations.NonNull;

public class SignUpAction implements ListenerConstruct {


  private final RegistryKey signAction;

  public SignUpAction() {
    this.signAction = RegistryKey.of(SignConstants.PLUGIN_SIGN_ACTION_UP);
  }

  @Override
  public void injectContext(ServiceContext context) {

  }

  @EventHandler
  public void onCustomSignInteract(@NonNull RelluEssentialsSignInteractEvent event) {
    if (!event.getActionKey().equals(signAction)) {
      return;
    }
    handleUp(event.getPlayer(), event.getClickedBlock());
  }

  private void handleUp(Player player, @NonNull Block signBlock) {
    Location signLocation = signBlock.getLocation();
    if (signLocation.getWorld() == null) {
      return;
    }

    int maxHeight = signLocation.getWorld().getMaxHeight();
    boolean endPointFound = false;

    for (int y = signLocation.getBlockY(); y <= maxHeight; y++) {
      Block candidateBlock = signLocation.add(0, 1, 0).getBlock();
      if (!SignHelper.isBlockSign(candidateBlock)) {
        continue;
      }

      String candidateLine1 = ((Sign) candidateBlock.getState()).getSide(Side.FRONT).getLine(1);
      if (SignRegistry.findEntryByLine(candidateLine1).isEmpty()) {
        continue;
      }

      Location destination = player.getLocation().clone();
      destination.setY(y);

      if (!destination.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR)) {
        endPointFound = true;
        player.teleport(destination, TeleportCause.COMMAND);
        break;
      }
    }

    if (!endPointFound) {
      player.sendMessage(
          translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT));
    }
  }
}
