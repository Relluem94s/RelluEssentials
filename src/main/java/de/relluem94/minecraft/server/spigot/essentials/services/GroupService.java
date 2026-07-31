package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.Generated;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GroupService {

  private static GroupRepository injectedGroupRepository = null;

  public GroupService() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static void injectGroupRepository(GroupRepository repository) {
    injectedGroupRepository = repository;
  }

  @Generated
  private static GroupRepository resolveGroupRepository() {
    if (injectedGroupRepository != null) {
      return injectedGroupRepository;
    }

    @Generated
    class DefaultGroupRepository implements GroupRepository {

      private final RelluEssentials instance = RelluEssentials.getInstance();

      @Override
      public void insertGroup(GroupEntry groupEntry) {
        instance.getDatabaseHelper().insertGroup(groupEntry);
      }

      @Override
      public List<GroupEntry> getGroups() {
        return instance.getDatabaseHelper().getGroups();
      }

      @Override
      public void addAllGroups(List<GroupEntry> groups) {
        instance.getGroupEntryList().addAll(groups);
      }
    }

    return new DefaultGroupRepository();
  }

  @SuppressWarnings("unused")
  public static boolean addGroup(@NotNull GroupEntry groupEntry) {
    if (!GroupRegistry.groupExists(groupEntry.getName())) {
      GroupRepository repository = resolveGroupRepository();
      repository.insertGroup(groupEntry);
      repository.addAllGroups(repository.getGroups());
      return true;
    }
    return false;
  }
}