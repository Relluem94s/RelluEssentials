package de.relluem94.minecraft.server.spigot.essentials.helpers.objects;


import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author Relluem94
 */
@Setter
@Getter
public class CustomInventory {
    private List<ItemHelper> customItems;
    private ItemHelper.Type type;
    private int size;
    private String titleGUI;

    public CustomInventory(ItemHelper.Type type, int size, String titleGUI) {
        this.type = type;
        this.size = size;
        this.titleGUI = titleGUI;
    }
}