package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.EnchantmentRegistry;
import java.util.Optional;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

/**
 * Removes all plugin-specific enchantments from the item in the player's main hand.
 */
public class RemoveEnchantsCommand implements SubCommand {

  @Override
  public void execute(Player player, String[] args) {
    ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
    Stream.of(
            EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS
        )
        .map(key -> EnchantmentRegistry.find(RegistryKey.of(key)))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(enchant -> enchant.removeFrom(itemInMainHand));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.NO_ENCHANT.getName().equalsIgnoreCase(args[0]);
  }
}