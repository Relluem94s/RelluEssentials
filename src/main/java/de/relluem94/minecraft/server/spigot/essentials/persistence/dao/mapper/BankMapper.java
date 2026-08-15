package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_BANK_ACCOUNT_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_COST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_INTEREST;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LIMIT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_VALUE;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jspecify.annotations.NonNull;

public class BankMapper {

  private BankMapper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static @NonNull BankAccountEntry mapBankAccount(@NonNull ResultSet rs)
      throws SQLException {
    BankAccountEntry bankAccountEntry = new BankAccountEntry();
    bankAccountEntry.setId(rs.getInt(DatabaseMappings.FIELD_ID));
    bankAccountEntry.setCreated(rs.getString(DatabaseMappings.FIELD_CREATED));
    bankAccountEntry.setCreatedBy(rs.getInt(DatabaseMappings.FIELD_CREATEDBY));
    bankAccountEntry.setUpdated(rs.getString(DatabaseMappings.FIELD_UPDATED));
    bankAccountEntry.setUpdatedBy(rs.getInt(DatabaseMappings.FIELD_UPDATEDBY));
    bankAccountEntry.setDeleted(rs.getString(DatabaseMappings.FIELD_DELETED));
    bankAccountEntry.setDeletedBy(rs.getInt(DatabaseMappings.FIELD_DELETEDBY));
    bankAccountEntry.setValue(rs.getDouble(DatabaseMappings.FIELD_VALUE));
    bankAccountEntry.setPlayerId(rs.getInt(DatabaseMappings.FIELD_PLAYER_FK));
    return bankAccountEntry;
  }

  public static @NonNull BankTransactionEntry mapBankTransaction(@NonNull ResultSet rs)
      throws SQLException {
    BankTransactionEntry bankTransactionEntry = new BankTransactionEntry();
    bankTransactionEntry.setId(rs.getInt(FIELD_ID));
    bankTransactionEntry.setCreated(rs.getString(FIELD_CREATED));
    bankTransactionEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
    bankTransactionEntry.setUpdated(rs.getString(FIELD_UPDATED));
    bankTransactionEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
    bankTransactionEntry.setDeleted(rs.getString(FIELD_DELETED));
    bankTransactionEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
    bankTransactionEntry.setBankAccountId(rs.getInt(FIELD_BANK_ACCOUNT_FK));
    bankTransactionEntry.setValue(rs.getDouble(FIELD_VALUE));
    return bankTransactionEntry;
  }

  public static @NonNull BankTierEntry mapBankTier(@NonNull ResultSet rs) throws SQLException {
    BankTierEntry bankTierEntry = new BankTierEntry();
    bankTierEntry.setId(rs.getInt(FIELD_ID));
    bankTierEntry.setName(rs.getString(FIELD_NAME));
    bankTierEntry.setLimit(rs.getLong(FIELD_LIMIT));
    bankTierEntry.setInterest(rs.getDouble(FIELD_INTEREST));
    bankTierEntry.setCost(rs.getLong(FIELD_COST));
    return bankTierEntry;
  }
}
