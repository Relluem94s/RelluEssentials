package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_CREATE_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_CREATE_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_LOAD_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_UNLOAD_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_UNLOAD_WORLD_NO_SAVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_WORLD_NOT_LOADED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WORLD_WRONG_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WRONG_SUB_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportWorld;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.*;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
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

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandName("world")
public class Worlds implements CommandConstruct {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (isCMDBlock(sender) && args.length == 2 && !args[0].equalsIgnoreCase(Commands.LIST.getName()) && args[1].equals("@p")) {
            BlockCommandSender bcs = (BlockCommandSender) sender;
            CommandBlock cb = (CommandBlock) bcs.getBlock().getState();
            Player p = PlayerHelper.getTargetedPlayer(cb.getBlock().getLocation());
            if(p == null){
                sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, "No Player in Reach"));
                return true;
            }

            teleportWorld(p, args[0]);
            return true;
        }

        if(!isPlayer(sender)){
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if(args.length == 0){
            p.sendMessage(PLUGIN_COMMAND_WORLD_INFO);
            openWorldMenu(p);
            return true;
        }

        if(args.length == 1){
            if (!args[0].equalsIgnoreCase(Commands.LIST.getName())) {
                teleportWorld(p, args[0]);
                return true;
            }

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            sendMessage(p, PLUGIN_COMMAND_WORLD);
            Bukkit.getWorlds().forEach(w -> sendMessage(p, w.getName()));
            return true;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if(args.length == 2){
            if(Commands.LOAD.getName().equalsIgnoreCase(args[0])){
                WorldHelper.loadWorld(args[1]);
                p.sendMessage(PLUGIN_COMMAND_WORLD_LOAD_WORLD);
                return true;
            }
            else if(Commands.UNLOAD.getName().equalsIgnoreCase(args[0])){
                unloadWorld(p, args[1], true);
                return true;
            }
            else if(Commands.UNLOAD_NO_SAVE.getName().equalsIgnoreCase(args[0])){
                unloadWorld(p, args[1], false);
                return true;
            }
            else if(Commands.CREATE.getName().equalsIgnoreCase(args[0])){
                p.sendMessage(PLUGIN_COMMAND_WORLD_CREATE_INFO);
                return true;
            }
            else{
                p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
                return true;
            }
        }

        if(args.length > 5){
            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if (!args[0].equalsIgnoreCase(Commands.CREATE.getName())) {
            p.sendMessage(PLUGIN_COMMAND_WRONG_SUB_COMMAND);
            return true;
        }

        createWorld(p, args);
        return true;
    }

    private boolean isValidWorldEnvironment(String input){
        return Arrays.stream(World.Environment.values())
                .filter(env -> env.name().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null) != null;
    }

    private void createWorld(Player p, String @NotNull [] args){
        if((WorldType.getByName(args[2].toUpperCase()) != null) && isValidWorldEnvironment(args[3]) && (Boolean.parseBoolean(args[4]))){
            WorldType type = WorldType.getByName(args[2].toUpperCase());
            World.Environment worldEnvironment = World.Environment.valueOf(args[3].toUpperCase());
            boolean structures = Boolean.parseBoolean(args[4]);
            WorldHelper.createWorld(args[1], type, worldEnvironment, structures);
            p.sendMessage(PLUGIN_COMMAND_WORLD_CREATE_WORLD);
        }
        else{
            p.sendMessage(PLUGIN_COMMAND_WORLD_WRONG_ARGUMENTS);
        }
    }

    private void unloadWorld(@NotNull Player p, String name, boolean save){
        try {
            WorldHelper.unloadWorld(name, save);
            p.sendMessage(save ? PLUGIN_COMMAND_WORLD_UNLOAD_WORLD : PLUGIN_COMMAND_WORLD_UNLOAD_WORLD_NO_SAVE);
            
        }
        catch (WorldNotLoadedException ex) {
                Logger.getLogger(Worlds.class.getName()).log(Level.SEVERE, PLUGIN_COMMAND_WORLD_WORLD_NOT_LOADED, ex);
        }
    }

    public static void openWorldMenu(Player p){
        org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(
            InventoryHelper.createInventory(18,
            Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE+ "§dWorlds"),
            CustomItems.npc_gui_disabled.getCustomItem()
        );

        for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
            ItemStack is = PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE);
            ItemMeta im = is.getItemMeta();

            if(im == null){
                return;
            }

            im.setDisplayName(Bukkit.getWorlds().get(i).getName());

            is.setItemMeta(im);
            inv.setItem(i, is);
        }

        InventoryHelper.openInventory(p, inv);
    }

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return new ArrayList<>();
        }

        if(strings.length > 5){
            return new ArrayList<>();
        }

        if(Commands.UNLOAD.getName().equalsIgnoreCase(strings[0])){
            TabCompleterHelper.getWorlds();
        }

        if(strings.length == 2){
            if(Commands.CREATE.getName().equalsIgnoreCase(strings[0])){
                return List.of("<enter name>");
            }
        }

        if(strings.length == 3){
            if(Commands.CREATE.getName().equalsIgnoreCase(strings[0])){
                return TabCompleterHelper.getWorldTypes();
            }
        }

        if(strings.length == 4){
            if(Commands.CREATE.getName().equalsIgnoreCase(strings[0])){
                return TabCompleterHelper.getWorldEnvironmentTypes();
            }
        }

        if(strings.length == 5){
            if(Commands.CREATE.getName().equalsIgnoreCase(strings[0])){
                return List.of("true", "false");
            }
        }





        return TabCompleterHelper.getCommands(Commands.values());
    }

    @Getter
    public enum Commands implements CommandsEnum{
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