package de.relluem94.minecraft.server.spigot.essentials.helpers.enums;

/**
 *
 * @author rellu
 */
public enum SignActionType {
    COMMAND(1, "Command", true),
    TELEPORT(2, "Teleport", true),
    SPAWN(3, "Spawn", false),
    UP(4, "Up", false),
    DOWN(5, "Down", false),
    HOME(6, "Home", true);
    
    
    private final int id;
    private final String displayName;
    private final boolean customInput;

    private SignActionType(int id, String displayName, boolean test) {
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
    
    public String getShorthand(){
        return "[" + id + "]";
    }
}