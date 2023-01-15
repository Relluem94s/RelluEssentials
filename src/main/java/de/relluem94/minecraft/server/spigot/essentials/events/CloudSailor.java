package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.areBlocksMaterial;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper.isInWorld;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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

public class CloudSailor implements Listener {

    //@TODO has to be done in Config (new Table?)
    private final String[] worlds = new String[]{"world", "world_nether", "world_the_end", "lobby"};

    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();
        if (e instanceof Chicken) {
            Random r = new Random();

            int i = r.nextInt(150);

            if (i == 19 || i == 94) {
                event.getDrops().clear();
                event.setDroppedExp(30);
                event.getDrops().add(CustomItems.cloudSailor.getCustomItem());
            }
        }
    }

    @EventHandler
    public void cloudBootsCrafting(PrepareItemCraftEvent e) {
        if (e.getRecipe() != null && e.getRecipe().getResult().hasItemMeta() && CustomItems.cloudBoots.equals(e.getRecipe().getResult())) {
            for (ItemStack is : e.getInventory().getMatrix()) {
                if (is != null) {
                    if (is.hasItemMeta()) {
                        if (!CustomItems.cloudSailor.equals(is)) {
                            e.getInventory().setResult(null);
                        }
                    } else {
                        e.getInventory().setResult(null);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (isInWorld(p, Arrays.asList(worlds))) {
                if (e.getCause().equals(DamageCause.FALL)) {
                    if (p.getInventory().getItemInOffHand().equals(CustomItems.cloudSailor.getCustomItem())) {
                        e.setDamage(e.getDamage() / 2);
                    } else if (p.getInventory().getBoots() != null && p.getInventory().getBoots().equals(CustomItems.cloudBoots.getCustomItem())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSail(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (isInWorld(p, Arrays.asList(worlds))) {
            if (!e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation())) {
                if (p.getInventory().getItemInOffHand().equals(CustomItems.cloudSailor.getCustomItem()) || (p.getInventory().getBoots() != null && p.getInventory().getBoots().equals(CustomItems.cloudBoots.getCustomItem()))) {
                    if (!p.isFlying() && !p.isSneaking()) {

                        List<Block> blocks = new ArrayList<Block>();
                        blocks.add(p.getLocation().getBlock().getRelative(BlockFace.DOWN));
                        blocks.add(blocks.get(0).getRelative(BlockFace.DOWN));

                        blocks.add(blocks.get(0).getRelative(BlockFace.EAST));
                        blocks.add(blocks.get(0).getRelative(BlockFace.NORTH));
                        blocks.add(blocks.get(0).getRelative(BlockFace.SOUTH));
                        blocks.add(blocks.get(0).getRelative(BlockFace.WEST));

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
