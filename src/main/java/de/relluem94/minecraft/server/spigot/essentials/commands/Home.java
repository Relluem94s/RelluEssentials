package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.locationToString;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportBed;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportHome;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("home")
public class Home implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        SET("set"),
        DELETE("delete"),
        LIST("list");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NotNull [] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());


        switch (args.length) {
            case 0:
                teleportBed(p);
                return true;
            case 1:


                if (args[0].equalsIgnoreCase(Commands.LIST.getName())) {
                    if (!pe.getHomes().isEmpty()) {
                        p.sendMessage(PLUGIN_COMMAND_HOME_LIST);
                        pe.getHomes().forEach(fle -> p.sendMessage(String.format(PLUGIN_COMMAND_HOME_LIST_NAME, fle.getLocationName(), locationToString(fle.getLocation()))));
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_HOME_NONE);
                    }

                    if(!pe.getDeaths().isEmpty()){
                        p.sendMessage(PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS);
                        pe.getDeaths().forEach(fle -> p.sendMessage(String.format(PLUGIN_COMMAND_HOME_LIST_DEATHPOINTS_NAME, fle.getLocationName(), locationToString(fle.getLocation()))));
                    }
                } else {
                    LocationEntry le = new LocationEntry();
                    le.setLocationName(args[0]);
                    le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(0));
                    le.setPlayerId(pe.getId());

                    if (homeExists(pe, le) || deathExists(pe, le)) {
                        teleportHome(p, getLocationEntry(pe, le));
                    } else {
                        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NOT_FOUND, args[0]));
                    }
                }
                return true;
            case 2:
                LocationEntry le = new LocationEntry();
                le.setLocation(p.getLocation());
                le.setLocationName(args[1]);
                le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(0));
                le.setPlayerId(pe.getId());

                if (args[0].equalsIgnoreCase(Commands.SET.getName())) {
                    if (homeExists(pe, le)) {
                        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_EXISTS, args[1]));
                    } else if (!args[1].startsWith("death_")) {
                        RelluEssentials.getInstance().getDatabaseHelper().insertLocation(le);
                        pe.getHomes().add(le);
                        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_SET, args[1]));
                    } else {
                        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_RESERVED, args[1]));
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase(Commands.DELETE.getName())) {
                    if (homeExists(pe, le)) {
                        le = getLocationEntry(pe, le);

                        pe.getHomes().remove(le);
                        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DELETE, args[1]));

                        if(le == null){
                            return true;
                        }

                        RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
                        return true;
                    }
                    else if(deathExists(pe, le)){
                        le = getLocationEntry(pe, le);
                        pe.getDeaths().remove(le);
                        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DEATH_DELETE, args[1]));

                        if(le == null){
                            return true;
                        }

                        RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(le);
                        return true;
                    }
                    else if(le.getLocationName().startsWith("death_") && le.getLocationName().contains("*")){
                        for(LocationEntry dle : pe.getDeaths()){
                            p.sendMessage(String.format(PLUGIN_COMMAND_HOME_DEATH_DELETE, dle.getLocationName()));
                            RelluEssentials.getInstance().getDatabaseHelper().deleteLocation(dle);
                        }

                        pe.getDeaths().clear();
                        return true;
                    }
                    else {
                        p.sendMessage(String.format(PLUGIN_COMMAND_HOME_NOT_FOUND, args[1]));
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    private boolean homeExists(@NotNull PlayerEntry pe, LocationEntry le) {
        return pe.getHomes().stream().anyMatch(fle -> (fle.getLocationName().equals(le.getLocationName())));
    }

    private boolean deathExists(@NotNull PlayerEntry pe, LocationEntry le) {
        return pe.getDeaths().stream().anyMatch(fle -> (fle.getLocationName().equals(le.getLocationName())));
    }

    private @Nullable LocationEntry getLocationEntry(@NotNull PlayerEntry pe, LocationEntry le) {
        for (LocationEntry fle : pe.getHomes()) {
            if (fle.getLocationName().equals(le.getLocationName())) {
                return fle;
            }
        }

        for (LocationEntry fle : pe.getDeaths()) {
            if (fle.getLocationName().equals(le.getLocationName())) {
                return fle;
            }
        }
        return null;
    }
}
