package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.translationService;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import java.util.Optional;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveManager implements Enable, Disable {

  public static final long AUTO_SAVE_MINUTES = 2;
  private final int MAX_RETRIES = 4;
  private int count = 0;
  @Override
  public void enable(Plugin plugin) {
    Optional<GroupEntry> adminGroup = ((RelluEssentials)plugin).getGroupRegistry().findByName("admin");

    if(!adminGroup.isPresent() && count <= MAX_RETRIES){
      count++;
      new BukkitRunnable() {
        @Override
        public void run(){
          enable(plugin);
        }
      }.runTaskLater(plugin, 100);
    }

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_AUTOSAVE));
    new BukkitRunnable() {
      @Override
      public void run() {

        BagHelper.saveBags(adminGroup.get());
      }
    }.runTaskTimer(plugin, 0L, 20 * 60 * AUTO_SAVE_MINUTES);

    new BukkitRunnable() {
      @Override
      public void run() {
        PlayerHelper.savePlayers(adminGroup.get());
      }
    }.runTaskTimer(plugin, 0L, 20 * 60 * AUTO_SAVE_MINUTES);

    new BukkitRunnable() {
      @Override
      public void run() {
        PlayerHelper.savePlayersInv(adminGroup.get());
      }
    }.runTaskTimer(plugin, 0L, 20 * 60 * AUTO_SAVE_MINUTES);

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_AUTOSAVE_REGISTERED));
  }

  @Override
  public void disable(Plugin plugin) {
    Optional<GroupEntry> adminGroup = ((RelluEssentials)plugin).getGroupRegistry().findByName("admin");

    if(!adminGroup.isPresent()){
      return;
    }
    BagHelper.saveBags(adminGroup.get());
    PlayerHelper.savePlayers(adminGroup.get());
    PlayerHelper.savePlayersInv(adminGroup.get());
  }
}