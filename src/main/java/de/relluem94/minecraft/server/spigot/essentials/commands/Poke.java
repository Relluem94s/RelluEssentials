package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_POKE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_POKE_MESSAGE_SENDER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_POKE_MESSAGE_TARGET;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_POKE_SUBTITLE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_POKE_TITLE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_POKE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Poke implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_POKE)) {
            return false;
        }

        if (args.length == 0) {
            if (!Permission.isAuthorized(sender, Groups.getGroup("vip").getId())) {
                sendMessage(sender, PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            } 

            sendMessage(sender, PLUGIN_COMMAND_POKE);
            return true;
        } 

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
            return true;
        }

        if (!Permission.isAuthorized(sender, Groups.getGroup("vip").getId())) {
            sendMessage(sender, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        poke(target);
        sendMessage(target, String.format(PLUGIN_COMMAND_POKE_MESSAGE_TARGET, isPlayer(sender) ? ((Player)sender).getDisplayName() : PLUGIN_NAME_CHAT_CONSOLE + sender.getName()));
        sendMessage(sender, String.format(PLUGIN_COMMAND_POKE_MESSAGE_SENDER, target.getDisplayName()));
        return true;
    }

    private void poke(Player target){
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10F, 0F);
        target.getWorld().playEffect(target.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
        target.getWorld().playEffect(target.getLocation(), Effect.EXTINGUISH, 5);
        target.getWorld().playEffect(target.getLocation(), Effect.ENDERDRAGON_GROWL, 5);
        target.sendTitle(PLUGIN_COMMAND_POKE_TITLE, PLUGIN_COMMAND_POKE_SUBTITLE, 5, 80, 5);
    }
}