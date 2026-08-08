package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.bukkit.plugin.Plugin;


public class BankManager implements Enable {

  private TranslationService translationService;
  private SchedulerService schedulerService;
  private BankService bankService;

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    translationService = serviceContext.getTranslationService();
    schedulerService = serviceContext.getSchedulerService();
    bankService = serviceContext.getBankService();

    if (relluEssentialsPlugin.isUnitTest()) {
      return;
    }
    triggerNext();
  }

  private void triggerNext() {
    schedulerService.runTaskLater(() -> {
      bankService.triggerInterestForAllOnlinePlayers();
      ChatHelper.consoleSendMessage(
          Constants.PLUGIN_NAME_CONSOLE,
          translationService.get(MessageKey.PLUGIN_BANK_INTEREST_NEXT_RUN,
              String.valueOf(getSecondsUntilMidnight()))
      );
      triggerNext();
    }, 20 * getSecondsUntilMidnight());
  }

  private long getSecondsUntilMidnight() {
    ZonedDateTime nowZoned = ZonedDateTime.now();
    Instant midnight = nowZoned.plusDays(1).toLocalDate().atStartOfDay(ZoneId.systemDefault())
        .toInstant();
    Duration duration = Duration.between(midnight, Instant.now());

    return Math.abs(duration.getSeconds());
  }
}