package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_RELLU_PICKAXE;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

@AllArgsConstructor
public class GivePickaxeCommand implements SubCommand {

  private final ServiceContext serviceContext;

  @Override
  public void execute(Player player, String[] args) {
    player.getInventory().addItem(
        serviceContext.getItemService().find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_RELLU_PICKAXE))
            .orElseThrow()
            .getCustomItem());
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.PICKAXE.getName().equalsIgnoreCase(args[0]);
  }
}