package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_PROTECT_RIGHTS;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ProtectionEntry;
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

public class ProtectionActionHelper {

  public static boolean removeProtectionFromBlock(Player p, Block b) {
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);
    if (RelluEssentials.getInstance().getProtectionRegistry().isProtectableMaterial(b.getType())) {
      Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
      ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(l);
      if (bpe != null) {
        if (bpe.getLocationEntry().getPlayerId() != pe.getId()) {
          p.sendMessage(
              languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
          return true;
        } else {
          RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
          RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(b.getLocation());
          p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
          return false;
        }
      }
    } else if (isAttachedToBlock(b, BlockFace.EAST)) {
      Location l = b.getRelative(BlockFace.EAST).getLocation();
      ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(l);
      if (bpe != null && bpe.getLocationEntry() != null
          && bpe.getLocationEntry().getPlayerId() == pe.getId()) {
        RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
        RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(b.getLocation());
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
        return false;
      } else {
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        return true;
      }

    } else if (isAttachedToBlock(b, BlockFace.SOUTH)) {
      Location l = b.getRelative(BlockFace.SOUTH).getLocation();
      ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(l);
      if (bpe != null && bpe.getLocationEntry() != null
          && bpe.getLocationEntry().getPlayerId() == pe.getId()) {
        RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
        RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(b.getLocation());
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
        return false;
      } else {
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        return true;
      }
    } else if (isAttachedToBlock(b, BlockFace.NORTH)) {
      Location l = b.getRelative(BlockFace.NORTH).getLocation();
      ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(l);
      if (bpe != null && bpe.getLocationEntry() != null
          && bpe.getLocationEntry().getPlayerId() == pe.getId()) {
        RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
        RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(b.getLocation());
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
        return false;
      } else {
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        return true;
      }
    } else if (isAttachedToBlock(b, BlockFace.WEST)) {
      Location l = b.getRelative(BlockFace.WEST).getLocation();
      ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(l);
      if (bpe != null && bpe.getLocationEntry() != null
          && bpe.getLocationEntry().getPlayerId() == pe.getId()) {
        RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
        RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(b.getLocation());
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
        return false;
      } else {
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
        return true;
      }
    } else if (isAttachedToBlock(b, BlockFace.UP)) {
      Location l = b.getRelative(BlockFace.UP).getLocation();
      ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionRegistry().getProtectionEntry(l);
      if (bpe != null && bpe.getLocationEntry() != null
          && bpe.getLocationEntry().getPlayerId() == pe.getId()) {
        RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
        RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(b.getLocation());
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE));
        return false;
      } else {
        if (bpe != null && bpe.getLocationEntry() != null) {
          p.sendMessage(
              languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
          return true;
        }
      }
    }
    return false;
  }

  public static boolean protectBlock(Player p, Block b) {
    if (RelluEssentials.getInstance().getProtectionRegistry().isProtectableMaterial(b.getType())) {
      PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);
      ProtectionEntry bpe = new ProtectionEntry();
      LocationEntry l = RelluEssentials.getInstance().getDatabaseHelper()
          .getLocation(b.getLocation(), 5);

      boolean hasRights = true;

      if (b.getBlockData() instanceof Chest cd) {

        if (!cd.getType().equals(Chest.Type.SINGLE)) {
          Block b2;
          Block b3 = switch (cd.getFacing()) {
            case NORTH -> {
              b2 = b.getRelative(BlockFace.EAST);
              yield b.getRelative(BlockFace.WEST);
            }
            case EAST -> {
              b2 = b.getRelative(BlockFace.SOUTH);
              yield b.getRelative(BlockFace.NORTH);
            }
            case SOUTH -> {
              b2 = b.getRelative(BlockFace.WEST);
              yield b.getRelative(BlockFace.EAST);
            }
            default -> {
              b2 = b.getRelative(BlockFace.NORTH);
              yield b.getRelative(BlockFace.SOUTH);
            }
          };

          if (b2.getBlockData() instanceof Chest cd2) {
            if (cd2.getFacing().equals(cd.getFacing())) {
              if (ProtectionHelper.hasPermission(b2, p)) {
                hasRights = false;
                p.sendMessage(
                    languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
                p.sendMessage(languageHelper.getWithPrefix(
                    MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY));
              }
            }
          } else if (b3.getBlockData() instanceof Chest cd3) {
            if (cd3.getFacing().equals(cd.getFacing())) {
              if (ProtectionHelper.hasPermission(b3, p)) {
                hasRights = false;
                p.sendMessage(
                    languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_DISALLOW));
                p.sendMessage(languageHelper.getWithPrefix(
                    MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY));
              }
            }
          }
        }
      }

      if (l == null && hasRights) {
        l = new LocationEntry();
        l.setLocation(b.getLocation());
        LocationTypeEntry lt = new LocationTypeEntry();
        lt.setId(5);
        l.setLocationType(lt);
        l.setPlayerId(pe.getId());

        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_ADD));

        RelluEssentials.getInstance().getDatabaseHelper().insertLocation(l);

        LocationEntry le = RelluEssentials.getInstance().getDatabaseHelper()
            .getLocation(b.getLocation(), 5);

        bpe.setCreatedBy(pe.getId());
        bpe.setMaterialName(b.getType().name());
        bpe.setLocationEntry(le);

        int playerPartnerFK = -1;

        if (pe.getPartner() != null) {
          if (pe.getId() != pe.getPartner().getFirstPartnerId()) {
            playerPartnerFK = pe.getPartner().getFirstPartnerId();
          } else {
            playerPartnerFK = pe.getPartner().getSecondPartnerId();
          }

        }

        int rightLength = 1;

        if (playerPartnerFK != -1) {
          rightLength = 2;
        }

        JSONObject rights = new JSONObject();
        int[] right = new int[rightLength];
        right[0] = pe.getId();

        if (playerPartnerFK != -1) {
          right[1] = playerPartnerFK;
        }

        rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, right);
        bpe.setRights(rights);

        JSONObject flags = new JSONObject();
        if (b.getType().equals(Material.LEVER) || b.getType().equals(Material.IRON_DOOR)) {
          JSONArray flagArray = new JSONArray();
          flagArray.put(ProtectionFlags.ALLOW_REDSTONE.name());
          flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flagArray);
        }
        bpe.setFlags(flags);

        RelluEssentials.getInstance().getDatabaseHelper().insertProtection(bpe);
        RelluEssentials.getInstance().getProtectionRegistry().putProtectionEntry(b.getLocation(),
            RelluEssentials.getInstance().getDatabaseHelper()
                .getProtectionByLocation(b.getLocation()));
        return true;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  public static void addRight(Player p, @NotNull ProtectionEntry pre, int id, boolean silent) {
    Location l = pre.getLocationEntry().getLocation();
    if (pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)) {
      JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);

      List<Object> list = rightJSON.toList();

      if (!list.contains(id)) {
        list.add(id);

        JSONObject rights = new JSONObject();
        rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, list);
        pre.setRights(rights);
        if (!silent) {
          p.sendMessage(
              languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD));
        }

        RelluEssentials.getInstance().getDatabaseHelper().updateProtectionRight(pre);
        RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(l);
        RelluEssentials.getInstance().getProtectionRegistry().putProtectionEntry(l, pre);
      } else {
        if (!silent) {
          p.sendMessage(
              languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED));
        }
      }
    }
  }

  public static void removeRight(Player p, @NotNull ProtectionEntry pre, int id, boolean silent) {
    Location l = pre.getLocationEntry().getLocation();
    if (pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)) {
      JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);

      List<Object> list = rightJSON.toList();

      if (list.contains(id)) {
        list.remove((Object) id);

        JSONObject rights = new JSONObject();
        rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, list);
        pre.setRights(rights);

        RelluEssentials.getInstance().getDatabaseHelper().updateProtectionRight(pre);
        RelluEssentials.getInstance().getProtectionRegistry().removeProtectionEntry(l);
        RelluEssentials.getInstance().getProtectionRegistry().putProtectionEntry(l, pre);

        if (!silent) {
          p.sendMessage(
              languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE));
        }
      } else {
        if (!silent) {
          p.sendMessage(languageHelper.getWithPrefix(
              MessageKey.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED));
        }
      }
    }
  }

  public static void removeRight(ProtectionEntry pre, int id) {
    removeRight(null, pre, id, true);
  }

  private static boolean isAttachedToBlock(@NotNull Block b, BlockFace face) {
    Block attachedBlock = b.getRelative(face);
    BlockData bd = attachedBlock.getBlockData();

    switch (bd) {
      case WallHangingSign _ -> {
        WallHangingSign sign = (WallHangingSign) attachedBlock.getBlockData();
        return attachedBlock.getRelative(sign.getFacing().getOppositeFace()).equals(b);
      }
      case WallSign _ -> {
        WallSign sign = (WallSign) attachedBlock.getBlockData();
        return attachedBlock.getRelative(sign.getFacing().getOppositeFace()).equals(b);
      }
      case Sign _, Door _ -> {
        return attachedBlock.getRelative(BlockFace.DOWN).equals(b);
      }
      default -> {
        return false;
      }
    }


  }
}
