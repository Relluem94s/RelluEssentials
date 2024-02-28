package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldType;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;

public class WorldManager implements IEnable, IDisable {

    private final Random r = new Random();

    @Override
    public void enable() {
        if(RelluEssentials.getInstance().isUnitTest()){
            return;
        }

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + "Worlds Size: " + RelluEssentials.getInstance().worldsMap.size());
        for (WorldGroupEntry wge : RelluEssentials.getInstance().worldsMap.keySet()) {
            if(wge == null){
                continue;
            }
            for(WorldEntry we: RelluEssentials.getInstance().worldsMap.get(wge)){
                if(!WorldHelper.worldExists(we.getName())){
                    createWorld(we);
                    continue;        
                }

                for(World w: Bukkit.getWorlds()){
                    if(!w.getName().equals(we.getName())){
                        WorldHelper.loadWorld(we.getName());
                        setStandardGameRules(we.getName());
                    }
                }
            }
        }
    }

    @Override
    public void disable() {
        if(RelluEssentials.getInstance().isUnitTest()){
            return;
        }
        for (WorldGroupEntry wge : RelluEssentials.getInstance().worldsMap.keySet()) {
            if(wge == null){
                continue;
            }

            for(WorldEntry we: RelluEssentials.getInstance().worldsMap.get(wge)){
                try{
                    WorldHelper.unloadWorld(we.getName(), true);
                }
                catch(WorldNotLoadedException e){
                    Logger.getLogger(WorldManager.class.getName()).log(Level.WARNING, e.getMessage());
                }
            }
        }
    }

    private void createWorld(WorldEntry we) {
        WorldType type = WorldType.NORMAL;
        World.Environment worldEnvironment = getEnvironment(we.getName());

        if(we.getName().equals("lobby")){
            WorldHelper.createWorld(we.getName(), type, worldEnvironment, false, 6203818585396731238L);
            setStandardGameRules(we.getName());
            setLobbySpawnLocation(we.getName());
        }
        else{
            WorldHelper.createWorld(we.getName(), type, worldEnvironment, false);
        }
    }

    private World.Environment getEnvironment(String name){
        if(name.endsWith("_nether")){
            return World.Environment.NETHER;
        }
        
        if(name.endsWith("_the_end")){
            return World.Environment.THE_END;
        }
        
        if(name.endsWith("_custom")){
            return World.Environment.CUSTOM;
        }

        return World.Environment.NORMAL;
    }

    private void setLobbySpawnLocation(String name){
        World world = Bukkit.getWorld(name);
        int random = r.nextInt(9+1 -1) +1;
        switch(random){
            case 1:
            world.setSpawnLocation(140, 143, 188);
            break;
            case 2:
            world.setSpawnLocation(-226, 115, -5777);
            break;
            case 3:
            world.setSpawnLocation(718, 136, -4215);
            break;
            case 4:
            world.setSpawnLocation(497, 68, -2800);
            break;
            default:
            world.setSpawnLocation(140, 143, 188);
            break;
        }
    }

    private void setStandardGameRules(String name){
        World lobbyWorld = Bukkit.getWorld(name);
        lobbyWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
        lobbyWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        lobbyWorld.setGameRule(GameRule.MOB_GRIEFING, false);
        lobbyWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
    }
}