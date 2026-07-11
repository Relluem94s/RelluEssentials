package de.relluem94.minecraft.server.spigot.essentials.commands;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_LIST_ENTRY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_LIST_HEADER;

@CommandName("list")
public class PlayerList implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return new CommandsEnum[0];
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        sender.sendMessage(String.format(PLUGIN_COMMAND_LIST_HEADER, onlinePlayers.size()));
        for (Player player : onlinePlayers) {
            if(sender instanceof Player p){
                if(!p.canSee(player)){
                    continue;
                }
            }
            PlayerEntry pet = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(player.getUniqueId());
            sender.sendMessage(String.format(PLUGIN_COMMAND_LIST_ENTRY,
                    pet.getGroup().getPrefix(), pet.getCustomName(),
                    pet.getGroup().getPrefix(), pet.getGroup().getName()));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}