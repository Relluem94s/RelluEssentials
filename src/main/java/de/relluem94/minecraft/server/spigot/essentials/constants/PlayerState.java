package de.relluem94.minecraft.server.spigot.essentials.constants;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.IPlayerState;

public enum PlayerState implements IPlayerState {
    PROTECTION_INFO,
    PROTECTION_ADD,
    PROTECTION_REMOVE,
    PROTECTION_FLAG_ADD,
    PROTECTION_FLAG_REMOVE,
    PROTECTION_RIGHT_ADD,
    PROTECTION_RIGHT_REMOVE,
    LIGHT_TOGGLE,
    SIGN_COPY,
    SIGN_PASTE,
    SIGN_EDIT,
    FAKE_AFK_ON,
    FAKE_AFK_ACTIVE,
    DAMAGE_INFO,
    DEFAULT
}