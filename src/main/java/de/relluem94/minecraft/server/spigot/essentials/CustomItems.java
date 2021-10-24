package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.ItemType;
import java.util.Arrays;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 *
 * @author rellu
 */
public class CustomItems {
    public static ItemHelper cloudSailor = new ItemHelper(Material.FEATHER, 1, PLUGIN_ITEM_CLOUDSAILOR, ItemType.HUB, Arrays.asList(new String[] {PLUGIN_ITEM_CLOUDSAILOR_LORE1, PLUGIN_ITEM_CLOUDSAILOR_LORE2}));
    public static ItemHelper cloudBoots = new ItemHelper(Material.LEATHER_BOOTS, 1, PLUGIN_ITEM_CLOUDBOOTS, ItemType.HUB, Arrays.asList(new String[] {PLUGIN_ITEM_CLOUDBOOTS_LORE1, PLUGIN_ITEM_CLOUDBOOTS_LORE2})){
        @Override
        public void init(){
            LeatherArmorMeta cloudBootsMeta = (LeatherArmorMeta) getItemMeta();
            cloudBootsMeta.setDisplayName(PLUGIN_ITEM_CLOUDBOOTS);
            cloudBootsMeta.setColor(Color.SILVER);
            cloudBootsMeta.setUnbreakable(true);
            setItemMeta(cloudBootsMeta);
        }
    };
}
