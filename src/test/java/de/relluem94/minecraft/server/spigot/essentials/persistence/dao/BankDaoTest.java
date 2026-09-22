package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_BANK_TIER_FK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  private BankDao bankDao;

  @BeforeEach
  void setUp() {
    bankDao = new BankDao(queryExecutor);
  }

  @Test
  void findAllBankTiersReturnsListFromQueryExecutor() {
    BankTierEntry tier = new BankTierEntry();
    when(queryExecutor.queryList(eq("getBankTiers.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(List.of(tier));

    List<BankTierEntry> result = bankDao.findAllBankTiers();

    assertEquals(1, result.size());
    assertEquals(tier, result.getFirst());
  }

  @Test
  void findAllBankTiersConfiguresEmptyStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getBankTiers.sql"), captor.capture(), any()))
        .thenReturn(List.of());

    bankDao.findAllBankTiers();

    captor.getValue().configure(preparedStatement);
  }

  @Test
  void findAllBankTiersPropagatesException() {
    when(queryExecutor.queryList(eq("getBankTiers.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankDao.findAllBankTiers());
  }

  @Test
  void findBankTierByIdReturnsSingleEntryFromQueryExecutor() {
    BankTierEntry tier = new BankTierEntry();
    when(queryExecutor.querySingle(eq("getBankTier.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(tier);

    BankTierEntry result = bankDao.findBankTierById(1);

    assertEquals(tier, result);
  }

  @Test
  void findBankTierByIdSetsIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getBankTier.sql"), captor.capture(), any()))
        .thenReturn(null);

    bankDao.findBankTierById(5);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 5);
  }

  @Test
  void findBankTierByIdReturnsNullWhenNotFound() {
    when(queryExecutor.querySingle(eq("getBankTier.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(null);

    BankTierEntry result = bankDao.findBankTierById(99);

    assertNull(result);
  }

  @Test
  void findBankTierByIdPropagatesException() {
    when(queryExecutor.querySingle(eq("getBankTier.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankDao.findBankTierById(1));
  }

  @Test
  void findBankAccountByPlayerIdReturnsMappedAccountWithTier() throws SQLException {
    BankTierEntry tier = new BankTierEntry();
    tier.setId(2);

    when(queryExecutor.querySingle(eq("getBankTier.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(tier);

    try (ResultSet resultSet = mock(ResultSet.class)) {
      lenient().when(resultSet.getInt(FIELD_BANK_TIER_FK)).thenReturn(2);

      when(queryExecutor.querySingle(eq("getBankAccountByPlayer.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<BankAccountEntry> mapper = invocation.getArgument(2);
            return mapper.map(resultSet);
          });

      BankAccountEntry result = bankDao.findBankAccountByPlayerId(1);

      assertNotNull(result);
      assertNotNull(result.getTier());
      assertEquals(2, result.getTier().getId());
    }
  }

  @Test
  void findBankAccountByPlayerIdSetsPlayerIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getBankAccountByPlayer.sql"), captor.capture(), any()))
        .thenReturn(null);

    bankDao.findBankAccountByPlayerId(7);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 7);
  }

  @Test
  void findBankAccountByPlayerIdReturnsNullWhenNotFound() {
    when(queryExecutor.querySingle(eq("getBankAccountByPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(null);

    BankAccountEntry result = bankDao.findBankAccountByPlayerId(99);

    assertNull(result);
  }

  @Test
  void findBankAccountByPlayerIdPopulatesTierAsNullWhenTierNotFound() throws SQLException {
    when(queryExecutor.querySingle(eq("getBankTier.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(null);

    try (ResultSet resultSet = mock(ResultSet.class)) {
      lenient().when(resultSet.getInt(FIELD_BANK_TIER_FK)).thenReturn(99);

      when(queryExecutor.querySingle(eq("getBankAccountByPlayer.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<BankAccountEntry> mapper = invocation.getArgument(2);
            return mapper.map(resultSet);
          });

      BankAccountEntry result = bankDao.findBankAccountByPlayerId(1);

      assertNotNull(result);
      assertNull(result.getTier());
    }
  }

  @Test
  void findBankAccountByPlayerIdPropagatesException() {
    when(queryExecutor.querySingle(eq("getBankAccountByPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankDao.findBankAccountByPlayerId(1));
  }

  @Test
  void insertBankAccountSetsAllParametersOnPreparedStatement() throws SQLException {
    BankTierEntry tier = new BankTierEntry();
    tier.setId(3);

    BankAccountEntry bae = new BankAccountEntry();
    bae.setPlayerId(10);
    bae.setValue(500.0);
    bae.setTier(tier);

    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertBankAccount.sql"), captor.capture());

    bankDao.insertBankAccount(bae);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 1);
    verify(preparedStatement).setInt(2, bae.getPlayerId());
    verify(preparedStatement).setDouble(3, bae.getValue());
    verify(preparedStatement).setInt(4, bae.getTier().getId());
  }

  @Test
  void insertBankAccountPropagatesException() {
    BankTierEntry tier = new BankTierEntry();
    tier.setId(1);

    BankAccountEntry bae = new BankAccountEntry();
    bae.setPlayerId(10);
    bae.setValue(100.0);
    bae.setTier(tier);

    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertBankAccount.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> bankDao.insertBankAccount(bae));
  }

  @Test
  void insertBankTransactionSetsAllParametersOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertBankTransaction.sql"), captor.capture());

    bankDao.insertBankTransaction(5, 12, -250.0);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 5);
    verify(preparedStatement).setInt(2, 12);
    verify(preparedStatement).setDouble(3, -250.0);
  }

  @Test
  void insertBankTransactionPropagatesException() {
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertBankTransaction.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> bankDao.insertBankTransaction(5, 12, -250.0));
  }

  @Test
  void updateBankAccountSetsAllParametersOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateBankAccount.sql"), captor.capture());

    bankDao.updateBankAccount(7, 1000.0, 2);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 7);
    verify(preparedStatement).setDouble(2, 1000.0);
    verify(preparedStatement).setInt(3, 2);
    verify(preparedStatement).setInt(4, 7);
  }

  @Test
  void updateBankAccountPropagatesException() {
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("updateBankAccount.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> bankDao.updateBankAccount(7, 1000.0, 2));
  }

  @Test
  void findTransactionsByBankAccountIdReturnsListFromQueryExecutor() {
    BankTransactionEntry transaction = new BankTransactionEntry();
    when(queryExecutor.queryList(eq("getBankAccountTransactionsByPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(List.of(transaction));

    List<BankTransactionEntry> result = bankDao.findTransactionsByBankAccountId(3);

    assertEquals(1, result.size());
    assertEquals(transaction, result.getFirst());
  }

  @Test
  void findTransactionsByBankAccountIdSetsBankAccountIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getBankAccountTransactionsByPlayer.sql"), captor.capture(), any()))
        .thenReturn(List.of());

    bankDao.findTransactionsByBankAccountId(3);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 3);
  }

  @Test
  void findTransactionsByBankAccountIdReturnsEmptyListWhenNoneFound() {
    when(queryExecutor.queryList(eq("getBankAccountTransactionsByPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(List.of());

    List<BankTransactionEntry> result = bankDao.findTransactionsByBankAccountId(99);

    assertEquals(0, result.size());
  }

  @Test
  void findTransactionsByBankAccountIdPropagatesException() {
    when(queryExecutor.queryList(eq("getBankAccountTransactionsByPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> bankDao.findTransactionsByBankAccountId(3));
  }
}