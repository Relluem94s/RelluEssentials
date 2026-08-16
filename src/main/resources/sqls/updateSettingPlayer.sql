UPDATE rellu_essentials.setting_player
SET UPDATED = now(), UPDATEDBY = ?, value = ?
WHERE ID = ?;