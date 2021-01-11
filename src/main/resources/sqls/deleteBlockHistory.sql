/**
 * Author:  rellu
 * Created: 10.01.2021
 */

UPDATE `block_history` 
SET 
    `DELETED` = now(), 
    `DELETEDBY` = ? 
WHERE 
    `block_history`.`location_fk` = ?
    AND DELETEDBY IS NULL;
