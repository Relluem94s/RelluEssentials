/**
 * Author:  rellu
 * Created: 19.02.2023
 */

UPDATE `plugin_informations` SET `deleted` = now(), `deletedby` = 1 WHERE `plugin_informations`.`DELETEDBY` IS NULL and `db_version` <> 9 ;