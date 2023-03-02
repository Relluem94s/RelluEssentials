package de.relluem94.minecraft.server.spigot.essentials.events;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.BlockData;

import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;

import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.ProtectionAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.constants.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.*;



/**
 *
 * @author rellu
 */
public class BetterLock implements Listener {

    private boolean handleMoveItemEvent(Inventory inventory) {
        Location location;
        InventoryHolder holder;
        if(inventory != null){
            holder = inventory.getHolder();
            try {
                if (holder instanceof BlockState) {
                    location = ((BlockState)holder).getLocation();
                } else if (holder instanceof DoubleChest) {
                    location = ((DoubleChest)holder).getLocation();
                } else {
                    return false;
                } 
            } catch (Exception e) {
                return false;
            } 
            
            ProtectionEntry protection = ProtectionAPI.getProtectionEntry(location);
            if (protection == null){
                return false; 
            }
            else{
                return !ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOWHOPPER);
            }
        }
        else{
            return false;
        }
    }

    private boolean removeProtectionFromBlock(Player p , Block b){
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
        if(ProtectionHelper.isLockable(b)){
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry bpe = ProtectionAPI.getProtectionEntry(l);
            if (bpe != null) {
                if(bpe.getLocation().getPlayerId() != pe.getID()){
                    p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                    return true;
                }
                else{            
                    dBH.deleteProtection(bpe);
                    ProtectionAPI.removeProtectionEntry(b.getLocation());
                    p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_REMOVE);
                    return false;
                }
            }
        }
        else if(isAttachedToBlock(b, BlockFace.EAST)){
            Location l = b.getRelative(BlockFace.EAST).getLocation();
            ProtectionEntry bpe = ProtectionAPI.getProtectionEntry(l);
            if(bpe != null && bpe.getLocation() != null && bpe.getLocation().getPlayerId() == pe.getID()){
                dBH.deleteProtection(bpe);
                ProtectionAPI.removeProtectionEntry(b.getLocation());
                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_REMOVE);
                return false;
            }
            else{
                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                return true;
            }
            
        }
        else if(isAttachedToBlock(b, BlockFace.SOUTH)){
            Location l = b.getRelative(BlockFace.SOUTH).getLocation();
            ProtectionEntry bpe = ProtectionAPI.getProtectionEntry(l);
            if(bpe != null && bpe.getLocation() != null && bpe.getLocation().getPlayerId() == pe.getID()){
                dBH.deleteProtection(bpe);
                ProtectionAPI.removeProtectionEntry(b.getLocation());
                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_REMOVE);
                return false;
            }
            else{
                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                return true;
            }
        }
        else if(isAttachedToBlock(b, BlockFace.NORTH)){
            Location l = b.getRelative(BlockFace.NORTH).getLocation();
            ProtectionEntry bpe = ProtectionAPI.getProtectionEntry(l);
            if(bpe != null && bpe.getLocation() != null && bpe.getLocation().getPlayerId() == pe.getID()){
                dBH.deleteProtection(bpe);
                ProtectionAPI.removeProtectionEntry(b.getLocation());
                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_REMOVE);
                return false;
            }
            else{
                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                return true;
            }
        }
        else if(isAttachedToBlock(b, BlockFace.WEST)){
            Location l = b.getRelative(BlockFace.WEST).getLocation();
            ProtectionEntry bpe = ProtectionAPI.getProtectionEntry(l);
            if(bpe != null && bpe.getLocation() != null && bpe.getLocation().getPlayerId() == pe.getID()){
                dBH.deleteProtection(bpe);
                ProtectionAPI.removeProtectionEntry(b.getLocation());
                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_REMOVE);
                return false;
            }
            else{
                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                return true;
            }
        }
        else if(isAttachedToBlock(b, BlockFace.UP)){
            Location l = b.getRelative(BlockFace.UP).getLocation();
            ProtectionEntry bpe = ProtectionAPI.getProtectionEntry(l);
            if(bpe != null && bpe.getLocation() != null && bpe.getLocation().getPlayerId() == pe.getID()){
                dBH.deleteProtection(bpe);
                ProtectionAPI.removeProtectionEntry(b.getLocation());
                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_REMOVE);
                return false;
            }
            else{
                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                return true;
            }
        }
        return false;
    }

    private boolean protectBlock(Player p, Block b) {
        if(ProtectionHelper.isLockable(b)){
            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
            ProtectionEntry bpe = new ProtectionEntry();
            LocationEntry l = dBH.getLocation(b.getLocation(), 5);

            boolean hasRights = true;

            if(b.getBlockData() instanceof Chest){
                Chest cd = (Chest) b.getBlockData();

                if(!cd.getType().equals(Type.SINGLE)){
                    Block b2;
                    Block b3;

                    switch (cd.getFacing()) {
                        case NORTH:
                            b2 = b.getRelative(BlockFace.EAST);
                            b3 = b.getRelative(BlockFace.WEST);
                            break;
                        case EAST:
                            b2 = b.getRelative(BlockFace.SOUTH);
                            b3 = b.getRelative(BlockFace.NORTH);
                            break;
                        case SOUTH:
                            b2 = b.getRelative(BlockFace.WEST);
                            b3 = b.getRelative(BlockFace.EAST);
                            break;
                        default:
                            b2 = b.getRelative(BlockFace.NORTH);
                            b3 = b.getRelative(BlockFace.SOUTH);
                            break;
                    }

                    if(b2.getBlockData() instanceof Chest){
                        Chest cd2 = (Chest) b2.getBlockData();
                        if(cd2.getFacing().equals(cd.getFacing())){
                            if(!ProtectionHelper.hasPermission(b2, p)){
                                hasRights = false;
                                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY);
                            }
                        }
                    }
                    else if(b3.getBlockData() instanceof Chest){
                        Chest cd3 = (Chest) b3.getBlockData();
                        if(cd3.getFacing().equals(cd.getFacing())){
                            if(!ProtectionHelper.hasPermission(b3, p)){
                                hasRights = false;
                                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY);
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
                l.setPlayerId(pe.getID());

                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_ADD);

                dBH.insertLocation(l);

                LocationEntry le = dBH.getLocation(b.getLocation(), 5);

                bpe.setCreatedby(pe.getID());
                bpe.setMaterialName(b.getType().name());
                bpe.setLocationEntry(le);

                JSONObject rights = new JSONObject();
                int[] right = new int[1];
                right[0] = pe.getID();
                rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, right);
                bpe.setRights(rights);

                JSONObject flags = new JSONObject();
                if(b.getType().equals(Material.LEVER) || b.getType().equals(Material.IRON_DOOR)){
                    String[] flag = {ProtectionFlags.ALLOWREDSTONE.getName()};
                    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flag);
                }
                bpe.setFlags(flags);

                dBH.insertProtection(bpe);
                ProtectionAPI.putProtectionEntry(b.getLocation(), dBH.getProtectionByLocation(b.getLocation()));
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return true;
        }
    }


    private boolean isAttachedToBlock(Block b, BlockFace face){
        Block attachedBlock = b.getRelative(face);
        BlockData bd = attachedBlock.getBlockData();

        if(bd instanceof WallHangingSign){
            WallHangingSign sign = (WallHangingSign) attachedBlock.getBlockData();
            return attachedBlock.getRelative(sign.getFacing().getOppositeFace()).equals(b);
        }
        else if(bd instanceof WallSign){
            WallSign sign = (WallSign) attachedBlock.getBlockData();
            return attachedBlock.getRelative(sign.getFacing().getOppositeFace()).equals(b);
        }
        else if(bd instanceof Sign){
            return attachedBlock.getRelative(BlockFace.DOWN).equals(b);
        }
        else{
            return false;
        }

        
    }






    @EventHandler
    public void placeBlocks(BlockPlaceEvent e) {
        e.setCancelled(!protectBlock(e.getPlayer(), e.getBlock()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(removeProtectionFromBlock(e.getPlayer(), e.getBlock())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerProtectionChange(PlayerInteractEvent e) {
        PlayerEntry pe = PlayerAPI.getPlayerEntry(e.getPlayer());

        if(pe.getPlayerState().equals(PlayerState.PROTECTION_ADD)){
            protectBlock(e.getPlayer(), e.getClickedBlock());
            pe.setPlayerState(PlayerState.DEFAULT);
            pe.setPlayerStateParameter(null);
            e.setCancelled(true);
        }
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_REMOVE)){
            removeProtectionFromBlock(e.getPlayer(), e.getClickedBlock());
            pe.setPlayerState(PlayerState.DEFAULT);
            pe.setPlayerStateParameter(null);
            e.setCancelled(true);
        }
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_INFO)){
            Block b = e.getClickedBlock();
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry pre = ProtectionAPI.getProtectionEntry(l);
            if(pre != null){
                Player p = e.getPlayer();
                Location loc = pre.getLocation().getLocation();

                p.sendMessage("");
                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_INFO);
                p.sendMessage("");
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_ID, pre.getId()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_CREATED, pre.getCreated()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_UPDATED, pre.getUpdated()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_LOCATION, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_ID, pre.getLocation().getPlayerId()));

                
                for(Map.Entry<UUID, PlayerEntry> entry : PlayerAPI.getPlayerEntryMap().entrySet()) {
                    if(entry.getValue().getID() == pre.getLocation().getPlayerId()){
                        OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(entry.getValue().getUUID()));
                        
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT);
                        cal.setTimeInMillis(op.getLastPlayed());

                        p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_UUID, entry.getValue().getUUID()));
                        p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_NAME, op.getName()));
                        p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN, df.format(cal.getTime())));
                    }
                }

                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_MATERIAL, pre.getMaterialName()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_FLAGS, pre.getFlags().toString()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_RIGHTS, pre.getRights().toString()));
                p.sendMessage("");
                p.sendMessage("");


            }
            pe.setPlayerState(PlayerState.DEFAULT);
            pe.setPlayerStateParameter(null);
            e.setCancelled(true);
        }
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_REMOVE)){
            Block b = e.getClickedBlock();
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry pre = ProtectionAPI.getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                if(pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)){
                    JSONArray flagJSON = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

                    List<Object> list = flagJSON.toList();
                    list.remove(ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).getName());
                    JSONObject flags = new JSONObject();
                    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, list);

                    pre.setFlags(flags);
                }

                dBH.updateProtectionFlag(pre);
                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE);
                e.setCancelled(true);
            }
            else{
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
            }
        }
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_ADD)){
            // TODO ERROR does Update entry but if flag was added and gets added again.. it will fail..  JSONObject["flags"] is not a JSONArray (Class String) 
            
            Block b = e.getClickedBlock();
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry pre = ProtectionAPI.getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                if(pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)){
                    JSONArray flagJSON = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

                    List<Object> list = flagJSON.toList();
                    
                    list.add(ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).getName());
                    
                    JSONObject flags = new JSONObject();
                    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, list);

                    pre.setFlags(flags);
                }
                else{
                    JSONObject flags = new JSONObject();
                    if(b.getType().equals(Material.LEVER) || b.getType().equals(Material.IRON_DOOR)){
                        String[] flag = {ProtectionFlags.ALLOWREDSTONE.getName(),ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).getName()};
                        flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flag);
                    }
                    else{
                        String[] flag = {ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).getName()};
                        flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flag);
                    }

                    pre.setFlags(flags);  
                }
                
                dBH.updateProtectionFlag(pre);
                
                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD);
                e.setCancelled(true);
            }
            else{
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
            }
        }
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_ADD)){
            Block b = e.getClickedBlock();
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry pre = ProtectionAPI.getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                if(pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)){
                    JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);

                    List<Object> list = rightJSON.toList();

                    UUID uuid = UUID.fromString((String)pe.getPlayerStateParameter());
                    int id = PlayerAPI.getPlayerEntry(uuid).getID();
                    if(!list.contains(id)){
                        list.add((Object)id);
                        
                        JSONObject rights = new JSONObject();
                        rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, list);
                        pre.setRights(rights);
                        e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD);

                        dBH.updateProtectionFlag(pre);
                    }                    
                }

                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
                e.setCancelled(true);
            }
            else{
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
            }
        }
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_REMOVE)){
            Block b = e.getClickedBlock();
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry pre = ProtectionAPI.getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                if(pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)){
                    JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);

                    List<Object> list = rightJSON.toList();

                    UUID uuid = UUID.fromString((String)pe.getPlayerStateParameter());
                    int id = PlayerAPI.getPlayerEntry(uuid).getID();
                    if(list.contains(id)){
                        list.remove((Object)id);
                        
                        JSONObject rights = new JSONObject();
                        rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, list);
                        pre.setRights(rights);

                        dBH.updateProtectionFlag(pre);
                    }
                }

                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE);
                e.setCancelled(true);
            }
            else{
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                pe.setPlayerState(PlayerState.DEFAULT);
                pe.setPlayerStateParameter(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
       
        if(b != null){
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            if(ProtectionHelper.isOpenable(b)){
                ProtectionEntry protection = ProtectionAPI.getProtectionEntry(l);
                PlayerEntry pe = PlayerAPI.getPlayerEntry(e.getPlayer());
                if (protection != null && pe != null && 
                    !(
                        pe.getPlayerState().equals(PlayerState.PROTECTION_INFO) || 
                        pe.getPlayerState().equals(PlayerState.PROTECTION_ADD) || 
                        pe.getPlayerState().equals(PlayerState.PROTECTION_REMOVE) || 
                        pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_ADD) || 
                        pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_REMOVE) || 
                        pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_ADD) || 
                        pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_REMOVE)
                    )
                ) {  
                    if (!ProtectionHelper.hasRights(protection, pe.getID())) {
                        if(ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOWPUBLIC)){
                            e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                        }
                        else{
                            if (Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId())) {
                                e.setCancelled(false); 
                                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW_ADMIN_OVERWRITE);
                            }
                            else{
                                e.setCancelled(true); 
                                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                            }
                            
                        }
                    }
                    else{
                        // TODO If Notify protection self on
                        e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);

                        Openable openable = (Openable) b.getBlockData();

                        if(openable instanceof Door){
                            Door door = (Door) b.getBlockData();
                            Block b2 = ProtectionHelper.getOtherPart(door, b);
                            if(b2 != null){
                                if(b2.getBlockData() instanceof Door){
                                    Door door2 = (Door) b2.getBlockData();
                                    if (door2.isOpen()) {
                                        door2.setOpen(false);
                                    }
                                    else{
                                        door2.setOpen(true);

                                        if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTOCLOSE)){
                                       Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), new Runnable() {

                                                @Override
                                                public void run() {
                                                    door.setOpen(false);
                                                    door2.setOpen(false);

                                                    b.setBlockData(door);
                                                    b2.setBlockData(door2);
                                                    e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                                    // TODO shows 2x Autoclose Message but door is already closed by player
                                                }
                                                
                                            }, 50);
                                        }
                                    }
                                    b2.setBlockData(door2);
                                }
                            }
                            else{
                                if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTOCLOSE)){
                                    Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), new Runnable() {

                                        @Override
                                        public void run() {
                                            door.setOpen(false);

                                            b.setBlockData(door);
                                            e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                        }
                                        
                                    }, 50);
                                }
                            }                        
                        }
                        else if(openable instanceof TrapDoor){
                            TrapDoor door = (TrapDoor) b.getBlockData();
                            if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTOCLOSE)){
                                Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), new Runnable() {

                                    @Override
                                    public void run() {
                                        door.setOpen(false);

                                        b.setBlockData(door);
                                        e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                    }
                                    
                                }, 50);
                            }
                        }
                        else if(openable instanceof Gate){
                            Gate door = (Gate) b.getBlockData();
                            if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTOCLOSE)){
                                Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), new Runnable() {

                                    @Override
                                    public void run() {
                                        door.setOpen(false);

                                        b.setBlockData(door);
                                        e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                    }
                                    
                                }, 50);
                            }
                        }
                        else{
                            // Other Openable Objects (Future Implementations)
                        }
                    }
                }
            }
            else if(ProtectionHelper.isLockable(b)){
                ProtectionEntry protection = ProtectionAPI.getProtectionEntry(l);
                PlayerEntry pe = PlayerAPI.getPlayerEntry(e.getPlayer());
                if (protection != null && pe != null && pe.getPlayerState().equals(PlayerState.DEFAULT)) { 
                    if (!ProtectionHelper.hasRights(protection, pe.getID())) {
                        if(ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOWPUBLIC)){
                            e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                        }
                        else{
                            if (Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId())) {
                                e.setCancelled(false); 
                                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW_ADMIN_OVERWRITE);
                            }
                            else{
                                e.setCancelled(true); 
                                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                            }
                        }
                    }
                    else{
                        // If Notify protection self on
                        e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                    }
                }
                else{
                    //Unprotected Block
                }
            }
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onMoveItem(InventoryMoveItemEvent e) {
        if (handleMoveItemEvent(e.getSource()) || handleMoveItemEvent(e.getDestination())){
            e.setCancelled(true); 
        }
    }

    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent e) {
        Block b = e.getBlock();
        ProtectionEntry protection;
        
        if (b != null){
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            protection = ProtectionAPI.getProtectionEntry(l);
            
            if (protection != null){
                JSONObject flags = protection.getFlags();
                boolean isAllowed = (!flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS).toList().contains(ProtectionFlags.ALLOWREDSTONE.getName()));
                if(!isAllowed){
                    e.setNewCurrent(e.getOldCurrent()); 
                }
            }
        }
    }


    @EventHandler
    public void entityBreakDoor(EntityBreakDoorEvent e) {
        Block b = e.getBlock();
        Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
        ProtectionEntry protection = ProtectionAPI.getProtectionEntry(l);
        if (protection != null) {
            e.setCancelled(true); 
        } 
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent e) {

        Block piston = e.getBlock();        
        BlockFace direction = null;

        if (piston.getBlockData() instanceof Piston){
            direction = ((Piston)piston.getBlockData()).getFacing(); 
        }
          
        if (direction == null){
            return; 
        }

        Block moved = piston.getRelative(direction, 2);
        if (moved.getType() == Material.OAK_DOOR || moved.getType() == Material.IRON_DOOR) {
            Block below = moved.getRelative(BlockFace.DOWN).getRelative(direction.getOppositeFace());

            ProtectionEntry protection = ProtectionAPI.getProtectionEntry(below.getLocation());
            if (protection != null) {
                e.setCancelled(true);
                return;
            } 
        } 
        ProtectionEntry protection = ProtectionAPI.getProtectionEntry(moved.getLocation());
        if (protection != null) {
            e.setCancelled(true); 
        } 
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent e) {
        Block piston = e.getBlock();        
        BlockFace direction = null;


        if (piston.getBlockData() instanceof Piston){
            direction = ((Piston)piston.getBlockData()).getFacing(); 
            Block block = e.getBlock().getRelative(direction);
            ProtectionEntry protection = ProtectionAPI.getProtectionEntry(block.getLocation());
            if (protection != null) {
                e.setCancelled(true);
                return;
            } 
        }
        if (direction == null){
            return; 
        }
          
        for (int i = 0; i <  e.getBlocks().size() + 2; i++) {
            Block block = piston.getRelative(direction, i);
            ProtectionEntry protection = ProtectionAPI.getProtectionEntry(block.getLocation());
            if (block.getType() == Material.AIR){
                break;
            }
            if (protection != null) {
                e.setCancelled(true);
                break;
            } 
        } 
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            ProtectionEntry protection = ProtectionAPI.getProtectionEntry(block.getLocation());
            if (protection != null) {
                dBH.deleteProtection(protection);
                continue;
            } 
            event.setCancelled(true);
        } 
    }

}