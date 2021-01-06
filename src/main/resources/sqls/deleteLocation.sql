/**
 * Author:  rellu
 * Created: 05.01.2021
 */

UPDATE 
    `rellu_essentials`.`location` 
SET 
    `deleted` = now(), 
    `deletedby` = ?, 
WHERE 
    id = ?