package de.relluem94.minecraft.server.spigot.essentials.constants;

import lombok.Getter;

@Getter
public enum ProtectionFlags {
    ALLOW_HOPPER(false),
    ALLOW_REDSTONE(false),
    ALLOW_PUBLIC(false),
    AUTO_CLOSE(false);

    
    private final boolean isDefault;

    ProtectionFlags(boolean isDefault){
        this.isDefault = isDefault;
    }
}