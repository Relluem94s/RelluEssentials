package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_SIGN_CLICK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_SIGN_NAME;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;

import de.relluem94.minecraft.server.spigot.essentials.exceptions.SignMissingCustomInputException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author rellu
 */
@Getter
public class SignHelper {

    private final String line1;
    private final String line2;
    
    private final ActionType signActionType;

    public SignHelper(@NotNull ActionType signActionType, String customInput) {
        this.signActionType = signActionType;
        this.line1 = signActionType.getDisplayName();
        if (signActionType.isCustomInput()) {
            this.line2 = customInput;
        } else {
            this.line2 = "";
        }
    }

    public SignHelper(@NotNull ActionType signActionType) throws SignMissingCustomInputException {
        this.signActionType = signActionType;
        this.line1 = signActionType.getDisplayName();
        this.line2 = "";
        if (signActionType.isCustomInput()) {
            throw new SignMissingCustomInputException("");
        }
    }

    @SuppressWarnings("SameReturnValue")
    public String getLine0() {
        return PLUGIN_SIGN_NAME;
    }

    @SuppressWarnings("SameReturnValue")
    public String getLine3() {
        return PLUGIN_SIGN_CLICK;
    }

    public static boolean isSign(@NotNull SignHelper sh, String line0, String line1, String line3) {
        return sh.getLine0().equals(line0) && sh.getLine1().equals(line1) && sh.getLine3().equals(line3);
    }

    public static boolean isSign(@NotNull SignHelper sh, String line1) {
        return sh.getSignActionType().getShorthand().equals(line1) || sh.getSignActionType().getName().equalsIgnoreCase(line1);
    }

    public static boolean isBlockASign(@NotNull Block b){
        return (
            b.getBlockData() instanceof WallSign ||
            b.getBlockData() instanceof Sign ||
            b.getBlockData() instanceof WallHangingSign ||
            b.getBlockData() instanceof HangingSign 
        );
    }

    @Getter
    public enum ActionType {
        COMMAND(1, "Command", true),
        TELEPORT(2, "Teleport", true),
        SPAWN(3, "Spawn", false),
        UP(4, "Up", false),
        DOWN(5, "Down", false),
        HOME(6, "Home", true);

        private final int id;
        private final String displayName;
        private final boolean customInput;

        ActionType(int id, String displayName, boolean test) {
            this.id = id;
            this.customInput = test;
            this.displayName = displayName;
        }


        @Contract(pure = true)
        public @NotNull String getShorthand() {
            return "[" + id + "]";
        }

        @Contract(pure = true)
        public @NotNull String getName() {
            return "[" + this.name() + "]";
        }
    }
}
