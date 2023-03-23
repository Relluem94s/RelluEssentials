package de.relluem94.minecraft.server.spigot.essentials;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

public class RelluEssentialsTest {

    private ServerMock server;
    private RelluEssentials plugin;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(RelluEssentials.class);
    }

    @After
    public void tearDown(){
        MockBukkit.unmock();
    }


    /*
    @Test
    public void test(){
        server.addSimpleWorld(Strings.PLUGIN_WORLD_LOBBY);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_NETHER);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_THE_END);

        server.getScheduler().performTicks(100L);

        PlayerEntry pe = getPlayerEntry();

        RelluEssentials.dBH.insertPlayer(pe);
        PlayerAPI.putPlayerEntry(UUID.fromString(pe.getUUID()), pe);

        
        


        PlayerMock player = server.addPlayer("Relluem94");
        player.sendMessage("Test");
        PlayerMock player2 = server.addPlayer("Stellachen");
        player2.sendMessage("Test2");

        server.setOfflinePlayers(4);
        for(OfflinePlayer op : server.getOfflinePlayers()){
            System.out.println(op.getName());
        }

        player.chat("test");


        Assert.assertTrue(server.getOnlinePlayers().isEmpty());
    }

    protected PlayerEntry getPlayerEntry(){
        PlayerEntry pe = new PlayerEntry();
        pe.setID(1);
        pe.setUUID(UUID.randomUUID().toString());
        pe.setName("Relluem94");
        pe.setCustomName("Rellu");
        pe.setGroup(Groups.getGroup("Admin"));
        
        return pe;
    }

     */
}
