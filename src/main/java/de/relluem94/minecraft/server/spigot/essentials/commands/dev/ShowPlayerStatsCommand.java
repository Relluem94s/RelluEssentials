package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ShowPlayerStatsCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage("Health: " + player.getHealth());
        player.sendMessage("Food: " + player.getFoodLevel());
        player.sendMessage("Exp: " + player.getExp());
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.PLAYER_STATS.getName().equalsIgnoreCase(args[0]);
    }
}