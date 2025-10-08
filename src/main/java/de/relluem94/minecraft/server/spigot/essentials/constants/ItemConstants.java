package de.relluem94.minecraft.server.spigot.essentials.constants;

import org.bukkit.ChatColor;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;

/**
 *
 * @author rellu
 */
public interface ItemConstants {

    //==============================================================================//
    //                              ITEM   STUFF                                    //
    //==============================================================================//

    String PLUGIN_ITEM_DUMMY = ChatColor.AQUA + "This is a Dummy Item";



    String PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS = "cloud_boots";

    String PLUGIN_ITEM_RELLU_HELMET = "§6Rellu's Helmet";
    String PLUGIN_ITEM_RELLU_CHESTPLATE = "§3Rellu's Chestplate";
    String PLUGIN_ITEM_RELLU_LEGGINGS = "§aRellu's Leggings";
    String PLUGIN_ITEM_RELLU_BOOTS = "§cRellu's Boots";
    String PLUGIN_ITEM_RELLU_SWORD = "§eRellu's Sword";
    String PLUGIN_ITEM_RELLU_SHIELD = "§5Rellu's Shield";
    String PLUGIN_ITEM_RELLU_PICKAXE = "§eRellu's Pickaxe";


    String PLUGIN_ITEM_AUTOSELLHOPER = PLUGIN_COLOR_MONEY + "\uD835\uDE3C\uD835\uDE6A\uD835\uDE69\uD835\uDE64 \uD835\uDE4E\uD835\uDE5A\uD835\uDE61\uD835\uDE61 \uD835\uDE43\uD835\uDE64\uD835\uDE65\uD835\uDE65\uD835\uDE5A\uD835\uDE67";
    String PLUGIN_ITEM_GRAPPLINGHOCK = "§cGrappling Hook";
    String PLUGIN_ITEM_WORLDSELECTOR = "§eWorld Selector";

    String PLUGIN_ITEM_INGREDIENT = "§4§l§oThis is a crafting ingredient";

    String PLUGIN_ITEM_CLOUDBOOTS = "§bCloud Boots";
    String PLUGIN_ITEM_CLOUDBOOTS_LORE1 = "§bGrants gliding abillity if worn.";
    String PLUGIN_ITEM_CLOUDBOOTS_LORE2 = "§bAlso reduces Fall Damage by 100%";
    String PLUGIN_ITEM_CLOUDSAILOR = "§bCloud Sailor";
    String PLUGIN_ITEM_CLOUDSAILOR_LORE1 = "§bHeld in Off Hand grants gliding abillity.";
    String PLUGIN_ITEM_CLOUDSAILOR_LORE2 = "§bAlso reduces Fall Damage by 50%";

    String PLUGIN_ITEM_POSITION_AXE = "§6Position Axe";
    String PLUGIN_ITEM_POSITION_AXE_LORE1 = "Left click to set first Position";
    String PLUGIN_ITEM_POSITION_AXE_LORE2 = "Right click for the second Positon";

    String PLUGIN_ITEM_NPC_BAGSALESMAN = "§dBag Salesman";



    String PLUGIN_ITEM_NPC_BANKER_COLOR = "§d";

    String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT = PLUGIN_ITEM_NPC_BANKER_COLOR + "Deposit " + PLUGIN_NAME_MONEY;

    String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_ALL = PLUGIN_ITEM_NPC_BANKER_COLOR + "Deposit all " + PLUGIN_NAME_MONEY;
    String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT = PLUGIN_ITEM_NPC_BANKER_COLOR + "Deposit " + PLUGIN_COLOR_MONEY + "%s" + PLUGIN_ITEM_NPC_BANKER_COLOR + " percent of " + PLUGIN_NAME_MONEY;
    String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1 = "Click to deposit";

    String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_ALL = PLUGIN_ITEM_NPC_BANKER_COLOR + "Withdraw all " + PLUGIN_NAME_MONEY;
    String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT = PLUGIN_ITEM_NPC_BANKER_COLOR + "Withdraw " + PLUGIN_COLOR_MONEY + "%s" + PLUGIN_ITEM_NPC_BANKER_COLOR + " percent of " + PLUGIN_NAME_MONEY;
    String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1 = "Click to withdraw";

    String PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_LORE1 = "Click to open";

    String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW = PLUGIN_ITEM_NPC_BANKER_COLOR + "Withdraw " + PLUGIN_NAME_MONEY;
    String PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_LORE1 = "Click to open";

    String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE = PLUGIN_ITEM_NPC_BANKER_COLOR + "Show Balance";
    String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_LORE1 = "Click to open";

    String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TOTAL = PLUGIN_ITEM_NPC_BANKER_COLOR + "Click to get your Balance";
    String PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TRANSACTIONS = PLUGIN_ITEM_NPC_BANKER_COLOR + "Click to get your last Transactions";


    String PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE = PLUGIN_ITEM_NPC_BANKER_COLOR + "Upgrade Account";
    String PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE_LORE1 = "Click to open";


    String PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK = PLUGIN_ITEM_NPC_BANKER_COLOR + "Portable Bank";
    String PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK_LORE1 = PLUGIN_ITEM_NPC_BANKER_COLOR + "Opens the Bank Menu";

    String PLUGIN_ITEM_COINS = Constants.PLUGIN_NAME_MONEY;
    String PLUGIN_ITEM_COINS_LORE =  Constants.PLUGIN_COLOR_MONEY + "%s " + Constants.PLUGIN_NAME_MONEY;
    
    String PLUGIN_ITEM_NPC_LORE1 = "§7Click to Spawn";

    String PLUGIN_ITEM_MAGIC_WATER_BUCKET = "§dMagic Water Bucket";
    String PLUGIN_ITEM_MAGIC_WATER_BUCKET_LORE = "§7Refills immediately";
}
