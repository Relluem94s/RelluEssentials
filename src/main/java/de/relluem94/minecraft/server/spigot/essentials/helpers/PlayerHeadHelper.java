package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;

public class PlayerHeadHelper {
    private static final ItemStack PLAYER_HEAD = new ItemStack(Material.PLAYER_HEAD, 1);

    public static ItemStack createSkull(String name){
        OfflinePlayerEntry player = PlayerHelper.getOfflinePlayerByName(name);
        ItemStack is = PLAYER_HEAD.clone();
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        if(player != null){
            PlayerProfile pp = Bukkit.createPlayerProfile(player.getID(), player.getName());
 
            sm.setOwnerProfile(pp);
            sm.setDisplayName(player.getName());
            is.setItemMeta((ItemMeta) sm);
        }
        return is;
    }

    public static ItemStack getCustomSkull(CustomHeads ch) {
        ItemStack ph = PLAYER_HEAD.clone();
        if (ch.getBase64().isEmpty()){
            return ph;
        } 

        SkullMeta sm = (SkullMeta) ph.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), ch.getName());
        profile.getProperties().put("textures", new Property("textures", ch.getBase64()));

        PlayerProfile pp = Bukkit.createPlayerProfile(ch.getUUID(), ch.getName());
        sm.setOwnerProfile(pp);
        sm.setDisplayName(ch.getName());

        try {
            Method m = sm.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            m.setAccessible(true);
            m.invoke(sm, profile);
        }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }

        ph.setItemMeta(sm);
        return ph;
    }
}