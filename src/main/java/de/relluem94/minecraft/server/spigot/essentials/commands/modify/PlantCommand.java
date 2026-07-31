package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.checkAndRemoveProtection;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.forEachBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.isPlantMaterial;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class PlantCommand implements SubCommand {

  private final int blocksPerTick;
  private final SelectionResolver selectionResolver;
  private final UndoHistoryManager undoHistoryManager;

  public PlantCommand(int blocksPerTick, SelectionResolver selectionResolver,
      UndoHistoryManager undoHistoryManager) {
    this.blocksPerTick = blocksPerTick;
    this.selectionResolver = selectionResolver;
    this.undoHistoryManager = undoHistoryManager;
  }

  @Override
  public void execute(Player player, String[] args) {
    Material material = Material.getMaterial(args[1].toUpperCase());
    if (material == null || !isPlantMaterial(material)) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_WRONG_MATERIAL));
      return;
    }

    Selection selection = selectionResolver.resolve(player);
      if (selection == null) {
          return;
      }

    BlockHelper blockHelper = new BlockHelper(material);
    List<ModifyHistoryEntry> history = new ArrayList<>();

    final long[] currentDelay = {0};
    final int[] counter = {0};

    forEachBlock(selection, block -> {
      Block below = block.getRelative(0, -1, 0);
        if (!below.getType().isSolid()) {
            return;
        }
        if (!block.isEmpty()) {
            return;
        }
        if (material.equals(block.getType())) {
            return;
        }

      checkAndRemoveProtection(block);
      history.add(
          new ModifyHistoryEntry(block.getLocation(), block.getType(), block.getBlockData()));
      blockHelper.addLocation(block.getLocation(), currentDelay[0]);
      counter[0]++;
      if (counter[0] >= blocksPerTick) {
        currentDelay[0]++;
        counter[0] = 0;
      }
    });

    blockHelper.setBlocks(0);
    undoHistoryManager.add(player, history);

    player.sendMessage(
        languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_PLANT_STARTED, history.size(),
            material.name()));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 2 && Modify.Commands.PLANT.getName().equalsIgnoreCase(args[0]);
  }
}