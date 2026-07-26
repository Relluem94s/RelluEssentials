package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.rellulib.stores.DoubleStore;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.*;

public class CopyCommand implements SubCommand {

    private final boolean isCut;
    private final int blocksPerTick;
    private final SelectionResolver selectionResolver;
    private final UndoHistoryManager undoHistoryManager;

    public CopyCommand(boolean isCut, int blocksPerTick, SelectionResolver selectionResolver, UndoHistoryManager undoHistoryManager) {
        this.isCut = isCut;
        this.blocksPerTick = blocksPerTick;
        this.selectionResolver = selectionResolver;
        this.undoHistoryManager = undoHistoryManager;
    }

    @Override
    public void execute(Player player, String[] args) {
        Selection selection = selectionResolver.resolve(player);
        if (selection == null) return;

        List<ModifyClipboardEntry> clipboardList = new ArrayList<>();
        List<ModifyHistoryEntry> history = new ArrayList<>();

        BlockHelper blockHelper = new BlockHelper(Material.AIR);
        BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);

        Location playerTargetLoc = player.getLocation().clone();
        playerTargetLoc.setX(playerTargetLoc.getBlockX());
        playerTargetLoc.setY(playerTargetLoc.getBlockY());
        playerTargetLoc.setZ(playerTargetLoc.getBlockZ());

        Selection newSelection = getRelativeCopySelection(selection, playerTargetLoc);

        forEachBlock(selection, block -> {
            ModifyHistoryEntry historyEntry = new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData());
            ModifyClipboardEntry clipboardEntry = getModifyClipboardEntry(block, player, playerTargetLoc);
            clipboardList.add(clipboardEntry);

            if (isCut) {
                history.add(historyEntry);
                checkAndRemoveProtection(block);
                blockProcessor.process(block, blockHelper);
            }
        });

        if (isCut) {
            blockHelper.setBlocks(0);
            undoHistoryManager.add(player, history);
        }

        RelluEssentials.getInstance().clipboard.put(player, new DoubleStore<>(newSelection, clipboardList));
        player.sendMessage(
                isCut
                        ? languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_CUT_STARTED, clipboardList.size())
                        : languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_COPY_STARTED, clipboardList.size())
        );
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1
                && (isCut
                ? Modify.Commands.CUT.getName().equalsIgnoreCase(args[0])
                : Modify.Commands.COPY.getName().equalsIgnoreCase(args[0]));
    }
}