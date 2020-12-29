package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.stringHelper;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.implode;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import static net.minecraft.server.v1_16_R3.Items.im;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Rename implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rename")) { 
            if (args.length >= 1) {
                if (sender instanceof Player) {
                    return rename((Player) sender, args);
                }
            } else {
                sender.sendMessage(PLUGIN_COMMAND_RENAME_INFO);
                return true;
            }
        }
        return false;
    }

    private boolean rename(Player p, String[] args) {
      
        if (!Permission.isAuthorized(p, Groups.MOD.getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }
        

        String message = implode(0, args);
        message = stringHelper.replaceSymbols(replaceColor(message));
        ItemStack is = p.getInventory().getItemInMainHand();
        if(!is.getType().equals(Material.AIR)){
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(message);
            p.sendMessage(PLUGIN_COMMAND_RENAME);
            
        }
        else{
            p.sendMessage(PLUGIN_COMMAND_RENAME_AIR);
        }
        return true;
    }
}
