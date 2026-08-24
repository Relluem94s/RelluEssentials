SELECT ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, DELETED, DELETEDBY, player_fk, setting_fk, value
FROM rellu_essentials.setting_player
WHERE player_fk = ?
  AND DELETED IS NULL;