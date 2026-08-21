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
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Builder for creating {@link CustomItem} instances.
 */
public class CustomItemBuilder {
  private final RelluEssentialsNamespacedKey relluEssentialsNamespacedKey;
  private final Material material;

  private int amount = 1;
  private String displayName = "";
  private List<String> lore = new ArrayList<>();
  private CustomItem.Type type = CustomItem.Type.NONE;
  private CustomItem.Rarity rarity = CustomItem.Rarity.NONE;
  private Integer cost = null;
  private final List<CustomItem.EnchantmentData> enchantments = new ArrayList<>();
  private final Map<String, Object> persistentData = new HashMap<>();
  private final List<Consumer<ItemMeta>> metaModifiers = new ArrayList<>();

  public CustomItemBuilder(RelluEssentialsNamespacedKey relluEssentialsNamespacedKey, Material material) {
    this.relluEssentialsNamespacedKey = relluEssentialsNamespacedKey;
    this.material = material;
  }

  public CustomItemBuilder amount(int amount) {
    this.amount = amount;
    return this;
  }

  public CustomItemBuilder displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public CustomItemBuilder lore(List<String> lore) {
    this.lore = new ArrayList<>(lore);
    return this;
  }

  public CustomItemBuilder type(CustomItem.Type type) {
    this.type = type;
    return this;
  }

  public CustomItemBuilder rarity(CustomItem.Rarity rarity) {
    this.rarity = rarity;
    return this;
  }

  public CustomItemBuilder cost(int cost) {
    this.cost = cost;
    return this;
  }

  public CustomItemBuilder enchantments(List<CustomItem.EnchantmentData> enchantments) {
    this.enchantments.addAll(enchantments);
    return this;
  }

  public CustomItemBuilder persistentData(Map<String, Object> persistentData) {
    this.persistentData.putAll(persistentData);
    return this;
  }

  public CustomItemBuilder addPersistentData(String key, Object value) {
    this.persistentData.put(key, value);
    return this;
  }

  public CustomItemBuilder metaModifier(Consumer<ItemMeta> modifier) {
    this.metaModifiers.add(modifier);
    return this;
  }

  public CustomItem build() {
    return new CustomItem(
        material,
        amount,
        displayName,
        Collections.unmodifiableList(lore),
        type,
        rarity,
        cost,
        Collections.unmodifiableList(enchantments),
        Collections.unmodifiableMap(persistentData),
        Collections.unmodifiableList(metaModifiers),
        relluEssentialsNamespacedKey
    );
  }
}