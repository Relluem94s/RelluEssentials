package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.model.NPC;
import lombok.Getter;

@Getter
public class NPCOperationResult {
    private final boolean successful;
    private final String errorMessage;
    private final NPC npc;

    private NPCOperationResult(boolean successful, String errorMessage, NPC npc) {
        this.successful = successful;
        this.errorMessage = errorMessage;
        this.npc = npc;
    }

    public static NPCOperationResult success(NPC npc) {
        return new NPCOperationResult(true, null, npc);
    }

    public static NPCOperationResult failure(String errorMessage) {
        return new NPCOperationResult(false, errorMessage, null);
    }
}