package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import de.relluem94.minecraft.server.spigot.essentials.CustomSigns;

import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPAWN;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper.isSign;

public class SignClick implements Listener {

    @EventHandler
    public void onChangeSignCreateActionSign(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) || e.getClickedBlock().getType().equals(Material.OAK_SIGN)) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if(isSign(CustomSigns.spawn, sign.getLine(0), sign.getLine(1), sign.getLine(3))){
                        e.getPlayer().performCommand(PLUGIN_COMMAND_NAME_SPAWN);
                    }
                    else if(isSign(CustomSigns.up, sign.getLine(0), sign.getLine(1), sign.getLine(3))){
                        // GO UP
                    }
                    else if(isSign(CustomSigns.down, sign.getLine(0), sign.getLine(1), sign.getLine(3))){
                        // DOWN
                    }
                    else if(isSign(CustomSigns.command, sign.getLine(0), sign.getLine(1), sign.getLine(3))){
                        e.getPlayer().performCommand(sign.getLine(2));
                    }
                    else if(isSign(CustomSigns.teleport, sign.getLine(0), sign.getLine(1), sign.getLine(3))){
                        String[] locationString = sign.getLine(2).split(",");
                        Location location = new Location(e.getPlayer().getWorld(), Integer.parseInt(locationString[0]), Integer.parseInt(locationString[1]), Integer.parseInt(locationString[2]));
                        e.getPlayer().teleport(location);
                    }
                    else if(isSign(CustomSigns.home, sign.getLine(0), sign.getLine(1), sign.getLine(3))){
                        e.getPlayer().performCommand(PLUGIN_COMMAND_NAME_HOME + " " + sign.getLine(2));
                    }
                }
            }
        }
    }
}