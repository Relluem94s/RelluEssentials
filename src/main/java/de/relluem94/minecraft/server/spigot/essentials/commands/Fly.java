package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.translationService;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("fly")
public class Fly implements CommandConstruct {

  private GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {

    if (!isPlayer(sender)) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!groupService.isSenderAuthorized(p, "vip")) {
      sender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      flyMode(p);
      return true;
    }

    Player target = Bukkit.getPlayer(args[0]);

    if (target == null) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    if (groupService.isSenderAuthorized(sender, "mod")) {
      p.sendMessage(translationService.getWithPrefix(
          MessageKey.COMMAND_FLYMODE,
          target.getCustomName(),
          !target.getAllowFlight() ? translationService.get(MessageKey.COMMAND_FLYMODE_ACTIVATED)
              : translationService.get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)
      ));
      flyMode(target);
    } else {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));

    }
    return true;
  }

  private void flyMode(@NotNull Player p) {
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(p.getUniqueId());
    pe.setFlying(!pe.isFlying());
    pe.setUpdatedBy(pe.getId());
    pe.setHasToBeUpdated(true);
    p.setAllowFlight(pe.isFlying());
    p.sendMessage(translationService.getWithPrefix(
        MessageKey.COMMAND_FLYMODE,
        p.getCustomName(),
        p.getAllowFlight() ? translationService.get(MessageKey.COMMAND_FLYMODE_ACTIVATED)
            : translationService.get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)
    ));
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