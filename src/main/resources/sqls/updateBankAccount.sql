/**
 * Author:  rellu
 * Created: 22.01.2023
 */

UPDATE 
    `rellu_essentials`.`bank_account` 
SET 
    `updated` = now(), 
    `updatedby` = ?, 
    `value` = ?, 
    `tier` = ?, 
WHERE 
    `bank_account`.`player_fk` = ?