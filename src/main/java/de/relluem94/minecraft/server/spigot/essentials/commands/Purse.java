package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PURSE_TOTAL;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PURSE_TOTAL_OTHER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PURSE_TO_ITEM;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PURSE_TO_ITEM_NOT_ENOUGH_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PURSE_TO_ITEM_VALUE_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PURSE_TO_ITEM_VALUE_TO_HIGH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PURSE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Purse implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                    if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_PURSE)) {
                        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
                        p.sendMessage(String.format(PLUGIN_COMMAND_PURSE_TOTAL, StringHelper.formatDouble(pe.getPurse())));
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (target != null) {
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        
                        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_PURSE)) {
                            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getUniqueId());
                            p.sendMessage(String.format(PLUGIN_COMMAND_PURSE_TOTAL_OTHER, target.getCustomName(), StringHelper.formatDouble(pe.getPurse())));
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
                if(TypeHelper.isLong(args[0])){
                    p.sendMessage(PLUGIN_COMMAND_PURSE_TO_ITEM_VALUE_TO_HIGH);
                    return true;
                }
                else if(TypeHelper.isInt(args[0])){
                    PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
                    double purse = pe.getPurse();
                    int coins = Math.abs(Integer.parseInt(args[0]));

                    if(coins == 0){
                        p.sendMessage(PLUGIN_COMMAND_PURSE_TO_ITEM_VALUE_INVALID);
                        return true;
                    }

                    if((purse < coins)){
                        p.sendMessage(PLUGIN_COMMAND_PURSE_TO_ITEM_NOT_ENOUGH_MONEY);
                        return true;
                    }

                    
                    ItemStack coin = CustomItems.coins.getCustomItem();
                    ItemMeta im = coin.getItemMeta();
                    im.setLore(Arrays.asList(String.format(ItemConstants.PLUGIN_ITEM_COINS_LORE, StringHelper.formatInt(coins))));
                    im.getPersistentDataContainer().set(ItemConstants.PLUGIN_ITEM_COINS_NAMESPACE, PersistentDataType.INTEGER, coins);

                    coin.setItemMeta(im);

                    pe.setPurse(pe.getPurse() - coins);
                    pe.setToBeUpdated(true);
                    pe.setUpdatedBy(pe.getID());

                    p.getInventory().addItem(coin);
                    p.sendMessage(String.format(PLUGIN_COMMAND_PURSE_TO_ITEM, StringHelper.formatInt(coins)));
                    return true;
                }

                p.sendMessage(PLUGIN_COMMAND_PURSE_TO_ITEM_VALUE_INVALID);
                return true;
            }
        }
        return false;
    }


}
