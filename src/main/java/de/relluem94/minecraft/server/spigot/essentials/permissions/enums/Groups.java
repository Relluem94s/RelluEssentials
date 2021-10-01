package de.relluem94.minecraft.server.spigot.essentials.permissions.enums;

/**
 *
 * @author rellu
 */
public enum Groups {
    USER("User", 1, "§8"),
    VIP("VIP", 2, "§a"),
    MOD("Mod", 4, "§6"),    
    ADMIN("Admin", 1073741824, "§5");
    
    private final String name;
    private final String prefix;
    private final int id;
    
    private Groups(String name, int id, String prefix){
        this.id = id;
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getId() {
        return id;
    }
}
