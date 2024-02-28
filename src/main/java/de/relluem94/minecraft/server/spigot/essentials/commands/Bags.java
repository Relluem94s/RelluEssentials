package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.TypeUtils;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_BAGS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Bags implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_BAGS)) {
            return false;
        }
        
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length != 1) {
            p.openInventory(BagHelper.getBags(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p)));
            return true;   
        }

        BagTypeEntry bte = null;
        if(TypeUtils.isInt(args[0])){
            bte = BagHelper.getBagTypeById(Integer.parseInt(args[0]));
        }
        else{
            bte = BagHelper.getBagTypeByName(args[0]);
        }

        if(bte != null){
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            if(BagHelper.hasBag(pe.getId(), bte.getId())){
                p.openInventory(BagHelper.getBag(bte.getId(), pe));
                return true;
            }
            else{
                p.sendMessage(String.format(PLUGIN_COMMAND_BAGS_NOT_FOUND, args[0]));
                return true;
            }
        }
        else{
            p.sendMessage(String.format(PLUGIN_COMMAND_BAGS_NOT_FOUND, args[0]));
            return true;
        }
    }
}