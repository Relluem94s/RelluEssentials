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

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LIMIT = "limit";
    public static final String FIELD_INTEREST = "interest";
    public static final String FIELD_COST = "cost";


    private int id;
    private String name;
    private long limit;
    private double interest;
    private long cost;
}