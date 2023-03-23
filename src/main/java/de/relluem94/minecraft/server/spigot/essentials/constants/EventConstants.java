package de.relluem94.minecraft.server.spigot.essentials.constants;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import de.relluem94.minecraft.server.spigot.essentials.Strings;

/**
 *
 * @author rellu
 */
public class EventConstants {

    private EventConstants() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    //==============================================================================//
    //                             EVENT   STUFF                                    //
    //==============================================================================//
    public static final String PLUGIN_EVENT_JOIN_MESSAGE = PLUGIN_COLOR_POSITIVE + "[" + PLUGIN_SYMBOL_BLACK_FOUR_POINTED_STAR + "] " + PLUGIN_COLOR_MESSAGE + "%s" + PLUGIN_COLOR_COMMAND + " hat den Server betreten.";
    public static final String PLUGIN_EVENT_QUIT_MESSAGE = PLUGIN_COLOR_NEGATIVE + "[" + PLUGIN_SYMBOL_CROSS_MARK + "] " + PLUGIN_COLOR_MESSAGE + "%s" + PLUGIN_COLOR_COMMAND + " hat den Server verlassen.";
    public static final String PLUGIN_EVENT_DEATH = PLUGIN_FORMS_COMMAND_PREFIX + "Du starbst bei " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + PLUGIN_COMMAND_WHERE_STRING + PLUGIN_COLOR_COMMAND;
    public static final String PLUGIN_EVENT_DEATH_TP = PLUGIN_FORMS_COMMAND_PREFIX + "Klicke diese Nachricht um dich zum Todespunkt zu teleportieren!";

    public static final String PLUGIN_EVENT_SKILL_REPAIR_DONE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast den Gegenstand repariert!";
    public static final String PLUGIN_EVENT_SKILL_REPAIR_WARNING = PLUGIN_FORMS_COMMAND_PREFIX + "Du benötigst mehr" + PLUGIN_COLOR_COMMAND_ARG + " %s " + PLUGIN_COLOR_COMMAND + "um diesen Gegenstand zu reparieren!";
    public static final String PLUGIN_EVENT_SKILL_SALVAGE_DONE = PLUGIN_FORMS_COMMAND_PREFIX + "Du hast" + PLUGIN_COLOR_COMMAND_ARG + " %s " + PLUGIN_COLOR_COMMAND + "erhalten!";
    
    public static final String PLUGIN_EVENT_SKULL_INFO_SPACER = "§8~~~~~~~~~~~~~~~~~~~~~~~";
    
    public static final String PLUGIN_EVENT_NO_DEATH_MESSAGE = "death_%s";

    public static final String PLUGIN_EVENT_PROTECT_RIGHTS = "IDs";
    public static final String PLUGIN_EVENT_PROTECT_FLAGS = "flags";

    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW_ADMIN_OVERWRITE = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_CHECK_MARK + PLUGIN_COLOR_COMMAND + " Admin Rights Detected Protection was ignored!";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_CHECK_MARK + PLUGIN_COLOR_COMMAND + " You're allowed to do this Action!";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + " You're not allowed to do this Action!";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_BLACK_LARGE_CIRCLE + PLUGIN_COLOR_COMMAND + " Auto Closed!";

    
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO = PLUGIN_FORMS_COMMAND_PREFIX + "Protection Info:";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_ID = "Protection ID: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_CREATED = "Created: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_UPDATED = "Updated: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_LOCATION = "Location: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + ", " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + ", " + PLUGIN_COLOR_COMMAND_ARG + "%s " + PLUGIN_COLOR_COMMAND + "in World " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_ID = "Player ID: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_UUID = "Player UUID: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_NAME = "Player Name: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN = "Last Login: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT = "E MMM d y hh:mm:ss a";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_MATERIAL = "Material: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_FLAGS = "Flags: " + PLUGIN_COLOR_COMMAND_ARG + "%s";
    public static final String PLUGIN_EVENT_PROTECTED_BLOCK_INFO_RIGHTS = "Rigths: " + PLUGIN_COLOR_COMMAND_ARG + "%s";

