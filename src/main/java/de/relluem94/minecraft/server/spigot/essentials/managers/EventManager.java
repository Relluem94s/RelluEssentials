package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
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
import de.relluem94.minecraft.server.spigot.essentials.wrapper.EventWrapper;
import java.util.List;

public class EventManager implements Enable {

  @Override
  public void enable(RelluEssentials plugin) {
    ServiceContext serviceContext = new ServiceContext(plugin);
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_REGISTER_EVENTS));
    eventWrapperList
        .forEach(eventWrapper -> eventWrapper.init(plugin, serviceContext));
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_EVENTS_REGISTERED,
            eventWrapperList.size()));


  }


  private final List<EventWrapper> eventWrapperList = List.of(
      new EventWrapper(new BetterChatFormat()),
      new EventWrapper(new BetterWorlds()),
      new EventWrapper(new BetterPlayerJoin()),
      new EventWrapper(new BetterPlayerQuit()),
      new EventWrapper(new BetterBlockDrop()),
      new EventWrapper(new BetterLights()),
      new EventWrapper(new BlockBreakBags()),
      new EventWrapper(new BlockDropItemBags()),
      new EventWrapper(new InventoryClickBags()),
      new EventWrapper(new EntityPickupItemBags()),
      new EventWrapper(new BlockPlace()),
      new EventWrapper(new BetterMobs()),
      new EventWrapper(new BetterSoil()),
      new EventWrapper(new NpcChunkLoadListener()),
      new EventWrapper(new DamgeNpc()),
      new EventWrapper(new DamgeTraderNpc()),
      new EventWrapper(new InteractNpc()),
      new EventWrapper(new InteractTraderNpc()),
      new EventWrapper(new InventoryClickNpc()),
      new EventWrapper(new PlaceNpc()),
      new EventWrapper(new BetterSafety()),
      new EventWrapper(new BlockPistonProtect()),
      new EventWrapper(new EntityBreakDoorProtect()),
      new EventWrapper(new InventoryMoveItemProtect()),
      new EventWrapper(new EntityExplodeProtect()),
      new EventWrapper(new BlockRedstoneProtect()),
      new EventWrapper(new BlockModifyProtect()),
      new EventWrapper(new PlayerInteractProtect()),
      new EventWrapper(new OpenWorldSelectorEvent()),
      new EventWrapper(new BetterLock()),
      new EventWrapper(new SkullInfo()),
      new EventWrapper(new NoDeathMessage()),
      new EventWrapper(new PlayerMove()),
      new EventWrapper(new MOTD()),
      new EventWrapper(new CloudSailor()),
      new EventWrapper(new CreateSignActions()),
      new EventWrapper(new SignEdit()),
      new EventWrapper(new ToolCrafting()),
      new EventWrapper(new CustomEnchantment()),
      new EventWrapper(new GrapplingHockEvent()),
      new EventWrapper(new PositionAxeListener()),
      new EventWrapper(new PreventCoinManipulation()),
      new EventWrapper(new IntegrationListener()),
      new EventWrapper(new SignInteractListener(RelluEssentials.getInstance())),
      new EventWrapper(new SignUpAction()),
      new EventWrapper(new SignDownAction()),
      new EventWrapper(new SignSpawnAction()),
      new EventWrapper(new SignHomeAction()),
      new EventWrapper(new SignTeleportAction()),
      new EventWrapper(new SignCommandAction())
      );

}
