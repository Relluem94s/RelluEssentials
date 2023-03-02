/**
 * Author:  rellu
 * Created: 19.02.2023
 */

INSERT INTO `rellu_essentials`.`world` 
(`created`, `createdby`, `name`, `world_group_fk`, `build_group_fk`) 
VALUES (now(), ?, ?, ?, ?);