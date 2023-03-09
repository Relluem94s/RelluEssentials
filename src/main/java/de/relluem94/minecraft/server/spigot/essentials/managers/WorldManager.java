package de.relluem94.minecraft.server.spigot.essentials.managers;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;

import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.worldsMap;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class WorldManager implements IEnable, IDisable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + "Worlds Size: " + worldsMap.size());

        System.out.println();
        for (WorldGroupEntry wge : worldsMap.keySet()) {
            for(WorldEntry we: worldsMap.get(wge)){
                if(WorldHelper.worldExists(we.getName())){
                    
                    for(World w: Bukkit.getWorlds()){
                        if(w.getName().equals(we.getName())){
                            continue;
                        }
                        else{
                            WorldHelper.loadWorld(we.getName());
                            World world = Bukkit.getWorld(we.getName());
                            world.setGameRule(GameRule.DO_FIRE_TICK, false);
                            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                            world.setGameRule(GameRule.MOB_GRIEFING, false);
                            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                        }
                    }                    
                }
                else{
                    
                    WorldType type = WorldType.NORMAL;
                    World.Environment world_environment = World.Environment.NORMAL;

                    if(we.getName().endsWith("_nether")){
                        world_environment = World.Environment.NETHER;
                    }
                    else if(we.getName().endsWith("_the_end")){
                        world_environment = World.Environment.THE_END;
                    }
                    else if(we.getName().endsWith("_custom")){
                        world_environment = World.Environment.CUSTOM;
                    }

                    if(we.getName().equals("lobby")){
                        WorldHelper.createWorld(we.getName(), type, world_environment, false, 6203818585396731238L);
                        World lobbyWorld = Bukkit.getWorld(we.getName());
                        lobbyWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
                        lobbyWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                        lobbyWorld.setGameRule(GameRule.MOB_GRIEFING, false);
                        lobbyWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                        Random r = new Random();
                        int random = r.nextInt(9+1 -1) +1;
                        switch(random){
                            case 1:
                                lobbyWorld.setSpawnLocation(140, 143, 188);
                                break;
                            case 2:
                                lobbyWorld.setSpawnLocation(-226, 115, -5777);
                                break;
                            case 3:
                                lobbyWorld.setSpawnLocation(718, 136, -4215);
                                break;
                            case 4:
                                lobbyWorld.setSpawnLocation(497, 68, -2800);
                                break;
                            default:
                                lobbyWorld.setSpawnLocation(140, 143, 188);
                                break;
                        }
                    }
                    else{
                        WorldHelper.createWorld(we.getName(), type, world_environment, false);
                    }
                }
            }
        }
    }

    @Override
    public void disable() {
        for (WorldGroupEntry wge : worldsMap.keySet()) {
            for(WorldEntry we: worldsMap.get(wge)){
                try{
                    WorldHelper.unloadWorld(we.getName(), true);
                }
                catch(WorldNotLoadedException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
}
