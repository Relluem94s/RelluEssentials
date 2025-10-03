package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.CustomSigns;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SignClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChangeSignCreateActionSign(@NotNull PlayerInteractEvent e) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer().getUniqueId());
        if (!pe.getPlayerState().equals(PlayerState.DEFAULT) || e.getHand() == null || !e.getHand().equals(EquipmentSlot.HAND) || e.getPlayer().isSneaking()) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block b = e.getClickedBlock();
        if (b == null || !SignHelper.isBlockASign(b)) {
            return;
        }

        cancelInteraction(e);

        Sign sign = (Sign) b.getState();
        final Player player = e.getPlayer();
        final String[] frontLines = new String[] {
                sign.getSide(Side.FRONT).getLine(0),
                sign.getSide(Side.FRONT).getLine(1),
                sign.getSide(Side.FRONT).getLine(2),
                sign.getSide(Side.FRONT).getLine(3)
        };

        if (SignHelper.isSign(CustomSigns.spawn, frontLines[0], frontLines[1], frontLines[3])) {
            player.performCommand(CommandNameConstants.PLUGIN_COMMAND_NAME_SPAWN);
            return;
        }

        if (SignHelper.isSign(CustomSigns.up, frontLines[0], frontLines[1], frontLines[3])) {
            Location l = b.getLocation();
            if (l.getWorld() == null) return;
            int maxHeight = l.getWorld().getMaxHeight();
            boolean endPoint = false;
            for (int y = l.getBlockY(); y <= maxHeight; y++) {
                Block endSignBlock = l.add(0, 1, 0).getBlock();
                if (SignHelper.isBlockASign(endSignBlock)) {
                    if (SignHelper.isSign(CustomSigns.up, frontLines[0], frontLines[1], frontLines[3])
                            || SignHelper.isSign(CustomSigns.down, frontLines[0], frontLines[1], frontLines[3])) {
                        Location loc = player.getLocation().clone();
                        loc.setY(y);
                        Location stand = loc.clone();
                        stand.add(0, -1, 0);
                        if (!stand.getBlock().getType().equals(Material.AIR)) {
                            endPoint = true;
                            player.teleport(loc, TeleportCause.COMMAND);
                            break;
                        }
                    }
                }
            }
            if (!endPoint) {
                player.sendMessage(EventConstants.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT);
            }
            return;
        }

        if (SignHelper.isSign(CustomSigns.down, frontLines[0], frontLines[1], frontLines[3])) {
            Location l = b.getLocation();
            if (l.getWorld() == null) return;
            int minHeight = l.getWorld().getMinHeight();
            boolean endPoint = false;
            for (int y = l.getBlockY(); y >= minHeight; y--) {
                Block endSignBlock = l.add(0, -1, 0).getBlock();
                if (SignHelper.isBlockASign(endSignBlock)) {
                    if (SignHelper.isSign(CustomSigns.up, frontLines[0], frontLines[1], frontLines[3])
                            || SignHelper.isSign(CustomSigns.down, frontLines[0], frontLines[1], frontLines[3])) {
                        Location loc = player.getLocation().clone();
                        loc.setY((double) y - 2);
                        Location stand = loc.clone();
                        stand.add(0, -1, 0);
                        if (!stand.getBlock().getType().equals(Material.AIR)) {
                            endPoint = true;
                            player.teleport(loc, TeleportCause.COMMAND);
                            break;
                        }
                    }
                }
            }
            if (!endPoint) {
                player.sendMessage(EventConstants.PLUGIN_EVENT_SIGN_UP_OR_DOWN_NO_END_POINT);
            }
            return;
        }

        if (SignHelper.isSign(CustomSigns.command, frontLines[0], frontLines[1], frontLines[3])) {
            player.performCommand(frontLines[2]);
            return;
        }

        if (SignHelper.isSign(CustomSigns.teleport, frontLines[0], frontLines[1], frontLines[3])) {
            String[] locationString = frontLines[2].split(",");
            Location location = new Location(player.getWorld(),
                    Integer.parseInt(locationString[0]),
                    Integer.parseInt(locationString[1]),
                    Integer.parseInt(locationString[2]));
            player.teleport(location);
            return;
        }

        if (SignHelper.isSign(CustomSigns.home, frontLines[0], frontLines[1], frontLines[3])) {
            player.performCommand(AnnotationHelper.getCommandName(Home.class) + " " + frontLines[2]);
        }
    }

    private void cancelInteraction(@NotNull PlayerInteractEvent e) {
        e.setCancelled(true);
        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.DENY);
    }
}
