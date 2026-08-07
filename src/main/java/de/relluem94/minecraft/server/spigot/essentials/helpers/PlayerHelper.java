package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_GRAPPLINGHOOK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_WORLDSELECTOR;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import de.relluem94.rellulib.utils.NetworkUtils;
import java.util.Properties;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 *
 * @author rellu
 */
public class PlayerHelper {

  private PlayerHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }


  public static OfflinePlayerEntry getOfflinePlayerByName(String name) {
    JSONObject json = NetworkUtils.getJSON(
        "https://api.mojang.com/users/profiles/minecraft/" + name);
    OfflinePlayerEntry ope = new OfflinePlayerEntry();

    if (json.has("name")) {
      ope.setId(UuidHelper.dashed((String) json.get("id")));
      ope.setName(json.get("name").toString());
      return ope;
    } else {
      return null;
    }
  }

  @SuppressWarnings("unused")
  public static @Nullable OfflinePlayerEntry getOfflinePlayerByUUID(UUID uuid) {
    JSONObject json = NetworkUtils.getJSON(
        "https://sessionserver.mojang.com/session/minecraft/profile/" + UuidHelper.unDashed(uuid)
            + "?unsigned=false");
    OfflinePlayerEntry ope = new OfflinePlayerEntry();
    if (json.has("name")) {
      ope.setId(uuid);
      ope.setName(json.get("name").toString());

      Properties properties = new Properties();
      JSONObject props = json.getJSONArray("properties").getJSONObject(0);
      properties.put("name", props.get("name"));
      properties.put("value", props.get("value"));
      properties.put("signature", props.get("signature"));
      ope.setProperties(properties);
      return ope;
    } else {
      return null;
    }
  }

  public static @Nullable OfflinePlayer getOfflinePlayer(@NotNull String name) {
    for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
      if (name.equals(op.getName())) {
        return op;
      }
    }

    return null;
  }

  public static @Nullable Player getTargetedPlayer(@NotNull Location loc) {
    Player nearestPlayer = null;
    double lastDistance = Double.MAX_VALUE;
    World world = loc.getWorld();

    if (world == null) {
      return null;
    }

    for (Player p : world.getPlayers()) {
      double distanceSquared = loc.distanceSquared(p.getLocation());
      if (distanceSquared < lastDistance) {
        lastDistance = distanceSquared;
        nearestPlayer = p;
      }
    }
    return nearestPlayer;
  }

  public static void setLobbyItems(@NotNull Player p) {
    ItemHelper grapplingHookItem = ItemRegistry.find(
        RegistryKey.of(PLUGIN_ITEM_NAMESPACE_GRAPPLINGHOOK)).orElseThrow();
    ItemHelper worldSelectorItem = ItemRegistry.find(
        RegistryKey.of(PLUGIN_ITEM_NAMESPACE_WORLDSELECTOR)).orElseThrow();
    ItemHelper cloudSailorItem = ItemRegistry.find(
        RegistryKey.of(PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR)).orElseThrow();

    for (ItemStack i : p.getInventory().getContents()) {
      if (i == null) {
        continue;
      }

      if (i.isSimilar(grapplingHookItem.getCustomItem())) {
        p.getInventory().remove(i);
      }
      if (i.isSimilar(cloudSailorItem.getCustomItem())) {
        p.getInventory().remove(i);
      }
      if (i.isSimilar(worldSelectorItem.getCustomItem())) {
        p.getInventory().remove(i);
      }
    }

    p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
    p.getInventory().setItemInOffHand(null);

    p.getInventory().setItem(0, grapplingHookItem.getCustomItem());
    p.getInventory().setItem(1, cloudSailorItem.getCustomItem());
    p.getInventory().setItem(4, worldSelectorItem.getCustomItem());
  }

  public static @NotNull Location getLookingLocation(@NotNull Player player, double range) {
    RayTraceResult result = player.rayTraceBlocks(range, FluidCollisionMode.ALWAYS);
    if (result != null && result.getHitBlock() != null) {
      return result.getHitBlock().getLocation();
    }
    return player.getLocation();
  }

  public static @NotNull Vector getPlayerDirection(@NotNull Player p) {
    return getLocationDirection(p.getLocation());
  }

  public static @NotNull Vector getLocationDirection(@NotNull Location l) {
    Vector direction = l.getDirection();
    double verticalThreshold = 0.5;

    if (Math.abs(direction.getY()) > verticalThreshold) {
      return new Vector(0, Math.signum(direction.getY()), 0);
    }

    direction.setY(0);
    direction.normalize();

    double yaw = Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
    if (yaw >= -45 && yaw < 45) {
      return new Vector(0, 0, 1);
    } else if (yaw >= 45 && yaw < 135) {
      return new Vector(-1, 0, 0);
    } else if (yaw >= -135 && yaw < -45) {
      return new Vector(1, 0, 0);
    } else {
      return new Vector(0, 0, -1);
    }
  }
}