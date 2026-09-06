package de.relluem94.minecraft.server.spigot.essentials.models.items;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomItemTest {

  @Mock
  Server server;

  @SuppressWarnings("unchecked")
  private static void setBukkitServer(Server server) throws Exception {
    java.lang.reflect.Field serverField = org.bukkit.Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, server);
  }

  private CustomItem buildMinimalItem() {
    return new CustomItem(Material.STONE, 1, "Name", List.of(), Type.NONE, Rarity.NONE, null,
        List.of(), Map.of(), List.of(), new RelluEssentialsNamespacedKey("test", "stone"), server);
  }

  private ItemMeta mockItemMetaOnItemStack(ItemStack mockedItemStack) {
    ItemMeta meta = mock(ItemMeta.class);
    PersistentDataContainer container = mock(PersistentDataContainer.class);
    lenient().when(meta.getPersistentDataContainer()).thenReturn(container);
    when(mockedItemStack.getItemMeta()).thenReturn(meta);
    return meta;
  }

  @Test
  void toItemStackReturnsNotNull() {
    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> mockItemMetaOnItemStack(mock))) {
      assertNotNull(buildMinimalItem().toItemStack());
    }
  }

  @Test
  void toItemStackSetsDisplayName() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      CustomItem customItem = new CustomItem(Material.STONE, 1, "My Sword", List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of(), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      verify(capturedMeta[0]).setDisplayName("My Sword");
    }
  }

  @Test
  void toItemStackSkipsDisplayNameWhenNull() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of(), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      verify(capturedMeta[0], never()).setDisplayName(Mockito.any());
    }
  }

  @Test
  void toItemStackSetsLore() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      List<String> lore = List.of("Line 1", "Line 2");

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, lore, Type.NONE, Rarity.NONE,
          null, List.of(), Map.of(), List.of(), new RelluEssentialsNamespacedKey("test", "stone"),
          server);

      customItem.toItemStack();

      verify(capturedMeta[0]).setLore(lore);
    }
  }

  @Test
  void toItemStackAddsRarityToNullLore() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> {
          capturedMeta[0] = mockItemMetaOnItemStack(mock);
          when(capturedMeta[0].getLore()).thenReturn(null);
        })) {

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.RARE, null, List.of(), Map.of(), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      verify(capturedMeta[0]).setLore(
          List.of(Rarity.RARE.getPrefix() + Rarity.RARE.getDisplayName()));
    }
  }

  @Test
  void toItemStackAddsRarityToExistingLore() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> {
          capturedMeta[0] = mockItemMetaOnItemStack(mock);
          when(capturedMeta[0].getLore()).thenReturn(new ArrayList<>(List.of("Existing Line")));
        })) {

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.EPIC, null, List.of(), Map.of(), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      verify(capturedMeta[0]).setLore(
          List.of("Existing Line", Rarity.EPIC.getPrefix() + Rarity.EPIC.getDisplayName()));
    }
  }

  @Test
  void toItemStackSetsCostInPersistentData() {
    ItemMeta[] capturedMeta = new ItemMeta[1];
    NamespacedKey mockedCostKey = mock(NamespacedKey.class);

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(
            mock)); MockedStatic<NamespacedKeyConstants> mockedKeyConstants = Mockito.mockStatic(
        NamespacedKeyConstants.class)) {

      mockedKeyConstants.when(NamespacedKeyConstants::itemCost).thenReturn(mockedCostKey);

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.NONE, 250, List.of(), Map.of(), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      verify(capturedMeta[0].getPersistentDataContainer()).set(mockedCostKey,
          PersistentDataType.INTEGER, 250);
    }
  }

  @Test
  void toItemStackInvokesMetaModifiers() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      @SuppressWarnings("unchecked") Consumer<ItemMeta> modifier = mock(Consumer.class);

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of(), List.of(modifier),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      verify(modifier).accept(capturedMeta[0]);
    }
  }

  @Test
  void toItemStackSkipsDisplayNameWhenEmpty() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      CustomItem customItem = new CustomItem(Material.STONE, 1, "", List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of(), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      verify(capturedMeta[0], never()).setDisplayName(Mockito.any());
    }
  }

  @Test
  void toItemStackSkipsLoreWhenEmpty() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      buildMinimalItem().toItemStack();

      verify(capturedMeta[0], never()).setLore(Mockito.any());
    }
  }

  @Test
  void toItemStackSkipsCostWhenNull() {
    ItemMeta[] capturedMeta = new ItemMeta[1];
    NamespacedKey mockedCostKey = mock(NamespacedKey.class);

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(
            mock)); MockedStatic<NamespacedKeyConstants> mockedKeyConstants = Mockito.mockStatic(
        NamespacedKeyConstants.class)) {

      mockedKeyConstants.when(NamespacedKeyConstants::itemCost).thenReturn(mockedCostKey);

      buildMinimalItem().toItemStack();

      verify(capturedMeta[0].getPersistentDataContainer(), never()).set(Mockito.eq(mockedCostKey),
          Mockito.eq(PersistentDataType.INTEGER), Mockito.any());
    }
  }

  @Test
  void toItemStackSetsStringPersistentData() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      NamespacedKey dataKey = NamespacedKey.fromString("test:mykey");

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of("test:mykey", "hello"), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      assertNotNull(dataKey);
      verify(capturedMeta[0].getPersistentDataContainer()).set(dataKey, PersistentDataType.STRING,
          "hello");
    }
  }

  @Test
  void toItemStackSetsIntegerPersistentData() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      NamespacedKey dataKey = NamespacedKey.fromString("test:mykey");

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of("test:mykey", 42), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      assertNotNull(dataKey);
      verify(capturedMeta[0].getPersistentDataContainer()).set(dataKey, PersistentDataType.INTEGER,
          42);
    }
  }

  @Test
  void toItemStackSetsBooleanPersistentData() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      NamespacedKey dataKey = NamespacedKey.fromString("test:mykey");

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of("test:mykey", true), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      assertNotNull(dataKey);
      verify(capturedMeta[0].getPersistentDataContainer()).set(dataKey, PersistentDataType.BYTE,
          (byte) 1);
    }
  }

  @Test
  void toItemStackSetsDoublePersistentData() {
    ItemMeta[] capturedMeta = new ItemMeta[1];

    try (MockedConstruction<ItemStack> ignored = Mockito.mockConstruction(ItemStack.class,
        (mock, _) -> capturedMeta[0] = mockItemMetaOnItemStack(mock))) {

      NamespacedKey dataKey = NamespacedKey.fromString("test:mykey");

      CustomItem customItem = new CustomItem(Material.STONE, 1, null, List.of(), Type.NONE,
          Rarity.NONE, null, List.of(), Map.of("test:mykey", 3.14), List.of(),
          new RelluEssentialsNamespacedKey("test", "stone"), server);

      customItem.toItemStack();

      assertNotNull(dataKey);
      verify(capturedMeta[0].getPersistentDataContainer()).set(dataKey, PersistentDataType.DOUBLE,
          3.14);
    }
  }
}