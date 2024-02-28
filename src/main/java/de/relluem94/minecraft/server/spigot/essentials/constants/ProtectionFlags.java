package de.relluem94.minecraft.server.spigot.essentials.constants;

import lombok.Getter;

@Getter
public enum ProtectionFlags {
    ALLOW_HOPPER("allowHopper", false),
    ALLOW_REDSTONE("allowRedstone", false),
    ALLOW_PUBLIC("allowPublic", false),
    AUTO_CLOSE("autoClose", false);

    
    private final String name;
    private final boolean isDefault;

    ProtectionFlags(String name, boolean isDefault){
        this.name = name;
        this.isDefault = isDefault;
    }
}