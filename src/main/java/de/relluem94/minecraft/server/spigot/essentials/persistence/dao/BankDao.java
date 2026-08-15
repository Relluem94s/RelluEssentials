package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.BankMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Data access object for all bank-related database operations.
 */
public class BankDao {

  private final QueryExecutor queryExecutor;

  /**
   * @param queryExecutor the executor used to run SQL queries
   */
  public BankDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves all available bank tiers.
   *
   * @return list of all {@link BankTierEntry} records
   */
  public List<BankTierEntry> findAllBankTiers() {
    return queryExecutor.queryList("getBankTiers.sql", _ -> {
    }, BankMapper::mapBankTier);
  }

  /**
   * Retrieves a single bank tier by its id.
   *
   * @param id the primary key of the bank tier
   * @return the matching {@link BankTierEntry}, or {@code null} if not found
   */
  public BankTierEntry findBankTierById(int id) {
    return queryExecutor.querySingle("getBankTier.sql", ps -> ps.setInt(1, id),
        BankMapper::mapBankTier);
  }

  /**
   * Retrieves the bank account for the given player.
   *
   * @param playerFK the internal player id (foreign key)
   * @return the {@link BankAccountEntry} with its tier populated, or {@code null} if not found
   */
  public BankAccountEntry findBankAccountByPlayerId(int playerFK) {
    return queryExecutor.querySingle("getBankAccountByPlayer.sql", ps -> ps.setInt(1, playerFK),
        rs -> {
          BankAccountEntry bae = BankMapper.mapBankAccount(rs);
          bae.setTier(findBankTierById(rs.getInt(DatabaseMappings.FIELD_BANK_TIER_FK)));
          return bae;
        });
  }

  /**
   * Inserts a new bank account record.
   *
   * @param bae the bank account to insert
   */
  public void insertBankAccount(@NotNull BankAccountEntry bae) {
    queryExecutor.executeUpdate("insertBankAccount.sql", ps -> {
      ps.setInt(1, 1);
      ps.setInt(2, bae.getPlayerId());
      ps.setDouble(3, bae.getValue());
      ps.setInt(4, bae.getTier().getId());
    });
  }

  /**
   * Inserts a bank transaction record.
   *
   * @param playerFK        the internal player id
   * @param bankAccountFK   the bank account id
   * @param transactionValue the signed value of the transaction
   */
  public void insertBankTransaction(int playerFK, int bankAccountFK, double transactionValue) {
    queryExecutor.executeUpdate("insertBankTransaction.sql", ps -> {
      ps.setInt(1, playerFK);
      ps.setInt(2, bankAccountFK);
      ps.setDouble(3, transactionValue);
    });
  }

  /**
   * Updates the bank account balance and tier for the given player.
   *
   * @param playerFK          the internal player id
   * @param newBalance        the new total balance to persist
   * @param tierId            the tier id to set on the account
   */
  public void updateBankAccount(int playerFK, double newBalance, int tierId) {
    queryExecutor.executeUpdate("updateBankAccount.sql", ps -> {
      ps.setInt(1, playerFK);
      ps.setDouble(2, newBalance);
      ps.setInt(3, tierId);
      ps.setInt(4, playerFK);
    });
  }

  /**
   * Retrieves all transactions for the given bank account.
   *
   * @param bankAccountFK the bank account id
   * @return list of {@link BankTransactionEntry} records
   */
  public List<BankTransactionEntry> findTransactionsByBankAccountId(int bankAccountFK) {
    return queryExecutor.queryList("getBankAccountTransactionsByPlayer.sql",
        ps -> ps.setInt(1, bankAccountFK), BankMapper::mapBankTransaction);
  }
}