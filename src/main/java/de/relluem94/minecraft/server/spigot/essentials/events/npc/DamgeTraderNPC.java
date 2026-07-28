package de.relluem94.minecraft.server.spigot.essentials.events.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class DamgeTraderNPC implements Listener {

    @EventHandler
    public void onNPCDamage(@NotNull EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Villager)){
            return;
        }

        if(e.getEntity().getCustomName() == null){
            return;
        }

        if(!RelluEssentials.getInstance().getNpcAPI().getNPCNameList().contains(e.getEntity().getCustomName())){
            return;
        }

        if(!(e.getDamager() instanceof Player p)){
            e.setCancelled(true);
            return;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
            e.setCancelled(true);
        }
    }
}
