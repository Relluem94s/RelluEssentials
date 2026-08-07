package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.SignAction;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
@Getter
public class RelluEssentialsSignInteractEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final Player player;
  private final Block clickedBlock;
  private final RegistryKey actionKey;
  private final SignAction signAction;
  private final String customInput;
  public RelluEssentialsSignInteractEvent(@NotNull Player player, @NotNull Block clickedBlock, @NotNull RegistryKey actionKey,
      SignAction signAction, String customInput) {
    this.player = player;
    this.clickedBlock = clickedBlock;
    this.actionKey = actionKey;
    this.signAction = signAction;
    this.customInput = customInput;
  }
  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}