package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_COLOR;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_SPACER;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
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
        if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            PlayerEntry pe = playerEntryList.get(p.getUniqueId());
            if (pe.isFlying()) {
                p.setAllowFlight(true);
                p.setFlying(true);
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
            Bukkit.broadcastMessage(PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + String.format(getText(!isAFK ? "PLUGIN_COMMAND_AFK_ACTIVATED" : "PLUGIN_COMMAND_AFK_DEACTIVATED"), p.getCustomName() + "§f", !isAFK ? "§c" : "§a"));
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

        pDA.a(IChatBaseComponent.ChatSerializer.a(tabHeader));
        pDA.a(IChatBaseComponent.ChatSerializer.a(tabFooter));
        try {
            pPOPLHF.a(pDA);
        } catch (IOException ex) {
            Logger.getLogger(PlayerHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        eP.playerConnection.sendPacket(pPOPLHF);

    }
}
