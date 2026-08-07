package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.normalizeYaw;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PasteCommand implements SubCommand {

  private final int blocksPerTick;
  private final UndoHistoryService undoHistoryService;
  private final TranslationService translationService;

  public PasteCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.undoHistoryService = serviceContext.getUndoHistoryService();
    this.translationService = serviceContext.getTranslationService();
  }

  @Override
  public void execute(Player player, String[] args) {
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardStore = RelluEssentials.getInstance().clipboard.get(
        player);
    if (clipboardStore == null || clipboardStore.getSecondValue() == null
        || clipboardStore.getSecondValue().isEmpty()) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_NO_CLIPBOARD));
      return;
    }

    List<ModifyHistoryEntry> history = new ArrayList<>();
    final long[] currentDelay = {0};
    final int[] counter = {0};

    Location playerTargetLoc = player.getLocation().clone();
    playerTargetLoc.setX(playerTargetLoc.getBlockX());
    playerTargetLoc.setY(playerTargetLoc.getBlockY());
    playerTargetLoc.setZ(playerTargetLoc.getBlockZ());

    float yaw = normalizeYaw(player.getLocation().getYaw());

    for (ModifyClipboardEntry entry : clipboardStore.getSecondValue()) {
      Block block = getBlock(entry, yaw, playerTargetLoc);

      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      checkAndRemoveProtection(block);

      new BukkitRunnable() {
        @Override
        public void run() {
          block.setType(entry.getMaterial());
          block.setBlockData(entry.getData());
        }
      }.runTaskLater(RelluEssentials.getInstance(), currentDelay[0]);

      counter[0]++;
      if (counter[0] >= blocksPerTick) {
        currentDelay[0]++;
        counter[0] = 0;
      }
    }

    undoHistoryService.addHistory(player, history);
    player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_PASTE_STARTED,
        clipboardStore.getSecondValue().size()));
  }

  @Override
  public boolean matches(String[] args) {
    return args.length == 1
        && Modify.Commands.PASTE.getName().equalsIgnoreCase(args[0]);
  }
}