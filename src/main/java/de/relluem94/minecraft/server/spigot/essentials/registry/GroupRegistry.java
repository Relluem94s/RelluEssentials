package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GroupRegistry {

  private static List<GroupEntry> injectedGroupEntries = null;

  GroupRegistry() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static void injectGroupEntries(List<GroupEntry> groupEntries) {
    injectedGroupEntries = groupEntries;
  }

  static List<GroupEntry> resolveGroupEntries() {
    if (injectedGroupEntries != null) {
      return injectedGroupEntries;
    }
    return RelluEssentials.getInstance().getGroupEntryList();
  }

  public static @NotNull GroupEntry getGroup(String name) {
    return resolveGroupEntries().stream()
        .filter(ge -> ge.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseGet(() -> resolveGroupEntries().stream()
            .filter(ge -> ge.getName().equalsIgnoreCase("user"))
            .findFirst()
            .orElse(new GroupEntry(1, "user", "§8")));
  }

  public static boolean groupExists(String name) {
    return resolveGroupEntries().stream()
        .anyMatch(ge -> ge.getName().equalsIgnoreCase(name));
  }

  public static @Nullable GroupEntry getGroup(int id) {
    return resolveGroupEntries().stream()
        .filter(ge -> ge.getId() == id)
        .findFirst()
        .orElse(null);
  }
}