package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author rellu
 */
public class PlayerHelper {

    public static void setFlying(Player p) {
        if (User.getGroup(p).getId() >= Groups.VIP.getId()) {
            if (players.getConfig().getBoolean("player." + p.getUniqueId() + ".fly")) {
                p.setAllowFlight(true);
                if (p.getLocation().getBlock().getRelative(0, -1, 0).getType().equals(Material.AIR)) {
                    p.setFlying(true);
                }
            }
        }
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
