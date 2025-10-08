package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.*;

public class PositionAxeListener implements Listener {

    public static final String PLUGIN_ITEM_POSITION_AXE = "§6Position Axe";

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.COPPER_AXE) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() || !meta.getDisplayName().equals(PLUGIN_ITEM_POSITION_AXE)) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
        } else {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        DoubleStore<Location, Location> positions = RelluEssentials.getInstance().position.computeIfAbsent(player, k -> new DoubleStore<>(null, null));

        Location clickedLocation = event.getClickedBlock().getLocation();

        if (player.isSneaking()) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                positions.setValue(null);
                player.sendMessage(PLUGIN_EVENT_POSITION_AXE_FIRST_RESET);
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                positions.setSecondValue(null);
                player.sendMessage(PLUGIN_EVENT_POSITION_AXE_SECOND_RESET);
            }
        } else {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                positions.setValue(clickedLocation);
                player.sendMessage(String.format(PLUGIN_EVENT_POSITION_AXE_FIRST_SET, clickedLocation.getBlockX(), clickedLocation.getBlockY(), clickedLocation.getBlockZ()));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                positions.setSecondValue(clickedLocation);
                player.sendMessage(String.format(PLUGIN_EVENT_POSITION_AXE_SECOND_SET, clickedLocation.getBlockX(), clickedLocation.getBlockY(), clickedLocation.getBlockZ()));
            }
        }
    }
}