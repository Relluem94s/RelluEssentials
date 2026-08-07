package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.OfflinePlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.rellulib.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("sudo")
public class Sudo implements CommandConstruct {

  private ServiceContext serviceContext;

  public static void exitSudo(@NotNull Player p, TranslationService translationService, PlayerService playerService) {
    PlayerEntry tpe = SudoManager.sudoers.get(p.getUniqueId());
    PlayerEntry pe = playerService.getPlayerEntry(p);
    WorldHelper.saveWorldGroupInventory(p, true);
    pe.setId(tpe.getId());
    pe.setCustomName(tpe.getCustomName());
    pe.setGroup(tpe.getGroup());
    pe.setHomes(tpe.getHomes());
    pe.setPurse(tpe.getPurse());
    p.setCustomName(tpe.getGroup().getPrefix() + p.getName());
    if (tpe.getCustomName() != null) {
      p.setCustomName(tpe.getGroup().getPrefix() + tpe.getCustomName());
    }
    WorldHelper.loadWorldGroupInventory(p);
    SudoManager.sudoers.remove(p.getUniqueId());
    p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_SUDO_DEACTIVATED));
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!serviceContext.getGroupService().isSenderAuthorized(sender, "admin")) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (args.length == 0) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
      return true;
    }

    if (RelluEssentials.getInstance().getCommand(args[0]) != null) {
      dispatchCommand(args);
      return true;
    }

    if (args.length != 1) {
      sender.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      return true;
    }

    if (SudoManager.sudoers.containsKey(p.getUniqueId())) {
      exitSudo(p, serviceContext.getTranslationService(), serviceContext.getPlayerService());
      return true;
    }

    OfflinePlayerEntry target = PlayerHelper.getOfflinePlayerByName(args[0]);
    PlayerEntry pe = serviceContext.getPlayerService().getPlayerEntry(p);

    if (target == null) {
      p.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_SUDO_PLAYER_NOT_FOUND, args[0]));
      return true;
    }

    if (RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(target.getId()) == null) {
      p.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_SUDO_PLAYER_NOT_FOUND, args[0]));
      return true;
    }

    PlayerEntry tpe = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(target.getId());
    SudoManager.sudoers.put(p.getUniqueId(), new PlayerEntry(pe));
    WorldHelper.saveWorldGroupInventory(p, true);
    pe.setId(tpe.getId());
    pe.setCustomName(tpe.getCustomName());
    pe.setGroup(tpe.getGroup());
    pe.setHomes(tpe.getHomes());
    pe.setPurse(tpe.getPurse());
    p.setCustomName(tpe.getGroup().getPrefix() + target.getName());
    if (tpe.getCustomName() != null) {
      p.setCustomName(tpe.getGroup().getPrefix() + tpe.getCustomName());
    }
    WorldHelper.loadWorldGroupInventory(p);
    p.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_SUDO_ACTIVATED,
            tpe.getGroup().getPrefix() + target.getName()));

    return true;
  }

  private void dispatchCommand(String[] args) {
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    Bukkit.getServer().dispatchCommand(console, StringUtils.toString(args));
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "admin")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length == 1) {
      tabList.addAll(TabCompleterHelper.getPluginCommands(
          serviceContext.getCommandManager().getCommandWrapperList()));
      tabList.addAll(TabCompleterHelper.getOnlinePlayers());
      return tabList;
    }

    if (strings.length == 2) {
      tabList.addAll(TabCompleterHelper.getOnlinePlayers());
      return tabList;
    }

    return tabList;
  }
}