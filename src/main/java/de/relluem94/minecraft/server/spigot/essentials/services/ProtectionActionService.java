package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_RIGHTS;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.ProtectionRegistry;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProtectionActionService {

  private final TranslationService translationService;
  private final DatabaseHelper databaseHelper;
  private final ProtectionRegistry protectionRegistry;
  private final PlayerRegistry playerRegistry;

  public ProtectionActionService(
      TranslationService translationService,
      DatabaseHelper databaseHelper,
      ProtectionRegistry protectionRegistry,
      PlayerRegistry playerRegistry
  ) {
    this.translationService = translationService;
    this.databaseHelper = databaseHelper;
    this.protectionRegistry = protectionRegistry;
    this.playerRegistry = playerRegistry;
  }

  public boolean removeProtectionFromBlock(Player p, Block b) {
    PlayerEntry pe = playerRegistry.getPlayerEntry(p);
    if (protectionRegistry.isProtectableMaterial(b.getType())) {
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry bpe = protectionRegistry.getProtectionEntry(l);
      if (bpe != null) {
        if (bpe.getLocationEntry().getPlayerId() != pe.getId()) {
          p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
          return true;
        } else {
          databaseHelper.deleteProtection(bpe);
          protectionRegistry.removeProtectionEntry(b.getLocation());
          p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
          return false;
        }
      }
    } else if (isAttachedToBlock(b, BlockFace.EAST)) {
      return removeProtectionForAttachedBlock(p, b, BlockFace.EAST, pe);
    } else if (isAttachedToBlock(b, BlockFace.SOUTH)) {
      return removeProtectionForAttachedBlock(p, b, BlockFace.SOUTH, pe);
    } else if (isAttachedToBlock(b, BlockFace.NORTH)) {
      return removeProtectionForAttachedBlock(p, b, BlockFace.NORTH, pe);
    } else if (isAttachedToBlock(b, BlockFace.WEST)) {
      return removeProtectionForAttachedBlock(p, b, BlockFace.WEST, pe);
    } else if (isAttachedToBlock(b, BlockFace.UP)) {
      return removeProtectionForBlockAttachedAbove(p, b, pe);
    }
    return false;
  }

  private boolean removeProtectionForAttachedBlock(Player p, Block b, BlockFace face, PlayerEntry pe) {
    Location l = b.getRelative(face).getLocation();
    ProtectionEntry bpe = protectionRegistry.getProtectionEntry(l);
    if (bpe != null && bpe.getLocationEntry() != null
        && bpe.getLocationEntry().getPlayerId() == pe.getId()) {
      databaseHelper.deleteProtection(bpe);
      protectionRegistry.removeProtectionEntry(b.getLocation());
      p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
      return false;
    } else {
      p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
      return true;
    }
  }

  private boolean removeProtectionForBlockAttachedAbove(Player p, Block b, PlayerEntry pe) {
    Location l = b.getRelative(BlockFace.UP).getLocation();
    ProtectionEntry bpe = protectionRegistry.getProtectionEntry(l);
    if (bpe != null && bpe.getLocationEntry() != null
        && bpe.getLocationEntry().getPlayerId() == pe.getId()) {
      databaseHelper.deleteProtection(bpe);
      protectionRegistry.removeProtectionEntry(b.getLocation());
      p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
      return false;
    } else {
      if (bpe != null && bpe.getLocationEntry() != null) {
        p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        return true;
      }
    }
    return false;
  }

  public boolean protectBlock(Player p, Block b) {
    if (!protectionRegistry.isProtectableMaterial(b.getType())) {
      return true;
    }

    PlayerEntry pe = playerRegistry.getPlayerEntry(p);
    ProtectionEntry bpe = new ProtectionEntry();
    LocationEntry l = databaseHelper.getLocation(b.getLocation(), 5);

    boolean playerHasRightsToProtect = true;

    if (b.getBlockData() instanceof Chest cd && !cd.getType().equals(Chest.Type.SINGLE)) {
      playerHasRightsToProtect = resolveDoubleChestProtectionRights(p, b, cd);
    }

    if (l != null || !playerHasRightsToProtect) {
      return false;
    }

    l = buildLocationEntry(b, pe);
    databaseHelper.insertLocation(l);

    LocationEntry persistedLocationEntry = databaseHelper.getLocation(b.getLocation(), 5);

    bpe.setCreatedBy(pe.getId());
    bpe.setMaterialName(b.getType().name());
    bpe.setLocationEntry(persistedLocationEntry);
    bpe.setRights(buildRightsJson(pe));
    bpe.setFlags(buildFlagsJson(b));

    p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD));

    databaseHelper.insertProtection(bpe);
    protectionRegistry.putProtectionEntry(b.getLocation(),
        databaseHelper.getProtectionByLocation(b.getLocation()));
    return true;
  }

  private boolean resolveDoubleChestProtectionRights(Player p, Block b, Chest cd) {
    Block leftNeighbour;
    Block rightNeighbour = switch (cd.getFacing()) {
      case NORTH -> {
        leftNeighbour = b.getRelative(BlockFace.EAST);
        yield b.getRelative(BlockFace.WEST);
      }
      case EAST -> {
        leftNeighbour = b.getRelative(BlockFace.SOUTH);
        yield b.getRelative(BlockFace.NORTH);
      }
      case SOUTH -> {
        leftNeighbour = b.getRelative(BlockFace.WEST);
        yield b.getRelative(BlockFace.EAST);
      }
      default -> {
        leftNeighbour = b.getRelative(BlockFace.NORTH);
        yield b.getRelative(BlockFace.SOUTH);
      }
    };

    if (isNeighbourChestProtectedByOther(leftNeighbour, cd, p)
        || isNeighbourChestProtectedByOther(rightNeighbour, cd, p)) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
      p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY));
      return false;
    }
    return true;
  }

  private boolean isNeighbourChestProtectedByOther(Block neighbour, Chest originalChest, Player p) {
    if (neighbour.getBlockData() instanceof Chest neighbourChest) {
      if (neighbourChest.getFacing().equals(originalChest.getFacing())) {
        return ProtectionHelper.hasPermission(neighbour, p);
      }
    }
    return false;
  }

  private LocationEntry buildLocationEntry(Block b, PlayerEntry pe) {
    LocationEntry l = new LocationEntry();
    l.setLocation(b.getLocation());
    LocationTypeEntry lt = new LocationTypeEntry();
    lt.setId(5);
    l.setLocationType(lt);
    l.setPlayerId(pe.getId());
    return l;
  }

  private JSONObject buildRightsJson(PlayerEntry pe) {
    int partnerPlayerId = resolvePartnerPlayerId(pe);

    int[] rightHolderIds = partnerPlayerId != -1
        ? new int[]{pe.getId(), partnerPlayerId}
        : new int[]{pe.getId()};

    JSONObject rights = new JSONObject();
    rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, rightHolderIds);
    return rights;
  }

  private int resolvePartnerPlayerId(PlayerEntry pe) {
    if (pe.getPartner() == null) {
      return -1;
    }
    return pe.getId() != pe.getPartner().getFirstPartnerId()
        ? pe.getPartner().getFirstPartnerId()
        : pe.getPartner().getSecondPartnerId();
  }

  private JSONObject buildFlagsJson(Block b) {
    JSONObject flags = new JSONObject();
    if (b.getType().equals(Material.LEVER) || b.getType().equals(Material.IRON_DOOR)) {
      JSONArray flagArray = new JSONArray();
      flagArray.put(ProtectionFlags.ALLOW_REDSTONE.name());
      flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flagArray);
    }
    return flags;
  }

  public void addRight(Player p, @NotNull ProtectionEntry pre, int id, boolean silent) {
    Location l = pre.getLocationEntry().getLocation();
    if (!pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)) {
      return;
    }

    JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);
    List<Object> list = rightJSON.toList();

    if (!list.contains(id)) {
      list.add(id);
      pre.setRights(new JSONObject().put(PLUGIN_EVENT_PROTECT_RIGHTS, list));
      databaseHelper.updateProtectionRight(pre);
      protectionRegistry.removeProtectionEntry(l);
      protectionRegistry.putProtectionEntry(l, pre);
      if (!silent) {
        p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD));
      }
    } else {
      if (!silent) {
        p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED));
      }
    }
  }

  public void removeRight(Player p, @NotNull ProtectionEntry pre, int id, boolean silent) {
    Location l = pre.getLocationEntry().getLocation();
    if (!pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)) {
      return;
    }

    JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);
    List<Object> list = rightJSON.toList();

    if (list.contains(id)) {
      list.remove((Object) id);
      pre.setRights(new JSONObject().put(PLUGIN_EVENT_PROTECT_RIGHTS, list));
      databaseHelper.updateProtectionRight(pre);
      protectionRegistry.removeProtectionEntry(l);
      protectionRegistry.putProtectionEntry(l, pre);
      if (!silent) {
        p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE));
      }
    } else {
      if (!silent) {
        p.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED));
      }
    }
  }

  public void removeRight(ProtectionEntry pre, int id) {
    removeRight(null, pre, id, true);
  }

  private boolean isAttachedToBlock(@NotNull Block b, BlockFace face) {
    Block attachedBlock = b.getRelative(face);
    BlockData bd = attachedBlock.getBlockData();

    return switch (bd) {
      case WallHangingSign _ -> {
        WallHangingSign sign = (WallHangingSign) attachedBlock.getBlockData();
        yield attachedBlock.getRelative(sign.getFacing().getOppositeFace()).equals(b);
      }
      case WallSign _ -> {
        WallSign sign = (WallSign) attachedBlock.getBlockData();
        yield attachedBlock.getRelative(sign.getFacing().getOppositeFace()).equals(b);
      }
      case Sign _, Door _ -> attachedBlock.getRelative(BlockFace.DOWN).equals(b);
      default -> false;
    };
  }
}