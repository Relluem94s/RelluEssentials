package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import java.util.Objects;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class Enchanttest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (isPlayer(sender)) {
            Player p = (Player) sender;
            if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                if (args.length == 0) {
                    p.getInventory().getItemInMainHand().addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt"))), 1);
                    p.getInventory().getItemInMainHand().addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis"))), 1);
                } else {
                    if (args[0].equals("smelt")) {
                        p.getInventory().getItemInMainHand().addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt"))), 1);

                    } else if (args[0].equals("tele")) {
                        p.getInventory().getItemInMainHand().addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis"))), 1);

                    }
                }
            } else {
                sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }
        }

        return false;
    }

}
