UPDATE custom_npc_dialogue cnd
JOIN custom_npc cn ON cn.id = cnd.custom_npc_fk
SET cnd.updated = CURRENT_TIMESTAMP,
    cnd.updatedby = ?,
    cnd.text = ?
WHERE cn.uuid = ?
AND cnd.listPosition = ?
AND cnd.deleted IS NULL