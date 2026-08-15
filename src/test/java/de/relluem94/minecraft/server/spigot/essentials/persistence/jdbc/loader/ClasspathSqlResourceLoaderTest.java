package de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.loader;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClasspathSqlResourceLoaderTest {

    private ClasspathSqlResourceLoader loader;

    @BeforeEach
    void setUp() {
        loader = new ClasspathSqlResourceLoader();
    }

    @Test
    void loadReturnsFileContentWithEolAppended() throws FileNotFoundException {
        String result = loader.load("sql/test-query.sql");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("SELECT 1")),
                () -> assertTrue(result.endsWith(Constants.PLUGIN_EOL))
        );
    }

    @Test
    void loadAppendsEolAfterEachLine() throws FileNotFoundException {
        String result = loader.load("sql/test-multiline-query.sql");

        long eolCount = result.chars()
                .filter(c -> c == Constants.PLUGIN_EOL.charAt(0))
                .count();

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(eolCount >= 2)
        );
    }

    @Test
    void loadThrowsFileNotFoundExceptionForNonExistentFile() {
        FileNotFoundException exception = assertThrows(
                FileNotFoundException.class,
                () -> loader.load("sql/non-existent-file.sql")
        );

        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getMessage().contains("non-existent-file.sql")),
                () -> assertTrue(exception.getMessage().contains("was not found!"))
        );
    }

    @Test
    void loadThrowsFileNotFoundExceptionWithCorrectMessageFormat() {
        String missingFileName = "sql/missing.sql";

        FileNotFoundException exception = assertThrows(
                FileNotFoundException.class,
                () -> loader.load(missingFileName)
        );

        assertEquals(
                String.format("File %s was not found!", missingFileName),
                exception.getMessage()
        );
    }

    @Test
    void loadReturnsEmptyStringForEmptyFile() throws FileNotFoundException {
        String result = loader.load("sql/test-empty.sql");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }
}