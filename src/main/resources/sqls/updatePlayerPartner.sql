/**
 * Author:  rellu
 * Created: 13.03.2023
 */

UPDATE 
    `rellu_essentials`.`player_partner` 
SET 
    `updated` = now(), 
    `updatedby` = ?, 
    `shareProtections` = ?
WHERE 
    `player_partner`.`id` = ?