    public static final String PLUGIN_EVENT_PROTECT_BLOCK_ADD = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_CHECK_MARK + PLUGIN_COLOR_COMMAND + " Successful created Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + " Can't create Protection, Doublechest detected!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_REMOVE = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + " Successful removed Protection!";

    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_CHECK_MARK + PLUGIN_COLOR_COMMAND + "Successful added Flag to Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD_FAILED = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + "Can't add Flag to Protection, Flag already found on Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + "Successful removed Flag from Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE_FAILED = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + "Can't remove Flag from Protection, Flag not found on Protection!";

    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_CHECK_MARK + PLUGIN_COLOR_COMMAND + "Successful added Right to Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + "Can't add Right to Protection, Right already found on Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + "Successful removed Right from Protection!";
    public static final String PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED = PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE+ PLUGIN_SYMBOL_HEAVY_MULTIPLICATION_X + PLUGIN_COLOR_COMMAND + "Can't remove Right from Protection, Right not found on Protection!";

    public static final String PLUGIN_EVENT_TO_MANY_PLAYERS_CANT_JOIN = PLUGIN_COLOR_NEGATIVE + "Der Server ist leider voll";

    public static final String PLUGIN_EVENT_SIGN_COPY = PLUGIN_FORMS_COMMAND_PREFIX + "Sign Copied";
    public static final String PLUGIN_EVENT_SIGN_COPY_TO_PASTE_MESSAGE = PLUGIN_FORMS_COMMAND_PREFIX + "Click to paste copied Sign";
    public static final String PLUGIN_EVENT_SIGN_PASTE = PLUGIN_FORMS_COMMAND_PREFIX + "Sign Pasted";
    public static final String PLUGIN_EVENT_SIGN_EDIT = PLUGIN_FORMS_COMMAND_PREFIX + "Sign Opened";
    public static final String PLUGIN_EVENT_NPC_BAGS_NO_COINS = PLUGIN_FORMS_COMMAND_PREFIX + "To less " + PLUGIN_NAME_MONEY;
    public static final String PLUGIN_EVENT_NPC_BAGS_NO_BAG_FOUND = PLUGIN_FORMS_COMMAND_PREFIX + "Error no Bag found";

    public static final String PLUGIN_EVENT_NPC_BAGS_BOUGHT = PLUGIN_FORMS_COMMAND_PREFIX + "You bought a %s";
    public static final String PLUGIN_EVENT_NPC_BAGS_ALREADY_BOUGHT = PLUGIN_FORMS_COMMAND_PREFIX + "You already Bought a %s";

    public static final String PLUGIN_EVENT_NPC_BANKER_DEPOIST_MESSAGE = PLUGIN_FORMS_COMMAND_PREFIX  + "You have deposited " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " to your Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_DEPOIST_NO_COINS_MESSAGE = PLUGIN_FORMS_COMMAND_PREFIX  + "To less " + PLUGIN_NAME_MONEY + " to do a transaction";
    public static final String PLUGIN_EVENT_NPC_BANKER_DEPOIST_LIMIT_MESSAGE = PLUGIN_FORMS_COMMAND_PREFIX  + "Your Bank Account is full. Consider Upgrading it!";
    public static final String PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE = PLUGIN_FORMS_COMMAND_PREFIX  + "You have withdrawn " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " from your Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_WITHDRAW_NO_COINS_MESSAGE = PLUGIN_FORMS_COMMAND_PREFIX  + "To less " + PLUGIN_NAME_MONEY + " to do a transaction";

    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION = PLUGIN_FORMS_COMMAND_PREFIX  + "Your transactions are:";
    


    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE = PLUGIN_SYMBOL_RIGHT_POINTING_ANGLE_BRACKET + PLUGIN_COLOR_MESSAGE;
    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE = PLUGIN_SYMBOL_LEFT_POINTING_ANGLE_BRACKET + PLUGIN_COLOR_MESSAGE;
    public static final String PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST = Strings.PLUGIN_FORMS_WHITESPACE_SHORT + "%s Transaction with " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " on %s";
    public static final String PLUGIN_EVENT_NPC_BANKER_TOTAL = PLUGIN_FORMS_COMMAND_PREFIX + "You have " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in your Bank!";

