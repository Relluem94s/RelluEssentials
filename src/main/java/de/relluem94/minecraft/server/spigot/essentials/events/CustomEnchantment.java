package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.Random;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

/**
 *
 * @author rellu
 */
public class CustomEnchantment implements Listener {

    Random random = new Random();

    @EventHandler
    public void addCustomEnchantToTable(PrepareItemEnchantEvent e) {
        Enchantment as = Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt"));
        Enchantment ts = Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis"));

        if (isTool(e.getItem().getType())) {
            // e.getOffers()[0] = new EnchantmentOffer(as, 1, 1);
            // e.getOffers()[1] = new EnchantmentOffer(new EnchantmentWrapper("autosmelt"), 1, 1);
            // e.getOffers()[2] = new EnchantmentOffer(ts, 1, 1);
//            consoleSendMessage("", "===");
//            for(EnchantmentOffer eo : e.getOffers()){
//                            consoleSendMessage("", eo.getEnchantment().getKey());
//            }

            //e.getOffers()[1] = new EnchantmentOffer(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "autosmelt")), 1, 0);
            //e.getOffers()[1] = new EnchantmentOffer(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis")), 1, 4);
            //e.getOffers()[2] = new EnchantmentOffer(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis")), 1, 3);
//            if (random.nextInt((10 - 0) + 1) >= 0) {
//                if (random.nextInt((10 - 0) + 1) >= 0) {
//                    e.getOffers()[random.nextInt((2 - 0) + 1)] = new EnchantmentOffer(new EnchantmentWrapper(RelluEssentials.autosmelt.getName().toLowerCase()), 1, 1);
//                }
//                if (random.nextInt((10 - 0) + 1) >= 0) {
//                    e.getOffers()[random.nextInt((2 - 0) + 1)] = new EnchantmentOffer(Enchantment.getByKey(new NamespacedKey(Strings.PLUGIN_NAME.toLowerCase(), "telekenesis")), 1, 1);
//                }
//            }
        }
    }

    private boolean isTool(Material item) {
        return item.equals(Material.WOODEN_SHOVEL)
                || item.equals(Material.STONE_SHOVEL)
                || item.equals(Material.IRON_SHOVEL)
                || item.equals(Material.DIAMOND_SHOVEL)
                || item.equals(Material.GOLDEN_SHOVEL)
                || item.equals(Material.WOODEN_PICKAXE)
                || item.equals(Material.STONE_PICKAXE)
                || item.equals(Material.IRON_PICKAXE)
                || item.equals(Material.DIAMOND_PICKAXE)
                || item.equals(Material.GOLDEN_PICKAXE)
                || item.equals(Material.WOODEN_AXE)
                || item.equals(Material.STONE_AXE)
                || item.equals(Material.IRON_AXE)
                || item.equals(Material.DIAMOND_AXE)
                || item.equals(Material.GOLDEN_AXE);
    }

}
