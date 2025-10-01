package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.SignHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class SignEdit implements Listener {

    @EventHandler
    public void onChangeSignEditSign(PlayerInteractEvent e) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer().getUniqueId());
        if (!pe.getPlayerState().equals(PlayerState.DEFAULT) && e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && !e.getPlayer().isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
                Block b =  e.getClickedBlock();
                if (SignHelper.isBlockASign(b)) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if(pe.getPlayerState().equals(PlayerState.SIGN_EDIT)){
                        e.getPlayer().openSign(sign);
                        pe.setPlayerState(PlayerState.DEFAULT);

                        e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_EDIT);
                    }
                    else if(pe.getPlayerState().equals(PlayerState.SIGN_COPY)){
                        pe.setPlayerStateParameter(sign);
                        pe.setPlayerState(PlayerState.SIGN_PASTE);
                        e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_COPY);
                        e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_COPY_TO_PASTE_MESSAGE);
                    }
                    else if(pe.getPlayerState().equals(PlayerState.SIGN_PASTE)){
                        if(pe.getPlayerStateParameter() instanceof Sign){
                            updateSign(sign, (Sign) pe.getPlayerStateParameter());
                            e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_SIGN_PASTE);
                        }
                        pe.setPlayerState(PlayerState.DEFAULT);
                    }
                }
            
        }
    }

    private void updateSign(Sign sign, Sign copiedSign){
        sign.getSide(Side.FRONT).setLine(0, copiedSign.getSide(Side.FRONT).getLine(0));
        sign.getSide(Side.FRONT).setLine(1, copiedSign.getSide(Side.FRONT).getLine(1));
        sign.getSide(Side.FRONT).setLine(2, copiedSign.getSide(Side.FRONT).getLine(2));
        sign.getSide(Side.FRONT).setLine(3, copiedSign.getSide(Side.FRONT).getLine(3));
        sign.update();
    }
}
