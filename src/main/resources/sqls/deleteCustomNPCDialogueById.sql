UPDATE custom_npc_dialogue cnd
JOIN custom_npc cn ON cn.id = cnd.custom_npc_fk
SET cnd.deleted = CURRENT_TIMESTAMP,
    cnd.deletedby = ?
WHERE cn.uuid = ?
AND cnd.listPosition = ?
AND cnd.deleted IS NULL