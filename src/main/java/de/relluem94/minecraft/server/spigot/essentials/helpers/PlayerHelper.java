package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_SPACER;

import java.util.Properties;
import java.util.UUID;

import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.NetworkUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 *
 * @author rellu
 */
public class PlayerHelper {

    /**
     *
     * @param p Player to set Flying
     */
    public static void setFlying(Player p) {
        if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            PlayerEntry pe = playerEntryList.get(p.getUniqueId());
            if (pe.isFlying()) {
                p.setAllowFlight(true);
                p.setFlying(true);
            }
        }
    }

    /**
     *
     * @param p Player
     * @param join Boolean
     * @return Boolean
     */
    public static boolean setAFK(Player p, boolean join) {
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
        boolean isAFK = pe.isAFK();

        if(pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)){
            isAFK = true;
        }
        else if(pe.getPlayerState().equals(PlayerState.FAKE_AFK_ON)){
            isAFK = false;
        }

        if (!join) {
            Bukkit.broadcastMessage(PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + String.format(getText(!isAFK ? "PLUGIN_COMMAND_AFK_ACTIVATED" : "PLUGIN_COMMAND_AFK_DEACTIVATED"), p.getCustomName() + "§f", !isAFK ? "§c" : "§a"));
            isAFK = !isAFK; // Invert for single invertion ^_^
        }

        if(pe.getPlayerState().equals(PlayerState.DEFAULT)){
            pe.setUpdatedBy(playerEntryList.get(p.getUniqueId()).getID());
            pe.setAFK(isAFK);
            dBH.updatePlayer(pe);
            p.setInvulnerable(isAFK);
        }
        
        p.setPlayerListName((isAFK ? "§c[AFK] " : "") + p.getCustomName());
        return true;
    }


    public static OfflinePlayerEntry getOfflinePlayerByName(String name){
        JSONObject json = NetworkUtils.getJSON("https://api.mojang.com/users/profiles/minecraft/" + name);
        OfflinePlayerEntry ope = new OfflinePlayerEntry();

        if(json.has("name")){
            String uuid = new StringBuilder(json.get("id") + "").insert(20, '-')
            .insert(16, '-')
            .insert(12, '-')
            .insert(8, '-')
            .toString();

            ope.setID(UUID.fromString(uuid));
            ope.setName(json.get("name").toString());
            return ope;
        }
        else{
            return null;
        }
    }

    public static OfflinePlayerEntry getOfflinePlayerByUUID(UUID uuid){
        JSONObject json = NetworkUtils.getJSON("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replaceAll("-", "") + "?unsigned=false");
        OfflinePlayerEntry ope = new OfflinePlayerEntry();

        if(json.has("name")){
           
            ope.setID(uuid);
            ope.setName(json.get("name").toString());

            Properties properties = new Properties();
            JSONObject props = json.getJSONObject("properties");
            properties.put("name", props.get("name"));
            properties.put("value", props.get("value"));
            properties.put("signature", props.get("signature"));

            ope.setProperties(properties);
            return ope;
        }
        else{
            return null;
        }
    }


    
}
