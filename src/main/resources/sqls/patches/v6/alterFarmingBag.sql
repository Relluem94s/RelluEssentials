/**
 * Author:  rellu
 * Created: 20.07.2023
 */

UPDATE 
    `bag_type` 
SET 
    `slot_15_name` = 'RED_MUSHROOM',
    `slot_16_name` = 'CRIMSON_FUNGUS',
    `slot_17_name` = 'CHORUS_FRUIT',
    `slot_18_name` = 'NETHER_WART',
    `slot_19_name` = 'GLOW_BERRIES',
    `slot_20_name` = 'POISONOUS_POTATO', 
    `slot_22_name` = 'BROWN_MUSHROOM',
    `slot_23_name` = 'WARPED_FUNGUS' 
WHERE 
    `bag_type`.`id` = 2; 