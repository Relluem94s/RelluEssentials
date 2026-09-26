package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BankDao;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankRepositoryTest {

  @Mock
  private BankDao bankDao;

  @InjectMocks
  private BankRepository bankRepository;

  @Test
  void findAllBankTiersReturnsDaoResult() {
    List<BankTierEntry> expected = List.of(new BankTierEntry(), new BankTierEntry());
    when(bankDao.findAllBankTiers()).thenReturn(expected);

    List<BankTierEntry> result = bankRepository.findAllBankTiers();

    assertEquals(expected, result);
    verify(bankDao).findAllBankTiers();
  }

  @Test
  void findAllBankTiersPropagatesException() {
    when(bankDao.findAllBankTiers()).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankRepository.findAllBankTiers());
  }

  @Test
  void findBankTierByIdReturnsDaoResult() {
    BankTierEntry expected = new BankTierEntry();
    when(bankDao.findBankTierById(1)).thenReturn(expected);

    BankTierEntry result = bankRepository.findBankTierById(1);

    assertEquals(expected, result);
    verify(bankDao).findBankTierById(1);
  }

  @Test
  void findBankTierByIdReturnsNullWhenNotFound() {
    when(bankDao.findBankTierById(99)).thenReturn(null);

    BankTierEntry result = bankRepository.findBankTierById(99);

    assertNull(result);
  }

  @Test
  void findBankTierByIdPropagatesException() {
    when(bankDao.findBankTierById(anyInt())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankRepository.findBankTierById(1));
  }

  @Test
  void findBankAccountByPlayerIdReturnsDaoResult() {
    BankAccountEntry expected = new BankAccountEntry();
    when(bankDao.findBankAccountByPlayerId(42)).thenReturn(expected);

    BankAccountEntry result = bankRepository.findBankAccountByPlayerId(42);

    assertEquals(expected, result);
    verify(bankDao).findBankAccountByPlayerId(42);
  }

  @Test
  void findBankAccountByPlayerIdReturnsNullWhenNotFound() {
    when(bankDao.findBankAccountByPlayerId(99)).thenReturn(null);

    BankAccountEntry result = bankRepository.findBankAccountByPlayerId(99);

    assertNull(result);
  }

  @Test
  void findBankAccountByPlayerIdPropagatesException() {
    when(bankDao.findBankAccountByPlayerId(anyInt())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankRepository.findBankAccountByPlayerId(42));
  }

  @Test
  void insertBankAccountDelegatestoDao() {
    BankAccountEntry entry = new BankAccountEntry();

    bankRepository.insertBankAccount(entry);

    verify(bankDao).insertBankAccount(entry);
  }

  @Test
  void insertBankAccountPropagatesException() {
    BankAccountEntry entry = new BankAccountEntry();
    doThrow(new RuntimeException("db error")).when(bankDao).insertBankAccount(entry);

    assertThrows(RuntimeException.class, () -> bankRepository.insertBankAccount(entry));
  }

  @Test
  void addTransactionToBankInsertsTransactionAndUpdatesBalance() {
    int playerFK = 1;
    int bankAccountFK = 2;
    double transactionValue = 100.0;
    double currentBalance = 500.0;
    int tierId = 3;

    bankRepository.addTransactionToBank(playerFK, bankAccountFK, transactionValue, currentBalance, tierId);

    verify(bankDao).insertBankTransaction(playerFK, bankAccountFK, transactionValue);
    verify(bankDao).updateBankAccount(playerFK, currentBalance + transactionValue, tierId);
  }

  @Test
  void addTransactionToBankPropagatesExceptionOnInsert() {
    doThrow(new RuntimeException("db error")).when(bankDao).insertBankTransaction(anyInt(), anyInt(), anyDouble());

    assertThrows(RuntimeException.class,
        () -> bankRepository.addTransactionToBank(1, 2, 100.0, 500.0, 3));
  }

  @Test
  void addTransactionToBankPropagatesExceptionOnUpdate() {
    doThrow(new RuntimeException("db error")).when(bankDao).updateBankAccount(anyInt(), anyDouble(), anyInt());

    assertThrows(RuntimeException.class,
        () -> bankRepository.addTransactionToBank(1, 2, 100.0, 500.0, 3));
  }

  @Test
  void addTransactionToBankCalculatesNewBalanceCorrectly() {
    int playerFK = 1;
    int bankAccountFK = 2;
    double transactionValue = 250.0;
    double currentBalance = 750.0;
    int tierId = 1;

    bankRepository.addTransactionToBank(playerFK, bankAccountFK, transactionValue, currentBalance, tierId);

    verify(bankDao).updateBankAccount(playerFK, 1000.0, tierId);
  }

  @Test
  void addTransactionToBankWithNegativeTransactionDeductsBalance() {
    int playerFK = 1;
    int bankAccountFK = 2;
    double transactionValue = -200.0;
    double currentBalance = 500.0;
    int tierId = 1;

    bankRepository.addTransactionToBank(playerFK, bankAccountFK, transactionValue, currentBalance, tierId);

    verify(bankDao).insertBankTransaction(playerFK, bankAccountFK, transactionValue);
    verify(bankDao).updateBankAccount(playerFK, 300.0, tierId);
  }

  @Test
  void updateBankAccountUpdatesWithCalculatedBalance() {
    int playerFK = 1;
    double transactionValue = 100.0;
    double currentBalance = 400.0;
    int tierId = 2;

    bankRepository.updateBankAccount(playerFK, transactionValue, currentBalance, tierId);

    verify(bankDao).updateBankAccount(playerFK, currentBalance + transactionValue, tierId);
    verify(bankDao, never()).insertBankTransaction(anyInt(), anyInt(), anyDouble());
  }

  @Test
  void updateBankAccountDoesNotRecordTransaction() {
    bankRepository.updateBankAccount(1, 50.0, 200.0, 1);

    verify(bankDao, never()).insertBankTransaction(anyInt(), anyInt(), anyDouble());
  }

  @Test
  void updateBankAccountPropagatesException() {
    doThrow(new RuntimeException("db error")).when(bankDao).updateBankAccount(anyInt(), anyDouble(), anyInt());

    assertThrows(RuntimeException.class,
        () -> bankRepository.updateBankAccount(1, 100.0, 400.0, 2));
  }

  @Test
  void updateBankAccountWithNegativeTransactionDeductsBalance() {
    int playerFK = 5;
    double transactionValue = -150.0;
    double currentBalance = 600.0;
    int tierId = 3;

    bankRepository.updateBankAccount(playerFK, transactionValue, currentBalance, tierId);

    verify(bankDao).updateBankAccount(playerFK, 450.0, tierId);
  }

  @Test
  void findTransactionsByBankAccountIdReturnsDaoResult() {
    List<BankTransactionEntry> expected = List.of(new BankTransactionEntry(), new BankTransactionEntry());
    when(bankDao.findTransactionsByBankAccountId(10)).thenReturn(expected);

    List<BankTransactionEntry> result = bankRepository.findTransactionsByBankAccountId(10);

    assertEquals(expected, result);
    verify(bankDao).findTransactionsByBankAccountId(10);
  }

  @Test
  void findTransactionsByBankAccountIdReturnsEmptyList() {
    when(bankDao.findTransactionsByBankAccountId(99)).thenReturn(List.of());

    List<BankTransactionEntry> result = bankRepository.findTransactionsByBankAccountId(99);

    assertTrue(result.isEmpty());
  }

  @Test
  void findTransactionsByBankAccountIdPropagatesException() {
    when(bankDao.findTransactionsByBankAccountId(anyInt())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankRepository.findTransactionsByBankAccountId(10));
  }
}