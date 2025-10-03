package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("fly")
public class Fly implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {

        if (!isPlayer(sender)){
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            flyMode(p);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null){
            p.sendMessage(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER);
            return true;
        }
        
        if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(String.format(PLUGIN_COMMAND_FLYMODE, target.getCustomName(), !target.getAllowFlight() ? PLUGIN_COMMAND_FLYMODE_ACTIVATED : PLUGIN_COMMAND_FLYMODE_DEACTIVATED));
            flyMode(target);
        }
        else {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);

        }
        return true;
    }

    private void flyMode(@NotNull Player p) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        pe.setFlying(!pe.isFlying());
        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);
        p.setAllowFlight(pe.isFlying());
        p.sendMessage(
            PLUGIN_FORMS_COMMAND_PREFIX + 
            String.format(
                getText(p.getLocale(), "PLUGIN_COMMAND_FLYMODE"), 
                p.getCustomName() + PLUGIN_COLOR_COMMAND, 
                PLUGIN_COLOR_COMMAND_ARG + 
                (pe.isFlying() ? 
                    getText(p.getLocale(), "PLUGIN_COMMAND_FLYMODE_ACTIVATED") : 
                    getText(p.getLocale(), "PLUGIN_COMMAND_FLYMODE_DEACTIVATED")
                ) + PLUGIN_COLOR_COMMAND
            )
        );
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return new ArrayList<>();
        }

        if(strings.length > 1){
            return new ArrayList<>();
        }

        return TabCompleterHelper.getOnlinePlayers();
    }
}