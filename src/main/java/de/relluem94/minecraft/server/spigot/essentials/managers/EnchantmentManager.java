package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_MANAGER_ENCHANTMENTS_REGISTERED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_MANAGER_REGISTER_ENCHANTMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;

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
        EnchantmentHelper.registerEnchants(CustomEnchants.scavengersI);         enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.scavengersII);        enchantmentCount++;
        EnchantmentHelper.registerEnchants(CustomEnchants.scavengersIII);       enchantmentCount++;

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_ENCHANTMENTS_REGISTERED, enchantmentCount));
    }
    
}
