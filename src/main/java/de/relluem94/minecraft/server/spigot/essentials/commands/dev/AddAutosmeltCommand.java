package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command that adds the Autosmelt enchantment to the item
 * currently held in the player's main hand.
 */
public class AddAutosmeltCommand implements SubCommand {

  @Override
  public void execute(Player player, String[] args) {
    EnchantmentRegistry.find(RegistryKey.of(RelluEssentials.getInstance(), EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT))
        .ifPresent(enchant -> enchant.addTo(player.getInventory().getItemInMainHand()));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.SMELT.getName().equalsIgnoreCase(args[0]);
  }
}