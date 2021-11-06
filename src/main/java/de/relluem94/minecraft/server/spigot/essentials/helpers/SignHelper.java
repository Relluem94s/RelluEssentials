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
    private final String line1 = PLUGIN_NAME_SIGN;
    private final String line2;
    private final String line3;
    private final String line4 = PLUGIN_CLICK_SIGN;
    private final SignActionType signActionType;

    public SignHelper(SignActionType signActionType, String customInput) {
        this.signActionType = signActionType;
        this.line2 = signActionType.getDisplayName();
        if(signActionType.hasCustomInput()){
            this.line3 = customInput;
        }
        else{
            this.line3 = "";
        }        
    }
    
    public SignHelper(SignActionType signActionType) throws SignMissingCustomInputException {
        this.signActionType = signActionType;
        this.line2 = signActionType.getDisplayName();
        this.line3 = "";
        if(signActionType.hasCustomInput()){
            throw new SignMissingCustomInputException("");
        }
    }

    public SignActionType getSignActionType() {
        return signActionType;
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

    public String getLine4() {
        return line4;
    }
}