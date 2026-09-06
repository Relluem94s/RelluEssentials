package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.enums.npc.NpcEquipmentSlot;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mannequin;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class for synchronizing equipment between a custom inventory and a
 * {@link org.bukkit.entity.Mannequin} entity.
 */
public class NpcEquipmentInventoryHelper {

  /**
   * Private constructor to prevent instantiation of this utility class.
   *
   * @throws IllegalStateException always, as this class is not meant to be instantiated
   */
  protected NpcEquipmentInventoryHelper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Applies all equipment items from the given inventory to the {@link org.bukkit.entity.Mannequin}
   * identified by the given UUID.
   *
   * <p>Each inventory slot is mapped to the corresponding equipment slot via
   * {@link de.relluem94.minecraft.server.spigot.essentials.enums.npc.NpcEquipmentSlot}. If no
   * entity with the given UUID is found, or the entity is not a
   * {@link org.bukkit.entity.Mannequin}, this method does nothing.</p>
   *
   * @param equipmentInventory the inventory containing the equipment items to apply
   * @param entityUuid         the UUID of the target {@link org.bukkit.entity.Mannequin} entity
   */
  public static void applyInventoryEquipmentToEntity(Inventory equipmentInventory,
      UUID entityUuid) {
    Entity entity = findEntityByUuid(entityUuid);
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

  /**
   * Loads all equipment items from the {@link Mannequin} identified by the given UUID into the
   * given inventory.
   *
   * <p>Each equipment slot is mapped to the corresponding inventory slot via
   * {@link NpcEquipmentSlot}. Items that are {@code null} or of type
   * {@link org.bukkit.Material#AIR} are not written into the inventory. If no entity with the given
   * UUID is found, or the entity is not a {@link Mannequin}, this method does nothing.</p>
   *
   * @param entityUuid         the UUID of the source {@link Mannequin} entity
   * @param equipmentInventory the inventory to load the equipment items into
   */
  public static void loadEntityEquipmentIntoInventory(UUID entityUuid,
      Inventory equipmentInventory) {
    Entity entity = findEntityByUuid(entityUuid);
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

  private static Entity findEntityByUuid(UUID entityUuid) {
    return org.bukkit.Bukkit.getWorlds().stream().flatMap(world -> world.getEntities().stream())
        .filter(entity -> entity.getUniqueId().equals(entityUuid)).findFirst().orElse(null);
  }
}