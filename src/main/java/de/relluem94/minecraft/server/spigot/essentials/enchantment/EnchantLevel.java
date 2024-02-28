package de.relluem94.minecraft.server.spigot.essentials.enchantment;

public class EnchantLevel {
    private final int startLevel;
    private final int maxLevel;

    public EnchantLevel(int startLevel, int maxLevel){
        this.startLevel = startLevel;
        this.maxLevel = maxLevel;
    }

    public int getStartLevel(){
        return startLevel;
    }

    public int getMaxLevel(){
        return maxLevel;
    }
}