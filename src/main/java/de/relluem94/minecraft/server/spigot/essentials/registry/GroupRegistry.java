package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.repository.GroupRepository;
import java.util.List;
import java.util.Optional;

public class GroupRegistry {

  private final GroupRepository groupRepository;

  public GroupRegistry(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  public void register(GroupEntry groupEntry) {
    if (contains(groupEntry)) {
      throw new IllegalArgumentException("GroupEntry is already registered: " + groupEntry);
    }
    groupRepository.save(groupEntry);
  }

  public void unregister(GroupEntry groupEntry) {
    if (!contains(groupEntry)) {
      throw new IllegalArgumentException("GroupEntry is not registered: " + groupEntry);
    }
    groupRepository.delete(groupEntry);
  }

  public boolean contains(GroupEntry groupEntry) {
    return groupRepository.findAll().contains(groupEntry);
  }

  public boolean containsByName(String name) {
    return groupRepository.findByName(name).isPresent();
  }

  public List<GroupEntry> getAll() {
    return groupRepository.findAll();
  }

  public Optional<GroupEntry> findById(int id) {
    return groupRepository.findById(id);
  }

  public Optional<GroupEntry> findByName(String name) {
    return groupRepository.findByName(name);
  }
}