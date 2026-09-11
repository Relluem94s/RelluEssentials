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

public class TraderNpc implements Trader {

  private final String name;
  private final CustomItem npcSpawnItem;
  private final Profession profession;
  private final Type type;

  public TraderNpc(@NonNull TraderNpcEntry traderNpcEntry) {
    this(traderNpcEntry.getName(), traderNpcEntry.getProfession(), traderNpcEntry.getType());
  }

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
  public Inventory getMainGUI() {
    throw new UnsupportedOperationException(PLUGIN_EXCEPTION_NPC_UNIMPLEMENTED_METHOD);
  }

  public enum Type {
    TRADER, BANKER, CHAT, ENCHANTER, BEEKEEPER, OTHER
  }
}