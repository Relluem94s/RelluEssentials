package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class TypeHelper {

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean areBlocksMaterial(List<Block> blocks, Material material) {
        boolean isMat = true;
        for (Block b : blocks) {
            if (!b.getType().equals(material)) {
                isMat = false;
            }
        }
        return isMat;
    }
    
    public static boolean isBlockOnOfMaterials(Block block, List<Material> materials) {
        return materials.stream().anyMatch(material -> (block.getType().equals(material)));
    }

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isCMDBlock(CommandSender sender) {
        return sender instanceof BlockCommandSender;
    }

    public static boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }

    public static boolean isMaterialInList(Material material, List<Material> materialList) {
        return materialList.contains(material);
    }

    public static boolean isMaterialInArray(Material material, Material[] materialArray) {
        return isMaterialInList(material, Arrays.asList(materialArray));
    }
}
