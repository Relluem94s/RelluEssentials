package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;

import de.relluem94.minecraft.server.spigot.essentials.CustomSigns;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HOME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPAWN;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper.isSign;

public class SignClick implements Listener {

    @EventHandler
    public void onChangeSignCreateActionSign(PlayerInteractEvent e) {
        PlayerEntry pe = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
        if (pe.getPlayerState().equals(PlayerState.DEFAULT) && e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && e.getPlayer().isSneaking() == false) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block b =  e.getClickedBlock();
                if (SignHelper.isBlockASign(b)) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if (isSign(CustomSigns.spawn, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                        e.getPlayer().performCommand(PLUGIN_COMMAND_NAME_SPAWN);
                    } else if (isSign(CustomSigns.up, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                        Location l = b.getLocation();
                        int max_height = l.getWorld().getMaxHeight();
                        boolean end_point = false;

                        for(int y = l.getBlockY(); y <= max_height; y++){
                            Block end_sign_block = l.add(0, 1, 0).getBlock();
                            if (SignHelper.isBlockASign(end_sign_block)) {
                                if (isSign(CustomSigns.up, sign.getLine(0), sign.getLine(1), sign.getLine(3)) || isSign(CustomSigns.down, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                                    Location loc = e.getPlayer().getLocation().clone();
                                    loc.setY(y);
                                    Location stand = loc.clone();
                                    stand.add(0,-1,0);
                                    if(!stand.getBlock().getType().equals(Material.AIR)){
                                        end_point = true;
                                        e.getPlayer().teleport(loc, TeleportCause.COMMAND);
                                        break;
                                    }
                                }
                            }
                        }
                        
                        if(!end_point){
                            e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT);
                        }

                    } else if (isSign(CustomSigns.down, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                        Location l = b.getLocation();
                        int min_height = l.getWorld().getMinHeight();
                        boolean end_point = false;
                        for(int y = l.getBlockY(); y >= min_height; y--){
                            Block end_sign_block = l.add(0, -1, 0).getBlock();
                            if (SignHelper.isBlockASign(end_sign_block)) {
                                if (isSign(CustomSigns.up, sign.getLine(0), sign.getLine(1), sign.getLine(3)) || isSign(CustomSigns.down, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                                    Location loc = e.getPlayer().getLocation().clone();
                                    loc.setY(y-2);
                                    Location stand = loc.clone();
                                    stand.add(0,-1,0);
                                    if(!stand.getBlock().getType().equals(Material.AIR)){
                                        end_point = true;
                                        e.getPlayer().teleport(loc, TeleportCause.COMMAND);
                                        break;
                                    }
                                }
                            }
                        }

                        if(!end_point){
                            e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT);
                        }
                        
                    } else if (isSign(CustomSigns.command, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                        e.getPlayer().performCommand(sign.getLine(2));
                    } else if (isSign(CustomSigns.teleport, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                        String[] locationString = sign.getLine(2).split(",");
                        Location location = new Location(e.getPlayer().getWorld(), Integer.parseInt(locationString[0]), Integer.parseInt(locationString[1]), Integer.parseInt(locationString[2]));
                        e.getPlayer().teleport(location);
                    } else if (isSign(CustomSigns.home, sign.getLine(0), sign.getLine(1), sign.getLine(3))) {
                        e.getPlayer().performCommand(PLUGIN_COMMAND_NAME_HOME + " " + sign.getLine(2));
                    }
                }
            }
        }
    }
}