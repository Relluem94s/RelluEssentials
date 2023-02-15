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
`slot_14_value` = ? 

WHERE `bag`.`id` = ?; 