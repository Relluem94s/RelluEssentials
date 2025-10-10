package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;
import java.util.Base64;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class PlayerHeadHelper {

    private PlayerHeadHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    private static final ItemStack PLAYER_HEAD = new ItemStack(Material.PLAYER_HEAD, 1);

    public static void createSkull(String name, org.bukkit.plugin.Plugin plugin, java.util.function.Consumer<org.bukkit.inventory.ItemStack> callback) {
        OfflinePlayerEntry player = PlayerHelper.getOfflinePlayerByName(name);
        final org.bukkit.inventory.ItemStack is = PLAYER_HEAD.clone();
        if (player == null) {
            callback.accept(is);
            return;
        }

        org.bukkit.profile.PlayerProfile profile = org.bukkit.Bukkit.createPlayerProfile(player.getId(), player.getName());
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
            PlayerProfile profile = Bukkit.createPlayerProfile(ch.getUUID(), ch.getName());
            PlayerTextures textures = profile.getTextures();

            String base64 = ch.getBase64();
            String jsonString = new String(Base64.getDecoder().decode(base64));
            String skinUrl = extractSkinUrlFromBase64(jsonString);

            if (skinUrl != null) {
                textures.setSkin(new URL(skinUrl));
                profile.setTextures(textures);
                sm.setOwnerProfile(profile);
                sm.setDisplayName(ch.getName());
            } else {
                Bukkit.getLogger().warning("Couldn't a Skin URL for head '" + ch.getName() + "' extrahieren.");
            }

            ph.setItemMeta(sm);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Couldn't create the custom head '" + ch.getName() + "': " + e.getMessage());
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