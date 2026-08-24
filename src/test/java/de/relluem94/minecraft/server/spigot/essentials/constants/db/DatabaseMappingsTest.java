package de.relluem94.minecraft.server.spigot.essentials.constants.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseMappingsTest {

  @Test
  public void constructorThrowsIllegalStateException() throws Exception {
    Constructor<DatabaseMappings> constructor = DatabaseMappings.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    InvocationTargetException thrown = Assertions.assertThrows(
        InvocationTargetException.class,
        () -> constructor.newInstance()
    );

    Assertions.assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  public void fieldIdHasCorrectValue() {
    Assertions.assertEquals("id", DatabaseMappings.FIELD_ID);
  }

  @Test
  public void fieldCreatedHasCorrectValue() {
    Assertions.assertEquals("created", DatabaseMappings.FIELD_CREATED);
  }

  @Test
  public void fieldCreatedByHasCorrectValue() {
    Assertions.assertEquals("createdby", DatabaseMappings.FIELD_CREATEDBY);
  }

  @Test
  public void fieldUpdatedHasCorrectValue() {
    Assertions.assertEquals("updated", DatabaseMappings.FIELD_UPDATED);
  }

  @Test
  public void fieldUpdatedByHasCorrectValue() {
    Assertions.assertEquals("updatedby", DatabaseMappings.FIELD_UPDATEDBY);
  }

  @Test
  public void fieldDeletedHasCorrectValue() {
    Assertions.assertEquals("deleted", DatabaseMappings.FIELD_DELETED);
  }

  @Test
  public void fieldDeletedByHasCorrectValue() {
    Assertions.assertEquals("deletedby", DatabaseMappings.FIELD_DELETEDBY);
  }

  @Test
  public void fieldBagTypeFkHasCorrectValue() {
    Assertions.assertEquals("bag_type_fk", DatabaseMappings.FIELD_BAG_TYPE_FK);
  }

  @Test
  public void fieldPlayerFkHasCorrectValue() {
    Assertions.assertEquals("player_fk", DatabaseMappings.FIELD_PLAYER_FK);
  }

  @Test
  public void fieldSlotVarValueHasCorrectFormatPattern() {
    Assertions.assertEquals("slot_%s_value", DatabaseMappings.FIELD_SLOT_VAR_VALUE);
  }

  @Test
  public void fieldSlotVarValueCanBeFormattedWithSlotNumber() {
    String formatted = String.format(DatabaseMappings.FIELD_SLOT_VAR_VALUE, "3");
    Assertions.assertEquals("slot_3_value", formatted);
  }

  @Test
  public void fieldNameHasCorrectValue() {
    Assertions.assertEquals("name", DatabaseMappings.FIELD_NAME);
  }

  @Test
  public void fieldDisplayNameHasCorrectValue() {
    Assertions.assertEquals("displayname", DatabaseMappings.FIELD_DISPLAY_NAME);
  }

  @Test
  public void fieldCostHasCorrectValue() {
    Assertions.assertEquals("cost", DatabaseMappings.FIELD_COST);
  }

  @Test
  public void fieldSlotVarNameHasCorrectFormatPattern() {
    Assertions.assertEquals("slot_%s_name", DatabaseMappings.FIELD_SLOT_VAR_NAME);
  }

  @Test
  public void fieldSlotVarNameCanBeFormattedWithSlotNumber() {
    String formatted = String.format(DatabaseMappings.FIELD_SLOT_VAR_NAME, "5");
    Assertions.assertEquals("slot_5_name", formatted);
  }

  @Test
  public void fieldValueHasCorrectValue() {
    Assertions.assertEquals("value", DatabaseMappings.FIELD_VALUE);
  }

  @Test
  public void fieldBankTierFkHasCorrectValue() {
    Assertions.assertEquals("bank_tier_fk", DatabaseMappings.FIELD_BANK_TIER_FK);
  }

  @Test
  public void fieldLimitHasCorrectValue() {
    Assertions.assertEquals("limit", DatabaseMappings.FIELD_LIMIT);
  }

  @Test
  public void fieldInterestHasCorrectValue() {
    Assertions.assertEquals("interest", DatabaseMappings.FIELD_INTEREST);
  }

  @Test
  public void fieldBankAccountFkHasCorrectValue() {
    Assertions.assertEquals("bank_account_fk", DatabaseMappings.FIELD_BANK_ACCOUNT_FK);
  }

  @Test
  public void fieldMaterialHasCorrectValue() {
    Assertions.assertEquals("material", DatabaseMappings.FIELD_MATERIAL);
  }

  @Test
  public void fieldPrefixHasCorrectValue() {
    Assertions.assertEquals("prefix", DatabaseMappings.FIELD_PREFIX);
  }

  @Test
  public void fieldLocationFkHasCorrectValue() {
    Assertions.assertEquals("location_fk", DatabaseMappings.FIELD_LOCATION_FK);
  }

  @Test
  public void fieldLocationNameHasCorrectValue() {
    Assertions.assertEquals("location_name", DatabaseMappings.FIELD_LOCATION_NAME);
  }

  @Test
  public void fieldWorldHasCorrectValue() {
    Assertions.assertEquals("world", DatabaseMappings.FIELD_WORLD);
  }

  @Test
  public void fieldPosXHasCorrectValue() {
    Assertions.assertEquals("x", DatabaseMappings.FIELD_POS_X);
  }

  @Test
  public void fieldPosYHasCorrectValue() {
    Assertions.assertEquals("y", DatabaseMappings.FIELD_POS_Y);
  }

  @Test
  public void fieldPosZHasCorrectValue() {
    Assertions.assertEquals("z", DatabaseMappings.FIELD_POS_Z);
  }

  @Test
  public void fieldYawHasCorrectValue() {
    Assertions.assertEquals("yaw", DatabaseMappings.FIELD_YAW);
  }

  @Test
  public void fieldPitchHasCorrectValue() {
    Assertions.assertEquals("pitch", DatabaseMappings.FIELD_PITCH);
  }

  @Test
  public void fieldLocationTypeHasCorrectValue() {
    Assertions.assertEquals("location_type", DatabaseMappings.FIELD_LOCATION_TYPE);
  }

  @Test
  public void fieldLocationTypeFkHasCorrectValue() {
    Assertions.assertEquals("location_type_fk", DatabaseMappings.FIELD_LOCATION_TYPE_FK);
  }

  @Test
  public void fieldProfessionHasCorrectValue() {
    Assertions.assertEquals("profession", DatabaseMappings.FIELD_PROFESSION);
  }

  @Test
  public void fieldTypeHasCorrectValue() {
    Assertions.assertEquals("type", DatabaseMappings.FIELD_TYPE);
  }

  @Test
  public void fieldCustomNameHasCorrectValue() {
    Assertions.assertEquals("customname", DatabaseMappings.FIELD_CUSTOM_NAME);
  }

  @Test
  public void fieldPurseHasCorrectValue() {
    Assertions.assertEquals("purse", DatabaseMappings.FIELD_PURSE);
  }

  @Test
  public void fieldFlyHasCorrectValue() {
    Assertions.assertEquals("fly", DatabaseMappings.FIELD_FLY);
  }

  @Test
  public void fieldAfkHasCorrectValue() {
    Assertions.assertEquals("afk", DatabaseMappings.FIELD_AFK);
  }

  @Test
  public void fieldGroupFkHasCorrectValue() {
    Assertions.assertEquals("group_fk", DatabaseMappings.FIELD_GROUP_FK);
  }

  @Test
  public void fieldUuidHasCorrectValue() {
    Assertions.assertEquals("uuid", DatabaseMappings.FIELD_UUID);
  }

  @Test
  public void fieldEntityUuidHasCorrectValue() {
    Assertions.assertEquals("entity_uuid", DatabaseMappings.FIELD_ENTITY_UUID);
  }

  @Test
  public void fieldFirstPartnerFkHasCorrectValue() {
    Assertions.assertEquals("first_partner_fk", DatabaseMappings.FIELD_FIRST_PARTNER_FK);
  }

  @Test
  public void fieldSecondPartnerFkHasCorrectValue() {
    Assertions.assertEquals("second_partner_fk", DatabaseMappings.FIELD_SECOND_PARTNER_FK);
  }

  @Test
  public void fieldShareProtectionsHasCorrectValue() {
    Assertions.assertEquals("shareProtections", DatabaseMappings.FIELD_SHARE_PROTECTIONS);
  }

  @Test
  public void fieldTabHeaderHasCorrectValue() {
    Assertions.assertEquals("tab_header", DatabaseMappings.FIELD_TAB_HEADER);
  }

  @Test
  public void fieldTabFooterHasCorrectValue() {
    Assertions.assertEquals("tab_footer", DatabaseMappings.FIELD_TAB_FOOTER);
  }

  @Test
  public void fieldMotdMessageHasCorrectValue() {
    Assertions.assertEquals("motd_message", DatabaseMappings.FIELD_MOTD_MESSAGE);
  }

  @Test
  public void fieldMotdPlayersHasCorrectValue() {
    Assertions.assertEquals("motd_players", DatabaseMappings.FIELD_MOTD_PLAYERS);
  }

  @Test
  public void fieldDbVersionHasCorrectValue() {
    Assertions.assertEquals("db_version", DatabaseMappings.FIELD_DB_VERSION);
  }

  @Test
  public void fieldFlagsHasCorrectValue() {
    Assertions.assertEquals("flags", DatabaseMappings.FIELD_FLAGS);
  }

  @Test
  public void fieldRightsHasCorrectValue() {
    Assertions.assertEquals("rights", DatabaseMappings.FIELD_RIGHTS);
  }

  @Test
  public void fieldMaterialNameHasCorrectValue() {
    Assertions.assertEquals("material_name", DatabaseMappings.FIELD_MATERIAL_NAME);
  }

  @Test
  public void fieldHealthHasCorrectValue() {
    Assertions.assertEquals("health", DatabaseMappings.FIELD_HEALTH);
  }

  @Test
  public void fieldTotalExperienceHasCorrectValue() {
    Assertions.assertEquals("totalExperience", DatabaseMappings.FIELD_TOTAL_EXPERIENCE);
  }

  @Test
  public void fieldFoodHasCorrectValue() {
    Assertions.assertEquals("food", DatabaseMappings.FIELD_FOOD);
  }

  @Test
  public void fieldInventoryHasCorrectValue() {
    Assertions.assertEquals("inventory", DatabaseMappings.FIELD_INVENTORY);
  }

  @Test
  public void fieldPlantHasCorrectValue() {
    Assertions.assertEquals("plant", DatabaseMappings.FIELD_PLANT);
  }

  @Test
  public void fieldSeedHasCorrectValue() {
    Assertions.assertEquals("seed", DatabaseMappings.FIELD_SEED);
  }

  @Test
  public void fieldMinIntHasCorrectValue() {
    Assertions.assertEquals("min_int", DatabaseMappings.FIELD_MIN_INT);
  }

  @Test
  public void fieldMaxIntHasCorrectValue() {
    Assertions.assertEquals("max_int", DatabaseMappings.FIELD_MAX_INT);
  }

  @Test
  public void fieldSettingFkHasCorrectValue() {
    Assertions.assertEquals("setting_fk", DatabaseMappings.FIELD_SETTING_FK);
  }

  @Test
  public void fieldWorldGroupFkHasCorrectValue() {
    Assertions.assertEquals("world_group_fk", DatabaseMappings.FIELD_WORLD_GORUP_FK);
  }

  @Test
  public void fieldProfileNameHasCorrectValue() {
    Assertions.assertEquals("profile_name", DatabaseMappings.FIELD_PROFILE_NAME);
  }

  @Test
  public void fieldListPositionHasCorrectValue() {
    Assertions.assertEquals("listPosition", DatabaseMappings.FIELD_LIST_POSITION);
  }

  @Test
  public void fieldTextHasCorrectValue() {
    Assertions.assertEquals("text", DatabaseMappings.FIELD_TEXT);
  }

  @Test
  public void fieldCustomNpcFkHasCorrectValue() {
    Assertions.assertEquals("custom_npc_fk", DatabaseMappings.FIELD_CUSTOM_NPC_FK);
  }
}