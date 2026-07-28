package de.relluem94.minecraft.server.spigot.essentials.events.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPCDialogueTracker;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NonNull;

import java.util.*;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_MSG_SPACER_IN;

public class InteractNPC implements Listener {
    private static final long INTERACTION_COOLDOWN_MS = 750;

    private NPCDialogueTracker dialogueTracker;
    private final Map<UUID, Long> lastInteractionTimestamp = new HashMap<>();


    public NPCDialogueTracker resovleDialogTracker() {
        if(dialogueTracker == null){
            setDialogTracker(RelluEssentials.getInstance().getNpcDialogueTracker());
        }
        return dialogueTracker;
    }

    public void setDialogTracker(NPCDialogueTracker dialogueTracker) {
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

        if (lastInteractionTimestamp.containsKey(playerUUID) &&
                now - lastInteractionTimestamp.get(playerUUID) < INTERACTION_COOLDOWN_MS) {
            return;
        }

        lastInteractionTimestamp.put(playerUUID, now);

        Optional<NPC> matchedNPC = RelluEssentials.getInstance()
                .getNpcService()
                .getNPCs()
                .stream()
                .filter(npc -> clickedMannequin.getUniqueId().equals(npc.getEntityUUID()))
                .findFirst();

        if (matchedNPC.isEmpty()) {
            return;
        }

        NPC npc = matchedNPC.get();
        List<String> dialogueLines = npc.getDialogueLines();

        if (dialogueLines.isEmpty()) {
            return;
        }

        int lineIndex = resovleDialogTracker().getNextLineIndexAndAdvance(
                npc.getId(),
                player.getUniqueId(),
                dialogueLines.size()
        );
        player.sendMessage("§e" + npc.getProfileName() + PLUGIN_FORMS_MSG_SPACER_IN + dialogueLines.get(lineIndex));
    }
}