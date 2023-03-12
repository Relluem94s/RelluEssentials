package de.relluem94.minecraft.server.spigot.essentials.constants;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.NamespacedKey;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;

/**
 *
 * @author rellu
 */
public class ItemConstants {

    //==============================================================================//
    //                              ITEM   STUFF                                    //
    //==============================================================================//
    public static final String PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS = "cloud_boots";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_PICKAXE = "smelter_pickaxe";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_TANK = "smelter_tank";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_FURNACE = "smelter_furnace";
    public static final String PLUGIN_ITEM_NAMESPACE_SMELTER_FUEL = "smelter_fuel";

    public static final String PLUGIN_ITEM_RELLU_HELMET = "§6Rellu's Helmet";
    public static final String PLUGIN_ITEM_RELLU_CHESTPLATE = "§3Rellu's Chestplate";
    public static final String PLUGIN_ITEM_RELLU_LEGGINGS = "§aRellu's Leggings";
    public static final String PLUGIN_ITEM_RELLU_BOOTS = "§cRellu's Boots";
    public static final String PLUGIN_ITEM_RELLU_SWORD = "§eRellu's Sword";
    public static final String PLUGIN_ITEM_RELLU_SHIELD = "§5Rellu's Shield";
    public static final String PLUGIN_ITEM_RELLU_PICKAXE = "§eRellu's Pickaxe";

    public static final String PLUGIN_ITEM_INGREDIENT = "§4§l§oThis is a crafting ingredient";

    public static final String PLUGIN_ITEM_AUTOSMELTER = "§cSmelter Pickaxe";
    public static final String PLUGIN_ITEM_AUTOSMELTER_TANK = "§cSmelter Tank";
    public static final String PLUGIN_ITEM_AUTOSMELTER_FURNACE = "§cSmelter Furnace";

    public static final String PLUGIN_ITEM_CLOUDBOOTS = "§bCloud Boots";
    public static final String PLUGIN_ITEM_CLOUDBOOTS_LORE1 = "§bGrants gliding abillity if worn.";
    public static final String PLUGIN_ITEM_CLOUDBOOTS_LORE2 = "§bAlso reduces Fall Damage by 100%";
    public static final String PLUGIN_ITEM_CLOUDSAILOR = "§bCloud Sailor";
    public static final String PLUGIN_ITEM_CLOUDSAILOR_LORE1 = "§bHeld in Off Hand grants gliding abillity.";
    public static final String PLUGIN_ITEM_CLOUDSAILOR_LORE2 = "§bAlso reduces Fall Damage by 50%";

    public static final String PLUGIN_ITEM_NPC_BAGSALESMAN = "§dBag Salesman";



    public static final String PLUGIN_ITEM_NPC_BANKER_COLOR = "§d";

    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT = PLUGIN_ITEM_NPC_BANKER_COLOR + "Deposit " + PLUGIN_MONEY_NAME;

    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_ALL = PLUGIN_ITEM_NPC_BANKER_COLOR + "Deposit all " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT = PLUGIN_ITEM_NPC_BANKER_COLOR + "Deposit " + PLUGIN_MONEY_COLOR + "%s" + PLUGIN_ITEM_NPC_BANKER_COLOR + " percent of " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1 = "Click to deposit";

    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_ALL = PLUGIN_ITEM_NPC_BANKER_COLOR + "Withdraw all " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT = PLUGIN_ITEM_NPC_BANKER_COLOR + "Withdraw " + PLUGIN_MONEY_COLOR + "%s" + PLUGIN_ITEM_NPC_BANKER_COLOR + " percent of " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1 = "Click to withdraw";

    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_LORE1 = "Click to open";

    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW = PLUGIN_ITEM_NPC_BANKER_COLOR + "Withdraw " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_LORE1 = "Click to open";

    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE = PLUGIN_ITEM_NPC_BANKER_COLOR + "Show Balance";
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_LORE1 = "Click to open";

    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TOTAL = PLUGIN_ITEM_NPC_BANKER_COLOR + "Click to get your Balance";
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TRANSACTIONS = PLUGIN_ITEM_NPC_BANKER_COLOR + "Click to get your last Transactions";


    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE = PLUGIN_ITEM_NPC_BANKER_COLOR + "Upgrade Account";
    public static final String PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE_LORE1 = "Click to open";


    public static final String PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK = PLUGIN_ITEM_NPC_BANKER_COLOR + "Portable Bank";
    public static final String PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK_LORE1 = PLUGIN_ITEM_NPC_BANKER_COLOR + "Opens the Bank Menu";

    public static final String PLUGIN_ITEM_COINS = Strings.PLUGIN_MONEY_NAME;
    public static final String PLUGIN_ITEM_COINS_LORE =  Strings.PLUGIN_MONEY_COLOR + "%s " + Strings.PLUGIN_MONEY_NAME;
    public static NamespacedKey PLUGIN_ITEM_COINS_NAMESPACE = new NamespacedKey(RelluEssentials.getInstance(), "coins");
    
    public static final String PLUGIN_ITEM_NPC_LORE1 = "§7Click to Spawn";
}
