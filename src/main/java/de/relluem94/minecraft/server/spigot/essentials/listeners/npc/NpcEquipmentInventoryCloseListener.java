package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.commands.admin.NpcEquipCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcEquipmentInventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;

/**
 * Listener that handles the closing of an NPC equipment inventory.
 *
 * <p>Unlike autoloaded listeners (e.g., {@link NpcChunkLoadListener}), this listener is
 * <strong>not registered automatically</strong> via the {@code @ListenerName} annotation.
 * It is <strong>manually instantiated and registered</strong> at runtime by {@link NpcEquipCommand}
 * for a specific player-NPC interaction session.
 *
 * <p>Once the target player closes the target inventory, this listener saves the updated
 * equipment to the NPC and unregisters itself automatically.
 */
public class NpcEquipmentInventoryCloseListener implements Listener {

  private final ServiceContext serviceContext;
  private final Player targetPlayer;
  private final Npc targetNpc;
  private final Inventory equipmentInventory;

  /**
   * Creates a new {@code NpcEquipmentInventoryCloseListener} for a specific player-NPC session.
   *
   * <p>This constructor must be followed by a manual registration of this listener via
   * {@code Bukkit.getPluginManager().registerEvents(this, plugin)} to activate it.
   *
   * @param serviceContext    the service context providing access to NPC and scheduler services
   * @param targetPlayer      the player whose inventory close event should be handled
   * @param targetNpc         the NPC whose equipment inventory is being edited
   * @param equipmentInventory the specific inventory instance to listen for
   */
  public NpcEquipmentInventoryCloseListener(ServiceContext serviceContext, Player targetPlayer,
      Npc targetNpc, Inventory equipmentInventory) {
    this.serviceContext = serviceContext;
    this.targetPlayer = targetPlayer;
    this.targetNpc = targetNpc;
    this.equipmentInventory = equipmentInventory;
  }

  /**
   * Handles the {@link InventoryCloseEvent} to persist and apply NPC equipment changes.
   *
   * <p>Only processes the event if both the closing player and the closed inventory match
   * the targets defined at construction time. Upon a matching close event, this listener
   * unregisters itself, saves the NPC inventory, and applies the equipment to the in-world
   * entity if it is currently spawned.
   *
   * @param event the inventory close event fired by Bukkit
   */
  @EventHandler
  public void onInventoryClose(@NonNull InventoryCloseEvent event) {
    if (!event.getPlayer().equals(targetPlayer)) {
      return;
    }
    if (!event.getInventory().equals(equipmentInventory)) {
      return;
    }
    HandlerList.unregisterAll(this);

    serviceContext.getNpcService().saveNpcInventory(targetNpc, event.getInventory());

    if (targetNpc.getEntityUUID() != null) {
      NpcEquipmentInventoryHelper.applyInventoryEquipmentToEntity(event.getInventory(),
          targetNpc.getEntityUUID());
    }
  }
}