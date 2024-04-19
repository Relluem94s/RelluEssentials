package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class EnchantmentManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_REGISTER_ENCHANTMENTS);
        int enchantmentCount = 0;
        EnchantmentHelper.registerEnchants(CustomEnchants.autosmelt);           enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.replenishment);       enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.telekinesis);         enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.delicate);            enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.thunderstrike);       enchantmentCount++;

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_ENCHANTMENTS_REGISTERED, enchantmentCount));
    }
    
}
