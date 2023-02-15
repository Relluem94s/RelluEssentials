/**
 * Author:  rellu
 * Created: 07.02.2023
 */

SELECT * FROM `rellu_essentials`.`bag`
WHERE bag_type_fk = ? 
AND player_fk = ?
AND deletedby IS NULL