package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.ChatService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import java.util.Optional;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetterChatFormatTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private GroupService groupService;

  @Mock
  private ChatService chatService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  @Mock
  private Player player;

  @Mock
  private AsyncPlayerChatEvent chatEvent;

  @Mock
  private GroupEntry vipGroupEntry;

  @Mock
  private GroupEntry modGroupEntry;

  @Mock
  private GroupEntry adminGroupEntry;

  private BetterChatFormat betterChatFormat;

  @BeforeEach
  void setUp() {
    betterChatFormat = new BetterChatFormat();
    betterChatFormat.injectContext(serviceContext);
  }

  @Test
  void onChatCancelsEventImmediately() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(chatEvent.getMessage()).thenReturn("hello");
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    verify(chatEvent).setCancelled(true);
  }

  @Test
  void onChatNonVipPlayerBroadcastsPlainMessage() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(chatEvent.getMessage()).thenReturn("hello world");
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(chatEvent).setCancelled(true),
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }

  @Test
  void onChatVipPlayerWithNoChannelPrefixBroadcastsToAll() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(chatEvent.getMessage()).thenReturn("regular message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }

  @Test
  void onChatAuthorizedVipPlayerWithVipPrefixSendsToVipChannel() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(serviceContext.getChatService()).thenReturn(chatService);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.VIP_CHANNEL + "vip message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.of(vipGroupEntry));
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(chatEvent.getPlayer()).thenReturn(player);

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(chatService).sendMessageInChannel(
            anyString(), eq(player), eq(BetterChatFormat.VIP_CHANNEL), eq(vipGroupEntry)),
        () -> verify(server, never()).broadcastMessage(anyString())
    );
  }

  @Test
  void onChatAuthorizedModPlayerWithModPrefixSendsToModChannel() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.MOD_CHANNEL + "mod message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.of(modGroupEntry));
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(true);
    when(serviceContext.getChatService()).thenReturn(chatService);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(chatEvent.getPlayer()).thenReturn(player);

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(chatService).sendMessageInChannel(
            anyString(), eq(player), eq(BetterChatFormat.MOD_CHANNEL), eq(modGroupEntry)),
        () -> verify(server, never()).broadcastMessage(anyString())
    );
  }

  @Test
  void onChatAuthorizedAdminPlayerWithAdminPrefixSendsToAdminChannel() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.ADMIN_CHANNEL + "admin message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroupEntry));
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(true);
    when(serviceContext.getChatService()).thenReturn(chatService);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(chatEvent.getPlayer()).thenReturn(player);

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(chatService).sendMessageInChannel(
            anyString(), eq(player), eq(BetterChatFormat.ADMIN_CHANNEL), eq(adminGroupEntry)),
        () -> verify(server, never()).broadcastMessage(anyString())
    );
  }

  @Test
  void onChatVipPlayerWithModPrefixButNotModAuthorizedBroadcastsToAll() {
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.MOD_CHANNEL + "mod message");
    when(chatEvent.getPlayer()).thenReturn(player);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(player.getCustomName()).thenReturn("TestPlayer");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.of(modGroupEntry));
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(groupService.isSenderAuthorized(player, "mod")).thenReturn(false);
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }

  @Test
  void onChatVipPlayerWithAdminPrefixButNotAdminAuthorizedBroadcastsToAll() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.ADMIN_CHANNEL + "admin message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.of(adminGroupEntry));
    when(groupService.isSenderAuthorized(player, "admin")).thenReturn(false);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }

  @Test
  void onChatVipPlayerWithVipPrefixButVipGroupAbsentBroadcastsToAll() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.VIP_CHANNEL + "vip message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }

  @Test
  void injectContextStoresServiceContext() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(false);
    when(chatEvent.getMessage()).thenReturn("hello");
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    BetterChatFormat listener = new BetterChatFormat();
    listener.injectContext(serviceContext);
    listener.onChat(chatEvent);

    verify(serviceContext).getGroupService();
  }

  @Test
  void channelConstantsHaveExpectedValues() {
    assertAll(
        () -> assertEquals("#v ", BetterChatFormat.VIP_CHANNEL),
        () -> assertEquals("#m ", BetterChatFormat.MOD_CHANNEL),
        () -> assertEquals("#a ", BetterChatFormat.ADMIN_CHANNEL)
    );
  }

  @Test
  void onChatVipPlayerWithVipPrefixButSecondVipAuthorizationFailsBroadcastsToAll() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true, false);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.VIP_CHANNEL + "vip message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.of(vipGroupEntry));
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }

  @Test
  void onChatVipPlayerWithModPrefixButModGroupAbsentBroadcastsToAll() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.MOD_CHANNEL + "mod message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }

  @Test
  void onChatVipPlayerWithAdminPrefixButAdminGroupAbsentBroadcastsToAll() {
    when(groupService.isSenderAuthorized(player, "vip")).thenReturn(true);
    when(chatEvent.getMessage()).thenReturn(BetterChatFormat.ADMIN_CHANNEL + "admin message");
    when(groupService.findGroupByName("vip")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("mod")).thenReturn(Optional.empty());
    when(groupService.findGroupByName("admin")).thenReturn(Optional.empty());
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    when(plugin.getServer()).thenReturn(server);
    when(chatEvent.getPlayer()).thenReturn(player);
    when(player.getCustomName()).thenReturn("TestPlayer");

    betterChatFormat.onChat(chatEvent);

    assertAll(
        () -> verify(server).broadcastMessage(anyString()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(Player.class), any(), any()),
        () -> verify(chatService, never()).sendMessageInChannel(any(), any(String.class), any(), any())
    );
  }
}