package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.Keyed;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerServiceTest {

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  private ServerService serverService;

  @BeforeEach
  void setUp() {
    when(plugin.getServer()).thenReturn(server);
    serverService = new ServerService(plugin);
  }

  @Test
  void getConsoleSenderReturnsConsoleSender() {
    ConsoleCommandSender consoleSender = mock(ConsoleCommandSender.class);
    when(server.getConsoleSender()).thenReturn(consoleSender);

    ConsoleCommandSender result = serverService.getConsoleSender();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(consoleSender, result)
    );
  }

  @Test
  void getOnlinePlayersReturnsOnlinePlayers() {
    Player player = mock(Player.class);
    Collection<Player> players = List.of(player);
    doReturn(players).when(server).getOnlinePlayers();

    Collection<? extends Player> result = serverService.getOnlinePlayers();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(players, result)
    );
  }

  @Test
  void getMaxPlayersReturnsMaxPlayers() {
    when(server.getMaxPlayers()).thenReturn(20);

    int result = serverService.getMaxPlayers();

    assertEquals(20, result);
  }

  @Test
  void setMaxPlayersDelegatesToServer() {
    serverService.setMaxPlayers(30);

    verify(server).setMaxPlayers(30);
  }

  @Test
  void getMotdReturnsMotd() {
    when(server.getMotd()).thenReturn("A Minecraft Server");

    String result = serverService.getMotd();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("A Minecraft Server", result)
    );
  }

  @Test
  void setMotdDelegatesToServer() {
    serverService.setMotd("New MOTD");

    verify(server).setMotd("New MOTD");
  }

  @Test
  void getPortReturnsPort() {
    when(server.getPort()).thenReturn(25565);

    int result = serverService.getPort();

    assertEquals(25565, result);
  }

  @Test
  void getPlayerByUuidReturnsPlayer() {
    UUID uuid = UUID.randomUUID();
    Player player = mock(Player.class);
    when(server.getPlayer(uuid)).thenReturn(player);

    Player result = serverService.getPlayer(uuid);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(player, result)
    );
  }

  @Test
  void getPlayerByNameReturnsPlayer() {
    Player player = mock(Player.class);
    when(server.getPlayer("TestPlayer")).thenReturn(player);

    Player result = serverService.getPlayer("TestPlayer");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(player, result)
    );
  }

  @Test
  void getOfflinePlayerByUuidReturnsOfflinePlayer() {
    UUID uuid = UUID.randomUUID();
    OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
    when(server.getOfflinePlayer(uuid)).thenReturn(offlinePlayer);

    OfflinePlayer result = serverService.getOfflinePlayer(uuid);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(offlinePlayer, result)
    );
  }

  @SuppressWarnings("deprecation")
  @Test
  void getOfflinePlayerByNameReturnsOfflinePlayer() {
    OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
    when(server.getOfflinePlayer("TestPlayer")).thenReturn(offlinePlayer);

    OfflinePlayer result = serverService.getOfflinePlayer("TestPlayer");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(offlinePlayer, result)
    );
  }

  @Test
  void getOfflinePlayersReturnsAllOfflinePlayers() {
    OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
    OfflinePlayer[] offlinePlayers = new OfflinePlayer[]{offlinePlayer};
    when(server.getOfflinePlayers()).thenReturn(offlinePlayers);

    OfflinePlayer[] result = serverService.getOfflinePlayers();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(offlinePlayers, result)
    );
  }

  @Test
  void getRegistryDelegatesToServer() {
    serverService.getRegistry(Keyed.class);

    verify(server).getRegistry(Keyed.class);
  }

  @Test
  void broadcastMessageReturnsBroadcastCount() {
    when(server.broadcastMessage("Hello")).thenReturn(5);

    int result = serverService.broadcastMessage("Hello");

    assertEquals(5, result);
  }

  @Test
  void createBlockDataReturnsBlockData() {
    BlockData blockData = mock(BlockData.class);
    when(server.createBlockData("minecraft:stone")).thenReturn(blockData);

    BlockData result = serverService.createBlockData("minecraft:stone");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(blockData, result)
    );
  }

  @Test
  void createInventoryReturnsInventory() {
    InventoryHolder holder = mock(InventoryHolder.class);
    Inventory inventory = mock(Inventory.class);
    when(server.createInventory(holder, 27, "Title")).thenReturn(inventory);

    Inventory result = serverService.createInventory(holder, 27, "Title");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(inventory, result)
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createInventoryWithTypeDelegatesToServer() {
    InventoryHolder holder = mock(InventoryHolder.class);

    serverService.createInventory(holder, null);

    verify(server).createInventory(eq(holder), eq(null));
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void createInventoryWithTypeAndTitleDelegatesToServer() {
    InventoryHolder holder = mock(InventoryHolder.class);

    serverService.createInventory(holder, null, "Title");

    verify(server).createInventory(eq(holder), eq(null), eq("Title"));
  }

  @Test
  void createInventoryWithSizeDelegatesToServer() {
    InventoryHolder holder = mock(InventoryHolder.class);

    serverService.createInventory(holder, 27);

    verify(server).createInventory(holder, 27);
  }

  @Test
  void createInventoryWithSizeReturnsInventory() {
    InventoryHolder holder = mock(InventoryHolder.class);
    Inventory inventory = mock(Inventory.class);
    when(server.createInventory(holder, 27)).thenReturn(inventory);

    Inventory result = serverService.createInventory(holder, 27);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(inventory, result)
    );
  }

  @Test
  void createPlayerProfileReturnsPlayerProfile() {
    UUID uuid = UUID.randomUUID();
    PlayerProfile playerProfile = mock(PlayerProfile.class);
    when(server.createPlayerProfile(uuid, "TestPlayer")).thenReturn(playerProfile);

    PlayerProfile result = serverService.createPlayerProfile(uuid, "TestPlayer");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(playerProfile, result)
    );
  }

  @Test
  void getWorldByNameReturnsWorld() {
    World world = mock(World.class);
    when(server.getWorld("world")).thenReturn(world);

    World result = serverService.getWorld("world");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(world, result)
    );
  }

  @Test
  void getWorldByUuidReturnsWorld() {
    UUID uuid = UUID.randomUUID();
    World world = mock(World.class);
    when(server.getWorld(uuid)).thenReturn(world);

    World result = serverService.getWorld(uuid);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(world, result)
    );
  }

  @Test
  void createWorldReturnsCreatedWorld() {
    WorldCreator creator = mock(WorldCreator.class);
    World world = mock(World.class);
    when(server.createWorld(creator)).thenReturn(world);

    World result = serverService.createWorld(creator);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(world, result)
    );
  }

  @Test
  void unloadWorldReturnsTrueOnSuccess() {
    World world = mock(World.class);
    when(server.unloadWorld(world, true)).thenReturn(true);

    boolean result = serverService.unloadWorld(world, true);

    assertTrue(result);
  }

  @Test
  void unloadWorldDelegatesToServer() {
    World world = mock(World.class);
    when(server.unloadWorld(world, false)).thenReturn(false);

    serverService.unloadWorld(world, false);

    verify(server).unloadWorld(world, false);
  }


  @Test
  void getWorldsReturnsAllWorlds() {
    World world = mock(World.class);
    List<World> worlds = List.of(world);
    when(server.getWorlds()).thenReturn(worlds);

    List<World> result = serverService.getWorlds();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(worlds, result)
    );
  }

}