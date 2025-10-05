package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_POKE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_POKE_MESSAGE_SENDER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_POKE_MESSAGE_TARGET;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_POKE_SUBTITLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_POKE_TITLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@CommandName("poke")
public class Poke implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
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

    private void poke(@NotNull Player target){
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10F, 0F);
        target.getWorld().playEffect(target.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
        target.getWorld().playEffect(target.getLocation(), Effect.EXTINGUISH, 5);
        target.getWorld().playEffect(target.getLocation(), Effect.ENDERDRAGON_GROWL, 5);
        target.sendTitle(PLUGIN_COMMAND_POKE_TITLE, PLUGIN_COMMAND_POKE_SUBTITLE, 5, 80, 5);
    }

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("user").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        if(strings.length > 1){
            return tabList;
        }

        tabList.addAll(TabCompleterHelper.getOnlinePlayers());

        return tabList;
    }
}