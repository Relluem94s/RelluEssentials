/**
 * Author:  rellu
 * Created: 31.01.2023
 */

UPDATE 
    `rellu_essentials`.`protections` 
SET 
    `updated` = now(), 
    `updatedby` = ?,
    `flags` = ?
WHERE 
    `protections`.`id` = ?