package de.relluem94.minecraft.server.spigot.essentials.services.migration;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.io.File;
import java.net.URL;
import java.util.List;
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

  private ConfigMigrationService configMigrationService;
  private File testResourceFolder;

  @BeforeEach
  void setUp() {
    URL resourceUrl = getClass().getClassLoader().getResource("test-players.yml");
    testResourceFolder = new File(resourceUrl.getPath()).getParentFile();
    configMigrationService = new ConfigMigrationService(testResourceFolder, serviceContext);
  }

  @Test
  void getPlayersReturnsCorrectPlayerEntry() {
    GroupEntry adminGroup = new GroupEntry();
    adminGroup.setName("admin");

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.resolveGroupWithFallback("admin")).thenReturn(adminGroup);

    try (MockedStatic<ChatHelper> chatHelper = mockStatic(ChatHelper.class)) {
      chatHelper.when(() -> consoleSendMessage(anyString(), anyString())).thenAnswer(inv -> null);

      List<PlayerEntry> players = configMigrationService.getPlayers("test-players");

      assertEquals(1, players.size());

      PlayerEntry player = players.get(0);
      assertEquals("550e8400-e29b-41d4-a716-446655440000", player.getUuid());
      assertEquals("TestPlayer", player.getCustomName());
      assertTrue(player.isFlying());
      assertFalse(player.isAfk());
      assertEquals("admin", player.getGroup().getName());
      assertEquals(1, player.getCreatedBy());
    }
  }
}