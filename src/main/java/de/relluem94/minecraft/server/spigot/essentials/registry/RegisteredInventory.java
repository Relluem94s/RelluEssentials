package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.CustomInventory;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RegisteredInventory {

    @Getter
    private final RegistryKey registryKey;
    @Getter
    private final String title;
    @Getter
    private final int size;
    @Getter
    private final ItemHelper.Type itemFilter;
    private final List<ItemHelper> fixedItems;

    RegisteredInventory(@NonNull RegistryKey registryKey, @NonNull String title, int size, @NonNull ItemHelper.Type itemFilter) {
        this.registryKey = registryKey;
        this.title = title;
        this.size = size;
        this.itemFilter = itemFilter;
        this.fixedItems = new ArrayList<>();
    }

    public RegisteredInventory withFixedItem(@NonNull ItemHelper item) {
        fixedItems.add(item);
        return this;
    }

    public void openFor(@NonNull Player player, ItemHelper... extraItems) {
        List<ItemHelper> items = new ArrayList<>(fixedItems);
        items.addAll(Arrays.asList(extraItems));

        CustomInventory customInventory = new CustomInventory(itemFilter, size, title);
        customInventory.setCustomItems(items);

        InventoryHelper.openInventory(player, InventoryHelper.getCustomItemInventory(customInventory));
    }

    public void openForWithTypeFilter(@NonNull Player player, ItemHelper... extraItems) {
        List<ItemHelper> items = new ArrayList<>(ItemRegistry.getAllByType(itemFilter));
        items.addAll(fixedItems);
        items.addAll(Arrays.asList(extraItems));

        CustomInventory customInventory = new CustomInventory(itemFilter, size, title);
        customInventory.setCustomItems(items);

        InventoryHelper.openInventory(player, InventoryHelper.getCustomItemInventory(customInventory, itemFilter));
    }

    public void openFor(@NonNull Player player) {
        openFor(player, new ItemHelper[0]);
    }

    public void openForWithTypeFilter(@NonNull Player player) {
        openForWithTypeFilter(player, new ItemHelper[0]);
    }

    public List<ItemHelper> getFixedItems() { return Collections.unmodifiableList(fixedItems); }
}