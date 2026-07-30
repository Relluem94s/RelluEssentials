package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.*;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isInt;

public class FillCommand implements SubCommand {

    private static final int[][] HORIZONTAL_DIRECTIONS = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
    private static final int[][] ALL_DIRECTIONS = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, -1, 0}};

    private final boolean recursive;
    private final int blocksPerTick;
    private final int maxRadius;
    private final int maxIterations;
    private final UndoHistoryManager undoHistoryManager;

    public FillCommand(boolean recursive, int blocksPerTick, int maxRadius, int maxIterations, UndoHistoryManager undoHistoryManager) {
        this.recursive = recursive;
        this.blocksPerTick = blocksPerTick;
        this.maxRadius = maxRadius;
        this.maxIterations = maxIterations;
        this.undoHistoryManager = undoHistoryManager;
    }

    @Override
    public void execute(Player player, String[] args) {
        Material material = Material.getMaterial(args[1].toUpperCase());
        if (material == null) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
            return;
        }

        if (!isInt(args[2])) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_INVALID));
            return;
        }

        int radius = Integer.parseInt(args[2]);
        if (radius <= 0) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_INVALID));
            return;
        }

        if (radius > maxRadius) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_FILL_RADIUS_TO_HIGH, maxRadius));
        }

        BlockHelper blockHelper = new BlockHelper(material);
        List<ModifyHistoryEntry> history = new ArrayList<>();
        final long[] currentDelay = {0};
        final int[] counter = {0};

        Location playerPos = player.getLocation().clone();
        Queue<Block> queue = new ArrayDeque<>();
        Set<Location> visited = new HashSet<>();

        Block startBlock = playerPos.getBlock();
        queue.add(startBlock);
        visited.add(startBlock.getLocation());

        int[][] directions = recursive ? ALL_DIRECTIONS : HORIZONTAL_DIRECTIONS;

        int iterations = 0;
        while (!queue.isEmpty()) {
            if (++iterations >= maxIterations) break;

            Block block = queue.poll();
            if (block.getLocation().distance(playerPos) > radius) continue;
            if (!block.isEmpty()) continue;

            checkAndRemoveProtection(block);
            history.add(new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
            blockHelper.addLocation(block.getLocation(), currentDelay[0]);
            counter[0]++;
            if (counter[0] >= blocksPerTick) {
                currentDelay[0]++;
                counter[0] = 0;
            }

            for (int[] dir : directions) {
                int neighborX = block.getX() + dir[0];
                int neighborY = block.getY() + dir[1];
                int neighborZ = block.getZ() + dir[2];
                Location neighborLocation = new Location(block.getWorld(), neighborX, neighborY, neighborZ);
                if (visited.contains(neighborLocation)) continue;
                Block neighbor = block.getWorld().getBlockAt(neighborX, neighborY, neighborZ);
                if (!neighbor.isEmpty()) continue;
                queue.add(neighbor);
                visited.add(neighborLocation);
            }
        }

        blockHelper.setBlocks(0);
        undoHistoryManager.add(player, history);

        player.sendMessage(
                recursive
                        ? languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_FILLR_STARTED, history.size(), material.name(), radius)
                        : languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_FILL_STARTED, history.size(), material.name(), radius)
        );
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 3 && (
                recursive
                        ? Modify.Commands.FILLR.getName().equalsIgnoreCase(args[0])
                        : Modify.Commands.FILL.getName().equalsIgnoreCase(args[0])
        );
    }
}