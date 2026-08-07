package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.SelectionService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.services.UndoHistoryService;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReplaceCommand implements SubCommand {

  private final int blocksPerTick;
  private final SelectionService selectionService;
  private final UndoHistoryService undoHistoryService;
  private final TranslationService translationService;

  public ReplaceCommand(ServiceContext serviceContext, int blocksPerTick) {
    this.blocksPerTick = blocksPerTick;
    this.selectionService = serviceContext.getSelectionService();
    this.undoHistoryService = serviceContext.getUndoHistoryService();
    this.translationService = serviceContext.getTranslationService();
  }

  @Override
  public void execute(Player player, String[] args) {
    Material fromMaterial = Material.getMaterial(args[1].toUpperCase());
    Material toMaterial = Material.getMaterial(args[2].toUpperCase());

    if (fromMaterial == null || toMaterial == null) {
      player.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
      return;
    }

    Selection selection = selectionService.resolve(player);
    if (selection == null) {
      return;
    }

    BlockHelper blockHelper = new BlockHelper(toMaterial);
    BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
    List<ModifyHistoryEntry> history = new ArrayList<>();

    forEachBlock(selection, block -> {
      if (block.getType() == toMaterial) {
        return;
      }
      if (block.getType() != fromMaterial) {
        return;
      }

      checkAndRemoveProtection(block);
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      blockProcessor.process(block, blockHelper);
    });

    blockHelper.setBlocks(0);
    undoHistoryService.addHistory(player, history);
    player.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_REPLACE_STARTED, history.size(),
            fromMaterial.name(), toMaterial.name()));
  }

  @Override
  public boolean matches(String[] args) {
    return args.length == 3
        && Modify.Commands.REPLACE.getName().equalsIgnoreCase(args[0]);
  }
}