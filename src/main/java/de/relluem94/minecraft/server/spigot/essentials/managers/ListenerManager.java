package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterLights;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterMobs;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterPlayerQuit;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterSafety;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterSoil;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BetterWorlds;
import de.relluem94.minecraft.server.spigot.essentials.listeners.BlockPlace;
import de.relluem94.minecraft.server.spigot.essentials.listeners.CloudSailor;
import de.relluem94.minecraft.server.spigot.essentials.listeners.CreateSignActions;
import de.relluem94.minecraft.server.spigot.essentials.listeners.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.listeners.GrapplingHockEvent;
import de.relluem94.minecraft.server.spigot.essentials.listeners.IntegrationListener;
import de.relluem94.minecraft.server.spigot.essentials.listeners.MOTD;
import de.relluem94.minecraft.server.spigot.essentials.listeners.NoDeathMessage;
import de.relluem94.minecraft.server.spigot.essentials.listeners.OpenWorldSelectorEvent;
import de.relluem94.minecraft.server.spigot.essentials.listeners.PlayerMove;
import de.relluem94.minecraft.server.spigot.essentials.listeners.PositionAxeListener;
import de.relluem94.minecraft.server.spigot.essentials.listeners.PreventCoinManipulation;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignCommandAction;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignDownAction;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignEdit;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignHomeAction;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignInteractListener;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignSpawnAction;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignTeleportAction;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SignUpAction;
import de.relluem94.minecraft.server.spigot.essentials.listeners.SkullInfo;
import de.relluem94.minecraft.server.spigot.essentials.listeners.ToolCrafting;
import de.relluem94.minecraft.server.spigot.essentials.listeners.bag.BlockBreakBags;
import de.relluem94.minecraft.server.spigot.essentials.listeners.bag.BlockDropItemBags;
import de.relluem94.minecraft.server.spigot.essentials.listeners.bag.EntityPickupItemBags;
import de.relluem94.minecraft.server.spigot.essentials.listeners.bag.InventoryClickBags;
import de.relluem94.minecraft.server.spigot.essentials.listeners.npc.DamgeNpc;
import de.relluem94.minecraft.server.spigot.essentials.listeners.npc.DamgeTraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.listeners.npc.InteractNpc;
import de.relluem94.minecraft.server.spigot.essentials.listeners.npc.InteractTraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.listeners.npc.InventoryClickNpc;
import de.relluem94.minecraft.server.spigot.essentials.listeners.npc.NpcChunkLoadListener;
import de.relluem94.minecraft.server.spigot.essentials.listeners.npc.PlaceNpc;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.BetterLock;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.BlockModifyProtect;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.BlockPistonProtect;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.BlockRedstoneProtect;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.EntityBreakDoorProtect;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.EntityExplodeProtect;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.InventoryMoveItemProtect;
import de.relluem94.minecraft.server.spigot.essentials.listeners.protect.PlayerInteractProtect;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.wrapper.ListenerWrapper;
import java.util.List;
import org.bukkit.plugin.Plugin;

public class ListenerManager implements Enable {

  private final List<ListenerWrapper> listenerWrapperList = List.of(
      new ListenerWrapper(new BetterChatFormat()),
      new ListenerWrapper(new BetterWorlds()),
      new ListenerWrapper(new BetterPlayerJoin()),
      new ListenerWrapper(new BetterPlayerQuit()),
      new ListenerWrapper(new BetterBlockDrop()),
      new ListenerWrapper(new BetterLights()),
      new ListenerWrapper(new BlockBreakBags()),
      new ListenerWrapper(new BlockDropItemBags()),
      new ListenerWrapper(new InventoryClickBags()),
      new ListenerWrapper(new EntityPickupItemBags()),
      new ListenerWrapper(new BlockPlace()),
      new ListenerWrapper(new BetterMobs()),
      new ListenerWrapper(new BetterSoil()),
      new ListenerWrapper(new NpcChunkLoadListener()),
      new ListenerWrapper(new DamgeNpc()),
      new ListenerWrapper(new DamgeTraderNpc()),
      new ListenerWrapper(new InteractNpc()),
      new ListenerWrapper(new InteractTraderNpc()),
      new ListenerWrapper(new InventoryClickNpc()),
      new ListenerWrapper(new PlaceNpc()),
      new ListenerWrapper(new BetterSafety()),
      new ListenerWrapper(new BlockPistonProtect()),
      new ListenerWrapper(new EntityBreakDoorProtect()),
      new ListenerWrapper(new InventoryMoveItemProtect()),
      new ListenerWrapper(new EntityExplodeProtect()),
      new ListenerWrapper(new BlockRedstoneProtect()),
      new ListenerWrapper(new BlockModifyProtect()),
      new ListenerWrapper(new PlayerInteractProtect()),
      new ListenerWrapper(new OpenWorldSelectorEvent()),
      new ListenerWrapper(new BetterLock()),
      new ListenerWrapper(new SkullInfo()),
      new ListenerWrapper(new NoDeathMessage()),
      new ListenerWrapper(new PlayerMove()),
      new ListenerWrapper(new MOTD()),
      new ListenerWrapper(new CloudSailor()),
      new ListenerWrapper(new CreateSignActions()),
      new ListenerWrapper(new SignEdit()),
      new ListenerWrapper(new ToolCrafting()),
      new ListenerWrapper(new CustomEnchantment()),
      new ListenerWrapper(new GrapplingHockEvent()),
      new ListenerWrapper(new PositionAxeListener()),
      new ListenerWrapper(new PreventCoinManipulation()),
      new ListenerWrapper(new IntegrationListener()),
      new ListenerWrapper(new SignInteractListener(RelluEssentials.getInstance())),
      new ListenerWrapper(new SignUpAction()),
      new ListenerWrapper(new SignDownAction()),
      new ListenerWrapper(new SignSpawnAction()),
      new ListenerWrapper(new SignHomeAction()),
      new ListenerWrapper(new SignTeleportAction()),
      new ListenerWrapper(new SignCommandAction())
  );

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    TranslationService translationService = relluEssentialsPlugin.getTranslationService();

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_EVENTS));
    listenerWrapperList
        .forEach(listenerWrapper -> listenerWrapper.init(relluEssentialsPlugin,
            relluEssentialsPlugin.getServiceContext()));
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_EVENTS_REGISTERED,
            listenerWrapperList.size()));
  }

}
