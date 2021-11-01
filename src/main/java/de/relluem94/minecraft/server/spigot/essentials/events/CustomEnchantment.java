package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.telekinesis;
import java.util.Random;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

/**
 *
 * @author rellu
 */
public class CustomEnchantment implements Listener {

    Random random = new Random();

    @EventHandler
    public void addCustomEnchantToTable(EnchantItemEvent e) {
        if (e.getExpLevelCost() == 30) {
            int r = random.nextInt(10) + 1;
            if (r == 9 || r == 4) {
                telekinesis.addTo(e.getItem());
                e.getEnchantsToAdd().merge(telekinesis, 1, (prev, one) -> prev + one);
            }
        }
    }
}
