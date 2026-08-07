package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import java.util.Optional;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GroupService {

  private final GroupRegistry groupRegistry;
  private final GroupRepository groupRepository;
  @Setter
  private PlayerRegistry playerRegistry;


  public GroupService(GroupRegistry groupRegistry, GroupRepository groupRepository) {
    this.groupRegistry = groupRegistry;
    this.groupRepository = groupRepository;
  }


  public boolean addGroup(@NotNull GroupEntry groupEntry) {
    if (groupRegistry.containsByName(groupEntry.getName())) {
      return false;
    }
    groupRepository.save(groupEntry);
    groupRegistry.register(groupEntry);
    return true;
  }

  public GroupEntry resolveGroupWithFallback(String name) {
    return groupRegistry.findByName(name)
        .or(() -> groupRegistry.findByName("user"))
        .orElse(new GroupEntry(1, "user", "§8"));
  }

  public GroupEntry resolveGroupWithFallback(int id) {
    return groupRegistry.findById(id)
        .or(() -> groupRegistry.findByName("user"))
        .orElse(new GroupEntry(1, "user", "§8"));
  }

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