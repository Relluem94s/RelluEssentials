package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.shared.AdminCommandHelper.cleanUpLocations;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CleanUpLocationsCommand implements SubCommand {

  @Override
  public void execute(Player player, String[] args) {
    if (!PermissionHelper.isAuthorized(player, GroupRegistry.getGroup("admin").getId())) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }
    cleanUpLocations(player);
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.CLEAN_LOCATIONS.getName().equalsIgnoreCase(args[0]);
  }
}