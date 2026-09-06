package de.relluem94.minecraft.server.spigot.essentials.services.migration;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConfigMigrationServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private GroupService groupService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  @Mock
  private World world;

  private ConfigMigrationService configMigrationService;

  @BeforeEach
  void setUp() {
    URL resourceUrl = getClass().getClassLoader().getResource("test-players.yml");
    assertNotNull(resourceUrl, "Test resource 'test-players.yml' not found in classpath");
    File testResourceFolder = new File(resourceUrl.getPath()).getParentFile();
    configMigrationService = new ConfigMigrationService(testResourceFolder, serviceContext);
  }


  @Test
  void getPlayersReturnsCorrectPlayerEntry() {
    GroupEntry adminGroup = new GroupEntry();
    adminGroup.setName("admin");

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveGroupWithFallback("admin")).thenReturn(adminGroup);

    try (MockedStatic<ChatHelper> chatHelper = mockStatic(ChatHelper.class)) {
      chatHelper.when(() -> consoleSendMessage(anyString(), anyString())).thenAnswer(_ -> null);

      List<PlayerEntry> players = configMigrationService.getPlayers("test-players");

      assertEquals(1, players.size());

      PlayerEntry player = players.getFirst();
      assertEquals("550e8400-e29b-41d4-a716-446655440000", player.getUuid());
      assertEquals("TestPlayer", player.getCustomName());
      assertTrue(player.isFlying());
      assertFalse(player.isAfk());
      assertEquals("admin", player.getGroup().getName());
      assertEquals(1, player.getCreatedBy());
    }
  }

  @Test
  void getHomesReturnsCorrectLocationEntries() {
    PlayerEntry player = new PlayerEntry();
    player.setUuid("550e8400-e29b-41d4-a716-446655440000");
    player.setId(1);

    try (MockedStatic<ChatHelper> chatHelper = mockStatic(ChatHelper.class);
        MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      chatHelper.when(() -> consoleSendMessage(anyString(), anyString())).thenAnswer(_ -> null);
      when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
      when(pluginMetadataService.getPlugin()).thenReturn(plugin);
      when(plugin.getServer()).thenReturn(server);
      when(server.getWorld("world")).thenReturn(world);
      bukkit.when(() -> Bukkit.getWorld("world")).thenReturn(world);

      List<LocationEntry> homes = configMigrationService.getHomes("test-homes", player);

      assertEquals(2, homes.size());

      LocationEntry spawnHome = homes.stream()
          .filter(h -> h.getLocationName().equals("spawn"))
          .findFirst()
          .orElseThrow();

      assertEquals(1, spawnHome.getLocationType().getId());
      assertEquals(100.0, spawnHome.getLocation().getX());
      assertEquals(64.0, spawnHome.getLocation().getY());
      assertEquals(100.0, spawnHome.getLocation().getZ());

      LocationEntry deathHome = homes.stream()
          .filter(h -> h.getLocationName().equals("death"))
          .findFirst()
          .orElseThrow();

      assertEquals(2, deathHome.getLocationType().getId());
      assertEquals(200.0, deathHome.getLocation().getX());
    }
  }

  @Test
  void getHomesSkipsNullConfigurationSections() {
    PlayerEntry player = new PlayerEntry();
    player.setUuid("550e8400-e29b-41d4-a716-446655440000");
    player.setId(1);

    try (MockedStatic<ChatHelper> chatHelper = mockStatic(ChatHelper.class);
        MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      chatHelper.when(() -> consoleSendMessage(anyString(), anyString())).thenAnswer(_ -> null);
      when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
      when(pluginMetadataService.getPlugin()).thenReturn(plugin);
      when(plugin.getServer()).thenReturn(server);
      when(server.getWorld("world")).thenReturn(world);
      bukkit.when(() -> Bukkit.getWorld("world")).thenReturn(world);

      List<LocationEntry> homes = configMigrationService.getHomes("test-homes", player);

      assertEquals(2, homes.size());
    }
  }

  @Test
  void getHomesReturnsEmptyListWhenNoHomesExist() {
    PlayerEntry player = new PlayerEntry();
    player.setUuid("550e8400-e29b-41d4-a716-446655440002");
    player.setId(1);

    try (MockedStatic<ChatHelper> chatHelper = mockStatic(ChatHelper.class)) {
      chatHelper.when(() -> consoleSendMessage(anyString(), anyString())).thenAnswer(_ -> null);

      List<LocationEntry> homes = configMigrationService.getHomes("test-homes", player);

      assertEquals(0, homes.size());
    }
  }

  @Test
  void legacyConfigExistsReturnsTrueForExistingFile() {
    assertTrue(configMigrationService.legacyConfigExists("test-players"));
  }

  @Test
  void legacyConfigExistsReturnsFalseForNonExistingFile() {
    assertFalse(configMigrationService.legacyConfigExists("non-existent-file"));
  }
}