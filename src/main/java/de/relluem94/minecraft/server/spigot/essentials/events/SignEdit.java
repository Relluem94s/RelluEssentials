package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class SignEdit implements Listener {

    @EventHandler
    public void onChangeSignEditSign(PlayerInteractEvent e) {
        PlayerEntry pe = PlayerAPI.getPlayerEntry(e.getPlayer().getUniqueId());
        if (!pe.getPlayerState().equals(PlayerState.DEFAULT) && e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && !e.getPlayer().isSneaking()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block b =  e.getClickedBlock();
                if (SignHelper.isBlockASign(b)) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if(pe.getPlayerState().equals(PlayerState.SIGNEDIT)){
                        e.getPlayer().openSign(sign);
                        pe.setPlayerState(PlayerState.DEFAULT);

                        e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_EDIT);
                    }
                    else if(pe.getPlayerState().equals(PlayerState.SIGNCOPY)){
                        pe.setPlayerStateParameter(sign);
                        pe.setPlayerState(PlayerState.SIGNPASTE);
                        e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_COPY);
                        e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_COPY_TO_PASTE_MESSAGE);
                    }
                    else if(pe.getPlayerState().equals(PlayerState.SIGNPASTE)){
                        if(pe.getPlayerStateParameter() instanceof Sign){
                            Sign copiedSign = (Sign) pe.getPlayerStateParameter();
                            sign.setLine(0, copiedSign.getLine(0));
                            sign.setLine(1, copiedSign.getLine(1));
                            sign.setLine(2, copiedSign.getLine(2));
                            sign.setLine(3, copiedSign.getLine(3));
                            sign.update();

                            e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_PASTE);
                        }
                        pe.setPlayerState(PlayerState.DEFAULT);
                    }
                    
                }
            }
        }
    }
}
