package de.relluem94.minecraft.server.spigot.essentials.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SignMissingCustomInputExceptionTest {

    @Test
    void constructorStoresMessageCorrectly() {
        String expectedMessage = "Sign is missing required custom input";
        SignMissingCustomInputException exception = new SignMissingCustomInputException(expectedMessage);

        assertAll(
                () -> assertEquals(expectedMessage, exception.getMessage()),
                () -> assertNull(exception.getCause()),
                () -> assertInstanceOf(Exception.class, exception)
        );
    }

    @Test
    void constructorHandlesNullMessage() {
        SignMissingCustomInputException exception = new SignMissingCustomInputException(null);

        assertAll(
                () -> assertNull(exception.getMessage()),
                () -> assertNull(exception.getCause()),
                () -> assertInstanceOf(Exception.class, exception)
        );
    }

    @Test
    void constructorHandlesEmptyMessage() {
        String emptyMessage = "";
        SignMissingCustomInputException exception = new SignMissingCustomInputException(emptyMessage);

        assertAll(
                () -> assertEquals(emptyMessage, exception.getMessage()),
                () -> assertNull(exception.getCause()),
                () -> assertInstanceOf(Exception.class, exception)
        );
    }

    @Test
    void exceptionCanBeThrown() {
        String expectedMessage = "Missing custom input on sign";

        SignMissingCustomInputException thrown = assertThrows(
                SignMissingCustomInputException.class,
                () -> { throw new SignMissingCustomInputException(expectedMessage); }
        );

        assertAll(
                () -> assertEquals(expectedMessage, thrown.getMessage()),
                () -> assertInstanceOf(SignMissingCustomInputException.class, thrown)
        );
    }

    @Test
    void exceptionIsInstanceOfException() {
        SignMissingCustomInputException exception = new SignMissingCustomInputException("test");
        assertInstanceOf(Exception.class, exception);
    }
}