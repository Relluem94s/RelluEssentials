package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_RIGHTS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
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
  void removeProtectionFromBlockReturnsTrueWhenPlayerIsNotOwner() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getBlockData()).thenReturn(mock(Chest.class));
    when(block.getLocation()).thenReturn(location);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(99, location);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(any(Location.class))).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("disallow")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenPlayerIsOwner() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getBlockData()).thenReturn(mock(Chest.class));
    when(block.getLocation()).thenReturn(location);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, location);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(any(Location.class))).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
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

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry existingLocationEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(existingLocationEntry);

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

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    boolean result = protectionActionService.protectBlock(player, block);

    assertAll(
        () -> assertTrue(result),
        () -> verify(locationService).save(builtEntry),
        () -> verify(protectionService).saveProtectionAndAddToRegistry(eq(location), any(ProtectionEntry.class)),
        () -> verify(player).sendMessage("protected")
    );
  }

  @Test
  void protectBlockReturnsFalseWhenPersistedLocationEntryIsNull() {
    Location location = new Location(world, 0, 0, 0);
    Logger.getLogger(ProtectionActionService.class.getName()).setLevel(Level.OFF);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(BlockData.class));

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(null);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);

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

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(any(Location.class), any(), any(LocationType.class), anyInt())).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    protectionActionService.protectBlock(player, block);

    verify(protectionService).saveProtectionAndAddToRegistry(eq(location), org.mockito.Mockito.argThat(entry -> {
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

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(any(Location.class), any(), any(LocationType.class), anyInt())).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    protectionActionService.protectBlock(player, block);

    verify(protectionService).saveProtectionAndAddToRegistry(eq(location), org.mockito.Mockito.argThat(entry -> {
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
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(any(Location.class), any(), any(LocationType.class), anyInt())).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    protectionActionService.protectBlock(player, block);

    verify(protectionService).saveProtectionAndAddToRegistry(eq(location), org.mockito.Mockito.argThat(entry -> {
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

    JSONArray existingRights = new JSONArray();
    existingRights.put(1);
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, existingRights);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(rightsJson);

    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD)).thenReturn("right added");

    protectionActionService.addRight(player, protectionEntry, 2, false);

    assertAll(
        () -> verify(protectionService).updateProtectionRights(protectionEntry),
        () -> verify(protectionService).removeProtectionEntry(location),
        () -> verify(protectionService).putProtectionEntry(location, protectionEntry),
        () -> verify(player).sendMessage("right added"),
        () -> assertTrue(protectionEntry.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS).toList().contains(2))
    );
  }

  @Test
  void addRightSendsFailedMessageWhenIdAlreadyExists() {
    LocationEntry locationEntry = mock(LocationEntry.class);

    JSONArray existingRights = new JSONArray();
    existingRights.put(2);
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, existingRights);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(rightsJson);

    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED)).thenReturn("right add failed");

    protectionActionService.addRight(player, protectionEntry, 2, false);

    assertAll(
        () -> verify(protectionService, never()).updateProtectionRights(any()),
        () -> verify(player).sendMessage("right add failed")
    );
  }

  @Test
  void addRightDoesNotSendMessageWhenSilent() {
    LocationEntry locationEntry = mock(LocationEntry.class);

    JSONArray existingRights = new JSONArray();
    existingRights.put(1);
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, existingRights);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(rightsJson);

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

    JSONArray existingRights = new JSONArray();
    existingRights.put(1);
    existingRights.put(2);
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, existingRights);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(rightsJson);

    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE)).thenReturn("right removed");

    protectionActionService.removeRight(player, protectionEntry, 2, false);

    assertAll(
        () -> verify(protectionService).updateProtectionRights(protectionEntry),
        () -> verify(protectionService).removeProtectionEntry(location),
        () -> verify(protectionService).putProtectionEntry(location, protectionEntry),
        () -> verify(player).sendMessage("right removed"),
        () -> assertFalse(protectionEntry.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS).toList().contains(2))
    );
  }

  @Test
  void removeRightSendsFailedMessageWhenIdDoesNotExist() {
    LocationEntry locationEntry = mock(LocationEntry.class);

    JSONArray existingRights = new JSONArray();
    existingRights.put(1);
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, existingRights);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(rightsJson);

    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED)).thenReturn("right remove failed");

    protectionActionService.removeRight(player, protectionEntry, 99, false);

    assertAll(
        () -> verify(protectionService, never()).updateProtectionRights(any()),
        () -> verify(player).sendMessage("right remove failed")
    );
  }

  @Test
  void removeRightDoesNotSendMessageWhenSilent() {
    LocationEntry locationEntry = mock(LocationEntry.class);

    JSONArray existingRights = new JSONArray();
    existingRights.put(1);
    existingRights.put(2);
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, existingRights);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(rightsJson);

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

    JSONArray existingRights = new JSONArray();
    existingRights.put(1);
    existingRights.put(2);
    JSONObject rightsJson = new JSONObject();
    rightsJson.put(PLUGIN_EVENT_PROTECT_RIGHTS, existingRights);

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(locationEntry);
    protectionEntry.setRights(rightsJson);

    protectionActionService.removeRight(protectionEntry, 2);

    assertAll(
        () -> verify(protectionService).updateProtectionRights(protectionEntry),
        () -> verify(protectionService).removeProtectionEntry(location),
        () -> verify(protectionService).putProtectionEntry(location, protectionEntry)
    );
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
    when(protectionService.getProtectionEntry(any(Location.class))).thenReturn(protectionEntryWithNullLocation);

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertFalse(result);
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenSignAttachedToEastAndPlayerIsOwner() {
    Location attachedBlockLocation = new Location(world, 1, 0, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);

    org.bukkit.block.data.type.WallSign wallSign = mock(org.bukkit.block.data.type.WallSign.class);
    when(wallSign.getFacing()).thenReturn(BlockFace.WEST);
    when(eastRelative.getBlockData()).thenReturn(wallSign);
    when(eastRelative.getRelative(BlockFace.EAST)).thenReturn(block);
    when(eastRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsTrueWhenSignAttachedToEastAndPlayerIsNotOwner() {
    Location attachedBlockLocation = new Location(world, 1, 0, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);

    org.bukkit.block.data.type.WallSign wallSign = mock(org.bukkit.block.data.type.WallSign.class);
    when(wallSign.getFacing()).thenReturn(BlockFace.WEST);
    when(eastRelative.getBlockData()).thenReturn(wallSign);
    when(eastRelative.getRelative(BlockFace.EAST)).thenReturn(block);
    when(eastRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(99, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("disallow")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenSignAttachedToSouthAndPlayerIsOwner() {
    Location attachedBlockLocation = new Location(world, 0, 0, 1);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.WallSign wallSign = mock(org.bukkit.block.data.type.WallSign.class);
    when(wallSign.getFacing()).thenReturn(BlockFace.NORTH);
    when(southRelative.getBlockData()).thenReturn(wallSign);
    when(southRelative.getRelative(BlockFace.SOUTH)).thenReturn(block);
    when(southRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenSignAttachedToNorthAndPlayerIsOwner() {
    Location attachedBlockLocation = new Location(world, 0, 0, -1);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.WallSign wallSign = mock(org.bukkit.block.data.type.WallSign.class);
    when(wallSign.getFacing()).thenReturn(BlockFace.SOUTH);
    when(northRelative.getBlockData()).thenReturn(wallSign);
    when(northRelative.getRelative(BlockFace.NORTH)).thenReturn(block);
    when(northRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenSignAttachedToWestAndPlayerIsOwner() {
    Location attachedBlockLocation = new Location(world, -1, 0, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.WallSign wallSign = mock(org.bukkit.block.data.type.WallSign.class);
    when(wallSign.getFacing()).thenReturn(BlockFace.EAST);
    when(westRelative.getBlockData()).thenReturn(wallSign);
    when(westRelative.getRelative(BlockFace.WEST)).thenReturn(block);
    when(westRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenSignAttachedAboveAndPlayerIsOwner() {
    Location attachedBlockLocation = new Location(world, 0, 1, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);
    when(westRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.Sign standingSign = mock(org.bukkit.block.data.type.Sign.class);
    when(upRelative.getBlockData()).thenReturn(standingSign);
    when(upRelative.getRelative(BlockFace.DOWN)).thenReturn(block);
    when(upRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);
    when(block.getRelative(BlockFace.UP)).thenReturn(upRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsTrueWhenSignAttachedAboveAndPlayerIsNotOwner() {
    Location attachedBlockLocation = new Location(world, 0, 1, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);
    when(westRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.Sign standingSign = mock(org.bukkit.block.data.type.Sign.class);
    when(upRelative.getBlockData()).thenReturn(standingSign);
    when(upRelative.getRelative(BlockFace.DOWN)).thenReturn(block);
    when(upRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);
    when(block.getRelative(BlockFace.UP)).thenReturn(upRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(99, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("disallow")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenSignAttachedAboveAndNoProtectionExists() {
    Location attachedBlockLocation = new Location(world, 0, 1, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);
    when(westRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.Sign standingSign = mock(org.bukkit.block.data.type.Sign.class);
    when(upRelative.getBlockData()).thenReturn(standingSign);
    when(upRelative.getRelative(BlockFace.DOWN)).thenReturn(block);
    when(upRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);
    when(block.getRelative(BlockFace.UP)).thenReturn(upRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(null);

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

    try (var mockedStatic = org.mockito.Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.class)) {
      mockedStatic.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.hasPermission(eastNeighbour, player)).thenReturn(true);

      PlayerEntry playerEntry = buildPlayerEntry();
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

      when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(null);
      when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");
      when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY)).thenReturn("chest deny");

      boolean result = protectionActionService.protectBlock(player, block);

      assertAll(
          () -> assertFalse(result),
          () -> verify(player).sendMessage("disallow"),
          () -> verify(player).sendMessage("chest deny")
      );
    }
  }

  @Test
  void protectBlockReturnsTrueWhenDoubleChestFacingNorthAndNeighboursAreNotProtected() {
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

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    boolean result = protectionActionService.protectBlock(player, block);

    assertTrue(result);
  }

  @Test
  void protectBlockReturnsTrueWhenDoubleChestFacingEastAndNeighboursAreNotProtected() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.EAST);
    when(block.getBlockData()).thenReturn(chestData);

    Block southNeighbour = mock(Block.class);
    Block northNeighbour = mock(Block.class);

    when(southNeighbour.getBlockData()).thenReturn(mock(BlockData.class));
    when(northNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southNeighbour);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northNeighbour);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    boolean result = protectionActionService.protectBlock(player, block);

    assertTrue(result);
  }

  @Test
  void protectBlockReturnsTrueWhenDoubleChestFacingSouthAndNeighboursAreNotProtected() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.SOUTH);
    when(block.getBlockData()).thenReturn(chestData);

    Block westNeighbour = mock(Block.class);
    Block eastNeighbour = mock(Block.class);

    when(westNeighbour.getBlockData()).thenReturn(mock(BlockData.class));
    when(eastNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    when(block.getRelative(BlockFace.WEST)).thenReturn(westNeighbour);
    when(block.getRelative(BlockFace.EAST)).thenReturn(eastNeighbour);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    boolean result = protectionActionService.protectBlock(player, block);

    assertTrue(result);
  }

  @Test
  void protectBlockReturnsTrueWhenDoubleChestFacingWestAndNeighboursAreNotProtected() {
    Location location = new Location(world, 0, 0, 0);
    when(block.getType()).thenReturn(Material.CHEST);
    when(protectionService.isProtectableMaterial(Material.CHEST)).thenReturn(true);
    when(block.getLocation()).thenReturn(location);

    Chest chestData = mock(Chest.class);
    when(chestData.getType()).thenReturn(Chest.Type.LEFT);
    when(chestData.getFacing()).thenReturn(BlockFace.WEST);
    when(block.getBlockData()).thenReturn(chestData);

    Block northNeighbour = mock(Block.class);
    Block southNeighbour = mock(Block.class);

    when(northNeighbour.getBlockData()).thenReturn(mock(BlockData.class));
    when(southNeighbour.getBlockData()).thenReturn(mock(BlockData.class));

    when(block.getRelative(BlockFace.NORTH)).thenReturn(northNeighbour);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southNeighbour);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry builtEntry = buildLocationEntry(1, location);
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

    boolean result = protectionActionService.protectBlock(player, block);

    assertTrue(result);
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
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION)).thenReturn(existingLocationEntry);

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
    when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
        .thenReturn(null)
        .thenReturn(builtEntry);

    when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

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

    try (var mockedStatic = org.mockito.Mockito.mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.class)) {
      mockedStatic.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper.hasPermission(eastNeighbour, player)).thenReturn(false);

      PlayerEntry playerEntry = buildPlayerEntry();
      when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

      LocationEntry builtEntry = buildLocationEntry(1, location);
      when(locationService.findByLocationAndType(location, LocationType.PROTECTION))
          .thenReturn(null)
          .thenReturn(builtEntry);

      when(locationService.buildLocationEntry(eq(location), eq(null), eq(LocationType.PROTECTION), eq(1))).thenReturn(builtEntry);
      when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD)).thenReturn("protected");

      boolean result = protectionActionService.protectBlock(player, block);

      assertTrue(result);
    }
  }


  @Test
  void removeProtectionFromBlockReturnsFalseWhenWallHangingSignAttachedToEastAndPlayerIsOwner() {
    Location attachedBlockLocation = new Location(world, 1, 0, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);

    org.bukkit.block.data.type.WallHangingSign wallHangingSign = mock(org.bukkit.block.data.type.WallHangingSign.class);
    when(wallHangingSign.getFacing()).thenReturn(BlockFace.WEST);
    when(eastRelative.getBlockData()).thenReturn(wallHangingSign);
    when(eastRelative.getRelative(BlockFace.EAST)).thenReturn(block);
    when(eastRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsTrueWhenWallHangingSignAttachedToEastAndPlayerIsNotOwner() {
    Location attachedBlockLocation = new Location(world, 1, 0, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);

    org.bukkit.block.data.type.WallHangingSign wallHangingSign = mock(org.bukkit.block.data.type.WallHangingSign.class);
    when(wallHangingSign.getFacing()).thenReturn(BlockFace.WEST);
    when(eastRelative.getBlockData()).thenReturn(wallHangingSign);
    when(eastRelative.getRelative(BlockFace.EAST)).thenReturn(block);
    when(eastRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(99, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("disallow")
    );
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

    org.bukkit.block.data.type.WallHangingSign wallHangingSign = mock(org.bukkit.block.data.type.WallHangingSign.class);
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
  void removeProtectionFromBlockReturnsFalseWhenDoorAttachedAboveAndPlayerIsOwner() {
    Location attachedBlockLocation = new Location(world, 0, 1, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);
    when(westRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.Door door = mock(org.bukkit.block.data.type.Door.class);
    when(upRelative.getBlockData()).thenReturn(door);
    when(upRelative.getRelative(BlockFace.DOWN)).thenReturn(block);
    when(upRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);
    when(block.getRelative(BlockFace.UP)).thenReturn(upRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(1, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE)).thenReturn("removed");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertFalse(result),
        () -> verify(protectionService).deleteProtectionAndRemoveFromRegistry(protectionEntry),
        () -> verify(player).sendMessage("removed")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsTrueWhenDoorAttachedAboveAndPlayerIsNotOwner() {
    Location attachedBlockLocation = new Location(world, 0, 1, 0);
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);
    when(westRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.Door door = mock(org.bukkit.block.data.type.Door.class);
    when(upRelative.getBlockData()).thenReturn(door);
    when(upRelative.getRelative(BlockFace.DOWN)).thenReturn(block);
    when(upRelative.getLocation()).thenReturn(attachedBlockLocation);

    when(block.getRelative(BlockFace.EAST)).thenReturn(eastRelative);
    when(block.getRelative(BlockFace.SOUTH)).thenReturn(southRelative);
    when(block.getRelative(BlockFace.NORTH)).thenReturn(northRelative);
    when(block.getRelative(BlockFace.WEST)).thenReturn(westRelative);
    when(block.getRelative(BlockFace.UP)).thenReturn(upRelative);

    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    LocationEntry locationEntry = buildLocationEntry(99, attachedBlockLocation);
    ProtectionEntry protectionEntry = buildProtectionEntry(locationEntry);
    when(protectionService.getProtectionEntry(attachedBlockLocation)).thenReturn(protectionEntry);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW)).thenReturn("disallow");

    boolean result = protectionActionService.removeProtectionFromBlock(player, block);

    assertAll(
        () -> assertTrue(result),
        () -> verify(player).sendMessage("disallow")
    );
  }

  @Test
  void removeProtectionFromBlockReturnsFalseWhenDoorBlockDownDoesNotPointToBlock() {
    when(block.getType()).thenReturn(Material.DIRT);
    when(protectionService.isProtectableMaterial(Material.DIRT)).thenReturn(false);

    Block eastRelative = mock(Block.class);
    Block southRelative = mock(Block.class);
    Block northRelative = mock(Block.class);
    Block westRelative = mock(Block.class);
    Block upRelative = mock(Block.class);
    Block differentBlock = mock(Block.class);

    BlockData nonMatchingData = mock(BlockData.class);
    when(eastRelative.getBlockData()).thenReturn(nonMatchingData);
    when(southRelative.getBlockData()).thenReturn(nonMatchingData);
    when(northRelative.getBlockData()).thenReturn(nonMatchingData);
    when(westRelative.getBlockData()).thenReturn(nonMatchingData);

    org.bukkit.block.data.type.Door door = mock(org.bukkit.block.data.type.Door.class);
    when(upRelative.getBlockData()).thenReturn(door);
    when(upRelative.getRelative(BlockFace.DOWN)).thenReturn(differentBlock);

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
}