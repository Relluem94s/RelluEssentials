package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BankDao;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Repository that provides bank-related persistence operations to the service layer.
 */
public class BankRepository {

  private final BankDao bankDao;

  /**
   * Creates a new BankRepository with the given DAO.
   *
   * @param bankDao the DAO used to access bank data
   */
  public BankRepository(BankDao bankDao) {
    this.bankDao = bankDao;
  }

  /**
   * Returns all available bank tiers.
   *
   * @return list of all {@link BankTierEntry} records
   */
  public List<BankTierEntry> findAllBankTiers() {
    return bankDao.findAllBankTiers();
  }

  /**
   * Returns a bank tier by its id.
   *
   * @param id the primary key of the bank tier
   * @return the matching {@link BankTierEntry}, or {@code null} if not found
   */
  public BankTierEntry findBankTierById(int id) {
    return bankDao.findBankTierById(id);
  }

  /**
   * Returns the bank account for the given player.
   *
   * @param playerFk the internal player id
   * @return the {@link BankAccountEntry} with its tier populated, or {@code null} if not found
   */
  public BankAccountEntry findBankAccountByPlayerId(int playerFk) {
    return bankDao.findBankAccountByPlayerId(playerFk);
  }

  /**
   * Inserts a new bank account.
   *
   * @param bae the bank account to persist
   */
  public void insertBankAccount(@NotNull BankAccountEntry bae) {
    bankDao.insertBankAccount(bae);
  }

  /**
   * Records a transaction and updates the account balance.
   *
   * @param playerFk         the internal player id
   * @param bankAccountFk    the bank account id
   * @param transactionValue the signed transaction amount
   * @param currentBalance   the balance before the transaction
   * @param tierId           the current tier id of the account
   */
  public void addTransactionToBank(int playerFk, int bankAccountFk, double transactionValue,
      double currentBalance, int tierId) {
    bankDao.insertBankTransaction(playerFk, bankAccountFk, transactionValue);
    bankDao.updateBankAccount(playerFk, currentBalance + transactionValue, tierId);
  }

  /**
   * Updates the bank account balance and tier without recording a transaction.
   *
   * @param playerFk       the internal player id
   * @param transactionValue the value to add to the current balance
   * @param currentBalance   the balance before the update
   * @param tierId           the tier id to set
   */
  public void updateBankAccount(int playerFk, double transactionValue, double currentBalance,
      int tierId) {
    bankDao.updateBankAccount(playerFk, currentBalance + transactionValue, tierId);
  }

  /**
   * Returns all transactions for the given bank account.
   *
   * @param bankAccountFk the bank account id
   * @return list of {@link BankTransactionEntry} records
   */
  public List<BankTransactionEntry> findTransactionsByBankAccountId(int bankAccountFk) {
    return bankDao.findTransactionsByBankAccountId(bankAccountFk);
  }
}