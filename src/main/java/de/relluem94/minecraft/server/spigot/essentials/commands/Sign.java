package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@CommandName("sign")
public class Sign implements CommandConstruct {

  private GroupService groupService;
  private TranslationService translationService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.translationService = context.getTranslationService();
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!groupService.isSenderAuthorized(p, "mod")) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_SIGN_INFO,
          AnnotationHelper.getCommandName(this.getClass()), Commands.COPY.getName(),
          Commands.EDIT.getName()));
    }

    if (args.length > 1) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);

    if (args[0].equalsIgnoreCase(Commands.EDIT.getName())) {
      pe.setPlayerState(PlayerState.SIGN_EDIT);
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_SIGN_EDIT));
    } else if (args[0].equalsIgnoreCase(Commands.COPY.getName())) {
      pe.setPlayerState(PlayerState.SIGN_COPY);
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_SIGN_COPY));
    }

    return true;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> tabList = new ArrayList<>();

    if (!groupService.isSenderAuthorized(commandSender, "user")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length == 1) {
      tabList.addAll(TabCompleterHelper.getCommands(getCommands()));
    }

    return tabList;
  }

  @Getter
  public enum Commands implements CommandsEnum {

    EDIT("edit"),
    COPY("copy");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}
