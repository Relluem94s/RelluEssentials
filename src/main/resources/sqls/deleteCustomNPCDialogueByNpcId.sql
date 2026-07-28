UPDATE custom_npc_dialogue
SET deleted = CURRENT_TIMESTAMP,
    deletedby = ?
WHERE custom_npc_fk = ?
AND deleted IS NULL