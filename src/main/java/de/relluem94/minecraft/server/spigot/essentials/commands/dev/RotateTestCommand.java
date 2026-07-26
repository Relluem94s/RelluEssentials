package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NonNull;

public class RotateTestCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        Location startLocation = player.getLocation().clone();

        RelluEssentials.getInstance().position.put(player, new DoubleStore<>(
                startLocation.clone().add(1, 0, 0),
                startLocation.clone().add(4, 2, 8)
        ));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.performCommand("modify set stone");
            }
        }.runTaskLater(RelluEssentials.getInstance(), 0L);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.performCommand("modify copy");
            }
        }.runTaskLater(RelluEssentials.getInstance(), 10L);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.performCommand("modify clipboard rotate");
            }
        }.runTaskLater(RelluEssentials.getInstance(), 11L);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(player.getLocation().clone().add(10, 0, 20));
            }
        }.runTaskLater(RelluEssentials.getInstance(), 12L);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.performCommand("modify paste");
            }
        }.runTaskLater(RelluEssentials.getInstance(), 13L);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("Clean Up in 400 ticks.");
            }
        }.runTaskLater(RelluEssentials.getInstance(), 140L);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("Clean Up in 200 ticks..");
            }
        }.runTaskLater(RelluEssentials.getInstance(), 340L);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("Clean Up...");
                player.teleport(startLocation);
                player.performCommand("modify undo");
                player.performCommand("modify undo");
            }
        }.runTaskLater(RelluEssentials.getInstance(), 540L);
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.ROTATE_TEST.getName().equalsIgnoreCase(args[0]);
    }
}