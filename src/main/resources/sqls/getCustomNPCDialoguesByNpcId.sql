SELECT * FROM custom_npc_dialogue
WHERE custom_npc_fk = ?
AND deleted IS NULL
ORDER BY listPosition ASC