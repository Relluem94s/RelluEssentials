package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

  @Mock
  private GroupRegistry groupRegistry;

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private PlayerRegistry playerRegistry;

  @Mock
  private Player player;

  @Mock
  private CommandSender commandSender;

  @Mock
  private PlayerEntry playerEntry;

  private GroupService groupService;

  @BeforeEach
  void setUp() {
    groupService = new GroupService(groupRegistry, groupRepository);
    groupService.setPlayerRegistry(playerRegistry);
  }

  @Test
  void addGroupReturnsFalseWhenGroupAlreadyExists() {
    GroupEntry groupEntry = new GroupEntry(1, "admin", "§c");
    when(groupRegistry.containsByName("admin")).thenReturn(true);

    boolean result = groupService.addGroup(groupEntry);

    assertAll(
        () -> assertFalse(result),
        () -> verify(groupRepository, never()).save(any()),
        () -> verify(groupRegistry, never()).register(any())
    );
  }

  @Test
  void addGroupReturnsTrueAndPersistsWhenGroupDoesNotExist() {
    GroupEntry groupEntry = new GroupEntry(2, "mod", "§6");
    when(groupRegistry.containsByName("mod")).thenReturn(false);

    boolean result = groupService.addGroup(groupEntry);

    assertAll(
        () -> assertTrue(result),
        () -> verify(groupRepository).save(groupEntry),
        () -> verify(groupRegistry).register(groupEntry)
    );
  }

  @Test
  void resolveGroupWithFallbackByNameReturnsFoundGroup() {
    GroupEntry groupEntry = new GroupEntry(3, "vip", "§b");
    when(groupRegistry.findByName("vip")).thenReturn(Optional.of(groupEntry));

    GroupEntry result = groupService.resolveGroupWithFallback("vip");

    assertAll(
        () -> assertEquals(3, result.getId()),
        () -> assertEquals("vip", result.getName()),
        () -> assertEquals("§b", result.getPrefix())
    );
  }

  @Test
  void resolveGroupWithFallbackByNameReturnsUserGroupWhenNameNotFound() {
    GroupEntry userGroup = new GroupEntry(1, "user", "§8");
    when(groupRegistry.findByName("unknown")).thenReturn(Optional.empty());
    when(groupRegistry.findByName("user")).thenReturn(Optional.of(userGroup));

    GroupEntry result = groupService.resolveGroupWithFallback("unknown");

    assertAll(
        () -> assertEquals(1, result.getId()),
        () -> assertEquals("user", result.getName()),
        () -> assertEquals("§8", result.getPrefix())
    );
  }

  @Test
  void resolveGroupWithFallbackByNameReturnsDefaultEntryWhenNeitherFoundNorUser() {
    when(groupRegistry.findByName("unknown")).thenReturn(Optional.empty());
    when(groupRegistry.findByName("user")).thenReturn(Optional.empty());

    GroupEntry result = groupService.resolveGroupWithFallback("unknown");

    assertAll(
        () -> assertEquals(1, result.getId()),
        () -> assertEquals("user", result.getName()),
        () -> assertEquals("§8", result.getPrefix())
    );
  }

  @Test
  void resolveGroupWithFallbackByIdReturnsFoundGroup() {
    GroupEntry groupEntry = new GroupEntry(5, "owner", "§4");
    when(groupRegistry.findById(5)).thenReturn(Optional.of(groupEntry));

    GroupEntry result = groupService.resolveGroupWithFallback(5);

    assertAll(
        () -> assertEquals(5, result.getId()),
        () -> assertEquals("owner", result.getName()),
        () -> assertEquals("§4", result.getPrefix())
    );
  }

  @Test
  void resolveGroupWithFallbackByIdReturnsUserGroupWhenIdNotFound() {
    GroupEntry userGroup = new GroupEntry(1, "user", "§8");
    when(groupRegistry.findById(99)).thenReturn(Optional.empty());
    when(groupRegistry.findByName("user")).thenReturn(Optional.of(userGroup));

    GroupEntry result = groupService.resolveGroupWithFallback(99);

    assertAll(
        () -> assertEquals(1, result.getId()),
        () -> assertEquals("user", result.getName()),
        () -> assertEquals("§8", result.getPrefix())
    );
  }

  @Test
  void resolveGroupWithFallbackByIdReturnsDefaultEntryWhenNeitherFoundNorUser() {
    when(groupRegistry.findById(99)).thenReturn(Optional.empty());
    when(groupRegistry.findByName("user")).thenReturn(Optional.empty());

    GroupEntry result = groupService.resolveGroupWithFallback(99);

    assertAll(
        () -> assertEquals(1, result.getId()),
        () -> assertEquals("user", result.getName()),
        () -> assertEquals("§8", result.getPrefix())
    );
  }

  @Test
  void findGroupByIdReturnsGroupWhenFound() {
    GroupEntry groupEntry = new GroupEntry(2, "mod", "§6");
    when(groupRegistry.findById(2)).thenReturn(Optional.of(groupEntry));

    Optional<GroupEntry> result = groupService.findGroupById(2);

    assertTrue(result.isPresent());
    assertAll(
        () -> assertEquals(2, result.get().getId()),
        () -> assertEquals("mod", result.get().getName()),
        () -> assertEquals("§6", result.get().getPrefix())
    );
  }

  @Test
  void findGroupByIdReturnsEmptyWhenNotFound() {
    when(groupRegistry.findById(99)).thenReturn(Optional.empty());

    Optional<GroupEntry> result = groupService.findGroupById(99);

    assertTrue(result.isEmpty());
  }

  @Test
  void findGroupByNameReturnsGroupWhenFound() {
    GroupEntry groupEntry = new GroupEntry(3, "admin", "§c");
    when(groupRegistry.findByName("admin")).thenReturn(Optional.of(groupEntry));

    Optional<GroupEntry> result = groupService.findGroupByName("admin");

    assertTrue(result.isPresent());
    assertAll(
        () -> assertEquals(3, result.get().getId()),
        () -> assertEquals("admin", result.get().getName()),
        () -> assertEquals("§c", result.get().getPrefix())
    );
  }

  @Test
  void findGroupByNameReturnsEmptyWhenNotFound() {
    when(groupRegistry.findByName("ghost")).thenReturn(Optional.empty());

    Optional<GroupEntry> result = groupService.findGroupByName("ghost");

    assertTrue(result.isEmpty());
  }

  @Test
  void findAllGroupsReturnsAllRegisteredGroups() {
    GroupEntry userGroup = new GroupEntry(1, "user", "§8");
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");
    when(groupRegistry.getAll()).thenReturn(List.of(userGroup, modGroup));

    List<GroupEntry> result = groupService.findAllGroups();

    assertAll(
        () -> assertEquals(2, result.size()),
        () -> assertEquals("user", result.getFirst().getName()),
        () -> assertEquals("mod", result.get(1).getName())
    );
  }

  @Test
  void resolveAuthorizedGroupReturnsEmptyWhenGroupNotFound() {
    when(groupRegistry.findByName("ghost")).thenReturn(Optional.empty());

    Optional<GroupEntry> result = groupService.resolveAuthorizedGroup(player, "ghost");

    assertTrue(result.isEmpty());
  }

  @Test
  void resolveAuthorizedGroupReturnsEmptyWhenPlayerNotAuthorized() {
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");
    GroupEntry userGroup = new GroupEntry(1, "user", "§8");

    when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
    when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getGroup()).thenReturn(userGroup);

    Optional<GroupEntry> result = groupService.resolveAuthorizedGroup(player, "mod");

    assertTrue(result.isEmpty());
  }

  @Test
  void resolveAuthorizedGroupReturnsGroupWhenPlayerIsAuthorized() {
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");
    GroupEntry adminGroup = new GroupEntry(3, "admin", "§c");

    when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getGroup()).thenReturn(adminGroup);

    Optional<GroupEntry> result = groupService.resolveAuthorizedGroup(player, "mod");

    assertTrue(result.isPresent());
    assertAll(
        () -> assertEquals(2, result.get().getId()),
        () -> assertEquals("mod", result.get().getName()),
        () -> assertEquals("§6", result.get().getPrefix())
    );
  }

  @Test
  void isPlayerInGroupOrHigherReturnsFalseWhenPlayerRegistryIsNull() {
    GroupService serviceWithoutRegistry = new GroupService(groupRegistry, groupRepository);

    boolean result = serviceWithoutRegistry.isPlayerInGroupOrHigher(player, "mod");

    assertFalse(result);
  }

  @Test
  void isPlayerInGroupOrHigherReturnsFalseWhenGroupNotFound() {
    when(groupRegistry.findByName("ghost")).thenReturn(Optional.empty());

    boolean result = groupService.isPlayerInGroupOrHigher(player, "ghost");

    assertFalse(result);
  }

  @Test
  void isPlayerInGroupOrHigherReturnsTrueWhenPlayerGroupIdMeetsRequirement() {
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");
    GroupEntry adminGroup = new GroupEntry(3, "admin", "§c");

    when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getGroup()).thenReturn(adminGroup);

    boolean result = groupService.isPlayerInGroupOrHigher(player, "mod");

    assertTrue(result);
  }

  @Test
  void isPlayerInGroupOrHigherReturnsTrueWhenPlayerGroupIdEqualsRequirement() {
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");

    when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getGroup()).thenReturn(modGroup);

    boolean result = groupService.isPlayerInGroupOrHigher(player, "mod");

    assertTrue(result);
  }

  @Test
  void isPlayerInGroupOrHigherReturnsFalseWhenPlayerGroupIdBelowRequirement() {
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");
    GroupEntry userGroup = new GroupEntry(1, "user", "§8");

    when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getGroup()).thenReturn(userGroup);

    boolean result = groupService.isPlayerInGroupOrHigher(player, "mod");

    assertFalse(result);
  }

  @Test
  void isSenderAuthorizedReturnsTrueForConsole() {
    try (MockedStatic<TypeHelper> typeHelper = mockStatic(TypeHelper.class)) {
      typeHelper.when(() -> TypeHelper.isConsole(commandSender)).thenReturn(true);

      boolean result = groupService.isSenderAuthorized(commandSender, "mod");

      assertTrue(result);
    }
  }

  @Test
  void isSenderAuthorizedReturnsTrueForCommandBlock() {
    try (MockedStatic<TypeHelper> typeHelper = mockStatic(TypeHelper.class)) {
      typeHelper.when(() -> TypeHelper.isConsole(commandSender)).thenReturn(false);
      typeHelper.when(() -> TypeHelper.isCMDBlock(commandSender)).thenReturn(true);

      boolean result = groupService.isSenderAuthorized(commandSender, "mod");

      assertTrue(result);
    }
  }

  @Test
  void isSenderAuthorizedReturnsTrueForAuthorizedPlayer() {
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");
    GroupEntry adminGroup = new GroupEntry(3, "admin", "§c");

    try (MockedStatic<TypeHelper> typeHelper = mockStatic(TypeHelper.class)) {
      typeHelper.when(() -> TypeHelper.isConsole(player)).thenReturn(false);
      typeHelper.when(() -> TypeHelper.isCMDBlock(player)).thenReturn(false);
      typeHelper.when(() -> TypeHelper.isPlayer(player)).thenReturn(true);

      when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
      when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
      when(playerEntry.getGroup()).thenReturn(adminGroup);

      boolean result = groupService.isSenderAuthorized(player, "mod");

      assertTrue(result);
    }
  }

  @Test
  void isSenderAuthorizedReturnsFalseForUnauthorizedPlayer() {
    GroupEntry modGroup = new GroupEntry(2, "mod", "§6");
    GroupEntry userGroup = new GroupEntry(1, "user", "§8");

    try (MockedStatic<TypeHelper> typeHelper = mockStatic(TypeHelper.class)) {
      typeHelper.when(() -> TypeHelper.isConsole(player)).thenReturn(false);
      typeHelper.when(() -> TypeHelper.isCMDBlock(player)).thenReturn(false);
      typeHelper.when(() -> TypeHelper.isPlayer(player)).thenReturn(true);

      when(groupRegistry.findByName("mod")).thenReturn(Optional.of(modGroup));
      when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);
      when(playerEntry.getGroup()).thenReturn(userGroup);

      boolean result = groupService.isSenderAuthorized(player, "mod");

      assertFalse(result);
    }
  }

  @Test
  void isSenderAuthorizedReturnsFalseForUnknownSenderType() {
    try (MockedStatic<TypeHelper> typeHelper = mockStatic(TypeHelper.class)) {
      typeHelper.when(() -> TypeHelper.isConsole(commandSender)).thenReturn(false);
      typeHelper.when(() -> TypeHelper.isCMDBlock(commandSender)).thenReturn(false);
      typeHelper.when(() -> TypeHelper.isPlayer(commandSender)).thenReturn(false);

      boolean result = groupService.isSenderAuthorized(commandSender, "mod");

      assertFalse(result);
    }
  }
}