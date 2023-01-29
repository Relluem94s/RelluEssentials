/**
 * Author:  rellu
 * Created: 29.01.2023
 */

INSERT INTO `protections` (`created`, `createdby`, `player_fk`, `location_fk`, `material_name`, `flags`, `rights`) 
VALUES (now(), ?, ?, ?, ?, ?, ?)