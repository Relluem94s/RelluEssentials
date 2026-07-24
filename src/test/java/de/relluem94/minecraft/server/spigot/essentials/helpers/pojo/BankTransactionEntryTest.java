package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BankTransactionEntryTest {

    private BankTransactionEntry bankTransactionEntry;

    @BeforeEach
    void setUp() {
        bankTransactionEntry = new BankTransactionEntry();
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        assertEquals(0, bankTransactionEntry.getId());
        assertNull(bankTransactionEntry.getCreated());
        assertEquals(0, bankTransactionEntry.getCreatedBy());
        assertNull(bankTransactionEntry.getUpdated());
        assertEquals(0, bankTransactionEntry.getUpdatedBy());
        assertNull(bankTransactionEntry.getDeleted());
        assertEquals(0, bankTransactionEntry.getDeletedBy());
        assertEquals(0, bankTransactionEntry.getBankAccountId());
        assertEquals(0.0, bankTransactionEntry.getValue());
    }

    @Test
    void shouldSetAndGetId() {
        bankTransactionEntry.setId(1);
        assertEquals(1, bankTransactionEntry.getId());
    }

    @Test
    void shouldSetAndGetCreated() {
        bankTransactionEntry.setCreated("2024-01-01");
        assertEquals("2024-01-01", bankTransactionEntry.getCreated());
    }

    @Test
    void shouldSetAndGetCreatedBy() {
        bankTransactionEntry.setCreatedBy(10);
        assertEquals(10, bankTransactionEntry.getCreatedBy());
    }

    @Test
    void shouldSetAndGetUpdated() {
        bankTransactionEntry.setUpdated("2024-01-02");
        assertEquals("2024-01-02", bankTransactionEntry.getUpdated());
    }

    @Test
    void shouldSetAndGetUpdatedBy() {
        bankTransactionEntry.setUpdatedBy(20);
        assertEquals(20, bankTransactionEntry.getUpdatedBy());
    }

    @Test
    void shouldSetAndGetDeleted() {
        bankTransactionEntry.setDeleted("2024-01-03");
        assertEquals("2024-01-03", bankTransactionEntry.getDeleted());
    }

    @Test
    void shouldSetAndGetDeletedBy() {
        bankTransactionEntry.setDeletedBy(30);
        assertEquals(30, bankTransactionEntry.getDeletedBy());
    }

    @Test
    void shouldSetAndGetBankAccountId() {
        bankTransactionEntry.setBankAccountId(99);
        assertEquals(99, bankTransactionEntry.getBankAccountId());
    }

    @Test
    void shouldSetAndGetPositiveValue() {
        bankTransactionEntry.setValue(250.75);
        assertEquals(250.75, bankTransactionEntry.getValue());
    }

    @Test
    void shouldSetAndGetNegativeValue() {
        bankTransactionEntry.setValue(-100.50);
        assertEquals(-100.50, bankTransactionEntry.getValue());
    }

    @Test
    void shouldSetAndGetZeroValue() {
        bankTransactionEntry.setValue(0.0);
        assertEquals(0.0, bankTransactionEntry.getValue());
    }
}