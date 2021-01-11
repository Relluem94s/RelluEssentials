/**
 * Author:  rellu
 * Created: 10.01.2021
 */

INSERT INTO `block_history` (`CREATED`, `CREATEDBY`, `location_fk`, `material`)
VALUES (now(), ?, ?, ?);