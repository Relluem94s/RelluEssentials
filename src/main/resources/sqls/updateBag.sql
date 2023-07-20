/**
 * Author:  rellu
 * Created: 07.02.2023
 */

UPDATE `rellu_essentials`.`bag` 
SET 
`updated` = now(), 
`updatedby` = ?, 
`slot_1_value` = ?, 
`slot_2_value` = ?, 
`slot_3_value` = ?, 
`slot_4_value` = ?, 
`slot_5_value` = ?, 
`slot_6_value` = ?,
`slot_7_value` = ?, 
`slot_8_value` = ?, 
`slot_9_value` = ?, 
`slot_10_value` = ?,
`slot_11_value` = ?,
`slot_12_value` = ?,
`slot_13_value` = ?,
`slot_14_value` = ?, 
`slot_15_value` = ?, 
`slot_16_value` = ?, 
`slot_17_value` = ?, 
`slot_18_value` = ?, 
`slot_19_value` = ?,
`slot_20_value` = ?, 
`slot_21_value` = ?, 
`slot_22_value` = ?, 
`slot_23_value` = ?,
`slot_24_value` = ?,
`slot_25_value` = ?,
`slot_26_value` = ?,
`slot_27_value` = ?,
`slot_28_value` = ?   

WHERE `bag`.`id` = ?; 