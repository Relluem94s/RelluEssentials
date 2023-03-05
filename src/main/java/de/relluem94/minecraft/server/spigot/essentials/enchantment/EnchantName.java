package de.relluem94.minecraft.server.spigot.essentials.enchantment;

public class EnchantName {

    private String name;
    private String displayName;

    public EnchantName(String name, String displayName){
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}