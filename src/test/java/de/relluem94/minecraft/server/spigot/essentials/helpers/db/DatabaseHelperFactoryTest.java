package de.relluem94.minecraft.server.spigot.essentials.helpers.db;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseHelperFactoryTest {

    @Mock
    private PlayerAPI playerAPI;

    @Mock
    private RelluEssentials pluginInstance;

    void setUpPluginMock() {
        File tempDataFolder = new File(System.getProperty("java.io.tmpdir"), "rellu-essentials-test");
        boolean created = tempDataFolder.mkdirs();
        if(created)
           when(pluginInstance.getDataFolder()).thenReturn(tempDataFolder);
    }

    @Test
    void createForProductionWithInvalidHostStillReturnsDatabaseHelper() throws SQLException {
        setUpPluginMock();
        try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
            mockedStatic.when(RelluEssentials::getInstance).thenReturn(pluginInstance);

            DatabaseHelper databaseHelper = DatabaseHelperFactory.createForProduction(
                    null, -1, null, null, playerAPI
            );

            assertNotNull(databaseHelper);
        }
    }

    @Test
    void createForTestWithInvalidHostStillReturnsDatabaseHelper() throws SQLException {
        try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
            mockedStatic.when(RelluEssentials::getInstance).thenReturn(pluginInstance);

            DatabaseHelper databaseHelper = DatabaseHelperFactory.createForTest(
                    null, -1, playerAPI
            );

            assertNotNull(databaseHelper);
        }
    }

    @Test
    void createForProductionReturnsConfiguredDatabaseHelper() throws SQLException {
        setUpPluginMock();
        try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
            mockedStatic.when(RelluEssentials::getInstance).thenReturn(pluginInstance);

            DatabaseHelper databaseHelper = DatabaseHelperFactory.createForProduction(
                    "localhost", 3306, "root", "", playerAPI
            );

            assertAll(
                    () -> assertNotNull(databaseHelper),
                    () -> assertInstanceOf(DatabaseHelper.class, databaseHelper)
            );
        }
    }

    @Test
    void createForTestReturnsConfiguredDatabaseHelper() throws SQLException {
        try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
            mockedStatic.when(RelluEssentials::getInstance).thenReturn(pluginInstance);

            DatabaseHelper databaseHelper = DatabaseHelperFactory.createForTest(
                    "localhost", 3306, playerAPI
            );

            assertAll(
                    () -> assertNotNull(databaseHelper),
                    () -> assertInstanceOf(DatabaseHelper.class, databaseHelper)
            );
        }
    }
    @Test
    void privateConstructorThrowsIllegalStateException() throws NoSuchMethodException {
        Constructor<DatabaseHelperFactory> constructor = DatabaseHelperFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertInstanceOf(IllegalStateException.class, thrown.getCause());
    }

    @Test
    void noOpPluginInfoConsumerDoesNothingWhenAccepted() {
        Consumer<PluginInformationEntry> consumer = DatabaseHelperFactory.noOpPluginInfoConsumer();
        assertDoesNotThrow(() -> consumer.accept(mock(PluginInformationEntry.class)));
    }
}