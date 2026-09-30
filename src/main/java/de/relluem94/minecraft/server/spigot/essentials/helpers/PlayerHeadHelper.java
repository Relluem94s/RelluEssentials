package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.OfflinePlayerEntry;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Base64;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * Utility class for creating and modifying player head {@link ItemStack}s.
 *
 * <p>Supports both custom heads via Base64-encoded texture data and player-specific skulls
 * resolved from offline player profiles.
 *
 * @author rellu
 */
public class PlayerHeadHelper {

  private static final ItemStack PLAYER_HEAD = new ItemStack(Material.PLAYER_HEAD, 1);

  private PlayerHeadHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Returns a consumer that applies custom head textures to ItemMeta.
   *
   * @param ch The custom head data.
   * @return A consumer to be used in meta modifiers.
   */
  public static Consumer<ItemMeta> customHeadModifier(@NotNull CustomHeads ch) {
    return meta -> {
      if (!(meta instanceof SkullMeta skullMeta)) {
        return;
      }

      if (ch.getBase64().isEmpty()) {
        return;
      }

      try {
        String jsonString = new String(Base64.getDecoder().decode(ch.getBase64()));
        String skinUrl = extractSkinUrlFromBase64(jsonString);

        if (skinUrl != null) {
          PlayerProfile profile = Bukkit.createPlayerProfile(ch.getUuid());
          PlayerTextures textures = profile.getTextures();
          textures.setSkin(URI.create(skinUrl).toURL());
          profile.setTextures(textures);
          skullMeta.setOwnerProfile(profile);
          skullMeta.setDisplayName(ch.getName());
        }
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    };
  }

  /**
   * Asynchronously creates a player skull {@link ItemStack} for the given player name.
   *
   * <p>Resolves the player's profile and updates it asynchronously. The resulting item is
   * passed to the callback on the main thread once the profile update completes.
   * If the player cannot be found or the profile update fails, a fallback skull is used.
   *
   * @param name     The name of the player whose skull should be created.
   * @param plugin   The plugin instance used to schedule the callback on the main thread.
   * @param callback A consumer that receives the resulting skull {@link ItemStack}.
   */
  public static void createSkull(String name, org.bukkit.plugin.Plugin plugin,
      java.util.function.Consumer<org.bukkit.inventory.ItemStack> callback) {
    OfflinePlayerEntry player = PlayerHelper.getOfflinePlayerByName(name);
    final org.bukkit.inventory.ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
    if (player == null) {
      callback.accept(is);
      return;
    }

    org.bukkit.profile.PlayerProfile profile = org.bukkit.Bukkit.createPlayerProfile(player.getId(),
        player.getName());
    profile.update().whenComplete((updated, ex) -> Bukkit.getScheduler().runTask(plugin, () -> {
      SkullMeta sm = (SkullMeta) is.getItemMeta();
      if (sm == null) {
        callback.accept(is);
        return;
      }
      if (ex == null && updated != null && updated.isComplete()) {
        sm.setOwnerProfile(updated);
      } else {
        sm.setOwningPlayer(Bukkit.getOfflinePlayer(player.getId()));
      }
      sm.setDisplayName(player.getName());
      is.setItemMeta(sm);
      callback.accept(is);
    }));
  }

  /**
   * Creates and returns a player head {@link ItemStack} with the texture
   * defined by the given {@link CustomHeads} entry.
   *
   * <p>Decodes the Base64 texture data, extracts the skin URL, and applies it to the skull's
   * {@link org.bukkit.profile.PlayerProfile}. Returns a plain player head if the Base64 value
   * is empty or the skin URL cannot be extracted.
   *
   * @param ch The {@link CustomHeads} entry containing the texture data and display name.
   * @return An {@link ItemStack} representing the custom skull.
   * @throws RuntimeException If the extracted skin URL is malformed.
   */
  public static @NotNull ItemStack getCustomSkull(@NotNull CustomHeads ch) {
    ItemStack ph = PLAYER_HEAD.clone();
    if (ch.getBase64().isEmpty()) {
      return ph;
    }

    SkullMeta sm = (SkullMeta) ph.getItemMeta();
    if (sm == null) {
      return ph;
    }

    try {
      String jsonString = new String(Base64.getDecoder().decode(ch.getBase64()));
      String skinUrl = extractSkinUrlFromBase64(jsonString);

      if (skinUrl != null) {
        PlayerProfile profile = Bukkit.createPlayerProfile(ch.getUuid());
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(URI.create(skinUrl).toURL());
        profile.setTextures(textures);
        sm.setOwnerProfile(profile);
        sm.setDisplayName(ch.getName());
        ph.setItemMeta(sm);
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }

    return ph;
  }

  private static @Nullable String extractSkinUrlFromBase64(String jsonString) {
    try {
      JSONObject json = new JSONObject(jsonString);
      return json.getJSONObject("textures")
          .getJSONObject("SKIN")
          .getString("url");
    } catch (Exception e) {
      Bukkit.getLogger().warning("Couldn't in parsing the Base64 texture: " + e.getMessage());
      return null;
    }
  }
}