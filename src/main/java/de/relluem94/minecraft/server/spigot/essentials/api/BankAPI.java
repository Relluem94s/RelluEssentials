package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;

public class BankAPI {
    
    private static List<BankTierEntry> bankTiersList = new ArrayList<>();

    /**
     * 
     * @param bankTierEntries List of BankTierEntry
     */
    public BankAPI(List<BankTierEntry> bankTierEntries){
        bankTiersList.addAll(bankTierEntries);
    }

    /**
     * 
     * Gives back a List of all BankTiers
     * @return List of BankTierEntry
     */
    public static List<BankTierEntry> getBankTiers(){
        return bankTiersList;
    }
}