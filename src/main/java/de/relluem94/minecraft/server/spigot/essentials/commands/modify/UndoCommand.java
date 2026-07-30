package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.undo;

public class UndoCommand implements SubCommand {

    private final int blocksPerTick;
    private final UndoHistoryManager undoHistoryManager;

    public UndoCommand(int blocksPerTick, UndoHistoryManager undoHistoryManager) {
        this.blocksPerTick = blocksPerTick;
        this.undoHistoryManager = undoHistoryManager;
    }

    @Override
    public void execute(Player player, String[] args) {
        List<ModifyHistoryEntry> lastHistory = undoHistoryManager.popLastHistory(player);

        if (lastHistory == null || lastHistory.isEmpty()) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_NO_UNDO_HISTORY));
            return;
        }

        long currentDelay = 0;
        int counter = 0;
        for (ModifyHistoryEntry entry : lastHistory) {
            long finalDelay = currentDelay;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RelluEssentials.getInstance(), () -> undo(entry), Math.abs(finalDelay));
            counter++;
            if (counter >= blocksPerTick) {
                currentDelay++;
                counter = 0;
            }
        }

        player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_UNDO_STARTED, lastHistory.size()));
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && Modify.Commands.UNDO.getName().equalsIgnoreCase(args[0]);
    }
}