UPDATE rellu_essentials.setting_player
SET DELETED = now(), DELETEDBY = ?
WHERE ID = ?;