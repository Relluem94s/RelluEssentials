package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.BagMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.BankMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.LocationMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.PlayerMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.SettingMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.TraderNpcMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.WorldGroupSettingMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.WorldMapper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.helpers.IPatchHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.helpers.db.loader.SqlResourceLoader;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupSettingEntry;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * Legacy DatabaseHelper will be removed.
 *
 * @author rellu
 */
@Deprecated
public class DatabaseHelper {

  private final DataSource dataSource;
  private final DataSource dataSourceNoSchema;
  private final SqlResourceLoader sqlResourceLoader;
  private final ServiceContext serviceContext;
  @Setter
  private IPatchHelper patchHelper;

  public DatabaseHelper(DataSource dataSource, DataSource dataSourceNoSchema,
      SqlResourceLoader sqlResourceLoader, ServiceContext serviceContext) {
    this.dataSource = dataSource;
    this.dataSourceNoSchema = dataSourceNoSchema;
    this.sqlResourceLoader = sqlResourceLoader;
    this.serviceContext = serviceContext;
  }

  public void init() {
    patchHelper.applyPatch(getPluginInformation().getDbVersion());
  }

  private <T> List<T> queryList(String sqlFile, StatementConfigurer configurer,
      RowMapper<T> mapper) {
    List<T> results = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          results.add(mapper.map(rs));
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return results;
  }

