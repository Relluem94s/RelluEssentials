package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.rellulib.stores.DoubleStore;
import de.relluem94.rellulib.stores.TrippleStore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isInt;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("modify")
public class Modify implements CommandConstruct {

    public static final int BLOCKS_PER_TICK = 64;
    public static final int MAX_RADIUS = 128;
    public static final int MAX_ITERATIONS = 1048576;

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        SET("set"),
        REPLACE("replace"),
        MOVE("move"),
        COPY("copy"),
        CUT("cut"),
        PASTE("paste"),
        CLIPBOARD("clipboard", "rotate"),
        UNDO("undo"),
        WALL("wall"),
        CYLINDER("cylinder"),
        FILL("fill"),
        FILLR("fillr");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] strings) {
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            commandSender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (!isPlayer(commandSender)) {
            commandSender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) commandSender;

        if (strings.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if (strings.length == 1) {
            if (Commands.COPY.getName().equalsIgnoreCase(strings[0]) || Commands.CUT.getName().equalsIgnoreCase(strings[0])) {

                Selection selection = getSelection(p);
                if (selection == null) return true;

                List<ModifyClipboardEntry> clipboardList = new ArrayList<>();
                List<ModifyHistoryEntry> history = new ArrayList<>();

                final long[] currentDelay = {0};
                final int[] counter = {0};

                BlockHelper bh = new BlockHelper(Material.AIR);
                Location playerTargetLoc = p.getLocation().clone();
                Selection newSelection = getRelativeCopySelection(selection, playerTargetLoc);

                forEachBlock(selection, block -> {
                    ModifyHistoryEntry entry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());


                    ModifyClipboardEntry clipboardEntry = new ModifyClipboardEntry(block.getLocation().clone().subtract(playerTargetLoc), block.getType(), block.getBlockData());
                    clipboardList.add(clipboardEntry);

                    if (Commands.CUT.getName().equalsIgnoreCase(strings[0])) {
                        history.add(entry);
                        checkAndRemoveProtection(block);
                        bh.addLocation(block.getLocation(), currentDelay[0]);

                        counter[0]++;
                        if (counter[0] >= BLOCKS_PER_TICK) {
                            currentDelay[0]++;
                            counter[0] = 0;
                        }
                    }
                });

                if (Commands.CUT.getName().equalsIgnoreCase(strings[0])) {
                    bh.setBlocks(0);
                    addUndoHistory(p, history);
                }

                RelluEssentials.getInstance().clipboard.put(p, new DoubleStore<>(newSelection, clipboardList));

                p.sendMessage(String.format(
                        Commands.CUT.getName().equalsIgnoreCase(strings[0]) ? PLUGIN_COOMAND_MODIFY_CUT_STARTED : PLUGIN_COOMAND_MODIFY_COPY_STARTED,
                        clipboardList.size()
                ));
                return true;
            }

            if (Commands.PASTE.getName().equalsIgnoreCase(strings[0])) {
                DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardList = RelluEssentials.getInstance().clipboard.get(p);
                if (clipboardList == null || clipboardList.getSecondValue() == null || clipboardList.getSecondValue().isEmpty()) {
                    p.sendMessage(PLUGIN_COOMAND_MODIFY_NO_CLIPBOARD);
                    return true;
                }

                List<ModifyHistoryEntry> history = new ArrayList<>();
                final long[] currentDelay = {0};
                final int[] counter = {0};

                Location playerTargetLoc = p.getLocation().clone();

                for (ModifyClipboardEntry entry : clipboardList.getSecondValue()) {
                    Location newLoc = entry.getLocation().clone().add(playerTargetLoc);
                    Block block = newLoc.getBlock();

                    ModifyHistoryEntry entryNew = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
                    history.add(entryNew);

                    checkAndRemoveProtection(block);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            block.setType(entry.getMaterial());
                            block.setBlockData(entry.getData());
                        }
                    }.runTaskLater(RelluEssentials.getInstance(), currentDelay[0]);

                    counter[0]++;
                    if (counter[0] >= BLOCKS_PER_TICK) {
                        currentDelay[0]++;
                        counter[0] = 0;
                    }
                }

                addUndoHistory(p, history);
                p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_PASTE_STARTED, clipboardList.getSecondValue().size()));
                return true;
            }

            if (!Commands.UNDO.getName().equalsIgnoreCase(strings[0])) {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }

            List<List<ModifyHistoryEntry>> playerUndo = RelluEssentials.getInstance().undo.get(p);
            if (playerUndo == null || playerUndo.isEmpty()) {
                p.sendMessage(PLUGIN_COOMAND_MODIFY_NO_UNDO_HISTORY);
                return true;
            }
            List<ModifyHistoryEntry> lastHistory = playerUndo.remove(playerUndo.size() - 1);
            if (lastHistory == null || lastHistory.isEmpty()) {
                p.sendMessage(PLUGIN_COOMAND_MODIFY_NO_UNDO_HISTORY);
                return true;
            }


            long currentDelay = 0;
            int counter = 0;
            for (ModifyHistoryEntry entry : lastHistory) {
                long finalDelay = currentDelay;
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RelluEssentials.getInstance(), () -> undo(entry), Math.abs(finalDelay));
                counter++;
                if (counter >= BLOCKS_PER_TICK) {
                    currentDelay += 1;
                    counter = 0;
                }
            }

            RelluEssentials.getInstance().undo.get(p).remove(playerUndo);

            p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_UNDO_STARTED, lastHistory.size()));
            return true;
        }

        if (strings.length == 2) {
            if (Commands.WALL.getName().equalsIgnoreCase(strings[0])) {
                Material material = Material.getMaterial(strings[1].toUpperCase());
                if (material == null) {
                    p.sendMessage(PLUGIN_COOMAND_MODIFY_WRONG_MATERIAL);
                    return true;
                }

                Selection selection = getSelection(p);
                if (selection == null) return true;

                BlockHelper bh = new BlockHelper(material);
                List<ModifyHistoryEntry> history = new ArrayList<>();

                final long[] currentDelay = {0};
                final int[] counter = {0};

                forEachBlock(selection, block -> {
                    int x = block.getX();
                    int z = block.getZ();

                    if (x != selection.getMinX() && x != selection.getMaxX()
                            && z != selection.getMinZ() && z != selection.getMaxZ()) {
                        return;
                    }

                    checkAndRemoveProtection(block);

                    ModifyHistoryEntry entry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
                    history.add(entry);

                    bh.addLocation(block.getLocation(), currentDelay[0]);
                    counter[0]++;
                    if (counter[0] >= BLOCKS_PER_TICK) {
                        currentDelay[0]++;
                        counter[0] = 0;
                    }
                });

                bh.setBlocks(0);
                addUndoHistory(p, history);

                p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_WALL_STARTED, history.size(), material.name()));
                return true;
            } else if (Commands.CYLINDER.getName().equalsIgnoreCase(strings[0])) {
                Material material = Material.getMaterial(strings[1].toUpperCase());
                if (material == null) {
                    p.sendMessage(PLUGIN_COOMAND_MODIFY_WRONG_MATERIAL);
                    return true;
                }

                Selection selection = getSelection(p);
                if (selection == null) return true;

                BlockHelper bh = new BlockHelper(material);
                List<ModifyHistoryEntry> history = new ArrayList<>();

                final long[] currentDelay = {0};
                final int[] counter = {0};

                double centerX = (selection.getMinX() + selection.getMaxX() + 1) / 2.0;
                double centerZ = (selection.getMinZ() + selection.getMaxZ() + 1) / 2.0;

                double radiusX = (selection.getMaxX() - selection.getMinX() + 1) / 2.0;
                double radiusZ = (selection.getMaxZ() - selection.getMinZ() + 1) / 2.0;
                double thickness = 1.0 / Math.min(radiusX, radiusZ);

                forEachBlock(selection, block -> {
                    int x = block.getX();
                    int z = block.getZ();

                    double dx = (x + 0.5 - centerX) / radiusX;
                    double dz = (z + 0.5 - centerZ) / radiusZ;
                    double dist = dx * dx + dz * dz;

                    if (dist < 1.0 - thickness || dist > 1.0 + thickness) {
                        return;
                    }

                    checkAndRemoveProtection(block);

                    ModifyHistoryEntry entry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
                    history.add(entry);

                    bh.addLocation(block.getLocation(), currentDelay[0]);
                    counter[0]++;
                    if (counter[0] >= BLOCKS_PER_TICK) {
                        currentDelay[0]++;
                        counter[0] = 0;
                    }
                });

                bh.setBlocks(0);
                addUndoHistory(p, history);

                p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_CYLINDER_STARTED, history.size(), material.name()));
                return true;
            } else if (Commands.MOVE.getName().equalsIgnoreCase(strings[0])) {
                if (!isInt(strings[1])) {
                    p.sendMessage(PLUGIN_COMMAND_INVALID);
                }

                int offset = Integer.parseInt(strings[1]);

                Selection selection = getSelection(p);
                if (selection == null) return true;

                List<ModifyHistoryEntry> history = new ArrayList<>();

                final long[] currentDelay = {0};
                final int[] counter = {0};

                Vector direction = getPlayerDirection(p).multiply(offset);

                forEachBlock(selection, block -> {
                    ModifyHistoryEntry entry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
                    history.add(entry);

                    Location location = block.getLocation().clone().add(direction);
                    Block newBlock = location.getBlock();

                    ModifyHistoryEntry entryNewBlock = new ModifyHistoryEntry(newBlock.getLocation(), newBlock.getType(), newBlock.getBlockData());
                    history.add(entryNewBlock);

                    checkAndRemoveProtection(block);
                    checkAndRemoveProtection(newBlock);

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            newBlock.setType(block.getType());
                            newBlock.setBlockData(block.getBlockData());

                            block.setType(Material.AIR);

                        }
                    }.runTaskLater(RelluEssentials.getInstance(), currentDelay[0]);

                    counter[0]++;
                    if (counter[0] >= BLOCKS_PER_TICK) {
                        currentDelay[0] += 1;
                        counter[0] = 0;
                    }
                });

                addUndoHistory(p, history);

                p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_MOVE_STARTED, history.size(), offset));
                return true;
            }




            else if (strings[1].equalsIgnoreCase(Commands.CLIPBOARD.getSubCommands()[0])) {
                DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardList = RelluEssentials.getInstance().clipboard.get(p);
                if (clipboardList == null || clipboardList.getSecondValue() == null || clipboardList.getSecondValue().isEmpty()) {
                    p.sendMessage(PLUGIN_COOMAND_MODIFY_NO_CLIPBOARD);
                    return true;
                }

                RelluEssentials.getInstance().clipboard.put(p, rotate(clipboardList.getSecondValue(), clipboardList.getValue()));

                p.sendMessage(PLUGIN_COOMAND_MODIFY_CLIPBOARD_ROTATE_SUCCESS);
                return true;
            }





            else if (!Commands.SET.getName().equalsIgnoreCase(strings[0])) {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }

            Material material = Material.getMaterial(strings[1].toUpperCase());

            if (material == null) {
                p.sendMessage(PLUGIN_COOMAND_MODIFY_WRONG_MATERIAL);
                return true;
            }

            Selection selection = getSelection(p);
            if (selection == null) return true;

            BlockHelper bh = new BlockHelper(material);
            List<ModifyHistoryEntry> history = new ArrayList<>();

            final long[] currentDelay = {0};
            final int[] counter = {0};


            forEachBlock(selection, block -> {
                if (material.equals(block.getType())) {
                    return;
                }

                checkAndRemoveProtection(block);

                ModifyHistoryEntry entry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
                history.add(entry);

                bh.addLocation(block.getLocation(), currentDelay[0]);
                counter[0]++;
                if (counter[0] >= BLOCKS_PER_TICK) {
                    currentDelay[0] += 1;
                    counter[0] = 0;
                }
            });


            bh.setBlocks(0);

            addUndoHistory(p, history);

            p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_SET_STARTED, history.size(), material.name()));

            return true;
        }

        if (strings.length == 3) {
            if (Commands.FILL.getName().equalsIgnoreCase(strings[0]) || Commands.FILLR.getName().equalsIgnoreCase(strings[0])) {
                Material material = Material.getMaterial(strings[1].toUpperCase());
                if (material == null) {
                    p.sendMessage(PLUGIN_COOMAND_MODIFY_WRONG_MATERIAL);
                    return true;
                }

                if (!isInt(strings[2])) {
                    p.sendMessage(PLUGIN_COMMAND_INVALID);
                    return true;
                }

                int radius = Integer.parseInt(strings[2]);
                if (radius <= 0) {
                    p.sendMessage(PLUGIN_COMMAND_INVALID);
                    return true;
                }

                if(radius > MAX_RADIUS){
                    p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_FILL_RADIUS_TO_HIGH, MAX_RADIUS));
                }

                BlockHelper bh = new BlockHelper(material);
                List<ModifyHistoryEntry> history = new ArrayList<>();
                final long[] currentDelay = {0};
                final int[] counter = {0};

                Location playerPos = p.getLocation().clone();

                Queue<Block> queue = new ArrayDeque<>();
                Set<Location> visited = new HashSet<>();

                Block startBlock = playerPos.getBlock();
                queue.add(startBlock);
                visited.add(startBlock.getLocation());

                int iterations = 0;
                while(!queue.isEmpty()){
                    iterations++;
                    if(iterations >= MAX_ITERATIONS) break;

                    Block block = queue.poll();
                    if (block.getLocation().distance(playerPos) > radius) continue;
                    if (!block.isEmpty()) continue;

                    checkAndRemoveProtection(block);
                    ModifyHistoryEntry entry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
                    history.add(entry);
                    bh.addLocation(block.getLocation(), currentDelay[0]);
                    counter[0]++;
                    if (counter[0] >= BLOCKS_PER_TICK) {
                        currentDelay[0]++;
                        counter[0] = 0;
                    }

                    int[][] directions;
                    if (Commands.FILL.getName().equalsIgnoreCase(strings[0])) {
                        directions = new int[][]{{1,0,0},{-1,0,0},{0,0,1},{0,0,-1}};
                    } else {
                        directions = new int[][]{{1,0,0},{-1,0,0},{0,0,1},{0,0,-1},{0,-1,0}};
                    }

                    for (int[] dir : directions){
                        int nx = block.getX() + dir[0];
                        int ny = block.getY() + dir[1];
                        int nz = block.getZ() + dir[2];

                        Location location = new Location(block.getWorld(), nx, ny, nz);

                        if (visited.contains(location)) continue;
                        Block neighbor = location.getBlock();
                        if (!neighbor.isEmpty()) continue;
                        queue.add(neighbor);
                        visited.add(location);
                    }
                }

                bh.setBlocks(0);
                addUndoHistory(p, history);

                p.sendMessage(String.format(
                        Commands.FILL.getName().equalsIgnoreCase(strings[0]) ? PLUGIN_COOMAND_MODIFY_FILL_STARTED : PLUGIN_COOMAND_MODIFY_FILLR_STARTED,
                        history.size(), material.name(), radius
                ));
                return true;
            }

            if (!Commands.REPLACE.getName().equalsIgnoreCase(strings[0])) {
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }

            Material fromMaterial = Material.getMaterial(strings[1].toUpperCase());
            Material toMaterial = Material.getMaterial(strings[2].toUpperCase());

            if (fromMaterial == null || toMaterial == null) {
                p.sendMessage(PLUGIN_COOMAND_MODIFY_WRONG_MATERIAL);
                return true;
            }

            Selection selection = getSelection(p);
            if (selection == null) return true;

            BlockHelper bh = new BlockHelper(toMaterial);
            List<ModifyHistoryEntry> history = new ArrayList<>();

            final long[] currentDelay = {0};
            final int[] counter = {0};

            forEachBlock(selection, block -> {
                if (block.getType() == toMaterial) {
                    return;
                }

                if (block.getType() != fromMaterial) {
                    return;
                }

                checkAndRemoveProtection(block);

                ModifyHistoryEntry entry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
                history.add(entry);

                bh.addLocation(block.getLocation(), currentDelay[0]);
                counter[0]++;
                if (counter[0] >= BLOCKS_PER_TICK) {
                    currentDelay[0] += 1;
                    counter[0] = 0;
                }
            });

            bh.setBlocks(0);
            addUndoHistory(p, history);

            p.sendMessage(String.format(PLUGIN_COOMAND_MODIFY_REPLACE_STARTED, history.size(), fromMaterial.name(), toMaterial.name()));
            return true;
        }

        p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
        return true;
    }

    private static @NotNull Selection getRelativeCopySelection(@NotNull Selection selection, Location playerTargetLoc) {
        Location pos1 = selection.getPos1().clone().subtract(playerTargetLoc);
        Location pos2 = selection.getPos2().clone().subtract(playerTargetLoc);

        pos1.setYaw(playerTargetLoc.getYaw());
        pos1.setPitch(playerTargetLoc.getPitch());

        pos2.setYaw(playerTargetLoc.getYaw());
        pos2.setPitch(playerTargetLoc.getPitch());
        return new Selection(pos1, pos2);
    }


    private @Nullable Selection getSelection(Player p) {
        if (!RelluEssentials.getInstance().position.containsKey(p)) {
            p.sendMessage(PLUGIN_COOMAND_MODIFY_NO_POSITIONS);
            return null;
        }

        Location pos1 = RelluEssentials.getInstance().position.get(p).getValue();
        Location pos2 = RelluEssentials.getInstance().position.get(p).getSecondValue();

        if (pos1 == null) {
            p.sendMessage(PLUGIN_COOMAND_MODIFY_POS_1_EMPTY);
            return null;
        }

        if (pos2 == null) {
            p.sendMessage(PLUGIN_COOMAND_MODIFY_POS_2_EMPTY);
            return null;
        }

        if (pos1.getWorld() != pos2.getWorld()) {
            p.sendMessage(PLUGIN_COOMAND_MODIFY_DIFFERENT_WORLDS);
            return null;
        }

        return new Selection(pos1, pos2);
    }

    private static void addUndoHistory(Player p, List<ModifyHistoryEntry> history) {
        List<List<ModifyHistoryEntry>> playerUndoList = RelluEssentials.getInstance().undo.getOrDefault(p, new ArrayList<>());
        playerUndoList.add(history);
        RelluEssentials.getInstance().undo.put(p, playerUndoList);
    }

    private static void checkAndRemoveProtection(Block block) {
        if (ProtectionHelper.isLockAble(block)) {
            ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(block.getLocation());

            if (protection != null) {
                RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(protection);
                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(block.getLocation());
            }
        }
    }

    public void forEachBlock(@NotNull Selection selection, Consumer<Block> action) {
        for (int y = selection.getMinY(); y <= selection.getMaxY(); y++) {
            for (int x = selection.getMinX(); x <= selection.getMaxX(); x++) {
                for (int z = selection.getMinZ(); z <= selection.getMaxZ(); z++) {
                    Block block = new Location(selection.getWorld(), x, y, z).getBlock();
                    action.accept(block);
                }
            }
        }
    }

    public DoubleStore<Selection, List<ModifyClipboardEntry>> rotate(@NotNull List<ModifyClipboardEntry> entries, @NotNull Selection selection) {
        DoubleStore<Boolean, Boolean> orientation = determineOrientation(selection);

        int zShift = 0;
        int xShift = 0;

        if(orientation.getValue() && orientation.getSecondValue()){
            zShift += 123456;
        } else if(orientation.getValue()){
            zShift += 123456;
            xShift += 123456;
        } else if(orientation.getSecondValue()){
            zShift += 123456;
            xShift += 123456;
        }

        int finalXShift = xShift;
        int finalZShift = zShift;
        List<ModifyClipboardEntry> rotatedEntries = entries.stream()
                .map(entry -> {
                    Location oldLocation = entry.getLocation();
                    int newX = oldLocation.getBlockZ() + finalXShift;
                    int newZ = -oldLocation.getBlockX() + finalZShift;
                    int newY = oldLocation.getBlockY();



                    Location newLocation = new Location(oldLocation.getWorld(), newX, newY, newZ);

                    return new ModifyClipboardEntry(newLocation, entry.getMaterial(), entry.getData());
                })
                .collect(Collectors.toList());

        Selection rotatedSelection = getRotatedSelection(selection, zShift);
        return new DoubleStore<>(rotatedSelection, rotatedEntries);
    }

    @Contract("_, _ -> new")
    private static @NotNull Selection getRotatedSelection(@NotNull Selection selection, int zShift) {
        Location newPos1 = new Location(
                selection.getWorld(),
                selection.getMinZ(),
                selection.getMinY(),
                -selection.getMaxX() + zShift
        );
        Location newPos2 = new Location(
                selection.getWorld(),
                selection.getMaxZ() + 1,
                selection.getMaxY(),
                -selection.getMinX() + zShift
        );

        return new Selection(newPos1, newPos2);
    }

    /**
     *
     * @param selection Selection
     * @return DoubleStore<Boolean, Boolean>
     *     <br>
     *     if first = true X<br>
     *     if second = true Z<br>
     *     if both = true X = Z<br>
     */
    private static @NotNull DoubleStore<Boolean, Boolean> determineOrientation(@NotNull Selection selection) {
        int xLength = selection.getMaxX() - selection.getMinX() + 1;
        int zLength = selection.getMaxZ() - selection.getMinZ() + 1;

        DoubleStore<Boolean, Boolean> result = new DoubleStore<>(false, false);

        if (zLength > xLength) {
            result.setSecondValue(true);
        } else if (xLength > zLength) {
            result.setValue(true);
        } else {
            result.setValue(true);
            result.setSecondValue(true);
        }

        return result;
    }


    private void undo(@NotNull ModifyHistoryEntry entry) {
        entry.getLocation().getBlock().setType(entry.getMaterial());
        entry.getLocation().getBlock().setBlockData(entry.getData());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return tabList;
        }

        if (strings.length == 1) {
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }

        if (strings[0].equalsIgnoreCase(Commands.UNDO.getName())) {
            return tabList;
        }

        if (strings[0].equalsIgnoreCase(Commands.MOVE.getName())) {
            return tabList;
        }


        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase(Commands.CLIPBOARD.getName())) {
                tabList.addAll(List.of(Commands.CLIPBOARD.getSubCommands()));
                return tabList;
            }

            tabList.addAll(TabCompleterHelper.getMaterials(strings[1].isEmpty() ? null : strings[1]));
            return tabList;
        }

        if (strings.length == 3) {
            if (!strings[0].equalsIgnoreCase(Commands.REPLACE.getName())) {
                return tabList;
            }

            tabList.addAll(TabCompleterHelper.getMaterials(strings[2].isEmpty() ? null : strings[2]));
            return tabList;
        }

        return tabList;
    }
}
