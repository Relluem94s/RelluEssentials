package de.relluem94.minecraft.server.spigot.essentials.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("rollback")
public class Rollback implements CommandConstruct {

    /*
    
    /rollback -> Info
    /rollback player <Player> --> Rolls back Player last to first block.                                                    // DONE
    /rollback player <Player> <Time> --> Rolls back Player last to first block in the last 2Y8M60d10h30m etc                // DONE
  * /rollback location (or pos) (needs selected Area) next update or so.
    /rollback undo player <Player> --> Undo rollback Player first to last  block.
    /rollback undo player <Player> <Time> --> Undo rollback Player first to last block in the last 2Y8M60D10h30m etc.
  * /rollback undo location (needs selected Area)
    /rollback preview player <Player> --> Shows 
    /rollback preview player <Player> <Time> 
  * /rollback preview location (needs selected Area)
    /rollback preview undo player <Player>
    /rollback preview undo player <Player> <Time> 
  * /rollback preview undo location (needs selected Area)
    
    
        
    
    Maybe also as an Inventory Menu.
    Undo reverts last action. (looks for timeframe but ignores the deleted)
    Preview sends block change to player and reverts after 60 seconds (if possible else no preview)
    
     */
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String @NotNull [] args) {
        switch (args.length) {
            case 2:
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        UUID targetUUID = Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId();
                        PlayerEntry pe = RelluEssentials.getInstance().getDatabaseHelper().getPlayer(targetUUID.toString());
                        if (pe != null && args[0].equalsIgnoreCase(Commands.PLAYER.getName())) {
                            int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId()).getId();
                            List<BlockHistoryEntry> list = RelluEssentials.getInstance().getDatabaseHelper().getBlockHistoryByPlayer(pe);
                            for (BlockHistoryEntry bh : list) {
                                bh.setDeletedBy(id);
                                RelluEssentials.getInstance().blockHistoryList.add(bh);
                            }
                            p.sendMessage("Added: " + list.size());
                        }
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
                break;
            case 3:
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        if (args[0].equalsIgnoreCase(Commands.PLAYER.getName())) {
                            UUID targetUUID = Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId();
                            PlayerEntry pe = RelluEssentials.getInstance().getDatabaseHelper().getPlayer(targetUUID.toString());

                            if (pe != null) {
                                int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId()).getId();
                                List<BlockHistoryEntry> list = RelluEssentials.getInstance().getDatabaseHelper().getBlockHistoryByPlayerAndTime(pe, args[2], false);
                                for (BlockHistoryEntry bh : list) {
                                    bh.setDeletedBy(id);
                                    RelluEssentials.getInstance().blockHistoryList.add(bh);
                                }
                                p.sendMessage("Added: " + list.size());
                            }
                        } else if (args[0].equalsIgnoreCase(Commands.UNDO.getName()) && args[1].equalsIgnoreCase(Commands.UNDO.getSubCommands()[0])) {
                            return true;
                        }

                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
                break;
            case 4:
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        if (args[0].equalsIgnoreCase(Commands.UNDO.getName()) && args[1].equalsIgnoreCase(Commands.UNDO.getSubCommands()[0])) {
                            UUID targetUUID = Objects.requireNonNull(Bukkit.getPlayer(args[2])).getUniqueId();
                            PlayerEntry pe = RelluEssentials.getInstance().getDatabaseHelper().getPlayer(targetUUID.toString());

                            if (pe != null) {
                                int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId()).getId();
                                List<BlockHistoryEntry> list = RelluEssentials.getInstance().getDatabaseHelper().getBlockHistoryByPlayerAndTime(pe, args[3], true);
                                for (BlockHistoryEntry bh : list) {
                                    bh.setDeletedBy(id);
                                    RelluEssentials.getInstance().blockHistoryList.add(bh);
                                }
                                p.sendMessage("Added: " + list.size());
                            }
                        }

                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
                break;
            case 5:
                break;
            default:
                // INFO
                break;
        }

        return true;
    }


    @Override
    public CommandsEnum[] getCommands() {
        return Worlds.Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum{
        PLAYER("player"),
        UNDO("undo", "player");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return new ArrayList<>();
        }

        if(strings.length > 1){
            return new ArrayList<>();
        }

        return TabCompleterHelper.getCommands(Commands.values());
    }
}
