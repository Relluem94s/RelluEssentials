package de.relluem94.minecraft.server.spigot.essentials.commands.admin.shared;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class AdminCommandHelper {

    public static void cleanUpLocations(@NonNull Player p) {
        int deleted = RelluEssentials.getInstance().getDatabaseHelper().cleanupLocations();
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END, deleted));
    }

    public static void cleanUpProtections(@NonNull Player p) {
        HashMap<Location, ProtectionEntry> protectionEntryList = new HashMap<>(
                RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList()
        );

        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_START,
                protectionEntryList.size()));

        List<Location> locations = new ArrayList<>(protectionEntryList.keySet());
        int[] index = {0};
        HashMap<Location, ProtectionEntry> removeMap = new HashMap<>();
        int total = locations.size();

        Bukkit.getScheduler().runTaskTimer(
                RelluEssentials.getInstance(),
                task -> {
                    int batchSize = 5;
                    int processed = 0;

                    while (index[0] < locations.size() && processed < batchSize) {
                        Location l = locations.get(index[0]);
                        ProtectionEntry pe = protectionEntryList.get(l);

                        if (!l.getChunk().isLoaded()) {
                            l.getChunk().load();
                        }

                        if (!l.getBlock().getType().equals(Material.getMaterial(pe.getMaterialName()))) {
                            removeMap.put(l, pe);
                            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS,
                                    pe.getId(), pe.getMaterialName(), l.getBlock().getType().name()));
                            RelluEssentials.getInstance().getDatabaseHelper().deleteProtection(pe);
                        }

                        index[0]++;
                        processed++;
                    }

                    int percent = (int) Math.round((index[0] / (double) total) * 100);
                    p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_PERCENTAGE,
                            index[0], total, percent));

                    if (index[0] >= locations.size()) {
                        task.cancel();

                        if (removeMap.isEmpty()) {
                            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_NONE));
                        } else {
                            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_CLEANING_UP,
                                    removeMap.size()));
                            for (Location l : removeMap.keySet()) {
                                RelluEssentials.getInstance().getProtectionAPI().removeProtectionEntry(l);
                            }
                            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_PROTECTIONS_END,
                                    RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList().size()));
                        }
                    }
                },
                0L,
                300L
        );

        int deleted = RelluEssentials.getInstance().getDatabaseHelper().cleanupProtections();
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_PROTECTIONS_END, deleted));
    }
}
