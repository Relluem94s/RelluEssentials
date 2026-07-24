package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatchHelperTest {

    @Mock
    private DatabaseHelper databaseHelper;
    @Mock
    private PlayerAPI playerAPIMock;
    @Mock
    private ConfigHelper configHelperMock;

    private PatchHelper patchHelper;

    private MockedStatic<ChatHelper> chatHelperMock;

    @BeforeEach
    void setUp() {
        chatHelperMock = mockStatic(ChatHelper.class);
        patchHelper = new PatchHelper(databaseHelper, playerAPIMock, _ -> {}, configHelperMock);
    }

    @AfterEach
    void tearDown() {
        chatHelperMock.close();
    }

    @Test
    void applyPatch_whenVersionIsMinusOne_appliesAllPatchesInOrder() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(-1);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch1Scripts(inOrder);
        verifyPatch2Scripts(inOrder);
        verifyPatch3Scripts(inOrder);
        verifyPatch4Scripts(inOrder);
        verifyPatch5Scripts(inOrder);
        verifyPatch6Scripts(inOrder);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIsZero_appliesAllPatchesInOrder() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(0);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch1Scripts(inOrder);
        verifyPatch2Scripts(inOrder);
        verifyPatch3Scripts(inOrder);
        verifyPatch4Scripts(inOrder);
        verifyPatch5Scripts(inOrder);
        verifyPatch6Scripts(inOrder);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs1_appliesPatchesFrom2To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(1);

        verify(databaseHelper, never()).executeScriptNoSchema(anyString());
        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch2Scripts(inOrder);
        verifyPatch3Scripts(inOrder);
        verifyPatch4Scripts(inOrder);
        verifyPatch5Scripts(inOrder);
        verifyPatch6Scripts(inOrder);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs2_appliesPatchesFrom3To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(2);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch3Scripts(inOrder);
        verifyPatch4Scripts(inOrder);
        verifyPatch5Scripts(inOrder);
        verifyPatch6Scripts(inOrder);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs3_appliesPatchesFrom4To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(3);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch4Scripts(inOrder);
        verifyPatch5Scripts(inOrder);
        verifyPatch6Scripts(inOrder);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs4_appliesPatchesFrom5To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(4);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch5Scripts(inOrder);
        verifyPatch6Scripts(inOrder);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs5_appliesPatchesFrom6To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(5);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch6Scripts(inOrder);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs6_appliesPatchesFrom7To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(6);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch7Scripts(inOrder);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs7_appliesPatchesFrom8To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(7);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch8Scripts(inOrder);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs8_appliesPatchesFrom9To10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(8);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch9Scripts(inOrder);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs9_appliesOnlyPatch10() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(9);

        InOrder inOrder = inOrder(databaseHelper);
        verifyPatch10Scripts(inOrder);
        inOrder.verify(databaseHelper).getPlayers();
        inOrder.verify(databaseHelper).getPluginInformation();
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11, 99, Integer.MAX_VALUE})
    void applyPatch_whenVersionIsCurrentOrHigher_appliesNoPatchesAndDoesNotCallFinishPatching(int upToDateVersion) {
        patchHelper.applyPatch(upToDateVersion);

        verify(databaseHelper, never()).executeScript(anyString());
        verify(databaseHelper, never()).executeScriptNoSchema(anyString());
        verify(databaseHelper, never()).getPlayers();
        verify(databaseHelper, never()).getPluginInformation();
    }

    @Test
    void applyPatch_whenVersionIs1_doesNotApplyPatch1Scripts() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(1);

        verify(databaseHelper, never()).executeScriptNoSchema("sqls/patches/v1/createSchema.sql");
        verify(databaseHelper, never()).executeScript("patches/v1/createGroup.sql");
    }

    @Test
    void finishPatching_whenPlayersExist_putsEachPlayerIntoPlayerAPI() {
        PlayerEntry playerEntryOne = buildPlayerEntryWithUuid(UUID.randomUUID());
        PlayerEntry playerEntryTwo = buildPlayerEntryWithUuid(UUID.randomUUID());
        when(databaseHelper.getPlayers()).thenReturn(List.of(playerEntryOne, playerEntryTwo));
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(9);

        verify(databaseHelper).getPlayers();
        verify(databaseHelper).getPluginInformation();
    }

    @Test
    void finishPatching_whenNoPlayersExist_completesWithoutError() {
        when(databaseHelper.getPlayers()).thenReturn(Collections.emptyList());
        when(databaseHelper.getPluginInformation()).thenReturn(new PluginInformationEntry());

        patchHelper.applyPatch(9);

        verify(databaseHelper).getPlayers();
        verify(databaseHelper).getPluginInformation();
    }

    private PlayerEntry buildPlayerEntryWithUuid(UUID uuid) {
        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setUuid(uuid.toString());
        return playerEntry;
    }

    private void verifyPatch1Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScriptNoSchema("patches/v1/createSchema.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/createGroup.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/createPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/createLocationType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/createLocation.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/createBlockHistory.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/createPluginInformation.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/insertGroups.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/insertPlayers.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/insertLocationTypes.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v1/insertPluginInformation.sql");
    }

    private void verifyPatch2Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v2/dropBlockHistory.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v2/createBlockHistory.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v2/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v2/updateOldPluginInformation.sql");
    }

    private void verifyPatch3Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v3/dropPlayerConstraint.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/updateAdminGroup.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/updateModGroup.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/updateVipGroup.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/updateAdminGroupPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/updateModGroupPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/updateVipGroupPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/addPlayerConstraint.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v3/updateOldPluginInformation.sql");
    }

    private void verifyPatch4Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v4/addBankTier.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addBankAccount.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addBag.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addBankTransaction.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addPermission.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addPermissionGroup.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addPermissionPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addProtections.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addSkills.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addSkillsPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addNPC.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/addProtectionLocks.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/updatePlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/insertProtectionLocks.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/insertNPC.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/insertSkills.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/insertBankTier.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/insertBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/insertLocationTypes.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/alterPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/alterBankAccount.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/alterBankTier.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/alterBankTransaction.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v4/updateOldPluginInformation.sql");
    }

    private void verifyPatch5Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v5/addSetting.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addPluginSetting.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addSettingPlayer.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addWorldGroup.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addWorld.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addWorldGroupInventory.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addWorldGroupSetting.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addCrops.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addDrops.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addPlayerPartner.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertSkills.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertSettings.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertWorldGroup.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertWorlds.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertWorldGroupSetting.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertCrops.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertDrops.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/addPlayerName.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/changePlayerCustomName.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v5/updateOldPluginInformation.sql");
    }

    private void verifyPatch6Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v6/updateNPCStick.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/updateNPCRedSand.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/updateNPCBambooBlock.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/updateNPCBamboo.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/alterBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/alterBag.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/alterLumberjackBag.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/insertNetherBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/alterLumberjackNPC.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/alterFarmingBag.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/alterMiningBag.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v6/updateOldPluginInformation.sql");
    }

    private void verifyPatch7Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v7/alterFarmingBag.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v7/alterFarmingBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v7/alterMiningBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v7/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v7/updateOldPluginInformation.sql");
    }

    private void verifyPatch8Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v8/insertProtectionLocks.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v8/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v8/updateOldPluginInformation.sql");
    }

    private void verifyPatch9Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v9/updateProtections.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v9/fixProtections.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v9/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v9/updateOldPluginInformation.sql");
    }

    private void verifyPatch10Scripts(InOrder inOrder) {
        inOrder.verify(databaseHelper).executeScript("patches/v10/RE-266_fixDeletedLocationsFromProtections.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/alterMonsterBag.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertProtectionLocks.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertAnimalBagType.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertSettings.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertWorldGroupSettingsCloudsailor.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertWorldGroupSettingsCoinsLose.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertWorldGroupSettingsDeathPoint.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertWorldGroupSettingsScoreBoardShow.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/updateFischerNPCTurtleScute.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/updateFloristNPCShortGrass.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/updateWorldGroupSettings_newColumn.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/updateWorldGroupSettings_moveValues.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/updateWorldGroupSettings_removeColumnAndRename.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/insertNewDBVersion.sql");
        inOrder.verify(databaseHelper).executeScript("patches/v10/updateOldPluginInformation.sql");
    }
}