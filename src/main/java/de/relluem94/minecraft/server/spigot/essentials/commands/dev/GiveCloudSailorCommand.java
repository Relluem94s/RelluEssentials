package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class GiveCloudSailorCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.getInventory().addItem(CustomItems.cloudSailor.getCustomItem());
        player.getInventory().addItem(CustomItems.cloudBoots.getCustomItem());
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.CLOUD_SAILOR.getName().equalsIgnoreCase(args[0]);
    }
}