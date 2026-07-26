package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.commands.admin.shared.AdminCommandHelper.cleanUpProtections;

public class CleanUpProtectionsCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        if (!Permission.isAuthorized(player, Groups.getGroup("admin").getId())) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
            return;
        }
        cleanUpProtections(player);
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && Admin.Commands.CLEAN_PROTECTIONS.getName().equalsIgnoreCase(args[0]);
    }
}