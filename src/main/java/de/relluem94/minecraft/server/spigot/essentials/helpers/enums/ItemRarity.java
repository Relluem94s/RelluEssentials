package de.relluem94.minecraft.server.spigot.essentials.helpers.enums;

/**
 *
 * @author rellu
 */
public enum ItemRarity {
    
    COMMON      ("Common",      "§f",   0),
    UNCOMMON    ("Uncommon",    "§a",   1),
    RARE        ("Rare",        "§9",   2),
    EPIC        ("Epic",        "§5",   3),
    LEGENDARY   ("Legendary",   "§6",   4);

    private final String displayName;
    private final String prefix;
    private final int level;

    private ItemRarity(String displayName, String prefix, int level) {
        this.displayName = displayName;
        this.prefix = prefix;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getLevel(){
        return level;
    }
}
