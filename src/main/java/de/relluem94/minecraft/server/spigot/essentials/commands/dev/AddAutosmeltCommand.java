package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class AddAutosmeltCommand implements SubCommand {

  @Override
  public void execute(Player player, String[] args) {
    CustomEnchants.autosmelt.addTo(player.getInventory().getItemInMainHand());
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.SMELT.getName().equalsIgnoreCase(args[0]);
  }
}