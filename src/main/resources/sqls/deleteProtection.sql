/**
 * Author:  rellu
 * Created: 29.01.2023
 */

UPDATE 
    `rellu_essentials`.`protections` 
SET 
    `deleted` = now(), 
    `deletedby` = ?
WHERE 
    `protections`.`id` = ?