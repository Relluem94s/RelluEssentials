package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("0")
public class GameModeSurvival implements CommandConstruct {

  private GroupService groupService;
  private TranslationService translationService;
  private PlayerService playerService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.translationService = context.getTranslationService();
    this.playerService = context.getPlayerService();
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, String[] args) {
    if (!groupService.isSenderAuthorized(sender, "mod")) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 1) {
      Player target = Bukkit.getPlayer(args[0]);

      if (target == null) {
        sender.sendMessage(translationService.get(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
        return true;
      }

      gameMode(target);
      return true;
    }

    if (!isPlayer(sender)) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    gameMode((Player) sender);
    return true;
  }

  private void gameMode(@NotNull Player p) {
    p.setGameMode(GameMode.SURVIVAL);
    playerService.setFlying(p);
    p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_GAMEMODE, p.getCustomName(),
        translationService.get(MessageKey.COMMAND_GAMEMODE_SURVIVAL)));
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!groupService.isSenderAuthorized(commandSender, "mod")) {
      return new ArrayList<>();
    }

    if (strings.length > 1) {
      return new ArrayList<>();
    }

    return TabCompleterHelper.getOnlinePlayers();
  }
}
