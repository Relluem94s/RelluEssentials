package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluLeggings;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluShield;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluSword;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;

public class GiveRelluGearCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.getInventory().addItem(ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_RELLU_HELMET)).orElseThrow().getCustomItem());
        player.getInventory().addItem(ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_RELLU_CHESTPLATE)).orElseThrow().getCustomItem());
        player.getInventory().addItem(new RelluLeggings().getCustomItem());
        player.getInventory().addItem(ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_RELLU_BOOTS)).orElseThrow().getCustomItem());
        player.getInventory().addItem(new RelluShield().getCustomItem());
        player.getInventory().addItem(new RelluSword().getCustomItem());
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.RELLU.getName().equalsIgnoreCase(args[0]);
    }
}