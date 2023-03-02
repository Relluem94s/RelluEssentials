package de.relluem94.minecraft.server.spigot.essentials.api;

import java.util.ArrayList;
import java.util.List;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;

public class BankAPI {
    
    private static List<BankTierEntry> bankTiersList = new ArrayList<>();

    public BankAPI(List<BankTierEntry> bankTierEntries){
        bankTiersList.addAll(bankTierEntries);
    }

    public static List<BankTierEntry> getBankTiers(){
        return bankTiersList;
    }






}
