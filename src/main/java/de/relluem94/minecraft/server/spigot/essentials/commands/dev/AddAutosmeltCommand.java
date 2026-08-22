package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command that adds the Autosmelt enchantment to the item currently held in the player's main
 * hand.
 */
public class AddAutosmeltCommand implements SubCommand {

  private final ServiceContext serviceContext;

  /**
   * Constructor for ServiceContext Injection.
   */
  public AddAutosmeltCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    serviceContext.getEnchantmentService().find(
            new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
                EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT))
        .ifPresent(enchant -> enchant.addTo(player.getInventory().getItemInMainHand()));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.SMELT.getName().equalsIgnoreCase(args[0]);
  }
}