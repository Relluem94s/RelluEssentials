package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEntryTest {

    private PlayerEntry playerEntry;

    @BeforeEach
    void setUp() {
        playerEntry = new PlayerEntry();
        playerEntry.setId(1);
        playerEntry.setCreated("2024-01-01");
        playerEntry.setCreatedBy(1);
        playerEntry.setUpdated("2024-01-02");
        playerEntry.setUpdatedBy(2);
        playerEntry.setDeleted("2024-01-03");
        playerEntry.setDeletedBy(3);
        playerEntry.setPurse(100.0);
        playerEntry.setUuid("test-uuid");
        playerEntry.setGroup(new GroupEntry());
        playerEntry.setAfk(true);
        playerEntry.setFlying(true);
        playerEntry.setName("TestPlayer");
        playerEntry.setCustomName("TestCustomName");
        playerEntry.setHomes(List.of(new LocationEntry()));
        playerEntry.setDeaths(List.of(new LocationEntry()));
        playerEntry.setPartner(new PlayerPartnerEntry());
        playerEntry.setPlayerState(PlayerState.PROTECTION_INFO);
        playerEntry.setPlayerStateParameter("testParameter");
        playerEntry.setHasToBeUpdated(true);
    }

    @Test
    void defaultConstructorInitializesEmptyHomesAndDeaths() {
        PlayerEntry freshPlayerEntry = new PlayerEntry();
        assertNotNull(freshPlayerEntry.getHomes());
        assertNotNull(freshPlayerEntry.getDeaths());
        assertTrue(freshPlayerEntry.getHomes().isEmpty());
        assertTrue(freshPlayerEntry.getDeaths().isEmpty());
    }

    @Test
    void defaultConstructorSetsHasToBeUpdatedToFalse() {
        PlayerEntry freshPlayerEntry = new PlayerEntry();
        assertFalse(freshPlayerEntry.isHasToBeUpdated());
    }

    @Test
    void copyConstructorCopiesAllFields() {
        PlayerEntry copiedPlayerEntry = new PlayerEntry(playerEntry);

        assertAll(
                () -> assertEquals(playerEntry.getId(), copiedPlayerEntry.getId()),
                () -> assertEquals(playerEntry.getCreated(), copiedPlayerEntry.getCreated()),
                () -> assertEquals(playerEntry.getCreatedBy(), copiedPlayerEntry.getCreatedBy()),
                () -> assertEquals(playerEntry.getUpdated(), copiedPlayerEntry.getUpdated()),
                () -> assertEquals(playerEntry.getUpdatedBy(), copiedPlayerEntry.getUpdatedBy()),
                () -> assertEquals(playerEntry.getDeleted(), copiedPlayerEntry.getDeleted()),
                () -> assertEquals(playerEntry.getDeletedBy(), copiedPlayerEntry.getDeletedBy()),
                () -> assertEquals(playerEntry.getPurse(), copiedPlayerEntry.getPurse()),
                () -> assertEquals(playerEntry.getUuid(), copiedPlayerEntry.getUuid()),
                () -> assertEquals(playerEntry.getGroup(), copiedPlayerEntry.getGroup()),
                () -> assertEquals(playerEntry.isAfk(), copiedPlayerEntry.isAfk()),
                () -> assertEquals(playerEntry.isFlying(), copiedPlayerEntry.isFlying()),
                () -> assertEquals(playerEntry.getName(), copiedPlayerEntry.getName()),
                () -> assertEquals(playerEntry.getCustomName(), copiedPlayerEntry.getCustomName()),
                () -> assertEquals(playerEntry.getHomes(), copiedPlayerEntry.getHomes()),
                () -> assertEquals(playerEntry.getDeaths(), copiedPlayerEntry.getDeaths()),
                () -> assertEquals(playerEntry.getPartner(), copiedPlayerEntry.getPartner()),
                () -> assertEquals(playerEntry.getPlayerState(), copiedPlayerEntry.getPlayerState()),
                () -> assertEquals(playerEntry.getPlayerStateParameter(), copiedPlayerEntry.getPlayerStateParameter()),
                () -> assertEquals(playerEntry.isHasToBeUpdated(), copiedPlayerEntry.isHasToBeUpdated())
        );
    }

    @Test
    void copyConstructorCreatesIndependentInstance() {
        PlayerEntry copiedPlayerEntry = new PlayerEntry(playerEntry);
        copiedPlayerEntry.setName("ModifiedPlayer");

        assertNotEquals(playerEntry.getName(), copiedPlayerEntry.getName());
    }
}