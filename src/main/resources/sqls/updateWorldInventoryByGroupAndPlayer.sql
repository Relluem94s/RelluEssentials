/**
 * Author:  rellu
 * Created: 19.02.2023
 */

UPDATE 
    `rellu_essentials`.`world_group_inventory`
SET 
    `updated` = now(), 
    `updatedby` = ?,
    `inventory` = ?,
    `health` = ?,
    `food` = ?,
    `totalExperience` = ?
WHERE 
    `player_fk` = ? AND
    `world_group_fk` = ? AND
    `deletedby` is null