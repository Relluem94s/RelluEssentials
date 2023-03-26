package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.ProtectionAPI;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLock;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MARRY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MARRY_ACCEPT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MARRY_DIVORCE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Marry implements CommandExecutor {

    HashMap<Player, Player> marryAcceptList = new HashMap<>();

    private void addMarryEntry(Player player, Player target){
        if(PlayerAPI.getPlayerEntry(player).getPartner() != null || PlayerAPI.getPlayerEntry(target).getPartner() != null){
            player.sendMessage(PLUGIN_COMMAND_MARRY_REQUEST_IS_MAARIED);
            return;
        }

        player.sendMessage(String.format(PLUGIN_COMMAND_MARRY_SEND_REQUEST, target.getCustomName()));

        marryAcceptList.put(target, player);
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), () -> {
            if(hasMarryEntry(target)){
                player.sendMessage(PLUGIN_COMMAND_MARRY_REQUEST_EXPIRED);
                target.sendMessage(PLUGIN_COMMAND_MARRY_REQUEST_EXPIRED);
                removeMarryEntry(target);
            }
        }, 20*60*2L);
    }

    private boolean hasMarryEntry(Player target) {
        return marryAcceptList.containsKey(target);
    }

    private void removeMarryEntry(Player target) {
        marryAcceptList.remove(target);
    }


    public void marry(Player player, Player target){
        target.sendMessage(String.format(PLUGIN_COMMAND_MARRY_MARRIED, player.getCustomName()));
        player.sendMessage(String.format(PLUGIN_COMMAND_MARRY_MARRIED, target.getCustomName()));

        PlayerEntry firstPlayer = PlayerAPI.getPlayerEntry(target);
        PlayerEntry secondPlayer = PlayerAPI.getPlayerEntry(player);

        PlayerPartnerEntry playerPartnerEntry = new PlayerPartnerEntry();
        playerPartnerEntry.setCreatedby(firstPlayer.getID());
        playerPartnerEntry.setFirstPlayerID(firstPlayer.getID());
        playerPartnerEntry.setSecondPlayerID(secondPlayer.getID());

        RelluEssentials.getInstance().getDatabaseHelper().insertPlayerPartner(playerPartnerEntry);
        playerPartnerEntry = PlayerAPI.getPartner(firstPlayer);

        firstPlayer.setPartner(playerPartnerEntry);
        secondPlayer.setPartner(playerPartnerEntry);

        Collection<ProtectionEntry> protectionEntryList = new ArrayList<> ();
        protectionEntryList.addAll(ProtectionAPI.getProtectionEntryList().values());

        for(ProtectionEntry pre : protectionEntryList){ 
            if(pre.getCreatedBy() == firstPlayer.getID()){
                BetterLock.addRight(target, pre, secondPlayer.getID(), true);
            }

            if(pre.getCreatedBy() == secondPlayer.getID()){
                BetterLock.addRight(player, pre, firstPlayer.getID(), true);
            }
            
        }
        
    }



    private void divorce(PlayerEntry pe) {
        PlayerPartnerEntry ppe = pe.getPartner();

        PlayerEntry secondPlayerEntry = PlayerAPI.getPlayerEntry((ppe.getSecondPlayerID() != pe.getID() ? ppe.getSecondPlayerID() : ppe.getFirstPlayerID()));

        Player firstPlayer = Bukkit.getPlayer(UUID.fromString(pe.getUUID()));
        OfflinePlayer secondOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(secondPlayerEntry.getUUID()));

        if(firstPlayer != null && secondOfflinePlayer != null){
            if(secondOfflinePlayer.isOnline()){
                Player secondPlayer = Bukkit.getPlayer(secondOfflinePlayer.getName());
                firstPlayer.sendMessage(String.format(PLUGIN_COMMAND_MARRY_DIVORCED, secondPlayer.getCustomName()));
                secondPlayer.sendMessage(String.format(PLUGIN_COMMAND_MARRY_DIVORCED, firstPlayer.getCustomName()));
            }
            else{
                firstPlayer.sendMessage(String.format(PLUGIN_COMMAND_MARRY_DIVORCED, secondOfflinePlayer.getName()));
            }

            ppe.setDeletedBy(pe.getID());
            pe.setPartner(null);
            secondPlayerEntry.setPartner(null);
        
            RelluEssentials.getInstance().getDatabaseHelper().deletePlayerPartner(ppe);

            Collection<ProtectionEntry> protectionEntryList = new ArrayList<> ();
            protectionEntryList.addAll(ProtectionAPI.getProtectionEntryList().values());

            for(ProtectionEntry pre : protectionEntryList){
                if(pre.getCreatedBy() == pe.getID()){
                    BetterLock.removeRight(firstPlayer, pre, secondPlayerEntry.getID(), true);
                }
    
                if(pre.getCreatedBy() == secondPlayerEntry.getID()){
                    BetterLock.removeRight(secondOfflinePlayer, pre, pe.getID());
                }
                
            }
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_MARRY)) {
            return false;
        }

        Player p = null;
        if (isPlayer(sender)) {
            p = (Player) sender;
        }

        if(p == null){
            return false;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(PLUGIN_COMMAND_MARRY_INFO);
            return true;
        }
        
        if (args.length == 1) {
            if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_MARRY_ACCEPT)){
                if(hasMarryEntry(p)){
                    marry(p, marryAcceptList.get(p));
                    removeMarryEntry(p);
                    return true;
                }

                p.sendMessage(PLUGIN_COMMAND_MARRY_ACCEPT_NO_REQUEST);
                return true;
            }

            if(args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_MARRY_DIVORCE)){
                PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
                if(PlayerAPI.getPlayerEntry(p).getPartner() != null){
                    divorce(pe);
                    return true;
                }

                p.sendMessage(PLUGIN_COMMAND_MARRY_DIVORCE_NOT_MARRIED);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
                return true;
            }

            if(target.getName().equalsIgnoreCase(p.getName())){
                p.sendMessage(PLUGIN_COMMAND_MARRY_SELF_MARRIGE);
                return true;
            }

            addMarryEntry(p, target);
            
            return true;
        }

        p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
        return true;
    }
}