/**
 * Author:  rellu
 * Created: 27.07.2026
 */

UPDATE custom_npc
SET entity_uuid  = ?,
    profile_name = ?,
    world        = ?,
    x            = ?,
    y            = ?,
    z            = ?,
    updatedby   = ?
WHERE id = ?
  AND deleted IS NULL;