package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.services.tasks.BlockService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command that generates a developer testing platform around the executing player.
 *
 * <p>The platform consists of a grid of 5x5 cells, each showcasing either a specific
 * {@link Material} via a repeating command block setup or a {@link TraderNpc} spawn point.
 * Blocks are placed asynchronously in batches to avoid server lag. All placed blocks are
 * recorded in the undo history to allow reverting the operation.
 */
public class DevPlattformCommand implements SubCommand {

  private static final List<Material> DEV_PLATTFORM_MATERIAL_LIST = Arrays.asList(Material.IRON_ORE,
      Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON_BLOCK, Material.COPPER_ORE,
      Material.DEEPSLATE_COPPER_ORE, Material.RAW_COPPER_BLOCK, Material.DIAMOND_ORE,
      Material.DEEPSLATE_DIAMOND_ORE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
      Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.GOLD_ORE,
      Material.DEEPSLATE_GOLD_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
      Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.ANCIENT_DEBRIS,
      Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.AMETHYST_BLOCK,
      Material.BUDDING_AMETHYST, Material.DIRT, Material.SAND, Material.RED_SAND,
      Material.SOUL_SAND, Material.SOUL_SOIL, Material.GRAVEL, Material.CLAY, Material.MUD,
      Material.STONE, Material.DEEPSLATE, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE,
      Material.GRASS_BLOCK, Material.TUFF, Material.CALCITE, Material.BLACKSTONE, Material.ANDESITE,
      Material.DIORITE, Material.GRANITE, Material.END_STONE, Material.NETHERRACK,
      Material.GRAY_STAINED_GLASS, Material.PUMPKIN, Material.MELON, Material.PALE_OAK_LOG);
  private final ServiceContext serviceContext;

