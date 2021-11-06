package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.CustomSigns;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPAWN;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SignClick implements Listener {

    @EventHandler
    public void onChangeSignCreateActionSign(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN) || e.getClickedBlock().getType().equals(Material.OAK_SIGN)) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if(sign.getLine(0).equals(CustomSigns.spawn.getLine1()) && sign.getLine(1).equals(CustomSigns.spawn.getLine2()) && sign.getLine(3).equals(CustomSigns.spawn.getLine4())){
                        e.getPlayer().performCommand(PLUGIN_COMMAND_NAME_SPAWN);
                    }
                    else if(sign.getLine(0).equals(CustomSigns.up.getLine1()) && sign.getLine(1).equals(CustomSigns.up.getLine2()) && sign.getLine(3).equals(CustomSigns.up.getLine4())){
                        // GO UP
                    }
                    else if(sign.getLine(0).equals(CustomSigns.down.getLine1()) && sign.getLine(1).equals(CustomSigns.down.getLine2()) && sign.getLine(3).equals(CustomSigns.down.getLine4())){
                        // DOWN
                    }
                    else if(sign.getLine(0).equals(CustomSigns.command.getLine1()) && sign.getLine(1).equals(CustomSigns.command.getLine2()) && sign.getLine(3).equals(CustomSigns.command.getLine4())){
                        e.getPlayer().performCommand(sign.getLine(2));
                    }
                    else if(sign.getLine(0).equals(CustomSigns.teleport.getLine1()) && sign.getLine(1).equals(CustomSigns.teleport.getLine2()) && sign.getLine(3).equals(CustomSigns.teleport.getLine4())){
                        String[] locationString = sign.getLine(2).split(",");
                        Location location = new Location(e.getPlayer().getWorld(), Integer.parseInt(locationString[0]), Integer.parseInt(locationString[1]), Integer.parseInt(locationString[2]));
                        e.getPlayer().teleport(location);
                    }
                    else if(sign.getLine(0).equals(CustomSigns.home.getLine1()) && sign.getLine(1).equals(CustomSigns.home.getLine2()) && sign.getLine(3).equals(CustomSigns.home.getLine4())){
                        e.getPlayer().performCommand(PLUGIN_COMMAND_NAME_HOME + " " + sign.getLine(2));
                    }
                }
            }
        }
    }
}