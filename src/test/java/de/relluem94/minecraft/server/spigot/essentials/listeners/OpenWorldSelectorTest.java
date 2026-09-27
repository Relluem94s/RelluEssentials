package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldMenuService;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenWorldSelectorTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private ItemService itemService;

  @Mock
  private WorldMenuService worldMenuService;

  @Mock
  private CustomItem customItem;

  @Mock
  private ItemStack worldSelectorItemStack;

  @Mock
  private ItemStack heldItemStack;

  @Mock
  private PlayerInteractEvent event;

  @Mock
  private Player player;

  private OpenWorldSelector listener;

  @BeforeEach
  void setUp() {
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");
    lenient().when(serviceContext.getItemService()).thenReturn(itemService);
    lenient().when(serviceContext.getWorldMenuService()).thenReturn(worldMenuService);

    listener = new OpenWorldSelector();
    listener.injectContext(serviceContext);
  }

  @Test
  void onWorldSelectorUseOpensWorldMenuWhenItemMatchesAndRightClickBlock() {
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
    when(event.getItem()).thenReturn(heldItemStack);
    when(event.getPlayer()).thenReturn(player);
    when(itemService.find(org.mockito.ArgumentMatchers.any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(worldSelectorItemStack);
    when(worldSelectorItemStack.isSimilar(heldItemStack)).thenReturn(true);

    listener.onWorldSelectorUse(event);

    verify(worldMenuService).openWorldMenu(player);
    verify(event).setCancelled(true);
  }

  @Test
  void onWorldSelectorUseOpensWorldMenuWhenItemMatchesAndRightClickAir() {
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
    when(event.getItem()).thenReturn(heldItemStack);
    when(event.getPlayer()).thenReturn(player);
    when(itemService.find(org.mockito.ArgumentMatchers.any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(worldSelectorItemStack);
    when(worldSelectorItemStack.isSimilar(heldItemStack)).thenReturn(true);

    listener.onWorldSelectorUse(event);

    verify(worldMenuService).openWorldMenu(player);
    verify(event).setCancelled(true);
  }

  @Test
  void onWorldSelectorUseDoesNotOpenMenuWhenHandIsNull() {
    when(event.getHand()).thenReturn(null);

    listener.onWorldSelectorUse(event);

    verify(worldMenuService, never()).openWorldMenu(org.mockito.ArgumentMatchers.any());
    verify(event, never()).setCancelled(true);
  }

  @ParameterizedTest
  @EnumSource(value = EquipmentSlot.class, names = {"OFF_HAND", "HEAD", "CHEST", "LEGS", "FEET", "BODY"})
  void onWorldSelectorUseDoesNotOpenMenuWhenHandIsNotMainHand(EquipmentSlot slot) {
    when(event.getHand()).thenReturn(slot);

    listener.onWorldSelectorUse(event);

    verify(worldMenuService, never()).openWorldMenu(org.mockito.ArgumentMatchers.any());
    verify(event, never()).setCancelled(true);
  }

  @ParameterizedTest
  @EnumSource(value = Action.class, names = {"LEFT_CLICK_BLOCK", "LEFT_CLICK_AIR", "PHYSICAL"})
  void onWorldSelectorUseDoesNotOpenMenuWhenActionIsNotRightClick(Action action) {
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getAction()).thenReturn(action);

    listener.onWorldSelectorUse(event);

    verify(worldMenuService, never()).openWorldMenu(org.mockito.ArgumentMatchers.any());
    verify(event, never()).setCancelled(true);
  }

  @Test
  void onWorldSelectorUseDoesNotOpenMenuWhenHeldItemIsNull() {
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
    when(event.getItem()).thenReturn(null);

    listener.onWorldSelectorUse(event);

    verify(worldMenuService, never()).openWorldMenu(org.mockito.ArgumentMatchers.any());
    verify(event, never()).setCancelled(true);
  }

  @Test
  void onWorldSelectorUseDoesNotOpenMenuWhenItemNotFoundInRegistry() {
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
    when(event.getItem()).thenReturn(heldItemStack);
    when(itemService.find(org.mockito.ArgumentMatchers.any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.empty());

    listener.onWorldSelectorUse(event);

    verify(worldMenuService, never()).openWorldMenu(org.mockito.ArgumentMatchers.any());
    verify(event, never()).setCancelled(true);
  }

  @Test
  void onWorldSelectorUseDoesNotOpenMenuWhenHeldItemDoesNotMatchWorldSelectorItem() {
    when(event.getHand()).thenReturn(EquipmentSlot.HAND);
    when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
    when(event.getItem()).thenReturn(heldItemStack);
    when(itemService.find(org.mockito.ArgumentMatchers.any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(worldSelectorItemStack);
    when(worldSelectorItemStack.isSimilar(heldItemStack)).thenReturn(false);

    listener.onWorldSelectorUse(event);

    verify(worldMenuService, never()).openWorldMenu(org.mockito.ArgumentMatchers.any());
    verify(event, never()).setCancelled(true);
  }

  @Test
  void injectContextInitializesWorldSelectorKeyWithPluginName() {
    verify(pluginMetadataService).getName();
    verify(serviceContext).getPluginMetadataService();
    verify(serviceContext, never()).getItemService();
    verify(serviceContext, never()).getWorldMenuService();
  }
}