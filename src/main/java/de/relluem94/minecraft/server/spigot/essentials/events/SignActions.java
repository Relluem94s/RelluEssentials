package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import de.relluem94.minecraft.server.spigot.essentials.CustomSigns;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper.isSign;

public class SignActions implements Listener {

    @EventHandler
    public void onChangeSignCreateActionSign(SignChangeEvent e) {
        if (Permission.isAuthorized(e.getPlayer(), Groups.getGroup("mod").getId())) {
            if (e.getLine(1) != null) {
                if (isSign(CustomSigns.spawn, e.getLine(1))) {
                    e.setLine(0, CustomSigns.spawn.getLine0());
                    e.setLine(1, CustomSigns.spawn.getLine1());
                    e.setLine(2, CustomSigns.spawn.getLine2());
                    e.setLine(3, CustomSigns.spawn.getLine3());
                } else if (isSign(CustomSigns.teleport, e.getLine(1))) {
                    e.setLine(0, CustomSigns.teleport.getLine0());
                    e.setLine(1, CustomSigns.teleport.getLine1());

                    e.setLine(3, CustomSigns.teleport.getLine3());
                } else if (isSign(CustomSigns.home, e.getLine(1))) {
                    e.setLine(0, CustomSigns.home.getLine0());
                    e.setLine(1, CustomSigns.home.getLine1());

                    e.setLine(3, CustomSigns.home.getLine3());
                } else if (isSign(CustomSigns.up, e.getLine(1))) {
                    e.setLine(0, CustomSigns.up.getLine0());
                    e.setLine(1, CustomSigns.up.getLine1());
                    e.setLine(2, CustomSigns.up.getLine2());
                    e.setLine(3, CustomSigns.up.getLine3());
                } else if (isSign(CustomSigns.down, e.getLine(1))) {
                    e.setLine(0, CustomSigns.down.getLine0());
                    e.setLine(1, CustomSigns.down.getLine1());
                    e.setLine(2, CustomSigns.down.getLine2());
                    e.setLine(3, CustomSigns.down.getLine3());
                } else if (isSign(CustomSigns.command, e.getLine(1))) {
                    e.setLine(0, CustomSigns.command.getLine0());
                    e.setLine(1, CustomSigns.command.getLine1());

                    e.setLine(3, CustomSigns.command.getLine3());
                }
            }
        }
    }
}
