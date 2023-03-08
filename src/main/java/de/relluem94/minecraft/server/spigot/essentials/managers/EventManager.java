package de.relluem94.minecraft.server.spigot.essentials.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;

import de.relluem94.minecraft.server.spigot.essentials.events.BetterBags;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLights;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLock;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterMobs;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSavety;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSoil;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterWorlds;
import de.relluem94.minecraft.server.spigot.essentials.events.MOTD;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterNPC;
import de.relluem94.minecraft.server.spigot.essentials.events.NoDeathMessage;
import de.relluem94.minecraft.server.spigot.essentials.events.PlayerMove;
import de.relluem94.minecraft.server.spigot.essentials.events.BlockPlace;
import de.relluem94.minecraft.server.spigot.essentials.events.CloudSailor;
import de.relluem94.minecraft.server.spigot.essentials.events.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.events.SkullInfo;
import de.relluem94.minecraft.server.spigot.essentials.events.ToolCrafting;
import de.relluem94.minecraft.server.spigot.essentials.events.SignActions;
import de.relluem94.minecraft.server.spigot.essentials.events.SignClick;
import de.relluem94.minecraft.server.spigot.essentials.events.SignEdit;


import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class EventManager implements Manager{
    
    @Override
    public void manage() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_REGISTER_EVENTS);
        /*	Events	*/
        pm.registerEvents(new BetterChatFormat(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterWorlds(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterPlayerJoin(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterPlayerQuit(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterBlockDrop(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterLights(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterBags(), RelluEssentials.getInstance());
        pm.registerEvents(new BlockPlace(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterMobs(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterSoil(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterNPC(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterSavety(), RelluEssentials.getInstance());
        pm.registerEvents(new BetterLock(), RelluEssentials.getInstance());
        pm.registerEvents(new SkullInfo(), RelluEssentials.getInstance());
        pm.registerEvents(new NoDeathMessage(), RelluEssentials.getInstance());
        pm.registerEvents(new PlayerMove(), RelluEssentials.getInstance());
        pm.registerEvents(new MOTD(), RelluEssentials.getInstance());
        pm.registerEvents(new CloudSailor(), RelluEssentials.getInstance());
        pm.registerEvents(new SignActions(), RelluEssentials.getInstance());
        pm.registerEvents(new SignClick(), RelluEssentials.getInstance());
        pm.registerEvents(new SignEdit(), RelluEssentials.getInstance());
        pm.registerEvents(new ToolCrafting(), RelluEssentials.getInstance());
        pm.registerEvents(new CustomEnchantment(), RelluEssentials.getInstance());
    }
}
