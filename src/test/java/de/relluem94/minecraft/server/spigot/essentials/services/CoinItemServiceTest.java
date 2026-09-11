package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoinItemServiceTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private ItemService itemService;
  @Mock
  private PluginMetadataService pluginMetadataService;
  @Mock
  private CustomItem customItem;
  @Mock
  private ItemStack itemStack;
  @Mock
  private ItemMeta itemMeta;
  @Mock
  private PersistentDataContainer persistentDataContainer;
  @Mock
  private NamespacedKey coinNamespacedKey;

  private CoinItemService coinItemService;

  @BeforeEach
  void setUp() {
    coinItemService = new CoinItemService(serviceContext);

    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("RelluEssentials");
    when(serviceContext.getItemService()).thenReturn(itemService);
  }

  @Test
  void getCoinReturnsCoinItemStackWithCorrectLoreAndPersistentData() {
    try (MockedStatic<NamespacedKeyConstants> mockedStatic = Mockito.mockStatic(NamespacedKeyConstants.class)) {
      mockedStatic.when(NamespacedKeyConstants::itemCoins).thenReturn(coinNamespacedKey);

      when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(customItem));
      when(customItem.toItemStack()).thenReturn(itemStack);
      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

      ItemStack result = coinItemService.getCoin(50);

      String expectedLore = String.format(ItemConstants.PLUGIN_ITEM_COINS_LORE, StringHelper.formatInt(50));

      assertAll(
          () -> assertNotNull(result),
          () -> assertEquals(itemStack, result),
          () -> verify(itemMeta).setLore(List.of(expectedLore)),
          () -> verify(persistentDataContainer).set(eq(coinNamespacedKey), eq(PersistentDataType.INTEGER), eq(50)),
          () -> verify(itemStack).setItemMeta(itemMeta)
      );
    }
  }

  @Test
  void getCoinSkipsMetaApplicationWhenItemMetaIsNull() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(itemStack);
    when(itemStack.getItemMeta()).thenReturn(null);

    ItemStack result = coinItemService.getCoin(10);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(itemStack, result)
    );
  }

  @Test
  void getCoinThrowsNoSuchElementExceptionWhenCoinItemNotRegistered() {
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> coinItemService.getCoin(10));
  }

  @Test
  void getCoinAppliesAbsoluteValueForNegativeCoins() {
    try (MockedStatic<NamespacedKeyConstants> mockedStatic = Mockito.mockStatic(NamespacedKeyConstants.class)) {
      mockedStatic.when(NamespacedKeyConstants::itemCoins).thenReturn(coinNamespacedKey);

      when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(customItem));
      when(customItem.toItemStack()).thenReturn(itemStack);
      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

      ItemStack result = coinItemService.getCoin(-25);

      assertAll(
          () -> assertNotNull(result),
          () -> verify(persistentDataContainer).set(eq(coinNamespacedKey), eq(PersistentDataType.INTEGER), eq(-25))
      );
    }
  }
}