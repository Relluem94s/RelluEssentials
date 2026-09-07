package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CUSTOM_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private LocationService locationService;

  @Mock
  private GroupService groupService;

  @Mock
  private PreparedStatement preparedStatement;

  private PlayerDao playerDao;

  @BeforeEach
  void setUp() {
    playerDao = new PlayerDao(queryExecutor, serviceContext);
  }

  @Test
  void findAllReturnsPopulatedPlayerEntries() {
    PlayerEntry playerEntry = buildPlayerEntry();
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();

    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(locationService.findByPlayerAndType(eq(1), eq(LocationType.HOME))).thenReturn(List.of());
    when(locationService.findByPlayerAndType(eq(1), eq(LocationType.DEATH))).thenReturn(List.of());
    when(queryExecutor.querySingle(eq("getPlayerPartner.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(partnerEntry);
    when(queryExecutor.queryList(eq("getPlayers.sql"), any(StatementConfigurer.class), any()))
        .thenAnswer(invocation -> {
          StatementConfigurer configurer = invocation.getArgument(1);
          configurer.configure(preparedStatement);
          RowMapper<PlayerEntry> mapper = invocation.getArgument(2);
          ResultSet resultSet = buildMockedPlayerResultSet(playerEntry);
          return List.of(mapper.map(resultSet));
        });

    List<PlayerEntry> result = playerDao.findAll();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertEquals(1, result.getFirst().getId()),
        () -> assertEquals("test-uuid", result.getFirst().getUuid()),
        () -> assertEquals("TestPlayer", result.getFirst().getName()),
        () -> assertEquals("CustomName", result.getFirst().getCustomName()),
        () -> assertEquals(PlayerState.DEFAULT, result.getFirst().getPlayerState()),
        () -> assertNotNull(result.getFirst().getHomes()),
        () -> assertNotNull(result.getFirst().getDeaths()),
        () -> assertNotNull(result.getFirst().getPartner())
    );
  }

  @Test
  void findAllPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.queryList(eq("getPlayers.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> playerDao.findAll());
  }

  @Test
  void findByUuidReturnsPopulatedPlayerEntry() {
    PlayerEntry playerEntry = buildPlayerEntry();
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();

    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(locationService.findByPlayerAndType(eq(1), eq(LocationType.HOME))).thenReturn(List.of());
    when(locationService.findByPlayerAndType(eq(1), eq(LocationType.DEATH))).thenReturn(List.of());
    when(queryExecutor.querySingle(eq("getPlayerPartner.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(partnerEntry);
    when(queryExecutor.querySingle(eq("getPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenAnswer(invocation -> {
          RowMapper<PlayerEntry> mapper = invocation.getArgument(2);
          ResultSet resultSet = buildMockedPlayerResultSet(playerEntry);
          return mapper.map(resultSet);
        });

    PlayerEntry result = playerDao.findByUuid("test-uuid");

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(1, result.getId()),
        () -> assertEquals("test-uuid", result.getUuid()),
        () -> assertEquals("TestPlayer", result.getName()),
        () -> assertEquals("CustomName", result.getCustomName()),
        () -> assertEquals(PlayerState.DEFAULT, result.getPlayerState()),
        () -> assertNotNull(result.getHomes()),
        () -> assertNotNull(result.getDeaths()),
        () -> assertNotNull(result.getPartner())
    );
  }

  @Test
  void findByUuidSetsUuidOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getPlayer.sql"), captor.capture(), any()))
        .thenReturn(null);

    playerDao.findByUuid("test-uuid");

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setString(1, "test-uuid");
  }

  @Test
  void findByUuidPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.querySingle(eq("getPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> playerDao.findByUuid("test-uuid"));
  }

  @Test
  void insertExecutesUpdateWithCorrectParameters() throws SQLException {
    PlayerEntry playerEntry = buildPlayerEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertPlayer.sql"), captor.capture());

    playerDao.insert(playerEntry);

    captor.getValue().configure(preparedStatement);
    assertAll(
        () -> verify(preparedStatement).setInt(1, playerEntry.getCreatedBy()),
        () -> verify(preparedStatement).setString(2, playerEntry.getUuid()),
        () -> verify(preparedStatement).setString(3, playerEntry.getName()),
        () -> verify(preparedStatement).setString(4, playerEntry.getCustomName()),
        () -> verify(preparedStatement).setInt(5, playerEntry.getGroup().getId())
    );
  }

  @Test
  void insertPropagatesExceptionFromQueryExecutor() {
    PlayerEntry playerEntry = buildPlayerEntry();
    doThrow(new RuntimeException("DB error")).when(queryExecutor)
        .executeUpdate(eq("insertPlayer.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> playerDao.insert(playerEntry));
  }

  @Test
  void updateExecutesUpdateWithCorrectParameters() throws SQLException {
    PlayerEntry playerEntry = buildPlayerEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updatePlayer.sql"), captor.capture());

    playerDao.update(playerEntry);

    captor.getValue().configure(preparedStatement);
    assertAll(
        () -> verify(preparedStatement).setInt(1, playerEntry.getId()),
        () -> verify(preparedStatement).setInt(2, playerEntry.getGroup().getId()),
        () -> verify(preparedStatement).setBoolean(3, playerEntry.isAfk()),
        () -> verify(preparedStatement).setBoolean(4, playerEntry.isFlying()),
        () -> verify(preparedStatement).setString(5, playerEntry.getName()),
        () -> verify(preparedStatement).setString(6, playerEntry.getCustomName()),
        () -> verify(preparedStatement).setDouble(7, playerEntry.getPurse()),
        () -> verify(preparedStatement).setString(8, playerEntry.getUuid())
    );
  }

  @Test
  void updatePropagatesExceptionFromQueryExecutor() {
    PlayerEntry playerEntry = buildPlayerEntry();
    doThrow(new RuntimeException("DB error")).when(queryExecutor)
        .executeUpdate(eq("updatePlayer.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> playerDao.update(playerEntry));
  }

  @Test
  void findPartnerByPlayerIdReturnsPartnerEntry() {
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();
    when(queryExecutor.querySingle(eq("getPlayerPartner.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(partnerEntry);

    PlayerPartnerEntry result = playerDao.findPartnerByPlayerId(1);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(10, result.getId()),
        () -> assertEquals(1, result.getFirstPartnerId()),
        () -> assertEquals(2, result.getSecondPartnerId()),
        () -> assertTrue(result.isShareProtections()),
        () -> assertEquals(1, result.getCreatedBy()),
        () -> assertEquals(1, result.getUpdatedBy()),
        () -> assertEquals(1, result.getDeletedBy())
    );
  }

  @Test
  void findPartnerByPlayerIdSetsBothPlayerIdsOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getPlayerPartner.sql"), captor.capture(), any()))
        .thenReturn(null);

    playerDao.findPartnerByPlayerId(1);

    captor.getValue().configure(preparedStatement);
    assertAll(
        () -> verify(preparedStatement).setInt(1, 1),
        () -> verify(preparedStatement).setInt(2, 1)
    );
  }

  @Test
  void findPartnerByPlayerIdPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.querySingle(eq("getPlayerPartner.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> playerDao.findPartnerByPlayerId(1));
  }

  @Test
  void insertPartnerExecutesUpdateWithCorrectParameters() throws SQLException {
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertPlayerPartner.sql"), captor.capture());

    playerDao.insertPartner(partnerEntry);

    captor.getValue().configure(preparedStatement);
    assertAll(
        () -> verify(preparedStatement).setInt(1, partnerEntry.getCreatedBy()),
        () -> verify(preparedStatement).setInt(2, partnerEntry.getFirstPartnerId()),
        () -> verify(preparedStatement).setInt(3, partnerEntry.getSecondPartnerId()),
        () -> verify(preparedStatement).setBoolean(4, partnerEntry.isShareProtections())
    );
  }

  @Test
  void insertPartnerPropagatesExceptionFromQueryExecutor() {
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();
    doThrow(new RuntimeException("DB error")).when(queryExecutor)
        .executeUpdate(eq("insertPlayerPartner.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> playerDao.insertPartner(partnerEntry));
  }

  @Test
  void deletePartnerExecutesUpdateWithCorrectParameters() throws SQLException {
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("deletePlayerPartner.sql"), captor.capture());

    playerDao.deletePartner(partnerEntry);

    captor.getValue().configure(preparedStatement);
    assertAll(
        () -> verify(preparedStatement).setInt(1, partnerEntry.getDeletedBy()),
        () -> verify(preparedStatement).setInt(2, partnerEntry.getId())
    );
  }

  @Test
  void deletePartnerPropagatesExceptionFromQueryExecutor() {
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();
    doThrow(new RuntimeException("DB error")).when(queryExecutor)
        .executeUpdate(eq("deletePlayerPartner.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> playerDao.deletePartner(partnerEntry));
  }

  @Test
  void updatePartnerExecutesUpdateWithCorrectParameters() throws SQLException {
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updatePlayerPartner.sql"), captor.capture());

    playerDao.updatePartner(partnerEntry);

    captor.getValue().configure(preparedStatement);
    assertAll(
        () -> verify(preparedStatement).setInt(1, partnerEntry.getUpdatedBy()),
        () -> verify(preparedStatement).setBoolean(2, partnerEntry.isShareProtections()),
        () -> verify(preparedStatement).setInt(3, partnerEntry.getId())
    );
  }

  @Test
  void updatePartnerPropagatesExceptionFromQueryExecutor() {
    PlayerPartnerEntry partnerEntry = buildPartnerEntry();
    doThrow(new RuntimeException("DB error")).when(queryExecutor)
        .executeUpdate(eq("updatePlayerPartner.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> playerDao.updatePartner(partnerEntry));
  }

  private ResultSet buildMockedPlayerResultSet(PlayerEntry playerEntry) throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    lenient().when(groupService.findGroupById(anyInt())).thenReturn(
        Optional.ofNullable(playerEntry.getGroup()));
    when(resultSet.getInt(anyString())).thenReturn(playerEntry.getId());
    when(resultSet.getString(anyString())).thenAnswer(invocation -> {
      String column = invocation.getArgument(0);
      return switch (column) {
        case FIELD_UUID -> playerEntry.getUuid();
        case FIELD_NAME -> playerEntry.getName();
        case FIELD_CUSTOM_NAME -> playerEntry.getCustomName();
        default -> null;
      };
    });
    when(resultSet.getBoolean(anyString())).thenReturn(false);
    when(resultSet.getDouble(anyString())).thenReturn(playerEntry.getPurse());
    return resultSet;
  }


  private PlayerEntry buildPlayerEntry() {
    GroupEntry group = new GroupEntry();
    group.setId(1);

    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setId(1);
    playerEntry.setUuid("test-uuid");
    playerEntry.setName("TestPlayer");
    playerEntry.setCustomName("CustomName");
    playerEntry.setAfk(false);
    playerEntry.setFlying(false);
    playerEntry.setPurse(100.0);
    playerEntry.setCreatedBy(1);
    playerEntry.setGroup(group);
    return playerEntry;
  }

  private PlayerPartnerEntry buildPartnerEntry() {
    PlayerPartnerEntry partnerEntry = new PlayerPartnerEntry();
    partnerEntry.setId(10);
    partnerEntry.setFirstPartnerId(1);
    partnerEntry.setSecondPartnerId(2);
    partnerEntry.setShareProtections(true);
    partnerEntry.setCreatedBy(1);
    partnerEntry.setUpdatedBy(1);
    partnerEntry.setDeletedBy(1);
    return partnerEntry;
  }
}