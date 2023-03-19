package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PLAYERINFO;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.Date;


public class PlayerInfo implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_PLAYERINFO)) {
            return false;
        }

        Player p = null;

        if (isPlayer(sender)) {
            p = (Player) sender;
        }

        if(p == null){
            return true;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }
      
        

        if (args.length < 1) {
            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if (args.length > 1) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }




        OfflinePlayer target = PlayerHelper.getOfflinePlayer(args[0]);
        
        if (target == null) {
            p.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
            return true;
        }

        PlayerEntry pet = PlayerAPI.getPlayerEntry(p);

        if(pet == null){
            p.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
            return true;
        }

        p.sendMessage(String.format(PLUGIN_COMMAND_PLAYERINFO, target.getName()));
        p.sendMessage(String.format("Homes: %s", pet.getHomes().size()));
        p.sendMessage(String.format("DeathPoints: %s", pet.getDeaths().size()));
        p.sendMessage(String.format("Group: %s", pet.getGroup().getPrefix() + pet.getGroup().getName()));

        if(pet.getPartner() != null){
            p.sendMessage(String.format("Married: %s $4\u2665§f %s",  PlayerAPI.getPlayerEntry(pet.getPartner().getFirstPlayerID()).getName(), PlayerAPI.getPlayerEntry(pet.getPartner().getSecondPlayerID())));
            p.sendMessage(String.format("Married since: %s", pet.getPartner().getCreated()));
        }


        
        p.sendMessage(String.format("Last Online: %s", new Date(target.getLastPlayed()).toString()));
        p.sendMessage(String.format("First Online: %s", new Date(target.getFirstPlayed()).toString()));

        

        p.sendMessage(String.format("%s mined: %s", Material.STONE.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.STONE)));
        p.sendMessage(String.format("%s mined: %s", Material.DIRT.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.DIRT)));
        p.sendMessage(String.format("%s mined: %s", Material.SAND.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.SAND)));
        p.sendMessage(String.format("%s mined: %s", Material.COBBLESTONE.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.COBBLESTONE)));
        p.sendMessage(String.format("%s mined: %s", Material.DEEPSLATE.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE)));
        p.sendMessage(String.format("%s mined: %s", Material.DIAMOND.name(), target.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND)));
        p.sendMessage(String.format("Deaths: %s", target.getStatistic(Statistic.DEATHS)));
        p.sendMessage(String.format("Jumped: %s", target.getStatistic(Statistic.JUMP)));
        p.sendMessage(String.format("Leaved Game: %s", target.getStatistic(Statistic.LEAVE_GAME)));

        return true;
    }
}
