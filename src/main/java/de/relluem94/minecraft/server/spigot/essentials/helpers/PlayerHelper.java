package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;

import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.items.GrapplingHook;
import de.relluem94.minecraft.server.spigot.essentials.items.WorldSelector;
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
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    /**
     *
     * @param p Player to set Flying
     */
    public static void setFlying(Player p) {
        if (Permission.isAuthorized(p, Objects.requireNonNull(Groups.getGroup("vip")).getId())) {
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
     *
     */
    public static void setAFK(Player p, boolean join) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        boolean isAFK = pe.isAfk();

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
                pe.setUpdatedBy(pe.getId());
                pe.setAfk(isAFK);
                pe.setUpdatedBy(pe.getId());
                pe.setHasToBeUpdated(true);
            }
            p.setInvulnerable(isAFK);
        }
        
        p.setPlayerListName((isAFK ? "§c[AFK] " : "") + p.getCustomName());
    }


    public static OfflinePlayerEntry getOfflinePlayerByName(String name){
        JSONObject json = NetworkUtils.getJSON("https://api.mojang.com/users/profiles/minecraft/" + name);
        OfflinePlayerEntry ope = new OfflinePlayerEntry();

        if(json.has("name")){
            ope.setId(UUIDHelper.dashed((String)json.get("id")));
            ope.setName(json.get("name").toString());
            return ope;
        }
        else{
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static @Nullable OfflinePlayerEntry getOfflinePlayerByUUID(UUID uuid){
        JSONObject json = NetworkUtils.getJSON("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDHelper.unDashed(uuid) + "?unsigned=false");
        OfflinePlayerEntry ope = new OfflinePlayerEntry();
        if(json.has("name")){
            ope.setId(uuid);
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

    public static void updateGroup(OfflinePlayer p, GroupEntry g) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());

        if(p.isOnline()){
            Player player = Bukkit.getPlayer(p.getUniqueId());
            if (player != null) {
                player.setCustomName(g.getPrefix() + getCustomName(player));
                player.setPlayerListName(g.getPrefix() + getCustomName(player));
            }
        }

        pe.setGroup(g);
        pe.setUpdatedBy(pe.getId());
        pe.setHasToBeUpdated(true);
    }


    public static String getCustomName(Player p) {
        String name;
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if (pe.getCustomName() != null && !pe.getCustomName().equals("null")) {
            name = pe.getCustomName();
        } 
        else {
            name = p.getName();
        }

        return name;
    }

    @SuppressWarnings("unused")
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
            ChatHelper.sendMessageInChannel(String.format(Constants.PLUGIN_PLAYERS_SAVED, BetterChatFormat.ADMIN_CHANNEL, updatedPlayers), Constants.PLUGIN_NAME_CHAT_CONSOLE, BetterChatFormat.ADMIN_CHANNEL, Groups.getGroup("admin"));
        }
    }

    public static void savePlayersInv(){
        int updatedPlayers = 0;

        for(Player p : Bukkit.getOnlinePlayers()) {
            updatedPlayers += WorldHelper.saveWorldGroupInventory(p, false) ? 1 : 0;
        }

        if(updatedPlayers != 0){
            ChatHelper.sendMessageInChannel(String.format(Constants.PLUGIN_PLAYERS_INVENTORY_SAVED, BetterChatFormat.ADMIN_CHANNEL, updatedPlayers), Constants.PLUGIN_NAME_CHAT_CONSOLE, BetterChatFormat.ADMIN_CHANNEL, Groups.getGroup("admin"));
        }
    }


    public static void savePlayer(Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        savePlayer(pe);
    }

    public static int savePlayer(@NotNull PlayerEntry pe){
        if(pe.isHasToBeUpdated()){
            RelluEssentials.getInstance().getDatabaseHelper().updatePlayer(pe);
            pe.setHasToBeUpdated(false);
            return 1;
        }

        return 0;
    }

    public static @Nullable OfflinePlayer getOfflinePlayer(@NonNull String name){
        for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
            if(name.equals(op.getName())){
                return op;
            }
        }

        return null;
    }

    @SuppressWarnings("unused")
    public static @Nullable PlayerEntry getPlayer(String name){
        for(PlayerEntry pe : RelluEssentials.getInstance().getPlayerAPI().getPlayerEntryMap().values()){
            if(pe.getName().equals(name)){
                return pe;
            }
        }

        return null;
    }

    public static @Nullable Player getTargetedPlayer(@NotNull Location loc){
        Player nearestPlayer = null;
        double lastDistance = Double.MAX_VALUE;
        World world = loc.getWorld();

        if(world == null){
            return null;
        }

        for(Player p : world.getPlayers()){
            double distanceSquared = loc.distanceSquared(p.getLocation());
            if(distanceSquared < lastDistance){
                lastDistance = distanceSquared;
                nearestPlayer = p;
            }
        }
        return nearestPlayer;
    }

    public static void setLobbyItems(Player p){
        GrapplingHook gh = new GrapplingHook();
        WorldSelector ws = new WorldSelector();

        for(ItemStack i : p.getInventory().getContents()){
            if(i == null){
                continue;
            }

            if(i.isSimilar(gh.getCustomItem())){
                p.getInventory().remove(i);
            }

            if(i.isSimilar(CustomItems.cloudSailor.getCustomItem())){
                p.getInventory().remove(i);
            }

            if(i.getType().equals(ws.getCustomItem().getType()) && i.hasItemMeta() && i.getItemMeta() != null && i.getItemMeta().getDisplayName().equals(ws.getDisplayName())){
                p.getInventory().remove(i);
            }
        }
    
        p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        p.getInventory().setItemInOffHand(null);
    
        

        p.getInventory().setItem(0, gh.getCustomItem());
        p.getInventory().setItem(1, CustomItems.cloudSailor.getCustomItem());
        p.getInventory().setItem(4, ws.getCustomItem());
    }
}