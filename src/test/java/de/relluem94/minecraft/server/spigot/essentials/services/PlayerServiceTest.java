package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PlayerRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerRegistry playerRegistry;

  @Mock
  private PlayerRepository playerRepository;

  @Mock
  private TranslationService translationService;

  @Mock
  private ChatService chatService;

  @Mock
  private GroupService groupService;

  @Mock
  private WorldGroupService worldGroupService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  private PlayerService playerService;

  @BeforeEach
  void setUp() {
    playerService = new PlayerService(serviceContext, playerRegistry, playerRepository);
  }

  private void setupServerChain() {
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
  }

  @Test
  void initializeLoadsPlayerEntriesAndAppliesGroupPrefixes() {
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    GroupEntry groupEntry = mock(GroupEntry.class);
    Player onlinePlayer = mock(Player.class);

    when(playerEntry.getUuid()).thenReturn(uuid.toString());
    when(playerEntry.getGroup()).thenReturn(groupEntry);
    when(playerEntry.getCustomName()).thenReturn(null);
    when(groupEntry.getPrefix()).thenReturn("§a[VIP] ");
    when(onlinePlayer.getUniqueId()).thenReturn(uuid);
    when(onlinePlayer.getName()).thenReturn("TestPlayer");
    when(playerRepository.findAll()).thenReturn(List.of(playerEntry));
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);

    Collection<Player> onlinePlayers = List.of(onlinePlayer);

    setupServerChain();
    doReturn(onlinePlayers).when(server).getOnlinePlayers();


    playerService.initialize();

    verify(playerRegistry).putPlayerEntry(uuid, playerEntry);
    verify(onlinePlayer).setCustomName("§a[VIP] TestPlayer");
    verify(onlinePlayer).setPlayerListName(onlinePlayer.getCustomName());
  }

  @Test
  void initializeThrowsIllegalStateExceptionWhenCalledTwice() {
    when(playerRepository.findAll()).thenReturn(List.of());

    setupServerChain();
    when(server.getOnlinePlayers()).thenReturn(List.of());

    playerService.initialize();

    assertThrows(IllegalStateException.class, () -> playerService.initialize());
  }

  @Test
  void getPlayerByNameReturnsCorrectEntry() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.getName()).thenReturn("TestPlayer");

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.randomUUID(), playerEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);

    PlayerEntry result = playerService.getPlayerByName("TestPlayer");

    assertNotNull(result);
    assertEquals(playerEntry, result);
  }

  @Test
  void getPlayerByNameReturnsNullWhenNotFound() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.getName()).thenReturn("OtherPlayer");

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.randomUUID(), playerEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);

    PlayerEntry result = playerService.getPlayerByName("TestPlayer");

    assertNull(result);
  }

  @Test
  void getPlayerByUuidReturnsCorrectEntry() {
    String uuid = UUID.randomUUID().toString();
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.getUuid()).thenReturn(uuid);

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.fromString(uuid), playerEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);

    PlayerEntry result = playerService.getPlayerByUuid(uuid);

    assertNotNull(result);
    assertEquals(playerEntry, result);
  }

  @Test
  void getPlayerByUuidReturnsNullWhenNotFound() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.getUuid()).thenReturn(UUID.randomUUID().toString());

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.randomUUID(), playerEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);

    PlayerEntry result = playerService.getPlayerByUuid(UUID.randomUUID().toString());

    assertNull(result);
  }

  @Test
  void getCustomNameReturnsCustomNameWhenSet() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getCustomName()).thenReturn("CustomName");

    String result = playerService.getCustomName(player);

    assertEquals("CustomName", result);
  }

  @Test
  void getCustomNameReturnsMinecraftNameWhenCustomNameIsNull() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getName()).thenReturn("MinecraftName");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getCustomName()).thenReturn(null);

    String result = playerService.getCustomName(player);

    assertEquals("MinecraftName", result);
  }

  @Test
  void getCustomNameReturnsMinecraftNameWhenCustomNameIsNullString() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getName()).thenReturn("MinecraftName");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getCustomName()).thenReturn("null");

    String result = playerService.getCustomName(player);

    assertEquals("MinecraftName", result);
  }

  @Test
  void setGroupAppliesPrefixToDisplayAndTabName() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    GroupEntry groupEntry = mock(GroupEntry.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getName()).thenReturn("TestPlayer");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getCustomName()).thenReturn(null);
    when(groupEntry.getPrefix()).thenReturn("§b[MOD] ");

    playerService.setGroup(player, groupEntry);

    assertAll(
        () -> verify(player).setCustomName("§b[MOD] TestPlayer"),
        () -> verify(player).setPlayerListName(player.getCustomName())
    );
  }

  @Test
  void updateGroupUpdatesOnlinePlayerNameAndPersistsEntry() {
    UUID uuid = UUID.randomUUID();
    org.bukkit.OfflinePlayer offlinePlayer = mock(org.bukkit.OfflinePlayer.class);
    Player onlinePlayer = mock(Player.class);
    GroupEntry groupEntry = mock(GroupEntry.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(offlinePlayer.getUniqueId()).thenReturn(uuid);
    when(offlinePlayer.isOnline()).thenReturn(true);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getCustomName()).thenReturn(null);
    when(onlinePlayer.getUniqueId()).thenReturn(uuid);
    when(onlinePlayer.getName()).thenReturn("TestPlayer");
    when(groupEntry.getPrefix()).thenReturn("§c[ADMIN] ");
    when(playerEntry.getId()).thenReturn(1);

    setupServerChain();
    when(server.getPlayer(uuid)).thenReturn(onlinePlayer);
    when(playerRegistry.getPlayerEntry(onlinePlayer.getUniqueId())).thenReturn(playerEntry);

    playerService.updateGroup(offlinePlayer, groupEntry);

    assertAll(
        () -> verify(playerEntry).setGroup(groupEntry),
        () -> verify(playerEntry).setUpdatedBy(1),
        () -> verify(playerEntry).setHasToBeUpdated(true)
    );
  }

  @Test
  void updateGroupSkipsOnlinePlayerUpdateWhenOffline() {
    UUID uuid = UUID.randomUUID();
    org.bukkit.OfflinePlayer offlinePlayer = mock(org.bukkit.OfflinePlayer.class);
    GroupEntry groupEntry = mock(GroupEntry.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(offlinePlayer.getUniqueId()).thenReturn(uuid);
    when(offlinePlayer.isOnline()).thenReturn(false);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);

    playerService.updateGroup(offlinePlayer, groupEntry);

    assertAll(
        () -> verify(playerEntry).setGroup(groupEntry),
        () -> verify(playerEntry).setHasToBeUpdated(true)
    );
  }

  @Test
  void getPlayerEntryByPlayerDelegatesToRegistry() {
    Player player = mock(Player.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);

    PlayerEntry result = playerService.getPlayerEntry(player);

    assertEquals(playerEntry, result);
  }

  @Test
  void getPlayerEntryByUuidDelegatesToRegistry() {
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);

    PlayerEntry result = playerService.getPlayerEntry(uuid);

    assertEquals(playerEntry, result);
  }

  @Test
  void getPlayerEntryByInternalIdReturnsMatchingEntry() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.getId()).thenReturn(42);

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.randomUUID(), playerEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);

    PlayerEntry result = playerService.getPlayerEntryByInternalId(42);

    assertEquals(playerEntry, result);
  }

  @Test
  void getPlayerEntryByInternalIdReturnsNullWhenNotFound() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.getId()).thenReturn(10);

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.randomUUID(), playerEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);

    PlayerEntry result = playerService.getPlayerEntryByInternalId(99);

    assertNull(result);
  }

  @Test
  void setAfkTogglesToAfkAndBroadcastsWhenNotJoin() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getLocale()).thenReturn("en_US");
    when(player.getCustomName()).thenReturn("§a[VIP] TestPlayer");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isAfk()).thenReturn(false);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(any(), anyString(), anyString(), anyString())).thenReturn("AFK message");

    setupServerChain();
    when(server.broadcastMessage("AFK message")).thenReturn(0);

    playerService.setAfk(player, false);

    verify(server).broadcastMessage("AFK message");

    assertAll(
        () -> verify(playerEntry).setAfk(true),
        () -> verify(playerEntry).setHasToBeUpdated(true),
        () -> verify(player).setInvulnerable(true)
    );
  }

  @Test
  void setAfkDoesNotPersistOnJoin() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isAfk()).thenReturn(false);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);

    playerService.setAfk(player, true);

    assertAll(
        () -> verify(playerEntry, never()).setAfk(any(Boolean.class)),
        () -> verify(playerEntry, never()).setHasToBeUpdated(any(Boolean.class))
    );
  }

  @Test
  void getPartnerReturnsCachedPartnerWhenPresent() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    PlayerPartnerEntry partnerEntry = mock(PlayerPartnerEntry.class);

    when(playerEntry.getPartner()).thenReturn(partnerEntry);

    PlayerPartnerEntry result = playerService.getPartner(playerEntry);

    assertEquals(partnerEntry, result);
    verify(playerRepository, never()).findPartnerByPlayerId(any(Integer.class));
  }

  @Test
  void getPartnerFetchesFromRepositoryWhenNotCached() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    PlayerPartnerEntry partnerEntry = mock(PlayerPartnerEntry.class);

    when(playerEntry.getPartner()).thenReturn(null);
    when(playerEntry.getId()).thenReturn(5);
    when(playerRepository.findPartnerByPlayerId(5)).thenReturn(partnerEntry);

    PlayerPartnerEntry result = playerService.getPartner(playerEntry);

    assertEquals(partnerEntry, result);
  }

  @Test
  void setFlyingEnablesFlightWhenAuthorizedAndFlyingFlagSet() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isFlying()).thenReturn(true);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);

    playerService.setFlying(player);

    assertAll(
        () -> verify(player).setAllowFlight(true),
        () -> verify(player).setFlying(true)
    );
  }

  @Test
  void setFlyingDoesNothingWhenNotAuthorized() {
    Player player = mock(Player.class);

    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);

    playerService.setFlying(player);

    verify(player, never()).setAllowFlight(any(Boolean.class));
    verify(player, never()).setFlying(any(Boolean.class));
  }

  @Test
  void savePlayersIteratesAllEntriesAndSendsMessageWhenUpdated() {
    GroupEntry adminGroup = mock(GroupEntry.class);
    PlayerEntry updatedEntry = mock(PlayerEntry.class);
    PlayerEntry unchangedEntry = mock(PlayerEntry.class);

    when(updatedEntry.isHasToBeUpdated()).thenReturn(true);
    when(unchangedEntry.isHasToBeUpdated()).thenReturn(false);

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.randomUUID(), updatedEntry);
    map.put(UUID.randomUUID(), unchangedEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("Players saved");
    when(serviceContext.getChatService()).thenReturn(chatService);

    playerService.savePlayers(adminGroup);

    assertAll(
        () -> verify(playerRepository).update(updatedEntry),
        () -> verify(chatService).sendMessageInChannel(
            "Players saved",
            de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE,
            BetterChatFormat.ADMIN_CHANNEL,
            adminGroup
        )
    );
  }

  @Test
  void savePlayersDoesNotSendMessageWhenNothingUpdated() {
    GroupEntry adminGroup = mock(GroupEntry.class);
    PlayerEntry unchangedEntry = mock(PlayerEntry.class);
    when(unchangedEntry.isHasToBeUpdated()).thenReturn(false);

    Map<UUID, PlayerEntry> map = new HashMap<>();
    map.put(UUID.randomUUID(), unchangedEntry);
    when(playerRegistry.getPlayerEntryMap()).thenReturn(map);

    playerService.savePlayers(adminGroup);

    verify(chatService, never()).sendMessageInChannel(anyString(), anyString(), any(), any());
  }

  @Test
  void savePlayersInvSavesOnlinePlayersAndSendsMessage() {
    GroupEntry adminGroup = mock(GroupEntry.class);
    Player player = mock(Player.class);

    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.saveWorldGroupInventoryForPlayer(player, false)).thenReturn(true);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("Inventories saved");
    when(serviceContext.getChatService()).thenReturn(chatService);

    setupServerChain();
    doReturn(List.of(player)).when(server).getOnlinePlayers();


    playerService.savePlayersInv(adminGroup);

    verify(chatService).sendMessageInChannel(
        "Inventories saved",
        de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CHAT_CONSOLE,
        BetterChatFormat.ADMIN_CHANNEL,
        adminGroup
    );
  }

  @Test
  void savePlayerByPlayerDelegatesToEntryMethod() {
    Player player = mock(Player.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.isHasToBeUpdated()).thenReturn(true);

    playerService.savePlayer(player);

    verify(playerRepository).update(playerEntry);
  }

  @Test
  void savePlayerByEntryUpdatesAndResetsFlagWhenPending() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.isHasToBeUpdated()).thenReturn(true);

    int result = playerService.savePlayer(playerEntry);

    assertAll(
        () -> assertEquals(1, result),
        () -> verify(playerRepository).update(playerEntry),
        () -> verify(playerEntry).setHasToBeUpdated(false)
    );
  }

  @Test
  void savePlayerByEntrySkipsWhenNoUpdateNeeded() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerEntry.isHasToBeUpdated()).thenReturn(false);

    int result = playerService.savePlayer(playerEntry);

    assertAll(
        () -> assertEquals(0, result),
        () -> verify(playerRepository, never()).update(any())
    );
  }

  @Test
  void putPlayerEntryDelegatesToRegistry() {
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    playerService.putPlayerEntry(uuid, playerEntry);

    verify(playerRegistry).putPlayerEntry(uuid, playerEntry);
  }

  @Test
  void getAllPlayerEntriesDelegatesToRegistry() {
    List<PlayerEntry> entries = List.of(mock(PlayerEntry.class));
    when(playerRegistry.getAllPlayerEntries()).thenReturn(entries);

    List<PlayerEntry> result = playerService.getAllPlayerEntries();

    assertEquals(entries, result);
  }

  @Test
  void clearPlayerEntriesDelegatesToRegistry() {
    playerService.clearPlayerEntries();

    verify(playerRegistry).clearPlayerEntries();
  }

  @Test
  void savePartnerDelegatesToRepository() {
    PlayerPartnerEntry partnerEntry = mock(PlayerPartnerEntry.class);

    playerService.savePartner(partnerEntry);

    verify(playerRepository).savePartner(partnerEntry);
  }

  @Test
  void deletePartnerDelegatesToRepository() {
    PlayerPartnerEntry partnerEntry = mock(PlayerPartnerEntry.class);

    playerService.deletePartner(partnerEntry);

    verify(playerRepository).deletePartner(partnerEntry);
  }

  @Test
  void updatePartnerDelegatesToRepository() {
    PlayerPartnerEntry partnerEntry = mock(PlayerPartnerEntry.class);

    playerService.updatePartner(partnerEntry);

    verify(playerRepository).updatePartner(partnerEntry);
  }

  @Test
  void findByUuidDelegatesToRepository() {
    String uuid = UUID.randomUUID().toString();
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    when(playerRepository.findByUuid(uuid)).thenReturn(playerEntry);

    PlayerEntry result = playerService.findByUuid(uuid);

    assertEquals(playerEntry, result);
  }

  @Test
  void registerNewPlayerSavesToRepositoryAndRegistry() {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    UUID uuid = UUID.randomUUID();
    when(playerEntry.getUuid()).thenReturn(uuid.toString());

    playerService.registerNewPlayer(playerEntry);

    assertAll(
        () -> verify(playerRepository).save(playerEntry),
        () -> verify(playerRegistry).putPlayerEntry(uuid, playerEntry)
    );
  }

  @Test
  void getHomeAndDeathLocationNamesReturnsAllLocationNames() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    LocationEntry homeEntry = mock(LocationEntry.class);
    LocationEntry deathEntry = mock(LocationEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(homeEntry.getLocationName()).thenReturn("home1");
    when(deathEntry.getLocationName()).thenReturn("death1");
    when(playerEntry.getHomes()).thenReturn(List.of(homeEntry));
    when(playerEntry.getDeaths()).thenReturn(List.of(deathEntry));

    List<String> result = playerService.getHomeAndDeathLocationNames(player);

    assertAll(
        () -> assertEquals(2, result.size()),
        () -> assertEquals("home1", result.getFirst()),
        () -> assertEquals("death1", result.get(1))
    );
  }

  @Test
  void getHomeAndDeathLocationNamesReturnsEmptyListWhenNoLocations() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getHomes()).thenReturn(new ArrayList<>());
    when(playerEntry.getDeaths()).thenReturn(new ArrayList<>());

    List<String> result = playerService.getHomeAndDeathLocationNames(player);

    assertEquals(0, result.size());
  }

  @Test
  void setAfkWithFakeAfkActiveStateForcesAfkTrueAndBroadcastsActivation() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getLocale()).thenReturn("en_US");
    when(player.getCustomName()).thenReturn("§a[VIP] TestPlayer");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isAfk()).thenReturn(false);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.FAKE_AFK_ACTIVE);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(
        eq(MessageKey.COMMAND_AFK_DEACTIVATED), anyString(), anyString(), eq("§a"))
    ).thenReturn("AFK deactivated message");

    setupServerChain();
    when(server.broadcastMessage("AFK deactivated message")).thenReturn(0);

    playerService.setAfk(player, false);

    verify(server).broadcastMessage("AFK deactivated message");

    verify(playerEntry, never()).setAfk(any(Boolean.class));
    verify(playerEntry, never()).setHasToBeUpdated(any(Boolean.class));
  }

  @Test
  void setAfkWithFakeAfkOnStateForcesAfkFalseAndBroadcastsActivation() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getLocale()).thenReturn("en_US");
    when(player.getCustomName()).thenReturn("§a[VIP] TestPlayer");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isAfk()).thenReturn(true);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.FAKE_AFK_ON);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(
        eq(MessageKey.COMMAND_AFK_ACTIVATED), anyString(), anyString(), eq("§c"))
    ).thenReturn("AFK activated message");

    setupServerChain();
    when(server.broadcastMessage("AFK activated message")).thenReturn(0);

    playerService.setAfk(player, false);

    verify(server).broadcastMessage("AFK activated message");

    verify(playerEntry, never()).setAfk(any(Boolean.class));
    verify(playerEntry, never()).setHasToBeUpdated(any(Boolean.class));
  }

  @Test
  void setAfkDeactivatesAfkAndBroadcastsDeactivationWhenAlreadyAfk() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getLocale()).thenReturn("en_US");
    when(player.getCustomName()).thenReturn("§a[VIP] TestPlayer");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isAfk()).thenReturn(true);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.DEFAULT);
    when(playerEntry.getId()).thenReturn(1);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(
        eq(MessageKey.COMMAND_AFK_DEACTIVATED), anyString(), anyString(), eq("§a"))
    ).thenReturn("AFK deactivated message");

    setupServerChain();
    when(server.broadcastMessage("AFK deactivated message")).thenReturn(0);

    playerService.setAfk(player, false);

    verify(server).broadcastMessage("AFK deactivated message");

    assertAll(
        () -> verify(playerEntry).setAfk(false),
        () -> verify(playerEntry).setHasToBeUpdated(true),
        () -> verify(player).setInvulnerable(false)
    );
  }

  @Test
  void setAfkSkipsPersistenceWhenPlayerStateIsNotDefault() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(player.getLocale()).thenReturn("en_US");
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isAfk()).thenReturn(false);
    when(playerEntry.getPlayerState()).thenReturn(PlayerState.SIGN_EDIT);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(any(), anyString(), anyString(), anyString())).thenReturn("AFK message");

    setupServerChain();
    when(server.broadcastMessage("AFK message")).thenReturn(0);

    playerService.setAfk(player, false);

    assertAll(
        () -> verify(playerEntry, never()).setAfk(any(Boolean.class)),
        () -> verify(playerEntry, never()).setHasToBeUpdated(any(Boolean.class)),
        () -> verify(player, never()).setInvulnerable(any(Boolean.class))
    );
  }

  @Test
  void setFlyingDoesNothingWhenAuthorizedButFlyingFlagNotSet() {
    Player player = mock(Player.class);
    UUID uuid = UUID.randomUUID();
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(player.getUniqueId()).thenReturn(uuid);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.isFlying()).thenReturn(false);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);

    playerService.setFlying(player);

    verify(player, never()).setAllowFlight(any(Boolean.class));
    verify(player, never()).setFlying(any(Boolean.class));
  }

  @Test
  void savePlayersInvDoesNotSendMessageWhenNoInventoriesUpdated() {
    GroupEntry adminGroup = mock(GroupEntry.class);
    Player player = mock(Player.class);

    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.saveWorldGroupInventoryForPlayer(player, false)).thenReturn(false);

    setupServerChain();
    doReturn(List.of(player)).when(server).getOnlinePlayers();


    playerService.savePlayersInv(adminGroup);

    verify(chatService, never()).sendMessageInChannel(anyString(), anyString(), any(), any());
  }

  @Test
  void updateGroupSkipsDisplayNameUpdateWhenOnlinePlayerIsNull() {
    UUID uuid = UUID.randomUUID();
    org.bukkit.OfflinePlayer offlinePlayer = mock(org.bukkit.OfflinePlayer.class);
    GroupEntry groupEntry = mock(GroupEntry.class);
    PlayerEntry playerEntry = mock(PlayerEntry.class);

    when(offlinePlayer.getUniqueId()).thenReturn(uuid);
    when(offlinePlayer.isOnline()).thenReturn(true);
    when(playerRegistry.getPlayerEntry(uuid)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);

    setupServerChain();
    when(server.getPlayer(uuid)).thenReturn(null);

    playerService.updateGroup(offlinePlayer, groupEntry);

    assertAll(
        () -> verify(playerEntry).setGroup(groupEntry),
        () -> verify(playerEntry).setUpdatedBy(1),
        () -> verify(playerEntry).setHasToBeUpdated(true)
    );
  }
}