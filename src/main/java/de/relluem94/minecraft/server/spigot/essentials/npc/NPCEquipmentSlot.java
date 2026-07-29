package de.relluem94.minecraft.server.spigot.essentials.npc;

import lombok.Getter;

@Getter
public enum NPCEquipmentSlot {
    HELMET(0),
    CHESTPLATE(9),
    LEGGINGS(18),
    BOOTS(27),
    MAIN_HAND(36),
    OFF_HAND(45);

    private final int inventorySlot;

    NPCEquipmentSlot(int inventorySlot) {
        this.inventorySlot = inventorySlot;
    }

}