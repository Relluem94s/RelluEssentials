/**
 * Author:  rellu
 * Created: 02.10.2021
 */

UPDATE `plugin_informations` SET `deleted` = now(), `deletedby` = 1 WHERE `plugin_informations`.`DELETEDBY` IS NULL and `db_version` <> 3 ;