package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
