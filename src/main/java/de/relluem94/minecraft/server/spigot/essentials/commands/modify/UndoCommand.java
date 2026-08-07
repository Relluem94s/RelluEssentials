package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.undo;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class UndoCommand implements SubCommand {

  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  public UndoCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = serviceContext;
  }

  @Override
  public void execute(Player player, String[] args) {
    List<ModifyHistoryEntry> lastHistory = serviceContext.getUndoHistoryService()
        .popLastHistory(player);

    if (lastHistory == null || lastHistory.isEmpty()) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_MODIFY_NO_UNDO_HISTORY));
      return;
    }

    long currentDelay = 0;
    int counter = 0;
    for (ModifyHistoryEntry entry : lastHistory) {
      long finalDelay = currentDelay;
      Bukkit.getServer().getScheduler()
          .scheduleSyncDelayedTask(RelluEssentials.getInstance(), () -> undo(entry),
              Math.abs(finalDelay));
      counter++;
      if (counter >= blocksPerTick) {
        currentDelay++;
        counter = 0;
      }
    }

    player.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_MODIFY_UNDO_STARTED,
            lastHistory.size()));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Modify.Commands.UNDO.getName().equalsIgnoreCase(args[0]);
  }
}