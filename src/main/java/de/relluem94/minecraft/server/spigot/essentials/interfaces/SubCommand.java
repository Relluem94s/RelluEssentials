package de.relluem94.minecraft.server.spigot.essentials.interfaces;

import org.bukkit.entity.Player;

/**
 * Represents a sub-command that can be executed by a {@link Player}.
 * <p>
 * Implementations define specific behavior triggered by a particular argument pattern within a
 * parent command.
 * </p>
 */
public interface SubCommand {

  /**
   * Executes this sub-command for the given player with the provided arguments.
   *
   * @param player the {@link Player} who issued the command
   * @param args   the arguments passed alongside the command
   */
  void execute(Player player, String[] args);

  /**
   * Determines whether this sub-command matches the given argument pattern.
   * <p>
   * Used by the {@link de.relluem94.minecraft.server.spigot.essentials.registry.SubCommandRegistry}
   * to find the correct sub-command for a given input.
   * </p>
   *
   * @param args the arguments passed alongside the command
   * @return {@code true} if this sub-command handles the given arguments, {@code false} otherwise
   */
  boolean matches(String[] args);
}
