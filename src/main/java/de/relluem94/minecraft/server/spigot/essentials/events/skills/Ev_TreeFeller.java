package de.relluem94.minecraft.server.spigot.essentials.events.skills;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.locationToString;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import java.util.Arrays;
import org.bukkit.Bukkit;

public class Ev_TreeFeller implements Listener {

    private final Material[] logs = {
        Material.OAK_LOG,
        Material.ACACIA_LOG,
        Material.BIRCH_LOG,
        Material.DARK_OAK_LOG,
        Material.JUNGLE_LOG,
        Material.SPRUCE_LOG
    };

    private final Material[] leaves = {
        Material.OAK_LEAVES,
        Material.ACACIA_LEAVES,
        Material.BIRCH_LEAVES,
        Material.DARK_OAK_LEAVES,
        Material.JUNGLE_LEAVES,
        Material.SPRUCE_LEAVES
    };

    @EventHandler
    public void onFell(BlockBreakEvent e) {
        ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta im = itemInHand.getItemMeta();

        if (itemInHand.getType().equals(Material.NETHERITE_AXE)) {
            for (Material b2d : logs) {
                if (e.getBlock().getType().equals(b2d)) {
                    fellTree(e.getBlock());
                    if (((Damageable) im) != null) {
                        User u = User.getUserByPlayerName(e.getPlayer().getName());
                        int score = u.treeFeller.getObjective().getScore("Tree Feller").getScore();
                        u.treeFeller.getObjective().getScore("Tree Feller").setScore(score + 1);

                    }
                }
            }
        }
        itemInHand.setItemMeta(im);
    }

    private void fellTree(Block b) {
        boolean isTree = false;
        Block temp = b;
        while (isBlock(temp, BlockFace.DOWN) || temp.getRelative(BlockFace.DOWN).getType().equals(Material.DIRT)) {
            temp = temp.getRelative(BlockFace.DOWN);
            if (temp.getType().equals(Material.DIRT)) {
                isTree = true;
            }
        }

        consoleSendMessage("isTree: ", isTree + "");

        if (isTree) {

            Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Block block = b;
                    BlockFace bf = BlockFace.UP;
                    while (isBlock(block, bf)) {
                        block = breakBlock(block, bf);
                        Block block_up = block;
                        breakingBad(block_up);

                    }
                }
            });

            Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Block block = b;
                    BlockFace bf = BlockFace.DOWN;
                    while (isBlock(block, bf)) {
                        block = breakBlock(block, bf);
                    }
                }
            });

        }
    }

    private void breakingBad(Block b) {
        Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
            @Override
            public void run() {
                Block block = b;
                BlockFace bf = BlockFace.WEST;
                while (isBlock(block, bf)) {
                    block = breakBlock(block, bf);
                    breakingBad(block);
                }
            }
        });

        Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
            @Override
            public void run() {
                Block block = b;
                BlockFace bf = BlockFace.NORTH;
                while (isBlock(block, bf)) {
                    block = breakBlock(block, bf);
                    breakingBad(block);
                }
            }
        });

        Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
            @Override
            public void run() {
                Block block = b;
                BlockFace bf = BlockFace.SOUTH;
                while (isBlock(block, bf)) {
                    block = breakBlock(block, bf);
                    breakingBad(block);
                }
            }
        });

        Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
            @Override
            public void run() {
                Block block = b;
                BlockFace bf = BlockFace.EAST;
                while (isBlock(block, bf)) {
                    block = breakBlock(block, bf);
                    breakingBad(block);
                }
            }
        });

        Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
            @Override
            public void run() {
                Block block = b;
                BlockFace bf = BlockFace.DOWN;
                while (isBlock(block, bf)) {
                    block = breakBlock(block, bf);
                    breakingBad(block);
                }
            }
        });
        
        Bukkit.getScheduler().runTask(RelluEssentials.getInstance(), new Runnable() {
            @Override
            public void run() {
                Block block = b;
                BlockFace bf = BlockFace.UP;
                while (isBlock(block, bf)) {
                    block = breakBlock(block, bf);
                    breakingBad(block);
                }
            }
        });
    }

    private boolean isBlock(Block b, BlockFace bf) {
        return TypeHelper.isBlockOnOfMaterials(b.getRelative(bf), Arrays.asList(logs)) || TypeHelper.isBlockOnOfMaterials(b.getRelative(bf), Arrays.asList(leaves));
    }

    private Block breakBlock(Block b, BlockFace bf) {
        b = b.getRelative(bf);
        b.breakNaturally();
        spawnParticle(b);

        consoleSendMessage("bf: ", bf.name() + " loc: " + locationToString(b.getLocation()) + " material: " + b.getType().name());

        return b;
    }

    private void spawnParticle(Block block) {
        block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 10, block.getType().createBlockData());
    }
}
