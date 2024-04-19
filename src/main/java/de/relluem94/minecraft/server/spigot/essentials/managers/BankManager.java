package de.relluem94.minecraft.server.spigot.essentials.managers;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;

public class BankManager implements IEnable {

    @Override
    public void enable() {
        if(RelluEssentials.getInstance().isUnitTest()){
            return;
        }
        triggerNext();   
    }

    private void triggerNext(){
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
            BankerHelper.doInterest();
            ChatHelper.consoleSendMessage(Constants.PLUGIN_NAME_CONSOLE, String.format(Constants.PLUGIN_BANK_INTEREST_NEXT_RUN, getSecondsUntilMidnight()));
            triggerNext();
        }, 20 *  getSecondsUntilMidnight());
    }

    private long getSecondsUntilMidnight(){
        ZonedDateTime nowZoned = ZonedDateTime.now();
        Instant midnight = nowZoned.plusDays(1).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Duration duration = Duration.between(midnight, Instant.now());

        return Math.abs(duration.getSeconds());
    }
}