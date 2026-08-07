package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author rellu
 */
public class ChatHelper {

  private ChatHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   *
   * @param type    Prefix to add before message
   * @param message Message to send
   */
  public static void consoleSendMessage(String type, String message) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    console.sendMessage(type + " " + message);
  }

  /**
   *
   * @param type    Prefix to add before message
   * @param message Message to send
   * @param repeat  how often should the message be sent
   */
  public static void consoleSendMessage(String type, String message, int repeat) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    for (int i = 0; i <= repeat; i++) {
      console.sendMessage(type + " " + message);
    }
  }



}
