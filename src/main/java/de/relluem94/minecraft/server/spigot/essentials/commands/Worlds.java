package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("world")
public class Worlds implements CommandConstruct {

  private ServiceContext serviceContext;

  public static void openWorldMenu(Player p, ItemService itemService, PluginMetadataService pluginMetadataService) {
    org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(18,
            Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dWorlds"),
        itemService.find(
                new RelluEssentialsNamespacedKey(pluginMetadataService.getName(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
            .orElseThrow()
            .toItemStack()
    );

    for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
      ItemStack is = PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE);
      ItemMeta im = is.getItemMeta();

      if (im == null) {
        return;
      }

      im.setDisplayName(Bukkit.getWorlds().get(i).getName());

      is.setItemMeta(im);
      inv.setItem(i, is);
    }

    InventoryHelper.openInventory(p, inv);
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public boolean onCommand(@NonNull CommandSender commandSender, @NotNull Command command,
      @NonNull String label, String[] args) {
    if (isCMDBlock(commandSender) && args.length == 2 && !args[0].equalsIgnoreCase(
        Commands.LIST.getName())
        && args[1].equals("@p")) {
      BlockCommandSender bcs = (BlockCommandSender) commandSender;
      CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
      Player p = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
      if (p == null) {
        commandSender.sendMessage(
            String.format(serviceContext.getTranslationService()
                    .getWithPrefix(MessageKey.COMMAND_TARGET_NOT_A_PLAYER),
                "No Player in Reach"));
        return true;
      }

      serviceContext.getTeleportService().teleportWorld(p, args[0]);
      return true;
    }

    if (!isPlayer(commandSender)) {
      commandSender.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NOT_A_PLAYER));
      return true;
    }

    Player p = (Player) commandSender;

    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "user")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 0) {
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_WORLD_INFO,
              Commands.LIST.getName(),
              Commands.LOAD.getName(),
              Commands.UNLOAD.getName(),
              Commands.UNLOAD_NO_SAVE.getName(),
              Commands.CREATE.getName()
          ));
      openWorldMenu(p, serviceContext.getItemService(), serviceContext.getPluginMetadataService());
      return true;
    }

    if (args.length == 1) {
      if (!args[0].equalsIgnoreCase(Commands.LIST.getName())) {
        serviceContext.getTeleportService().teleportWorld(p, args[0]);
        return true;
      }

      if (!serviceContext.getGroupService().isSenderAuthorized(p, "mod")) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        return true;
      }

      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_WORLD_INFO));
      Bukkit.getWorlds().forEach(w -> p.sendMessage(w.getName()));
      return true;
    }

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "admin")) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return true;
    }

    if (args.length == 2) {
      if (Commands.LOAD.getName().equalsIgnoreCase(args[0])) {
        WorldHelper.loadWorld(args[1]);
        p.sendMessage(
            serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_WORLD_LOAD));
        return true;
      } else if (Commands.UNLOAD.getName().equalsIgnoreCase(args[0])) {
        unloadWorld(p, args[1], true);
        return true;
      } else if (Commands.UNLOAD_NO_SAVE.getName().equalsIgnoreCase(args[0])) {
        unloadWorld(p, args[1], false);
        return true;
      } else if (Commands.CREATE.getName().equalsIgnoreCase(args[0])) {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WORLD_CREATE_INFO));
        return true;
      } else {
        p.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
        return true;
      }
    }

    if (args.length > 5) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_TO_LESS_ARGUMENTS));
      return true;
    }

    if (!args[0].equalsIgnoreCase(Commands.CREATE.getName())) {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WRONG_SUB_COMMAND));
      return true;
    }

    createWorld(p, args);
    return true;
  }

  private boolean isValidWorldEnvironment(String input) {
    return Arrays.stream(World.Environment.values())
        .filter(env -> env.name().equalsIgnoreCase(input))
        .findFirst()
        .orElse(null) != null;
  }

  private void createWorld(Player p, String @NotNull [] args) {
    if (WorldType.getByName(args[2].toUpperCase()) != null && isValidWorldEnvironment(args[3])
        && (Boolean.parseBoolean(args[4]))) {
      WorldType type = WorldType.getByName(args[2].toUpperCase());
      World.Environment worldEnvironment = World.Environment.valueOf(args[3].toUpperCase());
      boolean structures = Boolean.parseBoolean(args[4]);
      WorldHelper.createWorld(args[1], type, worldEnvironment, structures);
      p.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_WORLD_CREATE));
    } else {
      p.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_WORLD_WRONG_ARGUMENTS));
    }
  }

  private void unloadWorld(@NotNull Player p, String name, boolean save) {
    try {
      WorldHelper.unloadWorld(name, save);
      p.sendMessage(save
          ? serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_WORLD_UNLOAD)
          : serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_WORLD_UNLOAD_NO_SAVE));
    } catch (WorldNotLoadedException ex) {
      Logger.getLogger(Worlds.class.getName())
          .log(Level.SEVERE, serviceContext.getTranslationService()
                  .getWithPrefix(MessageKey.COMMAND_WORLD_NOT_LOADED),
              ex);
    }
  }

  @Override
  public CommandsEnum[] getCommands() {
    return Commands.values();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
      @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!serviceContext.getGroupService().isSenderAuthorized(commandSender, "mod")) {
      return new ArrayList<>();
    }

    if (strings.length > 5) {
      return new ArrayList<>();
    }

    if (strings.length == 1) {
      List<String> tabList = new ArrayList<>();
      tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
      tabList.addAll(TabCompleterHelper.getWorlds());
      return tabList;

    }

    if (strings.length == 2) {
      if (Commands.UNLOAD.getName().equalsIgnoreCase(strings[0])) {
        return TabCompleterHelper.getWorlds();
      }

      if (Commands.CREATE.getName().equalsIgnoreCase(strings[0])) {
        return List.of("<enter name>");
      }
    }

    if (strings.length == 3) {
      if (Commands.CREATE.getName().equalsIgnoreCase(strings[0])) {
        return TabCompleterHelper.getWorldTypes();
      }
    }

    if (strings.length == 4) {
      if (Commands.CREATE.getName().equalsIgnoreCase(strings[0])) {
        return TabCompleterHelper.getWorldEnvironmentTypes();
      }
    }

    if (strings.length == 5) {
      if (Commands.CREATE.getName().equalsIgnoreCase(strings[0])) {
        return List.of("true", "false");
      }
    }

    return new ArrayList<>();
  }

  @Getter
  public enum Commands implements CommandsEnum {
    CREATE("create"),
    LOAD("load"),
    LIST("list"),
    UNLOAD("unload"),
    UNLOAD_NO_SAVE("unloadNoSave");

    private final String name;
    private final String[] subCommands;

    Commands(String name, String... subCommands) {
      this.name = name;
      this.subCommands = subCommands;
    }
  }
}