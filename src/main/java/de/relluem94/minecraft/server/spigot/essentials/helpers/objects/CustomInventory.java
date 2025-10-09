package de.relluem94.minecraft.server.spigot.essentials.helpers.objects;


import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces.ICustomItems;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

/**
 *
 * @author Relluem94
 */
@Setter
@Getter
public class CustomInventory {
    private Class<? extends ICustomItems> customItems;
    private ItemHelper.Type type;
    private int size;
    private String titleGUI;

    @ApiStatus.Internal
    public CustomInventory(ItemHelper.Type type, int size, String titleGUI) {
        this.type = type;
        this.size = size;
        this.titleGUI = titleGUI;
    }

    @SuppressWarnings("unused")
    @ApiStatus.AvailableSince("v4.3.3")
    public CustomInventory(Class<? extends ICustomItems> customItems, ItemHelper.Type type, int size, String titleGUI) {
        this.type = type;
        this.size = size;
        this.titleGUI = titleGUI;
        this.customItems = customItems;
    }
}