package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper.getPlayerDirection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isInt;

public class MoveCommand implements SubCommand {

    private final int blocksPerTick;
    private final SelectionResolver selectionResolver;
    private final UndoHistoryManager undoHistoryManager;

    public MoveCommand(int blocksPerTick, SelectionResolver selectionResolver, UndoHistoryManager undoHistoryManager) {
        this.blocksPerTick = blocksPerTick;
        this.selectionResolver = selectionResolver;
        this.undoHistoryManager = undoHistoryManager;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!isInt(args[1])) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_INVALID));
            return;
        }

        int offset = Integer.parseInt(args[1]);
        Selection selection = selectionResolver.resolve(player);
        if (selection == null) return;

        List<ModifyHistoryEntry> history = new ArrayList<>();
        final long[] currentDelay = {0};
        final int[] counter = {0};

        Vector direction = getPlayerDirection(player).multiply(offset);

        forEachBlock(selection, block -> {
            history.add(new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));

            Location targetLocation = block.getLocation().clone().add(direction);
            Block targetBlock = targetLocation.getBlock();

            history.add(new ModifyHistoryEntry(targetBlock.getLocation(), targetBlock.getType(), targetBlock.getBlockData()));

            checkAndRemoveProtection(block);
            checkAndRemoveProtection(targetBlock);

            new BukkitRunnable() {
                @Override
                public void run() {
                    targetBlock.setType(block.getType());
                    targetBlock.setBlockData(block.getBlockData());
                    block.setType(Material.AIR);
                }
            }.runTaskLater(RelluEssentials.getInstance(), currentDelay[0]);

            counter[0]++;
            if (counter[0] >= blocksPerTick) {
                currentDelay[0]++;
                counter[0] = 0;
            }
        });

        undoHistoryManager.add(player, history);
        player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_MOVE_STARTED, history.size(), offset));
    }

    @Override
    public boolean matches(String[] args) {
        return args.length == 2
                && Modify.Commands.MOVE.getName().equalsIgnoreCase(args[0]);
    }
}