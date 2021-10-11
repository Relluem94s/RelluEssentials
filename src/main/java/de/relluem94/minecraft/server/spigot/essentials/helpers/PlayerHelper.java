package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_AFK;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_AFK_ACTIVATED;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_AFK_DEACTIVATED;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class PlayerHelper {

    /**
     *
     * @param p Player to set Flying
     */
    public static void setFlying(Player p) {
        if (User.getGroup(p).getId() >= Groups.getGroup("vip").getId()) {
            PlayerEntry pe = playerEntryList.get(p.getUniqueId());
            if (pe.isFlying()) {
                p.setAllowFlight(true);
                if (p.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.AIR)) {
                    p.setFlying(true);
                }
            }
        }
    }

    /**
     * 
     * @param p Player
     * @param join Boolean
     * @return Boolean
     */
    public static boolean setAFK(Player p, boolean join) {
        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
        boolean isAFK = pe.isAFK();

        if (!join) {
            Bukkit.broadcastMessage(String.format(PLUGIN_COMMAND_AFK, p.getCustomName(), !isAFK ? PLUGIN_COMMAND_AFK_ACTIVATED : PLUGIN_COMMAND_AFK_DEACTIVATED));
            isAFK = !isAFK; // Invert for single invertion ^_^
        }
        
        pe.setUpdatedby(playerEntryList.get(p.getUniqueId()).getId());
        pe.setAFK(isAFK);
        dBH.updatePlayer(pe);

        p.setInvulnerable(isAFK);
        p.setPlayerListName((isAFK ? "§c[AFK] " : "") + p.getCustomName());
        return true;
    }

    /**
     *
     * @param p Player to send TabList
     * @param header String Header Text
     * @param footer String Footer Text
     */
    public static void sendTablist(Player p, String header, String footer) {
        IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\":\"" + footer + "\"}");
        EntityPlayer eP = ((CraftPlayer) p).getHandle();
        ByteBuf bB = ByteBufAllocator.DEFAULT.buffer();
        PacketDataSerializer pDA = new PacketDataSerializer(bB);
        PacketPlayOutPlayerListHeaderFooter pPOPLHF = new PacketPlayOutPlayerListHeaderFooter();
        try {
            pDA.a(IChatBaseComponent.ChatSerializer.a(tabHeader));
            pDA.a(IChatBaseComponent.ChatSerializer.a(tabFooter));
            pPOPLHF.a(pDA);
            eP.playerConnection.sendPacket(pPOPLHF);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
