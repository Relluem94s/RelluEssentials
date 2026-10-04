package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.bukkit.plugin.Plugin;

/**
 * Manages the scheduled execution of bank interest payouts for all online players. Calculates the
 * delay until midnight and triggers interest distribution once per day.
 */
public class BankManager implements Enable {

  private ServiceContext serviceContext;

  /**
   * Initializes the manager by retrieving the service context and scheduling the first interest
   * trigger.
   *
   * @param plugin the plugin instance used to access the service context
   */
  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    serviceContext = relluEssentialsPlugin.getServiceContext();
    triggerNext();
  }

  private void triggerNext() {
    serviceContext.getSchedulerService().runTaskLater(() -> {
      serviceContext.getBankService().triggerInterestForAllOnlinePlayers();
      serviceContext.getPluginMetadataService().getPlugin().getServer().getConsoleSender()
          .sendMessage(Constants.PLUGIN_NAME_CONSOLE, serviceContext.getTranslationService()
              .get(MessageKey.PLUGIN_BANK_INTEREST_NEXT_RUN,
                  String.valueOf(getSecondsUntilMidnight())));
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