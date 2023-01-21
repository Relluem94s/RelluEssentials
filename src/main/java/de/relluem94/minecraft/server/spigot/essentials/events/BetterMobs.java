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
            float COINS_PER_DEATH = 5;
            switch(e.getEntity().getType()){
                case ZOMBIE:
                    COINS_PER_DEATH = 10;
                break;
                case CREEPER:
                    COINS_PER_DEATH = 15;
                break;
                case SPIDER:
                    COINS_PER_DEATH = 10;
                break;
                case ENDERMAN:
                    COINS_PER_DEATH = 100;
                break;
                case ENDERMITE:
                    COINS_PER_DEATH = 50;
                break;
                case CAVE_SPIDER:
                    COINS_PER_DEATH = 15;
                break;
                case WITCH:
                    COINS_PER_DEATH = 20;
                break;
                case ENDER_DRAGON:
                    COINS_PER_DEATH = 50000;
                break;
                case WITHER:
                    COINS_PER_DEATH = 10000;
                break;
                case WITHER_SKELETON:
                    COINS_PER_DEATH = 50;
                break;
                case WARDEN:
                    COINS_PER_DEATH = 25000;
                break;
                case SHULKER:
                    COINS_PER_DEATH = 20;
                break;
                case PIGLIN:
                    COINS_PER_DEATH = 30;
                break;

                case SHEEP:
                    COINS_PER_DEATH = 0.1f;
                break;
                case CHICKEN:
                    COINS_PER_DEATH = 0.1f;
                break;
                case COW:
                    COINS_PER_DEATH = 0.1f;
                break;
                case LLAMA:
                    COINS_PER_DEATH = 0.1f;
                break;
                case TURTLE:
                    COINS_PER_DEATH = 0.1f;
                break;

                default:
                    COINS_PER_DEATH = 5;
                break;
            }

            Player p = e.getEntity().getKiller();
            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
            pe.setPurse(pe.getPurse() + COINS_PER_DEATH);
            RelluEssentials.dBH.updatePlayer(pe);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format(Strings.PLUGIN_COMMAND_PURSE_TOTAL, String.format("%.2f", pe.getPurse()))));
        }
    }
}