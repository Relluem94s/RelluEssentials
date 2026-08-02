package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.PLUGIN_INVENTORY_ADMIN_TOOLS;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.InventoryRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class AdminToolsGuiCommand implements SubCommand {

  private final GroupService groupService;

  public AdminToolsGuiCommand(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!groupService.isSenderAuthorized(player, "admin")) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }
    InventoryRegistry.find(RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_INVENTORY_ADMIN_TOOLS))
        .ifPresent(registeredInventory -> registeredInventory.openFor(player));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.ADMIN_TOOLS.getName().equalsIgnoreCase(args[0]);
  }
}