package de.relluem94.minecraft.server.spigot.essentials.npc.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_NPC_UNIMPLEMENTED_METHOD;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.npc.Trader;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.TraderNPCEntry;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;

public class TraderNpc implements Trader {

  private final String name;
  private final ItemHelper npcSpawnItem;
  private final Profession profession;
  private final Type type;

  public TraderNpc(@NonNull TraderNPCEntry traderNpcEntry) {
    this(traderNpcEntry.getName(), traderNpcEntry.getProfession(), traderNpcEntry.getType());
  }

  public TraderNpc(String name, Profession profession, Type type) {
    this.name = name;
    this.profession = profession;
    this.type = type;
    this.npcSpawnItem = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, getName(),
        de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type.NPC,
        Rarity.LEGENDARY, List.of(ItemConstants.PLUGIN_ITEM_NPC_LORE1));
    RelluEssentials.getInstance().getTraderNpcRegistry().addNPC(this);
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
  public ItemHelper getItemHelper() {
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
    TRADER,
    BANKER,
    CHAT,
    ENCHANTER,
    BEEKEEPER,
    OTHER
  }
}