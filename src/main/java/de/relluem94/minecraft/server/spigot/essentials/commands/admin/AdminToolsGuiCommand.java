package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.PLUGIN_INVENTORY_ADMIN_TOOLS;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Command implementation for opening the Admin Tools GUI.
 */
public class AdminToolsGuiCommand implements SubCommand {

  private final ServiceContext serviceContext;

  /**
   * Constructs a new AdminToolsGuiCommand.
   *
   * @param context the service context to use for dependencies
   */
  public AdminToolsGuiCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!serviceContext.getGroupService().isSenderAuthorized(player, "admin")) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }
    serviceContext.getInventoryService().find(
            new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(), PLUGIN_INVENTORY_ADMIN_TOOLS))
        .ifPresent(registeredInventory -> registeredInventory.openFor(player));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.ADMIN_TOOLS.getName().equalsIgnoreCase(args[0]);
  }
}