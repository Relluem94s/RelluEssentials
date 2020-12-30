package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class Speed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("speed")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.MOD.getId())) {
                        if (args[0].matches("^\\d+$")) {
                            float speed = parseSpeed(args[0]);
                            if (p.isFlying()) {
                                p.setFlySpeed(speed);
                            } else {
                                p.setWalkSpeed(speed);
                            }
                            p.sendMessage(String.format(PLUGIN_COMMAND_SPEED, args[0]));
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                }
            } else {
                sender.sendMessage(PLUGIN_COMMAND_SPEED_INFO);
            }
        }
        return false;
    }

    
    private float parseSpeed(String arg) {
        int in = Integer.parseInt(arg);

        float speed = 0F;

        switch (in) {
            case 0:
                speed = 0.0F;
                break;
            case 1:
                speed = 0.1F;
                break;
            case 2:
                speed = 0.2F;
                break;
            case 3:
                speed = 0.3F;
                break;
            case 4:
                speed = 0.4F;
                break;
            case 5:
                speed = 0.5F;
                break;
            case 6:
                speed = 0.6F;
                break;
            case 7:
                speed = 0.7F;
                break;
            case 8:
                speed = 0.8F;
                break;
            case 9:
                speed = 0.9F;
                break;
            case 10:
                speed = 1.0F;
                break;
            default:
                speed = 0.1F;
                break;
        }
        return speed;
    }

}
