package de.relluem94.minecraft.server.spigot.essentials.api;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionLockEntry;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtectionAPI {

    private final List<Material> protectionLocksList = new ArrayList<>();
    private final HashMap<Location, ProtectionEntry> protectionEntryMap = new HashMap<>();

    /**
     * Creates a new ProtectionAPI instance and populates the internal protection locks list
     * and protection entry map with the provided data.
     *
     * @param protectionLocksEntryList list of {@link ProtectionLockEntry} whose materials will be registered as protectable
     * @param protectionEntryMap       map of {@link Location} to {@link ProtectionEntry} representing existing protection entries
     */
    public ProtectionAPI(List<ProtectionLockEntry> protectionLocksEntryList, Map<Location, ProtectionEntry> protectionEntryMap){
        for(ProtectionLockEntry ple: protectionLocksEntryList){
            addProtectionMaterial(ple.getValue());
        }
        this.protectionEntryMap.putAll(protectionEntryMap);
    }

    /**
     * Returns the {@link ProtectionEntry} associated with the given {@link Location}.
     *
     * @param l the location to look up
     * @return the {@link ProtectionEntry} at the given location, or {@code null} if none exists
     */
    public ProtectionEntry getProtectionEntry(Location l){
        return protectionEntryMap.get(l);
    }

    /**
     * Removes the {@link ProtectionEntry} associated with the given {@link Location}.
     *
     * @param l the location whose protection entry should be removed
     */
    public void removeProtectionEntry(Location l){
        protectionEntryMap.remove(l);
    }

    /**
     * Associates the given {@link ProtectionEntry} with the specified {@link Location}.
     * If an entry already exists for that location, it will be overwritten.
     *
     * @param l  the location to protect
     * @param pe the {@link ProtectionEntry} to associate with the location
     */
    public void putProtectionEntry(Location l, ProtectionEntry pe){
        protectionEntryMap.put(l, pe);
    }

    /**
     * Registers a {@link Material} as protectable via the API.
     *
     * @param m the material to add to the protection list
     */
    public void addProtectionMaterial(Material m){
        protectionLocksList.add(m);
    }

    /**
     * Returns the list of all {@link Material}s that are registered as protectable.
     *
     * @return a {@link List} of protectable {@link Material}s
     */
    public List<Material> getMaterialProtectionList(){
        return protectionLocksList;
    }

    /**
     * Returns the map of all current protection entries.
     *
     * @return a {@link Map} mapping each protected {@link Location} to its corresponding {@link ProtectionEntry}
     */
    public Map<Location, ProtectionEntry> getProtectionEntryList(){
        return protectionEntryMap;
    }

}