package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getModifyClipboardEntry;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.getRelativeCopySelection;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CopyCommand implements SubCommand {

  private final boolean isCut;
  private final int blocksPerTick;
  private final ServiceContext serviceContext;

  public CopyCommand(boolean isCut, int blocksPerTick, ServiceContext context) {
    this.isCut = isCut;
    this.blocksPerTick = blocksPerTick;
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    Selection selection = serviceContext.getSelectionService().resolve(player);
    if (selection == null) {
      return;
    }

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
      ModifyHistoryEntry historyEntry = new ModifyHistoryEntry(block.getLocation(), block.getType(),
          block.getBlockData());
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
      serviceContext.getUndoHistoryService().addHistory(player, history);
    }

    RelluEssentials.getInstance().clipboard.put(player,
        new DoubleStore<>(newSelection, clipboardList));
    player.sendMessage(
        isCut
            ? serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_CUT_STARTED,
                clipboardList.size())
            : serviceContext.getTranslationService()
                .getWithPrefix(MessageKey.COMMAND_MODIFY_COPY_STARTED,
                    clipboardList.size())
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