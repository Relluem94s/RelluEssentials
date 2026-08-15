package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import java.util.List;
import java.util.Optional;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Service for managing group-related business logic,
 * including registration, lookup, and authorization checks.
 *
 * @author rellu
 */
public class GroupService {

  private final GroupRegistry groupRegistry;
  private final GroupRepository groupRepository;
  @Setter
  private PlayerRegistry playerRegistry;

  /**
   * Creates a new {@code GroupService} with the given registry and repository.
   *
   * @param groupRegistry    the registry for in-memory group management
   * @param groupRepository  the repository for group persistence
   */
  public GroupService(GroupRegistry groupRegistry, GroupRepository groupRepository) {
    this.groupRegistry = groupRegistry;
    this.groupRepository = groupRepository;
  }

  /**
   * Adds a new group if no group with the same name already exists.
   *
   * @param groupEntry the group entry to add
   * @return {@code true} if the group was added, {@code false} if it already existed
   */
  public boolean addGroup(@NotNull GroupEntry groupEntry) {
    if (groupRegistry.containsByName(groupEntry.getName())) {
      return false;
    }
    groupRepository.save(groupEntry);
    groupRegistry.register(groupEntry);
    return true;
  }

  /**
   * Resolves a group by name, falling back to "user" or a default entry if not found.
   *
   * @param name the name of the group to resolve
   * @return the resolved {@link GroupEntry}
   */
  public GroupEntry resolveGroupWithFallback(String name) {
    return groupRegistry.findByName(name)
        .or(() -> groupRegistry.findByName("user"))
        .orElse(new GroupEntry(1, "user", "§8"));
  }

  /**
   * Resolves a group by ID, falling back to "user" or a default entry if not found.
   *
   * @param id the ID of the group to resolve
   * @return the resolved {@link GroupEntry}
   */
  public GroupEntry resolveGroupWithFallback(int id) {
    return groupRegistry.findById(id)
        .or(() -> groupRegistry.findByName("user"))
        .orElse(new GroupEntry(1, "user", "§8"));
  }

  /**
   * Finds a group by its unique ID.
   *
   * @param id the ID to search for
   * @return an {@link Optional} containing the group, or empty if not found
   */
  public Optional<GroupEntry> findGroupById(int id) {
    return groupRegistry.findById(id);
  }

  /**
   * Finds a group by its name.
   *
   * @param name the name to search for
   * @return an {@link Optional} containing the group, or empty if not found
   */
  public Optional<GroupEntry> findGroupByName(String name) {
    return groupRegistry.findByName(name);
  }

  /**
   * Returns all registered groups.
   *
   * @return a list of all {@link GroupEntry} instances
   */
  public List<GroupEntry> findAllGroups() {
    return groupRegistry.getAll();
  }

  /**
   * Resolves a group by name if the given player has at least the "mod" rank.
   *
   * @param player    the player whose rank is checked
   * @param groupName the name of the group to resolve
   * @return an {@link Optional} containing the group, or empty if unauthorized or not found
   */
  public Optional<GroupEntry> resolveAuthorizedGroup(Player player, String groupName) {
    Optional<GroupEntry> groupEntry = groupRegistry.findByName(groupName);
    if (groupEntry.isEmpty()) {
      return Optional.empty();
    }
    if (!isPlayerInGroupOrHigher(player, "mod")) {
      return Optional.empty();
    }
    return groupEntry;
  }

  /**
   * Checks whether the given player belongs to the specified group or a higher-ranked one.
   *
   * @param player    the player to check
   * @param groupName the minimum required group name
   * @return {@code true} if the player meets the requirement, {@code false} otherwise
   */
  public boolean isPlayerInGroupOrHigher(Player player, String groupName) {
    if (playerRegistry == null) {
      return false;
    }
    return groupRegistry.findByName(groupName)
        .map(requiredGroup -> {
          long playerGroupId = playerRegistry.getPlayerEntry(player).getGroup().getId();
          return playerGroupId >= requiredGroup.getId();
        })
        .orElse(false);
  }

  /**
   * Checks whether the given {@link CommandSender} is authorized based on the required group.
   * Console and command blocks are always authorized.
   *
   * @param sender            the sender to check
   * @param requiredGroupName the minimum required group name for players
   * @return {@code true} if authorized, {@code false} otherwise
   */
  public boolean isSenderAuthorized(CommandSender sender, String requiredGroupName) {
    if (TypeHelper.isConsole(sender) || TypeHelper.isCMDBlock(sender)) {
      return true;
    }
    if (TypeHelper.isPlayer(sender)) {
      return isPlayerInGroupOrHigher((Player) sender, requiredGroupName);
    }
    return false;
  }
}