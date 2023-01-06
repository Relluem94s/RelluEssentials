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
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.level.EntityPlayer;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
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

        pe.setUpdatedBy(playerEntryList.get(p.getUniqueId()).getID());
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
        EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(byteBuf);
        PacketPlayOutPlayerListHeaderFooter packetPlayOutPlayerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter(packetDataSerializer);

        packetDataSerializer.a(IChatBaseComponent.ChatSerializer.a(tabHeader));
        packetDataSerializer.a(IChatBaseComponent.ChatSerializer.a(tabFooter));
        packetPlayOutPlayerListHeaderFooter.a(packetDataSerializer);
        entityPlayer.b.a(packetPlayOutPlayerListHeaderFooter, null);
    }
}
