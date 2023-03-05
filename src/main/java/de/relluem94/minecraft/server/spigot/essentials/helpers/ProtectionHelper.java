package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.ProtectionAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.ProtectionFlags;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;

public class ProtectionHelper {
    
    /**
     * Use this Method to check if Block is protectable
     * @param b Block
     * @return boolean
     */
    public static boolean isLockable(Block b){
        return (ProtectionAPI.getMaterialProtectionList().contains(b.getType()));
    }

    /**
     * Use this Method to check if a Block is a instance of Openable
     * @param b Block
     * @return boolean
     */
    public static boolean isOpenable(Block b){
        return b.getBlockData() instanceof Openable;
    }

    /**
     * Use this Method to check if Player has Permission for that Block
     * @param b Block
     * @param p Player
     * @return boolean
     */
    public static boolean hasPermission(Block b, Player p){
        Location l = getLocationFromBlockAlternateForDoor(b);
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
        ProtectionEntry pre = ProtectionAPI.getProtectionEntry(l);
        if (pre != null) {
            if(pre.getLocation().getPlayerId() != pe.getID()){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
    }

    /**
     * Use this Method to check if Player has Permission for that ProtectionEntry
     * @param pre ProtectionEntry
     * @param p Player
     * @return boolean
     */
    public static boolean hasPermission(ProtectionEntry pre, Player p){
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
        if (pre != null) {
            if(pre.getLocation().getPlayerId() != pe.getID()){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
    }

    /**
     * Use this with an ProtectionEntry to check ifa Protection has a Specific Flag
     * @param protection ProtectionEntry
     * @param flag ProtectionFlags
     * @return
     */
    public static boolean hasFlag(ProtectionEntry protection, ProtectionFlags flag) {
        JSONObject flags = protection.getFlags();
        if(
            (!flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.get(PLUGIN_EVENT_PROTECT_FLAGS) instanceof JSONArray && flags.getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS).toList().contains(flag.getName())) ||
            !flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.get(PLUGIN_EVENT_PROTECT_FLAGS) instanceof String && flags.get(PLUGIN_EVENT_PROTECT_FLAGS).equals(flag.getName())
        ){
            return true; 
        }
        else{
            return false;
        }
    }

    /**
     * Use this Method to check if Player has Rights to the ProtectionEntry
     * @param protection ProtectionEntry
     * @param player_fk
     * @return
     */
    public static boolean hasRights(ProtectionEntry protection, int player_fk) {
        JSONObject rights = protection.getRights();
        if(!rights.isEmpty() && rights.has(PLUGIN_EVENT_PROTECT_RIGHTS)){
            if(rights.getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS).toList().contains(player_fk)){
                return true; 
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
     * 
     * @param b
     * @return
     */
    public static Location getLocationFromBlockAlternateForDoor(Block b){
        if(b != null){
            Location l = b.getLocation();
            if(isOpenable(b)){
                Openable openable = (Openable) b.getBlockData();
                if(openable instanceof Door){
                    Door door = (Door) b.getBlockData();
                    if (door.getHalf().equals(Half.TOP)) {
                        l.add(0, -1, 0);
                    }
                }
            }
            return l;
        }
        return null;
    }
    
    /**
     * 
     * @param door
     * @param block
     * @return
     */
    public static Block getOtherPart(Door door, Block block) {
        Location l = block.getLocation().clone();
        if(door != null){
            if(door.getHinge().equals(Hinge.LEFT)){
                if(door.isOpen()){
                    if(door.getFacing().equals(BlockFace.EAST)){
                        l = l.add(0, 0, 1);
                    }
                    if(door.getFacing().equals(BlockFace.WEST)){
                        l = l.add(0, 0, -1);
                    }
                    if(door.getFacing().equals(BlockFace.SOUTH)){
                        l = l.add(-1, 0, 0);
                    }
                    if(door.getFacing().equals(BlockFace.NORTH)){
                        l = l.add(1, 0, 0);
                    }
                }
                else{
                    if(door.getFacing().equals(BlockFace.EAST)){
                        l = l.add(0, 0, 1);
                    }
                    if(door.getFacing().equals(BlockFace.WEST)){
                        l = l.add(0, 0, -1);
                    }
                    if(door.getFacing().equals(BlockFace.SOUTH)){
                        l = l.add(-1, 0, 0);
                    }
                    if(door.getFacing().equals(BlockFace.NORTH)){
                        l = l.add(1, 0, 0);
                    }
                }
            }
            else{
                if(door.isOpen()){
                    if(door.getFacing().equals(BlockFace.EAST)){
                        l = l.add(0, 0, -1);
                    }
                    if(door.getFacing().equals(BlockFace.WEST)){
                        l = l.add(0, 0, 1);
                    }
                    if(door.getFacing().equals(BlockFace.SOUTH)){
                        l = l.add(1, 0, 0);
                    }
                    if(door.getFacing().equals(BlockFace.NORTH)){
                        l = l.add(-1, 0, 0);
                    }
                }
                else{
                    if(door.getFacing().equals(BlockFace.EAST)){
                        l = l.add(0, 0, -1);
                    }
                    if(door.getFacing().equals(BlockFace.WEST)){
                        l = l.add(0, 0, 1);
                    }
                    if(door.getFacing().equals(BlockFace.SOUTH)){
                        l = l.add(1, 0, 0);
                    }
                    if(door.getFacing().equals(BlockFace.NORTH)){
                        l = l.add(-1, 0, 0);
                    }
                }
            }
        }
        if(isOpenable(l.getBlock())){
            return l.getBlock();
        }

        return null;
    }
}
