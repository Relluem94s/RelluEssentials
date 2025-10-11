package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isInt;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

@CommandName("modify")
public class Modify implements CommandConstruct {

    public static final int BLOCKS_PER_TICK = 64;

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        SET("set"),
        REPLACE("replace"),
        MOVE("move"),
        UNDO("undo");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())){
            commandSender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if(!isPlayer(commandSender)){
            commandSender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) commandSender;

        if(strings.length == 0){
            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if(strings.length == 1){
            if(!Commands.UNDO.getName().equalsIgnoreCase(strings[0])){
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

        if(strings.length == 2){
            if(Commands.MOVE.getName().equalsIgnoreCase(strings[0])){
                if(!isInt(strings[1])){
                    p.sendMessage(PLUGIN_COMMAND_INVALID);
                }

                int offset = Integer.parseInt(strings[1]);

                Selection selection = getSelection(p);
                if(selection == null) return true;

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


            if(!Commands.SET.getName().equalsIgnoreCase(strings[0])){
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }

            Material material = Material.getMaterial(strings[1].toUpperCase());

            if(material == null){
                p.sendMessage(PLUGIN_COOMAND_MODIFY_WRONG_MATERIAL);
                return true;
            }

            Selection selection = getSelection(p);
            if(selection == null) return true;

            BlockHelper bh = new BlockHelper(material);
            List<ModifyHistoryEntry> history = new ArrayList<>();

            final long[] currentDelay = {0};
            final int[] counter = {0};


            forEachBlock(selection, block -> {
                if(material.equals(block.getType())){
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

        if(strings.length == 3){
            if(!Commands.REPLACE.getName().equalsIgnoreCase(strings[0])){
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }

            Material fromMaterial = Material.getMaterial(strings[1].toUpperCase());
            Material toMaterial = Material.getMaterial(strings[2].toUpperCase());

            if(fromMaterial == null || toMaterial == null){
                p.sendMessage(PLUGIN_COOMAND_MODIFY_WRONG_MATERIAL);
                return true;
            }

            Selection selection = getSelection(p);
            if(selection == null) return true;

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

    private @Nullable Selection getSelection(Player p) {
        if(!RelluEssentials.getInstance().position.containsKey(p)){
            p.sendMessage(PLUGIN_COOMAND_MODIFY_NO_POSITIONS);
            return null;
        }

        Location pos1 = RelluEssentials.getInstance().position.get(p).getValue();
        Location pos2 = RelluEssentials.getInstance().position.get(p).getSecondValue();

        if(pos1 == null){
            p.sendMessage(PLUGIN_COOMAND_MODIFY_POS_1_EMPTY);
            return null;
        }

        if(pos2 == null){
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
        if(ProtectionHelper.isLockAble(block)){
            ProtectionEntry protection = RelluEssentials.getInstance().getProtectionAPI().getProtectionEntry(block.getLocation());

            if(protection != null){
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

    private void undo(@NotNull ModifyHistoryEntry entry) {
        entry.getLocation().getBlock().setType(entry.getMaterial());
        entry.getLocation().getBlock().setBlockData(entry.getData());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if(!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())){
            return tabList;
        }

        if(strings.length == 1){
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }

        if(strings[0].equalsIgnoreCase(Commands.UNDO.getName())){
            return tabList;
        }

        if(strings[0].equalsIgnoreCase(Commands.MOVE.getName())){
            return tabList;
        }


        if(strings.length == 2){
            tabList.addAll(TabCompleterHelper.getMaterials(strings[1].isEmpty() ? null : strings[1]));
            return tabList;
        }

        if(strings.length == 3){
            if(!strings[0].equalsIgnoreCase(Commands.REPLACE.getName())){
                return tabList;
            }

            tabList.addAll(TabCompleterHelper.getMaterials(strings[2].isEmpty() ? null : strings[2]));
            return tabList;
        }

        return tabList;
    }
}
