package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.cleanup.ProtectionCleanUpService;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CleanUpProtectionsCommand implements SubCommand {

  private final GroupService groupService;
  private final TranslationService translationService;
  private final ProtectionCleanUpService adminCommandHelper;

  public CleanUpProtectionsCommand(ServiceContext context) {
    this.groupService = context.getGroupService();
    translationService = context.getTranslationService();
    adminCommandHelper = new ProtectionCleanUpService(translationService,
        context.getProtectionRegistry(), context.getDatabaseHelper(),
        context.getSchedulerService());
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!groupService.isSenderAuthorized(player, "admin")) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }
    adminCommandHelper.cleanUpProtections(player);
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.CLEAN_PROTECTIONS.getName().equalsIgnoreCase(args[0]);
  }
}