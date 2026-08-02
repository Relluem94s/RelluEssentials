package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;


public class BankManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    if (relluEssentialsPlugin.isUnitTest()) {
      return;
    }
    triggerNext(relluEssentialsPlugin);
  }

  private void triggerNext(RelluEssentials plugin) {
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      BankerHelper.doInterest();
      ChatHelper.consoleSendMessage(
          Constants.PLUGIN_NAME_CONSOLE,
          languageHelper.get(MessageKey.PLUGIN_BANK_INTEREST_NEXT_RUN,
              String.valueOf(getSecondsUntilMidnight()))
      );
      triggerNext(plugin);
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