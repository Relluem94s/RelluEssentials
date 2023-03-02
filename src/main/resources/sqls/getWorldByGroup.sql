/**
 * Author:  rellu
 * Created: 19.02.2023
 */

SELECT * FROM `rellu_essentials`.`world` w

WHERE w.deletedby IS NULL AND w.world_group_fk = ?;