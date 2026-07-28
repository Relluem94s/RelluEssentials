UPDATE custom_npc_dialogue
SET updated = CURRENT_TIMESTAMP,
    updatedby = ?,
    listPosition = ?,
    text = ?
WHERE id = ?
AND deleted IS NULL