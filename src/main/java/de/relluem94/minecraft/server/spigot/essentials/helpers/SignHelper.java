package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_SIGN_CLICK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_SIGN_NAME;

import de.relluem94.minecraft.server.spigot.essentials.model.SignAction;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.jetbrains.annotations.NotNull;

@Getter
public class SignHelper {

  private final SignAction signAction;
  private final String line1;
  private final String line2;

  public SignHelper(@NotNull SignAction signAction, String customInput) {
    this.signAction = signAction;
    this.line1 = signAction.getDisplayName();
    this.line2 = signAction.requiresCustomInput() ? customInput : "";
  }

  public SignHelper(@NotNull SignAction signAction) {
    this.signAction = signAction;
    this.line1 = signAction.getDisplayName();
    this.line2 = "";
  }

  public static boolean isSign(@NotNull SignHelper sh, String line0, String line1, String line3) {
    return sh.getLine0().equals(line0) && sh.getLine1().equals(line1) && sh.getLine3()
        .equals(line3);
  }

  public static boolean isSign(@NotNull SignHelper sh, String line1) {
    return sh.getSignAction().getShorthandBracket().equalsIgnoreCase(line1)
        || sh.getSignAction().getNameBracket().equalsIgnoreCase(line1);
  }

  public static boolean isBlockASign(@NotNull Block b) {
    return b.getBlockData() instanceof WallSign ||
        b.getBlockData() instanceof Sign ||
        b.getBlockData() instanceof WallHangingSign ||
        b.getBlockData() instanceof HangingSign;
  }

  @SuppressWarnings("SameReturnValue")
  public String getLine0() {
    return PLUGIN_SIGN_NAME;
  }

  @SuppressWarnings("SameReturnValue")
  public String getLine3() {
    return PLUGIN_SIGN_CLICK;
  }
}