package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorldHelperTest {

    @Mock
    private Player player;

    @Mock
    private World world;

    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeEach
    void setUp() {
        mockedBukkit = mockStatic(Bukkit.class);
    }

    @AfterEach
    void tearDown() {
        mockedBukkit.close();
    }

    @Nested
    class IsInWorldWithPlayerAndWorldName {

        @Test
        void returnsTrueWhenPlayerIsInGivenWorld() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("world");

            assertTrue(WorldHelper.isInWorld(player, "world"));
        }

        @Test
        void returnsTrueWhenPlayerIsInGivenWorldCaseInsensitive() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("World");

            assertTrue(WorldHelper.isInWorld(player, "world"));
        }

        @Test
        void returnsFalseWhenPlayerIsNotInGivenWorld() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("other_world");

            assertFalse(WorldHelper.isInWorld(player, "world"));
        }
    }

    @Nested
    class IsInWorldWithCommandSenderAndWorldName {

        @Mock
        private CommandSender nonPlayerSender;

        @Test
        void returnsTrueWhenSenderIsNotAPlayer() {
            assertTrue(WorldHelper.isInWorld(nonPlayerSender, "world"));
        }

        @Test
        void returnsTrueWhenSenderIsPlayerInGivenWorld() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("world");

            assertTrue(WorldHelper.isInWorld((CommandSender) player, "world"));
        }

        @Test
        void returnsFalseWhenSenderIsPlayerNotInGivenWorld() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("other_world");

            assertFalse(WorldHelper.isInWorld((CommandSender) player, "world"));
        }
    }

    @Nested
    class IsInWorldWithPlayerAndWorldList {

        @Test
        void returnsTrueWhenPlayerWorldIsInList() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("world");

            assertTrue(WorldHelper.isInWorld(player, List.of("world", "world_nether")));
        }

        @Test
        void returnsFalseWhenPlayerWorldIsNotInList() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("other_world");

            assertFalse(WorldHelper.isInWorld(player, List.of("world", "world_nether")));
        }

        @Test
        void returnsFalseWhenListIsEmpty() {
            when(player.getWorld()).thenReturn(world);
            when(world.getName()).thenReturn("world");

            assertFalse(WorldHelper.isInWorld(player, List.of()));
        }
    }

    @Nested
    class IsInWorldWithCommandSenderAndWorld {

        @Mock
        private CommandSender nonPlayerSender;

        @Test
        void returnsTrueWhenSenderIsNotAPlayer() {
            assertTrue(WorldHelper.isInWorld(nonPlayerSender, world));
        }

        @Test
        void returnsTrueWhenSenderIsPlayerInGivenWorld() {
            when(player.getWorld()).thenReturn(world);

            assertTrue(WorldHelper.isInWorld((CommandSender) player, world));
        }

        @Test
        void returnsFalseWhenSenderIsPlayerNotInGivenWorld() {
            World otherWorld = mock(World.class);
            when(player.getWorld()).thenReturn(otherWorld);

            assertFalse(WorldHelper.isInWorld((CommandSender) player, world));
        }
    }

    @Nested
    class IsInWorldWithBlock {

        @Mock
        private Block block;

        @Test
        void returnsTrueWhenBlockIsInGivenWorld() {
            when(block.getWorld()).thenReturn(world);

            assertTrue(WorldHelper.isInWorld(block, world));
        }

        @Test
        void returnsFalseWhenBlockIsNotInGivenWorld() {
            World otherWorld = mock(World.class);
            when(block.getWorld()).thenReturn(otherWorld);

            assertFalse(WorldHelper.isInWorld(block, world));
        }
    }

    @Nested
    class IsInWorldWithEntity {

        @Mock
        private Entity entity;

        @Test
        void returnsTrueWhenEntityIsInGivenWorld() {
            when(entity.getWorld()).thenReturn(world);

            assertTrue(WorldHelper.isInWorld(entity, world));
        }

        @Test
        void returnsFalseWhenEntityIsNotInGivenWorld() {
            World otherWorld = mock(World.class);
            when(entity.getWorld()).thenReturn(otherWorld);

            assertFalse(WorldHelper.isInWorld(entity, world));
        }
    }

    @Nested
    class UnloadWorld {

        @Test
        void unloadsWorldSuccessfullyWhenWorldIsLoaded() throws WorldNotLoadedException {
            mockedBukkit.when(() -> Bukkit.getWorld("world")).thenReturn(world);

            WorldHelper.unloadWorld("world", true);

            mockedBukkit.verify(() -> Bukkit.unloadWorld("world", true));
        }

        @Test
        void throwsWorldNotLoadedExceptionWhenWorldIsNotLoaded() {
            mockedBukkit.when(() -> Bukkit.getWorld("unknown_world")).thenReturn(null);

            assertThrows(WorldNotLoadedException.class, () -> WorldHelper.unloadWorld("unknown_world", true));
        }

        @Test
        void unloadsWorldWithoutSavingWhenSaveIsFalse() throws WorldNotLoadedException {
            mockedBukkit.when(() -> Bukkit.getWorld("world")).thenReturn(world);

            WorldHelper.unloadWorld("world", false);

            mockedBukkit.verify(() -> Bukkit.unloadWorld("world", false));
        }
    }

    @Nested
    class CloneWorld {

        @Test
        void throwsWorldNotFoundExceptionWhenOriginalWorldIsNotLoaded() {
            mockedBukkit.when(() -> Bukkit.getWorld("nonexistent_world")).thenReturn(null);

            assertThrows(WorldNotFoundException.class,
                    () -> WorldHelper.cloneWorld("cloned_world", "nonexistent_world"));
        }

        @Test
        void createsClonedWorldWhenOriginalWorldExists() throws WorldNotFoundException {
            mockedBukkit.when(() -> Bukkit.getWorld("original_world")).thenReturn(world);

            WorldHelper.cloneWorld("cloned_world", "original_world");

            mockedBukkit.verify(() -> Bukkit.createWorld(any()));
        }
    }
}