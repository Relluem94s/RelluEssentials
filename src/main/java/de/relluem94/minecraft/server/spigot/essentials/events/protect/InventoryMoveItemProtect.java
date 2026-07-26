package de.relluem94.minecraft.server.spigot.essentials.events.protect;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.constants.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

public class InventoryMoveItemProtect implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onMoveItem(@NotNull InventoryMoveItemEvent e) {
        if (handleMoveItemEvent(e.getSource(), e.getItem(), true) || handleMoveItemEvent(e.getDestination(), e.getItem(), false)){
            e.setCancelled(true);
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
                    im.getPersistentDataContainer().set(itemCoins(), PersistentDataType.INTEGER, sellPriceItem);

                    coin.setItemMeta(im);

                    inventory.addItem(coin);

                    final ItemStack toRemove = is.clone();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            inventory.removeItem(toRemove);
                        }
                    }.runTaskLater(RelluEssentials.getInstance(), 1L);
                    return false;
                }
                else{
                    return true;
                }
            }
        }
        return false;
    }


}
