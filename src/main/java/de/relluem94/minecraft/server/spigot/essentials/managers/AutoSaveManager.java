package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import java.util.Optional;
import org.bukkit.plugin.Plugin;

public class AutoSaveManager implements Enable, Disable {

  public static final long AUTO_SAVE_MINUTES = 2;
  private final int MAX_RETRIES = 4;
  private int count = 0;
  private ServiceContext context;

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    context = relluEssentialsPlugin.getServiceContext();

    Optional<GroupEntry> adminGroup = context.getGroupService()
        .findGroupByName("admin");

    if (!adminGroup.isPresent() && count <= MAX_RETRIES) {
      count++;
      context.getSchedulerService().runTaskLater(() -> {
        enable(plugin);
      }, 100);
    }

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        context.getTranslationService().get(MessageKey.PLUGIN_MANAGER_REGISTER_AUTOSAVE));

    context.getSchedulerService().runTaskTimer(() -> adminGroup.ifPresent(context.getBagService()::savePendingBagUpdates),
        0L, 20 * 60 * AUTO_SAVE_MINUTES);

    context.getSchedulerService().runTaskTimer(
        () -> adminGroup.ifPresent(context.getPlayerService()::savePlayers),
        0L, 20 * 60 * AUTO_SAVE_MINUTES);

    context.getSchedulerService().runTaskTimer(
        () -> adminGroup.ifPresent(context.getPlayerService()::savePlayersInv),
        0L, 20 * 60 * AUTO_SAVE_MINUTES);

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        context.getTranslationService().get(MessageKey.PLUGIN_MANAGER_AUTOSAVE_REGISTERED));
  }

  @Override
  public void disable(Plugin plugin) {
    Optional<GroupEntry> adminGroup = context.getGroupService().findGroupByName("admin");

    if (adminGroup.isEmpty()) {
      return;
    }

    context.getBagService().savePendingBagUpdates(adminGroup.get());
    context.getPlayerService().savePlayers(adminGroup.get());
    context.getPlayerService().savePlayersInv(adminGroup.get());
  }
}