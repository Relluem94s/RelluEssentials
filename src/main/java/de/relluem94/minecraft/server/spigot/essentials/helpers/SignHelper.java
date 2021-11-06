package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_CLICK_SIGN;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_SIGN;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.SignMissingCustomInputException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.SignActionType;

/**
 *
 * @author rellu
 */
public class SignHelper {
    private final String line0 = PLUGIN_NAME_SIGN;
    private final String line1;
    private final String line2;
    private final String line3 = PLUGIN_CLICK_SIGN;
    private final SignActionType signActionType;

    public SignHelper(SignActionType signActionType, String customInput) {
        this.signActionType = signActionType;
        this.line1 = signActionType.getDisplayName();
        if(signActionType.hasCustomInput()){
            this.line2 = customInput;
        }
        else{
            this.line2 = "";
        }        
    }
    
    public SignHelper(SignActionType signActionType) throws SignMissingCustomInputException {
        this.signActionType = signActionType;
        this.line1 = signActionType.getDisplayName();
        this.line2 = "";
        if(signActionType.hasCustomInput()){
            throw new SignMissingCustomInputException("");
        }
    }

    public SignActionType getSignActionType() {
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
    
    public static boolean isSign(SignHelper sh, String line0, String line1, String line3){
        return sh.getLine0().equals(line0) && sh.getLine1().equals(line1) && sh.getLine3().equals(line3);
    }
    
    public static boolean isSign(SignHelper sh, String line1){
        return sh.getSignActionType().getShorthand().equals(line1) || sh.getSignActionType().getName().equalsIgnoreCase(line1);
    }
}