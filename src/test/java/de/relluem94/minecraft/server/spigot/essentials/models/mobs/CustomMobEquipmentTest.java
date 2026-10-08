package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomMobEquipmentTest {

  @Mock
  private ItemStack mockedMainHand;

  @Mock
  private ItemStack mockedOffHand;

  @Mock
  private ItemStack mockedHelmet;

  @Mock
  private ItemStack mockedChestplate;

  @Mock
  private ItemStack mockedLeggings;

  @Mock
  private ItemStack mockedBoots;

  @Test
  void mainHandReturnsCorrectItemStack() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings,
            mockedBoots);

    assertEquals(mockedMainHand, equipment.mainHand());
  }

  @Test
  void offHandReturnsCorrectItemStack() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings,
            mockedBoots);

    assertEquals(mockedOffHand, equipment.offHand());
  }

  @Test
  void helmetReturnsCorrectItemStack() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings,
            mockedBoots);

    assertEquals(mockedHelmet, equipment.helmet());
  }

  @Test
  void chestplateReturnsCorrectItemStack() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings,
            mockedBoots);

    assertEquals(mockedChestplate, equipment.chestplate());
  }

  @Test
  void leggingsReturnsCorrectItemStack() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings,
            mockedBoots);

    assertEquals(mockedLeggings, equipment.leggings());
  }

  @Test
  void bootsReturnsCorrectItemStack() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings,
            mockedBoots);

    assertEquals(mockedBoots, equipment.boots());
  }

  @Test
  void mainHandReturnsNullWhenNotProvided() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(null, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings, mockedBoots);

    assertNull(equipment.mainHand());
  }

  @Test
  void offHandReturnsNullWhenNotProvided() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, null, mockedHelmet, mockedChestplate, mockedLeggings, mockedBoots);

    assertNull(equipment.offHand());
  }

  @Test
  void helmetReturnsNullWhenNotProvided() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, null, mockedChestplate, mockedLeggings, mockedBoots);

    assertNull(equipment.helmet());
  }

  @Test
  void chestplateReturnsNullWhenNotProvided() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, null, mockedLeggings, mockedBoots);

    assertNull(equipment.chestplate());
  }

  @Test
  void leggingsReturnsNullWhenNotProvided() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, null, mockedBoots);

    assertNull(equipment.leggings());
  }

  @Test
  void bootsReturnsNullWhenNotProvided() {
    CustomMobEquipment equipment =
        new CustomMobEquipment(mockedMainHand, mockedOffHand, mockedHelmet, mockedChestplate, mockedLeggings, null);

    assertNull(equipment.boots());
  }

  @Test
  void allFieldsReturnNullWhenAllNotProvided() {
    CustomMobEquipment equipment = new CustomMobEquipment(null, null, null, null, null, null);

    assertNull(equipment.mainHand());
    assertNull(equipment.offHand());
    assertNull(equipment.helmet());
    assertNull(equipment.chestplate());
    assertNull(equipment.leggings());
    assertNull(equipment.boots());
  }
}