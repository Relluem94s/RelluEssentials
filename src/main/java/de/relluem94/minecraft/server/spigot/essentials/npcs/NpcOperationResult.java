package de.relluem94.minecraft.server.spigot.essentials.npcs;

import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcValidator.ValidationResult;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

/**
 * Represents the result of an NPC operation, encapsulating whether the operation succeeded,
 * an optional error message on failure, and the affected {@link Npc} on success.
 *
 * @author rellu
 */
@Getter
public class NpcOperationResult {

  private final boolean successful;
  private final ValidationResult validationResult;
  private final Npc npc;

  private NpcOperationResult(boolean successful, ValidationResult validationResult, Npc npc) {
    this.successful = successful;
    this.validationResult = validationResult;
    this.npc = npc;
  }

  /**
   * Creates a successful {@link NpcOperationResult} containing the given {@link Npc}.
   *
   * @param npc the {@link Npc} that was affected by the operation
   * @return a non-null {@link NpcOperationResult} indicating success
   */
  public static @NonNull NpcOperationResult success(Npc npc) {
    return new NpcOperationResult(true, null, npc);
  }

  /**
   * Creates a failed {@link NpcOperationResult} containing the given {@link ValidationResult}.
   *
   * @param validationResult the {@link ValidationResult} describing the reason for the failure
   * @return a non-null {@link NpcOperationResult} indicating failure
   */
  public static @NonNull NpcOperationResult failure(ValidationResult validationResult) {
    return new NpcOperationResult(false, validationResult, null);
  }
}