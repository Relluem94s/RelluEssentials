package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.rellulib.utils.TypeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("bags")
public class Bags implements CommandConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length > 1) {
      return tabList;
    }

    PlayerEntry playerEntry = serviceContext.getPlayerService().getPlayerEntry((Player)commandSender);
    tabList.addAll(serviceContext.getBagService().getBagTypeNamesForPlayer(playerEntry.getId()));

    return tabList;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public boolean onCommand(@NonNull CommandSender commandSender, @NotNull Command command,
      @NonNull String label, String[] args) {

    if (!isPlayer(commandSender)) {
      commandSender.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) commandSender;

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    boolean bagsEnabled = serviceContext.getWorldGroupService()
        .isSettingActiveForWorld(WorldSetting.COLLECT_BAG, p.getWorld().getName());

    if(!bagsEnabled){
      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length != 1) {
      p.openInventory(
          serviceContext.getBagService().getBagsInventory(
              serviceContext.getPlayerService().getPlayerEntry(p)));
      return true;
    }

    Optional<BagTypeEntry> bte;
    if (TypeUtils.isInt(args[0])) {
      bte = serviceContext.getBagService().findBagTypeById(Integer.parseInt(args[0]));
    } else {
      bte = serviceContext.getBagService().findBagTypeByPartialName(args[0]);
    }

    if (bte.isPresent()) {
      PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);
      if (serviceContext.getBagService().hasBag(pe.getId(), bte.get().getId())) {
        p.openInventory(Objects.requireNonNull(serviceContext.getBagService().getBagInventory(bte.get().getId(), pe)));
      } else {
        p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, args[0]));
      }

    } else {
      p.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, args[0]));
    }
    return true;
  }
}