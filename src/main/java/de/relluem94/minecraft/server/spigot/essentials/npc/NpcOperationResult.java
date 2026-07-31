package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.model.Npc;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

@Getter
public class NpcOperationResult {

  private final boolean successful;
  private final String errorMessage;
  private final Npc npc;

  private NpcOperationResult(boolean successful, String errorMessage, Npc npc) {
    this.successful = successful;
    this.errorMessage = errorMessage;
    this.npc = npc;
  }

  public static @NonNull NpcOperationResult success(Npc npc) {
    return new NpcOperationResult(true, null, npc);
  }

  public static @NonNull NpcOperationResult failure(String errorMessage) {
    return new NpcOperationResult(false, errorMessage, null);
  }
}