package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_MSG_SPACER_IN;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.model.Npc;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcDialogueTracker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NonNull;

public class InteractNpc implements Listener {

  private static final long INTERACTION_COOLDOWN_MS = 750;
  private final Map<UUID, Long> lastInteractionTimestamp = new HashMap<>();
  private NpcDialogueTracker dialogueTracker;

  public NpcDialogueTracker resovleDialogTracker() {
    if (dialogueTracker == null) {
      setDialogTracker(RelluEssentials.getInstance().getNpcDialogueTracker());
    }
    return dialogueTracker;
  }

  public void setDialogTracker(NpcDialogueTracker dialogueTracker) {
    this.dialogueTracker = dialogueTracker;
  }

  @EventHandler
  public void onPlayerInteractEntity(@NonNull PlayerInteractEntityEvent event) {
    if (!(event.getRightClicked() instanceof Mannequin clickedMannequin)) {
      return;
    }

    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    Player player = event.getPlayer();
    UUID playerUUID = player.getUniqueId();
    long now = System.currentTimeMillis();

    if (lastInteractionTimestamp.containsKey(playerUUID)
        && now - lastInteractionTimestamp.get(playerUUID) < INTERACTION_COOLDOWN_MS) {
      return;
    }

    lastInteractionTimestamp.put(playerUUID, now);

    Optional<Npc> matchedNPC = RelluEssentials.getInstance().getNpcService().getNPCs().stream()
        .filter(npc -> clickedMannequin.getUniqueId().equals(npc.getEntityUUID())).findFirst();

    if (matchedNPC.isEmpty()) {
      return;
    }

    Npc npc = matchedNPC.get();
    List<NpcDialogueEntry> dialogueLines = npc.getDialogueLines();

    if (dialogueLines.isEmpty()) {
      return;
    }

    int lineIndex = resovleDialogTracker().getNextLineIndexAndAdvance(npc.getId(),
        player.getUniqueId(), dialogueLines.size());
    player.sendMessage(
        "§e" + npc.getProfileName() + PLUGIN_FORMS_MSG_SPACER_IN + dialogueLines.get(lineIndex)
            .getText());
  }
}