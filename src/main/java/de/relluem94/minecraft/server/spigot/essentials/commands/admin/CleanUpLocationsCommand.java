package de.relluem94.minecraft.server.spigot.essentials.commands.admin;


import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CleanUpLocationsCommand implements SubCommand {

  private final ServiceContext serviceContext;


  public CleanUpLocationsCommand(ServiceContext context) {
    serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!serviceContext.getGroupService().isSenderAuthorized(player, "admin")) {
      player.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }
    serviceContext.getLocationCleanUpService().cleanUpLocations(player);
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.CLEAN_LOCATIONS.getName().equalsIgnoreCase(args[0]);
  }
}