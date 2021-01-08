/**
 * Author:  rellu
 * Created: 05.01.2021
 */

UPDATE 
    `rellu_essentials`.`player` 
SET 
    `updated` = now(), 
    `updatedby` = ?, 
    `group_fk` = ?, 
    `afk` = ?, 
    `fly` = ?, 
    `customname` = ? 
WHERE 
    `player`.`uuid` = ?