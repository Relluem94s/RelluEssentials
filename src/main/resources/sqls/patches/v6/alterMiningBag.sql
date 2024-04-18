/**
 * Author:  rellu
 * Created: 20.07.2023
 */

UPDATE 
    `bag_type` 
SET 
    `slot_15_name` = 'AMETHYST_BLOCK', 
    `slot_16_name` = 'RAW_GOLD_BLOCK', 
    `slot_17_name` = 'RAW_IRON_BLOCK', 
    `slot_18_name` = 'RAW_COPPER_BLOCK', 
    `slot_19_name` = 'ANCIENT_DEBRIS', 
    `slot_22_name` = 'AMETHYST_SHARD', 
    `slot_23_name` = 'GOLD_NUGGET', 
    `slot_24_name` = 'IRON_NUGGET', 
    `slot_25_name` = 'QUARTZ', 
    `slot_26_name` = 'EMERALD'
WHERE
    `bag_type`.`id` = 1; 