package de.relluem94.minecraft.server.spigot.essentials.enums.npc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class NpcEquipmentSlotTest {

  @Test
  void helmetHasInventorySlotZero() {
    assertEquals(0, NpcEquipmentSlot.HELMET.getInventorySlot());
  }

  @Test
  void chestplateHasInventorySlotNine() {
    assertEquals(9, NpcEquipmentSlot.CHESTPLATE.getInventorySlot());
  }

  @Test
  void leggingsHasInventorySlotEighteen() {
    assertEquals(18, NpcEquipmentSlot.LEGGINGS.getInventorySlot());
  }

  @Test
  void bootsHasInventorySlotTwentySeven() {
    assertEquals(27, NpcEquipmentSlot.BOOTS.getInventorySlot());
  }

  @Test
  void mainHandHasInventorySlotThirtySix() {
    assertEquals(36, NpcEquipmentSlot.MAIN_HAND.getInventorySlot());
  }

  @Test
  void offHandHasInventorySlotFortyFive() {
    assertEquals(45, NpcEquipmentSlot.OFF_HAND.getInventorySlot());
  }

  @Test
  void enumContainsSixValues() {
    assertEquals(6, NpcEquipmentSlot.values().length);
  }

  @ParameterizedTest
  @EnumSource(NpcEquipmentSlot.class)
  void everySlotHasNonNegativeInventorySlot(NpcEquipmentSlot slot) {
    assertTrue(slot.getInventorySlot() >= 0);
  }

  @ParameterizedTest
  @EnumSource(NpcEquipmentSlot.class)
  void everySlotInventorySlotIsMultipleOfNine(NpcEquipmentSlot slot) {
    assertEquals(0, slot.getInventorySlot() % 9);
  }

  @Test
  void valueOfReturnsCorrectEnumConstant() {
    assertEquals(NpcEquipmentSlot.HELMET, NpcEquipmentSlot.valueOf("HELMET"));
    assertEquals(NpcEquipmentSlot.MAIN_HAND, NpcEquipmentSlot.valueOf("MAIN_HAND"));
    assertEquals(NpcEquipmentSlot.OFF_HAND, NpcEquipmentSlot.valueOf("OFF_HAND"));
  }
}