package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CloudSailorTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private ItemService itemService;

  @Mock
  private WorldGroupService worldGroupService;

  @Mock
  private CustomItem cloudSailorCustomItem;

  @Mock
  private CustomItem cloudBootsCustomItem;

  @Mock
  private ItemStack cloudSailorItemStack;

  @Mock
  private ItemStack cloudBootsItemStack;

  @Mock
  private EntityDeathEvent entityDeathEvent;

  @Mock
  private PrepareItemCraftEvent prepareItemCraftEvent;

  @Mock
  private CraftingInventory craftingInventory;

  @Mock
  private EntityDamageEvent entityDamageEvent;

  @Mock
  private PlayerMoveEvent playerMoveEvent;

  @Mock
  private Player player;

  @Mock
  private PlayerInventory playerInventory;

  @Mock
  private World world;

  @Mock
  private Location fromLocation;

  @Mock
  private Location toLocation;

  @Mock
  private Block fromBlock;

  @Mock
  private Block toBlock;

  @Mock
  private Location fromBlockLocation;

  @Mock
  private Location toBlockLocation;

  private CloudSailor listener;

  @Mock
  private Block blockBelowPlayer;

  @Mock
  private Block blockTwoBelowPlayer;

  @Mock
  private Block blockEastOfBelow;

  @Mock
  private Block blockNorthOfBelow;

  @Mock
  private Block blockSouthOfBelow;

  @Mock
  private Block blockWestOfBelow;

  @Mock
  private Location playerLocation;

  @Mock
  private Block playerLocationBlock;

  @Mock
  private Vector playerDirection;

  @BeforeEach
  void setUp() {
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("relluessentials");
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class)))
        .thenReturn(Optional.of(cloudSailorCustomItem))
        .thenReturn(Optional.of(cloudBootsCustomItem));
    lenient().when(cloudSailorCustomItem.toItemStack()).thenReturn(cloudSailorItemStack);
    lenient().when(cloudBootsCustomItem.toItemStack()).thenReturn(cloudBootsItemStack);

    listener = new CloudSailor();
    listener.injectContext(serviceContext);
  }

  private void stubWorldSettingActive(boolean active) {
    when(playerMoveEvent.getPlayer()).thenReturn(player);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.USE_CLOUDSAILOR, "world")).thenReturn(active);
  }

  private void stubBlockLocationsDifferent() {
    when(playerMoveEvent.getFrom()).thenReturn(fromLocation);
    when(fromLocation.getBlock()).thenReturn(fromBlock);
    when(fromBlock.getLocation()).thenReturn(fromBlockLocation);
    when(playerMoveEvent.getTo()).thenReturn(toLocation);
    when(toLocation.getBlock()).thenReturn(toBlock);
    when(toBlock.getLocation()).thenReturn(toBlockLocation);
  }

  private void stubBlockLocationsSame() {
    when(playerMoveEvent.getFrom()).thenReturn(fromLocation);
    when(fromLocation.getBlock()).thenReturn(fromBlock);
    when(fromBlock.getLocation()).thenReturn(fromBlockLocation);
    when(playerMoveEvent.getTo()).thenReturn(toLocation);
    when(toLocation.getBlock()).thenReturn(toBlock);
    when(toBlock.getLocation()).thenReturn(fromBlockLocation);
  }

  @Test
  void mobDeathDropsCloudSailorItemWhenChickenDies() {
    org.bukkit.entity.Chicken chicken = Mockito.mock(org.bukkit.entity.Chicken.class);
    List<ItemStack> drops = new ArrayList<>();
    when(entityDeathEvent.getEntity()).thenReturn(chicken);
    when(entityDeathEvent.getDrops()).thenReturn(drops);

    for (int attempt = 0; attempt < 1000; attempt++) {
      listener.mobDeath(entityDeathEvent);
    }

    verify(entityDeathEvent, Mockito.atLeastOnce()).getDrops();
  }

  @Test
  void mobDeathDoesNotDropCloudSailorItemWhenEntityIsNotChicken() {
    org.bukkit.entity.Zombie zombie = Mockito.mock(org.bukkit.entity.Zombie.class);
    when(entityDeathEvent.getEntity()).thenReturn(zombie);

    listener.mobDeath(entityDeathEvent);

    verify(entityDeathEvent, never()).getDrops();
    verify(entityDeathEvent, never()).setDroppedExp(30);
  }

  @Test
  void cloudBootsCraftingCancelsResultWhenMatrixItemHasNoItemMeta() {
    org.bukkit.inventory.Recipe recipe = Mockito.mock(org.bukkit.inventory.Recipe.class);
    ItemStack recipeResult = Mockito.mock(ItemStack.class);
    ItemStack matrixItem = Mockito.mock(ItemStack.class);

    when(prepareItemCraftEvent.getRecipe()).thenReturn(recipe);
    when(recipe.getResult()).thenReturn(recipeResult);
    when(recipeResult.hasItemMeta()).thenReturn(true);
    when(cloudSailorItemStack.isSimilar(recipeResult)).thenReturn(true);
    when(prepareItemCraftEvent.getInventory()).thenReturn(craftingInventory);
    when(craftingInventory.getMatrix()).thenReturn(new ItemStack[]{matrixItem});
    when(matrixItem.hasItemMeta()).thenReturn(false);

    listener.cloudBootsCrafting(prepareItemCraftEvent);

    verify(craftingInventory).setResult(null);
  }

  @Test
  void cloudBootsCraftingCancelsResultWhenMatrixItemIsNotCloudSailorItem() {
    org.bukkit.inventory.Recipe recipe = Mockito.mock(org.bukkit.inventory.Recipe.class);
    ItemStack recipeResult = Mockito.mock(ItemStack.class);
    ItemStack matrixItem = Mockito.mock(ItemStack.class);

    when(prepareItemCraftEvent.getRecipe()).thenReturn(recipe);
    when(recipe.getResult()).thenReturn(recipeResult);
    when(recipeResult.hasItemMeta()).thenReturn(true);
    when(cloudSailorItemStack.isSimilar(recipeResult)).thenReturn(true);
    when(prepareItemCraftEvent.getInventory()).thenReturn(craftingInventory);
    when(craftingInventory.getMatrix()).thenReturn(new ItemStack[]{matrixItem});
    when(matrixItem.hasItemMeta()).thenReturn(true);
    when(cloudSailorItemStack.isSimilar(matrixItem)).thenReturn(false);

    listener.cloudBootsCrafting(prepareItemCraftEvent);

    verify(craftingInventory).setResult(null);
  }

  @Test
  void cloudBootsCraftingDoesNotCancelResultWhenMatrixItemIsCloudSailorItem() {
    org.bukkit.inventory.Recipe recipe = Mockito.mock(org.bukkit.inventory.Recipe.class);
    ItemStack recipeResult = Mockito.mock(ItemStack.class);
    ItemStack matrixItem = Mockito.mock(ItemStack.class);

    when(prepareItemCraftEvent.getRecipe()).thenReturn(recipe);
    when(recipe.getResult()).thenReturn(recipeResult);
    when(recipeResult.hasItemMeta()).thenReturn(true);
    when(cloudSailorItemStack.isSimilar(recipeResult)).thenReturn(true);
    when(prepareItemCraftEvent.getInventory()).thenReturn(craftingInventory);
    when(craftingInventory.getMatrix()).thenReturn(new ItemStack[]{matrixItem});
    when(matrixItem.hasItemMeta()).thenReturn(true);
    when(cloudSailorItemStack.isSimilar(matrixItem)).thenReturn(true);

    listener.cloudBootsCrafting(prepareItemCraftEvent);

    verify(craftingInventory, never()).setResult(null);
  }

  @Test
  void cloudBootsCraftingDoesNothingWhenRecipeIsNull() {
    when(prepareItemCraftEvent.getRecipe()).thenReturn(null);

    listener.cloudBootsCrafting(prepareItemCraftEvent);

    verify(prepareItemCraftEvent, never()).getInventory();
  }

  @Test
  void cloudBootsCraftingDoesNothingWhenResultHasNoItemMeta() {
    org.bukkit.inventory.Recipe recipe = Mockito.mock(org.bukkit.inventory.Recipe.class);
    ItemStack recipeResult = Mockito.mock(ItemStack.class);

    when(prepareItemCraftEvent.getRecipe()).thenReturn(recipe);
    when(recipe.getResult()).thenReturn(recipeResult);
    when(recipeResult.hasItemMeta()).thenReturn(false);

    listener.cloudBootsCrafting(prepareItemCraftEvent);

    verify(prepareItemCraftEvent, never()).getInventory();
  }

  @Test
  void cloudBootsCraftingDoesNothingWhenResultIsNotCloudSailorItem() {
    org.bukkit.inventory.Recipe recipe = Mockito.mock(org.bukkit.inventory.Recipe.class);
    ItemStack recipeResult = Mockito.mock(ItemStack.class);

    when(prepareItemCraftEvent.getRecipe()).thenReturn(recipe);
    when(recipe.getResult()).thenReturn(recipeResult);
    when(recipeResult.hasItemMeta()).thenReturn(true);
    when(cloudSailorItemStack.isSimilar(recipeResult)).thenReturn(false);

    listener.cloudBootsCrafting(prepareItemCraftEvent);

    verify(prepareItemCraftEvent, never()).getInventory();
  }

  @Test
  void cloudBootsCraftingSkipsNullMatrixSlots() {
    org.bukkit.inventory.Recipe recipe = Mockito.mock(org.bukkit.inventory.Recipe.class);
    ItemStack recipeResult = Mockito.mock(ItemStack.class);

    when(prepareItemCraftEvent.getRecipe()).thenReturn(recipe);
    when(recipe.getResult()).thenReturn(recipeResult);
    when(recipeResult.hasItemMeta()).thenReturn(true);
    when(cloudSailorItemStack.isSimilar(recipeResult)).thenReturn(true);
    when(prepareItemCraftEvent.getInventory()).thenReturn(craftingInventory);
    when(craftingInventory.getMatrix()).thenReturn(new ItemStack[]{null, null});

    listener.cloudBootsCrafting(prepareItemCraftEvent);

    verify(craftingInventory, never()).setResult(null);
  }

  @Test
  void onFallDamageDoesNothingWhenEntityIsNotPlayer() {
    org.bukkit.entity.Zombie zombie = Mockito.mock(org.bukkit.entity.Zombie.class);
    when(entityDamageEvent.getEntity()).thenReturn(zombie);

    listener.onFallDamage(entityDamageEvent);

    verify(entityDamageEvent, never()).setCancelled(true);
    verify(entityDamageEvent, never()).setDamage(any(double.class));
  }

  @Test
  void onFallDamageDoesNothingWhenCloudSailorSettingIsDisabled() {
    when(entityDamageEvent.getEntity()).thenReturn(player);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.USE_CLOUDSAILOR, "world")).thenReturn(false);

    listener.onFallDamage(entityDamageEvent);

    verify(entityDamageEvent, never()).setCancelled(true);
    verify(entityDamageEvent, never()).setDamage(any(double.class));
  }

  @Test
  void onFallDamageDoesNothingWhenDamageCauseIsNotFall() {
    when(entityDamageEvent.getEntity()).thenReturn(player);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.USE_CLOUDSAILOR, "world")).thenReturn(true);
    when(entityDamageEvent.getCause()).thenReturn(DamageCause.VOID);

    listener.onFallDamage(entityDamageEvent);

    verify(entityDamageEvent, never()).setCancelled(true);
    verify(entityDamageEvent, never()).setDamage(any(double.class));
  }

  @Test
  void onFallDamageCancelsDamageWhenPlayerWearingCloudBoots() {
    when(entityDamageEvent.getEntity()).thenReturn(player);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.USE_CLOUDSAILOR, "world")).thenReturn(true);
    when(entityDamageEvent.getCause()).thenReturn(DamageCause.FALL);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getBoots()).thenReturn(cloudBootsItemStack);

    listener.onFallDamage(entityDamageEvent);

    verify(entityDamageEvent).setCancelled(true);
  }

  @Test
  void onFallDamageHalvesDamageWhenPlayerHoldsCloudSailorInOffHand() {
    when(entityDamageEvent.getEntity()).thenReturn(player);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.USE_CLOUDSAILOR, "world")).thenReturn(true);
    when(entityDamageEvent.getCause()).thenReturn(DamageCause.FALL);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getBoots()).thenReturn(null);
    when(playerInventory.getItemInOffHand()).thenReturn(cloudSailorItemStack);
    when(entityDamageEvent.getDamage()).thenReturn(10.0);

    listener.onFallDamage(entityDamageEvent);

    verify(entityDamageEvent).setDamage(5.0);
  }

  @Test
  void onFallDamageDoesNothingWhenPlayerHasNeitherBootsNorCloudSailor() {
    ItemStack otherItem = Mockito.mock(ItemStack.class);

    when(entityDamageEvent.getEntity()).thenReturn(player);
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("world");
    when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    when(worldGroupService.isSettingActiveForWorld(WorldSetting.USE_CLOUDSAILOR, "world")).thenReturn(true);
    when(entityDamageEvent.getCause()).thenReturn(DamageCause.FALL);
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getBoots()).thenReturn(null);
    when(playerInventory.getItemInOffHand()).thenReturn(otherItem);

    listener.onFallDamage(entityDamageEvent);

    verify(entityDamageEvent, never()).setCancelled(true);
    verify(entityDamageEvent, never()).setDamage(any(double.class));
  }

  @Test
  void onSailDoesNothingWhenCloudSailorSettingIsDisabled() {
    stubWorldSettingActive(false);

    listener.onSail(playerMoveEvent);

    verify(playerMoveEvent, never()).getTo();
  }

  @Test
  void onSailDoesNothingWhenToLocationIsNull() {
    stubWorldSettingActive(true);
    when(playerMoveEvent.getTo()).thenReturn(null);
    lenient().when(playerMoveEvent.getFrom()).thenReturn(fromLocation);
    lenient().when(fromLocation.getBlock()).thenReturn(fromBlock);
    lenient().when(fromBlock.getLocation()).thenReturn(fromBlockLocation);

    listener.onSail(playerMoveEvent);

    verify(player, never()).setVelocity(any());
  }

  @Test
  void onSailDoesNothingWhenFromAndToBlockLocationAreEqual() {
    stubWorldSettingActive(true);
    stubBlockLocationsSame();

    listener.onSail(playerMoveEvent);

    verify(player, never()).setVelocity(any());
  }

  @Test
  void onSailDoesNothingWhenPlayerHasNeitherCloudSailorNorBoots() {
    ItemStack otherItem = Mockito.mock(ItemStack.class);

    stubWorldSettingActive(true);
    stubBlockLocationsDifferent();
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInOffHand()).thenReturn(otherItem);
    when(playerInventory.getBoots()).thenReturn(null);

    listener.onSail(playerMoveEvent);

    verify(player, never()).setVelocity(any());
  }

  @Test
  void onSailDoesNothingWhenPlayerIsFlying() {
    stubWorldSettingActive(true);
    stubBlockLocationsDifferent();
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInOffHand()).thenReturn(cloudSailorItemStack);
    when(player.isFlying()).thenReturn(true);

    listener.onSail(playerMoveEvent);

    verify(player, never()).setVelocity(any());
  }

  @Test
  void onSailDoesNothingWhenPlayerIsSneaking() {
    stubWorldSettingActive(true);
    stubBlockLocationsDifferent();
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInOffHand()).thenReturn(cloudSailorItemStack);
    when(player.isFlying()).thenReturn(false);
    when(player.isSneaking()).thenReturn(true);

    listener.onSail(playerMoveEvent);

    verify(player, never()).setVelocity(any());
  }

  @Test
  void injectContextResolvesCloudSailorAndCloudBootsItems() {
    verify(serviceContext, Mockito.times(2)).getPluginMetadataService();
    verify(pluginMetadataService, Mockito.times(2)).getName();
    verify(serviceContext, Mockito.times(2)).getItemService();
    verify(itemService, Mockito.times(2)).find(any(RelluEssentialsNamespacedKey.class));
  }

  @Test
  void onSailDoesNothingWhenPlayerWearingCloudBootsAndBlocksBelowAreNotAir() {
    stubWorldSettingActive(true);
    stubBlockLocationsDifferent();
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInOffHand()).thenReturn(Mockito.mock(ItemStack.class));
    when(playerInventory.getBoots()).thenReturn(cloudBootsItemStack);
    when(player.isFlying()).thenReturn(false);
    when(player.isSneaking()).thenReturn(false);
    stubPlayerLocationBlockChainWithMaterial(Material.STONE);

    listener.onSail(playerMoveEvent);

    verify(player, never()).setVelocity(any());
  }

  @Test
  void onSailSetsVelocityAndPlaysEffectWhenPlayerWearingCloudBootsAndBlocksBelowAreAir() {
    stubWorldSettingActive(true);
    stubBlockLocationsDifferent();
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInOffHand()).thenReturn(Mockito.mock(ItemStack.class));
    when(playerInventory.getBoots()).thenReturn(cloudBootsItemStack);
    when(player.isFlying()).thenReturn(false);
    when(player.isSneaking()).thenReturn(false);
    stubPlayerLocationBlockChainWithMaterial(Material.AIR);
    stubPlayerDirectionAndWorld();

    listener.onSail(playerMoveEvent);

    verify(player).setVelocity(any(org.bukkit.util.Vector.class));
    verify(world).playEffect(any(Location.class), any(Effect.class), anyInt());
  }

  @Test
  void onSailSetsVelocityAndPlaysEffectWhenPlayerHoldsCloudSailorAndBlocksBelowAreAir() {
    stubWorldSettingActive(true);
    stubBlockLocationsDifferent();
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInOffHand()).thenReturn(cloudSailorItemStack);
    when(player.isFlying()).thenReturn(false);
    when(player.isSneaking()).thenReturn(false);
    stubPlayerLocationBlockChainWithMaterial(Material.AIR);
    stubPlayerDirectionAndWorld();

    listener.onSail(playerMoveEvent);

    verify(player).setVelocity(any(org.bukkit.util.Vector.class));
    verify(world).playEffect(any(Location.class), any(Effect.class), anyInt());
  }

  @Test
  void onSailDoesNotSetVelocityWhenPlayerHoldsCloudSailorAndBlocksBelowAreNotAir() {
    stubWorldSettingActive(true);
    stubBlockLocationsDifferent();
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getItemInOffHand()).thenReturn(cloudSailorItemStack);
    when(player.isFlying()).thenReturn(false);
    when(player.isSneaking()).thenReturn(false);
    stubPlayerLocationBlockChainWithMaterial(Material.STONE);

    listener.onSail(playerMoveEvent);

    verify(player, never()).setVelocity(any());
    verify(world, never()).playEffect(any(), any(), anyInt());
  }


  private void stubPlayerLocationBlockChainWithMaterial(Material material) {
    when(player.getLocation()).thenReturn(playerLocation);
    when(playerLocation.getBlock()).thenReturn(playerLocationBlock);
    when(playerLocationBlock.getRelative(BlockFace.DOWN)).thenReturn(blockBelowPlayer);
    when(blockBelowPlayer.getRelative(BlockFace.DOWN)).thenReturn(blockTwoBelowPlayer);
    when(blockBelowPlayer.getRelative(BlockFace.EAST)).thenReturn(blockEastOfBelow);
    when(blockBelowPlayer.getRelative(BlockFace.NORTH)).thenReturn(blockNorthOfBelow);
    when(blockBelowPlayer.getRelative(BlockFace.SOUTH)).thenReturn(blockSouthOfBelow);
    when(blockBelowPlayer.getRelative(BlockFace.WEST)).thenReturn(blockWestOfBelow);
    when(blockBelowPlayer.getType()).thenReturn(material);
    when(blockTwoBelowPlayer.getType()).thenReturn(material);
    when(blockEastOfBelow.getType()).thenReturn(material);
    when(blockNorthOfBelow.getType()).thenReturn(material);
    when(blockSouthOfBelow.getType()).thenReturn(material);
    when(blockWestOfBelow.getType()).thenReturn(material);
  }

  private void stubPlayerDirectionAndWorld() {
    when(playerLocation.getDirection()).thenReturn(playerDirection);
    when(playerDirection.multiply(0.5)).thenReturn(playerDirection);
    when(playerDirection.getX()).thenReturn(1.0);
    when(playerDirection.getZ()).thenReturn(0.0);
    when(player.getWorld()).thenReturn(world);
  }

}