package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.File;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author rellu
 */
@Getter
public class ConfigHelper {

    private final File file;
    private YamlConfiguration config;
    private boolean configFound = true;

    public ConfigHelper(String name) {

        file = new File(RelluEssentials.getInstance().getDataFolder(), name + ".yml");
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            configFound = false;
        }
    }

    /**
     *
     * @return Returns List of all Players from config file
     */
    public List<PlayerEntry> getPlayers() {
        List<PlayerEntry> list = new ArrayList<>();
        ConfigurationSection cs = config.getConfigurationSection("player");

        for (String uuid : Objects.requireNonNull(cs).getKeys(false)) {
            ConfigurationSection player = cs.getConfigurationSection(uuid);

            String groupName = Objects.requireNonNull(Objects.requireNonNull(player).getString("group")).toLowerCase();
            int groupFK = Groups.getGroup(groupName).getId();
            boolean fly = player.getBoolean("fly");
            boolean afk = player.getBoolean("afk");
            String customname = player.getString("customname");

            consoleSendMessage(PLUGIN_NAME_CONSOLE, "Found Player: " + uuid + " customname:" + customname + " afk:" + afk + " fly:" + fly + " group id:" + groupFK + " group:" + groupName);

            PlayerEntry p = new PlayerEntry();
            p.setGroup(Groups.getGroup(groupName));
            p.setAfk(afk);
            p.setFlying(fly);
            p.setCreatedBy(1);
            p.setCustomName(customname);
            p.setUuid(uuid);

            list.add(p);
        }
        return list;
    }

    /**
     *
     * @param p Player
     * @return List of Homes as LocationEntry
     */
    public List<LocationEntry> getHomes(PlayerEntry p) {
        List<LocationEntry> list = new ArrayList<>();
        ConfigurationSection homes = config.getConfigurationSection("player." + p.getUuid() + ".home");
        for (String home : Objects.requireNonNull(homes).getKeys(false)) {
            ConfigurationSection h = homes.getConfigurationSection(home);
            if(h == null){
                continue;
            }

            float x;
            float y;
            float z;
            float yaw;
            float pitch;
            x = (float) h.getDouble("x");
            y = (float) h.getDouble("y");
            z = (float) h.getDouble("z");
            yaw = (float) h.getDouble("yaw");
            pitch = (float) h.getDouble("pitch");
            int type = home.equals("death") ? 2 : 1;
            String worldName = h.getString("world");

            World world = Bukkit.getServer().getWorld(Objects.requireNonNull(worldName));

            consoleSendMessage(PLUGIN_NAME_CONSOLE, "Found Home: " + home + " x:" + x + " y:" + y + " z:" + z + " yaw:" + yaw + " pitch:" + pitch + " world:" + world);

            LocationEntry l = new LocationEntry();
            l.setLocation(new Location(world, x, y, z, yaw, pitch));
            l.setLocationName(home);
            l.setPlayerId(p.getId());
            LocationTypeEntry lt = new LocationTypeEntry();
            lt.setId(type);
            l.setLocationType(lt);

            list.add(l);
        }
        return list;
    }
}