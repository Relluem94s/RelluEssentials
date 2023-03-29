package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_CHAT_CLEARED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_END;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_START;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_PING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_PING_OTHER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_PING_OTHER_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_TOP;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ADMIN_WRONG_SUBCOMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ADMIN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ADMIN_PING;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Admin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_ADMIN)) {
            return false;
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

        if (args.length == 0) {
            p.sendMessage(Strings.PLUGIN_COMMAND_ADMIN_INFO);
            return true;
        } else if (args.length == 1) {
            if (args[0].equals("npc")) {
                if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }

                org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(
                    InventoryHelper.createInventory(18,
                    Strings.PLUGIN_NAME_PREFIX + Strings.PLUGIN_FORMS_SPACER_MESSAGE+ "§dNPCs"),
                    CustomItems.npc_gui_disabled.getCustomItem()
                );

                for (int i = 0; i < RelluEssentials.getInstance().getNpcAPI().getNPCs().size(); i++) {
                    inv.setItem(i, RelluEssentials.getInstance().getNpcAPI().getNPCs().get(i).getItemHelper().getCustomItem());
                }

                InventoryHelper.openInventory(sender, inv);
                return true;
            } 
            else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ADMIN_PING)) {
                p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_PING, p.getPing()));
                return true;
            }
            else if (args[0].equalsIgnoreCase("chat")) {
                for (Player bp : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < 100; i++) {
                        bp.sendMessage("");
                    }
                }
                p.sendMessage(PLUGIN_COMMAND_ADMIN_CHAT_CLEARED);
                return true;
            }
            else if (args[0].equalsIgnoreCase("light")) {
                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

                if (pe.getPlayerState().equals(PlayerState.LIGHT_TOOGLE)) {
                    pe.setPlayerState(PlayerState.DEFAULT);
                    p.sendMessage(Strings.PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE_DISABLED);
                } else {
                    p.sendMessage(Strings.PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE);
                    pe.setPlayerState(PlayerState.LIGHT_TOOGLE);
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("cleanProtections")) {
                HashMap<Location, ProtectionEntry> removeMap = new HashMap<>();

                p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_START,
                        RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList().size()));
                for (Location l : RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList().keySet()) {
                    ProtectionEntry pe = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList().get(l);
                    if (!l.getBlock().getType().equals(Material.getMaterial(pe.getMaterialName()))) {
                        removeMap.put(l, pe);
                        p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS, pe.getId(),
                                pe.getMaterialName(), l.getBlock().getType().name()));
                        RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(pe);
                    }
                }

                if (removeMap.size() == 0) {
                    p.sendMessage(PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE);
                    
                }
                else {
                    p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP, removeMap.size()));
                    for (Location l : removeMap.keySet()) {
                        RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(l);
                    }
                    p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_END,
                            RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList().size()));
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("afk")) {
                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

                if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)) {
                    PlayerHelper.setAFK(p, false);
                    pe.setPlayerState(PlayerState.DEFAULT);
                } else {
                    pe.setPlayerState(PlayerState.FAKE_AFK_ON);
                    PlayerHelper.setAFK(p, false);
                    pe.setPlayerState(PlayerState.FAKE_AFK_ACTIVE);
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("top")) {
                Location l = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0, 1, 0);
                p.sendMessage(PLUGIN_COMMAND_ADMIN_TOP);
                p.teleport(l);
                return true;
            }
            else {
                p.sendMessage(PLUGIN_COMMAND_ADMIN_WRONG_SUBCOMMAND);
                return true;
            }
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ADMIN_PING)) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_PING_OTHER_NOT_FOUND, args[1]));
                    return true;
                }

                if (isPlayer(sender)) {
                    p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_PING_OTHER, target.getCustomName(), target.getPing()));
                    return true;
                }
            }
        }
        else {
            p.sendMessage(PLUGIN_COMMAND_ADMIN_WRONG_SUBCOMMAND);
            return true;
        }
        return false;
    }
}
