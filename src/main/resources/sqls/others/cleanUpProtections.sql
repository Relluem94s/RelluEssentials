/**
 * Author:  rellu
 * Created: 12.02.2023
 */

DELETE FROM 
    `rellu_essentials`.`protections` 
WHERE 
    `protections`.`deletedby` IS NOT NULL;
