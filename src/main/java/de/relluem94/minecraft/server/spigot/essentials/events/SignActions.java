package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.CustomSigns;
import de.relluem94.minecraft.server.spigot.essentials.helpers.enums.SignActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignActions implements Listener {
    
    @EventHandler
    public void onChangeSignCreateActionSign(SignChangeEvent e) {
        if (e.getLine(1) != null) {
            if(e.getLine(1).equals(SignActionType.SPAWN.getShorthand())) {
                e.setLine(0, CustomSigns.spawn.getLine1());
                e.setLine(1, CustomSigns.spawn.getLine2());
                e.setLine(2, CustomSigns.spawn.getLine3());
                e.setLine(3, CustomSigns.spawn.getLine4());
            }
            else if(e.getLine(1).equals(SignActionType.TELEPORT.getShorthand())) {
                
            }
            else if(e.getLine(1).equals(SignActionType.HOME.getShorthand())) {
                e.setLine(0, CustomSigns.spawn.getLine1());
                e.setLine(1, CustomSigns.spawn.getLine2());
                
                e.setLine(3, CustomSigns.spawn.getLine4());
            }
            else if(e.getLine(1).equals(SignActionType.UP.getShorthand())) {
                e.setLine(0, CustomSigns.spawn.getLine1());
                e.setLine(1, CustomSigns.spawn.getLine2());
                e.setLine(2, CustomSigns.spawn.getLine3());
                e.setLine(3, CustomSigns.spawn.getLine4());
            }
            else if(e.getLine(1).equals(SignActionType.DOWN.getShorthand())) {
                e.setLine(0, CustomSigns.spawn.getLine1());
                e.setLine(1, CustomSigns.spawn.getLine2());
                e.setLine(2, CustomSigns.spawn.getLine3());
                e.setLine(3, CustomSigns.spawn.getLine4());
            }
            else if(e.getLine(1).equals(SignActionType.COMMAND.getShorthand())) {
                e.setLine(0, CustomSigns.spawn.getLine1());
                e.setLine(1, CustomSigns.spawn.getLine2());
                
                e.setLine(3, CustomSigns.spawn.getLine4());
            }
        }
    }
}
