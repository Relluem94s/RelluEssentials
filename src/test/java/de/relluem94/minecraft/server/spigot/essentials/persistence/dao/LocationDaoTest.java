package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_TYPE_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PITCH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PLAYER_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_X;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Y;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Z;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_YAW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationTypeService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private LocationTypeService locationTypeService;

  @Mock
  private PreparedStatement preparedStatement;

  private LocationDao locationDao;

  @BeforeEach
  void setUp() {
    locationDao = new LocationDao(queryExecutor, serviceContext);
  }

  @Test
  void deleteOutdatedLocationsReturnsCountFromQueryExecutor() {
    when(queryExecutor.executeUpdateWithCount(eq("cleanupLocations.sql"), any(StatementConfigurer.class)))
        .thenReturn(5);

    int result = locationDao.deleteOutdatedLocations();

    assertEquals(5, result);
  }

  @Test
  void deleteOutdatedLocationsPropagatesException() {
    when(queryExecutor.executeUpdateWithCount(eq("cleanupLocations.sql"), any(StatementConfigurer.class)))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationDao.deleteOutdatedLocations());
  }

  @Test
  void getLocationReturnsSingleEntryFromQueryExecutor() {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    Location location = buildBukkitLocation();
    LocationEntry expected = new LocationEntry();
    when(queryExecutor.querySingle(eq("getLocationByLocation.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(expected);

    LocationEntry result = locationDao.getLocation(location, 1);

    assertEquals(expected, result);
  }

  @Test
  void getLocationSetsAllParametersOnPreparedStatement() throws SQLException {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    Location location = buildBukkitLocation();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getLocationByLocation.sql"), captor.capture(), any()))
        .thenReturn(null);

    locationDao.getLocation(location, 2);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setFloat(1, (float) location.getX());
    verify(preparedStatement).setFloat(2, (float) location.getY());
    verify(preparedStatement).setFloat(3, (float) location.getZ());
    verify(preparedStatement).setInt(4, 2);
  }

  @Test
  void getLocationMapsAllFieldsFromResultSet() throws SQLException {
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findById(3)).thenReturn(Optional.empty());

    Location location = buildBukkitLocation();

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(10);
      when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(99);
      when(resultSet.getString(FIELD_LOCATION_NAME)).thenReturn("home");
      when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
      when(resultSet.getFloat(FIELD_POS_X)).thenReturn(1.0f);
      when(resultSet.getFloat(FIELD_POS_Y)).thenReturn(64.0f);
      when(resultSet.getFloat(FIELD_POS_Z)).thenReturn(-1.0f);
      when(resultSet.getFloat(FIELD_YAW)).thenReturn(90.0f);
      when(resultSet.getFloat(FIELD_PITCH)).thenReturn(0.0f);
      when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(3);

      when(queryExecutor.querySingle(eq("getLocationByLocation.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            StatementConfigurer configurer = invocation.getArgument(1);
            configurer.configure(preparedStatement);
            RowMapper<LocationEntry> mapper = invocation.getArgument(2);
            return mapper.map(resultSet);
          });

      LocationEntry result = locationDao.getLocation(location, 3);

      assertEquals(10, result.getId());
      assertEquals(99, result.getPlayerId());
      assertEquals("home", result.getLocationName());
      assertEquals("world", result.getWorld());
      assertEquals(1.0f, result.getX());
      assertEquals(64.0f, result.getY());
      assertEquals(-1.0f, result.getZ());
      assertEquals(90.0f, result.getYaw());
      assertEquals(0.0f, result.getPitch());
      assertNull(result.getLocationType());
    }
  }

  @Test
  void getLocationMapsLocationTypeWhenFoundByService() throws SQLException {
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
    locationTypeEntry.setId(3);
    when(locationTypeService.findById(3)).thenReturn(Optional.of(locationTypeEntry));

    Location location = buildBukkitLocation();

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(10);
      when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(99);
      when(resultSet.getString(FIELD_LOCATION_NAME)).thenReturn("home");
      when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
      when(resultSet.getFloat(FIELD_POS_X)).thenReturn(1.0f);
      when(resultSet.getFloat(FIELD_POS_Y)).thenReturn(64.0f);
      when(resultSet.getFloat(FIELD_POS_Z)).thenReturn(-1.0f);
      when(resultSet.getFloat(FIELD_YAW)).thenReturn(90.0f);
      when(resultSet.getFloat(FIELD_PITCH)).thenReturn(0.0f);
      when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(3);

      when(queryExecutor.querySingle(eq("getLocationByLocation.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            StatementConfigurer configurer = invocation.getArgument(1);
            configurer.configure(preparedStatement);
            RowMapper<LocationEntry> mapper = invocation.getArgument(2);
            return mapper.map(resultSet);
          });

      LocationEntry result = locationDao.getLocation(location, 3);

      assertNotNull(result.getLocationType());
      assertEquals(3, result.getLocationType().getId());
    }
  }

  @Test
  void getLocationPropagatesException() {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    Location location = buildBukkitLocation();
    when(queryExecutor.querySingle(eq("getLocationByLocation.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationDao.getLocation(location, 1));
  }

  @Test
  void findByIdReturnsSingleEntryFromQueryExecutor() {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    LocationEntry expected = new LocationEntry();
    when(queryExecutor.querySingle(eq("getLocationById.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(expected);

    LocationEntry result = locationDao.findById(7);

    assertEquals(expected, result);
  }

  @Test
  void findByIdSetsIdOnPreparedStatement() throws SQLException {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getLocationById.sql"), captor.capture(), any()))
        .thenReturn(null);

    locationDao.findById(7);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 7);
  }

  @Test
  void findByIdMapsAllFieldsFromResultSet() throws SQLException {
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findById(2)).thenReturn(Optional.empty());

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(7);
      when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(55);
      when(resultSet.getString(FIELD_LOCATION_NAME)).thenReturn("spawn");
      when(resultSet.getString(FIELD_WORLD)).thenReturn("world_nether");
      when(resultSet.getFloat(FIELD_POS_X)).thenReturn(10.5f);
      when(resultSet.getFloat(FIELD_POS_Y)).thenReturn(70.0f);
      when(resultSet.getFloat(FIELD_POS_Z)).thenReturn(20.5f);
      when(resultSet.getFloat(FIELD_YAW)).thenReturn(45.0f);
      when(resultSet.getFloat(FIELD_PITCH)).thenReturn(10.0f);
      when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(2);

      when(queryExecutor.querySingle(eq("getLocationById.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            StatementConfigurer configurer = invocation.getArgument(1);
            configurer.configure(preparedStatement);
            RowMapper<LocationEntry> mapper = invocation.getArgument(2);
            return mapper.map(resultSet);
          });

      LocationEntry result = locationDao.findById(7);

      assertEquals(7, result.getId());
      assertEquals(55, result.getPlayerId());
      assertEquals("spawn", result.getLocationName());
      assertEquals("world_nether", result.getWorld());
      assertEquals(10.5f, result.getX());
      assertEquals(70.0f, result.getY());
      assertEquals(20.5f, result.getZ());
      assertEquals(45.0f, result.getYaw());
      assertEquals(10.0f, result.getPitch());
      assertNull(result.getLocationType());
    }
  }

  @Test
  void findByIdPropagatesException() {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(queryExecutor.querySingle(eq("getLocationById.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationDao.findById(7));
  }

  @Test
  void insertLocationSetsAllParametersOnPreparedStatement() throws SQLException {
    LocationEntry locationEntry = buildLocationEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertLocation.sql"), captor.capture());

    try (var bukkit = mockStatic(Bukkit.class)) {
      World world = mock(World.class);
      when(world.getName()).thenReturn("world");
      bukkit.when(() -> Bukkit.getWorld("world")).thenReturn(world);

      locationDao.insertLocation(locationEntry);

      captor.getValue().configure(preparedStatement);
    }

    verify(preparedStatement).setInt(1, locationEntry.getPlayerId());
    verify(preparedStatement).setFloat(2, (float) locationEntry.getX());
    verify(preparedStatement).setFloat(3, (float) locationEntry.getY());
    verify(preparedStatement).setFloat(4, (float) locationEntry.getZ());
    verify(preparedStatement).setFloat(5, locationEntry.getYaw());
    verify(preparedStatement).setFloat(6, locationEntry.getPitch());
    verify(preparedStatement).setString(7, "world");
    verify(preparedStatement).setString(8, locationEntry.getLocationName());
    verify(preparedStatement).setInt(9, locationEntry.getLocationType().getId());
    verify(preparedStatement).setInt(10, locationEntry.getPlayerId());
  }


  @Test
  void insertLocationPropagatesException() {
    LocationEntry locationEntry = buildLocationEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertLocation.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> locationDao.insertLocation(locationEntry));
  }

  @Test
  void deleteLocationDelegatesToDeleteById() throws SQLException {
    LocationEntry locationEntry = buildLocationEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("deleteLocation.sql"), captor.capture());

    locationDao.deleteLocation(locationEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, locationEntry.getPlayerId());
    verify(preparedStatement).setInt(2, locationEntry.getId());
  }

  @Test
  void deleteLocationPropagatesException() {
    LocationEntry locationEntry = buildLocationEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("deleteLocation.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> locationDao.deleteLocation(locationEntry));
  }

  @Test
  void deleteByIdSetsPlayerIdAndIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("deleteLocation.sql"), captor.capture());

    locationDao.deleteById(5, 99);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 99);
    verify(preparedStatement).setInt(2, 5);
  }

  @Test
  void deleteByIdPropagatesException() {
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("deleteLocation.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> locationDao.deleteById(5, 99));
  }

  @Test
  void getLocationsReturnsFilteredListByType() throws SQLException {
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findById(any(Integer.class))).thenReturn(Optional.empty());

    try (ResultSet resultSet = mock(ResultSet.class)) {
      try {
        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(99);
        when(resultSet.getString(FIELD_LOCATION_NAME)).thenReturn("home");
        when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
        when(resultSet.getFloat(FIELD_POS_X)).thenReturn(0.0f);
        when(resultSet.getFloat(FIELD_POS_Y)).thenReturn(64.0f);
        when(resultSet.getFloat(FIELD_POS_Z)).thenReturn(0.0f);
        when(resultSet.getFloat(FIELD_YAW)).thenReturn(0.0f);
        when(resultSet.getFloat(FIELD_PITCH)).thenReturn(0.0f);
        when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(1);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      when(queryExecutor.queryList(eq("getLocationsByPlayer.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<LocationEntry> mapper = invocation.getArgument(2);
            try {
              return List.of(mapper.map(resultSet));
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });

      List<LocationEntry> result = locationDao.getLocations(99, 1);

      assertEquals(1, result.size());
      assertEquals(1, result.getFirst().getId());
    }
  }

  @Test
  void getLocationsFiltersOutEntriesWithNonMatchingType() throws SQLException {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);

    try (ResultSet resultSet = mock(ResultSet.class)) {
      try {
        when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(2);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      when(queryExecutor.queryList(eq("getLocationsByPlayer.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<LocationEntry> mapper = invocation.getArgument(2);
            try {
              return Collections.singletonList(mapper.map(resultSet));
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });

      List<LocationEntry> result = locationDao.getLocations(99, 1);

      assertEquals(0, result.size());
    }
  }

  @Test
  void getLocationsSetsPlayerIdOnPreparedStatement() throws SQLException {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getLocationsByPlayer.sql"), captor.capture(), any()))
        .thenReturn(List.of());

    locationDao.getLocations(99, 1);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 99);
  }

  @Test
  void getLocationsPropagatesException() {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(queryExecutor.queryList(eq("getLocationsByPlayer.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationDao.getLocations(99, 1));
  }

  @Test
  void getLocationsByTypeReturnsFilteredListByType() throws SQLException {
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(locationTypeService.findById(any(Integer.class))).thenReturn(Optional.empty());

    try (ResultSet resultSet = mock(ResultSet.class)) {
      try {
        when(resultSet.getInt(FIELD_ID)).thenReturn(2);
        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(10);
        when(resultSet.getString(FIELD_LOCATION_NAME)).thenReturn("warp");
        when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
        when(resultSet.getFloat(FIELD_POS_X)).thenReturn(5.0f);
        when(resultSet.getFloat(FIELD_POS_Y)).thenReturn(70.0f);
        when(resultSet.getFloat(FIELD_POS_Z)).thenReturn(5.0f);
        when(resultSet.getFloat(FIELD_YAW)).thenReturn(0.0f);
        when(resultSet.getFloat(FIELD_PITCH)).thenReturn(0.0f);
        when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(4);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      when(queryExecutor.queryList(eq("getLocationsByType.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<LocationEntry> mapper = invocation.getArgument(2);
            try {
              return List.of(mapper.map(resultSet));
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });

      List<LocationEntry> result = locationDao.getLocationsByType(4);

      assertEquals(1, result.size());
      assertEquals(2, result.getFirst().getId());
    }
  }

  @Test
  void getLocationsByTypeFiltersOutEntriesWithNonMatchingType() throws SQLException {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);

    try (ResultSet resultSet = mock(ResultSet.class)) {
      try {
        when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(9);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      when(queryExecutor.queryList(eq("getLocationsByType.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<LocationEntry> mapper = invocation.getArgument(2);
            try {
              return Collections.singletonList(mapper.map(resultSet));
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });

      List<LocationEntry> result = locationDao.getLocationsByType(4);

      assertEquals(0, result.size());
    }
  }

  @Test
  void getLocationsByTypeSetsTypeOnPreparedStatement() throws SQLException {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getLocationsByType.sql"), captor.capture(), any()))
        .thenReturn(List.of());

    locationDao.getLocationsByType(4);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 4);
  }

  @Test
  void getLocationsByTypePropagatesException() {
    lenient().when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(queryExecutor.queryList(eq("getLocationsByType.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> locationDao.getLocationsByType(4));
  }

  @Test
  void deleteOutdatedLocationsExecutesConfigurerWithoutSettingParameters() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.executeUpdateWithCount(eq("cleanupLocations.sql"), captor.capture()))
        .thenReturn(0);

    locationDao.deleteOutdatedLocations();

    captor.getValue().configure(preparedStatement);
  }

  private Location buildBukkitLocation() {
    World world = mock(World.class);
    lenient().when(world.getName()).thenReturn("world");
    return new Location(world, 1.0, 64.0, -1.0, 90.0f, 0.0f);
  }

  private LocationEntry buildLocationEntry() {
    LocationTypeEntry locationType = new LocationTypeEntry();
    locationType.setId(1);

    World world = mock(World.class);
    lenient().when(world.getName()).thenReturn("world");

    LocationEntry entry = new LocationEntry();
    entry.setId(10);
    entry.setPlayerId(99);
    entry.setLocationName("home");
    entry.setWorld("world");
    entry.setX(1.0);
    entry.setY(64.0);
    entry.setZ(-1.0);
    entry.setYaw(90.0f);
    entry.setPitch(0.0f);
    entry.setLocationType(locationType);
    return entry;
  }
}