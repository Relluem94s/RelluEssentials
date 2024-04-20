package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_EVENTS_REGISTERED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_REGISTER_EVENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBags;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLights;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLock;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterMobs;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterNPC;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSavety;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterSoil;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterWorlds;
import de.relluem94.minecraft.server.spigot.essentials.events.BlockPlace;
import de.relluem94.minecraft.server.spigot.essentials.events.CloudSailor;
import de.relluem94.minecraft.server.spigot.essentials.events.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.events.GrapplingHockEvent;
import de.relluem94.minecraft.server.spigot.essentials.events.MOTD;
import de.relluem94.minecraft.server.spigot.essentials.events.NoDeathMessage;
import de.relluem94.minecraft.server.spigot.essentials.events.PlayerMove;
import de.relluem94.minecraft.server.spigot.essentials.events.SignActions;
import de.relluem94.minecraft.server.spigot.essentials.events.SignClick;
import de.relluem94.minecraft.server.spigot.essentials.events.SignEdit;
import de.relluem94.minecraft.server.spigot.essentials.events.SkullInfo;
import de.relluem94.minecraft.server.spigot.essentials.events.ToolCrafting;

public class EventManager implements IEnable{
    
    @Override
    public void enable() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_REGISTER_EVENTS);

        int eventCount = 0;
        pm.registerEvents(new BetterChatFormat(), RelluEssentials.getInstance());           eventCount++;
        pm.registerEvents(new BetterWorlds(), RelluEssentials.getInstance());               eventCount++;
        pm.registerEvents(new BetterPlayerJoin(), RelluEssentials.getInstance());           eventCount++;
        pm.registerEvents(new BetterPlayerQuit(), RelluEssentials.getInstance());           eventCount++;
        pm.registerEvents(new BetterBlockDrop(), RelluEssentials.getInstance());            eventCount++;
        pm.registerEvents(new BetterLights(), RelluEssentials.getInstance());               eventCount++;
        pm.registerEvents(new BetterBags(), RelluEssentials.getInstance());                 eventCount++;
        pm.registerEvents(new BlockPlace(), RelluEssentials.getInstance());                 eventCount++;
        pm.registerEvents(new BetterMobs(), RelluEssentials.getInstance());                 eventCount++;
        pm.registerEvents(new BetterSoil(), RelluEssentials.getInstance());                 eventCount++;
        pm.registerEvents(new BetterNPC(), RelluEssentials.getInstance());                  eventCount++;
        pm.registerEvents(new BetterSavety(), RelluEssentials.getInstance());               eventCount++;
        pm.registerEvents(new BetterLock(), RelluEssentials.getInstance());                 eventCount++;
        pm.registerEvents(new SkullInfo(), RelluEssentials.getInstance());                  eventCount++;
        pm.registerEvents(new NoDeathMessage(), RelluEssentials.getInstance());             eventCount++;
        pm.registerEvents(new PlayerMove(), RelluEssentials.getInstance());                 eventCount++;
        pm.registerEvents(new MOTD(), RelluEssentials.getInstance());                       eventCount++;
        pm.registerEvents(new CloudSailor(), RelluEssentials.getInstance());                eventCount++;
        pm.registerEvents(new SignActions(), RelluEssentials.getInstance());                eventCount++;
        pm.registerEvents(new SignClick(), RelluEssentials.getInstance());                  eventCount++;
        pm.registerEvents(new SignEdit(), RelluEssentials.getInstance());                   eventCount++;
        pm.registerEvents(new ToolCrafting(), RelluEssentials.getInstance());               eventCount++;
        pm.registerEvents(new CustomEnchantment(), RelluEssentials.getInstance());          eventCount++;
        pm.registerEvents(new GrapplingHockEvent(), RelluEssentials.getInstance());         eventCount++;
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_EVENTS_REGISTERED, eventCount));
    }
}
