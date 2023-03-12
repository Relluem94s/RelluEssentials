package de.relluem94.minecraft.server.spigot.essentials.managers;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.bukkit.scheduler.BukkitRunnable;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;

public class BankManager implements IEnable {

    @Override
    public void enable() {
        triggerNext();   
    }

    private void triggerNext(){
        new BukkitRunnable() {
            @Override
            public void run() {
                BankerHelper.doInterest();
                ChatHelper.consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, String.format(Strings.PLUGIN_BANK_INTEREST_NEXT_RUN, getSecondsUntilMidnight()));
                triggerNext();
            }
        }.runTaskLater(RelluEssentials.getInstance(), 20 *  getSecondsUntilMidnight());
    }

    private long getSecondsUntilMidnight(){
        ZonedDateTime nowZoned = ZonedDateTime.now();
        Instant midnight = nowZoned.plusDays(1).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Duration duration = Duration.between(midnight, Instant.now());

        return Math.abs(duration.getSeconds());
    }
}