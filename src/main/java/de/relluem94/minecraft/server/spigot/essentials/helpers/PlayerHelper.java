package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_PREFIX;

import java.util.Properties;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.items.GrapplingHook;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.utils.NetworkUtils;

/**
 *
 * @author rellu
 */
public class PlayerHelper {

    private PlayerHelper() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    /**
     *
     * @param p Player to set Flying
     */
    public static void setFlying(Player p) {
        if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
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
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        boolean isAFK = pe.isAFK();

        if(pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)){
            isAFK = true;
        }
        else if(pe.getPlayerState().equals(PlayerState.FAKE_AFK_ON)){
            isAFK = false;
        }

        if (!join) {
            Bukkit.broadcastMessage(PLUGIN_NAME_PREFIX + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_COMMAND + String.format(getText(p.getLocale(), !isAFK ? "PLUGIN_COMMAND_AFK_ACTIVATED" : "PLUGIN_COMMAND_AFK_DEACTIVATED"), p.getCustomName() + "§f", !isAFK ? "§c" : "§a"));
            isAFK = !isAFK; // Invert for single invertion ^_^
        }

        if(pe.getPlayerState().equals(PlayerState.DEFAULT)){
            if(!join){
                pe.setUpdatedBy(pe.getID());
                pe.setAFK(isAFK);
                pe.setUpdatedBy(pe.getID());
                pe.setToBeUpdated(true);
            }
            p.setInvulnerable(isAFK);
        }
        
        p.setPlayerListName((isAFK ? "§c[AFK] " : "") + p.getCustomName());
        return true;
    }


    public static OfflinePlayerEntry getOfflinePlayerByName(String name){
        JSONObject json = NetworkUtils.getJSON("https://api.mojang.com/users/profiles/minecraft/" + name);
        OfflinePlayerEntry ope = new OfflinePlayerEntry();

        if(json.has("name")){
            ope.setID(UUIDHelper.dashed((String)json.get("id")));
            ope.setName(json.get("name").toString());
            return ope;
        }
        else{
            return null;
        }
    }

    public static OfflinePlayerEntry getOfflinePlayerByUUID(UUID uuid){
        JSONObject json = NetworkUtils.getJSON("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDHelper.unDashed(uuid) + "?unsigned=false");
        OfflinePlayerEntry ope = new OfflinePlayerEntry();
        if(json.has("name")){
            ope.setID(uuid);
            ope.setName(json.get("name").toString());

            Properties properties = new Properties();
            JSONObject props = json.getJSONArray("properties").getJSONObject(0);
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

    public static void setGroup(Player p, GroupEntry g) {
        p.setCustomName(g.getPrefix() + getCustomName(p));
        p.setPlayerListName(p.getCustomName());
        p.setScoreboard(ScoreBoardManager.board);
    }

    public static void updateGroup(Player p, GroupEntry g) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        pe.setGroup(g);
        pe.setUpdatedBy(pe.getID());
        pe.setToBeUpdated(true);
        setGroup(p, g);
    }

    public static void updateGroup(OfflinePlayer p, GroupEntry g) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        pe.setGroup(g);
        pe.setUpdatedBy(pe.getID());
        pe.setToBeUpdated(true);
    }


    public static String getCustomName(Player p) {
        String name;
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if (pe.getCustomName() != null && pe.getCustomName().equals("null")) {
            name = pe.getCustomName();
        } 
        else {
            if (pe.getName() != null && pe.getName().equals("null")) {
                name = pe.getName();
            } 
            else {
                name = p.getName();
            }
        }

        return name;
    }

    public static GroupEntry getGroup(Player p) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());

        if (pe != null) {
            return pe.getGroup();
        }
        else {
            return Groups.getGroup(1);
        }
    }

    public static void savePlayers(){
        int updatedPlayers = 0;

        for(PlayerEntry pe : RelluEssentials.getInstance().getPlayerAPI().getPlayerEntryMap().values()) {
            updatedPlayers += savePlayer(pe);
        }

        if(updatedPlayers != 0){
            ChatHelper.sendMessageInChannel(String.format(Strings.PLUGIN_PLAYERS_SAVED, BetterChatFormat.ADMIN_CHANNEL, updatedPlayers), Strings.PLUGIN_NAME_CHAT_CONSOLE, BetterChatFormat.ADMIN_CHANNEL, Groups.getGroup("admin"));
        }
    }

    public static int savePlayer(Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        return savePlayer(pe);
    }

    public static int savePlayer(PlayerEntry pe){
        if(pe.hasToBeUpdated()){
            RelluEssentials.getInstance().getDatabaseHelper().updatePlayer(pe);
            pe.setToBeUpdated(false);
            return 1;
        }

        return 0;
    }

    public static OfflinePlayer getOfflinePlayer(String name){
        for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
            if(op.getName().equals(name)){
                return op;
            }
        }

        return null;
    }

    public static PlayerEntry getPlayer(String name){
        for(PlayerEntry pe : RelluEssentials.getInstance().getPlayerAPI().getPlayerEntryMap().values()){
            if(pe.getName().equals(name)){
                return pe;
            }
        }

        return null;
    }

    public static Player getTargetedPlayer(Location loc){
        Player nearestPlayer = null;
        double lastDistance = Double.MAX_VALUE;
        for(Player p : loc.getWorld().getPlayers()){
            double distanceSquared = loc.distanceSquared(p.getLocation());
            if(distanceSquared < lastDistance){
                lastDistance = distanceSquared;
                nearestPlayer = p;
            }
        }
        return nearestPlayer;
    }

    public static void setLobbyItems(Player p){
        p.getInventory().setItem(0, new GrapplingHook().getCustomItem());
        p.getInventory().setItem(1, CustomItems.cloudSailor.getCustomItem());
    }
}