package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.AFK;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.commands.Back;
import de.relluem94.minecraft.server.spigot.essentials.commands.Bags;
import de.relluem94.minecraft.server.spigot.essentials.commands.Broadcast;
import de.relluem94.minecraft.server.spigot.essentials.commands.Cookies;
import de.relluem94.minecraft.server.spigot.essentials.commands.CraftingBench;
import de.relluem94.minecraft.server.spigot.essentials.commands.CustomHead;
import de.relluem94.minecraft.server.spigot.essentials.commands.Day;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.commands.Enderchest;
import de.relluem94.minecraft.server.spigot.essentials.commands.Exit;
import de.relluem94.minecraft.server.spigot.essentials.commands.Fly;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeAdventure;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeCreative;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeSpectator;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameModeSurvival;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameRules;
import de.relluem94.minecraft.server.spigot.essentials.commands.God;
import de.relluem94.minecraft.server.spigot.essentials.commands.Head;
import de.relluem94.minecraft.server.spigot.essentials.commands.Heal;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.commands.Inventory;
import de.relluem94.minecraft.server.spigot.essentials.commands.Marry;
import de.relluem94.minecraft.server.spigot.essentials.commands.Message;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.commands.More;
import de.relluem94.minecraft.server.spigot.essentials.commands.Nick;
import de.relluem94.minecraft.server.spigot.essentials.commands.Night;
import de.relluem94.minecraft.server.spigot.essentials.commands.PermissionsGroup;
import de.relluem94.minecraft.server.spigot.essentials.commands.PlayerInfo;
import de.relluem94.minecraft.server.spigot.essentials.commands.PlayerList;
import de.relluem94.minecraft.server.spigot.essentials.commands.PlayerWeather;
import de.relluem94.minecraft.server.spigot.essentials.commands.Poke;
import de.relluem94.minecraft.server.spigot.essentials.commands.Position;
import de.relluem94.minecraft.server.spigot.essentials.commands.Print;
import de.relluem94.minecraft.server.spigot.essentials.commands.Protect;
import de.relluem94.minecraft.server.spigot.essentials.commands.Purse;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rain;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rename;
import de.relluem94.minecraft.server.spigot.essentials.commands.Repair;
import de.relluem94.minecraft.server.spigot.essentials.commands.Reply;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sign;
import de.relluem94.minecraft.server.spigot.essentials.commands.Spawn;
import de.relluem94.minecraft.server.spigot.essentials.commands.Speed;
import de.relluem94.minecraft.server.spigot.essentials.commands.Storm;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.commands.Suicide;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sun;
import de.relluem94.minecraft.server.spigot.essentials.commands.Team;
import de.relluem94.minecraft.server.spigot.essentials.commands.Teleport;
import de.relluem94.minecraft.server.spigot.essentials.commands.Title;
import de.relluem94.minecraft.server.spigot.essentials.commands.Vanish;
import de.relluem94.minecraft.server.spigot.essentials.commands.Warp;
import de.relluem94.minecraft.server.spigot.essentials.commands.Where;
import de.relluem94.minecraft.server.spigot.essentials.commands.Worlds;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.registries.CommandRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.CommandService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.minecraft.server.spigot.essentials.wrappers.CommandWrapper;
import java.util.List;
import lombok.Getter;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.Plugin;

public class CommandManager implements Enable {

  @Getter
  private List<CommandWrapper> commandWrapperList = List.of(
      new CommandWrapper(new Admin()),
      new CommandWrapper(new AFK()),
      new CommandWrapper(new Back()),
      new CommandWrapper(new Bags()),
      new CommandWrapper(new Broadcast()),
      new CommandWrapper(new Cookies()),
      new CommandWrapper(new CraftingBench()),
      new CommandWrapper(new CustomHead()),
      new CommandWrapper(new Day()),
      new CommandWrapper(new Enderchest()),
      new CommandWrapper(new Exit()),
      new CommandWrapper(new Fly()),
      new CommandWrapper(new GameModeAdventure()),
      new CommandWrapper(new GameModeCreative()),
      new CommandWrapper(new GameModeSpectator()),
      new CommandWrapper(new GameModeSurvival()),
      new CommandWrapper(new GameRules()),
      new CommandWrapper(new God()),
      new CommandWrapper(new Head()),
      new CommandWrapper(new Heal()),
      new CommandWrapper(new Home()),
      new CommandWrapper(new Inventory()),
      new CommandWrapper(new Marry()),
      new CommandWrapper(new Message()),
      new CommandWrapper(new Modify()),
      new CommandWrapper(new More()),
      new CommandWrapper(new Nick()),
      new CommandWrapper(new Night()),
      new CommandWrapper(new PermissionsGroup()),
      new CommandWrapper(new PlayerInfo()),
      new CommandWrapper(new PlayerList()),
      new CommandWrapper(new PlayerWeather()),
      new CommandWrapper(new Poke()),
      new CommandWrapper(new Position()),
      new CommandWrapper(new Print()),
      new CommandWrapper(new Protect()),
      new CommandWrapper(new Purse()),
      new CommandWrapper(new Rain()),
      new CommandWrapper(new Rename()),
      new CommandWrapper(new Repair()),
      new CommandWrapper(new Reply()),
      new CommandWrapper(new Sign()),
      new CommandWrapper(new Spawn()),
      new CommandWrapper(new Speed()),
      new CommandWrapper(new Storm()),
      new CommandWrapper(new Sudo()),
      new CommandWrapper(new Suicide()),
      new CommandWrapper(new Sun()),
      new CommandWrapper(new Team()),
      new CommandWrapper(new Teleport()),
      new CommandWrapper(new Title()),
      new CommandWrapper(new Vanish()),
      new CommandWrapper(new Warp()),
      new CommandWrapper(new Where()),
      new CommandWrapper(new Worlds()),

      // THIS IS A DEV COMMAND
      new CommandWrapper(new DevCommand())
  );

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    TranslationService translationService = serviceContext.getTranslationService();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_COMMANDS));

    CommandRegistry commandRegistry = new CommandRegistry();

    commandWrapperList.forEach(wrapper -> {
      wrapper.init(relluEssentialsPlugin, serviceContext);
      commandRegistry.register(wrapper);
    });

    CommandService commandService = new CommandService(commandRegistry);
    serviceContext.setCommandService(commandService);

    int commands = PluginCommandYamlParser.parse(plugin).size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_COMMANDS_REGISTERED, commands));
  }
}