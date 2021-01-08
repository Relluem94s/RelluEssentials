/**
 * Author:  rellu
 * Created: 05.01.2021
 */

UPDATE 
    `rellu_essentials`.`plugin_informations` 
SET 
    `deleted` = now(), 
    `deletedby` = ?, 
WHERE 
    `deletedby` is null;