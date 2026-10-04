package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import java.util.Optional;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Manages the automatic saving of player data, inventories and bag updates at a fixed interval. Retries initialization
 * until the admin group is available or the retry limit is reached.
 *
 * @author rellu
 */
public class AutoSaveManager implements Enable, Disable {

  public static final long AUTO_SAVE_MINUTES = 2;
  public static final int MAX_RETRIES = 4;
  private int count = 0;
  private ServiceContext context;

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    context = relluEssentialsPlugin.getServiceContext();

    Optional<GroupEntry> adminGroup = context
        .getGroupService()
        .findGroupByName("admin");

    if (adminGroup.isEmpty() && count <= MAX_RETRIES) {
      count++;
      context
          .getSchedulerService()
          .runTaskLater(() -> enable(plugin), 100);
      return;
    }

    ConsoleCommandSender consoleCommandSender = context
        .getServerService()
        .getConsoleSender();
    consoleCommandSender.sendMessage(PLUGIN_NAME_CONSOLE + context
        .getTranslationService()
        .get(MessageKey.PLUGIN_MANAGER_REGISTER_AUTOSAVE));

    context
        .getSchedulerService()
        .runTaskTimer(() -> adminGroup.ifPresent(context.getBagService()::savePendingBagUpdates), 0L,
            20 * 60 * AUTO_SAVE_MINUTES);

    context
        .getSchedulerService()
        .runTaskTimer(() -> adminGroup.ifPresent(context.getPlayerService()::savePlayers), 0L,
            20 * 60 * AUTO_SAVE_MINUTES);

    context
        .getSchedulerService()
        .runTaskTimer(() -> adminGroup.ifPresent(context.getPlayerService()::savePlayersInv), 0L,
            20 * 60 * AUTO_SAVE_MINUTES);

    consoleCommandSender.sendMessage(PLUGIN_NAME_CONSOLE + context
        .getTranslationService()
        .get(MessageKey.PLUGIN_MANAGER_AUTOSAVE_REGISTERED));
  }

  @Override
  public void disable(Plugin plugin) {
    Optional<GroupEntry> adminGroup = context
        .getGroupService()
        .findGroupByName("admin");

    if (adminGroup.isEmpty()) {
      return;
    }

    context
        .getBagService()
        .savePendingBagUpdates(adminGroup.get());
    context
        .getPlayerService()
        .savePlayers(adminGroup.get());
    context
        .getPlayerService()
        .savePlayersInv(adminGroup.get());
  }
}