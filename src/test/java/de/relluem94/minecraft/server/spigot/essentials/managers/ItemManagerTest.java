package de.relluem94.minecraft.server.spigot.essentials.managers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.model.RegisteredInventory;
import de.relluem94.minecraft.server.spigot.essentials.services.EnchantmentService;
import de.relluem94.minecraft.server.spigot.essentials.services.InventoryService;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemManagerTest {

  @Mock
  private RelluEssentials plugin;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private ItemService itemService;

  @Mock
  private InventoryService inventoryService;

  @Mock
  private EnchantmentService enchantmentService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private TranslationService translationService;

  @Mock
  private ServerService serverService;

  @Mock
  private ConsoleCommandSender consoleCommandSender;

  @Mock
  private RegisteredInventory registeredInventory;

  private ItemManager itemManager;

  @BeforeEach
  void setUp() {
    itemManager = new ItemManager();

    lenient().when(plugin.getServiceContext()).thenReturn(serviceContext);
    lenient().when(plugin.getName()).thenReturn("RelluEssentials");
    lenient().when(serviceContext.getItemService()).thenReturn(itemService);
    lenient().when(serviceContext.getInventoryService()).thenReturn(inventoryService);
    lenient().when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);
    lenient().when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
    lenient().when(serverService.getConsoleSender()).thenReturn(consoleCommandSender);
    lenient().when(pluginMetadataService.getName()).thenReturn("RelluEssentials");

    lenient().when(itemService.getAll()).thenReturn(Map.of());
    lenient().when(inventoryService.getAllByNamespace(anyString())).thenReturn(List.of());

    CustomItem mockItem = mock(CustomItem.class);
    lenient().when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(mockItem));

    lenient().when(inventoryService.create(any(Plugin.class), anyString(), anyString(), anyInt(), any(CustomItem.Type.class)))
        .thenReturn(registeredInventory);
    lenient().when(registeredInventory.withFixedItem(any(CustomItem.class))).thenReturn(registeredInventory);

    lenient().when(enchantmentService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());
    lenient().when(translationService.getWithPrefix(any(MessageKey.class), any())).thenReturn("");
  }

  @Test
  void enableRegistersAllCustomItems() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(itemService, atLeastOnce()).register(any(CustomItem.class));
    }
  }

  @Test
  void enableRegistersPositionAxeItem() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(itemService, atLeastOnce()).register(argThatHasNamespace());
    }
  }

  @Test
  void enableRegistersAllBankItems() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(itemService, atLeastOnce()).register(any(CustomItem.class));
    }
  }

  @Test
  void enableCreatesAdminToolsInventory() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(inventoryService).create(eq(plugin), anyString(), anyString(), eq(9), eq(CustomItem.Type.NONE));
    }
  }

  @Test
  void enableLogsItemRegistrationCount() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(translationService).getWithPrefix(eq(MessageKey.PLUGIN_MANAGER_ITEMS_REGISTERED), any());
    }
  }

  @Test
  void enableLogsInventoryRegistrationCount() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(translationService).getWithPrefix(eq(MessageKey.PLUGIN_MANAGER_INVENTORIES_REGISTERED), any());
    }
  }

  @Test
  void enableThrowsWhenPositionAxeItemNotFound() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());

      assertThrows(Exception.class, () -> itemManager.enable(plugin));
    }
  }

  @Test
  void enableRegistersRelluPickaxeAndSwordWithEnchantmentMetaModifiers() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(itemService, atLeastOnce()).register(argThatHasNamespace());
    }
  }

  @Test
  void enableCallsGetAllOnItemServiceForLogging() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(itemService).getAll();
    }
  }

  @Test
  void enableCallsGetAllByNamespaceOnInventoryServiceForLogging() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(inventoryService).getAllByNamespace("RelluEssentials");
    }
  }

  @Test
  void enableSendsRegistrationMessageToConsoleSender() {
    try (MockedStatic<PlayerHeadHelper> playerHeadHelperMock = mockStatic(PlayerHeadHelper.class)) {
      playerHeadHelperMock.when(() -> PlayerHeadHelper.customHeadModifier(any())).thenReturn(mock(Consumer.class));

      itemManager.enable(plugin);

      verify(consoleCommandSender, atLeastOnce()).sendMessage(anyString());
    }
  }

  @Test
  void enableThrowsWhenPluginIsNotRelluEssentialsInstance() {
    Plugin nonRelluPlugin = mock(Plugin.class);

    assertThrows(ClassCastException.class, () -> itemManager.enable(nonRelluPlugin));
  }

  private CustomItem argThatHasNamespace() {
    return argThat(item ->
        item != null &&
            item.relluEssentialsNamespacedKey() != null &&
            "RelluEssentials".equals(item.relluEssentialsNamespacedKey().getNamespace())
    );
  }
}