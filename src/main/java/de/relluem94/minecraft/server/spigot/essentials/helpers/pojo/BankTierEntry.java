package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class BankTierEntry {
    private int id;
    private String name;
    private long limit;
    private double interest;
    private long cost;
}