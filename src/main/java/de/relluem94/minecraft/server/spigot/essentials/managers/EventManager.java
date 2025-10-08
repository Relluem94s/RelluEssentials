package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_EVENTS_REGISTERED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_REGISTER_EVENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.events.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

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
        pm.registerEvents(new BetterSafety(), RelluEssentials.getInstance());               eventCount++;
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
        pm.registerEvents(new PositionAxeListener(), RelluEssentials.getInstance());        eventCount++;
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_EVENTS_REGISTERED, eventCount));
    }
}
