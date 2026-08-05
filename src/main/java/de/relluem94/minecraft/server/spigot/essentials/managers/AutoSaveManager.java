package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Optional;
import org.bukkit.plugin.Plugin;

public class AutoSaveManager implements Enable, Disable {

  public static final long AUTO_SAVE_MINUTES = 2;
  private final int MAX_RETRIES = 4;
  private int count = 0;
  private TranslationService translationService;
  private SchedulerService schedulerService;

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    translationService = relluEssentialsPlugin.getTranslationService();
    schedulerService = relluEssentialsPlugin.getSchedulerService();
    Optional<GroupEntry> adminGroup = ((RelluEssentials) plugin).getGroupRegistry()
        .findByName("admin");

    if (!adminGroup.isPresent() && count <= MAX_RETRIES) {
      count++;
      schedulerService.runTaskLater(() -> {
        enable(plugin);
      }, 100);
    }

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_AUTOSAVE));

    schedulerService.runTaskTimer(() -> adminGroup.ifPresent(BagHelper::saveBags),
        0L, 20 * 60 * AUTO_SAVE_MINUTES);

    schedulerService.runTaskTimer(
        () -> adminGroup.ifPresent(PlayerHelper::savePlayers),
        0L, 20 * 60 * AUTO_SAVE_MINUTES);

    schedulerService.runTaskTimer(
        () -> adminGroup.ifPresent(PlayerHelper::savePlayersInv),
        0L, 20 * 60 * AUTO_SAVE_MINUTES);

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_AUTOSAVE_REGISTERED));
  }

  @Override
  public void disable(Plugin plugin) {
    Optional<GroupEntry> adminGroup = ((RelluEssentials) plugin).getGroupRegistry()
        .findByName("admin");

    if (!adminGroup.isPresent()) {
      return;
    }
    BagHelper.saveBags(adminGroup.get());
    PlayerHelper.savePlayers(adminGroup.get());
    PlayerHelper.savePlayersInv(adminGroup.get());
  }
}