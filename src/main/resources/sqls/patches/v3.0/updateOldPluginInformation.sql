/**
 * Author:  rellu
 * Created: 15.01.2023
 */

UPDATE `plugin_informations` SET `deleted` = now(), `deletedby` = 1 WHERE `plugin_informations`.`DELETEDBY` IS NULL and `db_version` <> 4 ;