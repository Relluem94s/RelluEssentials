package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
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

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {

    if (!isPlayer(sender)) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!PermissionHelper.isAuthorized(p, GroupRegistry.getGroup("vip").getId())) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      flyMode(p);
      return true;
    }

    Player target = Bukkit.getPlayer(args[0]);

    if (target == null) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    if (PermissionHelper.isAuthorized(p, GroupRegistry.getGroup("mod").getId())) {
      p.sendMessage(languageHelper.getWithPrefix(
          MessageKey.COMMAND_FLYMODE,
          target.getCustomName(),
          !target.getAllowFlight() ? languageHelper.get(MessageKey.COMMAND_FLYMODE_ACTIVATED)
              : languageHelper.get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)
      ));
      flyMode(target);
    } else {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));

    }
    return true;
  }

  private void flyMode(@NotNull Player p) {
    PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p.getUniqueId());
    pe.setFlying(!pe.isFlying());
    pe.setUpdatedBy(pe.getId());
    pe.setHasToBeUpdated(true);
    p.setAllowFlight(pe.isFlying());
    p.sendMessage(languageHelper.getWithPrefix(
        MessageKey.COMMAND_FLYMODE,
        p.getCustomName(),
        p.getAllowFlight() ? languageHelper.get(MessageKey.COMMAND_FLYMODE_ACTIVATED)
            : languageHelper.get(MessageKey.COMMAND_FLYMODE_DEACTIVATED)
    ));
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!PermissionHelper.isAuthorized(commandSender, GroupRegistry.getGroup("mod").getId())) {
      return new ArrayList<>();
    }

    if (strings.length > 1) {
      return new ArrayList<>();
    }

    return TabCompleterHelper.getOnlinePlayers();
  }
}