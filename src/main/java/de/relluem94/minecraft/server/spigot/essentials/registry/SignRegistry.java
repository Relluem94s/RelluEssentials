package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.SignAction;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class SignRegistry {

  private static final Map<String, SignAction> registeredActions = new LinkedHashMap<>();

  private SignRegistry() {
  }

  public static void register(@NonNull Plugin plugin, @NonNull String actionId,
      @NonNull SignAction action) {
    RegistryKey key = RegistryKey.of(plugin, actionId);
    if (registeredActions.containsKey(key.toString())) {
      throw new IllegalArgumentException(
          "SignAction already registered for key: " + key);
    }
    registeredActions.put(key.toString(), action);
  }

  public static Optional<SignAction> find(@NonNull RegistryKey key) {
    return Optional.ofNullable(registeredActions.get(key.toString()));
  }

  public static Optional<SignAction> findByLine(@NonNull String signLine) {
    return registeredActions.values().stream()
        .filter(action -> action.getShorthandBracket().equalsIgnoreCase(signLine)
            || action.getNameBracket().equalsIgnoreCase(signLine)
            || action.getDisplayName().equalsIgnoreCase(signLine))
        .findFirst();
  }

  public static Optional<Map.Entry<RegistryKey, SignAction>> findEntryByLine(
      @NonNull String signLine) {
    String normalizedLine = signLine.replace("[", "").replace("]", "").trim();
    return registeredActions.entrySet().stream()
        .filter(entry -> {
          SignAction action = entry.getValue();
          return action.getShorthandBracket().equalsIgnoreCase(normalizedLine)
              || action.getNameBracket().equalsIgnoreCase(normalizedLine)
              || action.getDisplayName().equalsIgnoreCase(normalizedLine);
        })
        .map(entry -> Map.entry(RegistryKey.of(entry.getKey()), entry.getValue()))
        .findFirst();
  }

  public static Collection<SignAction> getAll() {
    return Collections.unmodifiableCollection(registeredActions.values());
  }

  public static Collection<SignAction> getAllByNamespace(@NonNull String namespace) {
    return registeredActions.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(namespace.toLowerCase() + ":"))
        .map(Map.Entry::getValue)
        .toList();
  }
}