/**
 * Author:  rellu
 * Created: 15.07.2026
 */

SELECT
    ID,
    CREATED,
    CREATEDBY,
    UPDATED,
    UPDATEDBY,
    setting_fk,
    world_group_fk,
    value
FROM
    `world_group_setting`
WHERE
    DELETEDBY IS NULL