package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeHelperTest {

    @Mock
    private Block block;

    @Mock
    private Player player;

    @Mock
    private ConsoleCommandSender consoleCommandSender;

    @Mock
    private BlockCommandSender blockCommandSender;

    @Mock
    private CommandSender commandSender;

    @Test
    void constructorThrowsIllegalStateException() throws Exception {
        Constructor<TypeHelper> constructor = TypeHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrownException = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrownException.getCause());
    }

    @Test
    void constructorIllegalStateExceptionMessageMatchesConstant() throws Exception {
        Constructor<TypeHelper> constructor = TypeHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrownException = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertEquals(Constants.PLUGIN_INTERNAL_UTILITY_CLASS, thrownException.getCause().getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "-1", "2147483647", "-2147483648", "42"})
    void isIntReturnsTrueForValidIntegers(String value) {
        assertTrue(TypeHelper.isInt(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.5", "abc", "", " ", "2147483648", "null", "1,0"})
    void isIntReturnsFalseForInvalidIntegers(String value) {
        assertFalse(TypeHelper.isInt(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.0", "1.5", "-1.5", "3.14", "42", "1e10"})
    void isDoubleReturnsTrueForValidDoubles(String value) {
        assertTrue(TypeHelper.isDouble(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "", " ", "null", "1,0", "1.2.3"})
    void isDoubleReturnsFalseForInvalidDoubles(String value) {
        assertFalse(TypeHelper.isDouble(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.0", "1.5", "-1.5", "3.14", "42", "1e10"})
    void isFloatReturnsTrueForValidFloats(String value) {
        assertTrue(TypeHelper.isFloat(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "", " ", "null", "1,0", "1.2.3"})
    void isFloatReturnsFalseForInvalidFloats(String value) {
        assertFalse(TypeHelper.isFloat(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "-1", "9223372036854775807", "-9223372036854775808", "42"})
    void isLongReturnsTrueForValidLongs(String value) {
        assertTrue(TypeHelper.isLong(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.5", "abc", "", " ", "9223372036854775808", "null", "1,0"})
    void isLongReturnsFalseForInvalidLongs(String value) {
        assertFalse(TypeHelper.isLong(value));
    }

    @Test
    void areBlocksMaterialReturnsTrueWhenAllBlocksMatchMaterial() {
        Block secondBlock = mock(Block.class);
        when(block.getType()).thenReturn(Material.STONE);
        when(secondBlock.getType()).thenReturn(Material.STONE);
        List<Block> blocks = Arrays.asList(block, secondBlock);
        assertTrue(TypeHelper.areBlocksMaterial(blocks, Material.STONE));
    }

    @Test
    void areBlocksMaterialReturnsFalseWhenOneBlockDoesNotMatchMaterial() {
        Block secondBlock = mock(Block.class);
        when(block.getType()).thenReturn(Material.STONE);
        when(secondBlock.getType()).thenReturn(Material.DIRT);
        List<Block> blocks = Arrays.asList(block, secondBlock);
        assertFalse(TypeHelper.areBlocksMaterial(blocks, Material.STONE));
    }

    @Test
    void areBlocksMaterialReturnsTrueForEmptyList() {
        assertTrue(TypeHelper.areBlocksMaterial(Collections.emptyList(), Material.STONE));
    }

    @Test
    void isBlockOneOfMaterialsReturnsTrueWhenBlockMaterialIsInList() {
        when(block.getType()).thenReturn(Material.STONE);
        List<Material> materials = Arrays.asList(Material.STONE, Material.DIRT);
        assertTrue(TypeHelper.isBlockOneOfMaterials(block, materials));
    }

    @Test
    void isBlockOneOfMaterialsReturnsFalseWhenBlockMaterialIsNotInList() {
        when(block.getType()).thenReturn(Material.GRASS_BLOCK);
        List<Material> materials = Arrays.asList(Material.STONE, Material.DIRT);
        assertFalse(TypeHelper.isBlockOneOfMaterials(block, materials));
    }

    @Test
    void isBlockOneOfMaterialsReturnsFalseForEmptyMaterialList() {
        assertFalse(TypeHelper.isBlockOneOfMaterials(block, Collections.emptyList()));
    }

    @Test
    void isPlayerReturnsTrueForPlayerSender() {
        assertTrue(TypeHelper.isPlayer(player));
    }

    @Test
    void isPlayerReturnsFalseForConsoleSender() {
        assertFalse(TypeHelper.isPlayer(consoleCommandSender));
    }

    @Test
    void isPlayerReturnsFalseForBlockCommandSender() {
        assertFalse(TypeHelper.isPlayer(blockCommandSender));
    }

    @Test
    void isPlayerReturnsFalseForGenericCommandSender() {
        assertFalse(TypeHelper.isPlayer(commandSender));
    }

    @Test
    void isCmdBlockReturnsTrueForBlockCommandSender() {
        assertTrue(TypeHelper.isCmdBlock(blockCommandSender));
    }

    @Test
    void isCmdBlockReturnsFalseForPlayerSender() {
        assertFalse(TypeHelper.isCmdBlock(player));
    }

    @Test
    void isCmdBlockReturnsFalseForConsoleSender() {
        assertFalse(TypeHelper.isCmdBlock(consoleCommandSender));
    }

    @Test
    void isCmdBlockReturnsFalseForGenericCommandSender() {
        assertFalse(TypeHelper.isCmdBlock(commandSender));
    }

    @Test
    void isConsoleReturnsTrueForConsoleCommandSender() {
        assertTrue(TypeHelper.isConsole(consoleCommandSender));
    }

    @Test
    void isConsoleReturnsFalseForPlayerSender() {
        assertFalse(TypeHelper.isConsole(player));
    }

    @Test
    void isConsoleReturnsFalseForBlockCommandSender() {
        assertFalse(TypeHelper.isConsole(blockCommandSender));
    }

    @Test
    void isConsoleReturnsFalseForGenericCommandSender() {
        assertFalse(TypeHelper.isConsole(commandSender));
    }

    @Test
    void isMaterialInListReturnsTrueWhenMaterialIsPresent() {
        List<Material> materials = Arrays.asList(Material.STONE, Material.DIRT);
        assertTrue(TypeHelper.isMaterialInList(Material.STONE, materials));
    }

    @Test
    void isMaterialInListReturnsFalseWhenMaterialIsAbsent() {
        List<Material> materials = Arrays.asList(Material.STONE, Material.DIRT);
        assertFalse(TypeHelper.isMaterialInList(Material.GRASS_BLOCK, materials));
    }

    @Test
    void isMaterialInListReturnsFalseForEmptyList() {
        assertFalse(TypeHelper.isMaterialInList(Material.STONE, Collections.emptyList()));
    }

    @Test
    void isMaterialInArrayReturnsTrueWhenMaterialIsPresent() {
        Material[] materials = {Material.STONE, Material.DIRT};
        assertTrue(TypeHelper.isMaterialInArray(Material.STONE, materials));
    }

    @Test
    void isMaterialInArrayReturnsFalseWhenMaterialIsAbsent() {
        Material[] materials = {Material.STONE, Material.DIRT};
        assertFalse(TypeHelper.isMaterialInArray(Material.GRASS_BLOCK, materials));
    }

    @Test
    void isMaterialInArrayReturnsFalseForEmptyArray() {
        assertFalse(TypeHelper.isMaterialInArray(Material.STONE, new Material[]{}));
    }

    @ParameterizedTest
    @MethodSource("provideMaterialListPresenceScenarios")
    void isMaterialInListHandlesVariousScenarios(Material target, List<Material> list, boolean expected) {
        assertEquals(expected, TypeHelper.isMaterialInList(target, list));
    }

    private static Stream<Arguments> provideMaterialListPresenceScenarios() {
        return Stream.of(
            Arguments.of(Material.STONE, Arrays.asList(Material.STONE, Material.DIRT), true),
            Arguments.of(Material.DIRT, Arrays.asList(Material.STONE, Material.DIRT), true),
            Arguments.of(Material.GRASS_BLOCK, Arrays.asList(Material.STONE, Material.DIRT), false),
            Arguments.of(Material.STONE, Collections.emptyList(), false),
            Arguments.of(Material.STONE, Collections.singletonList(Material.STONE), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMaterialArrayPresenceScenarios")
    void isMaterialInArrayHandlesVariousScenarios(Material target, Material[] array, boolean expected) {
        assertEquals(expected, TypeHelper.isMaterialInArray(target, array));
    }

    private static Stream<Arguments> provideMaterialArrayPresenceScenarios() {
        return Stream.of(
            Arguments.of(Material.STONE, new Material[]{Material.STONE, Material.DIRT}, true),
            Arguments.of(Material.DIRT, new Material[]{Material.STONE, Material.DIRT}, true),
            Arguments.of(Material.GRASS_BLOCK, new Material[]{Material.STONE, Material.DIRT}, false),
            Arguments.of(Material.STONE, new Material[]{}, false),
            Arguments.of(Material.STONE, new Material[]{Material.STONE}, true)
        );
    }
}