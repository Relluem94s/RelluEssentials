package de.relluem94.minecraft.server.spigot.essentials.helpers;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for sign-related block checks.
 */
@Getter
public class SignHelper {

  /**
   * Checks whether the given block is any type of sign.
   *
   * @param b the block to check
   * @return {@code true} if the block is a sign, {@code false} otherwise
   */
  public static boolean isBlockSign(@NotNull Block b) {
    return b.getBlockData() instanceof WallSign
        || b.getBlockData() instanceof Sign
        || b.getBlockData() instanceof WallHangingSign
        || b.getBlockData() instanceof HangingSign;
  }
}