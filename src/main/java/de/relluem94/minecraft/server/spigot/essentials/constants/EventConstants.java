package de.relluem94.minecraft.server.spigot.essentials.constants;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_ARG_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_WHERE_STRING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_MESSAGE_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_SPACER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_MONEY_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_MONEY_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_EOL;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_WHITE_SPACE_SHORT;

import de.relluem94.minecraft.server.spigot.essentials.Strings;

/**
 *
 * @author rellu
 */
public class EventConstants {

    //==============================================================================//
    //                             EVENT   STUFF                                    //
    //==============================================================================//
    public static final String PLUGIN_EVENT_JOIN_MESSAGE = "§2[\u2726] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server betreten.";
    public static final String PLUGIN_EVENT_QUIT_MESSAGE = "§4[\u274C] " + PLUGIN_MESSAGE_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " hat den Server verlassen.";
    public static final String PLUGIN_EVENT_DEATH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du starbst bei " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + PLUGIN_COMMAND_WHERE_STRING + PLUGIN_COMMAND_COLOR;
    public static final String PLUGIN_EVENT_DEATH_TP = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Klicke diese Nachricht um dich zum Todespunkt zu teleportieren!";

    public static final String PLUGIN_EVENT_SKILL_REPAIR_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast den Gegenstand repariert!";
    public static final String PLUGIN_EVENT_SKILL_REPAIR_WARNING = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du benötigst mehr" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "um diesen Gegenstand zu reparieren!";
    public static final String PLUGIN_EVENT_SKILL_SALVAGE_DONE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast" + PLUGIN_COMMAND_ARG_COLOR + " %s " + PLUGIN_COMMAND_COLOR + "erhalten!";
    
    public static final String PLUGIN_EVENT_SKULL_INFO_SPACER = "§8~~~~~~~~~~~~~~~~~~~~~~~";
    
    public static final String PLUGIN_EVENT_NO_DEATH_MESSAGE = "death_%s";


    public static final String PLUGIN_EVENT_PROTECT_RIGHTS = "IDs";
    public static final String PLUGIN_EVENT_PROTECT_FLAGS = "flags";

    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW_ADMIN_OVERWRITE = PLUGIN_PREFIX + PLUGIN_SPACER +"§2\u2714 " + PLUGIN_COMMAND_COLOR + " Admin Rights Detected Protection was ignored!";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW = PLUGIN_PREFIX + PLUGIN_SPACER +"§2\u2714 " + PLUGIN_COMMAND_COLOR + " You're allowed to do this Action!";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + " You're not allowed to do this Action!";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE = PLUGIN_PREFIX + PLUGIN_SPACER +"§6\u2B24 " + PLUGIN_COMMAND_COLOR + " Auto Closed!";

    
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Protection Info:";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_ID = "Protection ID: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_CREATED = "Created: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_UPDATED = "Updated: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_LOCATION = "Location: " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + ", " + PLUGIN_COMMAND_ARG_COLOR + "%s " + PLUGIN_COMMAND_COLOR + "in World " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_ID = "Player ID: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_UUID = "Player UUID: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_NAME = "Player Name: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN = "Last Login: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT = "E MMM d y hh:mm:ss a";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_MATERIAL = "Material: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_FLAGS = "Flags: " + PLUGIN_COMMAND_ARG_COLOR + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_RIGHTS = "Rigths: " + PLUGIN_COMMAND_ARG_COLOR + "%s";

    public static final String PLUGIN_EVENT_PROTECT_BLOCK_ADD = PLUGIN_PREFIX + PLUGIN_SPACER +"§2\u2714 " + PLUGIN_COMMAND_COLOR + " Successful created Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + " Can't create Protection, Doublechest detected!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_REMOVE = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + " Successful removed Protection!";

    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD = PLUGIN_PREFIX + PLUGIN_SPACER +"§2\u2714 " + PLUGIN_COMMAND_COLOR + "Successful added Flag to Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD_FAILED = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + "Can't add Flag to Protection, Flag already found on Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + "Successful removed Flag from Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE_FAILED = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + "Can't remove Flag from Protection, Flag not found on Protection!";

    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD = PLUGIN_PREFIX + PLUGIN_SPACER +"§2\u2714 " + PLUGIN_COMMAND_COLOR + "Successful added Right to Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + "Can't add Right to Protection, Right already found on Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + "Successful removed Right from Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED = PLUGIN_PREFIX + PLUGIN_SPACER +"§4\u2716 " + PLUGIN_COMMAND_COLOR + "Can't remove Right from Protection, Right not found on Protection!";

    public static final String PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN = "§cDer Server ist leider voll";

