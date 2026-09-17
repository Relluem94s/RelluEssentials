package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_MSG_SPACER_IN;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NonNull;

/**
 * Listener that handles player interactions with NPC mannequin entities.
 *
 * <p>When a player right-clicks a mannequin that is registered as an NPC,
 * this listener advances the player's dialogue progress and displays the
 * next dialogue line. A cooldown prevents repeated interactions within
 * a short time window.
 *
 * @author rellu
 */
@ListenerName("InteractNpc")
public class InteractNpc implements ListenerConstruct {

  private static final long INTERACTION_COOLDOWN_MS = 750;
  private final Map<UUID, Long> lastInteractionTimestamp = new HashMap<>();
  private ServiceContext serviceContext;

  /**
   * Injects the service context required to access NPC and dialogue progress services.
   *
   * @param context the service context providing access to application services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Handles the player interact entity event to trigger NPC dialogue.
   *
   * <p>Processes right-click interactions on mannequin entities registered as NPCs.
   * Applies an interaction cooldown per player to prevent rapid repeated triggers.
   * Advances the player's dialogue progress and sends the next dialogue line to the player.
   *
   * @param event the event fired when a player interacts with an entity
   */
  @EventHandler
  public void onPlayerInteractEntity(@NonNull PlayerInteractEntityEvent event) {
    if (!(event.getRightClicked() instanceof Mannequin clickedMannequin)) {
      return;
    }

    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    Player player = event.getPlayer();
    UUID playerUuid = player.getUniqueId();
    long now = System.currentTimeMillis();

    if (lastInteractionTimestamp.containsKey(playerUuid)
        && now - lastInteractionTimestamp.get(playerUuid) < INTERACTION_COOLDOWN_MS) {
      return;
    }

    lastInteractionTimestamp.put(playerUuid, now);

    Optional<Npc> matchedNpc = serviceContext.getNpcService().getNpcs().stream()
        .filter(npc -> clickedMannequin.getUniqueId().equals(npc.getEntityUUID())).findFirst();

    if (matchedNpc.isEmpty()) {
      return;
    }

    Npc npc = matchedNpc.get();
    List<NpcDialogueEntry> dialogueLines = npc.getDialogueLines();

    if (dialogueLines.isEmpty()) {
      return;
    }

    int lineIndex = serviceContext.getNpcDialogueProgressService().getNextLineIndexAndAdvance(
        npc.getId(),
        player.getUniqueId(),
        dialogueLines.size()
    );
    player.sendMessage(
        "§e" + npc.getProfileName() + PLUGIN_FORMS_MSG_SPACER_IN + dialogueLines.get(lineIndex)
            .getText());
  }
}