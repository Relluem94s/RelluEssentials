package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("back")
public class Back implements CommandConstruct {

  private static final Map<Player, Location> backPlayerLocation = new HashMap<>();

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return new ArrayList<>();
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

    Player player = (Player) commandSender;

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
      player.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (!serviceContext.getBackService().hasBackPoint(player)) {
      player.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_BACK_NO_LOCATION));
      return true;
    }

    Location backLocation = serviceContext.getBackService()
        .findBackPoint(player).get();
    serviceContext.getBackService().removeBackPoint(player);
    serviceContext.getTeleportService().teleportBack(player, backLocation);
    return true;
  }
}