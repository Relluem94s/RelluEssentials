package de.relluem94.minecraft.server.spigot.essentials.services;

import java.util.Collection;
import java.util.UUID;
import org.bukkit.Keyed;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;

/**
 * Service over {@link Server} to avoid static {@code Bukkit.*} access throughout the codebase.
 */
public class ServerService {

  private final Server server;

  /**
   * Creates a new instance of {@link ServerService}.
   *
   * @param plugin the plugin whose server instance will be used
   */
  public ServerService(Plugin plugin) {
    this.server = plugin.getServer();
  }

  /**
   * Gets the console command sender.
   *
   * @return the console command sender
   */
  public ConsoleCommandSender getConsoleSender() {
    return server.getConsoleSender();
  }

  /**
   * Gets all currently online players.
   *
   * @return a collection of online players
   */
  public Collection<? extends Player> getOnlinePlayers() {
    return server.getOnlinePlayers();
  }

  /**
   * Gets the maximum number of players allowed on the server.
   *
   * @return the max player count
   */
  public int getMaxPlayers() {
    return server.getMaxPlayers();
  }

  /**
   * Sets the maximum number of players allowed on the server.
   *
   * @param maxPlayers the new max player count
   */
  public void setMaxPlayers(int maxPlayers) {
    server.setMaxPlayers(maxPlayers);
  }

  /**
   * Gets the server's message of the day.
   *
   * @return the MOTD
   */
  public String getMotd() {
    return server.getMotd();
  }

  /**
   * Sets the server's message of the day.
   *
   * @param motd the new MOTD
   */
  public void setMotd(String motd) {
    server.setMotd(motd);
  }

  /**
   * Gets the port the server is running on.
   *
   * @return the server port
   */
  public int getPort() {
    return server.getPort();
  }

  /**
   * Gets an online player by their UUID.
   *
   * @param uuid the UUID of the player
   * @return the player, or {@code null} if not online
   */
  public Player getPlayer(UUID uuid) {
    return server.getPlayer(uuid);
  }

  /**
   * Gets an online player by their name.
   *
   * @param name the name of the player
   * @return the player, or {@code null} if not online
   */
  public Player getPlayer(String name) {
    return server.getPlayer(name);
  }

  /**
   * Gets an offline player by their UUID.
   *
   * @param uuid the UUID of the player
   * @return the offline player
   */
  public OfflinePlayer getOfflinePlayer(UUID uuid) {
    return server.getOfflinePlayer(uuid);
  }

  /**
   * Gets an offline player by their name.
   *
   * @param name the name of the player
   * @return the offline player
   */
  @SuppressWarnings("deprecation")
  public OfflinePlayer getOfflinePlayer(String name) {
    return server.getOfflinePlayer(name);
  }

  /**
   * Gets all offline players that have ever played on this server.
   *
   * @return an array of offline players
   */
  public OfflinePlayer[] getOfflinePlayers() {
    return server.getOfflinePlayers();
  }

  /**
   * Gets the registry for the given type.
   *
   * @param <T>          the registry type
   * @param registryType the class of the registry type
   * @return the registry
   */
  public <T extends Keyed> Registry<T> getRegistry(Class<T> registryType) {
    return server.getRegistry(registryType);
  }

  /**
   * Broadcasts a message to all online players.
   *
   * @param message the message to broadcast
   * @return the number of players who received the message
   */
  public int broadcastMessage(String message) {
    return server.broadcastMessage(message);
  }

  /**
   * Creates block data for the given material string.
   *
   * @param data the block data string
   * @return the created block data
   */
  public BlockData createBlockData(String data) {
    return server.createBlockData(data);
  }

  /**
   * Creates an inventory with the given holder, size, and title.
   *
   * @param holder the inventory holder, or {@code null}
   * @param size   the inventory size (must be a multiple of 9)
   * @param title  the inventory title
   * @return the created inventory
   */
  public Inventory createInventory(InventoryHolder holder, int size, String title) {
    return server.createInventory(holder, size, title);
  }

  /**
   * Creates a player profile for the given UUID and name.
   *
   * @param uuid the UUID of the player
   * @param name the name of the player
   * @return the created player profile
   */
  public PlayerProfile createPlayerProfile(UUID uuid, String name) {
    return server.createPlayerProfile(uuid, name);
  }

  /**
   * Gets a world by its name.
   *
   * @param name the name of the world
   * @return the world, or {@code null} if not found
   */
  public World getWorld(String name) {
    return server.getWorld(name);
  }

  /**
   * Gets a world by its UUID.
   *
   * @param uuid the UUID of the world
   * @return the world, or {@code null} if not found
   */
  public World getWorld(UUID uuid) {
    return server.getWorld(uuid);
  }

  /**
   * Creates and loads a new world using the given creator.
   *
   * @param creator the world creator configuration
   * @return the created world, or {@code null} if creation failed
   */
  public World createWorld(WorldCreator creator) {
    return server.createWorld(creator);
  }

  /**
   * Unloads a world, optionally saving it before unloading.
   *
   * @param world the world to unload
   * @param save  whether to save the world before unloading
   * @return {@code true} if the world was successfully unloaded
   */
  public boolean unloadWorld(World world, boolean save) {
    return server.unloadWorld(world, save);
  }
}