package de.relluem94.minecraft.server.spigot.essentials.npc;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.interfaces.INPC;

public class NPC implements INPC {

    private String name;
    private ItemHelper npcSpawnItem;
    private Profession profession;
    private Type type;

    public NPC(NPCEntry npcEntry){
        this(npcEntry.getName(), npcEntry.getProfession(), npcEntry.getType());
    }

    public NPC(String name, Profession profession, Type type){
        this.name = name;
        this.profession = profession;
        this.type = type;
        this.npcSpawnItem = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, getName(), de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));
        RelluEssentials.getInstance().getNpcAPI().addNPC(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return Strings.PLUGIN_NAME_PREFIX + Strings.PLUGIN_FORMS_SPACER_MESSAGE+ getName();
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
        OTHER;
    }

    @Override
    public Inventory getMainGUI() {
        throw new UnsupportedOperationException("Unimplemented method 'getMainGUI'");
    }
}