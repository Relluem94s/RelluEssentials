package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class GiveSkullsCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.BAG_OF_COINS));
        player.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.MONEY_BAG));
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.SKULL.getName().equalsIgnoreCase(args[0]);
    }
}