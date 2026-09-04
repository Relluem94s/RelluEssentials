package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_RIGHTS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProtectionActionServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerService playerService;

  @Mock
  private ProtectionService protectionService;

  @Mock
  private LocationService locationService;

  @Mock
  private TranslationService translationService;

  @Mock
  private Player player;

  @Mock
  private Block block;

  @Mock
  private World world;

  private ProtectionActionService protectionActionService;

  @BeforeEach
  void setUp() {
    lenient().when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getProtectionService()).thenReturn(protectionService);
    lenient().when(serviceContext.getLocationService()).thenReturn(locationService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    protectionActionService = new ProtectionActionService(serviceContext);
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenMaterialIsNotProtectable() {
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);
    BlockData nonMatchingData = mock(BlockData.class);
    Block eastRelative = buildRelativeBlockWithData(nonMatchingData);
    Block southRelative = buildRelativeBlockWithData(nonMatchingData);
    Block northRelative = buildRelativeBlockWithData(nonMatchingData);
    Block westRelative = buildRelativeBlockWithData(nonMatchingData);
    Block upRelative = buildRelativeBlockWithData(nonMatchingData);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);
    when(block.getRelative(BlockFace.UP)).thenReturn(upRelative);
    setupPlayerEntryWithId();

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertFalse(result);
  }

  @Test
  void removeProtectionFromBlockReturnsTrueWhenPlayerIsNotOwner() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getBlockData()).thenReturn(mock(Chest.class));
    when(block.getLocation()).thenReturn(location);
    setupPlayerEntryWithId();

    LocationEntry locationEntry = buildLocationEntry(99, location);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(any(Location.class))).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(
        MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(() -> assertTrue(result), () -> verify(player).sendMessage("disallow"));
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenPlayerIsOwner() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getBlockData()).thenReturn(mock(Chest.class));
    when(block.getLocation()).thenReturn(location);
    setupPlayerEntryWithId();

    LocationEntry locationEntry = buildLocationEntry(1, location);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(any(Location.class))).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn(
        "removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(() -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed"));
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenNoProtectionExists() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getBlockData()).thenReturn(mock(Chest.class));
    when(block.getLocation()).thenReturn(location);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(protectionService.getProtectionEntry(any(Location.class))).thenReturn(null);

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertFalse(result);
  }

  @Test
  void protectBlockReturnsFalseWhenMaterialIsNotProtectable() {
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    boolean result = protectionActionService.protectBlock(player, block);

    assertFalse(result);
  }

  @Test
  void protectBlockReturnsFalseWhenAlreadyProtected() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));
    setupPlayerEntryWithId();

    LocationEntry existingLocationEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(
        existingLocationEntry);

    boolean result = protectionActionService.protectBlock(player, block);

    assertFalse(result);
  }

  @Test
  void protectBlockReturnsTrueWhenSuccessful() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));
    setupPlayerEntryWithId();

    LocationEntry builtEntry = buildLocationEntry(1, location);
    setupLocationServiceForSuccessfulProtection(location, builtEntry);

    boolean result = protectionActionService.protectBlock(player, block);

    assertAll(() -> assertTrue(result), () -> verify(locationService).save(builtEntry),
        () -> verify(protectionService).saveProtectionAndAddToRegistry(eq(location),
            any(ProtectionEntry.class)), () -> verify(player).sendMessage("protected"));
  }

  @Test
  void protectBlockReturnsFalseWhenPersistedLocationEntryIsNull() {
    Location location = new Location(world, 0, 0, 0);
    Logger.getLogger(ProtectionActionService.class.getName()).setLevel(Level.OFF);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));
    setupPlayerEntryWithId();

    when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(null)
        .thenReturn(null);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION),
        eq(1))).thenReturn(builtEntry);

    boolean result = protectionActionService.protectBlock(player, block);

    assertFalse(result);
  }

  @Test
  void protectBlockSetsAllowRedstoneFlagForLever() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.LEVER);
    when(protectionService.isProtectableMaterial(Material.LEVER)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));
    setupPlayerEntryWithId();

    LocationEntry builtEntry = buildLocationEntry(1, location);
    setupLocationServiceForSuccessfulProtection(location, builtEntry);
    protectionActionService.protectBlock(player, block);

    verify(protectionService).saveProtectionAndAddToRegistry(eq(location),
        org.mockito.Mockito.argThat(entry -> {
          JSONObject flags = entry.getFlags();
          if (!flags.has(PLUGIN_EVENT_PROTECT_FLAGS)) {
            return false;
          }
          JSONArray flagArray = flags.getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);
          return flagArray.toList().contains(ProtectionFlags.ALLOW_REDSTONE.name());
        }));
  }

  @Test
  void protectBlockSetsAllowRedstoneFlagForIronDoor() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.IRON_DOOR);
    when(protectionService.isProtectableMaterial(Material.IRON_DOOR)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Door doorData = mock(Door.class);
    when(block.getBlockData()).thenReturn(doorData);
    setupPlayerEntryWithId();

    LocationEntry builtEntry = buildLocationEntry(1, location);
    setupLocationServiceForSuccessfulProtection(location, builtEntry);
    protectionActionService.protectBlock(player, block);

    verify(protectionService).saveProtectionAndAddToRegistry(eq(location),
        org.mockito.Mockito.argThat(entry -> {
          JSONObject flags = entry.getFlags();
          if (!flags.has(PLUGIN_EVENT_PROTECT_FLAGS)) {
            return false;
          }
          JSONArray flagArray = flags.getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);
          return flagArray.toList().contains(ProtectionFlags.ALLOW_REDSTONE.name());
        }));
  }

  @Test
  void protectBlockIncludesPartnerInRightsWhenPlayerHasPartner() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    PlayerPartnerEntry partnerEntry = new PlayerPartnerEntry();
    partnerEntry.setFirstPartnerId(1);
    partnerEntry.setSecondPartnerId(2);

    PlayerEntry playerEntry = buildPlayerEntry();
    playerEntry.setPartner(partnerEntry);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    setupLocationServiceForSuccessfulProtection(location, builtEntry);
    protectionActionService.protectBlock(player, block);

    verify(protectionService).saveProtectionAndAddToRegistry(eq(location),
        org.mockito.Mockito.argThat(entry -> {
          JSONObject rights = entry.getRights();
          if (!rights.has(PLUGIN_EVENT_PROTECT_RIGHTS)) {
            return false;
          }
          JSONArray rightArray = rights.getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);
          return rightArray.length() == 2;
        }));
  }

  @Test
  void addRightAddsPlayerIdAndUpdatesRegistry() {
    Location location = new Location(world, 0, 0, 0);
    LocationEntry locationEntry = mock(LocationEntry.class);
    when(locationEntry.getLocation()).thenReturn(location);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 1);
    when(translationService.getWithPrefix(
        MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD)).thenReturn("right added");

    protectionActionService.addRight(player, protectionEntry, 2, false);

    assertAll(() -> verify(protectionService).updateProtectionRights(protectionEntry),
        () -> verify(protectionService).removeProtectionEntry(location),
        () -> verify(protectionService).putProtectionEntry(location, protectionEntry),
        () -> verify(player).sendMessage("right added"), () -> assertTrue(
            protectionEntry.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS).toList()
                .contains(2)));
  }

  @Test
  void addRightSendsFailedMessageWhenIdAlreadyExists() {
    LocationEntry locationEntry = mock(LocationEntry.class);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 2);
    when(translationService.getWithPrefix(
        MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED)).thenReturn("right add failed");

    protectionActionService.addRight(player, protectionEntry, 2, false);

    assertAll(() -> verify(protectionService, never()).updateProtectionRights(any()),
        () -> verify(player).sendMessage("right add failed"));
  }

  @Test
  void addRightDoesNotSendMessageWhenSilent() {
    LocationEntry locationEntry = mock(LocationEntry.class);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 1);
    protectionActionService.addRight(player, protectionEntry, 2, true);

    verify(player, never()).sendMessage(any(String.class));
  }

  @Test
  void addRightDoesNothingWhenRightsKeyMissing() {
    Location location = new Location(world, 0, 0, 0);
    LocationEntry locationEntry = buildLocationEntry(1, location);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(new JSONObject());

    protectionActionService.addRight(player, protectionEntry, 2, false);

    verify(protectionService, never()).updateProtectionRights(any());
  }

  @Test
  void removeRightRemovesPlayerIdAndUpdatesRegistry() {
    Location location = new Location(world, 0, 0, 0);
    LocationEntry locationEntry = mock(LocationEntry.class);
    when(locationEntry.getLocation()).thenReturn(location);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 1, 2);
    when(translationService.getWithPrefix(
        MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE)).thenReturn("right removed");

    protectionActionService.removeRight(player, protectionEntry, 2, false);

    assertAll(() -> verify(protectionService).updateProtectionRights(protectionEntry),
        () -> verify(protectionService).removeProtectionEntry(location),
        () -> verify(protectionService).putProtectionEntry(location, protectionEntry),
        () -> verify(player).sendMessage("right removed"), () -> assertFalse(
            protectionEntry.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS).toList()
                .contains(2)));
  }

  @Test
  void removeRightSendsFailedMessageWhenIdDoesNotExist() {
    LocationEntry locationEntry = mock(LocationEntry.class);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 1);
    when(translationService.getWithPrefix(
        MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED)).thenReturn(
        "right remove failed");

    protectionActionService.removeRight(player, protectionEntry, 99, false);

    assertAll(() -> verify(protectionService, never()).updateProtectionRights(any()),
        () -> verify(player).sendMessage("right remove failed"));
  }

  @Test
  void removeRightDoesNotSendMessageWhenSilent() {
    LocationEntry locationEntry = mock(LocationEntry.class);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 1, 2);
    protectionActionService.removeRight(player, protectionEntry, 2, true);

    verify(player, never()).sendMessage(any(String.class));
  }

  @Test
  void removeRightDoesNothingWhenRightsKeyMissing() {
    LocationEntry locationEntry = mock(LocationEntry.class);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(new JSONObject());

    protectionActionService.removeRight(player, protectionEntry, 2, false);

    verify(protectionService, never()).updateProtectionRights(any());
  }

  @Test
  void removeRightWithTwoArgsCallsSilentRemoveRight() {
    Location location = new Location(world, 0, 0, 0);
    LocationEntry locationEntry = mock(LocationEntry.class);
    when(locationEntry.getLocation()).thenReturn(location);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 1, 2);

    protectionActionService.removeRight(protectionEntry, 2);

    assertAll(() -> verify(protectionService).updateProtectionRights(protectionEntry),
        () -> verify(protectionService).removeProtectionEntry(location),
        () -> verify(protectionService).putProtectionEntry(location, protectionEntry));
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenProtectionEntryHasNullLocationEntry() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getBlockData()).thenReturn(mock(Chest.class));
    when(block.getLocation()).thenReturn(location);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    ProtectionEntry protectionEntryWithNullLocation = new ProtectionEntry();
    protectionEntryWithNullLocation.setLocationEntry(null);
    when(protectionService.getProtectionEntry(any(Location.class))).thenReturn(
        protectionEntryWithNullLocation);

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertFalse(result);
  }

  @Test
  void protectBlockReturnsFalseWhenDoubleChestNeighbourIsProtectedByOther() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(block.getBlockData()).thenReturn(chestData);

    Block eastNeighbour = mock(Block.class);
    Block westNeighbour = mock(Block.class);

    Chest neighbourChestData = mock(Chest.class);
    when(neighbourChestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(eastNeighbour.getBlockData()).thenReturn(neighbourChestData);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastNeighbour);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westNeighbour);

    try (var mockedStatic = org.mockito.Mockito.mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.class)) {
      mockedStatic.when(
          () -> de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.hasPermission(
              eastNeighbour, player)).thenReturn(true);

      PlayerEntry playerEntry = buildPlayerEntry();
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

      when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(
          null);
      when(translationService.getWithPrefix(
          MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");
      when(translationService.getWithPrefix(
          MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY)).thenReturn("chest deny");

      boolean result = protectionActionService.protectBlock(player, block);

      assertAll(() -> assertFalse(result), () -> verify(player).sendMessage("disallow"),
          () -> verify(player).sendMessage("chest deny"));
    }
  }

  @Test
  void protectBlockReturnsFalseWhenLocationAlreadyExistsAndDoubleChestHasNoRights() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(block.getBlockData()).thenReturn(chestData);

    Block eastNeighbour = mock(Block.class);
    Block westNeighbour = mock(Block.class);

    when(eastNeighbour.getBlockData()).thenReturn(mock(BlockData.class));
    when(westNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastNeighbour);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westNeighbour);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry existingLocationEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(
        existingLocationEntry);

    boolean result = protectionActionService.protectBlock(player, block);

    assertFalse(result);
  }

  @Test
  void protectBlockReturnsFalseWhenNeighbourChestHasDifferentFacing() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(block.getBlockData()).thenReturn(chestData);

    Block eastNeighbour = mock(Block.class);
    Block westNeighbour = mock(Block.class);

    Chest neighbourChestData = mock(Chest.class);
    when(neighbourChestData.getFacing()).thenReturn(BlockFace.EAST);
    when(eastNeighbour.getBlockData()).thenReturn(neighbourChestData);
    when(westNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastNeighbour);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westNeighbour);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION),
        eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn(
        "protected");

    boolean result = protectionActionService.protectBlock(player, block);

    assertTrue(result);
  }

  @Test
  void protectBlockReturnsTrueWhenNeighbourChestHasSameFacingButNoProtection() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(block.getBlockData()).thenReturn(chestData);

    Block eastNeighbour = mock(Block.class);
    Block westNeighbour = mock(Block.class);

    Chest neighbourChestData = mock(Chest.class);
    when(neighbourChestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(eastNeighbour.getBlockData()).thenReturn(neighbourChestData);
    when(westNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastNeighbour);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westNeighbour);

    try (var mockedStatic = org.mockito.Mockito.mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.class)) {
      mockedStatic.when(
          () -> de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.hasPermission(
              eastNeighbour, player)).thenReturn(false);

      PlayerEntry playerEntry = buildPlayerEntry();
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

      LocationEntry builtEntry = buildLocationEntry(1, location);
      when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(
          null).thenReturn(builtEntry);

      when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION),
          eq(1))).thenReturn(builtEntry);
      when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn(
          "protected");

      boolean result = protectionActionService.protectBlock(player, block);

      assertTrue(result);
    }
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenWallHangingSignFacingDoesNotPointToBlock() {
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);
    Block differentBlock = mock(Block.class);

    WallHangingSign wallHangingSign = mock(
        WallHangingSign.class);
    when(wallHangingSign.getFacing()).thenReturn(BlockFace.WEST);
    when(eastRelative.getBlockData()).thenReturn(wallHangingSign);
    when(eastRelative.getRelative(BlockFace.EAST)).thenReturn(differentBlock);

    BlockData nonMatchingData = mock(BlockData.class);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);
    when(westRelative.getBlockData()).thenReturn(nonMatchingData);
    when(upRelative.getBlockData()).thenReturn(nonMatchingData);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);
    when(block.getRelative(BlockFace.UP)).thenReturn(upRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertFalse(result);
  }

  @Test
  void protectBlockIncludesFirstPartnerIdInRightsWhenPlayerIsSecondPartner() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    PlayerPartnerEntry partnerEntry = new PlayerPartnerEntry();
    partnerEntry.setFirstPartnerId(2);
    partnerEntry.setSecondPartnerId(1);

    PlayerEntry playerEntry = buildPlayerEntry();
    playerEntry.setPartner(partnerEntry);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    setupLocationServiceForSuccessfulProtection(location, builtEntry);
    protectionActionService.protectBlock(player, block);

    verify(protectionService).saveProtectionAndAddToRegistry(eq(location),
        org.mockito.Mockito.argThat(entry ->
            entry.getRights()
                .getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS)
                .toList()
                .contains(2)));
  }

  @Test
  void removeRightDoesNotSendMessageWhenSilentAndIdDoesNotExist() {
    LocationEntry locationEntry = mock(LocationEntry.class);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 1);
    protectionActionService.removeRight(player, protectionEntry, 99, true);

    assertAll(() -> verify(protectionService, never()).updateProtectionRights(any()),
        () -> verify(player, never()).sendMessage(any(String.class)));
  }

  @Test
  void addRightDoesNotSendMessageWhenSilentAndIdAlreadyExists() {
    LocationEntry locationEntry = mock(LocationEntry.class);
    ProtectionEntry protectionEntry = buildProtectionEntryWithRights(locationEntry, 2);
    protectionActionService.addRight(player, protectionEntry, 2, true);

    assertAll(() -> verify(protectionService, never()).updateProtectionRights(any()),
        () -> verify(player, never()).sendMessage(any(String.class)));
  }

  @Test
  void protectBlockReturnsFalseWhenRightNeighbourChestIsProtectedByOther() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(block.getBlockData()).thenReturn(chestData);

    Block eastNeighbour = mock(Block.class);
    Block westNeighbour = mock(Block.class);

    when(eastNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    Chest westNeighbourChestData = mock(Chest.class);
    when(westNeighbourChestData.getFacing()).thenReturn(BlockFace.NORTH);
    when(westNeighbour.getBlockData()).thenReturn(westNeighbourChestData);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastNeighbour);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westNeighbour);

    try (var mockedStatic = org.mockito.Mockito.mockStatic(
        de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.class)) {
      mockedStatic.when(
          () -> de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.hasPermission(
              westNeighbour, player)).thenReturn(true);

      PlayerEntry playerEntry = buildPlayerEntry();
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

      when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(null);
      when(translationService.getWithPrefix(
          MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");
      when(translationService.getWithPrefix(
          MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY)).thenReturn("chest deny");

      boolean result = protectionActionService.protectBlock(player, block);

      assertAll(() -> assertFalse(result), () -> verify(player).sendMessage("disallow"),
          () -> verify(player).sendMessage("chest deny"));
    }
  }

  @ParameterizedTest
  @MethodSource("wallSignAndStandingSignAttachmentScenarios")
  void removeProtectionFromBlockHandlesWallSignAndStandingSignAttachment(
      BlockFace attachedFace, boolean isWallSign, BlockFace signFacing,
      Location attachedLocation, Integer locationEntryPlayerId,
      boolean expectedResult, MessageKey translationKey, String translationValue) {

    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block attachedRelative = mock(Block.class);

    if (isWallSign) {
      WallSign wallSign = mock(WallSign.class);
      when(wallSign.getFacing()).thenReturn(signFacing);
      when(attachedRelative.getBlockData()).thenReturn(wallSign);
      when(attachedRelative.getRelative(signFacing.getOppositeFace())).thenReturn(block);
    } else {
      Sign standingSign = mock(Sign.class);
      when(attachedRelative.getBlockData()).thenReturn(standingSign);
      when(attachedRelative.getRelative(BlockFace.DOWN)).thenReturn(block);
    }

    setupNonMatchingRelativesExcept(attachedFace);
    doReturn(attachedRelative).when(block).getRelative(attachedFace);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    when(attachedRelative.getLocation()).thenReturn(attachedLocation);

    if (locationEntryPlayerId == null) {
      when(protectionService.getProtectionEntry(attachedLocation)).thenReturn(null);
    } else if (locationEntryPlayerId == -1) {
      ProtectionEntry protectionEntryWithNullLocation = new ProtectionEntry();
      protectionEntryWithNullLocation.setLocationEntry(null);
      when(protectionService.getProtectionEntry(attachedLocation)).thenReturn(
          protectionEntryWithNullLocation);
    } else {
      LocationEntry locationEntry = buildLocationEntry(locationEntryPlayerId, attachedLocation);
      ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
      when(protectionService.getProtectionEntry(attachedLocation)).thenReturn(protectionEntry);
      when(translationService.getWithPrefix(translationKey)).thenReturn(translationValue);
    }

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    if (locationEntryPlayerId == null || locationEntryPlayerId == -1) {
      assertFalse(result);
    } else if (expectedResult) {
      assertAll(
          () -> assertTrue(result),
          () -> verify(player).sendMessage(translationValue)
      );
    } else {
      ProtectionEntry capturedEntry = protectionService.getProtectionEntry(attachedLocation);
      assertAll(
          () -> assertFalse(result),
          () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(capturedEntry),
          () -> verify(player).sendMessage(translationValue)
      );
    }
  }

  static Stream<Arguments> wallSignAndStandingSignAttachmentScenarios() {
    World world = mock(World.class);
    Location eastLocation  = new Location(world, 1,  0,  0);
    Location southLocation = new Location(world, 0,  0,  1);
    Location northLocation = new Location(world, 0,  0, -1);
    Location westLocation  = new Location(world, -1, 0,  0);
    Location aboveLocation = new Location(world, 0,  1,  0);
    return Stream.of(
        Arguments.of(BlockFace.EAST,  true,  BlockFace.WEST,  eastLocation,  1,    false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.EAST,  true,  BlockFace.WEST,  eastLocation,  99,   true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW, "disallow"),
        Arguments.of(BlockFace.EAST,  true,  BlockFace.WEST,  eastLocation,  null, false, null, null),
        Arguments.of(BlockFace.EAST,  true,  BlockFace.WEST,  eastLocation,  -1,   false, null, null),
        Arguments.of(BlockFace.SOUTH, true,  BlockFace.NORTH, southLocation, 1,    false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.SOUTH, true,  BlockFace.NORTH, southLocation, 99,   true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW, "disallow"),
        Arguments.of(BlockFace.SOUTH, true,  BlockFace.NORTH, southLocation, null, false, null, null),
        Arguments.of(BlockFace.SOUTH, true,  BlockFace.NORTH, southLocation, -1,   false, null, null),
        Arguments.of(BlockFace.NORTH, true,  BlockFace.SOUTH, northLocation, 1,    false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.NORTH, true,  BlockFace.SOUTH, northLocation, 99,   true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW, "disallow"),
        Arguments.of(BlockFace.NORTH, true,  BlockFace.SOUTH, northLocation, null, false, null, null),
        Arguments.of(BlockFace.NORTH, true,  BlockFace.SOUTH, northLocation, -1,   false, null, null),
        Arguments.of(BlockFace.WEST,  true,  BlockFace.EAST,  westLocation,  1,    false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.WEST,  true,  BlockFace.EAST,  westLocation,  99,   true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW, "disallow"),
        Arguments.of(BlockFace.WEST,  true,  BlockFace.EAST,  westLocation,  null, false, null, null),
        Arguments.of(BlockFace.WEST,  true,  BlockFace.EAST,  westLocation,  -1,   false, null, null),
        Arguments.of(BlockFace.UP,    false, null,            aboveLocation, 1,    false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.UP,    false, null,            aboveLocation, 99,   true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW, "disallow"),
        Arguments.of(BlockFace.UP,    false, null,            aboveLocation, null, false, null, null),
        Arguments.of(BlockFace.UP,    false, null,            aboveLocation, -1,   false, null, null)
    );
  }

  @Test
  void protectBlockSkipsDoubleChestLogicWhenChestIsSingle() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.SINGLE);
    when(block.getBlockData()).thenReturn(chestData);

    setupPlayerEntryWithId();

    LocationEntry builtEntry = buildLocationEntry(1, location);
    setupLocationServiceForSuccessfulProtection(location, builtEntry);

    boolean result = protectionActionService.protectBlock(player, block);

    assertAll(() -> assertTrue(result),
        () -> verify(protectionService).saveProtectionAndAddToRegistry(eq(location),
            any(ProtectionEntry.class)));
  }

  @ParameterizedTest
  @MethodSource("wallHangingSignAttachmentScenarios")
  void removeProtectionFromBlockHandlesWallHangingSignAttachment(
      BlockFace attachedFace, BlockFace signFacing, Location attachedLocation,
      int locationEntryPlayerId, boolean expectedResult, MessageKey translationKey, String translationValue) {

    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block attachedRelative = mock(Block.class);
    WallHangingSign wallHangingSign = mock(WallHangingSign.class);
    when(wallHangingSign.getFacing()).thenReturn(signFacing);
    when(attachedRelative.getBlockData()).thenReturn(wallHangingSign);
    when(attachedRelative.getRelative(signFacing.getOppositeFace())).thenReturn(block);
    when(attachedRelative.getLocation()).thenReturn(attachedLocation);

    setupNonMatchingRelativesExcept(attachedFace);
    doReturn(attachedRelative).when(block).getRelative(attachedFace);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(locationEntryPlayerId, attachedLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(translationKey)).thenReturn(translationValue);
    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    if (expectedResult) {
      assertAll(
          () -> assertTrue(result),
          () -> verify(player).sendMessage(translationValue)
      );
    } else {
      assertAll(
          () -> assertFalse(result),
          () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
          () -> verify(player).sendMessage(translationValue)
      );
    }
  }

  static Stream<Arguments> wallHangingSignAttachmentScenarios() {
    World world = mock(World.class);
    return Stream.of(
        Arguments.of(BlockFace.EAST,  BlockFace.WEST,  new Location(world, 1,  0,  0), 1,  false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.EAST,  BlockFace.WEST,  new Location(world, 1,  0,  0), 99, true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW,  "disallow"),
        Arguments.of(BlockFace.SOUTH, BlockFace.NORTH, new Location(world, 0,  0,  1), 1,  false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.SOUTH, BlockFace.NORTH, new Location(world, 0,  0,  1), 99, true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW,  "disallow"),
        Arguments.of(BlockFace.NORTH, BlockFace.SOUTH, new Location(world, 0,  0, -1), 1,  false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.NORTH, BlockFace.SOUTH, new Location(world, 0,  0, -1), 99, true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW,  "disallow"),
        Arguments.of(BlockFace.WEST,  BlockFace.EAST,  new Location(world, -1, 0,  0), 1,  false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,   "removed"),
        Arguments.of(BlockFace.WEST,  BlockFace.EAST,  new Location(world, -1, 0,  0), 99, true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW,  "disallow")
    );
  }

  @ParameterizedTest
  @MethodSource("wallSignAttachmentDirections")
  void removeProtectionFromBlockReturnsFalseWhenWallSignAttachedToDirectionAndPlayerIsOwner(
      BlockFace attachedFace, BlockFace signFacing, Location attachedLocation) {

    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block attachedRelative = mock(Block.class);
    WallSign wallSign = mock(WallSign.class);
    when(wallSign.getFacing()).thenReturn(signFacing);
    when(attachedRelative.getBlockData()).thenReturn(wallSign);
    when(attachedRelative.getRelative(signFacing.getOppositeFace())).thenReturn(block);
    when(attachedRelative.getLocation()).thenReturn(attachedLocation);

    setupNonMatchingRelativesExcept(attachedFace);
    doReturn(attachedRelative).when(block).getRelative(attachedFace);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE))
        .thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  static Stream<Arguments> wallSignAttachmentDirections() {
    World world = mock(World.class);
    return Stream.of(
        Arguments.of(BlockFace.EAST,  BlockFace.WEST,  new Location(world, 1,  0, 0)),
        Arguments.of(BlockFace.SOUTH, BlockFace.NORTH, new Location(world, 0,  0, 1)),
        Arguments.of(BlockFace.NORTH, BlockFace.SOUTH, new Location(world, 0,  0, -1)),
        Arguments.of(BlockFace.WEST,  BlockFace.EAST,  new Location(world, -1, 0, 0))
    );
  }

  @ParameterizedTest
  @MethodSource("doubleChestFacingDirectionsWithNeighbourFaces")
  void protectBlockReturnsTrueWhenDoubleChestFacingDirectionAndNeighboursAreNotProtected(
      BlockFace chestFacing, BlockFace leftNeighbourFace, BlockFace rightNeighbourFace) {

    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(chestFacing);
    when(block.getBlockData()).thenReturn(chestData);

    Block leftNeighbour = mock(Block.class);
    Block rightNeighbour = mock(Block.class);
    when(leftNeighbour.getBlockData()).thenReturn(mock(BlockData.class));
    when(rightNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    when(block.getRelative(leftNeighbourFace)).thenReturn(leftNeighbour);
    when(block.getRelative(rightNeighbourFace)).thenReturn(rightNeighbour);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);
    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1)))
        .thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD))
        .thenReturn("protected");

    boolean result = protectionActionService.protectBlock(player, block);

    assertTrue(result);
  }

  static Stream<Arguments> doubleChestFacingDirectionsWithNeighbourFaces() {
    return Stream.of(
        Arguments.of(BlockFace.NORTH, BlockFace.EAST,  BlockFace.WEST),
        Arguments.of(BlockFace.EAST,  BlockFace.SOUTH, BlockFace.NORTH),
        Arguments.of(BlockFace.SOUTH, BlockFace.WEST,  BlockFace.EAST),
        Arguments.of(BlockFace.WEST,  BlockFace.NORTH, BlockFace.SOUTH)
    );
  }

  @ParameterizedTest
  @MethodSource("doorAttachmentAboveScenarios")
  void removeProtectionFromBlockHandlesDoorAttachedAbove(
      boolean doorPointsToBlock, int locationEntryPlayerId,
      boolean expectedResult, MessageKey translationKey, String translationValue) {

    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Location attachedBlockLocation = new Location(world, 0, 1, 0);
    Block upRelative = mock(Block.class);
    Block blockBelowDoor = doorPointsToBlock ? block : mock(Block.class);

    Door door = mock(Door.class);
    when(upRelative.getBlockData()).thenReturn(door);
    when(upRelative.getRelative(BlockFace.DOWN)).thenReturn(blockBelowDoor);

    if (doorPointsToBlock) {
      when(upRelative.getLocation()).thenReturn(attachedBlockLocation);
    }

    setupNonMatchingRelativesExcept(BlockFace.UP);
    doReturn(upRelative).when(block).getRelative(BlockFace.UP);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    if (doorPointsToBlock) {
      LocationEntry locationEntry = buildLocationEntry(locationEntryPlayerId, attachedBlockLocation);
      ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
      when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
      when(translationService.getWithPrefix(translationKey)).thenReturn(translationValue);
    }

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    if (!doorPointsToBlock) {
      assertFalse(result);
    } else if (expectedResult) {
      assertAll(
          () -> assertTrue(result),
          () -> verify(player).sendMessage(translationValue)
      );
    } else {
      ProtectionEntry capturedEntry = protectionService.getProtectionEntry(attachedBlockLocation);
      assertAll(
          () -> assertFalse(result),
          () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(capturedEntry),
          () -> verify(player).sendMessage(translationValue)
      );
    }
  }

  static Stream<Arguments> doorAttachmentAboveScenarios() {
    return Stream.of(
        Arguments.of(true,  1,  false, MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE,  "removed"),
        Arguments.of(true,  99, true,  MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW, "disallow"),
        Arguments.of(false, 0,  false, null, null)
    );
  }


  private void setupNonMatchingRelativesExcept(BlockFace excludedFace) {
    BlockData nonMatchingData = mock(BlockData.class);
    List<BlockFace> sideFaces = List.of(BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.WEST, BlockFace.UP);
    for (BlockFace face : sideFaces) {
      if (!face.equals(excludedFace)) {
        Block relative = mock(Block.class);
        lenient().when(relative.getBlockData()).thenReturn(nonMatchingData);
        lenient().when(block.getRelative(face)).thenReturn(relative);
      }
    }
  }

  private PlayerEntry buildPlayerEntry() {
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(1);
    return playerEntry;
  }

  private LocationEntry buildLocationEntry(int playerId, Location location) {
    LocationEntry locationEntry = new LocationEntry();
    locationEntry.setPlayerId(playerId);
    locationEntry.setLocation(location);
    return locationEntry;
  }

  private ProtectionEntry buildProtectionEntry(LocationEntry locationEntry) {
    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    return protectionEntry;
  }

  private Block buildRelativeBlockWithData(BlockData data) {
    Block relative = mock(Block.class);
    when(relative.getBlockData()).thenReturn(data);
    return relative;
  }

  private void setupPlayerEntryWithId() {
    PlayerEntry playerEntry = buildPlayerEntry();
    playerEntry.setId(1);
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
  }

  private void setupLocationServiceForSuccessfulProtection(Location location,
      LocationEntry builtEntry) {
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(null)
        .thenReturn(builtEntry);
    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION),
        eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn(
        "protected");
  }

  private JSONObject buildRightsJson(Integer... playerIds) {
    JSONArray rights = new JSONArray();
    for (Integer id : playerIds) {
      rights.put(id);
    }
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, rights);
    return rightsJson;
  }

  private ProtectionEntry buildProtectionEntryWithRights(LocationEntry locationEntry,
      Integer... playerIds) {
    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(buildRightsJson(playerIds));
    return protectionEntry;
  }
}