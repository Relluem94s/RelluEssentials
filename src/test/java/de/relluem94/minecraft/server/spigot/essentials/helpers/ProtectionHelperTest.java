package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_RIGHTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProtectionHelperTest {

  @Mock
  private ProtectionEntry protectionEntry;

  @Mock
  private Block block;

  @Mock
  private Door door;

  @Mock
  private World world;

  @BeforeEach
  void setUp() {
  }

  @Test
  void constructorThrowsIllegalStateException() throws Exception {
    Constructor<ProtectionHelper> constructor = ProtectionHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  void isOpenAbleReturnsTrueWhenBlockDataIsOpenable() {
    Openable openable = mock(Openable.class);
    when(block.getBlockData()).thenReturn(openable);
    assertTrue(ProtectionHelper.isOpenAble(block));
  }

  @Test
  void isOpenAbleReturnsFalseWhenBlockDataIsNotOpenable() {
    when(block.getBlockData()).thenReturn(mock(org.bukkit.block.data.BlockData.class));
    assertFalse(ProtectionHelper.isOpenAble(block));
  }

  @Test
  void hasFlagReturnsTrueWhenFlagStoredAsJSONArray() {
    JSONArray flagArray = new JSONArray();
    flagArray.put(ProtectionFlags.AUTO_CLOSE.name());
    JSONObject flags = new JSONObject();
    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flagArray);
    when(protectionEntry.getFlags()).thenReturn(flags);

    assertTrue(ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE));
  }

  @Test
  void hasFlagReturnsTrueWhenFlagStoredAsString() {
    JSONObject flags = new JSONObject();
    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, ProtectionFlags.AUTO_CLOSE.name());
    when(protectionEntry.getFlags()).thenReturn(flags);

    assertTrue(ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE));
  }

  @Test
  void hasFlagReturnsFalseWhenFlagsAreEmpty() {
    when(protectionEntry.getFlags()).thenReturn(new JSONObject());
    assertFalse(ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE));
  }

  @Test
  void hasFlagReturnsFalseWhenFlagKeyMissing() {
    JSONObject flags = new JSONObject();
    flags.put("otherKey", "someValue");
    when(protectionEntry.getFlags()).thenReturn(flags);

    assertFalse(ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.AUTO_CLOSE));
  }

  @Test
  void hasFlagReturnsFalseWhenFlagNotInArray() {
    JSONArray flagArray = new JSONArray();
    flagArray.put(ProtectionFlags.AUTO_CLOSE.name());
    JSONObject flags = new JSONObject();
    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flagArray);
    when(protectionEntry.getFlags()).thenReturn(flags);

    assertFalse(ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.ALLOW_PUBLIC));
  }

  @Test
  void hasFlagReturnsFalseWhenStringFlagDoesNotMatch() {
    JSONObject flags = new JSONObject();
    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, ProtectionFlags.AUTO_CLOSE.name());
    when(protectionEntry.getFlags()).thenReturn(flags);

    assertFalse(ProtectionHelper.hasFlag(protectionEntry, ProtectionFlags.ALLOW_PUBLIC));
  }

  @Test
  void hasRightsReturnsTrueWhenRightsAreEmpty() {
    when(protectionEntry.getRights()).thenReturn(new JSONObject());
    assertTrue(ProtectionHelper.hasRights(protectionEntry, 1));
  }

  @Test
  void hasRightsReturnsTrueWhenRightsKeyMissing() {
    JSONObject rights = new JSONObject();
    rights.put("otherKey", new JSONArray());
    when(protectionEntry.getRights()).thenReturn(rights);

    assertTrue(ProtectionHelper.hasRights(protectionEntry, 1));
  }

  @Test
  void hasRightsReturnsTrueWhenPlayerIdNotInRightsList() {
    JSONArray rightsArray = new JSONArray();
    rightsArray.put(2);
    rightsArray.put(3);
    JSONObject rights = new JSONObject();
    rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, rightsArray);
    when(protectionEntry.getRights()).thenReturn(rights);

    assertTrue(ProtectionHelper.hasRights(protectionEntry, 1));
  }

  @Test
  void hasRightsReturnsFalseWhenPlayerIdIsInRightsList() {
    JSONArray rightsArray = new JSONArray();
    rightsArray.put(1);
    JSONObject rights = new JSONObject();
    rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, rightsArray);
    when(protectionEntry.getRights()).thenReturn(rights);

    assertFalse(ProtectionHelper.hasRights(protectionEntry, 1));
  }

  @Test
  void isOwnerReturnsTrueWhenPlayerIdMatchesCreatedBy() {
    when(protectionEntry.getCreatedBy()).thenReturn(42);
    assertTrue(ProtectionHelper.isOwner(protectionEntry, 42));
  }

  @Test
  void isOwnerReturnsFalseWhenPlayerIdDoesNotMatchCreatedBy() {
    when(protectionEntry.getCreatedBy()).thenReturn(42);
    assertFalse(ProtectionHelper.isOwner(protectionEntry, 99));
  }

  @Test
  void getLocationFromBlockAlternateForDoorReturnsNullWhenBlockIsNull() {
    assertNull(ProtectionHelper.getLocationFromBlockAlternateForDoor(null));
  }

  @Test
  void getLocationFromBlockAlternateForDoorReturnsLocationWhenBlockIsNotOpenable() {
    Location location = new Location(world, 1, 2, 3);
    when(block.getLocation()).thenReturn(location);
    when(block.getBlockData()).thenReturn(mock(org.bukkit.block.data.BlockData.class));

    Location result = ProtectionHelper.getLocationFromBlockAlternateForDoor(block);

    assertNotNull(result);
    assertEquals(1, result.getX());
    assertEquals(2, result.getY());
    assertEquals(3, result.getZ());
  }

  @Test
  void getLocationFromBlockAlternateForDoorReturnsLocationWhenOpenableIsNotDoor() {
    Location location = new Location(world, 1, 2, 3);
    when(block.getLocation()).thenReturn(location);
    Openable nonDoorOpenable = mock(Openable.class);
    when(block.getBlockData()).thenReturn(nonDoorOpenable);

    Location result = ProtectionHelper.getLocationFromBlockAlternateForDoor(block);

    assertNotNull(result);
    assertEquals(1, result.getX());
    assertEquals(2, result.getY());
    assertEquals(3, result.getZ());
  }

  @Test
  void getLocationFromBlockAlternateForDoorAdjustsYWhenDoorIsTopHalf() {
    Location location = new Location(world, 1, 2, 3);
    when(block.getLocation()).thenReturn(location);
    when(door.getHalf()).thenReturn(Half.TOP);
    when(block.getBlockData()).thenReturn(door);

    Location result = ProtectionHelper.getLocationFromBlockAlternateForDoor(block);

    assertNotNull(result);
    assertEquals(1, result.getX());
    assertEquals(1, result.getY());
    assertEquals(3, result.getZ());
  }

  @Test
  void getLocationFromBlockAlternateForDoorDoesNotAdjustYWhenDoorIsBottomHalf() {
    Location location = new Location(world, 1, 2, 3);
    when(block.getLocation()).thenReturn(location);
    when(door.getHalf()).thenReturn(Half.BOTTOM);
    when(block.getBlockData()).thenReturn(door);

    Location result = ProtectionHelper.getLocationFromBlockAlternateForDoor(block);

    assertNotNull(result);
    assertEquals(1, result.getX());
    assertEquals(2, result.getY());
    assertEquals(3, result.getZ());
  }

  @Test
  void getOtherPartReturnsNullWhenAdjacentBlockIsNotOpenable() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.EAST);
    when(door.getHinge()).thenReturn(Hinge.LEFT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(mock(org.bukkit.block.data.BlockData.class));

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNull(result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenOpenableAndFacingEastHingeLeft() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.EAST);
    when(door.getHinge()).thenReturn(Hinge.LEFT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenFacingEastHingeRight() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.EAST);
    when(door.getHinge()).thenReturn(Hinge.RIGHT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenFacingWestHingeLeft() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.WEST);
    when(door.getHinge()).thenReturn(Hinge.LEFT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenFacingWestHingeRight() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.WEST);
    when(door.getHinge()).thenReturn(Hinge.RIGHT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenFacingSouthHingeLeft() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.SOUTH);
    when(door.getHinge()).thenReturn(Hinge.LEFT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenFacingSouthHingeRight() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.SOUTH);
    when(door.getHinge()).thenReturn(Hinge.RIGHT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenFacingNorthHingeLeft() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.NORTH);
    when(door.getHinge()).thenReturn(Hinge.LEFT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartReturnsAdjacentBlockWhenFacingNorthHingeRight() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(door.getFacing()).thenReturn(BlockFace.NORTH);
    when(door.getHinge()).thenReturn(Hinge.RIGHT);
    when(world.getBlockAt(any(Location.class))).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(door, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }

  @Test
  void getOtherPartWithNullDoorReturnsBlockAtOriginalLocation() {
    Location location = new Location(world, 0, 0, 0);
    Block adjacentBlock = mock(Block.class);
    Openable adjacentOpenable = mock(Openable.class);

    when(block.getLocation()).thenReturn(location);
    when(world.getBlockAt(location)).thenReturn(adjacentBlock);
    when(adjacentBlock.getBlockData()).thenReturn(adjacentOpenable);

    Block result = ProtectionHelper.getOtherPart(null, block);

    assertNotNull(result);
    assertEquals(adjacentBlock, result);
  }
}