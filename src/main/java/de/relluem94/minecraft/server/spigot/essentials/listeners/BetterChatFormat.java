package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_SPACER_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessageInChannel;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;

import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.rellulib.utils.StringUtils;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BetterChatFormat implements ListenerConstruct {

  public static final String VIP_CHANNEL = "#v ";
  public static final String MOD_CHANNEL = "#m ";
  public static final String ADMIN_CHANNEL = "#a ";
  private GroupService groupService;
  private GroupRegistry groupRegistry;

  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {
    e.setCancelled(true);
    Player p = e.getPlayer();
    if (groupService.isSenderAuthorized(p, "vip")) {
      e.setMessage(StringUtils.replaceSymbols(e.getMessage()));

      Optional<GroupEntry> vip = groupRegistry.findByName("vip");
      Optional<GroupEntry> mod = groupRegistry.findByName("vip");
      Optional<GroupEntry> admin = groupRegistry.findByName("vip");
      if (e.getMessage().startsWith(VIP_CHANNEL) && vip.isPresent()
          && groupService.isSenderAuthorized(p, "vip")) {
        sendMessageInChannel(e.getMessage(), p, VIP_CHANNEL, vip.get());
      } else if (e.getMessage().startsWith(MOD_CHANNEL) && mod.isPresent()
          && groupService.isSenderAuthorized(p, "mod")) {
        sendMessageInChannel(e.getMessage(), p, MOD_CHANNEL, mod.get());
      } else if (e.getMessage().startsWith(ADMIN_CHANNEL) && admin.isPresent()
          && groupService.isSenderAuthorized(p, "admin")) {
        sendMessageInChannel(e.getMessage(), p, ADMIN_CHANNEL, admin.get());
      } else {
        Bukkit.broadcastMessage(
            p.getCustomName() + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + replaceColor(
                e.getMessage()));
      }
    } else {
      Bukkit.broadcastMessage(
          p.getCustomName() + PLUGIN_FORMS_SPACER_MESSAGE + PLUGIN_COLOR_MESSAGE + e.getMessage());
    }
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.groupRegistry = context.getGroupRegistry();
  }
}