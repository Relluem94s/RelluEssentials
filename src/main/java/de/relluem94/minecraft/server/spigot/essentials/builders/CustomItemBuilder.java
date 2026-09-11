package de.relluem94.minecraft.server.spigot.essentials.builders;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Builder for creating {@link CustomItem} instances with a fluent API.
 *
 * <p>Requires a {@link RelluEssentialsNamespacedKey} and a {@link Material} as mandatory
 * parameters. All other properties are optional and fall back to their respective defaults if not
 * specified.
 */
public class CustomItemBuilder {

  private final RelluEssentialsNamespacedKey relluEssentialsNamespacedKey;
  private final Material material;
  private final List<CustomItem.EnchantmentData> enchantments = new ArrayList<>();
  private final Map<String, Object> persistentData = new HashMap<>();
  private final List<Consumer<ItemMeta>> metaModifiers = new ArrayList<>();
  private int amount = 1;
  private String displayName = "";
  private List<String> lore = new ArrayList<>();
  private CustomItem.Type type = CustomItem.Type.NONE;
  private CustomItem.Rarity rarity = CustomItem.Rarity.NONE;
  private Integer cost = null;
  private Server server = null;

  /**
   * Builder for creating {@link CustomItem} instances with a fluent API.
   *
   * <p>Requires a {@link RelluEssentialsNamespacedKey} and a {@link Material} as mandatory
   * parameters. All other properties are optional and fall back to their respective defaults if not
   * specified.
   */
  public CustomItemBuilder(RelluEssentialsNamespacedKey relluEssentialsNamespacedKey,
      Material material) {
    this.relluEssentialsNamespacedKey = relluEssentialsNamespacedKey;
    this.material = material;
  }

  /**
   * Sets the stack size of the item.
   *
   * @param amount the number of items in the stack
   * @return this builder instance
   */
  public CustomItemBuilder amount(int amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Sets the display name of the item.
   *
   * @param displayName the name shown to players in-game
   * @return this builder instance
   */
  public CustomItemBuilder displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Sets the lore lines of the item, replacing any previously set lore.
   *
   * @param lore the list of lore lines to display beneath the item name
   * @return this builder instance
   */
  public CustomItemBuilder lore(List<String> lore) {
    this.lore = new ArrayList<>(lore);
    return this;
  }

  /**
   * Sets the type classification of the item.
   *
   * @param type the {@link CustomItem.Type} to assign to the item
   * @return this builder instance
   */
  public CustomItemBuilder type(CustomItem.Type type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the rarity of the item.
   *
   * @param rarity the {@link CustomItem.Rarity} to assign to the item
   * @return this builder instance
   */
  public CustomItemBuilder rarity(CustomItem.Rarity rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Sets the cost of the item.
   *
   * @param cost the monetary or point value associated with the item
   * @return this builder instance
   */
  public CustomItemBuilder cost(int cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Adds a list of enchantments to the item, appending to any previously added enchantments.
   *
   * @param enchantments the list of {@link CustomItem.EnchantmentData} to apply to the item
   * @param server       the Bukkit {@link Server} instance used to resolve enchantments from the
   *                     registry
   * @return this builder instance
   */
  public CustomItemBuilder enchantments(List<CustomItem.EnchantmentData> enchantments,
      Server server) {
    this.enchantments.addAll(enchantments);
    this.server = server;
    return this;
  }

  /**
   * Adds multiple persistent data entries to the item, merging with any previously added entries.
   *
   * @param persistentData a map of keys to values to store as persistent data on the item
   * @return this builder instance
   */
  public CustomItemBuilder persistentData(Map<String, Object> persistentData) {
    this.persistentData.putAll(persistentData);
    return this;
  }

  /**
   * Adds a single persistent data entry to the item.
   *
   * @param key   the key under which the value is stored
   * @param value the value to associate with the given key
   * @return this builder instance
   */
  public CustomItemBuilder addPersistentData(String key, Object value) {
    this.persistentData.put(key, value);
    return this;
  }

  /**
   * Adds a custom {@link ItemMeta} modifier that is applied during item construction.
   *
   * <p>Multiple modifiers are applied in the order they were added.
   *
   * @param modifier a {@link Consumer} that receives and modifies the item's {@link ItemMeta}
   * @return this builder instance
   */
  public CustomItemBuilder metaModifier(Consumer<ItemMeta> modifier) {
    this.metaModifiers.add(modifier);
    return this;
  }

  /**
   * Constructs and returns a new {@link CustomItem} based on the current builder state.
   *
   * <p>All collection-based properties are wrapped in unmodifiable views before being passed
   * to the {@link CustomItem} constructor.
   *
   * @return a fully configured {@link CustomItem} instance
   */
  public CustomItem build() {
    return new CustomItem(material, amount, displayName, Collections.unmodifiableList(lore), type,
        rarity, cost, Collections.unmodifiableList(enchantments),
        Collections.unmodifiableMap(persistentData), Collections.unmodifiableList(metaModifiers),
        relluEssentialsNamespacedKey, server);
  }
}