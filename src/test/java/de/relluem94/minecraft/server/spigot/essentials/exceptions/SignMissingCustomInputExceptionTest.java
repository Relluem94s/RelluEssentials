package de.relluem94.minecraft.server.spigot.essentials.exceptions;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_SIGNHELPER_SIGN_MISSING_CUSTOM_INPUT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignMissingCustomInputExceptionTest {

    @Test
    void constructorStoresMessageCorrectly() {
        SignMissingCustomInputException exception = new SignMissingCustomInputException(PLUGIN_EXCEPTION_SIGNHELPER_SIGN_MISSING_CUSTOM_INPUT);

        assertAll(
                () -> assertEquals(PLUGIN_EXCEPTION_SIGNHELPER_SIGN_MISSING_CUSTOM_INPUT, exception.getMessage()),
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
        SignMissingCustomInputException thrown = assertThrows(
                SignMissingCustomInputException.class,
                () -> { throw new SignMissingCustomInputException(PLUGIN_EXCEPTION_SIGNHELPER_SIGN_MISSING_CUSTOM_INPUT); }
        );

        assertAll(
                () -> assertEquals(PLUGIN_EXCEPTION_SIGNHELPER_SIGN_MISSING_CUSTOM_INPUT, thrown.getMessage()),
                () -> assertInstanceOf(SignMissingCustomInputException.class, thrown)
        );
    }

    @Test
    void exceptionIsInstanceOfException() {
        SignMissingCustomInputException exception = new SignMissingCustomInputException("test");
        assertInstanceOf(Exception.class, exception);
    }
}