  /**
   * Creates a new {@link DevPlattformCommand} with the given service context.
   *
   * @param context the {@link ServiceContext} providing access to required services such as
   *                the scheduler, trader NPC service, undo history service and plugin metadata
   */
  public DevPlattformCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Generates the developer platform centered at the executing player's current location.
   *
   * <p>Builds a grid of cells oriented relative to the player's facing direction.
   * Each cell either places a repeating command block that sets the showcased {@link Material}
   * one block above, backed by a redstone block, or spawns a {@link TraderNpc} once all
   * materials have been displayed. All modified block states are recorded for undo support.
   *
   * @param player the player who triggered the command and whose location and facing direction
   *               determine the platform's origin and orientation
   * @param args   the command arguments passed to this sub-command
   */
  @Override
  public void execute(Player player, String[] args) {
    World world = player.getWorld();
    Location base = player.getLocation();
    int originX = base.getBlockX();
    int originY = base.getBlockY() - 1;
    int originZ = base.getBlockZ();

    List<ModifyHistoryEntry> undoList = new ArrayList<>();

    BlockService frame = new BlockService(serviceContext.getSchedulerService(),
        Material.OCHRE_FROGLIGHT,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    BlockService inner = new BlockService(serviceContext.getSchedulerService(),
        Material.BIRCH_PLANKS,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    BlockService redstone = new BlockService(serviceContext.getSchedulerService(),
        Material.REDSTONE_BLOCK,
        serviceContext.getPluginMetadataService().getPlugin().getServer());
    BlockService air = new BlockService(serviceContext.getSchedulerService(), Material.AIR,
        serviceContext.getPluginMetadataService().getPlugin().getServer());

    List<TraderNpc> traderNpcs = serviceContext.getTraderNpcService().getAllNpcs();

    int npcIndex = 0;
    int cols = 5;
    int npcCount = traderNpcs.size();
    int totalBlocks = DEV_PLATTFORM_MATERIAL_LIST.size();
    int totalAmount = totalBlocks + npcCount;
    int rows = (totalAmount + cols) / cols;

    int perTick = 64;
    int placedThisTick = 0;
    long schedule = 0L;

    double yaw = ((player.getLocation().getYaw() % 360) + 360) % 360;
    int fx;
    int fz;
    if (yaw >= 315 || yaw < 45) {
      fx = 0;
      fz = 1;
    } else if (yaw >= 45 && yaw < 135) {
      fx = -1;
      fz = 0;
    } else if (yaw >= 135 && yaw < 225) {
      fx = 0;
      fz = -1;
    } else {
      fx = 1;
      fz = 0;
    }
    int rx = fz;
    int rz = -fx;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        int oreIndex = r * cols + c;
        if (oreIndex >= totalAmount) {
          break;
        }

        int forwardOffset = r * 5;
        int rightOffset = c * 5;
        int cx = originX + fx * forwardOffset + rx * rightOffset;
        int cz = originZ + fz * forwardOffset + rz * rightOffset;
        int startX = cx - 2;
        int startZ = cz - 2;
        int endX = cx + 2;
        int endZ = cz + 2;

        for (int yy = originY; yy <= originY + 14; yy++) {
          for (int xx = startX; xx <= endX; xx++) {
            for (int zz = startZ; zz <= endZ; zz++) {
              if (placedThisTick >= perTick) {
                schedule++;
                placedThisTick = 0;
              }
              Location airLocation = new Location(world, xx, yy, zz);
              air.addLocation(airLocation, schedule);
              undoList.add(new ModifyHistoryEntry(airLocation, airLocation.getBlock().getType(),
                  airLocation.getBlock().getBlockData()));
              placedThisTick++;
            }
          }
        }

        for (int dx = -2; dx <= 2; dx++) {
          for (int dz = -2; dz <= 2; dz++) {
            int bx = cx + dx;
            int bz = cz + dz;
            if (placedThisTick >= perTick) {
              schedule++;
              placedThisTick = 0;
            }

            if (Math.abs(dx) == 2 || Math.abs(dz) == 2) {
              Location frameLocation = new Location(world, bx, originY, bz);
              frame.addLocation(frameLocation, schedule);
              undoList.add(new ModifyHistoryEntry(frameLocation, frameLocation.getBlock().getType(),
                  frameLocation.getBlock().getBlockData()));
              placedThisTick++;
            } else {
              if (dx == 0 && dz == 0) {
                if (DEV_PLATTFORM_MATERIAL_LIST.size() <= oreIndex) {
                  Block b = world.getBlockAt(bx, originY, bz);
                  undoList.add(
                      new ModifyHistoryEntry(b.getLocation(), b.getType(), b.getBlockData()));
                  inner.addLocation(b.getLocation(), schedule);

                  NpcHelper nh = new NpcHelper(world.getBlockAt(bx, originY + 1, bz).getLocation(),
                      traderNpcs.get(npcIndex));
                  serviceContext.getSchedulerService().runTaskLater(nh::spawn, schedule + 11);

                  npcIndex++;
                  continue;
                }

                Material oreMaterial = DEV_PLATTFORM_MATERIAL_LIST.get(oreIndex);
                String blockName = "minecraft:" + oreMaterial.name().toLowerCase();
                placedThisTick++;

                serviceContext.getSchedulerService().runTaskLater(() -> {
                  Block b = world.getBlockAt(bx, originY, bz);
                  undoList.add(
                      new ModifyHistoryEntry(b.getLocation(), b.getType(), b.getBlockData()));
                  b.setType(Material.REPEATING_COMMAND_BLOCK, true);
                  if (b.getState() instanceof CommandBlock cb) {
                    cb.setCommand("/setblock ~ ~1 ~ " + blockName);
                    cb.update(true);
                  }
                }, schedule);

                Location redstoneLocation = world.getBlockAt(bx, originY, bz).getLocation().clone()
                    .subtract(0, 1, 0);
                redstone.addLocation(redstoneLocation, schedule);
                undoList.add(
                    new ModifyHistoryEntry(redstoneLocation, redstoneLocation.getBlock().getType(),
                        redstoneLocation.getBlock().getBlockData()));
              } else {
                Location innerLocation = new Location(world, bx, originY, bz);
                inner.addLocation(innerLocation, schedule);
                undoList.add(
                    new ModifyHistoryEntry(innerLocation, innerLocation.getBlock().getType(),
                        innerLocation.getBlock().getBlockData()));
                placedThisTick++;
              }
            }
          }
        }
      }
    }

    air.applyBlocks();
    frame.applyBlocks(5);
    inner.applyBlocks(10);
    redstone.applyBlocks(15);

    serviceContext.getUndoHistoryService().addHistory(player, undoList);
  }

  /**
   * Determines whether the given arguments match this sub-command.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if exactly one argument is provided and it equals the
   *         {@link DevCommand.Commands#DEV_PLATTFORM}
   *         command name, ignoring case; {@code false} otherwise
   */
  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && DevCommand.Commands.DEV_PLATTFORM.getName()
        .equalsIgnoreCase(args[0]);
  }
}