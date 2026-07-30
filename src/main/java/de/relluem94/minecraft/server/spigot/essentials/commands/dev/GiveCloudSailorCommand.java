package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR;

public class GiveCloudSailorCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR)).ifPresent(item -> player.getInventory().addItem(item.getCustomItem()));
        ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS)).ifPresent(item -> player.getInventory().addItem(item.getCustomItem()));
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.CLOUD_SAILOR.getName().equalsIgnoreCase(args[0]);
    }
}