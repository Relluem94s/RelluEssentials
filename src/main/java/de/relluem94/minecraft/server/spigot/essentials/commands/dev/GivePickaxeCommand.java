package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluPickaxe;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class GivePickaxeCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.getInventory().addItem(new RelluPickaxe().getCustomItem());
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.PICKAXE.getName().equalsIgnoreCase(args[0]);
    }
}