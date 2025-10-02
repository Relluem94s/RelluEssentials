package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.TypeUtils;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("bags")
public class Bags implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {

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

        BagTypeEntry bte;
        if(TypeUtils.isInt(args[0])){
            bte = BagHelper.getBagTypeById(Integer.parseInt(args[0]));
        }
        else{
            bte = BagHelper.getBagTypeByName(args[0]);
        }

        if(bte != null){
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            if(BagHelper.hasBag(pe.getId(), bte.getId())){
                p.openInventory(Objects.requireNonNull(BagHelper.getBag(bte.getId(), pe)));
            }
            else{
                p.sendMessage(String.format(PLUGIN_COMMAND_BAGS_NOT_FOUND, args[0]));
            }

        }
        else{
            p.sendMessage(String.format(PLUGIN_COMMAND_BAGS_NOT_FOUND, args[0]));
        }
        return true;
    }
}