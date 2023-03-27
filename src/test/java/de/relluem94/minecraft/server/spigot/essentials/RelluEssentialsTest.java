package de.relluem94.minecraft.server.spigot.essentials;


import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;

public class RelluEssentialsTest {

    private ServerMock server;
    private RelluEssentials plugin;
    private DB db;

    
    @Before
    public void setUp() throws ManagedProcessException {

        PrintStream sysOut = System.out;

        PrintStream noOut = new PrintStream(new OutputStream(){
            public void write(int b) {}
        });

        System.setOut(noOut); 

        db = DB.newEmbeddedDB(DatabaseHelper.DB_TEST_PORT);
        db.start();
        System.setOut(sysOut);

        db.createDB(DatabaseConstants.PLUGIN_DATABASE_NAME);
        
        

        db.source("de/relluem94/minecraft/server/spigot/essentials/rellu_essentials.sql");
        server = MockBukkit.mock();
        plugin = MockBukkit.load(RelluEssentials.class);
    }

    @After
    public void tearDown() throws ManagedProcessException{
        MockBukkit.unmock();
        db.stop();
    }


    @Test
    @DisplayName("Test Plugin 1")
    public void test(){
        System.out.println("HAAAALLLOO 1");
        Assert.assertTrue(true);

    }


    
    @Test
    @DisplayName("Test Plugin 2")
    public void test2(){
        System.out.println("HAAAALLLOO 2");
        server.addSimpleWorld(Strings.PLUGIN_WORLD_LOBBY);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_NETHER);
        server.addSimpleWorld(Strings.PLUGIN_WORLD_WORLD_THE_END);

        PlayerMock player = server.addPlayer();
        PlayerMock player2 = server.addPlayer();

        player.assertGameMode(GameMode.SURVIVAL);
        player2.assertGameMode(GameMode.CREATIVE);

        player.simulateBlockBreak(server.getWorld(Strings.PLUGIN_WORLD_LOBBY).getBlockAt(1, 1, 1));
        player2.simulateBlockBreak(server.getWorld(Strings.PLUGIN_WORLD_LOBBY).getBlockAt(1, 1, 2));

        server.execute("poke", player, player2.getName());

        Assertions.assertTrue(server.getOnlinePlayers().isEmpty());
        Assert.assertTrue(server.getOnlinePlayers().isEmpty());
    }
}