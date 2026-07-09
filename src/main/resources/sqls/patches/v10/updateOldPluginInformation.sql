/**
 * Author:  rellu
 * Created: 09.07.2026
 */

UPDATE `plugin_informations` SET `deleted` = now(), `deletedby` = 1 WHERE `plugin_informations`.`DELETEDBY` IS NULL and `db_version` <> 10 ;