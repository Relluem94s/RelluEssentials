package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.helpers.MobHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("AQmNCRXEdwSGU7DvEcXTbBkp2qEaCSSNkQcMhL3m7KSDtmXWaxtbYCaQCFBR96fj")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.getName().equalsIgnoreCase("Relluem94")) {
                    MobHelper mh = new MobHelper(p.getLocation(), EntityType.ZOGLIN, 40, "X Æ A-12", true);
                    mh.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1));
                    mh.setCanPickupItems(true);

                    mh.spawn();
                } else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10, 1));
                }
            }
        }
        return false;
    }
}
