package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class BankTransactionEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_BANK_ACCOUNT_FK = "bank_account_fk";
    public static final String FIELD_VALUE = "value";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private int bankAccountId;
    private double value;
}