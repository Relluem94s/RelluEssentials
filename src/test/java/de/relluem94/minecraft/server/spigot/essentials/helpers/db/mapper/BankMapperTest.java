package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTransactionEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void constructorThrowsIllegalStateException() throws Exception {
        Constructor<BankMapper> constructor = BankMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, exception.getCause());
    }

    @Test
    void mapBankAccountMapsAllFieldsCorrectly() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
        when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-01-03");
        when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(4);
        when(resultSet.getDouble(FIELD_VALUE)).thenReturn(100.50);
        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(5);

        BankAccountEntry result = BankMapper.mapBankAccount(resultSet);

        assertAll(
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("2024-01-01", result.getCreated()),
                () -> assertEquals(2, result.getCreatedBy()),
                () -> assertEquals("2024-01-02", result.getUpdated()),
                () -> assertEquals(3, result.getUpdatedBy()),
                () -> assertEquals("2024-01-03", result.getDeleted()),
                () -> assertEquals(4, result.getDeletedBy()),
                () -> assertEquals(100.50, result.getValue()),
                () -> assertEquals(5, result.getPlayerId())
        );
    }

    @Test
    void mapBankAccountThrowsSQLExceptionWhenResultSetFails() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));

        assertThrows(SQLException.class, () -> BankMapper.mapBankAccount(resultSet));
    }

    @Test
    void mapBankTransactionMapsAllFieldsCorrectly() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(10);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-02-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(20);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-02-02");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(30);
        when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-02-03");
        when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(40);
        when(resultSet.getInt(FIELD_BANK_ACCOUNT_FK)).thenReturn(50);
        when(resultSet.getDouble(FIELD_VALUE)).thenReturn(200.75);

        BankTransactionEntry result = BankMapper.mapBankTransaction(resultSet);

        assertAll(
                () -> assertEquals(10, result.getId()),
                () -> assertEquals("2024-02-01", result.getCreated()),
                () -> assertEquals(20, result.getCreatedBy()),
                () -> assertEquals("2024-02-02", result.getUpdated()),
                () -> assertEquals(30, result.getUpdatedBy()),
                () -> assertEquals("2024-02-03", result.getDeleted()),
                () -> assertEquals(40, result.getDeletedBy()),
                () -> assertEquals(50, result.getBankAccountId()),
                () -> assertEquals(200.75, result.getValue())
        );
    }

    @Test
    void mapBankTransactionThrowsSQLExceptionWhenResultSetFails() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));

        assertThrows(SQLException.class, () -> BankMapper.mapBankTransaction(resultSet));
    }

    @Test
    void mapBankTierMapsAllFieldsCorrectly() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(100);
        when(resultSet.getString(FIELD_NAME)).thenReturn("Gold");
        when(resultSet.getLong(FIELD_LIMIT)).thenReturn(50000L);
        when(resultSet.getDouble(FIELD_INTEREST)).thenReturn(1.5);
        when(resultSet.getLong(FIELD_COST)).thenReturn(1000L);

        BankTierEntry result = BankMapper.mapBankTier(resultSet);

        assertAll(
                () -> assertEquals(100, result.getId()),
                () -> assertEquals("Gold", result.getName()),
                () -> assertEquals(50000L, result.getLimit()),
                () -> assertEquals(1.5, result.getInterest()),
                () -> assertEquals(1000L, result.getCost())
        );
    }

    @Test
    void mapBankTierThrowsSQLExceptionWhenResultSetFails() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));

        assertThrows(SQLException.class, () -> BankMapper.mapBankTier(resultSet));
    }
}