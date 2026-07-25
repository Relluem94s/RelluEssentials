package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.items.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class GiveRelluGearCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.getInventory().addItem(new RelluHelmet().getCustomItem());
        player.getInventory().addItem(new RelluChestplate().getCustomItem());
        player.getInventory().addItem(new RelluLeggings().getCustomItem());
        player.getInventory().addItem(new RelluBoots().getCustomItem());
        player.getInventory().addItem(new RelluShield().getCustomItem());
        player.getInventory().addItem(new RelluSword().getCustomItem());
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.RELLU.getName().equalsIgnoreCase(args[0]);
    }
}