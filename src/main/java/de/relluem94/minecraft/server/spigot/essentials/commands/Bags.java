package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BagService;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
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

  private GroupService groupService;
  private TranslationService translationService;
  private BagService bagService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.translationService = context.getTranslationService();
    this.bagService = context.getBagService();
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

    if (strings.length > 1) {
      return tabList;
    }

    tabList.addAll(TabCompleterHelper.getBags((Player) commandSender));

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
      commandSender.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) commandSender;

    if (!groupService.isSenderAuthorized(commandSender, "user")) {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length != 1) {
      p.openInventory(
          bagService.getBagsInventory(
              RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p)));
      return true;
    }

    Optional<BagTypeEntry> bte;
    if (TypeUtils.isInt(args[0])) {
      bte = RelluEssentials.getInstance().getBagTypeRegistry().findById(Integer.parseInt(args[0]));
    } else {
      bte = RelluEssentials.getInstance().getBagTypeRegistry().findByName(args[0]);
    }

    if (bte.isPresent()) {
      PlayerEntry pe = RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p);
      if (bagService.hasBag(pe.getId(), bte.get().getId())) {
        p.openInventory(Objects.requireNonNull(bagService.getBagInventory(bte.get().getId(), pe)));
      } else {
        p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, args[0]));
      }

    } else {
      p.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_BAGS_NOT_FOUND, args[0]));
    }
    return true;
  }
}