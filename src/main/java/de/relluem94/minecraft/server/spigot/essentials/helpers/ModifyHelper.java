package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModifyHelper {
    private ModifyHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static float normalizeYaw(float yaw) {
        yaw = ((yaw % 360) + 360) % 360;
        return Math.round(yaw / 90.0f) * 90.0f % 360.0f;
    }

    @Contract(pure = true)
    public static int @NonNull [] worldToLocal(int dx, int dz, float yaw) {
        int roundedYaw = ((Math.round(yaw) % 360) + 360) % 360;
        return switch (roundedYaw) {
            case 90 -> new int[]{-dz, dx};
            case 180 -> new int[]{-dx, -dz};
            case 270 -> new int[]{dz, -dx};
            default -> new int[]{dx, dz};
        };
    }

    @Contract(pure = true)
    public static int @NonNull [] relativeToWorld(int relX, int relZ, float yaw) {
        int roundedYaw = ((Math.round(yaw) % 360) + 360) % 360;
        return switch (roundedYaw) {
            case 90 -> new int[]{-relZ, relX};
            case 180 -> new int[]{-relX, -relZ};
            case 270 -> new int[]{relZ, -relX};
            default -> new int[]{relX, relZ};
        };
    }

    public static @NonNull Block getBlock(@NonNull ModifyClipboardEntry entry, float yaw, @NonNull Location playerTargetLoc) {
        int relX = entry.getLocation().getBlockX();
        int relY = entry.getLocation().getBlockY();
        int relZ = entry.getLocation().getBlockZ();

        int[] world = relativeToWorld(relX, relZ, yaw);

        Location newLoc = new Location(
                playerTargetLoc.getWorld(),
                playerTargetLoc.getBlockX() + world[0],
                playerTargetLoc.getBlockY() + relY,
                playerTargetLoc.getBlockZ() + world[1]
        );
        return newLoc.getBlock();
    }

    public static @NotNull Selection getRelativeCopySelection(@NotNull Selection selection, Location playerTargetLoc) {
        Location pos1 = selection.getPos1().clone().subtract(playerTargetLoc);
        Location pos2 = selection.getPos2().clone().subtract(playerTargetLoc);
        pos1.setYaw(playerTargetLoc.getYaw());
        pos1.setPitch(playerTargetLoc.getPitch());
        pos2.setYaw(playerTargetLoc.getYaw());
        pos2.setPitch(playerTargetLoc.getPitch());
        return new Selection(pos1, pos2);
    }


    public static boolean isPlantMaterial(Material material) {
        return Tag.FLOWERS.isTagged(material)
                || Tag.SAPLINGS.isTagged(material)
                || Tag.CROPS.isTagged(material)
                || material == Material.BAMBOO
                || material == Material.BAMBOO_SAPLING
                || material == Material.SUGAR_CANE
                || material == Material.CACTUS
                || material == Material.SWEET_BERRY_BUSH
                || material == Material.KELP
                || material == Material.SEA_PICKLE
                || material == Material.LILY_PAD;
    }

    public static @NonNull ModifyClipboardEntry getModifyClipboardEntry(@NonNull Block block, @NonNull Player p, @NonNull Location playerTargetLoc) {
        float yaw = normalizeYaw(p.getLocation().getYaw());

        Location relLoc = new Location(
                block.getWorld(),
                block.getX() - playerTargetLoc.getBlockX(),
                block.getY() - playerTargetLoc.getBlockY(),
                block.getZ() - playerTargetLoc.getBlockZ()
        );

        int[] local = worldToLocal(relLoc.getBlockX(), relLoc.getBlockZ(), yaw);
        Location localLoc = new Location(block.getWorld(), local[0], relLoc.getBlockY(), local[1]);
        return new ModifyClipboardEntry(localLoc, block.getType(), block.getBlockData());
    }


    public static @NonNull DoubleStore<Selection, List<ModifyClipboardEntry>> rotate(@NotNull List<ModifyClipboardEntry> entries, @NotNull Selection selection) {
        List<ModifyClipboardEntry> rotatedEntries = entries.stream()
                .map(entry -> {
                    Location oldLocation = entry.getLocation();
                    int newX = oldLocation.getBlockZ();
                    int newZ = -oldLocation.getBlockX();
                    int newY = oldLocation.getBlockY();

                    Location newLocation = new Location(oldLocation.getWorld(), newX, newY, newZ);
                    return new ModifyClipboardEntry(newLocation, entry.getMaterial(), entry.getData());
                })
                .toList();

        int minX = rotatedEntries.stream().mapToInt(e -> e.getLocation().getBlockX()).min().orElse(0);
        int minZ = rotatedEntries.stream().mapToInt(e -> e.getLocation().getBlockZ()).min().orElse(0);

        List<ModifyClipboardEntry> normalizedEntries = rotatedEntries.stream()
                .map(entry -> {
                    Location loc = entry.getLocation();
                    Location normalizedLoc = new Location(
                            loc.getWorld(),
                            loc.getBlockX() - minX,
                            loc.getBlockY(),
                            loc.getBlockZ() - minZ + 1
                    );
                    return new ModifyClipboardEntry(normalizedLoc, entry.getMaterial(), entry.getData());
                })
                .collect(Collectors.toList());

        int maxX = normalizedEntries.stream().mapToInt(e -> e.getLocation().getBlockX()).max().orElse(0);
        int maxZ = normalizedEntries.stream().mapToInt(e -> e.getLocation().getBlockZ()).max().orElse(0);
        int minY = selection.getMinY();
        int maxY = selection.getMaxY();

        Location newPos1 = new Location(selection.getWorld(), 0, minY, 0,
                selection.getPos1().getYaw(), selection.getPos1().getPitch());
        Location newPos2 = new Location(selection.getWorld(), maxX, maxY, maxZ,
                selection.getPos2().getYaw(), selection.getPos2().getPitch());

        Selection rotatedSelection = new Selection(newPos1, newPos2);
        return new DoubleStore<>(rotatedSelection, normalizedEntries);
    }

    public static void undo(@NotNull ModifyHistoryEntry entry) {
        entry.getLocation().getBlock().setType(entry.getMaterial());
        entry.getLocation().getBlock().setBlockData(entry.getData());
    }


    public static void addUndoHistory(Player p, List<ModifyHistoryEntry> history) {
        List<List<ModifyHistoryEntry>> playerUndoList = RelluEssentials.getInstance().undo.getOrDefault(p, new ArrayList<>());
        playerUndoList.add(history);
        RelluEssentials.getInstance().undo.put(p, playerUndoList);
    }

    public static void checkAndRemoveProtection(Block block) {
        if (ProtectionHelper.isLockAble(block)) {
            ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(block.getLocation());

            if (protection != null) {
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(protection);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(block.getLocation());
            }
        }
    }

    public static void forEachBlock(@NotNull Selection selection, Consumer<Block> action) {
        for (int y = selection.getMinY(); y <= selection.getMaxY(); y++) {
            for (int x = selection.getMinX(); x <= selection.getMaxX(); x++) {
                for (int z = selection.getMinZ(); z <= selection.getMaxZ(); z++) {
                    Block block = new Location(selection.getWorld(), x, y, z).getBlock();
                    action.accept(block);
                }
            }
        }
    }

}
