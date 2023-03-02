/**
 * Author:  rellu
 * Created: 19.02.2023
 */

SELECT * FROM `rellu_essentials`.`world_group_inventory` wgi

WHERE wgi.deletedby IS NULL 
    AND wgi.world_group_fk = ? 
    AND wgi.player_fk = ?;