    public static final String PLUGIN_EVENT_SIGN_COPY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Sign Copied";
    public static final String PLUGIN_EVENT_SIGN_COPY_TO_PASTE_MESSAGE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Click to paste copied Sign";
    public static final String PLUGIN_EVENT_SIGN_PASTE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Sign Pasted";
    public static final String PLUGIN_EVENT_SIGN_EDIT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Sign Opened";
    public static final String PLUGIN_EVENT_NPC_BAGS_NO_COINS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "To less " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_EVENT_NPC_BAGS_NO_BAG_FOUND = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Error no Bag found";

    public static final String PLUGIN_EVENT_NPC_BAGS_BOUGHT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You bought a %s";
    public static final String PLUGIN_EVENT_NPC_BAGS_ALREADY_BOUGHT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You already Bought a %s";

    public static final String PLUGIN_EVENT_NPC_BANKER_DEPOIST_MESSAGE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "You have deposited " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " to your Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_DEPOIST_NO_COINS_MESSAGE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "To less " + PLUGIN_MONEY_NAME + " to do a transaction";
    public static final String PLUGIN_EVENT_NPC_BANKER_DEPOIST_LIMIT_MESSAGE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "Your Bank Account is full. Consider Upgrading it!";
    public static final String PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "You have withdrawn " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " from your Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_WITHDRAW_NO_COINS_MESSAGE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "To less " + PLUGIN_MONEY_NAME + " to do a transaction";

    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "Your transactions are:";


    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE = "§a\u232A" + PLUGIN_MESSAGE_COLOR;
    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE = "§c\u2329" + PLUGIN_MESSAGE_COLOR;
    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST = Strings.PLUGIN_WHITE_SPACE_SHORT + "%s Transaction with " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " on %s";
    public static final String PLUGIN_EVENT_NPC_BANKER_TOTAL = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You have " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in your Bank!";

    public static final String PLUGIN_EVENT_NPC_BUY = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Bought %s for " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + "." + PLUGIN_EOL + PLUGIN_WHITE_SPACE_SHORT + "You now have " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in your Purse!";
    public static final String PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "Can't buy %s for " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + "." + PLUGIN_EOL + PLUGIN_WHITE_SPACE_SHORT + "You only have " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in your Purse!";
    public static final String PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "Can't buy %s for " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + "." + PLUGIN_EOL + PLUGIN_WHITE_SPACE_SHORT + "You have not enough Inventory Space left!";
    public static final String PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR  + "Can't buy this Item";

    public static final String PLUGIN_EVENT_NPC_SELL = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Sold %s for " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + "." + PLUGIN_EOL + PLUGIN_WHITE_SPACE_SHORT + "You now have " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in your Purse!";
    public static final String PLUGIN_EVENT_NPC_SELL_ENCHANTED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You can't sell enchanted Items!";
    public static final String PLUGIN_EVENT_NPC_SELL_NO_PRICE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You can't sell this Item!";
    public static final String PLUGIN_EVENT_NPC_SELL_RENAMED = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You can't sell this Item!";
    public static final String PLUGIN_EVENT_NPC_SELL_USED_ITEM = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You can't sell used Item!";
    public static final String PLUGIN_EVENT_BAG_COLLECT = "Added %sx %s to your Bag";
    public static final String PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT_TO_LESS_COINS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You don't have enough " + PLUGIN_MONEY_NAME + "." + PLUGIN_EOL + PLUGIN_WHITE_SPACE_SHORT + "The initial Account costs %sCoins!";
    public static final String PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Welcome to your new Bank Account";
    public static final String PLUGIN_EVENT_PLAYER_DEATH_LOST_COINS =  PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "Du hast " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " verloren!";
    public static final String PLUGIN_EVENT_NPC_SPAWN =  PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "A %s " + PLUGIN_COMMAND_COLOR + "NPC was placed!";
    public static final String PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "No End Point found! Can't teleport.";
    public static final String PLUGIN_EVENT_LIGHTS_TOOGLE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You toogled the Light";
    public static final String PLUGIN_EVENT_NPC_BANKER_LOWER_ACCOUNT = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You already have a greater Tier";
    public static final String PLUGIN_EVENT_NPC_BANKER_ALREADY_BOUGHT =  PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "It's already your Account Tier";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_USING_PURSE = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You Bought the Upgrade with your Purse";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_USING_BANK = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You Bought the Upgrade with your Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_USING_BOTH = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You Bought the Upgrade with your Purse and Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "To less " + PLUGIN_MONEY_NAME;
    public static final String PLUGIN_EVENT_NPC_BANKER_INTEREST = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + "You earnd " + PLUGIN_MONEY_COLOR + "%s " + PLUGIN_MONEY_NAME + " in interest!";

    public static final String PLUGIN_EVENT_DAMAGE_SHOW = PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR +  "Damage: " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " Last Damage: " + PLUGIN_COMMAND_ARG_COLOR + "%s" + PLUGIN_COMMAND_COLOR + " Health: " + PLUGIN_COMMAND_ARG_COLOR + "%s";

}