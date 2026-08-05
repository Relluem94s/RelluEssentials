package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("setGroup")
public class PermissionsGroup implements CommandConstruct {

  private GroupService groupService;
  private GroupRegistry groupRegistry;
  private TranslationService translationService;
  private PlayerService playerService;

  private @Nullable GroupEntry checkGroupExists(GroupService groupService,
      GroupRegistry groupRegistry, String groupName, Player p) {
    Optional<GroupEntry> groupEntry = groupRegistry.findByName(groupName);
    if (!groupEntry.isPresent()) {
      p.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP_GROUP_NOT_FOUND, groupName));
      return null;
    }

    if (!groupService.isSenderAuthorized(p, "mod")) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return null;
    }
    return groupEntry.get();
  }

  private void setGroupForTarget(@NotNull CommandSender s, @NotNull GroupEntry g,
      @NotNull OfflinePlayer target) {
    s.sendMessage(
        translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP, g.getPrefix() + g.getName(),
            target.getName()));
    if (target.isOnline() && Bukkit.getPlayer(target.getUniqueId()) != null) {
      Objects.requireNonNull(Bukkit.getPlayer(target.getUniqueId()))
          .sendMessage(
              translationService.getWithPrefix(MessageKey.COMMAND_SETGROUP,
                  g.getPrefix() + g.getName(),
                  target.getName()));
    }
    playerService.updateGroup(target, g);
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.translationService = context.getTranslationService();
    this.groupRegistry = context.getGroupRegistry();
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String @NotNull [] args) {

    if (args.length < 2) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
      return true;
    }

    if (args.length > 2) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    OfflinePlayer target = PlayerHelper.getOfflinePlayer(args[0]);

    if (target == null) {
      sender.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(target.getUniqueId())
        == null) {
      sender.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER, args[0]));
      return true;
    }

    if (isPlayer(sender)) {
      Player p = (Player) sender;
      GroupEntry g = checkGroupExists(groupService, groupRegistry, args[1], p);
      setGroupForTarget(p, Objects.requireNonNull(g), target);
      return true;
    } else if (isCMDBlock(sender) || isConsole(sender)) {
      GroupEntry g = groupService.resolveGroupWithFallback(args[1]);
      setGroupForTarget(sender, Objects.requireNonNull(g), target);
      return true;
    }
    return false;
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!groupService.isSenderAuthorized(commandSender, "mod")) {
      return new ArrayList<>();
    }

    if (strings.length > 2) {
      return new ArrayList<>();
    }

    if (strings.length == 1) {
      return TabCompleterHelper.getOnlinePlayers();
    }

    return TabCompleterHelper.getGroups(groupRegistry.getAll());
  }
}