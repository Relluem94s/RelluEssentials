package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

public class DeathChestService {

  private static final List<BlockFace> HORIZONTAL_FACES = List.of(
      BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST
  );

  private final ServiceContext serviceContext;

  public DeathChestService(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  public boolean spawnDeathChestForPlayer(Player player) {
    ItemStack[] inventoryContents = player.getInventory().getContents();
    ItemStack[] armorContents = player.getInventory().getArmorContents();
    ItemStack[] offHandContents = player.getInventory().getExtraContents();

    boolean hasAnyItem = hasAnyNonNullItem(inventoryContents)
        || hasAnyNonNullItem(armorContents)
        || hasAnyNonNullItem(offHandContents);

    if (!hasAnyItem) {
      return false;
    }

    Location deathLocation = player.getLocation();
    Block[] chestBlocks = findTwoAdjacentAirBlocks(deathLocation);

    if (chestBlocks == null) {
      return false;
    }

    placeDoubleChest(chestBlocks[0], chestBlocks[1]);

    Inventory chestInventory = ((Chest) chestBlocks[0].getState()).getInventory();
    if (chestInventory.getHolder() instanceof DoubleChest doubleChest) {
      chestInventory = doubleChest.getInventory();
    }

    fillChestWithPlayerItems(chestInventory, inventoryContents, armorContents, offHandContents);
    clearPlayerInventory(player);

    protectBothChestBlocksForPlayer(chestBlocks[0], chestBlocks[1], player);

    return true;
  }

  private boolean hasAnyNonNullItem(ItemStack[] items) {
    return Arrays.stream(items).anyMatch(item -> item != null && item.getType() != Material.AIR);
  }

  private Block[] findTwoAdjacentAirBlocks(Location origin) {
    Block originBlock = origin.getBlock();

    for (int yOffset = 0; yOffset <= 3; yOffset++) {
      Block candidateBlock = originBlock.getRelative(0, yOffset, 0);
      for (BlockFace face : HORIZONTAL_FACES) {
        Block neighbor = candidateBlock.getRelative(face);
        if (candidateBlock.getType() == Material.AIR && neighbor.getType() == Material.AIR) {
          return new Block[]{candidateBlock, neighbor};
        }
      }
    }

    return null;
  }


  private void placeDoubleChest(Block firstBlock, Block secondBlock) {
    BlockFace facingDirection = determineFacingForDoubleChest(firstBlock, secondBlock);

    firstBlock.setType(Material.CHEST);
    org.bukkit.block.data.type.Chest firstChestData = (org.bukkit.block.data.type.Chest) firstBlock.getBlockData();
    firstChestData.setFacing(facingDirection);
    firstChestData.setType(Type.LEFT);
    firstBlock.setBlockData(firstChestData);

    secondBlock.setType(Material.CHEST);
    org.bukkit.block.data.type.Chest secondChestData = (org.bukkit.block.data.type.Chest) secondBlock.getBlockData();
    secondChestData.setFacing(facingDirection);
    secondChestData.setType(Type.RIGHT);
    secondBlock.setBlockData(secondChestData);
  }

  private BlockFace determineFacingForDoubleChest(Block firstBlock, Block secondBlock) {
    BlockFace connectionAxis = firstBlock.getFace(secondBlock);
    if (connectionAxis == BlockFace.NORTH || connectionAxis == BlockFace.SOUTH) {
      return BlockFace.WEST;
    }
    return BlockFace.SOUTH;
  }

  private void fillChestWithPlayerItems(Inventory chestInventory, ItemStack[] inventoryContents,
      ItemStack[] armorContents, ItemStack[] offHandContents) {
    for (ItemStack item : inventoryContents) {
      if (item != null && item.getType() != Material.AIR) {
        chestInventory.addItem(item);
      }
    }
    for (ItemStack item : armorContents) {
      if (item != null && item.getType() != Material.AIR) {
        chestInventory.addItem(item);
      }
    }
    for (ItemStack item : offHandContents) {
      if (item != null && item.getType() != Material.AIR) {
        chestInventory.addItem(item);
      }
    }
  }

  private void clearPlayerInventory(Player player) {
    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    player.getInventory().setExtraContents(null);
  }

  private void protectBothChestBlocksForPlayer(Block firstBlock, Block secondBlock, Player player) {
    protectChestBlockForPlayer(firstBlock, player);
    protectChestBlockForPlayer(secondBlock, player);
  }

  private void protectChestBlockForPlayer(Block block, Player player) {
    PlayerEntry playerEntry = serviceContext.getPlayerService().getPlayerEntry(player);
    Optional<LocationTypeEntry> locationTypeEntry = serviceContext.getLocationTypeService().findByName(LocationType.PROTECTION);

    if (locationTypeEntry.isEmpty()) {
      return;
    }

    LocationEntry locationEntry = new LocationEntry();
    locationEntry.setLocation(block.getLocation());
    locationEntry.setPlayerId(playerEntry.getId());
    locationEntry.setLocationType(locationTypeEntry.get());

    LocationEntry persistedLocationEntry = serviceContext.getLocationService().saveAndFetch(locationEntry);

    JSONObject rights = new JSONObject();
    rights.put("IDs", new JSONArray(List.of(playerEntry.getId())));

    ProtectionEntry protectionEntry = new ProtectionEntry();
    protectionEntry.setLocationEntry(persistedLocationEntry);
    protectionEntry.setMaterialName(block.getType().name());
    protectionEntry.setFlags(new org.json.JSONObject());
    protectionEntry.setRights(rights);

    serviceContext.getProtectionService().saveProtectionAndAddToRegistry(block.getLocation(), protectionEntry);
  }
}