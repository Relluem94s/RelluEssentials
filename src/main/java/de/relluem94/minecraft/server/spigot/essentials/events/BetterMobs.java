package de.relluem94.minecraft.server.spigot.essentials.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.EntityCoins;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class BetterMobs implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        EntityType et = e.getEntity().getType();
        if (et == EntityType.PHANTOM) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
            int COINS_PER_DEATH = EntityCoins.valueOf(e.getEntity().getType().name()).getCoins();
            if(COINS_PER_DEATH >= 0){
                Player p = e.getEntity().getKiller();
                PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
                pe.setPurse(pe.getPurse() + COINS_PER_DEATH);
                RelluEssentials.dBH.updatePlayer(pe);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format(Strings.PLUGIN_COMMAND_PURSE_TOTAL, String.format("%.2f", pe.getPurse()))));
            }
        }
    }
}