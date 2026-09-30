package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_NPC_UNIMPLEMENTED_METHOD;

import de.relluem94.minecraft.server.spigot.essentials.builders.CustomItemBuilder;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.npc.Trader;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;

/**
 * Represents a trader NPC that can be spawned and interacted with in the game world.
 * Each trader NPC has a name, profession, type, and an associated spawn item.
 *
 * @author rellu
 */
public class TraderNpc implements Trader {

  private final String name;
  private final CustomItem npcSpawnItem;
  private final Profession profession;
  private final Type type;

  /**
   * Constructs a TraderNpc from a {@link TraderNpcEntry} configuration object.
   *
   * @param traderNpcEntry the entry containing the name, profession, and type of the trader NPC
   */
  public TraderNpc(@NonNull TraderNpcEntry traderNpcEntry) {
    this(traderNpcEntry.getName(), traderNpcEntry.getProfession(), traderNpcEntry.getType());
  }

  /**
   * Constructs a TraderNpc with the given name, profession, and type.
   * Also initializes the NPC spawn item using the provided name as identifier and display name.
   *
   * @param name       the display name of the trader NPC
   * @param profession the villager profession assigned to this NPC
   * @param type       the functional type of this trader NPC
   */
  public TraderNpc(String name, Profession profession, Type type) {
    this.name = name;
    this.profession = profession;
    this.type = type;
    this.npcSpawnItem = new CustomItemBuilder(
        new RelluEssentialsNamespacedKey("relluessentials", name),
        Material.VILLAGER_SPAWN_EGG)
        .type(CustomItem.Type.NPC)
        .rarity(CustomItem.Rarity.LEGENDARY)
        .displayName(name)
        .lore(List.of(ItemConstants.PLUGIN_ITEM_NPC_LORE1))
        .amount(1)
        .build();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getTitle() {
    return Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + getName();
  }

  @Override
  public CustomItem getCustomItem() {
    return npcSpawnItem;
  }

  @Override
  public Profession getProfession() {
    return profession;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Inventory getMainGui() {
    throw new UnsupportedOperationException(PLUGIN_EXCEPTION_NPC_UNIMPLEMENTED_METHOD);
  }

  /**
   * Defines the functional role of a trader NPC within the plugin.
   *
   * <ul>
   *   <li>{@link #TRADER} – a standard trading NPC offering buy and sell interactions</li>
   *   <li>{@link #BANKER} – a banking NPC handling currency or storage interactions</li>
   *   <li>{@link #CHAT} – a NPC focused on dialogue or information delivery</li>
   *   <li>{@link #ENCHANTER} – a NPC offering enchantment-related services</li>
   *   <li>{@link #BEEKEEPER} – a NPC specialized in beekeeping-related trades</li>
   *   <li>{@link #OTHER} – a NPC with a role not covered by the other types</li>
   * </ul>
   */
  public enum Type {
    TRADER, BANKER, CHAT, ENCHANTER, BEEKEEPER, OTHER
  }
}