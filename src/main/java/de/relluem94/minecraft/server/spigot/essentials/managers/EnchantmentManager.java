package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;

public class EnchantmentManager implements Manager {

    @Override
    public void manage() {
        EnchantmentHelper.registerEnchants(CustomEnchants.autosmelt);
        EnchantmentHelper.registerEnchants(CustomEnchants.replenishment);
        EnchantmentHelper.registerEnchants(CustomEnchants.telekinesis);
        EnchantmentHelper.registerEnchants(CustomEnchants.delicate);
        EnchantmentHelper.registerEnchants(CustomEnchants.thunderstrike);
    }
    
}