    public static final String PLUGIN_EVENT_NPC_BUY = PLUGIN_FORMS_COMMAND_PREFIX + "Bought %s for " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + "." + PLUGIN_EOL + PLUGIN_FORMS_WHITESPACE_SHORT + "You now have " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in your Purse!";
    public static final String PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS = PLUGIN_FORMS_COMMAND_PREFIX  + "Can't buy %s for " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + "." + PLUGIN_EOL + PLUGIN_FORMS_WHITESPACE_SHORT + "You only have " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in your Purse!";
    public static final String PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL = PLUGIN_FORMS_COMMAND_PREFIX  + "Can't buy %s for " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + "." + PLUGIN_EOL + PLUGIN_FORMS_WHITESPACE_SHORT + "You have not enough Inventory Space left!";
    public static final String PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE = PLUGIN_FORMS_COMMAND_PREFIX  + "Can't buy this Item";

    public static final String PLUGIN_EVENT_NPC_SELL = PLUGIN_FORMS_COMMAND_PREFIX + "Sold %s for " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + "." + PLUGIN_EOL + PLUGIN_FORMS_WHITESPACE_SHORT + "You now have " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in your Purse!";
    public static final String PLUGIN_EVENT_NPC_SELL_ENCHANTED = PLUGIN_FORMS_COMMAND_PREFIX + "You can't sell enchanted Items!";
    public static final String PLUGIN_EVENT_NPC_SELL_NO_PRICE = PLUGIN_FORMS_COMMAND_PREFIX + "You can't sell this Item!";
    public static final String PLUGIN_EVENT_NPC_SELL_RENAMED = PLUGIN_FORMS_COMMAND_PREFIX + "You can't sell this Item!";
    public static final String PLUGIN_EVENT_NPC_SELL_USED_ITEM = PLUGIN_FORMS_COMMAND_PREFIX + "You can't sell used Item!";
    public static final String PLUGIN_EVENT_BAG_COLLECT = "Added %sx %s to your Bag";
    public static final String PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT_TO_LESS_COINS = PLUGIN_FORMS_COMMAND_PREFIX + "You don't have enough " + PLUGIN_NAME_MONEY + "." + PLUGIN_EOL + PLUGIN_FORMS_WHITESPACE_SHORT + "The initial Account costs %sCoins!";
    public static final String PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT = PLUGIN_FORMS_COMMAND_PREFIX + "Welcome to your new Bank Account";
    public static final String PLUGIN_EVENT_PLAYER_DEATH_LOST_COINS =  PLUGIN_FORMS_COMMAND_PREFIX + "Du hast " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " verloren!";
    public static final String PLUGIN_EVENT_NPC_SPAWN =  PLUGIN_FORMS_COMMAND_PREFIX + "A %s " + PLUGIN_COLOR_COMMAND + "NPC was placed!";
    public static final String PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT = PLUGIN_FORMS_COMMAND_PREFIX + "No End Point found! Can't teleport.";
    public static final String PLUGIN_EVENT_LIGHTS_TOOGLE = PLUGIN_FORMS_COMMAND_PREFIX + "You toogled the Light";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_LOWER_ACCOUNT = PLUGIN_FORMS_COMMAND_PREFIX + "You already have a greater Tier";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT =  PLUGIN_FORMS_COMMAND_PREFIX + "It's already your Account Tier";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_USING_PURSE = PLUGIN_FORMS_COMMAND_PREFIX + "You Bought the Upgrade with your Purse";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_USING_BANK = PLUGIN_FORMS_COMMAND_PREFIX + "You Bought the Upgrade with your Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_BUY_USING_BOTH = PLUGIN_FORMS_COMMAND_PREFIX + "You Bought the Upgrade with your Purse and Bank Account";
    public static final String PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS = PLUGIN_FORMS_COMMAND_PREFIX + "To less " + PLUGIN_NAME_MONEY;
    public static final String PLUGIN_EVENT_NPC_BANKER_INTEREST = PLUGIN_FORMS_COMMAND_PREFIX + "You earnd " + PLUGIN_COLOR_MONEY + "%s " + PLUGIN_NAME_MONEY + " in interest!";

    public static final String PLUGIN_EVENT_DAMAGE_SHOW = PLUGIN_FORMS_COMMAND_PREFIX +  "Damage: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " Last Damage: " + PLUGIN_COLOR_COMMAND_ARG + "%s" + PLUGIN_COLOR_COMMAND + " Health: " + PLUGIN_COLOR_COMMAND_ARG + "%s";

}