package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW_ADMIN_OVERWRITE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_LOCATION;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_MATERIAL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_UUID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_RIGHTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECTED_BLOCK_INFO_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_ADD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD_FAILED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE_FAILED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_REMOVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_PROTECT_RIGHTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.constants.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

/**
 *
 * @author rellu
 */
public class BetterLock implements Listener {
    @EventHandler
    public void onWaterMove(@NotNull BlockFromToEvent e) {
        List<Material> unBreakable = RelluEssentials.getInstance().getProtectionAPI().getMaterialProtectionList();
        Block block = e.getToBlock();
        if (unBreakable.contains(block.getType())){
            if(RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(block.getLocation()) != null){
                e.setCancelled(true);
            }
        }
    }

    private boolean handleMoveItemEvent(Inventory inventory, ItemStack is, boolean isSource) {
        Location location;
        Location locationOtherSide = null;
        InventoryHolder holder;
        if(inventory != null){
            holder = inventory.getHolder();

            if(inventory.getType().equals(InventoryType.HOPPER)){
                return sellItem(inventory, is, isSource, ((BlockState) Objects.requireNonNull(holder)).getLocation());
            }

            try {
                if (holder instanceof BlockState) {
                    location = ((BlockState)holder).getLocation();
                } else if (holder instanceof DoubleChest doubleChest) {
                    location = Objects.requireNonNull(Objects.requireNonNull(doubleChest.getRightSide()).getInventory().getLocation()).getBlock().getLocation();
                    locationOtherSide = Objects.requireNonNull(Objects.requireNonNull(doubleChest.getLeftSide()).getInventory().getLocation()).getBlock().getLocation();
                } else {
                    return false;
                } 
            } catch (Exception e) {
                return false;
            }

            ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(location);

            if (protection == null){
                if(locationOtherSide == null){
                    return false;
                }
                else{
                    protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(locationOtherSide);
                }

                if (protection == null){
                    return false;
                }
                else{
                    if(isSource){
                        return !ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_HOPPER);
                    }
                    else{
                        return true;
                    }
                }
            }
            else{
                return !ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_HOPPER);
            }
        }
        else{
            return false;
        }
    }

    private static boolean sellItem(Inventory inventory, ItemStack is, boolean isSource, @NotNull Location location) {
        BlockState state = location.getBlock().getState();
        if((state instanceof Nameable)) {
            String name = ((Nameable)state).getCustomName();

            if(name != null && name.contains(PLUGIN_ITEM_AUTOSELLHOPER)){
                ItemPrice itemPrice = ItemPrice.from(is.getType());
                int sellPriceItem = itemPrice.getSellPrice() * is.getAmount();

                int size = 0;
                for(ItemStack itemStack : inventory.getStorageContents()){
                    if(itemStack == null){
                        continue;
                    }

                    if(!itemStack.getType().equals(Material.AIR)){
                        size++;
                    }
                }

                if(CustomItems.coins.almostEquals(is) || sellPriceItem == 0){
                    return false;
                }


                if(!isSource && (inventory.firstEmpty() != -1 && size < 4)){
                    ItemStack coin = CustomItems.coins.getCustomItem();
                    ItemMeta im = coin.getItemMeta();
                    Objects.requireNonNull(im).setLore(Collections.singletonList(String.format(ItemConstants.PLUGIN_ITEM_COINS_LORE, StringHelper.formatInt(sellPriceItem))));
                    im.getPersistentDataContainer().set(itemCoins, PersistentDataType.INTEGER, sellPriceItem);

                    coin.setItemMeta(im);

                    inventory.addItem(coin);

                    new BukkitRunnable() {
                    @Override
                        public void run() {
                            inventory.remove(is);
                        }
                    }.runTaskLater(RelluEssentials.getInstance(),  1L);
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        return false;
    }

    private boolean removeProtectionFromBlock(Player p , Block b){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        if(ProtectionHelper.isLockAble(b)){
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if (bpe != null) {
                if(bpe.getLocationEntry().getPlayerId() != pe.getId()){
                    p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                    return true;
                }
                else{            
                    RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
                    RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(b.getLocation());
                    p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_REMOVE);
                    return false;
                }
            }
        }
        else if(isAttachedToBlock(b, BlockFace.EAST)){
            Location l = b.getRelative(BlockFace.EAST).getLocation();
            ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(bpe != null && bpe.getLocationEntry() != null && bpe.getLocationEntry().getPlayerId() == pe.getId()){
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(b.getLocation());
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
            ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(bpe != null && bpe.getLocationEntry() != null && bpe.getLocationEntry().getPlayerId() == pe.getId()){
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(b.getLocation());
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
            ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(bpe != null && bpe.getLocationEntry() != null && bpe.getLocationEntry().getPlayerId() == pe.getId()){
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(b.getLocation());
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
            ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(bpe != null && bpe.getLocationEntry() != null && bpe.getLocationEntry().getPlayerId() == pe.getId()){
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(b.getLocation());
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
            ProtectionEntry bpe = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(bpe != null && bpe.getLocationEntry() != null && bpe.getLocationEntry().getPlayerId() == pe.getId()){
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(bpe);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(b.getLocation());
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
        if(ProtectionHelper.isLockAble(b)){
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            ProtectionEntry bpe = new ProtectionEntry();
            LocationEntry l = RelluEssentials.getInstance().getDatabaseHelper().getLocation(b.getLocation(), 5);

            boolean hasRights = true;

            if(b.getBlockData() instanceof Chest cd){

                if(!cd.getType().equals(Type.SINGLE)){
                    Block b2;
                    Block b3 = switch (cd.getFacing()) {
                        case NORTH -> {
                            b2 = b.getRelative(BlockFace.EAST);
                            yield b.getRelative(BlockFace.WEST);
                        }
                        case EAST -> {
                            b2 = b.getRelative(BlockFace.SOUTH);
                            yield b.getRelative(BlockFace.NORTH);
                        }
                        case SOUTH -> {
                            b2 = b.getRelative(BlockFace.WEST);
                            yield b.getRelative(BlockFace.EAST);
                        }
                        default -> {
                            b2 = b.getRelative(BlockFace.NORTH);
                            yield b.getRelative(BlockFace.SOUTH);
                        }
                    };

                    if(b2.getBlockData() instanceof Chest cd2){
                        if(cd2.getFacing().equals(cd.getFacing())){
                            if(ProtectionHelper.hasPermission(b2, p)){
                                hasRights = false;
                                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_DISALLOW);
                                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_ADD_CHEST_DENY);
                            }
                        }
                    }
                    else if(b3.getBlockData() instanceof Chest cd3){
                        if(cd3.getFacing().equals(cd.getFacing())){
                            if(ProtectionHelper.hasPermission(b3, p)){
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
                l.setPlayerId(pe.getId());

                p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_ADD);

                RelluEssentials.getInstance().getDatabaseHelper().insertLocation(l);

                LocationEntry le = RelluEssentials.getInstance().getDatabaseHelper().getLocation(b.getLocation(), 5);

                bpe.setCreatedBy(pe.getId());
                bpe.setMaterialName(b.getType().name());
                bpe.setLocationEntry(le);

                int playerPartnerFK = -1;

                if(pe.getPartner() != null){
                    if(pe.getId() != pe.getPartner().getFirstPartnerId()){
                        playerPartnerFK = pe.getPartner().getFirstPartnerId();
                    }
                    else{
                        playerPartnerFK = pe.getPartner().getSecondPartnerId();
                    }
                    
                }

                int rightLength = 1;

                if(playerPartnerFK != -1){
                    rightLength = 2;
                }

                JSONObject rights = new JSONObject();
                int[] right = new int[rightLength];
                right[0] = pe.getId();

                if(playerPartnerFK != -1){
                    right[1] = playerPartnerFK;
                }

                rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, right);
                bpe.setRights(rights);

                JSONObject flags = new JSONObject();
                if(b.getType().equals(Material.LEVER) || b.getType().equals(Material.IRON_DOOR)){
                    String[] flag = {ProtectionFlags.ALLOW_REDSTONE.name()};
                    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flag);
                }
                bpe.setFlags(flags);

                RelluEssentials.getInstance().getDatabaseHelper().insertProtection(bpe);
                RelluEssentials.getInstance().getProtectionAPI().putProtectionEntry(b.getLocation(), RelluEssentials.getInstance().getDatabaseHelper().getProtectionByLocation(b.getLocation()));
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


    private boolean isAttachedToBlock(@NotNull Block b, BlockFace face){
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
        else if(bd instanceof Door){
            return attachedBlock.getRelative(BlockFace.DOWN).equals(b);
        }
        else{
            return false;
        }

        
    }






    @EventHandler
    public void placeBlocks(@NotNull BlockPlaceEvent e) {
        e.setCancelled(!protectBlock(e.getPlayer(), e.getBlock()));
    }

    @EventHandler
    public void onBlockBreak(@NotNull BlockBreakEvent e) {
        if(removeProtectionFromBlock(e.getPlayer(), e.getBlock())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerProtectionChange(@NotNull PlayerInteractEvent e) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer());

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
            ProtectionEntry pre = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(pre != null){
                Player p = e.getPlayer();
                Location loc = pre.getLocationEntry().getLocation();

                p.sendMessage("");
                p.sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_INFO);
                p.sendMessage("");
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_ID, pre.getId()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_CREATED, pre.getCreated()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_UPDATED, pre.getUpdated()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_LOCATION, loc.getX(), loc.getY(), loc.getZ(), Objects.requireNonNull(loc.getWorld()).getName()));
                p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_ID, pre.getLocationEntry().getPlayerId()));

                
                for(Map.Entry<UUID, PlayerEntry> entry : RelluEssentials.getInstance().getPlayerAPI().getPlayerEntryMap().entrySet()) {
                    if(entry.getValue().getId() == pre.getLocationEntry().getPlayerId()){
                        OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(entry.getValue().getUuid()));
                        
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_LAST_LOGIN_DATE_FORMAT);
                        cal.setTimeInMillis(op.getLastPlayed());

                        p.sendMessage(String.format(PLUGIN_EVENT_PROTECTED_BLOCK_INFO_PLAYER_UUID, entry.getValue().getUuid()));
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
            ProtectionEntry pre = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                boolean update = false;
                if(pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)){
                    JSONArray flagJSON = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

                    String flag = ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).name();
                    List<Object> list = flagJSON.toList();
                    if(list.contains(flag)){
                        update = true;
                        list.remove(flag);
                        JSONObject flags = new JSONObject();
                        flags.put(PLUGIN_EVENT_PROTECT_FLAGS, list);

                        pre.setFlags(flags);
                    }
                }

                if(update){
                    RelluEssentials.getInstance().getDatabaseHelper().updateProtectionFlag(pre);
                    RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(l);
                    RelluEssentials.getInstance().getProtectionAPI().putProtectionEntry(l, pre);
                    e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE);
                }
                else{
                    e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_FLAG_REMOVE_FAILED);
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
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_FLAG_ADD)){            
            Block b = e.getClickedBlock();
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry pre = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                boolean update = false;
                if(pre.getFlags().has(PLUGIN_EVENT_PROTECT_FLAGS)){
                    JSONArray flagJSON = pre.getFlags().getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS);

                    List<Object> list = flagJSON.toList();
                    String flag = ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).name();

                    if(!list.contains(flag)){
                        update = true;
                        list.add(ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).name());
                    }

                    JSONObject flags = new JSONObject();
                    flags.put(PLUGIN_EVENT_PROTECT_FLAGS, list);

                    pre.setFlags(flags);
                }
                else{
                    JSONObject flags = new JSONObject();
                    if(Objects.requireNonNull(b).getType().equals(Material.LEVER) || b.getType().equals(Material.IRON_DOOR)){
                        String[] flag = {ProtectionFlags.ALLOW_REDSTONE.name(),ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).name()};
                        flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flag);
                    }
                    else{
                        String[] flag = {ProtectionFlags.valueOf(((String)pe.getPlayerStateParameter()).toUpperCase()).name()};
                        flags.put(PLUGIN_EVENT_PROTECT_FLAGS, flag);
                    }
                    update = true;
                    pre.setFlags(flags);  
                }
                
                if(update){
                    RelluEssentials.getInstance().getDatabaseHelper().updateProtectionFlag(pre);
                    RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(l);
                    RelluEssentials.getInstance().getProtectionAPI().putProtectionEntry(l, pre);
                    e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD);
                }
                else{
                    e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_FLAG_ADD_FAILED);
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
        else if(pe.getPlayerState().equals(PlayerState.PROTECTION_RIGHT_ADD)){
            Block b = e.getClickedBlock();
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            ProtectionEntry pre = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                
                UUID uuid = UUID.fromString((String)pe.getPlayerStateParameter());
                int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(uuid).getId();

                addRight(e.getPlayer(), pre, id, false);

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
            ProtectionEntry pre = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
            if(pre != null && ProtectionHelper.hasPermission(pre, e.getPlayer())){
                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_ALLOW);
                
                UUID uuid = UUID.fromString((String)pe.getPlayerStateParameter());
                int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(uuid).getId();
                
                removeRight(e.getPlayer(), pre, id, false);

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
    }

    public static void addRight(Player p, @NotNull ProtectionEntry pre, int id, boolean silent) {
        Location l = pre.getLocationEntry().getLocation();
        if(pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)){
            JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);

            List<Object> list = rightJSON.toList();

            if(!list.contains(id)){
                list.add(id);
                
                JSONObject rights = new JSONObject();
                rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, list);
                pre.setRights(rights);
                if(!silent){
                    p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD);
                }
                

                RelluEssentials.getInstance().getDatabaseHelper().updateProtectionRight(pre);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(l);
                RelluEssentials.getInstance().getProtectionAPI().putProtectionEntry(l, pre);
            }
            else{
                if(!silent){
                    p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_ADD_FAILED);
                }
            }                 
        }
    }

    public static void removeRight(Player p, @NotNull ProtectionEntry pre, int id, boolean silent) {
        Location l = pre.getLocationEntry().getLocation();
        if(pre.getRights().has(PLUGIN_EVENT_PROTECT_RIGHTS)){
            JSONArray rightJSON = pre.getRights().getJSONArray(PLUGIN_EVENT_PROTECT_RIGHTS);

            List<Object> list = rightJSON.toList();

            if(list.contains(id)){
                list.remove((Object)id);
                
                JSONObject rights = new JSONObject();
                rights.put(PLUGIN_EVENT_PROTECT_RIGHTS, list);
                pre.setRights(rights);

                RelluEssentials.getInstance().getDatabaseHelper().updateProtectionRight(pre);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(l);
                RelluEssentials.getInstance().getProtectionAPI().putProtectionEntry(l, pre);
                
                if(!silent){
                    p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE);
                }
            }
            else{
                if(!silent){
                    p.sendMessage(PLUGIN_EVENT_PROTECT_BLOCK_RIGHT_REMOVE_FAILED);
                }
            }
        }
    }

    public static void removeRight(ProtectionEntry pre, int id) {
        removeRight(null, pre, id, true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(@NotNull PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
       
        if(b != null){
            Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
            if(ProtectionHelper.isOpenAble(b)){
                ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer());
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
                    if (ProtectionHelper.hasRights(protection, pe.getId())) {
                        if(ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_PUBLIC)){
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
                                if(b2.getBlockData() instanceof Door door2){
                                    if(door2.getHinge() != door.getHinge()){
                                        if (door2.isOpen()) {
                                            door2.setOpen(false);
                                        }
                                        else{
                                            door2.setOpen(true);
    
                                            if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)){
                                                Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                                                door.setOpen(false);
                                                door2.setOpen(false);

                                                b.setBlockData(door);
                                                b2.setBlockData(door2);
                                                e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                           }, 50);
                                            }
                                        }
                                        b2.setBlockData(door2);
                                    }
                                }
                            }
                            else{
                                if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)){
                                    Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                                        door.setOpen(false);

                                        b.setBlockData(door);
                                        e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                    }, 50);
                                }
                            }                        
                        }
                        else if(openable instanceof TrapDoor){
                            TrapDoor door = (TrapDoor) b.getBlockData();
                            if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)){
                                Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                                    door.setOpen(false);

                                    b.setBlockData(door);
                                    e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                }, 50);
                            }
                        }
                        else if(openable instanceof Gate){
                            Gate door = (Gate) b.getBlockData();
                            if(ProtectionHelper.hasFlag(protection, ProtectionFlags.AUTO_CLOSE)){
                                Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
                                    door.setOpen(false);

                                    b.setBlockData(door);
                                    e.getPlayer().sendMessage(PLUGIN_EVENT_PROTECTED_BLOCK_AUTOCLOSE);
                                }, 50);
                            }
                        }
                        // ELSE Other Openable Objects (Future Implementations)
                    }
                }
            }
            else if(ProtectionHelper.isLockAble(b)){
                ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer());
                if (protection != null && pe != null && pe.getPlayerState().equals(PlayerState.DEFAULT)) { 
                    if (ProtectionHelper.hasRights(protection, pe.getId())) {
                        if(ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_PUBLIC)){
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
            }
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onMoveItem(@NotNull InventoryMoveItemEvent e) {
        if (handleMoveItemEvent(e.getSource(), e.getItem(), true) || handleMoveItemEvent(e.getDestination(), e.getItem(), false)){
            e.setCancelled(true); 
        }
    }

    @EventHandler
    public void onBlockRedstoneChange(@NotNull BlockRedstoneEvent e) {
        Block b = e.getBlock();
        ProtectionEntry protection;

        Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
        protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);

        if (protection != null){
            JSONObject flags = protection.getFlags();
            boolean isAllowed = (!flags.isEmpty() && flags.has(PLUGIN_EVENT_PROTECT_FLAGS) && flags.getJSONArray(PLUGIN_EVENT_PROTECT_FLAGS).toList().contains(ProtectionFlags.ALLOW_REDSTONE.name()));
            if(!isAllowed){
                e.setNewCurrent(e.getOldCurrent());
            }
        }
    }


    @EventHandler
    public void entityBreakDoor(@NotNull EntityBreakDoorEvent e) {
        Block b = e.getBlock();
        Location l = ProtectionHelper.getLocationFromBlockAlternateForDoor(b);
        ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(l);
        if (protection != null) {
            e.setCancelled(true); 
        } 
    }

    @EventHandler
    public void onBlockPistonExtend(@NotNull BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(b.getLocation());
            if (protection != null || isProtected(b, BlockFace.UP) || isProtected(b, BlockFace.DOWN)) {
                e.setCancelled(!b.getType().equals(Material.WATER));
                break;
            }
        } 
    }

    @EventHandler
    public void onBlockPistonRetract(@NotNull BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(b.getLocation());
            if (protection != null || isProtected(b, BlockFace.UP) || isProtected(b, BlockFace.DOWN)) {
                e.setCancelled(true);
                break;
            }
        } 
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(@NotNull EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(block.getLocation());
            if (protection != null) {
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(protection);
                continue;
            } 
            event.setCancelled(true);
        } 
    }

    private boolean isProtected(@NotNull Block b, BlockFace bf){
        return RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(b.getRelative(bf).getLocation()) != null;
    }

}