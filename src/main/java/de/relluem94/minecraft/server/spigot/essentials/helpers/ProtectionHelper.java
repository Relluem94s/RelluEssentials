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

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ProtectionFlags;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;

public class ProtectionHelper {

    private ProtectionHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }
    
    /**
     * Use this Method to check if Block is protectAble
     * @param b Block
     * @return boolean
     */
    public static boolean isLockAble(Block b){
        return (RelluEssentials.getInstance().getProtectionAPI().getMaterialProtectionList().contains(b.getType()));
    }

    /**
     * Use this Method to check if a Block is an instance of OpenAble
     * @param b Block
     * @return boolean
     */
    public static boolean isOpenAble(Block b){
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
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        ProtectionEntry pre = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
        if (pre != null) {
            return pre.getLocationEntry().getPlayerId() != pe.getId();
        }
        else{
            return false;
        }
    }

    /**
     * Use this Method to check if Player has Permission for that ProtectionEntry
     * @param pre ProtectionEntry
     * @param p Player
     * @return boolean
     */
    public static boolean hasPermission(ProtectionEntry pre, Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        if (pre != null) {
            return pre.getLocationEntry().getPlayerId() == pe.getId();
        }
        else{
            return true;
        }
    }

    /**
     * Use this with an ProtectionEntry to check ifa Protection has a Specific Flag
     * @param protection ProtectionEntry
     * @param flag ProtectionFlags
     * @return boolean
     */
    public static boolean hasFlag(ProtectionEntry protection, ProtectionFlags flag) {
        JSONObject flags = protection.getFlags();
        return(
            (!flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.get(PLUGIN_EVENT_PROTECT_FLAGS) instanceof JSONArray && flags.getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS).toList().contains(flag.getName())) ||
            !flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.get(PLUGIN_EVENT_PROTECT_FLAGS) instanceof String && flags.get(PLUGIN_EVENT_PROTECT_FLAGS).equals(flag.getName())
        );
    }

    /**
     * Use this Method to check if Player has Rights to the ProtectionEntry
     * @param protection ProtectionEntry
     * @param playerId int
     * @return boolean
     */
    public static boolean hasRights(ProtectionEntry protection, int playerId) {
        JSONObject rights = protection.getRights();
        if(!rights.isEmpty() && rights.has(PLUGIN_EVENT_PROTECT_RIGHTS)){
            return (!rights.getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS).toList().contains(playerId));
        }
        else{
            return true;
        }
    }

    /**
     * 
     * @param b Block
     * @return Location
     */
    public static Location getLocationFromBlockAlternateForDoor(Block b){
        if(b != null){
            Location l = b.getLocation();
            if(isOpenAble(b)){
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
     * @param door Door
     * @param block Block
     * @return Block
     */
    public static Block getOtherPart(Door door, Block block) {
        Location l = block.getLocation().clone();
        if(door != null){
            l = addLocationFromOtherPart(l, door.getFacing(), door.getHinge());
        }
        if(isOpenAble(l.getBlock())){
            return l.getBlock();
        }

        return null;
    }

    private static Location addLocationFromOtherPart(Location location, BlockFace blockFace, Hinge hinge){
        int plus = 1;
        int minus = -1;

        if(hinge.equals(Hinge.RIGHT)){
            plus = -1;
            minus = 1;
        }

        if(blockFace.equals(BlockFace.EAST)){
            location = location.add(0, 0, plus);
        }
        if(blockFace.equals(BlockFace.WEST)){
            location = location.add(0, 0, minus);
        }
        if(blockFace.equals(BlockFace.SOUTH)){
            location = location.add(minus, 0, 0);
        }
        if(blockFace.equals(BlockFace.NORTH)){
            location = location.add(plus, 0, 0);
        }
        return location;
    }
}
