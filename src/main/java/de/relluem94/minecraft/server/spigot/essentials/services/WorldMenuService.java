package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Service responsible for building and presenting the world selection menu to a player.
 */
public class WorldMenuService {

  private final ItemService itemService;
  private final PluginMetadataService pluginMetadataService;

  /**
   * Constructs a new WorldMenuService.
   *
   * @param itemService           the service used to retrieve disabled-slot items
   * @param pluginMetadataService the service used to access plugin and server metadata
   */
  public WorldMenuService(ItemService itemService, PluginMetadataService pluginMetadataService) {
    this.itemService = itemService;
    this.pluginMetadataService = pluginMetadataService;
  }

  /**
   * Opens a graphical world selection inventory for the given player.
   *
   * <p>Displays all currently loaded worlds as clickable globe heads
   * inside an {@link Inventory}.</p>
   *
   * @param player the player who receives the inventory
   */
  public void openWorldMenu(Player player) {
    Inventory inventory = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(18,
            Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dWorlds"),
        itemService.find(new RelluEssentialsNamespacedKey(pluginMetadataService.getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED)).orElseThrow().toItemStack());

    List<World> worlds = pluginMetadataService.getPlugin().getServer().getWorlds();
    for (int i = 0; i < worlds.size(); i++) {
      ItemStack globeHead = PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE);
      ItemMeta meta = globeHead.getItemMeta();

      if (meta == null) {
        return;
      }

      meta.setDisplayName(worlds.get(i).getName());
      globeHead.setItemMeta(meta);
      inventory.setItem(i, globeHead);
    }

    InventoryHelper.openInventory(player, inventory);
  }
}