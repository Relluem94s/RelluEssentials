package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.npc.NPCEquipmentSlot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mannequin;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NPCEquipmentInventoryHelper {

    protected NPCEquipmentInventoryHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void applyInventoryEquipmentToEntity(Inventory equipmentInventory, UUID entityUUID) {
        Entity entity = findEntityByUUID(entityUUID);
        if (!(entity instanceof Mannequin mannequin)) {
            return;
        }

        EntityEquipment equipment = mannequin.getEquipment();
        if (equipment == null) {
            return;
        }

        equipment.setHelmet(equipmentInventory.getItem(NPCEquipmentSlot.HELMET.getInventorySlot()));
        equipment.setChestplate(equipmentInventory.getItem(NPCEquipmentSlot.CHESTPLATE.getInventorySlot()));
        equipment.setLeggings(equipmentInventory.getItem(NPCEquipmentSlot.LEGGINGS.getInventorySlot()));
        equipment.setBoots(equipmentInventory.getItem(NPCEquipmentSlot.BOOTS.getInventorySlot()));
        equipment.setItemInMainHand(equipmentInventory.getItem(NPCEquipmentSlot.MAIN_HAND.getInventorySlot()));
        equipment.setItemInOffHand(equipmentInventory.getItem(NPCEquipmentSlot.OFF_HAND.getInventorySlot()));
    }

    public static void loadEntityEquipmentIntoInventory(UUID entityUUID, Inventory equipmentInventory) {
        Entity entity = findEntityByUUID(entityUUID);
        if (!(entity instanceof Mannequin mannequin)) {
            return;
        }

        EntityEquipment equipment = mannequin.getEquipment();
        if (equipment == null) {
            return;
        }

        setSlotIfNotNull(equipmentInventory, NPCEquipmentSlot.HELMET.getInventorySlot(), equipment.getHelmet());
        setSlotIfNotNull(equipmentInventory, NPCEquipmentSlot.CHESTPLATE.getInventorySlot(), equipment.getChestplate());
        setSlotIfNotNull(equipmentInventory, NPCEquipmentSlot.LEGGINGS.getInventorySlot(), equipment.getLeggings());
        setSlotIfNotNull(equipmentInventory, NPCEquipmentSlot.BOOTS.getInventorySlot(), equipment.getBoots());
        setSlotIfNotNull(equipmentInventory, NPCEquipmentSlot.MAIN_HAND.getInventorySlot(), equipment.getItemInMainHand());
        setSlotIfNotNull(equipmentInventory, NPCEquipmentSlot.OFF_HAND.getInventorySlot(), equipment.getItemInOffHand());
    }

    private static void setSlotIfNotNull(Inventory inventory, int slot, ItemStack item) {
        if (item != null && item.getType() != org.bukkit.Material.AIR) {
            inventory.setItem(slot, item);
        }
    }

    private static Entity findEntityByUUID(UUID entityUUID) {
        return org.bukkit.Bukkit.getWorlds().stream()
                .flatMap(world -> world.getEntities().stream())
                .filter(entity -> entity.getUniqueId().equals(entityUUID))
                .findFirst()
                .orElse(null);
    }
}