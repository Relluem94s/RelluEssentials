package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.BlockProcessor;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.SelectionResolver;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BlockHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyHistoryEntry;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SetCommand implements SubCommand {

  private final int blocksPerTick;
  private final SelectionResolver selectionResolver;
  private final UndoHistoryManager undoHistoryManager;

  public SetCommand(int blocksPerTick, SelectionResolver selectionResolver,
      UndoHistoryManager undoHistoryManager) {
    this.blocksPerTick = blocksPerTick;
    this.selectionResolver = selectionResolver;
    this.undoHistoryManager = undoHistoryManager;
  }

  @Override
  public void execute(Player player, String[] args) {
    Material material = Material.getMaterial(args[1].toUpperCase());
    if (material == null) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
      return;
    }

    Selection selection = selectionResolver.resolve(player);
      if (selection == null) {
          return;
      }

    BlockHelper blockHelper = new BlockHelper(material);
    BlockProcessor blockProcessor = new BlockProcessor(blocksPerTick);
    List<ModifyHistoryEntry> history = new ArrayList<>();

    forEachBlock(selection, block -> {
        if (material.equals(block.getType())) {
            return;
        }

      checkAndRemoveProtection(block);
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      blockProcessor.process(block, blockHelper);
    });

    blockHelper.setBlocks(0);
    undoHistoryManager.add(player, history);
    player.sendMessage(
        languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_SET_STARTED, history.size(),
            material.name()));
  }

  @Override
  public boolean matches(String[] args) {
    return args.length == 2
        && Modify.Commands.SET.getName().equalsIgnoreCase(args[0]);
  }
}