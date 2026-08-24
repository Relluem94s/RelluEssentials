package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.enums.npc.NpcEquipmentSlot;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mannequin;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NpcEquipmentInventoryHelper {

  protected NpcEquipmentInventoryHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static void applyInventoryEquipmentToEntity(Inventory equipmentInventory,
      UUID entityUUID) {
    Entity entity = findEntityByUUID(entityUUID);
    if (!(entity instanceof Mannequin mannequin)) {
      return;
    }

    EntityEquipment equipment = mannequin.getEquipment();
    if (equipment == null) {
      return;
    }

    equipment.setHelmet(equipmentInventory.getItem(NpcEquipmentSlot.HELMET.getInventorySlot()));
    equipment.setChestplate(
        equipmentInventory.getItem(NpcEquipmentSlot.CHESTPLATE.getInventorySlot()));
    equipment.setLeggings(equipmentInventory.getItem(NpcEquipmentSlot.LEGGINGS.getInventorySlot()));
    equipment.setBoots(equipmentInventory.getItem(NpcEquipmentSlot.BOOTS.getInventorySlot()));
    equipment.setItemInMainHand(
        equipmentInventory.getItem(NpcEquipmentSlot.MAIN_HAND.getInventorySlot()));
    equipment.setItemInOffHand(
        equipmentInventory.getItem(NpcEquipmentSlot.OFF_HAND.getInventorySlot()));
  }

  public static void loadEntityEquipmentIntoInventory(UUID entityUUID,
      Inventory equipmentInventory) {
    Entity entity = findEntityByUUID(entityUUID);
    if (!(entity instanceof Mannequin mannequin)) {
      return;
    }

    EntityEquipment equipment = mannequin.getEquipment();
    if (equipment == null) {
      return;
    }

    setSlotIfNotNull(equipmentInventory, NpcEquipmentSlot.HELMET.getInventorySlot(),
        equipment.getHelmet());
    setSlotIfNotNull(equipmentInventory, NpcEquipmentSlot.CHESTPLATE.getInventorySlot(),
        equipment.getChestplate());
    setSlotIfNotNull(equipmentInventory, NpcEquipmentSlot.LEGGINGS.getInventorySlot(),
        equipment.getLeggings());
    setSlotIfNotNull(equipmentInventory, NpcEquipmentSlot.BOOTS.getInventorySlot(),
        equipment.getBoots());
    setSlotIfNotNull(equipmentInventory, NpcEquipmentSlot.MAIN_HAND.getInventorySlot(),
        equipment.getItemInMainHand());
    setSlotIfNotNull(equipmentInventory, NpcEquipmentSlot.OFF_HAND.getInventorySlot(),
        equipment.getItemInOffHand());
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