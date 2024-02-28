/**
 * Author:  rellu
 * Created: 27.02.2024
 */

UPDATE 
    `bag` 
SET 
    `slot_18_value` = `slot_18_value` + `slot_14_value`,
    `slot_14_value` = 0
WHERE 
    `bag`.`bag_type_fk` = 2; 