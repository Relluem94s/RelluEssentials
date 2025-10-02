package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_CHAT_CLEARED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_END;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_CLEAN_PROTECTIONS_START;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_PING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_PING_OTHER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_PING_OTHER_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_ADMIN_TOP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WRONG_SUB_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.HashMap;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;

@CommandName("admin")
public class Admin implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return Admin.Commands.values();
    }


    @Getter
    public enum Commands implements CommandsEnum {
        AFK("afk"),
        CLEAN_PROTECTIONS("cleanProtections"),
        CHAT("chat"),
        LIGHT("light"),
        NPC("npc"),
        PING("ping"),
        TOP("top");


        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
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
            p.sendMessage(Constants.PLUGIN_COMMAND_ADMIN_INFO);
            return true;
        } else if (args.length == 1) {
            if (Commands.NPC.getName().equalsIgnoreCase(args[0])) {
                if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }

                org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(
                    InventoryHelper.createInventory(18,
                    Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE+ "§dNPCs"),
                    CustomItems.npc_gui_disabled.getCustomItem()
                );

                for (int i = 0; i < RelluEssentials.getInstance().getNpcAPI().getNPCs().size(); i++) {
                    inv.setItem(i, RelluEssentials.getInstance().getNpcAPI().getNPCs().get(i).getItemHelper().getCustomItem());
                }

                InventoryHelper.openInventory(sender, inv);
                return true;
            } 
            else if (Commands.PING.getName().equalsIgnoreCase(args[0])) {
                p.sendMessage(String.format(PLUGIN_COMMAND_ADMIN_PING, p.getPing()));
                return true;
            }
            else if (Commands.CHAT.getName().equalsIgnoreCase(args[0])) {
                for (Player bp : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < 100; i++) {
                        bp.sendMessage("");
                    }
                }
                p.sendMessage(PLUGIN_COMMAND_ADMIN_CHAT_CLEARED);
                return true;
            }
            else if (Commands.LIGHT.getName().equalsIgnoreCase(args[0])) {
                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

                if (pe.getPlayerState().equals(PlayerState.LIGHT_TOGGLE)) {
                    pe.setPlayerState(PlayerState.DEFAULT);
                    p.sendMessage(Constants.PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE_DISABLED);
                } else {
                    p.sendMessage(Constants.PLUGIN_COMMAND_ADMIN_LIGHT_TOOGLE);
                    pe.setPlayerState(PlayerState.LIGHT_TOGGLE);
                }
                return true;
            }
            else if (Commands.CLEAN_PROTECTIONS.getName().equalsIgnoreCase(args[0])) {
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

                if (removeMap.isEmpty()) {
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
            else if (Commands.AFK.getName().equalsIgnoreCase(args[0])) {
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
            else if (Commands.TOP.getName().equalsIgnoreCase(args[0])) {
                Location l = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0, 1, 0);
                p.sendMessage(PLUGIN_COMMAND_ADMIN_TOP);
                p.teleport(l);
                return true;
            }
            else {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }
        }
        else if (args.length == 2) {
            if (Commands.PING.getName().equalsIgnoreCase(args[0])) {
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
            p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
            return true;
        }
        return false;
    }
}
