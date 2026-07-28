UPDATE custom_npc_dialogue
SET deleted = CURRENT_TIMESTAMP,
    deletedby = ?
WHERE id = ?
AND deleted IS NULL