package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupRepository {

  private final List<GroupEntry> groupEntries;

  public GroupRepository(List<GroupEntry> groupEntries) {
    this.groupEntries = new ArrayList<>(groupEntries);
  }

  public List<GroupEntry> findAll() {
    return List.copyOf(groupEntries);
  }

  public Optional<GroupEntry> findByName(String name) {
    return groupEntries.stream()
        .filter(entry -> entry.getName().equalsIgnoreCase(name))
        .findFirst();
  }

  public Optional<GroupEntry> findById(int id) {
    return groupEntries.stream()
        .filter(entry -> entry.getId() == id)
        .findFirst();
  }

  public GroupEntry save(GroupEntry groupEntry) {
    groupEntries.add(groupEntry);
    return groupEntry;
  }

  public void delete(GroupEntry groupEntry) {
    groupEntries.remove(groupEntry);
  }
}