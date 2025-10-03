package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SPAWN;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper.isSign;

import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
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

public class SignClick implements Listener {

    @EventHandler
    public void onChangeSignCreateActionSign(PlayerInteractEvent e) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer().getUniqueId());
        if (pe.getPlayerState().equals(PlayerState.DEFAULT) && e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && !e.getPlayer().isSneaking()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block b =  e.getClickedBlock();
                if (SignHelper.isBlockASign(b)) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if (isSign(CustomSigns.spawn, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                        e.getPlayer().performCommand(PLUGIN_COMMAND_NAME_SPAWN);
                    } else if (isSign(CustomSigns.up, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                        Location l = b.getLocation();
                        int maxHeight = l.getWorld().getMaxHeight();
                        boolean endPoint = false;

                        for(int y = l.getBlockY(); y <= maxHeight; y++){
                            Block endSignBlock = l.add(0, 1, 0).getBlock();
                            if (SignHelper.isBlockASign(endSignBlock)) {
                                if (isSign(CustomSigns.up, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3)) || isSign(CustomSigns.down, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                                    Location loc = e.getPlayer().getLocation().clone();
                                    loc.setY(y);
                                    Location stand = loc.clone();
                                    stand.add(0,-1,0);
                                    if(!stand.getBlock().getType().equals(Material.AIR)){
                                        endPoint = true;
                                        e.getPlayer().teleport(loc, TeleportCause.COMMAND);
                                        break;
                                    }
                                }
                            }
                        }
                        
                        if(!endPoint){
                            e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT);
                        }

                    } else if (isSign(CustomSigns.down, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                        Location l = b.getLocation();
                        int minHeight = l.getWorld().getMinHeight();
                        boolean endPoint = false;
                        for(int y = l.getBlockY(); y >= minHeight; y--){
                            Block endSignBlock = l.add(0, -1, 0).getBlock();
                            if (SignHelper.isBlockASign(endSignBlock)) {
                                if (isSign(CustomSigns.up, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3)) || isSign(CustomSigns.down, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                                    Location loc = e.getPlayer().getLocation().clone();
                                    loc.setY((double)y-2);
                                    Location stand = loc.clone();
                                    stand.add(0,-1,0);
                                    if(!stand.getBlock().getType().equals(Material.AIR)){
                                        endPoint = true;
                                        e.getPlayer().teleport(loc, TeleportCause.COMMAND);
                                        break;
                                    }
                                }
                            }
                        }

                        if(!endPoint){
                            e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT);
                        }
                        
                    } else if (isSign(CustomSigns.command, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                        e.getPlayer().performCommand(sign.getSide(Side.FRONT).getLine(2));
                    } else if (isSign(CustomSigns.teleport, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                        String[] locationString = sign.getSide(Side.FRONT).getLine(2).split(",");
                        Location location = new Location(e.getPlayer().getWorld(), Integer.parseInt(locationString[0]), Integer.parseInt(locationString[1]), Integer.parseInt(locationString[2]));
                        e.getPlayer().teleport(location);
                    } else if (isSign(CustomSigns.home, sign.getSide(Side.FRONT).getLine(0), sign.getSide(Side.FRONT).getLine(1), sign.getSide(Side.FRONT).getLine(3))) {
                        e.getPlayer().performCommand(AnnotationHelper.getCommandName(Home.class) + " " + sign.getSide(Side.FRONT).getLine(2));
                    }
                }
            }
        }
    }
}