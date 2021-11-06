package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.CustomSigns;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignActions implements Listener {

    @EventHandler
    public void onChangeSignCreateActionSign(SignChangeEvent e) {
        if (Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId())) {
            if (e.getLine(1) != null) {
                if (e.getLine(1).equals(CustomSigns.spawn.getSignActionType().getShorthand())) {
                    e.setLine(0, CustomSigns.spawn.getLine1());
                    e.setLine(1, CustomSigns.spawn.getLine2());
                    e.setLine(2, CustomSigns.spawn.getLine3());
                    e.setLine(3, CustomSigns.spawn.getLine4());
                } else if (e.getLine(1).equals(CustomSigns.teleport.getSignActionType().getShorthand())) {
                    e.setLine(0, CustomSigns.teleport.getLine1());
                    e.setLine(1, CustomSigns.teleport.getLine2());

                    e.setLine(3, CustomSigns.teleport.getLine4());
                } else if (e.getLine(1).equals(CustomSigns.home.getSignActionType().getShorthand())) {
                    e.setLine(0, CustomSigns.home.getLine1());
                    e.setLine(1, CustomSigns.home.getLine2());

                    e.setLine(3, CustomSigns.home.getLine4());
                } else if (e.getLine(1).equals(CustomSigns.up.getSignActionType().getShorthand())) {
                    e.setLine(0, CustomSigns.up.getLine1());
                    e.setLine(1, CustomSigns.up.getLine2());
                    e.setLine(2, CustomSigns.up.getLine3());
                    e.setLine(3, CustomSigns.up.getLine4());
                } else if (e.getLine(1).equals(CustomSigns.down.getSignActionType().getShorthand())) {
                    e.setLine(0, CustomSigns.down.getLine1());
                    e.setLine(1, CustomSigns.down.getLine2());
                    e.setLine(2, CustomSigns.down.getLine3());
                    e.setLine(3, CustomSigns.down.getLine4());
                } else if (e.getLine(1).equals(CustomSigns.command.getSignActionType().getShorthand())) {
                    e.setLine(0, CustomSigns.command.getLine1());
                    e.setLine(1, CustomSigns.command.getLine2());

                    e.setLine(3, CustomSigns.command.getLine4());
                }
            }
        }
    }
}
