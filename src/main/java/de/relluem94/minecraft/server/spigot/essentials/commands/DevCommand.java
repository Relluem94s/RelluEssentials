package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.AddAutosmeltCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.AddTelekinesisCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.CloneWorldCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.CustomMobCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.DevPlattformCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.GiveCloudSailorCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.GivePickaxeCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.GiveRelluGearCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.GiveSkullsCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.RemoveEnchantsCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.RotateTestCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.ShowPlayerStatsCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.dev.ToggleDamageInfoCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared.UndoHistoryManager;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.registry.SubCommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@CommandName("ZAQmNCRXEdwSGU7DvEcXTbBkp2qEaCSSNkQcMhL3m7KSDtmXWaxtbYCaQCFBR96fj")
public class DevCommand implements CommandConstruct {

  private final SubCommandRegistry<SubCommand> subCommandRegistry;
  private GroupService groupService;

  public DevCommand() {
    UndoHistoryManager undoHistoryManager = new UndoHistoryManager();
    subCommandRegistry = new SubCommandRegistry<>(List.of(
        new CustomMobCommand(),
        new RotateTestCommand(),
        new DevPlattformCommand(undoHistoryManager),
        new GivePickaxeCommand(),
        new GiveCloudSailorCommand(),
        new GiveRelluGearCommand(),
        new AddAutosmeltCommand(),
        new AddTelekinesisCommand(),
        new RemoveEnchantsCommand(),
        new CloneWorldCommand(),
        new ToggleDamageInfoCommand(),
        new ShowPlayerStatsCommand(),
        new GiveSkullsCommand()
    ));
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (!isPlayer(sender)) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) sender;

    if (!p.getName().equalsIgnoreCase("Relluem94")) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_INVALID));
      return true;
    }

    if (args.length < 1) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
      return true;
    }

    if (args.length > 1) {
      sender.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_TO_MANY_ARGUMENTS));
      return true;
    }

    SubCommand subCommand = subCommandRegistry.find(args);
    if (subCommand == null) {
      p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      return true;
    }

    subCommand.execute(p, args);
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

    if (!groupService.isSenderAuthorized(commandSender, "mod")) {
      return tabList;
    }

    if (!isPlayer(commandSender)) {
      return tabList;
    }

    if (strings.length == 1) {
      tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
      return tabList;
    }

    return tabList;
  }

  @Getter
  public enum Commands implements CommandsEnum {
    CUSTOM_MOB("cm"),
    CLOUD_SAILOR("cs"),
    PICKAXE("pick"),
    RELLU("rellu"),
    SMELT("smelt"),
    TELE("tele"),
    NO_ENCHANT("noenchant"),
    WORLDS("worlds"),
    PLAYER_STATS("pl"),
    DAMAGE_INFO("di"),
    SKULL("sk"),
    ROTATE_TEST("rt"),
    DEV_PLATTFORM("dp");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}