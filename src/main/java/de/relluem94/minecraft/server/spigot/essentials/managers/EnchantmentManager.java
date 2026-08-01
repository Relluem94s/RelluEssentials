package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AttributeHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.model.enchantment.EnchantName;
import de.relluem94.minecraft.server.spigot.essentials.registry.EnchantmentRegistry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.plugin.Plugin;

/**
 * Manages the registration of all custom enchantments for the plugin. Implements {@link Enable} to
 * hook into the plugin lifecycle.
 */
public class EnchantmentManager implements Enable {

  private final Plugin plugin;

  /**
   * Creates a new EnchantmentManager with the given plugin instance.
   *
   * @param plugin the plugin instance used for enchantment registration
   */
  public EnchantmentManager(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Registers all custom enchantments into the {@link EnchantmentRegistry}. This includes
   * enchantments such as Autosmelt, Telekinesis, Replenishment, Delicate and Thunderstrike.
   */
  @Override
  public void enable() {
    EnchantmentRegistry.register(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT,
        new EnchantmentHelper(
            new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT,
                EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_DISPLAYNAME),
            EnchantmentTarget.TOOL,
            new EnchantLevel(1, 1),
            EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LORE,
            Rarity.LEGENDARY,
            AttributeHelper.addAttribute()
        ));

    EnchantmentRegistry.register(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS,
        new EnchantmentHelper(
            new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS,
                EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS_DISPLAYNAME),
            EnchantmentTarget.BREAKABLE,
            new EnchantLevel(1, 1),
            EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS_LORE,
            Rarity.LEGENDARY,
            AttributeHelper.addAttribute()
        ));

    EnchantmentRegistry.register(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT,
        new EnchantmentHelper(
            new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT,
                EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT_DISPLAYNAME),
            EnchantmentTarget.TOOL,
            new EnchantLevel(1, 1),
            EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT_LORE,
            Rarity.LEGENDARY,
            AttributeHelper.addAttribute()
        ));

    EnchantmentRegistry.register(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE,
        new EnchantmentHelper(
            new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE,
                EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE_DISPLAYNAME),
            EnchantmentTarget.TOOL,
            new EnchantLevel(1, 1),
            EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE_LORE,
            Rarity.LEGENDARY,
            AttributeHelper.addAttribute()
        ));

    EnchantmentRegistry.register(plugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE,
        new EnchantmentHelper(
            new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE,
                EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE_DISPLAYNAME),
            EnchantmentTarget.WEAPON,
            new EnchantLevel(1, 1),
            EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE_LORE,
            Rarity.LEGENDARY,
            AttributeHelper.addAttribute(
                Attribute.ATTACK_DAMAGE,
                Operation.ADD_NUMBER,
                EquipmentSlotGroup.HAND,
                EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE,
                50
            )
        ));

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_ENCHANTMENTS_REGISTERED,
            EnchantmentRegistry.count()));
  }
}