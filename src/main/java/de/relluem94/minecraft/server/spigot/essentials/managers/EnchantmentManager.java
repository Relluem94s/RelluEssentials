package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EnchantmentConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AttributeHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantName;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.EnchantmentService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.plugin.Plugin;

/**
 * Manages the registration of all custom enchantments for the plugin. Implements {@link Enable} to hook into the plugin
 * lifecycle.
 */
public class EnchantmentManager implements Enable {

  /**
   * Registers all custom enchantments into the {@link EnchantmentRegistry}. This includes enchantments such as
   * Autosmelt, Telekinesis, Replenishment, Delicate and Thunderstrike.
   */
  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    EnchantmentService enchantmentService = serviceContext.getEnchantmentService();

    enchantmentService.register(relluEssentialsPlugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS,
        new EnchantmentHelper(new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS_DISPLAYNAME), EnchantmentTarget.WEAPON,
            new EnchantLevel(1, 1), EnchantmentConstants.PLUGIN_ENCHANTMENT_SCAVENGERS_LORE, Rarity.EPIC, List.of(),
            50000));

    enchantmentService.register(relluEssentialsPlugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_LIFESTEAL,
        new EnchantmentHelper(new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_LIFESTEAL,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_LIFESTEAL_DISPLAYNAME), EnchantmentTarget.WEAPON,
            new EnchantLevel(1, 1), EnchantmentConstants.PLUGIN_ENCHANTMENT_LIFESTEAL_LORE, Rarity.RARE, List.of(),
            25000));

    enchantmentService.register(relluEssentialsPlugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT,
        new EnchantmentHelper(new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_DISPLAYNAME), EnchantmentTarget.TOOL,
            new EnchantLevel(1, 1), EnchantmentConstants.PLUGIN_ENCHANTMENT_AUTOSMELT_LORE, Rarity.LEGENDARY, List.of(),
            250000));

    enchantmentService.register(relluEssentialsPlugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS,
        new EnchantmentHelper(new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS_DISPLAYNAME), EnchantmentTarget.BREAKABLE,
            new EnchantLevel(1, 1), EnchantmentConstants.PLUGIN_ENCHANTMENT_TELEKINESIS_LORE, Rarity.LEGENDARY,
            List.of(), 250000));

    enchantmentService.register(relluEssentialsPlugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT,
        new EnchantmentHelper(new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT_DISPLAYNAME), EnchantmentTarget.TOOL,
            new EnchantLevel(1, 1), EnchantmentConstants.PLUGIN_ENCHANTMENT_REPLENISHMENT_LORE, Rarity.LEGENDARY,
            List.of(), 250000));

    enchantmentService.register(relluEssentialsPlugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE,
        new EnchantmentHelper(new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE_DISPLAYNAME), EnchantmentTarget.TOOL,
            new EnchantLevel(1, 1), EnchantmentConstants.PLUGIN_ENCHANTMENT_DELICATE_LORE, Rarity.LEGENDARY, List.of(),
            250000));

    // noinspection UnstableApiUsage
    enchantmentService.register(relluEssentialsPlugin, EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE,
        new EnchantmentHelper(new EnchantName(EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE,
            EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE_DISPLAYNAME), EnchantmentTarget.WEAPON,
            new EnchantLevel(1, 1), EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE_LORE, Rarity.LEGENDARY,
            AttributeHelper.buildSingleAttributeList(Attribute.ATTACK_DAMAGE, Operation.ADD_NUMBER,
                EquipmentSlotGroup.HAND, EnchantmentConstants.PLUGIN_ENCHANTMENT_THUNDERSTRIKE, 50), 1500000));

    TranslationService translationService = serviceContext.getTranslationService();
    ConsoleCommandSender consoleCommandSender = serviceContext
        .getServerService()
        .getConsoleSender();
    consoleCommandSender.sendMessage(PLUGIN_NAME_CONSOLE
        + translationService.get(MessageKey.PLUGIN_MANAGER_ENCHANTMENTS_REGISTERED, relluEssentialsPlugin
            .getServiceContext()
            .getEnchantmentService()
            .count()));
  }
}