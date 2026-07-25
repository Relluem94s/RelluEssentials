package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;

public class ReplaceCommand implements SubCommand {

    private final int blocksPerTick;
    private final SelectionResolver selectionResolver;
    private final UndoHistoryManager undoHistoryManager;

    public ReplaceCommand(int blocksPerTick, SelectionResolver selectionResolver, UndoHistoryManager undoHistoryManager) {
        this.blocksPerTick = blocksPerTick;
        this.selectionResolver = selectionResolver;
        this.undoHistoryManager = undoHistoryManager;
    }

    @Override
    public void execute(Player player, String[] args) {
        Material fromMaterial = Material.getMaterial(args[1].toUpperCase());
        Material toMaterial = Material.getMaterial(args[2].toUpperCase());

        if (fromMaterial == null || toMaterial == null) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
            return;
        }

        Selection selection = selectionResolver.resolve(player);
        if (selection == null) return;

        BlockHelper blockHelper = new BlockHelper(toMaterial);
        BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
        List<ModifyHistoryEntry> history = new ArrayList<>();

        forEachBlock(selection, block -> {
            if (block.getType() == toMaterial) return;
            if (block.getType() != fromMaterial) return;

            checkAndRemoveProtection(block);
            history.add(new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
            blockProcessor.process(block, blockHelper);
        });

        blockHelper.setBlocks(0);
        undoHistoryManager.add(player, history);
        player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_REPLACE_STARTED, history.size(), fromMaterial.name(), toMaterial.name()));
    }

    @Override
    public boolean matches(String[] args) {
        return args.length == 3
                && Modify.Commands.REPLACE.getName().equalsIgnoreCase(args[0]);
    }
}