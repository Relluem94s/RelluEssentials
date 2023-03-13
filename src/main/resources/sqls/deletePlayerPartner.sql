/**
 * Author:  rellu
 * Created: 13.03.2023
 */

UPDATE 
    `rellu_essentials`.`player_partner` 
SET 
    `deleted` = now(), 
    `deletedby` = ?
WHERE 
    `player_partner`.`id` = ?;
