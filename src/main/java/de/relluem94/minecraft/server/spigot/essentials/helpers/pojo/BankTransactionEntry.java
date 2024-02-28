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