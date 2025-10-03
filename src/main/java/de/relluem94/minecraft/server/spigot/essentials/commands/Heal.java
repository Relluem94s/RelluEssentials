package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_HEAL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import java.util.Objects;

@CommandName("heal")
public class Heal implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {

        if (isCMDBlock(sender) || isConsole(sender)) {
            if (args.length < 1) {
                sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
                return true;   
            }
            
            heal(sender, args[0]);
        }


        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;
        
        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length > 0) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;   
        }

        heal(p);
        return true;
    }

    private void heal(Player p) {
        p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).getDefaultValue());
        p.setFoodLevel(20);
        p.sendMessage(PLUGIN_COMMAND_HEAL);
    }

    private void heal(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        
        if (target == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, targetName));
            return;
        }

        heal(target);
    }
}
