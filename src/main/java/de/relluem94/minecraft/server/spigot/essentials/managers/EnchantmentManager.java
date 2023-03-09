package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class EnchantmentManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_REGISTER_ENCHANTMENTS);
        int enchantmentCount = 0;
        EnchantmentHelper.registerEnchants(CustomEnchants.autosmelt);           enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.replenishment);       enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.telekinesis);         enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.delicate);            enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.thunderstrike);       enchantmentCount++;

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + String.format(LANG_ENCHANTMENTS_REGISTERED, enchantmentCount));
    }
    
}
