package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_CLICK_SIGN;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_SIGN;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;

import de.relluem94.minecraft.server.spigot.essentials.exceptions.SignMissingCustomInputException;

/**
 *
 * @author rellu
 */
public class SignHelper {

    private final String line0 = PLUGIN_NAME_SIGN;
    private final String line1;
    private final String line2;
    private final String line3 = PLUGIN_CLICK_SIGN;
    private final ActionType signActionType;

    public SignHelper(ActionType signActionType, String customInput) {
        this.signActionType = signActionType;
        this.line1 = signActionType.getDisplayName();
        if (signActionType.hasCustomInput()) {
            this.line2 = customInput;
        } else {
            this.line2 = "";
        }
    }

    public SignHelper(ActionType signActionType) throws SignMissingCustomInputException {
        this.signActionType = signActionType;
        this.line1 = signActionType.getDisplayName();
        this.line2 = "";
        if (signActionType.hasCustomInput()) {
            throw new SignMissingCustomInputException("");
        }
    }

    public ActionType getSignActionType() {
        return signActionType;
    }

    public String getLine0() {
        return line0;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public static boolean isSign(SignHelper sh, String line0, String line1, String line3) {
        return sh.getLine0().equals(line0) && sh.getLine1().equals(line1) && sh.getLine3().equals(line3);
    }

    public static boolean isSign(SignHelper sh, String line1) {
        return sh.getSignActionType().getShorthand().equals(line1) || sh.getSignActionType().getName().equalsIgnoreCase(line1);
    }

    public static boolean isBlockASign(Block b){
        return (
            b.getBlockData() instanceof WallSign ||
            b.getBlockData() instanceof Sign ||
            b.getBlockData() instanceof WallHangingSign ||
            b.getBlockData() instanceof HangingSign 
        );
    }

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

        private ActionType(int id, String displayName, boolean test) {
            this.id = id;
            this.customInput = test;
            this.displayName = displayName;
        }

        public int getId() {
            return id;
        }

        public boolean hasCustomInput() {
            return customInput;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getShorthand() {
            return "[" + id + "]";
        }

        public String getName() {
            return "[" + this.name() + "]";
        }
    }
}
