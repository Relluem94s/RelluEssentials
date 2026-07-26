package de.relluem94.minecraft.server.spigot.essentials.events.protect;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionActionHelper.protectBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionActionHelper.removeProtectionFromBlock;

public class BlockModifyProtect implements Listener {
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
    public void onBlockMove(@NotNull BlockFromToEvent e) {
        List<Material> unBreakable = RelluEssentials.getInstance().getProtectionAPI().getMaterialProtectionList();
        Block block = e.getToBlock();
        if (unBreakable.contains(block.getType())){
            if(RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(block.getLocation()) != null){
                e.setCancelled(true);
            }
        }
    }
}