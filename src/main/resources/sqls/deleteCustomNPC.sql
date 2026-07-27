/**
 * Author:  rellu
 * Created: 27.07.2026
 */

UPDATE custom_npc
SET deleted    = CURRENT_TIMESTAMP,
    deletedby = ?
WHERE uuid = ?
  AND deleted IS NULL;