  private <T> T querySingle(String sqlFile, StatementConfigurer configurer, RowMapper<T> mapper) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return mapper.map(rs);
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return null;
  }

  private <T> T querySingleNoSchema(String sqlFile, StatementConfigurer configurer,
      RowMapper<T> mapper) {
    try (Connection connection = dataSourceNoSchema.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return mapper.map(rs);
        }
      }
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
      throw new RuntimeException(ex);
    }
    return null;
  }

  private void executeUpdate(String sqlFile, StatementConfigurer configurer) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      configurer.configure(ps);
      ps.execute();
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  private void executeUpdateNoSchema(String sqlFile) {
    try (Connection connection = dataSourceNoSchema.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            sqlResourceLoader.load("sqls/" + sqlFile))) {
      ps.execute();
    } catch (SQLException | FileNotFoundException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  void executeScript(String script) {
    executeUpdate(script, _ -> {
    });
  }

  void executeScriptNoSchema(String script) {
    executeUpdateNoSchema(script);
  }

  public PluginInformationEntry getPluginInformation() {
    PluginInformationEntry fallback = new PluginInformationEntry();
    try {
      return querySingleNoSchema("getPluginInformation.sql", _ -> {
      }, MiscMapper::mapPluginInformation);
    } catch (Exception ex) {
      consoleSendMessage(PLUGIN_NAME_CONSOLE, "Init Database..");
      fallback.setDbVersion(-1);
      return fallback;
    }
  }

  public List<SettingEntry> getAllSettings() {
    return queryList("getAllSettings.sql", _ -> {
    }, SettingMapper::mapSetting);
  }

  public List<WorldGroupSettingEntry> getAllWorldGroupSettings() {
    return queryList("getAllWorldGroupSettings.sql",
        ps -> {
        },
        rs -> WorldGroupSettingMapper.mapWorldGroupSetting(rs, serviceContext.getSettingService())
    );
  }

  public List<LocationTypeEntry> getLocationTypes() {
    return queryList("getLocationTypes.sql", _ -> {
    }, LocationMapper::mapLocationType);
  }

  public List<LocationEntry> getWarps() {
    return queryList("getWarps.sql", _ -> {
    }, rs -> LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService()));
  }

  public List<GroupEntry> getGroups() {
    return queryList("getGroups.sql", _ -> {
    }, PlayerMapper::mapGroup);
  }

  public List<TraderNPCEntry> getTraderNPCs() {
    return queryList("getNPCs.sql", _ -> {
    }, rs -> TraderNpcMapper.mapNPC(rs,
        key -> Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(key))));
  }

  public List<BankTierEntry> getBankTiers() {
    return queryList("getBankTiers.sql", _ -> {
    }, BankMapper::mapBankTier);
  }

  public List<BagTypeEntry> getBagTypes() {
    return queryList("getBagTypes.sql", _ -> {
    }, BagMapper::mapBagType);
  }

  public BagTypeEntry getBagType(int type) {
    return querySingle("getBagTypeById.sql", ps -> ps.setInt(1, type), BagMapper::mapBagType);
  }

  public BankTierEntry getBankTier(int id) {
    return querySingle("getBankTier.sql", ps -> ps.setInt(1, id), BankMapper::mapBankTier);
  }

  public List<WorldGroupEntry> getWorldGroups() {
    List<WorldGroupSettingEntry> allWorldGroupSettings = getAllWorldGroupSettings();
    return queryList("getWorldGroups.sql", _ -> {
    }, rs -> WorldMapper.mapWorldGroup(rs, allWorldGroupSettings));
  }

  public List<WorldEntry> getWorldByGroup(@NotNull WorldGroupEntry wge) {
    return queryList("getWorldByGroup.sql", ps -> ps.setInt(1, wge.getId()), rs -> {
      WorldEntry we = WorldMapper.mapWorld(rs);
      we.setWorldGroupEntry(wge);
      return we;
    });
  }

  public void insertWorldGroupInventory(
      @NotNull WorldGroupInventoryEntry worldGroupInventoryEntry) {
    executeUpdate("insertWorldInventoryByGroupAndPlayer.sql", ps -> {
      ps.setInt(1, worldGroupInventoryEntry.getPlayerId());
      ps.setInt(2, worldGroupInventoryEntry.getPlayerId());
      ps.setInt(3, worldGroupInventoryEntry.getWorldGroupEntry().getId());
      ps.setString(4, worldGroupInventoryEntry.getInventory().toString());
      ps.setDouble(5, worldGroupInventoryEntry.getHealth());
      ps.setInt(6, worldGroupInventoryEntry.getFoodLevel());
      ps.setInt(7, worldGroupInventoryEntry.getTotalExperience());
    });
  }

  public void updateWorldGroupInventory(
      @NotNull WorldGroupInventoryEntry worldGroupInventoryEntry) {
    executeUpdate("updateWorldInventoryByGroupAndPlayer.sql", ps -> {
      ps.setInt(1, worldGroupInventoryEntry.getUpdatedBy());
      ps.setString(2, worldGroupInventoryEntry.getInventory().toString());
      ps.setDouble(3, worldGroupInventoryEntry.getHealth());
      ps.setInt(4, worldGroupInventoryEntry.getFoodLevel());
      ps.setInt(5, worldGroupInventoryEntry.getTotalExperience());
      ps.setInt(6, worldGroupInventoryEntry.getPlayerId());
      ps.setInt(7, worldGroupInventoryEntry.getWorldGroupEntry().getId());
    });
  }

  public WorldGroupInventoryEntry getWorldGroupInventory(@NotNull PlayerEntry pe,
      @NotNull WorldGroupEntry wge) {
    return querySingle("getWorldInventoryByGroupAndPlayer.sql", ps -> {
      ps.setInt(1, wge.getId());
      ps.setInt(2, pe.getId());
    }, rs -> {
      WorldGroupInventoryEntry worldGroupInventoryEntry = WorldMapper.mapWorldGroupInventory(rs);
      worldGroupInventoryEntry.setWorldGroupEntry(wge);
      return worldGroupInventoryEntry;
    });
  }

  @SuppressWarnings("unused")
  public void insertWorld(@NotNull WorldEntry we) {
    executeUpdate("insertWorld.sql", ps -> {
      ps.setInt(1, we.getCreatedBy());
      ps.setString(2, we.getName());
      ps.setInt(3, we.getWorldGroupEntry().getId());
      ps.setInt(4, we.getGroupEntry().getId());
    });
  }

  @SuppressWarnings("unused")
  public void insertWorldGroup(@NotNull WorldGroupEntry wge) {
    executeUpdate("insertWorldGroup.sql", ps -> {
      ps.setInt(1, wge.getCreatedBy());
      ps.setString(2, wge.getName());
    });
  }

  @SuppressWarnings("unused")
  public WorldGroupEntry getWorldGroup(String name) {
    return querySingle("getWorldGroupByName.sql", ps -> ps.setString(1, name),
        rs -> WorldMapper.mapWorldGroup(rs, getAllWorldGroupSettings()));
  }

  public BankAccountEntry getPlayerBankAccount(int playerFK) {
    return querySingle("getBankAccountByPlayer.sql", ps -> ps.setInt(1, playerFK), rs -> {
      BankAccountEntry bae = BankMapper.mapBankAccount(rs);
      bae.setTier(getBankTier(rs.getInt(DatabaseMappings.FIELD_BANK_TIER_FK)));
      return bae;
    });
  }

  public void insertBankAccount(@NotNull BankAccountEntry bae) {
    executeUpdate("insertBankAccount.sql", ps -> {
      ps.setInt(1, 1);
      ps.setInt(2, bae.getPlayerId());
      ps.setDouble(3, bae.getValue());
      ps.setInt(4, bae.getTier().getId());
    });
  }

  public void addTransactionToBank(int playerFK, int bankAccountFK, double transactionValue,
      double bankaccountTotal, int tier) {
    executeUpdate("insertBankTransaction.sql", ps -> {
      ps.setInt(1, playerFK);
      ps.setInt(2, bankAccountFK);
      ps.setDouble(3, transactionValue);
    });
    updateBankAccount(playerFK, transactionValue, bankaccountTotal, tier);
  }

  public void updateBankAccount(int playerFK, double transactionValue, double bankaccountTotal,
      int tier) {
    executeUpdate("updateBankAccount.sql", ps -> {
      ps.setInt(1, playerFK);
      ps.setDouble(2, bankaccountTotal + transactionValue);
      ps.setInt(3, tier);
      ps.setInt(4, playerFK);
    });
  }

  public List<BankTransactionEntry> getTransactionsToBankFromPlayer(int bankAccountFK) {
    return queryList("getBankAccountTransactionsByPlayer.sql", ps -> ps.setInt(1, bankAccountFK),
        BankMapper::mapBankTransaction);
  }

  public void insertGroup(@NotNull GroupEntry ge) {
    executeUpdate("insertGroup.sql", ps -> {
      ps.setInt(1, ge.getId());
      ps.setString(2, ge.getName());
      ps.setString(3, ge.getPrefix());
    });
  }

  public BagEntry getBag(int type, int playerFK) {
    return querySingle("getBagByPlayerAndType.sql", ps -> {
      ps.setInt(1, type);
      ps.setInt(2, playerFK);
    }, rs -> {
      BagEntry be = BagMapper.mapBag(rs);
      be.setBagType(getBagType(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)));
      return be;
    });
  }

  public void insertBag(int type, int id) {
    executeUpdate("insertBag.sql", ps -> {
      ps.setInt(1, id);
      ps.setInt(2, id);
      ps.setInt(3, type);
    });
  }

  public List<BagEntry> getBags() {
    return queryList("getBags.sql", _ -> {
    }, rs -> {
      BagEntry be = BagMapper.mapBag(rs);
      be.setBagType(getBagType(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)));
      return be;
    });
  }

  public void updateBagEntry(@NotNull BagEntry be) {
    executeUpdate("updateBag.sql", ps -> {
      ps.setInt(1, be.getPlayerId());
      for (int i = 0; i < BagHelper.BAG_SIZE; i++) {
        ps.setInt(i + 2, be.getSlotValue(i));
      }
      ps.setInt(BagHelper.BAG_SIZE + 2, be.getId());
    });
  }

  @FunctionalInterface
  private interface StatementConfigurer {

    void configure(PreparedStatement ps) throws SQLException;
  }

  @FunctionalInterface
  private interface RowMapper<T> {

    T map(ResultSet rs) throws SQLException;
  }
}