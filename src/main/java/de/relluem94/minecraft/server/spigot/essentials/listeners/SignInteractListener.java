package de.relluem94.minecraft.server.spigot.essentials.listeners;
import de.relluem94.minecraft.server.spigot.essentials.events.RelluEssentialsSignInteractEvent;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.SignAction;
import de.relluem94.minecraft.server.spigot.essentials.registry.SignRegistry;
import java.util.Map;
import java.util.Optional;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class SignInteractListener implements Listener {

  private final Plugin plugin;

  public SignInteractListener(Plugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerInteract(@NonNull PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    Block clickedBlock = event.getClickedBlock();
    if (clickedBlock == null || !SignHelper.isBlockSign(clickedBlock)) {
      return;
    }
    Sign sign = (Sign) clickedBlock.getState();
    String actionLine = sign.getSide(Side.FRONT).getLine(1);
    Optional<Map.Entry<RegistryKey, SignAction>> foundEntry =
        SignRegistry.findEntryByLine(actionLine);
    if (foundEntry.isEmpty()) {
      return;
    }
    event.setCancelled(true);
    RegistryKey actionKey = foundEntry.get().getKey();
    SignAction signAction = foundEntry.get().getValue();
    String customInput = sign.getSide(Side.FRONT).getLine(2);
    Player player = event.getPlayer();

    plugin.getServer().getPluginManager()
        .callEvent(new RelluEssentialsSignInteractEvent(player, clickedBlock, actionKey, signAction,
            customInput));
  }
}