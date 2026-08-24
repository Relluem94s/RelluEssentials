/**
 * Author:  rellu
 * Created: 27.07.2026
 */

UPDATE custom_npc
SET entity_uuid   = ?,
    profile_name  = ?,
    inventory     = ?,
    world         = ?,
    x             = ?,
    y             = ?,
    z             = ?,
    yaw           = ?,
    pitch         = ?,
    updatedby     = ?
WHERE id = ?;