package de.relluem94.minecraft.server.spigot.essentials.commands.admin;


import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.cleanup.LocationCleanUpService;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CleanUpLocationsCommand implements SubCommand {

  private final GroupService groupService;
  private final TranslationService translationService;
  private final LocationCleanUpService locationCleanUpService;


  public CleanUpLocationsCommand(ServiceContext context) {
    this.groupService = context.getGroupService();
    translationService = context.getTranslationService();
    locationCleanUpService = new LocationCleanUpService(translationService,
        context.getDatabaseHelper());
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!groupService.isSenderAuthorized(player, "admin")) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }
    locationCleanUpService.cleanUpLocations(player);
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.CLEAN_LOCATIONS.getName().equalsIgnoreCase(args[0]);
  }
}