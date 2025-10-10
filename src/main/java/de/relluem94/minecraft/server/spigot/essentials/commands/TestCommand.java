package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_INVALID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.helpers.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluBoots;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluChestplate;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluHelmet;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluLeggings;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluPickaxe;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluShield;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluSword;

@CommandName("ZAQmNCRXEdwSGU7DvEcXTbBkp2qEaCSSNkQcMhL3m7KSDtmXWaxtbYCaQCFBR96fj")
public class TestCommand implements CommandConstruct {


    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!p.getName().equalsIgnoreCase("Relluem94")) {
            sender.sendMessage(PLUGIN_COMMAND_INVALID);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;   
        }

        if (args.length > 1) {
            sender.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;   
        }

        if (args[0].equalsIgnoreCase(Commands.CUSTOM_MOB.getName())) {
            cm(p.getLocation());
        } else if (args[0].equalsIgnoreCase(Commands.DEV_PLATTFORM.getName())) {
            devPlattform(p);
        } else if (args[0].equalsIgnoreCase(Commands.PICKAXE.getName())) {
            p.getInventory().addItem(new RelluPickaxe().getCustomItem());
        } else if (args[0].equalsIgnoreCase(Commands.CLOUD_SAILOR.getName())) {
            p.getInventory().addItem(CustomItems.cloudSailor.getCustomItem());
            p.getInventory().addItem(CustomItems.cloudBoots.getCustomItem());
        } else if (args[0].equalsIgnoreCase(Commands.RELLU.getName())) {
            rellu(p);
        } else if (args[0].equalsIgnoreCase(Commands.SMELT.getName())) {
            CustomEnchants.autosmelt.addTo(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(Commands.TELE.getName())) {
            CustomEnchants.telekinesis.addTo(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(Commands.NO_ENCHANT.getName())) {
            CustomEnchants.autosmelt.removeFrom(p.getInventory().getItemInMainHand());
            CustomEnchants.telekinesis.removeFrom(p.getInventory().getItemInMainHand());
        } else if (args[0].equalsIgnoreCase(Commands.WORLDS.getName())) {
            worlds(p);
        } else if (args[0].equalsIgnoreCase(Commands.DAMAGE_INFO.getName())) {
            di(p);
        } else if (args[0].equalsIgnoreCase(Commands.PLAYER_STATS.getName())) {
            pl(p);
        } else if (args[0].equalsIgnoreCase(Commands.SKULL.getName())) {
            p.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.BAG_OF_COINS));
            p.getInventory().addItem(PlayerHeadHelper.getCustomSkull(CustomHeads.MONEY_BAG));
        }

        return true;
    }

    private void cm(Location location){
        MobHelper mh = new MobHelper(location, EntityType.ZOMBIE, "§aX Æ A-XII", true);
        mh.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1));
        mh.setCanPickupItems(true);

        mh.spawn(
            new ItemStack(Material.WOODEN_SWORD, 1),
            new ItemStack(Material.SHIELD, 1),
            new ItemStack(Material.LEATHER_HELMET, 1),
            new ItemStack(Material.LEATHER_CHESTPLATE, 1),
            new ItemStack(Material.LEATHER_LEGGINGS, 1),
            new ItemStack(Material.LEATHER_BOOTS, 1)
        );
    }

    private void rellu(@NotNull Player p){
        p.getInventory().addItem(new RelluHelmet().getCustomItem());
        p.getInventory().addItem(new RelluChestplate().getCustomItem());
        p.getInventory().addItem(new RelluLeggings().getCustomItem());
        p.getInventory().addItem(new RelluBoots().getCustomItem());
        p.getInventory().addItem(new RelluShield().getCustomItem());
        p.getInventory().addItem(new RelluSword().getCustomItem());
    }

    private void worlds(Player p){
        try {
            WorldHelper.cloneWorld("world2", "world");
        } catch (WorldNotFoundException ex) {
            Logger.getLogger(TestCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

        World world2 = Bukkit.getWorld("world2");
        if(world2 == null){
            return;
        }

        p.teleport(world2.getSpawnLocation());
    }

    private void di(Player p){
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
        if (pe.getPlayerState().equals(PlayerState.DEFAULT)) {
            pe.setPlayerState(PlayerState.DAMAGE_INFO);
        } else {
            pe.setPlayerState(PlayerState.DEFAULT);
        }
    }


    private static final List<Material> DEV_PLATTFORM_MATERIAL_LIST = Arrays.asList(
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.RAW_IRON_BLOCK,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.RAW_COPPER_BLOCK,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.COAL_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.ANCIENT_DEBRIS,
            Material.NETHER_QUARTZ_ORE,
            Material.NETHER_GOLD_ORE,
            Material.AMETHYST_BLOCK,
            Material.BUDDING_AMETHYST,
            Material.DIRT,
            Material.SAND,
            Material.RED_SAND,
            Material.SOUL_SAND,
            Material.SOUL_SOIL,
            Material.GRAVEL,
            Material.CLAY,
            Material.MUD,
            Material.STONE,
            Material.DEEPSLATE,
            Material.COBBLESTONE,
            Material.COBBLED_DEEPSLATE,
            Material.GRASS_BLOCK,
            Material.TUFF,
            Material.CALCITE,
            Material.BLACKSTONE,
            Material.ANDESITE,
            Material.DIORITE,
            Material.GRANITE,
            Material.END_STONE,
            Material.NETHERRACK,
            Material.GRAY_STAINED_GLASS,
            Material.PUMPKIN,
            Material.MELON,

            Material.FARMLAND
    );

    private void devPlattform(@NotNull Player p){
        World world = p.getWorld();
        Location base = p.getLocation();
        int originX = base.getBlockX();
        int originY = base.getBlockY() - 1;
        int originZ = base.getBlockZ();

        List<ModifyHistoryEntry> undoList = new ArrayList<>();
        BlockHelper frame = new BlockHelper(Material.OCHRE_FROGLIGHT);
        BlockHelper inner = new BlockHelper(Material.BIRCH_PLANKS);
        BlockHelper redstone = new BlockHelper(Material.REDSTONE_BLOCK);
        BlockHelper air = new BlockHelper(Material.AIR);

        int cols = 5;
        int rows = (DEV_PLATTFORM_MATERIAL_LIST.size() + cols - 1) / cols;
        int perTick = 64;
        int placedThisTick = 0;
        long delay = 0L;
        double yaw = p.getLocation().getYaw();
        int fx = 0, fz = 0;
        yaw = ((yaw % 360) + 360) % 360;
        if (yaw >= 315 || yaw < 45) { fx = 0; fz = 1; }
        else if (yaw >= 45 && yaw < 135) { fx = -1; fz = 0; }
        else if (yaw >= 135 && yaw < 225) { fx = 0; fz = -1; }
        else { fx = 1; fz = 0; }
        int rx = fz; int rz = -fx;

        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                int oreIndex = r * cols + c;
                if (oreIndex >= DEV_PLATTFORM_MATERIAL_LIST.size()) break;
                int forwardOffset = r * 5; // statt 4 — volle Reihe, keine Lücke
                int rightOffset = c * 5;
                int cx = originX + fx * forwardOffset + rx * rightOffset;
                int cz = originZ + fz * forwardOffset + rz * rightOffset;
                int cy = originY;
                int startX = cx - 2;
                int startZ = cz - 2;
                int endX = cx + 2;
                int endZ = cz + 2;

                for (int yy = cy; yy <= cy + 14; yy++){
                    for (int xx = startX; xx <= endX; xx++){
                        for (int zz = startZ; zz <= endZ; zz++){
                            if (placedThisTick >= perTick){ delay++; placedThisTick = 0; }
                            Location airLocation = new Location(world, xx, yy, zz);
                            air.addLocation(airLocation, delay);
                            undoList.add(new ModifyHistoryEntry(airLocation, airLocation.getBlock().getType()));
                            placedThisTick++;
                        }
                    }
                }

                for (int dx = -2; dx <= 2; dx++){
                    for (int dz = -2; dz <= 2; dz++){
                        int bx = cx + dx;
                        int bz = cz + dz;
                        if (placedThisTick >= perTick){ delay++; placedThisTick = 0; }

                        if (Math.abs(dx) == 2 || Math.abs(dz) == 2){
                            Location frameLocation = new Location(world, bx, cy, bz);
                            frame.addLocation(frameLocation, delay);
                            undoList.add(new ModifyHistoryEntry(frameLocation, frameLocation.getBlock().getType()));
                            placedThisTick++;
                        } else {
                            if (dx == 0 && dz == 0){
                                Material oreMat = DEV_PLATTFORM_MATERIAL_LIST.get(oreIndex);
                                String blockName = "minecraft:" + oreMat.name().toLowerCase();
                                long schedule = delay;
                                placedThisTick++;
                                new BukkitRunnable(){
                                    @Override
                                    public void run(){
                                        Block b = world.getBlockAt(bx, cy, bz);
                                        undoList.add(new ModifyHistoryEntry(b.getLocation(), b.getType()));
                                        b.setType(Material.REPEATING_COMMAND_BLOCK, true);
                                        if (b.getState() instanceof CommandBlock cb){
                                            cb.setCommand("/setblock ~ ~1 ~ " + blockName);
                                            cb.update(true);
                                        }
                                    }
                                }.runTaskLater(RelluEssentials.getInstance(), schedule);

                                Location redstone_block = world.getBlockAt(bx, cy, bz).getLocation().clone().subtract(0,1,0);
                                redstone.addLocation(redstone_block, schedule);
                                undoList.add(new ModifyHistoryEntry(redstone_block, redstone_block.getBlock().getType()));
                            } else {
                                Location innerLocation = new Location(world, bx, cy, bz);
                                inner.addLocation(innerLocation, delay);
                                undoList.add(new ModifyHistoryEntry(innerLocation, innerLocation.getBlock().getType()));
                                placedThisTick++;
                            }
                        }
                    }
                }
            }
        }

        air.setBlocks();
        frame.setBlocks(9);
        inner.setBlocks(19);
        redstone.setBlocks(29);



        List<List<ModifyHistoryEntry>> playerUndoList = RelluEssentials.getInstance().undo.getOrDefault(p, new ArrayList<>());
        playerUndoList.add(undoList);
        RelluEssentials.getInstance().undo.put(p, playerUndoList);
    }


    private void pl(@NotNull Player p){
        p.sendMessage("Health: " + p.getHealth());
        p.sendMessage("Food: " + p.getFoodLevel());
        p.sendMessage("Exp: " + p.getExp());
    }

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        CUSTOM_MOB("cm"),
        CLOUD_SAILOR("cs"),
        PICKAXE("pick"),
        RELLU("rellu"),
        SMELT("smelt"),
        TELE("tele"),
        NO_ENCHANT("noenchant"),
        WORLDS("worlds"),
        PLAYER_STATS("pl"),
        DAMAGE_INFO("di"),
        SKULL("sk"),
        DEV_PLATTFORM("dp");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("mod").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        if(strings.length == 1){
            tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
            return tabList;
        }

        return tabList;
    }
}