package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * WILL BE REMOVED.
 *
 * @author rellu
 */
@Deprecated
public class ChatHelper {

  @Deprecated
  private ChatHelper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * WILL BE REMOVED.
   *
   * @param type    Prefix to add before message
   * @param message Message to send
   */
  @Deprecated
  public static void consoleSendMessage(String type, String message) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    console.sendMessage(type + " " + message);
  }

  /**
   * WILL BE REMOVED.
   *
   * @param type    Prefix to add before message
   * @param message Message to send
   * @param repeat  how often should the message be sent
   */
  @Deprecated
  public static void consoleSendMessage(String type, String message, int repeat) {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    for (int i = 0; i <= repeat; i++) {
      console.sendMessage(type + " " + message);
    }
  }
}
