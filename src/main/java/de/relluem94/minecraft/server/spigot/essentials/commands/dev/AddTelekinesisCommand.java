package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class AddTelekinesisCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        CustomEnchants.telekinesis.addTo(player.getInventory().getItemInMainHand());
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.TELE.getName().equalsIgnoreCase(args[0]);
    }
}