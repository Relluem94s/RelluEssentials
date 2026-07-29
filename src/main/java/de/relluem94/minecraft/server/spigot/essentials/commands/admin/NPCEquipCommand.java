package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCEquipmentInventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class NPCEquipCommand implements SubCommand {

    private static final int ARGS_SUBCOMMAND_INDEX = 0;
    private static final int ARGS_ACTION_INDEX = 1;
    private static final int ARGS_NPC_ID_INDEX = 2;
    private static final int REQUIRED_ARGS_LENGTH = 3;
    private static final int NPC_EQUIPMENT_INVENTORY_SIZE = 54;
    private static final String NPC_EQUIPMENT_INVENTORY_TITLE_PREFIX = "NPC Equipment: ";

    @Override
    public void execute(Player player, String[] args) {
        if (!Permission.isAuthorized(player, Groups.getGroup("admin").getId())) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
            return;
        }

        if (args.length < REQUIRED_ARGS_LENGTH) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_EQUIP_USAGE));
            return;
        }

        UUID npcId;
        try {
            npcId = UUID.fromString(args[ARGS_NPC_ID_INDEX]);
        } catch (IllegalArgumentException e) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_INVALID_ID));
            return;
        }

        Optional<NPC> npcOptional = RelluEssentials.getInstance().getNpcService().getNPCById(npcId);
        if (npcOptional.isEmpty()) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_NOT_FOUND));
            return;
        }

        NPC npc = npcOptional.get();
        openNPCInventoryForPlayer(player, npc);
    }

    private void openNPCInventoryForPlayer(Player player, @NonNull NPC npc) {
        Inventory equipmentInventory = Bukkit.createInventory(
                null,
                NPC_EQUIPMENT_INVENTORY_SIZE,
                NPC_EQUIPMENT_INVENTORY_TITLE_PREFIX + npc.getId()
        );

        if (npc.getInventory() != null) {
            InventoryHelper.loadInventoryFromJSON(equipmentInventory, npc.getInventory());
        }

        if (npc.getEntityUUID() != null) {
            NPCEquipmentInventoryHelper.loadEntityEquipmentIntoInventory(npc.getEntityUUID(), equipmentInventory);
        }

        player.openInventory(equipmentInventory);
        registerInventoryCloseListener(player, npc, equipmentInventory);
    }

    private void registerInventoryCloseListener(Player player, NPC npc, Inventory equipmentInventory) {
        Listener closeListener = new Listener() {
            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (!event.getPlayer().equals(player)) {
                    return;
                }
                if (!event.getInventory().equals(equipmentInventory)) {
                    return;
                }
                HandlerList.unregisterAll(this);

                RelluEssentials.getInstance().getNpcService().saveNPCInventory(npc, event.getInventory());

                if (npc.getEntityUUID() != null) {
                    NPCEquipmentInventoryHelper.applyInventoryEquipmentToEntity(event.getInventory(), npc.getEntityUUID());
                }
            }
        };
        Bukkit.getPluginManager().registerEvents(closeListener, RelluEssentials.getInstance());
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length >= 2
                && Admin.Commands.NPC.getName().equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX])
                && "equip".equalsIgnoreCase(args[ARGS_ACTION_INDEX]);
    }
}