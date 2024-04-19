package de.relluem94.minecraft.server.spigot.essentials.npc;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.interfaces.INPC;

public class NPC implements INPC {

    private final String name;
    private final ItemHelper npcSpawnItem;
    private final Profession profession;
    private final Type type;

    public NPC(NPCEntry npcEntry){
        this(npcEntry.getName(), npcEntry.getProfession(), npcEntry.getType());
    }

    public NPC(String name, Profession profession, Type type){
        this.name = name;
        this.profession = profession;
        this.type = type;
        this.npcSpawnItem = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, getName(), de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type.NPC, Rarity.LEGENDARY, List.of(ItemConstants.PLUGIN_ITEM_NPC_LORE1));
        RelluEssentials.getInstance().getNpcAPI().addNPC(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE+ getName();
    }

    @Override
    public ItemHelper getItemHelper() {
        return npcSpawnItem;
    }

    @Override
    public Profession getProfession(){
        return profession;
    }

    @Override
    public Type getType(){
        return type;
    }

    public enum Type {
        TRADER,
        BANKER,
        CHAT,
        ENCHANTER,
        OTHER
    }

    @Override
    public Inventory getMainGUI() {
        throw new UnsupportedOperationException("Unimplemented method 'getMainGUI'");
    }
}