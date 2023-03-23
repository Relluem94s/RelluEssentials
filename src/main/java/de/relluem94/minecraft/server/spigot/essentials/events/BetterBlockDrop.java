package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

import org.bukkit.Bukkit;

public class BetterBlockDrop implements Listener {

    private final Material[] ores = {
        Material.DIAMOND_ORE,
        Material.LAPIS_ORE,
        Material.REDSTONE_ORE,
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.COPPER_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.GOLD_ORE,
        Material.EMERALD_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE
    };

    public void runLater(Runnable r, long d) {
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), r, d);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Material m = e.getBlock().getBlockData().getMaterial();
        for (Material ore : ores) {
            if (m == ore && Arrays.asList(RelluEssentials.ore_respawn).contains(e.getBlock().getLocation().getWorld().getName())) {
                runLater(() -> e.getBlock().setType(m), 10000L);
                break;
            }
        }
    }
}