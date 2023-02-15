/**
 * Author:  rellu
 * Created: 12.02.2023
 */

DELETE FROM 
    `rellu_essentials`.`location` 
WHERE 
    `location`.`location_type_fk` in(1,2,3,5)
    AND `location`.`deletedby` IS NOT NULL;
