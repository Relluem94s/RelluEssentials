package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.areBlocksMaterial;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper.isInWorld;

public class CloudSailor implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void mobDeath(@NonNull EntityDeathEvent event) {
        LivingEntity e = event.getEntity();
        if (e instanceof Chicken) {
            int i = random.nextInt(150);

            if (i == 19 || i == 94) {
                event.getDrops().clear();
                event.setDroppedExp(30);
                event.getDrops().add(CustomItems.cloudSailor.getCustomItem());
            }
        }
    }

    @EventHandler
    public void cloudBootsCrafting(@NonNull PrepareItemCraftEvent e) {
        if (e.getRecipe() != null && e.getRecipe().getResult().hasItemMeta() && CustomItems.cloudBoots.equalsExact(e.getRecipe().getResult())) {
            for (ItemStack is : e.getInventory().getMatrix()) {
                if (is != null) {
                    if (is.hasItemMeta()) {
                        if (!CustomItems.cloudSailor.equalsExact(is)) {
                            e.getInventory().setResult(null);
                        }
                    }
                    else {
                        e.getInventory().setResult(null);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFallDamage(@NonNull EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (isInWorld(p, RelluEssentials.getInstance().useCloudsailorWorlds.stream().toList())) {
                if (e.getCause().equals(DamageCause.FALL)) {
                    if (p.getInventory().getBoots() != null && p.getInventory().getBoots().equals(CustomItems.cloudBoots.getCustomItem())) {
                        e.setCancelled(true);
                    }
                    else if (p.getInventory().getItemInOffHand().equals(CustomItems.cloudSailor.getCustomItem())) {
                        e.setDamage(e.getDamage() / 2);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSail(@NonNull PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (isInWorld(p, RelluEssentials.getInstance().useCloudsailorWorlds.stream().toList())) {
            if (e.getTo() != null && !e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation())) {
                if (p.getInventory().getItemInOffHand().equals(CustomItems.cloudSailor.getCustomItem()) || (p.getInventory().getBoots() != null && p.getInventory().getBoots().equals(CustomItems.cloudBoots.getCustomItem()))) {
                    if (!p.isFlying() && !p.isSneaking()) {

                        List<Block> blocks = new ArrayList<>();
                        blocks.add(p.getLocation().getBlock().getRelative(BlockFace.DOWN));
                        blocks.add(blocks.getFirst().getRelative(BlockFace.DOWN));

                        blocks.add(blocks.getFirst().getRelative(BlockFace.EAST));
                        blocks.add(blocks.getFirst().getRelative(BlockFace.NORTH));
                        blocks.add(blocks.getFirst().getRelative(BlockFace.SOUTH));
                        blocks.add(blocks.getFirst().getRelative(BlockFace.WEST));

                        if (areBlocksMaterial(blocks, Material.AIR)) {
                            Vector dir = p.getLocation().getDirection().multiply(0.5);
                            Vector vec = new Vector(dir.getX(), -0.001D, dir.getZ());
                            p.setVelocity(vec);
                            p.getWorld().playEffect(p.getLocation(), Effect.BAT_TAKEOFF, 1);
                        }
                    }
                }
            }
        }
    }
}