package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_DEATHPOINTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_DEATHS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_FIRST_ONLINE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_GROUP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_HOMES;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_JUMPED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_LAST_ONLINE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_LEFT_GAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_MARRIED_SINCE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_MARRIED_TO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PLAYERINFO_MINED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@CommandName("playerinfo")
public class PlayerInfo implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String @NotNull [] args) {
        if (args.length < 1) {
            sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }

        if (!Permission.isAuthorized(sender, Groups.getGroup("user").getId())) {
            sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }
      
        OfflinePlayer target = PlayerHelper.getOfflinePlayer(args[0]);
        
        if (target == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
            return true;
        }

        showPlayerInfo(sender, args[0]);
        return true;
    }

    private void showPlayerInfo(CommandSender sender, String targetName){
        OfflinePlayer target = PlayerHelper.getOfflinePlayer(targetName);
        
        if (target == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, targetName));
            return;
        }

        PlayerEntry pet = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getUniqueId());

        if(pet == null){
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, targetName));
            return;
        }

        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO, target.getName()));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_HOMES, pet.getHomes().size()));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_DEATHPOINTS, pet.getDeaths().size()));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_GROUP, pet.getGroup().getPrefix() + pet.getGroup().getName()));

        if(pet.getPartner() != null){
            sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MARRIED_TO,  RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(pet.getPartner().getFirstPartnerId()).getName(), RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(pet.getPartner().getSecondPartnerId())));
            sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MARRIED_SINCE, pet.getPartner().getCreated()));
        }

        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_LAST_ONLINE, new Date(target.getLastPlayed())));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_FIRST_ONLINE, new Date(target.getFirstPlayed())));

        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MINED, Material.STONE.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.STONE)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MINED, Material.DIRT.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.DIRT)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MINED, Material.SAND.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.SAND)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MINED, Material.COBBLESTONE.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.COBBLESTONE)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MINED, Material.DEEPSLATE.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_MINED, Material.DIAMOND_ORE.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE) + target.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE_DIAMOND_ORE)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_DEATHS, target.getStatistic(Statistic.DEATHS)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_JUMPED, target.getStatistic(Statistic.JUMP)));
        sender.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO_LEFT_GAME, target.getStatistic(Statistic.LEAVE_GAME)));
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
