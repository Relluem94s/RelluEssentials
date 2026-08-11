package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_MSG_SPACER_IN;

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

public class InteractNpc implements ListenerConstruct {

  private static final long INTERACTION_COOLDOWN_MS = 750;
  private final Map<UUID, Long> lastInteractionTimestamp = new HashMap<>();
  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
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
    UUID playerUuid = player.getUniqueId();
    long now = System.currentTimeMillis();

    if (lastInteractionTimestamp.containsKey(playerUuid)
        && now - lastInteractionTimestamp.get(playerUuid) < INTERACTION_COOLDOWN_MS) {
      return;
    }

    lastInteractionTimestamp.put(playerUuid, now);

    Optional<Npc> matchedNpc = serviceContext.getNpcService().getNPCs().stream()
        .filter(npc -> clickedMannequin.getUniqueId().equals(npc.getEntityUUID())).findFirst();

    if (matchedNpc.isEmpty()) {
      return;
    }

    Npc npc = matchedNpc.get();
    List<NpcDialogueEntry> dialogueLines = npc.getDialogueLines();

    if (dialogueLines.isEmpty()) {
      return;
    }

    int lineIndex = serviceContext.getNpcDialogueProgressService().getNextLineIndexAndAdvance(npc.getId(),
        player.getUniqueId(), dialogueLines.size());
    player.sendMessage(
        "§e" + npc.getProfileName() + PLUGIN_FORMS_MSG_SPACER_IN + dialogueLines.get(lineIndex)
            .getText());
  }
}