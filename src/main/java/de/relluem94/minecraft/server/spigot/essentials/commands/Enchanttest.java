package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import java.util.Objects;
import main.java.de.relluem94.minecraft.server.spigot.essentials.Strings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class Enchanttest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Permission.isAuthorized(p, 2)) {
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
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }
        }

        return false;
    }

}
