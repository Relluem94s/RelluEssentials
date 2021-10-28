package de.relluem94.minecraft.server.spigot.essentials.helpers.enums;

/**
 *
 * @author rellu
 */
public enum ItemRarity {
    
    COMMON      ("Common",      "§l§f",   0),
    UNCOMMON    ("Uncommon",    "§l§a",   1),
    RARE        ("Rare",        "§l§9",   2),
    EPIC        ("Epic",        "§l§5",   3),
    LEGENDARY   ("Legendary",   "§l§6",   4);

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
