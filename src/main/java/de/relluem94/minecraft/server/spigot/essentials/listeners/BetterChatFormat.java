package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.rellulib.utils.StringUtils;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Listener that intercepts player chat events and formats messages based on group membership.
 *
 * <p>Supports VIP, moderator, and admin channels via channel prefixes, and applies symbol
 * replacement for authorized players. Unauthorized players receive plain message formatting.</p>
 */
@ListenerName("BetterChatFormat")
public class BetterChatFormat implements ListenerConstruct {

  public static final String VIP_CHANNEL = "#v ";
  public static final String MOD_CHANNEL = "#m ";
  public static final String ADMIN_CHANNEL = "#a ";
  private ServiceContext serviceContext;

  /**
   * Handles incoming player chat events by cancelling the default chat and routing the message to
   * the appropriate channel or broadcasting it to all players.
   *
   * <p>VIP players may use symbol replacements and send messages to restricted channels
   * using the prefixes {@value #VIP_CHANNEL}, {@value #MOD_CHANNEL}, or {@value #ADMIN_CHANNEL}.
   * Players without VIP authorization receive plain message broadcasting without symbol
   * replacement.</p>
   *
   * @param e the asynchronous player chat event containing the sender and message
   */
  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {
    e.setCancelled(true);
    Player p = e.getPlayer();
    if (serviceContext.getGroupService().isSenderAuthorized(p, "vip")) {
      e.setMessage(StringUtils.replaceSymbols(e.getMessage()));

      Optional<GroupEntry> vip = serviceContext.getGroupService().findGroupByName("vip");
      Optional<GroupEntry> mod = serviceContext.getGroupService().findGroupByName("mod");
      Optional<GroupEntry> admin = serviceContext.getGroupService().findGroupByName("admin");
      if (e.getMessage().startsWith(VIP_CHANNEL) && vip.isPresent()
          && serviceContext.getGroupService().isSenderAuthorized(p, "vip")) {
        serviceContext.getChatService()
            .sendMessageInChannel(e.getMessage(), p, VIP_CHANNEL, vip.get());
      } else if (e.getMessage().startsWith(MOD_CHANNEL) && mod.isPresent()
          && serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
        serviceContext.getChatService()
            .sendMessageInChannel(e.getMessage(), p, MOD_CHANNEL, mod.get());
      } else if (e.getMessage().startsWith(ADMIN_CHANNEL) && admin.isPresent()
          && serviceContext.getGroupService().isSenderAuthorized(p, "admin")) {
        serviceContext.getChatService()
            .sendMessageInChannel(e.getMessage(), p, ADMIN_CHANNEL, admin.get());
      } else {
        serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
            p.getCustomName() + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + replaceColor(
                e.getMessage()));
      }
    } else {
      serviceContext.getPluginMetadataService().getPlugin().getServer().broadcastMessage(
          p.getCustomName() + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + e.getMessage());
    }
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }
}