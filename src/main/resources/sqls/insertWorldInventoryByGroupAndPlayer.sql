/**
 * Author:  rellu
 * Created: 19.02.2023
 */

INSERT INTO `rellu_essentials`.`world_group_inventory` 
(`created`, `createdby`, `player_fk`, `world_group_fk`, `inventory`, `health`, `food`, `exp`, `expToLevel`, `totalExperience`, `level`) 
VALUES (now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);