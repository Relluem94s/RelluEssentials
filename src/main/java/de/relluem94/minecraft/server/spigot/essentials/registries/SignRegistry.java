package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.SignAction;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

/**
 * A registry that manages the mapping between unique keys and {@link SignAction} implementations.
 * This class allows for registering, finding, and filtering sign-based actions using either their
 * registry keys or the text content found on signs.
 */
public class SignRegistry {

  private static final Map<String, SignAction> registeredActions = new LinkedHashMap<>();

  private SignRegistry() {
  }

  /**
   * Registers a new sign action associated with a specific plugin and action ID.
   *
   * @param plugin   The plugin responsible for this action.
   * @param actionId The unique identifier for the action within the plugin's namespace.
   * @param action   The sign action implementation to register.
   * @throws IllegalArgumentException if an action with the same RegistryKey is already registered.
   */
  public static void register(@NonNull Plugin plugin, @NonNull String actionId,
      @NonNull SignAction action) {
    RegistryKey key = RegistryKey.of(plugin, actionId);
    if (registeredActions.containsKey(key.toString())) {
      throw new IllegalArgumentException(
          "SignAction already registered for key: " + key);
    }
    registeredActions.put(key.toString(), action);
  }

  /**
   * Finds a registered sign action by its unique registry key.
   *
   * @param key The registry key to search for.
   * @return An Optional containing the found SignAction, or empty if not found.
   */
  public static Optional<SignAction> find(@NonNull RegistryKey key) {
    return Optional.ofNullable(registeredActions.get(key.toString()));
  }

  /**
   * Finds a registered sign action by matching a provided sign line against shorthand brackets,
   * name brackets, or display names.
   *
   * @param signLine The string content of the sign line to match.
   * @return An Optional containing the matching SignAction, or empty if no match is found.
   */
  public static Optional<SignAction> findByLine(@NonNull String signLine) {
    return registeredActions.values().stream()
        .filter(action -> action.getShorthandBracket().equalsIgnoreCase(signLine)
            || action.getNameBracket().equalsIgnoreCase(signLine)
            || action.getDisplayName().equalsIgnoreCase(signLine))
        .findFirst();
  }

  /**
   * Finds a registered sign action entry by matching a normalized sign line. The line is normalized
   * by removing brackets and trimming whitespace.
   *
   * @param signLine The string content of the sign line to match.
   * @return An Optional containing a Map Entry of the RegistryKey and SignAction, or empty if no
   *     match is found.
   */
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
        .map(entry -> Map.entry(RegistryKey.fromString(entry.getKey()), entry.getValue()))
        .findFirst();
  }

  /**
   * Retrieves all currently registered sign actions.
   *
   * @return An unmodifiable collection of all registered SignActions.
   */
  public static Collection<SignAction> getAll() {
    return Collections.unmodifiableCollection(registeredActions.values());
  }

  /**
   * Retrieves all registered sign actions that belong to a specific namespace.
   *
   * @param namespace The namespace to filter by (e.g., "pluginname").
   * @return A collection of SignActions belonging to the specified namespace.
   */
  public static Collection<SignAction> getAllByNamespace(@NonNull String namespace) {
    return registeredActions.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(namespace.toLowerCase() + ":"))
        .map(Map.Entry::getValue)
        .toList();
  }
}