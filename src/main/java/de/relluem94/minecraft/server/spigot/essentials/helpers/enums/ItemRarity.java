package de.relluem94.minecraft.server.spigot.essentials.helpers.enums;

/**
 *
 * @author rellu
 */
public enum ItemRarity {

    COMMON("Common", "§f§l", 0),
    UNCOMMON("Uncommon", "§a§l", 1),
    RARE("Rare", "§9§l", 2),
    EPIC("Epic", "§5§l", 3),
    LEGENDARY("Legendary", "§6§l", 4);

    private final String displayName;
    private final String prefix;
    private final int level;

    private ItemRarity(String displayName, String prefix, int level) {
        this.displayName = displayName;
        this.prefix = prefix;
        this.level = level;
    }

    /**
     * Returns Displayname of Rarity
     *
     * @return String displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns Prefix of Rarity (Color and Bold)
     *
     * @return String prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns Level of Rarity
     *
     * @return int level
     */
    public int getLevel() {
        return level;
    }
}
