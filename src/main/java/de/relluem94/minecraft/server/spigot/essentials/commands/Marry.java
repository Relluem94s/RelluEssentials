package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_ACCEPT_NO_REQUEST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_DIVORCED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_DIVORCE_NOT_MARRIED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_INFO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_MARRIED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_RECEIVE_REQUEST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_REQUEST_EXPIRED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_REQUEST_IS_MARRIED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_SELF_MARRIAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_MARRY_SEND_REQUEST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import java.util.*;

import de.relluem94.minecraft.server.spigot.essentials.helpers.TabCompleterHelper;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterLock;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import org.jetbrains.annotations.Nullable;

@CommandName("marry")
public class Marry implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return Admin.Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {

        ACCEPT("accept"),
        DIVORCE("divorce");

        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }

    private final HashMap<Player, Player> marryAcceptList = new HashMap<>();

    private void addMarryEntry(Player player, Player target){
        if(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(player).getPartner() != null || RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target).getPartner() != null){
            player.sendMessage(PLUGIN_COMMAND_MARRY_REQUEST_IS_MARRIED);
            return;
        }

        player.sendMessage(String.format(PLUGIN_COMMAND_MARRY_SEND_REQUEST, target.getCustomName()));
        target.sendMessage(String.format(PLUGIN_COMMAND_MARRY_RECEIVE_REQUEST, player.getCustomName()));

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


    public void marry(@NotNull Player player, @NotNull Player target){
        target.sendMessage(String.format(PLUGIN_COMMAND_MARRY_MARRIED, player.getCustomName()));
        player.sendMessage(String.format(PLUGIN_COMMAND_MARRY_MARRIED, target.getCustomName()));

        PlayerEntry firstPlayer = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target);
        PlayerEntry secondPlayer = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(player);

        PlayerPartnerEntry playerPartnerEntry = new PlayerPartnerEntry();
        playerPartnerEntry.setCreatedBy(firstPlayer.getId());
        playerPartnerEntry.setFirstPartnerId(firstPlayer.getId());
        playerPartnerEntry.setSecondPartnerId(secondPlayer.getId());

        RelluEssentials.getInstance().getDatabaseHelper().insertPlayerPartner(playerPartnerEntry);
        playerPartnerEntry = RelluEssentials.getInstance().getPlayerAPI().getPartner(firstPlayer);

        firstPlayer.setPartner(playerPartnerEntry);
        secondPlayer.setPartner(playerPartnerEntry);

        Collection<ProtectionEntry> protectionEntryList = new ArrayList<>(RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList().values());

        for(ProtectionEntry pre : protectionEntryList){ 
            if(pre.getCreatedBy() == firstPlayer.getId()){
                BetterLock.addRight(target, pre, secondPlayer.getId(), true);
            }

            if(pre.getCreatedBy() == secondPlayer.getId()){
                BetterLock.addRight(player, pre, firstPlayer.getId(), true);
            }
            
        }
        
    }



    private void divorce(@NotNull PlayerEntry pe) {
        PlayerPartnerEntry ppe = pe.getPartner();

        PlayerEntry secondPlayerEntry = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry((ppe.getSecondPartnerId() != pe.getId() ? ppe.getSecondPartnerId() : ppe.getFirstPartnerId()));

        Player firstPlayer = Bukkit.getPlayer(UUID.fromString(pe.getUuid()));
        OfflinePlayer secondOfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(secondPlayerEntry.getUuid()));

        if(firstPlayer != null && secondOfflinePlayer.getName() != null){
            Player secondPlayer = Bukkit.getPlayer(secondOfflinePlayer.getName());
            if(secondOfflinePlayer.isOnline() && secondPlayer != null){
                firstPlayer.sendMessage(String.format(PLUGIN_COMMAND_MARRY_DIVORCED, secondPlayer.getDisplayName()));
                secondPlayer.sendMessage(String.format(PLUGIN_COMMAND_MARRY_DIVORCED, firstPlayer.getCustomName()));
            }
            else{
                firstPlayer.sendMessage(String.format(PLUGIN_COMMAND_MARRY_DIVORCED, secondOfflinePlayer.getName()));
            }

            ppe.setDeletedBy(pe.getId());
            pe.setPartner(null);
            secondPlayerEntry.setPartner(null);
        
            RelluEssentials.getInstance().getDatabaseHelper().deletePlayerPartner(ppe);

            Collection<ProtectionEntry> protectionEntryList = new ArrayList<>(RelluEssentials.getInstance().getProtectionAPI().getProtectionEntryList().values());

            for(ProtectionEntry pre : protectionEntryList){
                if(pre.getCreatedBy() == pe.getId()){
                    BetterLock.removeRight(firstPlayer, pre, secondPlayerEntry.getId(), true);
                }
    
                if(pre.getCreatedBy() == secondPlayerEntry.getId()){
                    BetterLock.removeRight(pre, pe.getId());
                }
                
            }
        }
    }


    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {

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
            if(args[0].equalsIgnoreCase(Commands.ACCEPT.getName())){
                if(hasMarryEntry(p)){
                    marry(p, marryAcceptList.get(p));
                    removeMarryEntry(p);
                    return true;
                }

                p.sendMessage(PLUGIN_COMMAND_MARRY_ACCEPT_NO_REQUEST);
                return true;
            }

            if(args[0].equalsIgnoreCase(Commands.DIVORCE.getName())){
                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                if(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p).getPartner() != null){
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
                p.sendMessage(PLUGIN_COMMAND_MARRY_SELF_MARRIAGE);
                return true;
            }

            addMarryEntry(p, target);
            
            return true;
        }

        p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tabList = new ArrayList<>();

        if (!Permission.isAuthorized(commandSender, Groups.getGroup("user").getId())) {
            return tabList;
        }

        if (!isPlayer(commandSender)) {
            return tabList;
        }

        if(strings.length > 1){
            return tabList;
        }

        tabList.addAll(TabCompleterHelper.getCommands(Commands.values()));
        tabList.addAll(TabCompleterHelper.getOnlinePlayers());

        return tabList;
    }
}