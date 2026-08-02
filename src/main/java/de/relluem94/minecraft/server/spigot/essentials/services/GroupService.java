package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repository.GroupRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GroupService {

  private final GroupRegistry groupRegistry;
  private final GroupRepository groupRepository;
  private final PlayerRegistry playerRegistry;


  public GroupService(GroupRegistry groupRegistry, GroupRepository groupRepository, PlayerRegistry playerRegistry) {
    this.groupRegistry = groupRegistry;
    this.groupRepository = groupRepository;
    this.playerRegistry = playerRegistry;
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

  public boolean isPlayerInGroupOrHigher(Player player, String groupName) {
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