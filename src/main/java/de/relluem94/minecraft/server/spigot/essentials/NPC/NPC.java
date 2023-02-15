package de.relluem94.minecraft.server.spigot.essentials.NPC;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.NPC.interfaces.INPC;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;

public abstract class NPC implements INPC {

    private String name;
    private ItemHelper npc;
    private Profession profession;
    private Type type;

    public NPC(NPCEntry npcEntry){
        this(npcEntry.getName(), npcEntry.getProfession(), npcEntry.getType());
    }

    public NPC(String name, Profession profession, Type type){
        this.name = name;
        this.profession = profession;
        this.type = type;
        this.npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, getName(), de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));
        RelluEssentials.npcs.add(this);
        RelluEssentials.npc_itemstack.add(this.npc.getCustomItem());
        RelluEssentials.npc_name.add(getName());

        if(getType().equals(Type.TRADER)){
            RelluEssentials.npc_trader_title.add(getTitle());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + getName();
    }

    @Override
    public ItemHelper getItemHelper() {
        return npc;
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
}
